package cic.h2h.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cic.h2h.dao.hibernate.PartnerDao;
import cic.h2h.dao.hibernate.RpFileUnstructuredDao;
import cic.h2h.form.QnaOutFileForm;
import cic.h2h.form.RpFileUnstructuredForm;
import cic.utils.ExchgFtpContext;
import cic.utils.FTPUtils;
import cic.utils.FtpContext;
import cic.utils.FtpInf;
import common.util.Base64Utils;
import common.util.Formater;
import common.util.ResourceException;
import entity.Partner;
import entity.PartnerBranch;
import entity.QnaOutFile;
import entity.RpFileUnstructured;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.PartnerBranchDao;
import frwk.utils.ApplicationContext;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/rpFileUnstructured")
public class RpFileUnstructuredController extends CatalogController<RpFileUnstructuredForm, RpFileUnstructured> {

	private static Logger lg = LogManager.getLogger(RpFileUnstructuredController.class);

	@Autowired
	private RpFileUnstructuredDao rpFileUnstructuredDao;

	@Autowired
	PartnerDao partnerDao;

	@Autowired
	ExchgFtpContext exchgFtpContext;
	
	@Autowired
	private PartnerBranchDao partnerBranchDao;
	@Autowired
	private Partner partner;

	@Override
	public BaseDao<RpFileUnstructured> createSearchDAO(HttpServletRequest request, RpFileUnstructuredForm form)
			throws Exception {
		RpFileUnstructuredDao dao = new RpFileUnstructuredDao();
		RpFileUnstructuredForm rpFileUnstructuredForm = (RpFileUnstructuredForm) form;
		if (!Formater.isNull(rpFileUnstructuredForm.getKeyword_code()))
			dao.addRestriction(
					Restrictions.like("fileName", rpFileUnstructuredForm.getKeyword_code().trim(), MatchMode.ANYWHERE)
							.ignoreCase());

		if (!Formater.isNull(rpFileUnstructuredForm.getKeyword_name()))
			dao.addRestriction(
					Restrictions.like("tctdCode", rpFileUnstructuredForm.getKeyword_name().trim(), MatchMode.ANYWHERE)
							.ignoreCase());
		if (rpFileUnstructuredForm.getCicOwner() != null) {
			// nsd tctd
			if (rpFileUnstructuredForm.getCicOwner()) {
				dao.addRestriction(Restrictions.eq("type", new BigDecimal("0")));

			} else {
				dao.addRestriction(Restrictions.eq("type", new BigDecimal("1")));
			}
		}
		if (!Formater.isNull(rpFileUnstructuredForm.getFromDate())) {
			dao.addRestriction(Restrictions.ge("createdDate", Formater.str2date(rpFileUnstructuredForm.getFromDate())));
		}
		if (!Formater.isNull(rpFileUnstructuredForm.getToDate())) {
			Date currentDate = Formater.str2date(rpFileUnstructuredForm.getToDate());
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE, 1);
			Date dateAdd = c.getTime();
			dao.addRestriction(Restrictions.lt("createdDate", dateAdd));
		}
		dao.addOrder(Order.desc("createdDate"));
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, RpFileUnstructured e, RpFileUnstructuredForm modelForm) throws Exception {
		ja.put(e.getFileName());
		ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getCreatedDate()));
		ja.put(e.getBranch());
		ja.put(e.getCreatedBy());
		ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getLastDownloadTime()));
		ja.put(e.getFileSize() == null ? "" : e.getFileSize() + " KB");
		ja.put("<button type=\"button\" onclick=\"taiFile('" + e.getId() + "')\" "
				+ "style=\"font-size: 10px; padding: 0px 8px; border-radius: 0px; margin: 5px; height: 22px !important; display: initial;\" "
				+ "class=\"btn blue\">T&#7843;i file</button>");
	}

	@Override
	public BaseDao<RpFileUnstructured> getDao() {
		// TODO Auto-generated method stub
		return rpFileUnstructuredDao;
	}

	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "rp_file_unstructured/view";
	}

	@Autowired
	ExportExcel exportExcel;

	public void ExportFileExcel(ModelMap model, HttpServletRequest request, HttpServletResponse rs,
			RpFileUnstructuredForm form) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String partnerCode = request.getParameter("partnerCode");
		String fileName = request.getParameter("fileName");
		String cicOwner = request.getParameter("cicOwner");
		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");
		List<RpFileUnstructured> list = rpFileUnstructuredDao.reports(partnerCode, fileName, cicOwner, fromDate, toDate);
		for (RpFileUnstructured file : list) {
			if (file.getCreatedDate() != null)
				file.setCreateDateStr(Formater.date2ddsmmsyyyspHHmmss(file.getCreatedDate()));

			if (file.getLastDownloadTime() != null)
				file.setLastDownloadTimeStr(Formater.date2ddsmmsyyyspHHmmss(file.getLastDownloadTime()));
//			if (!Formater.isNull(file.getTctdCode())) {
//				Partner o = partnerDao.getPartnerByCode(file.getTctdCode());
//				if (o != null)
//					file.setPartnerName(o.getName());
//			}
			if(file.getFileSize() != null)
				file.setFileSizeStr(file.getFileSize() + " KB");
			
		}
		map.put("reports", list);
		exportExcel.export("Theo_doi_tep_phi_cau_truc_tra_soat", rs, map);
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RpFileUnstructuredForm form)
			throws Exception {
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		if (appContext != null) {
			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
			lg.info(user);
		}
		List<PartnerBranch> dsDonVi = partnerBranchDao.getBranch(partner);
		model.addAttribute("dsDonVi", dsDonVi);
	}

	public void downloadFileFtp(ModelMap map, HttpServletRequest rq, HttpServletResponse rp,
			RpFileUnstructuredForm form) throws Exception {
//		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
//		RpFileUnstructured obj = rpFileUnstructuredDao.getObject(RpFileUnstructured.class, id);
//		String folderFTP = obj.getType().compareTo(BigDecimal.ZERO) == 0 ? exchgFtpContext.getFtpExchgInFld()
//				: exchgFtpContext.getFtpExchgOutFld();
//		String filePath = folderFTP + "/" + obj.getTctdCode() + "/" + obj.getFileName();
//		if (!FTPUtils.existFile(exchgFtpContext.getFtpInf(), filePath)) {
//			rp.setContentType("text/html;charset=utf-8");
//			PrintWriter pw = rp.getWriter();
//			pw.print("File kh&#244;ng t&#7891;n t&#7841;i!");
//			pw.flush();
//			pw.close();
//			return;
//		}
//		String fileName = URLEncoder.encode(obj.getFileName(), "UTF-8").replace("+", "%20");
//		rp.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName);
//		FTPUtils.downloadFile(exchgFtpContext.getFtpInf(), rp.getOutputStream(), filePath);
	}

	@RequestMapping(value = "uploadFile", method = { RequestMethod.POST })
	public void uploadFile(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("inputFile") MultipartFile inputFile, @RequestParam("fileName") String fileName,
			@RequestParam("tctd") String tctd) throws Exception {

		String filePath = exchgFtpContext.getFtpExchgOutFld() + "/" + tctd;
		if (FTPUtils.existFile(exchgFtpContext.getFtpInf(), filePath + "/" + fileName)) {
			rs.setContentType("text/html;charset=utf-8");
			PrintWriter pw = rs.getWriter();
			pw.print("T&#234;n t&#7879;p &#273;&#227; t&#7891;n t&#7841;i tr&#234;n h&#7879; th&#7889;ng");
			pw.flush();
			pw.close();
			return;
		}
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
		String noiDungTep = loadFile(inputFile);
		byte[] ndFile = Base64.decodeBase64(noiDungTep);
		FTPUtils.storeFile(exchgFtpContext.getFtpInf(), new ByteArrayInputStream(ndFile), filePath, fileName);
		RpFileUnstructured object = new RpFileUnstructured();
		object.setFileName(fileName);
		object.setBranch(tctd);
		object.setCreatedDate(new Date());
		object.setCreatedBy(user.getUsername());
		object.setFileSize(BigDecimal.valueOf(ndFile.length).divide(BigDecimal.valueOf(1024*2)));
		object.setType(BigDecimal.ZERO);
		rpFileUnstructuredDao.save(object);
	}

	private String loadFile(MultipartFile inputFile) throws IOException {
		InputStream inputStream = new BufferedInputStream(inputFile.getInputStream());
		return Base64Utils.encodeFile(inputStream);
	}

	public void test(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RpFileUnstructuredForm form)
			throws Exception {
		SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
		FTPFile[] lstRpExchaneOut;
		try {
			lstRpExchaneOut = FTPUtils.getFileByPath(exchgFtpContext.getFtpInf(), exchgFtpContext.getFtpExchgOutFld());
		} catch (Exception e) {
			lg.error(e);
			return;
		}
		if (lstRpExchaneOut == null || lstRpExchaneOut.length == 0)
			return;

		List<String> lstTCTD = new ArrayList<String>();
		// lay danh sach TCTD trong folder FXQNA
		for (FTPFile folderChildrenRpExchaneOut : lstRpExchaneOut) {
//			if (f.format(folderChildrenRpExchaneOut.getTimestamp().getTime()).equals(f.format(new Date()))
//					|| f.format(folderChildrenRpExchaneOut.getTimestamp().getTime())
//							.equals(f.format(DateUtils.addDate(new Date(), -1))))
			lstTCTD.add(folderChildrenRpExchaneOut.getName());
		}
		if (lstTCTD == null || lstTCTD.size() == 0)
			return;
		List<RpFileUnstructured> newFiles = new ArrayList<RpFileUnstructured>();
		for (String str : lstTCTD) { // lap trong danh sach cac TCTD de lay danh sach file
			FTPFile[] tctdFiles = null;
			try {
				tctdFiles = FTPUtils.getFileByPath(exchgFtpContext.getFtpInf(),
						exchgFtpContext.getFtpExchgOutFld() + "/" + str);
			} catch (Exception e) {
				continue;
			}
			if (tctdFiles == null || tctdFiles.length == 0)
				continue;
			for (FTPFile tctdF : tctdFiles) { // for danh sach file cua TCTD
				lg.info(new String(tctdF.getName().getBytes("ISO-8859-1"), "UTF-8"));

			}
		}
	}
}
