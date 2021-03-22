package cic.h2h.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.util.IOUtils;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import cic.h2h.dao.hibernate.AppFlowDao;
import cic.h2h.dao.hibernate.CatAgencyStructureDao;
import cic.h2h.dao.hibernate.PocFileDao;
import cic.h2h.dao.hibernate.PocUserinfoFormDAO;
import cic.h2h.dao.hibernate.TemplatesDao;
import cic.h2h.form.PocUserinfoFormForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.AppFlow;
import entity.CatAgencyStructure;
import entity.PocFile;
import entity.PocInterViewRs;
import entity.PocUserinfoForm;
import entity.Templates;
import entity.frwk.SysDictParam;
import entity.frwk.SysUsers;
import entity.frwk.SysUsersLog;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.dao.hibernate.sys.UserLogDao;

@Controller
@RequestMapping("/erqMng")
public class ErqMngController extends CatalogController<PocUserinfoFormForm, PocUserinfoForm> {

	private static Logger log = LogManager.getLogger(ErqMngController.class);

	@Override
	public BaseDao<PocUserinfoForm> createSearchDAO(HttpServletRequest request, PocUserinfoFormForm form)
			throws Exception {
		PocUserinfoFormDAO dao = new PocUserinfoFormDAO();
		if (!Formater.isNull(form.getName())) {
			dao.addRestriction(Restrictions.like("name", form.getName().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(form.getCardId())) {
			dao.addRestriction(Restrictions.like("cardId", form.getName().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(form.getStatus())) {
			AppFlow status = appFlowDao.get(AppFlow.class, form.getStatus());
			dao.addRestriction(Restrictions.eq("status", status.getStatus()));
			if (!Formater.isNull(status.getSubStatus()))
				dao.addRestriction(Restrictions.eq("subStatus", status.getSubStatus()));

		}
		if (!Formater.isNull(getSessionUser().getAgencyStructureId())) {
			dao.addRestriction(Restrictions.sqlRestriction("exists (select 1\r\n" + "          from sys_users su,\r\n"
					+ "               (select *\r\n" + "                  from CAT_AGENCY_STRUCTURE s\r\n"
					+ "                connect by prior s.id = s.parent_id\r\n"
					+ "                 start with s.id = ?) t\r\n" + "         where t.id = su.agency_structure_id\r\n"
					+ "           and su.id = {alias}.create_id)", getSessionUser().getAgencyStructureId(),
					StringType.INSTANCE));
		}
		dao.addOrder(Order.desc("createDt"));
		return dao;
	}

	@Autowired
	private SysDictParamDao sysDictParamDao;
	@Autowired
	private SysUsersDao sysUsersDao;
	@Autowired
	private CatAgencyStructureDao catAgencyStructureDao;

	@Override
	protected void pushToJa(JSONArray ja, PocUserinfoForm e, PocUserinfoFormForm modelForm) throws Exception {

		SysUsers su = sysUsersDao.getUserById(e.getCreateId());
		String createInf = su.getUsername();
		if (!Formater.isNull(su.getAgencyStructureId())) {
			CatAgencyStructure agenStr = catAgencyStructureDao.get(CatAgencyStructure.class, su.getAgencyStructureId());
			createInf += "(" + agenStr.getCode() + "-" + agenStr.getName() + ")";
		}
		ja.put("<span class='characterwrap'>" + createInf + "</span>");
		ja.put("<a href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getName() + "</a>");
		ja.put(e.getDateOfBirthdayStr());
		if (Formater.isNull(e.getPlaceOfBirth())) {
			ja.put("");
		} else {
			SysDictParam dictType = sysDictParamDao.get(SysDictParam.class, e.getPlaceOfBirth());
			if (dictType != null)
				ja.put(dictType.getValue());
			else
				ja.put("");
		}
		ja.put("<span class='characterwrap'>" + (Formater.isNull(e.getEmail()) ? "" : e.getEmail()) + "</span>");
		ja.put("<span class='characterwrap'>" + (Formater.isNull(e.getCardId()) ? "" : e.getCardId()) + "</span>");
		ja.put("<span class='characterwrap'>" + (Formater.isNull(e.getCardIdOther()) ? "" : e.getCardIdOther())
				+ "</span>");

		if (Formater.isNull(e.getNationality())) {
			ja.put("");
		} else {
			SysDictParam dictType = sysDictParamDao.get(SysDictParam.class, e.getNationality());
			if (dictType != null)
				ja.put(dictType.getValue());
			else
				ja.put("");
		}
		ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getCreateDt()));
		AppFlow appFlow = appFlowDao.getByStatus(e.getStatus(), e.getSubStatus(), modelForm.getModelClass());
		if (appFlow != null)
			ja.put(appFlow.getCode() + "-" + appFlow.getName());
		else
			ja.put(e.getStatus() + (e.getSubStatus() == null ? "" : "." + e.getSubStatus()));
	}

	@Override
	public BaseDao<PocUserinfoForm> getDao() {

		return pocUserinfoFormDAO;
	}

	@Override
	public String getJsp() {

		return "erqMng/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PocUserinfoFormForm form)
			throws Exception {
		Templates temp = templatesDao.getByCode("POC_USERINFO");
		model.addAttribute("testHTML", inputTemp.getDynHtml(temp, form.getModel(), null));
		List<AppFlow> lstAppflow = appFlowDao.getAllOrder(form.getModelClass());
		model.addAttribute("lstAppflow", lstAppflow);

	}

	@Autowired
	private TemplatesDao templatesDao;
	@Autowired
	private InputTemp inputTemp;

	@Autowired
	private AppFlowDao appFlowDao;
	@Autowired
	private PocUserinfoFormDAO pocUserinfoFormDAO;

	@Autowired
	SysParamDao sysParamDao;
	public void next(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PocUserinfoFormForm form)
			throws Exception {
		approve(1, model, rq, rs, form);

	}

	public void previous(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PocUserinfoFormForm form)
			throws Exception {
		approve(-1, model, rq, rs, form);
	}

	private void approve(Integer type, ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			PocUserinfoFormForm form) throws Exception {
		PocUserinfoForm erqInf = form.getModel();
		String notes = erqInf.getDescription();
		pocUserinfoFormDAO.load(erqInf);
		if (!RightUtils.haveAction(rq, "method=next&sts=" + erqInf.getStatusCode()))
			throw new ResourceException("User don't have righs in this status");
		erqInf.setDescription(notes);
		List<AppFlow> lstAppflow = appFlowDao.getAllOrder(form.getModelClass());
		if (type == -1)
			erqInf.previos(lstAppflow);
		else if (type == 1)
			erqInf.next(lstAppflow);
		pocUserinfoFormDAO.save(erqInf);
	}

	@Autowired
	private PocFileDao pocFileDao;

	public void download(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PocUserinfoFormForm form)
			throws Exception {
		String id = rq.getParameter("fileId");
		String fileNameField = rq.getParameter("fileId");
		String fileIdField = rq.getParameter("fileIdField");
		if (Formater.isNull(id)) {
			throw new ResourceException("Khong ton tai ban ghi");
		}
		PocFile pocFile = pocFileDao.get(PocFile.class, id);
		if (Formater.isNull(pocFile.getFilePath()) && Formater.isNull(pocFile.getFileName())) {
			throw new ResourceException("Chua upload file");
		}
		try {
			File file = new File(pocFile.getFilePath());
			InputStream fileInputStream = new FileInputStream(file);
			rs.addHeader("Content-Disposition", "attachment; filename=" + pocFile.getFileName());
			OutputStream responseOutputStream = rs.getOutputStream();
			int bytes;
			while ((bytes = fileInputStream.read()) != -1) {
				responseOutputStream.write(bytes);
			}
			fileInputStream.close();
			responseOutputStream.close();
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
			throw new ResourceException("Loi he thong lien he Admin");
		}
	}

	@RequestMapping(value = "upload", method = { RequestMethod.POST })
	public void upload(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PocUserinfoFormForm form,
			@RequestParam("inputFile") MultipartFile inputFile, @RequestParam("fileName") String fileName,
			@RequestParam("idRecordUpload") String fileId) throws Exception {
		try {
			if (Formater.isNull(fileId)) {
				rs.setContentType("text/html;charset=utf-8");
				PrintWriter pw = rs.getWriter();
				pw.print("File id is not exists!");
				return;
			}
			String PATH = "E://POC_FILE";
			String tmp[] = fileName.split("[.]");
			PocFile pocFile = pocFileDao.get(PocFile.class, fileId);
			pocFile.setFileName(fileName);
			pocFile.setFilePath(PATH + File.separator + fileId + "." + tmp[1]);
			pocFileDao.save(pocFile);
			File directory = new File(PATH);
			if (!directory.exists()) {
				directory.mkdir();
			}
			File file = new File(PATH + File.separator + fileId + "." + tmp[1]);
			InputStream is = inputFile.getInputStream();
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			IOUtils.copy(is, fos);

			fos.close();
			is.close();
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
			rs.setContentType("text/html;charset=utf-8");
			PrintWriter pw = rs.getWriter();
			pw.print("Co loi xay ra lien he Admin!");
		}

	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PocUserinfoFormForm form)
			throws Exception {
		PocUserinfoForm erqInf = form.getModel();
		processData(erqInf);
//		super.save(model, rq, rs, form);
		pocUserinfoFormDAO.saveTemplate(erqInf);
		returnObject(rs, form.getModel());
	}

	private void processData(PocUserinfoForm erqInf) throws Exception {
		PocUserinfoForm tmp = pocUserinfoFormDAO.getByIdCard(erqInf.getCardId());
		if (tmp != null) {
			if (!tmp.getId().equals(erqInf.getId()))
				throw new ResourceException("ID card already exists!");
		}

		// Ngay tao, nguoi tao, don vi tao
		if (Formater.isNull(erqInf.getId())) {
			erqInf.setCreateDt(Calendar.getInstance().getTime());
			erqInf.setCreateId(getSessionUser().getId());
			erqInf.setBuId(getSessionUser().getDepartmentId());
		} else {
			PocUserinfoForm old = pocUserinfoFormDAO.load(PocUserinfoForm.class, erqInf.getId());
			erqInf.setCreateDt(old.getCreateDt());
			erqInf.setCreateId(old.getCreateId());
			erqInf.setBuId(old.getBuId());
		}

		// Trang thai
		List<AppFlow> lstAppflow = appFlowDao.getAllOrder(erqInf.getClass());
		erqInf.setAppFlow(lstAppflow.get(0));

	}

	@Override
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PocUserinfoFormForm form)
			throws Exception {
		String id = rq.getParameter("id");
		getDao().load(form.getModel(), id);
		Templates temp = templatesDao.getByCode("POC_USERINFO");
		if (temp != null)
			form.getModel().setTemplateId(temp.getId());
		AppFlow appFlow = appFlowDao.getByStatus(form.getModel().getStatus(), form.getModel().getSubStatus(),
				form.getModelClass());
		if (appFlow != null) {
			form.getModel().setAppFlow(appFlow);
			List<AppFlow> lstAppflow = appFlowDao.getAllOrder(form.getModelClass());
			for (int i = 0; i < lstAppflow.size(); i++) {
				if (appFlow.equalStatus(lstAppflow.get(i))) {
					if (i == 0) {
						appFlow.setFirst(Boolean.TRUE);
					} else {
						appFlow.setFirst(Boolean.FALSE);
					}
					if (i == lstAppflow.size() - 1) {
						appFlow.setLast(Boolean.TRUE);
					} else {
						appFlow.setLast(Boolean.FALSE);
					}
					break;
				}
			}
		} else {
			appFlow = new AppFlow();
			appFlow.setFirst(Boolean.FALSE);
		}
		// check quyen
		if (RightUtils.haveAction(rq, "erqMng?method=next&sts=" + form.getModel().getStatusCode()))
			appFlow.setRight(Boolean.TRUE);
		else
			appFlow.setRight(Boolean.FALSE);

		if (getSessionUser().getId().equals(form.getModel().getCreateId()))
			appFlow.setOwner(Boolean.TRUE);
		else
			appFlow.setOwner(Boolean.FALSE);
		returnObject(rs, form.getModel());
	}

	public void defaultObject(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PocUserinfoFormForm form)
			throws Exception {
		PocUserinfoForm df = form.getModel();// pocInterViewRs
		List<SysDictParam> questions = sysDictParamDao.getByType("QUESTION");
		for (SysDictParam q : questions)
			df.getPocInterViewRs().add(new PocInterViewRs(q));
		returnObject(rs, df);
	}

	@Autowired
	UserLogDao userLogDao;

	public void getHistory(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PocUserinfoFormForm form)
			throws Exception {
		try {
			rs.setContentType("text/html;charset=utf-8");
			PrintWriter pw = rs.getWriter();
			String recordId = rq.getParameter("id");
			if (Formater.isNull(recordId)) {
				pw.print("Chua ton tai ung vien");
				pw.close();
				return;
			}
			List<SysUsersLog> logs = userLogDao.findByRecordid(recordId);
			JSONArray jsonArray = new JSONArray();
			for (SysUsersLog log : logs) {
				if (Formater.isNull(log.getDetail()))
					continue;
				JSONObject object = new JSONObject(log.getDetail());
				JSONObject jsonObject = new JSONObject();
				JSONObject jsonApp = null;
				if (object.has("appFlow") && object.get("appFlow") != null && !object.isNull("appFlow"))
					jsonApp = new JSONObject(object.get("appFlow").toString());
				jsonObject.put("name", log.getUserId());
				if (jsonApp != null)
					jsonObject.put("status", jsonApp.get("name"));
				else
					jsonObject.put("status", "");
				jsonObject.put("date", Formater.date2ddsmmsyyyspHHmmss(log.getModifyTime()));
				jsonObject.put("note", object.get("description"));
				jsonArray.put(jsonObject);
			}
			pw.print(jsonArray);
			pw.close();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new ResourceException("Co loi xay ra lien he Admin");
		}
	}

	@RequestMapping(value = "/uploadCMT", method = { RequestMethod.POST })
	public void uploadCMT(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("fileMT") MultipartFile fileMT, @RequestParam("fileMS") MultipartFile fileMS)
			throws IOException {
		Object objMT = callAPICMT(fileMT);
		Object objMS = callAPICMT(fileMS);
		JSONObject obj = new JSONObject();
		obj.put("objMT", objMT);
		obj.put("objMS", objMS);
		rs.setContentType("text/html;charset=utf-8");
		PrintWriter pw = rs.getWriter();
		pw.print(obj);
		pw.close();
	}

	public Object callAPICMT(MultipartFile file) throws IOException {
		LinkedMultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
		map.add("image", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("api_key", sysParamDao.getSysParamByCode("FPT_AI_KEY").getValue());
		HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(map, headers);
		String serverUrl = "https://api.fpt.ai/vision/idr/vnm";
		String json = restTemplate.exchange(serverUrl, HttpMethod.POST, requestEntity, String.class).getBody();
		JSONObject jsonObject = new JSONObject(json);
		return jsonObject.get("data");
	}

	class MultipartInputStreamFileResource extends InputStreamResource {

		private final String filename;

		MultipartInputStreamFileResource(InputStream inputStream, String filename) {
			super(inputStream);
			this.filename = filename;
		}

		@Override
		public String getFilename() {
			return this.filename;
		}

		@Override
		public long contentLength() throws IOException {
			return -1; // we do not want to generally read the whole stream into memory ...
		}
	}

}
