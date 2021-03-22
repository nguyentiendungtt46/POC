package cic.h2h.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import cic.h2h.dao.hibernate.CatProductDao;
import cic.h2h.dao.hibernate.H2HBaseDao;
import cic.h2h.dao.hibernate.PartnerDao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import cic.h2h.dao.hibernate.ServiceProductDao;
import cic.h2h.form.PartnerForm;
import cic.utils.FTPUtils;
import cic.utils.QnAFtpContext;
import common.util.Formater;
import common.util.LocaleUtils;
import common.util.ResourceException;
import entity.CatProduct;
import entity.Partner;
import entity.PartnerConnectIp;
import entity.PartnerService;
import entity.Province;
import entity.ServiceInfo;
import entity.ServiceProduct;
import entity.frwk.SysDictParam;
import entity.frwk.SysDictType;
import entity.frwk.SysUsers;
import frwk.constants.Constants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysDictTypeDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/partner")
public class PartnerController extends CatalogController<PartnerForm, Partner> {

	private static Logger lg = LogManager.getLogger(PartnerController.class);

	@Autowired
	private PartnerDao partnerDao;

	@Autowired
	private ServiceInfoDao serviceInfoDao;

	@Autowired
	private SysUsersDao sysUsersDao;

	@Autowired
	private ServiceProductDao serviceProductDao;

	@Autowired
	MessageSource messageSource;

	@Autowired
	private QnAFtpContext qnaFtpContext;

	@Autowired
	SysDictParamDao sysDictParamDao;

	@Autowired
	SysDictTypeDao sysDictTypeDao;

	@Override
	public String getJsp() {

		return "dv_thanhvien/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PartnerForm form)
			throws Exception {
		SysDictType typeProvince = sysDictTypeDao.getSysDictTypeByCode("01");
		List<SysDictParam> lstProvince = sysDictParamDao.sysDictParams("", "", typeProvince.getId());
		model.addAttribute("lstProvince", lstProvince);
		model.addAttribute("serviceInfos", serviceInfoDao.getAllGWService());
		model.addAttribute("lstSysUser", null);
	}

	@Override
	public BaseDao<Partner> createSearchDAO(HttpServletRequest request, PartnerForm form) throws Exception {

		PartnerDao dao = new PartnerDao();
		PartnerForm partnerForm = (PartnerForm) form;

		// Search by hieu luc hop dong
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		if (!Formater.isNull(partnerForm.getFormDate())) {
			dao.addRestriction(Restrictions.ge("validityContractFrom", df.parse(partnerForm.getFormDate())));
		}
		if (!Formater.isNull(partnerForm.getToDate())) {
			Date currentDate = df.parse(partnerForm.getToDate());
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE, 1);
			Date dateAdd = c.getTime();
			dao.addRestriction(Restrictions.le("validityContractFrom", dateAdd));
		}
		dao.addRestriction((Restrictions.or(Restrictions.isNull("foreignBank"), Restrictions.eq("foreignBank", 0))));
		if (!Formater.isNull(partnerForm.getKeyword_code()))
			dao.addRestriction(
					Restrictions.like("code", partnerForm.getKeyword_code().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(partnerForm.getKeyword_name()))
			dao.addRestriction(
					Restrictions.like("name", partnerForm.getKeyword_name().trim(), MatchMode.ANYWHERE).ignoreCase());
		dao.addOrder(Order.asc("name"));
		return dao;
	}

	@Override
	public void pushToJa(JSONArray ja, Partner temp, PartnerForm modal) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + temp.getId() + "\")'>" + temp.getCode()
				+ "</a>");
		ja.put(temp.getName());
		ja.put(temp.getAddress());

		if (temp.getValidityContractFrom() != null) {
			ja.put(Formater.date2str(temp.getValidityContractFrom()));
		} else {
			ja.put("");
		}

		if (temp.getValidityContractTo() != null) {
			ja.put(Formater.date2str(temp.getValidityContractTo()));
		} else {
			ja.put("");
		}

		ja.put("<a href = '#' onclick = 'window.open(\"partnerBranch?partnerId=" + temp.getId()
				+ "\").focus()'>Chi nh&#225;nh TCTD</a>");

	}

	public void getProvince(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("partnerForm") PartnerForm form) throws Exception {
		// lay thong tin tinh/tp
		// ServicePck servicePcks = servicePckDAO.getById(serviceGroupId);
		List<Province> listProvince = new H2HBaseDao<Province>().getAll(Province.class);
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray(new ObjectMapper().writeValueAsString(listProvince));
		out.print(jsonArray);
		out.flush();
		out.close();
	}

	public void getServiceInfo(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("partnerForm") PartnerForm form) throws Exception {
		// lay thong tin tinh/tp
		// ServicePck servicePcks = servicePckDAO.getById(serviceGroupId);
		List<ServiceInfo> serviceInfos = serviceInfoDao.getAll();
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray(new ObjectMapper().writeValueAsString(serviceInfos));
		out.print(jsonArray);
		out.flush();
		out.close();
	}

	//
	public void getProductByService(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("partnerForm") PartnerForm form) throws Exception {
		String serviceId = request.getParameter("serviceId");
		List<ServiceProduct> list = serviceProductDao.getListbyServiceId(serviceId);
		List<CatProduct> catProducts = new ArrayList<CatProduct>();
		for (ServiceProduct item : list) {
			if (item == null)
				continue;
			if (item.getCatProductId() == null)
				continue;
			CatProduct catProduct = new CatProduct();
			catProduct.setId(item.getCatProductId().getId());
			catProduct.setCode(item.getCatProductId().getCode());
			catProduct.setName(item.getCatProductId().getName());
			catProducts.add(catProduct);
		}
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray(new ObjectMapper().writeValueAsString(catProducts));
		out.print(jsonArray);
		out.flush();
		out.close();
	}

	public void buildAllServiceProduct(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("partnerForm") PartnerForm form) throws IOException {
		String id = request.getParameter("partnerId");
		Partner partner = new Partner();
		Partner partner2 = new Partner();
		Map<String, PartnerService> map = new HashMap<String, PartnerService>();
		if (!Formater.isNull(id)) {
			partner2 = partnerDao.getObject(Partner.class, id);
			if (!Formater.isNull(partner2.getPartnerServices())) {
				for (PartnerService partnerService : partner2.getPartnerServices()) {
					String key = "";
					if (partnerService == null)
						continue;
					if (partnerService.getServiceInfoId() != null)
						key += partnerService.getServiceInfoId().getId();
					if (partnerService.getCatProductId() != null)
						key += partnerService.getCatProductId().getId();

					if (!Formater.isNull(key))
						map.put(key, partnerService);
				}
			}
		}
		partner.setId(id);
		partner.setPartnerServicesArrayList(new ArrayList<PartnerService>());
		List<ServiceInfo> infos = serviceInfoDao.getAllGWService();
		for (ServiceInfo serviceInfo : infos) {
			if (serviceInfo == null)
				continue;
			List<ServiceProduct> list = serviceProductDao.getListbyServiceId(serviceInfo.getId());
			if (Formater.isNull(list)) {
				PartnerService partnerService = new PartnerService();
				partnerService.setServiceInfoId(serviceInfo);
				partnerService.setPeriod(1L);
				partnerService.setRate(1L);
				partner.getPartnerServicesArrayList().add(partnerService);
			} else {
				for (ServiceProduct product : list) {
					PartnerService partnerService = new PartnerService();
					CatProduct catProduct = new CatProduct();
					partnerService.setServiceInfoId(serviceInfo);
					partnerService.setPeriod(1L);
					partnerService.setRate(1L);
					if (product == null)
						continue;
					if (product.getCatProductId() != null) {
						catProduct.setId(product.getCatProductId().getId());
						catProduct.setCode(product.getCatProductId().getCode());
						catProduct.setName(product.getCatProductId().getName());
						partnerService.setCatProductId(catProduct);
					}
					PartnerService checkExist = map.get(serviceInfo.getId() + catProduct.getId());
					if (checkExist != null) {
						partnerService.setId(checkExist.getId());
					}
					partner.getPartnerServicesArrayList().add(partnerService);
				}
			}
		}
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		JSONObject jsonArray = new JSONObject(new ObjectMapper().writeValueAsString(partner));
		out.print(jsonArray);
		out.flush();
		out.close();
	}

	public void getUserByPartnerId(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("partnerForm") PartnerForm form) throws Exception {
		String PartnerId = request.getParameter("partnerId");
		List<SysUsers> sysUsers = sysUsersDao.getSysUserByPartnerId(PartnerId);
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray(new ObjectMapper().writeValueAsString(sysUsers));
		out.print(jsonArray);
		out.flush();
		out.close();
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PartnerForm form) throws Exception {
		lg.info("BEGIN save Partner");
		try {
			PartnerForm partnerForm = (PartnerForm) form;
			Partner obj = new Partner();
			obj = partnerForm.getPartner();

			// check use phone
			if (!Formater.isNull(obj.getPhone())) {
				List<String> lstPhoneCheck = getLstPhone(obj.getPhone(), obj.getId());

				for (String p : lstPhoneCheck) {
					if (partnerDao.checkUsePhone(p)) {// check phone da duoc su dung
						throw new ResourceException(
								"S&#272;T &#273;ang &#273;&#432;&#7907;c s&#7917; d&#7909;ng: " + p);
					} else if (!checkFormatPhone(p) || p.length() != 10) {
						throw new ResourceException(
								"S&#272;T kh&#244;ng &#273;&#250;ng &#273;&#7883;nh d&#7841;ng: " + p);
					}
				}
			}

			obj.setModifyTime(new Date());
			obj.setIntcde("99901");
			obj.setForeignBank(Integer.valueOf("0"));
			if (Formater.isNull(obj.getId())) {
				if (partnerDao.getPartnerByCode(obj.getCode()) != null) {
					throw new ResourceException("\u0110\u1ED1i t\u00E1c " + obj.getCode() + " "
							+ messageSource.getMessage(Constants.DUPLICATE_CODE, null, "Default", null));
				}
			} else {
				Partner oldPartner = partnerDao.getObject(Partner.class, obj.getId());
				if (!oldPartner.getCode().equals(obj.getCode()) && partnerDao.getPartnerByCode(obj.getCode()) != null) {
					throw new ResourceException(
							obj.getCode() + messageSource.getMessage(Constants.DUPLICATE_CODE, null, "Default", null));
				}
			}

			if (!Formater.isNull(obj.getPartnerConnectIpArrayList())) {
				for (PartnerConnectIp item : obj.getPartnerConnectIpArrayList()) {
					if (Formater.isNull(item.getId())) {
						item.setId(null);
						item.setPartnerId(obj);
					}
				}
				obj.getPartnerConnectIps().addAll(obj.getPartnerConnectIpArrayList());
			}

			if (!Formater.isNull(obj.getPartnerServicesArrayList())) {
				for (int i = 0; i < obj.getPartnerServicesArrayList().size() - 1; i++) {
					for (int j = i + 1; j < obj.getPartnerServicesArrayList().size(); j++) {
						if (obj.getPartnerServicesArrayList().get(i).getServiceInfoId().getId()
								.equals(obj.getPartnerServicesArrayList().get(j).getServiceInfoId().getId()))
							if (obj.getPartnerServicesArrayList().get(j).getCatProductId() == null
									&& obj.getPartnerServicesArrayList().get(i).getCatProductId() == null)
								throw new ResourceException(String.format(
										"Tr&#249;ng d&#7919; li&#7879;u Service v&#7899;i s&#7843;n ph&#7849;m d&#242;ng %s v&#224;%s",
										new Object[] { i + 1, j + 1 }));
							else if (obj.getPartnerServicesArrayList().get(i).getCatProductId().getId()
									.equals(obj.getPartnerServicesArrayList().get(j).getCatProductId().getId())) {
								throw new ResourceException(String.format(
										"Tr&#249;ng d&#7919; li&#7879;u Service v&#7899;i s&#7843;n ph&#7849;m d&#242;ng %s v&#224; %s",
										new Object[] { i + 1, j + 1 }));

							}
					}
				}
//				List<PartnerService> lstDup = new ArrayList<PartnerService>();
				for (PartnerService service : obj.getPartnerServicesArrayList()) {
//					if (!lstDup.stream().filter(o -> o.getServiceInfoId().getId().equals(service.getServiceInfoId().getId()) && ((o.getCatProductId() == null && service.getCatProductId() == null) || o.getCatProductId().getId().equals(service.getCatProductId().getId())) )
//							.findFirst()
//							.isPresent()) {
//						lstDup.add(service);
//					}
					if (service == null)
						continue;
					if (Formater.isNull(service.getId())) {
						service.setId(null);
						service.setPartnerId(obj);
					}
					if (service.getCatProductId() == null || Formater.isNull(service.getCatProductId().getId())) {
						service.setCatProductId(null);
					}
				}

//				if(lstDup.size() != obj.getPartnerServicesArrayList().size()) {
//					throw new ResourceException("D&#7919; li&#7879;u Serivce v&#224; S&#7843;n ph&#7849;m b&#7883; tr&#249;ng l&#7863;p");
//				}

				obj.getPartnerServices().addAll(obj.getPartnerServicesArrayList());
			}
			partnerDao.save(partnerForm.getModel());
			lg.info("end save Partner");
		} catch (ConstraintViolationException ex) {
			lg.error(ex);
			throw new ResourceException("Du lieu trung");

		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PartnerForm form) throws Exception {
		if (!Formater.isNull(form.getPartner().getPartnerServicesArrayList())) {
			for (PartnerService service : form.getPartner().getPartnerServicesArrayList()) {
				if (!Formater.isNull(service.getId()))
					throw new ResourceException(
							"\u0110\u1ED1i t\u00E1c \u0111\u00E3 c\u1EA5u h\u00ECnh service, kh\u00F4ng th\u1EC3 x\u00F3a");
			}
		}

		super.del(model, rq, rs, form);
	}

	@Override
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PartnerForm form) throws Exception {
		String id = rq.getParameter("id");
		Partner o = partnerDao.getObject(Partner.class, id);
		for (PartnerConnectIp a : o.getPartnerConnectIps()) {
			if (o.getPartnerConnectIpArrayList() == null)
				o.setPartnerConnectIpArrayList(new ArrayList<PartnerConnectIp>());
			o.getPartnerConnectIpArrayList().add(a);
		}
		for (PartnerService service : o.getPartnerServices()) {
			if (Formater.isNull(o.getPartnerServicesArrayList()))
				o.setPartnerServicesArrayList(new ArrayList<PartnerService>());
			o.getPartnerServicesArrayList().add(service);
		}
		if (o.getPartnerServicesArrayList() != null) {
			Collections.sort(o.getPartnerServicesArrayList(), new Comparator<PartnerService>() {
				@Override
				public int compare(final PartnerService before, final PartnerService afer) {
					try {
						// So sanh theo ten service
						if (before.getServiceInfoId() == null
								|| Formater.isNull(before.getServiceInfoId().getServiceName())) {
							if (afer.getServiceInfoId() != null
									&& !Formater.isNull(afer.getServiceInfoId().getServiceName()))
								return -1;
							else
								return 0;
						}
						if (afer.getServiceInfoId() == null
								|| Formater.isNull(afer.getServiceInfoId().getServiceName()))
							return 1;
						if (LocaleUtils.generator(before.getServiceInfoId().getServiceName())
								.compareTo(LocaleUtils.generator(afer.getServiceInfoId().getServiceName())) != 0)
							return LocaleUtils.generator(before.getServiceInfoId().getServiceName())
									.compareTo(LocaleUtils.generator(afer.getServiceInfoId().getServiceName()));

						// So sanh theo ten san pham
						if (before.getCatProductId() == null || Formater.isNull(before.getCatProductId().getName())) {
							if (afer.getCatProductId() != null && !Formater.isNull(afer.getCatProductId().getName()))
								return -1;
							else
								return 0;
						}
						if (afer.getCatProductId() == null || Formater.isNull(afer.getCatProductId().getName()))
							return 1;

						return LocaleUtils.generator(before.getCatProductId().getName())
								.compareTo(LocaleUtils.generator(afer.getCatProductId().getName()));

					} catch (Exception ex) {
						return 0;
					}
				}
			});
		}
		for (SysUsers user : o.getSysUsers()) {
			if (Formater.isNull(o.getSysUsersArrayList()))
				o.setSysUsersArrayList(new ArrayList<SysUsers>());
			o.getSysUsersArrayList().add(user);
		}
		returnObject(rs, o);
	}

	@Autowired
	private ExportExcel exportExcel;

	public void exportFileExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PartnerForm form) {
		try {
			String code = rq.getParameter("code");
			String name = rq.getParameter("name");
			List<Partner> partners = partnerDao.getPanters(code, name);
			List<Province> listProvince = new H2HBaseDao<Province>().getAll(Province.class);
			Map<String, Province> provinceMap = new HashMap<String, Province>();
			for (Province province : listProvince) {
				provinceMap.put(province.getCode(), province);
			}
			Map<String, Object> map = new HashMap<String, Object>();
			for (Partner partner : partners) {
				if (partner == null)
					continue;
				if (Formater.isNull(partner.getProvincecode()))
					continue;
				Province province = provinceMap.get(partner.getProvincecode());
				if (province != null) {
					partner.setProvincecode(province.getName());
				}
			}
			map.put("partners", partners);
			map.put("code", code);
			map.put("nameSearch", name);

			exportExcel.export("Danh_sach_doi_tac_he_thong", rs, map);
		} catch (Exception e) {
			lg.error(e);
		}
	}

	@Override
	public BaseDao<Partner> getDao() {
		return partnerDao;
	}

	public void pingIp(ModelMap model, HttpServletRequest rq, HttpServletResponse rp, PartnerForm form)
			throws IOException {
		PrintWriter out = rp.getWriter();
		try {
			String ipAddress = Formater.isNull(rq.getParameter("ipAddress")) ? "" : rq.getParameter("ipAddress");
			InetAddress inet = InetAddress.getByName(ipAddress);

			if (inet.isReachable(5000)) {
				out.println("IP:" + ipAddress + " | K&#7871;t n&#7889;i th&#224;nh c&#244;ng.");

			} else {
				out.println("IP:" + ipAddress + " | K&#7871;t n&#7889;i kh&#244;ng th&#224;nh c&#244;ng.");
			}
		} catch (Exception e) {
			lg.error(e);
		}

		out.flush();
		out.close();
	}

	private List<String> splitByChart(String obj, String chart) {
		String lst[] = obj.split(chart);
		return new ArrayList<>(Arrays.asList(lst));
	}

	private boolean checkFormatPhone(String phone) {
		// mobile viettel: 086, 096, 097, 098, 032, 033, 034, 035, 036, 037, 038, 039
		// mobile Vinaphone: 091, 094, 088, 083, 084, 085, 081, 082
		// mobile Mobiphone: 089, 090, 093, 070, 079, 077, 076, 078
		// mobile Vietnammobile: 092, 056, 058
		// mobile Gmobile: 099, 059
		// dien thoai co dinh:
		// 296,254,209,204,291,222,275,256,274,271,252,290,292,206,236,262,261,215,251,277,269,226,024,239,220,225,293,
		// 028,221,258,297,260,213,263,205,214,272,228,238,259,229,257,232,235,255,203,233,299,212,276,227,208,237,234,273,294,207,270,216
		Pattern pattern = Pattern
				.compile("((086|096|097|098|032|033|034|035|036|037|038|039|091|094|088|083|084|085|081|082|089|090"
						+ "|093|070|079|077|076|078|092|056|058|099|059|296|254|209|204|291|222|275|256|274|271|252|290|292|206|236|262|261"
						+ "|215|251|277|269|226|024|239|220|225|293|028|221|258|297|260|213|263|205|214|272|228|238|259|229|257|232|235|255"
						+ "|203|233|299|212|276|227|208|237|234|273|294|207|270|216)+([0-9]{7})\\b)");
		Matcher matcher;
		matcher = pattern.matcher(phone);
		return matcher.find();

	}

	private List<String> getLstPhone(String phone, String partnerId) {
		List<String> lstPhone = splitByChart(phone, ";");// tach phone theo ky tu (;)
		List<String> lstPhoneCheck = new ArrayList<String>();

		if (!Formater.isNull(partnerId)) {// update - không check những số điện thoại đã được lưu từ trước.
			Partner partner = partnerDao.getPartnerById(partnerId);
			Map<String, String> maps = new LinkedHashMap<>();
			if (!Formater.isNull(partner.getPhone())) {
				List<String> lstPhoneN = splitByChart(partner.getPhone(), ";");

				for (String pn : lstPhoneN) {// set phone vào -> maps
					maps.put(pn, "");
				}
			}

			for (String p : lstPhone) {// lấy những số mới để check
				if (!maps.containsKey(p)) {
					lstPhoneCheck.add(p);
				}
			}

		} else {// Add - check all
			lstPhoneCheck = lstPhone;
		}
		return lstPhoneCheck;
	}

	public void load(ModelMap model, HttpServletRequest rq, HttpServletResponse rp, PartnerForm form)
			throws IOException {

		returnJsonArray(rp, partnerDao.getAll(Partner.class));

	}

	public void checkPhone(ModelMap model, HttpServletRequest rq, HttpServletResponse rp, PartnerForm form)
			throws IOException {
		rp.setContentType("application/json;charset=utf-8");
		rp.setHeader("Cache-Control", "no-store");
		PrintWriter out = rp.getWriter();
		try {
			String phone = Formater.isNull(rq.getParameter("phone")) ? "" : rq.getParameter("phone");
			String partnerId = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");

			List<String> lstPhoneCheck = getLstPhone(phone, partnerId);

			JSONArray jsonArray = new JSONArray();
			for (String p : lstPhoneCheck) {
				JSONObject outStr = new JSONObject();
				outStr.put("phone", p);
				if (!checkFormatPhone(p) || p.length() != 10) {
					outStr.put("msg",
							"<font color=\"red\">Kh&#244;ng &#273;&#250;ng &#273;&#7883;nh d&#7841;ng</font>");
				} else {
					if (partnerDao.checkUsePhone(p)) {// check phone da duoc su dung
						outStr.put("msg",
								"<font color=\"red\">&#272;ang &#273;&#432;&#7907;c s&#7917; d&#7909;ng</font>");
					} else {
						outStr.put("msg", "<font color=\"blue\">&#272;&#250;ng &#273;&#7883;nh d&#7841;ng</font>");
					}
				}
				jsonArray.put(outStr);
			}
			out.println(jsonArray);
		} catch (Exception e) {
			lg.error(e);
		}
		out.flush();
		out.close();
	}

	public void createFtpFolder(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("partnerForm") PartnerForm form) throws IOException {
		String id = request.getParameter("partnerId");
		PrintWriter out = response.getWriter();
		Partner obj = partnerDao.getObject(Partner.class, id);
		String pathFTPQnaOut = qnaFtpContext.getFtpQnaOutFld() + "/" + obj.getCode();
		String pathFTPFx = qnaFtpContext.getFtpQnaFxFld() + "/" + obj.getCode();
		String outStr = "";
		boolean createdQnaOut = FTPUtils.makeDirectoryByPath(qnaFtpContext.getFtpInf(), pathFTPQnaOut);
		boolean createdFx = FTPUtils.makeDirectoryByPath(qnaFtpContext.getFtpInf(), pathFTPFx);
		if (createdQnaOut && createdFx)
			outStr = "T&#7841;o folder " + qnaFtpContext.getFtpQnaOutFld() + " v&#224; "
					+ qnaFtpContext.getFtpQnaFxFld() + " th&#224;nh c&#244;ng ";
		else if (!createdQnaOut && createdFx)
			outStr = "T&#7841;o folder " + qnaFtpContext.getFtpQnaOutFld() + " th&#224;nh c&#244;ng ";
		else if (createdQnaOut && !createdFx)
			outStr = "T&#7841;o folder " + qnaFtpContext.getFtpQnaFxFld() + " th&#224;nh c&#244;ng ";
		else {
			outStr = "Th&#432; m&#7909;c &#273;&#227; t&#7891;n t&#7841;i";
		}
		out.print(outStr);
		out.flush();
		out.close();
	}
}
