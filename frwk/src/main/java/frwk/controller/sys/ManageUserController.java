package frwk.controller.sys;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.util.Formater;
import common.util.RandomPassWord;
import common.util.ResourceException;
import entity.CatAgencyStructure;
import entity.Partner;
import entity.PartnerBranch;
import entity.frwk.Company;
import entity.frwk.SysDictParam;
import entity.frwk.SysObjects;
import entity.frwk.SysParam;
import entity.frwk.SysRoles;
import entity.frwk.SysSecurity;
import entity.frwk.SysUsers;
import frwk.constants.RightConstants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.CatAgencyStructureDao;
import frwk.dao.hibernate.sys.CompanyDao;
import frwk.dao.hibernate.sys.PartnerBranchDao;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.dao.hibernate.sys.RoleDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysObjectDao;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.dao.hibernate.sys.SysSecurityDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.dao.hibernate.sys.UserDao;
import frwk.form.ManageUserForm;
import frwk.utils.ApplicationContext;
import frwk.utils.ExportExcel;
import frwk.utils.Utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Controller
@RequestMapping("/manageUser")
public class ManageUserController extends CatalogController<ManageUserForm, SysUsers> {

	private static Logger lg = LogManager.getLogger(ManageUserController.class);

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private SysPartnerDao sysPartnerDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private SysUsersDao sysUsersDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private ExportExcel exportExcel;

	@Autowired
	private SysObjectDao sysObjectDao;
	
	@Autowired
	private CatAgencyStructureDao catAgencyStructureDao;
	
//	@Autowired
//	private JavaMailSender mailSender;

	ObjectMapper mapper = new ObjectMapper();
	private Boolean isRSA = Boolean.FALSE;
	private ArrayList<SysRoles> dsNhomQuyenCha;
	private String comIdFilter;

	protected String getReportTitle() {
		return null;
	}

	protected String getTemplateFileName() {
		return null;
	}

	public void changePIN(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, ManageUserForm form)
			throws IOException {
		rp.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = rp.getWriter();
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		if (!RightUtils.haveRight(RightConstants.MNRSA, appContext)) {
			pw.print(getText("not_access"));
			pw.flush();
			pw.close();
			return;
		}
		// String oldPIN = "";
		String PIN = rq.getParameter("PIN");
		String username = rq.getParameter("username");
		String OTP = rq.getParameter("OTP");
		String result = "";

		pw.print(result);
		pw.flush();
		pw.close();
	}

	public String unsubscriber(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, ManageUserForm form)
			throws IOException {
		rp.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = rp.getWriter();
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		if (!RightUtils.haveRight(RightConstants.MNRSA, appContext)) {
			pw.print(getText("not_access"));
			pw.flush();
			pw.close();
			return null;
		}

		pw.print("SUCCESS");

		pw.flush();
		pw.close();
		return null;
	}

	private static final BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

	public void resetPass(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, ManageUserForm form)
			throws Exception {
//		if (!RightUtils.haveRight(RightConstants.MNUSER_RESET_PASS,
//				(ApplicationContext) rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT)))
//			throw new ResourceException("not_access");		
		SysUsers su = form.getModel();
		String newPass = new RandomPassWord(8, 20).nextString();
		sysUsersDao.resetPassWord(su, encode.encode(newPass));
		su.setPassWordPlainText(newPass);
		if (!Formater.isNull(su.getEmail())) {
			SimpleMailMessage email = new SimpleMailMessage();
			email.setTo(su.getEmail());
			email.setSubject("Thong Bao Mat Khau");
			email.setText("user/password:" + su.getUsername() + "/" + newPass);
			// sends the e-mail
			try {
				//mailSender.send(email);
			} catch (Exception e) {
				lg.error("Khong gui duoc email");
			}
		}

		rp.setContentType("application/json;charset=utf-8");
		rp.setHeader("Cache-Control", "no-store");
		PrintWriter out = rp.getWriter();
		String jsonObject = mapper.writeValueAsString(su);
		out.print(jsonObject);
		out.flush();
		out.close();

	}

	@Override
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form)
			throws Exception {
		String id = rq.getParameter("id");
		SysUsers user = sysUsersDao.getUser(id);
		rs.setContentType("application/json;charset=utf-8");
		rs.setHeader("Cache-Control", "no-store");
		PrintWriter out = rs.getWriter();
		user.setpWExpriredDateStr(expiredPassWordDate(user.getPwdDate()));
//		if (!Formater.isNull(user.getCompanyId()))
//			user.setLstDepartment(companyDao.getChild(user.getCompanyId()));
		String jsonObject = mapper.writeValueAsString(user);
		out.print(jsonObject);
		out.flush();
		out.close();
	}

	@Autowired
	MessageSource messageSource;

	@Autowired
	private SysSecurityDao sysSecurityDao;

	private String expiredPassWordDate(Date pwDate) {
		SysSecurity expriredDays = sysSecurityDao.findByCodeAndActive("PW_EXPIRED_IN", true);
		Date date = new Date();
		if (expriredDays != null) {
			Calendar c = Calendar.getInstance();
			c.setTime(pwDate);
			c.add(Calendar.DATE, Integer.parseInt(expriredDays.getValue()));
			long dateTime = 60 * 24 * 60 * 1000;
			String dateStr = "";
			long diff = 0;
			if (c.getTime().after(date)) {
				diff = c.getTimeInMillis() - date.getTime();
				dateStr = String.format(messageSource.getMessage("PW_WR", null, "Default", null), diff / dateTime);
				return dateStr;
			} else {
				return messageSource.getMessage("PW_EX", null, "Default", null);
			}
		}
		return null;
	}

	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form)
			throws Exception {

		if (!RightUtils.haveRight(RightConstants.MNUSER_Delete,
				(ApplicationContext) rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT)))
			throw new ResourceException("not_access");
		// super.del(model, rq, rs, form);
		ManageUserForm userForm = (ManageUserForm) form;
		SysUsers sysUsers = userForm.getSu();
		userDao.del(sysUsers);
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form)
			throws Exception {
		SysUsers su = ((ManageUserForm) form).getModel();
//		if (editor.getCompany() != null && !"CIC".equals(editor.getCompany().getCode())) {
//			if (editor.getCompany() != null && editor.getCompany().getId() != null
//					&& !editor.getCompany().getId().equals(su.getCompanyId()))
//				throw new ResourceException("KhÃƒÂ´ng sÃ¡Â»Â­a dÃ¡Â»Â¯ liÃ¡Â»â€¡u Ã„â€˜Ã¡Â»â€˜i tÃƒÂ¡c khÃƒÂ¡c!");
//
//		}
		// validate max user in partner
		if (!Formater.isNull(su.getCompanyId()) && !validateMaxUser(su.getCompanyId())) {
			throw new ResourceException(
					"S\u1ED1 l\u01B0\u1EE3ng user v\u01B0\u1EE3t qu\u00E1 ng\u01B0\u1EE1ng kh\u00F4ng \u0111\u01B0\u1EE3c t\u1EA1o user v\u1EDBi t\u1ED5 ch\u1EE9c t\u00EDn d\u1EE5ng n\u00E0y");

		}
		String passWordPlainText = new RandomPassWord(8, 12).nextString();
		if (Formater.isNull(su.getId())) { // Khong duoc sua password, ngay hieu luc password

			// Kiem tra quyen tao moi
//			if (!RightUtils.haveRight(RightConstants.MNUSER_Create,
//					(ApplicationContext) rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT)))
//				throw new ResourceException("not_access");

			// TODO: sua

			su.setPassWordPlainText(passWordPlainText);
//			String passWordInDb = userDao.getHash(su.getUsername(), /* + Constants.APPLICATION_KEY */su.getPassWordPlainText());
			// new encode
			String passWordInDb = encode.encode(su.getPassWordPlainText());
			su.setPassword(passWordInDb);
			su.setPwdDate(Calendar.getInstance().getTime());
			su.setActive(Boolean.TRUE);
			if (!Formater.isNull(su.getEmail())) {
				SimpleMailMessage email = new SimpleMailMessage();
				email.setTo(su.getEmail());
				email.setSubject("Thong Bao Mat Khau");
				email.setText("user/password:" + su.getUsername() + "/" + passWordPlainText);
				// sends the e-mail
				try {
					//mailSender.send(email);
				} catch (Exception e) {
					lg.error("Khong gui duoc email", e);
				}

			}
		} else {
			// Kiem tra quyen sua
//			if (!RightUtils.haveRight(RightConstants.MNUSER_Edit,
//					(ApplicationContext) rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT)))
//				throw new ResourceException("not_access");

		}
		su.setCompanyId(partner.getId());
		// Gui mail thong bao
		try {
			// TODO: phai sua
			userDao.save(su);

		} catch (ConstraintViolationException constraintViolationException) {
			lg.error(constraintViolationException);
			throw new ResourceException("D\u1EEF li\u1EC7u \u0111\u00E3 t\u1ED3n t\u1EA1i");
		}
		Utils.jsonSerialize(rs, su);
	}

	public void setDsNhomQuyenCha(ArrayList<SysRoles> dsNhomQuyenCha) {
		this.dsNhomQuyenCha = dsNhomQuyenCha;
	}

	public ArrayList<SysRoles> getDsNhomQuyenCha() {
		return dsNhomQuyenCha;
	}

	public void setIsRSA(Boolean isRSA) {
		this.isRSA = isRSA;
	}

	public Boolean getIsRSA() {
		return isRSA;
	}

	public String changeBranch(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form)
			throws IOException, ResourceException, Exception {
		String newBrId = rq.getParameter("newBrId");
		rs.setContentType("text/text;charset=utf-8");
		rs.setHeader("cache-control", "no-cache");
		PrintWriter out = rs.getWriter();
		StringBuffer lstResult = new StringBuffer();
		String ttct = "";

		List<Company> dsChiNhanh = companyDao.getChild(newBrId);
		if (dsChiNhanh == null)
			return null;
		for (Company pr : dsChiNhanh) {
			ttct = ttct + "<option value='" + pr.getId() + "' >" + pr.getCode() + "-" + pr.getName() + "</option>";
		}
		lstResult.append("<option value=''></option>" + ttct);

		out.println(lstResult.toString());
		out.flush();
		out.close();
		return null;
	}

	public String checkMaxUser(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form)
			throws IOException, ResourceException, Exception {
		String partnerId = rq.getParameter("partnerId");
		if (Formater.isNull(partnerId))
			partnerId = form.getModel().getCompanyId();
		rs.setContentType("text/text;charset=utf-8");
		rs.setHeader("cache-control", "no-cache");
		PrintWriter out = rs.getWriter();
		if (!validateMaxUser(partnerId)) {
			form.setValidateMaxUser("0");
			out.println(
					"S\u1ED1 l\u01B0\u1EE3ng user v\u01B0\u1EE3t qu\u00E1 ng\u01B0\u1EE1ng kh\u00F4ng \u0111\u01B0\u1EE3c t\u1EA1o user v\u1EDBi t\u1ED5 ch\u1EE9c t\u00EDn d\u1EE5ng n\u00E0y");
		} else {
			form.setValidateMaxUser("1");
			out.println("");
		}
		out.flush();
		out.close();
		return null;
	}

	private boolean validateMaxUser(String partnerId) {
		Partner partner = null;
		if (!Formater.isNull(partnerId)) {
			partner = sysPartnerDao.getObject(Partner.class, partnerId);
		}
		List<SysUsers> users = new ArrayList<SysUsers>();
		int maxUser = 0;
		if (partner != null && partner.getMaxUser() != null) {
			maxUser = partner.getMaxUser().intValue();
			users = sysUsersDao.getSysUserByPartnerId(partnerId);
		}
		if (maxUser != 0 && users.size() >= maxUser) {
			return false;
		} else {
			return true;
		}
	}

	public void setComIdFilter(String comIdFilter) {
		this.comIdFilter = comIdFilter;
	}

	public String getComIdFilter() {
		return comIdFilter;
	}

	@Override
	public BaseDao<SysUsers> getDao() {
		return userDao;
	}

	@Override
	public BaseDao<SysUsers> createSearchDAO(HttpServletRequest request, ManageUserForm form) throws Exception {
		UserDao dao = new UserDao();
		dao.addRestriction(Restrictions.eq("companyId", partner.getId()));
		// Name
		ManageUserForm userForm = (ManageUserForm) form;
		if (!Formater.isNull(userForm.getStrname()))
			dao.addRestriction(
					Restrictions.like("name", userForm.getStrname().trim(), MatchMode.ANYWHERE).ignoreCase());

		if (!Formater.isNull(userForm.getJob_name()))
			dao.addRestriction(Restrictions.eq("departmentId", userForm.getJob_name()));
		// username
		if (!Formater.isNull(userForm.getStrusername()))
			dao.addRestriction(
					Restrictions.like("username", userForm.getStrusername().trim(), MatchMode.ANYWHERE).ignoreCase());

		dao.addOrder(Order.asc("username"));
		return dao;
	}

	@Override
	public void pushToJa(JSONArray ja, SysUsers temp, ManageUserForm modal) throws Exception {

		if (!Formater.isNull(temp.getUsername()))
			if (!Formater.isNull(temp.getUsername()))
				ja.put("<a class='characterwrap' href = '#' onclick = 'editUser(\"" + temp.getId() + "\")'>"
						+ temp.getUsername() + "</a>");
			else
				ja.put("");
		else
			ja.put("");

		ja.put(temp.getName());
		ja.put(temp.getJobName());

		if (!Formater.isNull(temp.getCompanyId())) {
			Partner partner = sysPartnerDao.getObject(temp.getCompany());
			ja.put(partner.getCode() + "-" + partner.getName());

		}

		else
			ja.put("");

		if (!Formater.isNull(temp.getCellPhone()))
			ja.put("<span class='characterwrap'>" + temp.getCellPhone() + "</span>");
		else
			ja.put("");
//		ja.put(temp.getIdentification());
//		ja.put(temp.getIdentification());

	}

	@Autowired
	private Partner partner;

	@Override
	public String getJsp() {
		return "qtht/quan_ly_nguoi_dung";
	}

	@Autowired
	private PartnerBranchDao partnerBranchDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form)
			throws Exception {
		List<PartnerBranch> dsDonVi = partnerBranchDao.getBranch(partner);
		List<SysDictParam> dsGender = sysDictParamDao.getByType(RightConstants.CAT_TYPE_GENDER_1);
		List<CatAgencyStructure> dsCatAgencyStructure = catAgencyStructureDao.getAll(CatAgencyStructure.class);
		model.addAttribute("dsDonVi", dsDonVi);
		model.addAttribute("dsGender", dsGender);
		model.addAttribute("dsCatAgencyStructure", dsCatAgencyStructure);
	}

	@Autowired
	private SysDictParamDao sysDictParamDao;

	public void treeRole(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form)
			throws Exception {
		rs.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = rs.getWriter();
		JSONObject result = new JSONObject();
		JSONArray array = new JSONArray();
		SysUsers su = null;
		SysUsers su2 = null;
		if (!Formater.isNull(rq.getParameter("suID"))) {
			su = sysUsersDao.getWithRole(rq.getParameter("suID"));
		}
		Boolean bNotPublicRole = Boolean.TRUE;
		array = roleDao.getTreeRolesData(bNotPublicRole, rq.getParameter("parentRoleId"), su);
		result.put("treeRoles", array);
		pw.print(result);
		pw.flush();
		pw.close();
	}

	public void ExportFileExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form)
			throws Exception {
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		SysUsers sysUser = sysUsersDao.getWithRole(id);
		Partner partner = sysPartnerDao.getObject(Partner.class, sysUser.getCompany().getId());
		sysUser.setCompany(partner);
		// get role
		List<SysObjects> objects = sysObjectDao.getUserRight(sysUser);
		sysUser.setLstObj(objects);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("services", sysUser.getCompany().getPartnerServices());
		map.put("sysUser", sysUser);
		exportExcel.export("Thong_tin_nguoi_dung", rs, map);
	}

	@SuppressWarnings("unused")
	public void exportExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ManageUserForm form) {
		try {
			String usernameRq = rq.getParameter("username");
			String fullnameRq = rq.getParameter("fullname");
			String partnerId = rq.getParameter("partner");
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Thong_Tin_Nguoi_Dung");
			int rowNum = 0;
			// search info
			Row title = sheet.createRow(rowNum++);
			Cell titleCell = title.createCell(0);
			titleCell.setCellValue("1. Thông tin tìm kiếm");
			Row usernameRow = sheet.createRow(rowNum++);
			Cell usernameCell = usernameRow.createCell(1);
			usernameCell.setCellValue("Username : ");
			Cell username = usernameRow.createCell(2);
			username.setCellValue(usernameRq);
			Row fullnameRow = sheet.createRow(rowNum++);
			Cell fullnameCell = fullnameRow.createCell(1);
			fullnameCell.setCellValue("Fullname : ");
			Cell fullname = fullnameRow.createCell(2);
			fullname.setCellValue(fullnameRq);
			Row partnerRow = sheet.createRow(rowNum++);
			Cell partnerCell = partnerRow.createCell(1);
			partnerCell.setCellValue("Partner : ");
			if (!Formater.isNull(rq.getParameter("partner"))) {
				Cell parter = partnerRow.createCell(2);
				parter.setCellValue(sysPartnerDao.getObject(Partner.class, partnerId).getName());
			}
			// end search info
			// find customer and fill it to excel
			Row rowBank = sheet.createRow(rowNum++);
			Row rowTitileResult = sheet.createRow(rowNum++);
			Cell cellResult = rowTitileResult.createCell(0);
			sheet.setColumnWidth(0, 25 * 256);
			// create cell before
			List<SysObjects> lstRole = sysObjectDao.getAll(SysObjects.class);
			Map<String, Integer> mapCell = new HashMap<String, Integer>();
			Row rowCell = sheet.createRow(rowNum++);
			Cell cellUser = rowCell.createCell(1);
			sheet.setColumnWidth(1, 25 * 256);
			for (int i = 2; i < lstRole.size(); i++) {
				sheet.setColumnWidth(i, 25 * 256);
				Cell cell = rowCell.createCell(i);
				cell.setCellValue(lstRole.get(i).getName());
				mapCell.put(lstRole.get(i).getObjectId(), i);
			}

			Map<Integer, SysUsers> mapRow = new HashMap<Integer, SysUsers>();
			List<SysUsers> users = sysUsersDao.getSysUsers(usernameRq, fullnameRq, partnerId);
			for (SysUsers item : users) {
				List<SysObjects> lstRoleUser = sysObjectDao.getUserRight(item);
				item.setLstObj(lstRoleUser);
				int row = rowNum++;
				Row rowIndex = sheet.createRow(row);
				Cell cell = rowIndex.createCell(1);
				cell.setCellValue(item.getUsername());
				mapRow.put(row, item);
			}
			int i = 7;
			SysUsers mapRoleUser = null;
			for (int j = 0; j < users.size(); j++) {
				mapRoleUser = mapRow.get(i);
				List<SysObjects> objects = mapRoleUser.getLstObj();
				Row row = sheet.getRow(i);
				for (SysObjects object : objects) {
					Integer cellIndex = mapCell.get(object.getObjectId());
					if (cellIndex == null)
						continue;
					Cell cell = row.createCell(cellIndex);
					cell.setCellValue("1");
				}
				i++;
			}
			// end

			rs.setContentType("application/vnd.ms-excel");
			rs.setHeader("Content-Disposition", "attachment;filename=danh_sach_nguoi_dung.xlsx");
			ServletOutputStream outputStream = rs.getOutputStream();
			workbook.write(outputStream);
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			lg.error(e);
		} catch (IOException e) {
			lg.error(e);
		} catch (Exception e) {
			lg.error(e);
		}

	}
	
	public void getTree(ModelMap model, HttpServletRequest request, HttpServletResponse response, ManageUserForm form)
			throws Exception {
		String parentId = request.getParameter("parentId");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		JSONArray array = null;
		array = catAgencyStructureDao.getTreeRoot(parentId, false);
		pw.print(array);
		pw.close();
	}
}
