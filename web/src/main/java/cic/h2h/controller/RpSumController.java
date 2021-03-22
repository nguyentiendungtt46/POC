package cic.h2h.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.CatErrorDAO;
import cic.h2h.dao.hibernate.RpSumDao;
import cic.h2h.dao.hibernate.RpSumErrorDao;
import cic.h2h.dao.hibernate.RpTypeDao;
import cic.h2h.dao.hibernate.RpValidateDetailDao;
import cic.h2h.form.RpSumForm;
import cic.utils.FTPUtils;
import cic.utils.FtpContext;
import cic.utils.M1FtpContext;
import common.util.FormatNumber;
import common.util.Formater;
import constants.RightConstants;
import entity.CatError;
import entity.Partner;
import entity.PartnerBranch;
import entity.RpSum;
import entity.RpType;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.PartnerBranchDao;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/rpSum")
public class RpSumController extends CatalogController<RpSumForm, RpSum> {

	static Logger lg = LogManager.getLogger(RpSumController.class);

	@Autowired
	private RpSumDao rpSumDao;

	@Autowired
	private RpTypeDao rpTypeDao;

	@Autowired
	private RpValidateDetailDao validateDetailDao;

	@Autowired
	private SysPartnerDao sysPartnerDao;

	@Autowired
	FtpContext ftpContext;

	@Autowired
	M1FtpContext m1FtpContext;

	@Autowired
	private ExportExcel exportExcel;

	private List<Partner> dsDoiTac;

	private List<RpType> lstRpType;

	@Override
	public BaseDao<RpSum> getDao() {
		return rpSumDao;
	}
	
	@Autowired
	private PartnerBranchDao partnerBranchDao;
	@Autowired
	private Partner partner;

	@Override
	public BaseDao<RpSum> createSearchDAO(HttpServletRequest request, RpSumForm rpSumForm) throws Exception {
		RpSumDao dao = new RpSumDao();

		if (!Formater.isNull(rpSumForm.getFormDate())) {
			dao.addRestriction(Restrictions.ge("createdDate", Formater.str2date(rpSumForm.getFormDate())));
		}
		if (!Formater.isNull(rpSumForm.getToDate())) {
			Date currentDate = Formater.str2date(rpSumForm.getToDate());
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE, 1);
			Date dateAdd = c.getTime();
			dao.addRestriction(Restrictions.lt("createdDate", dateAdd));
		}

		// Search by user bao cao
		if (!Formater.isNull(rpSumForm.getUserReport())) {
			dao.addRestriction(
					Restrictions.like("userReport", rpSumForm.getUserReport().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		// Search by ma bao cao rut gon
		if (!Formater.isNull(rpSumForm.getReportCode()))
			dao.addRestriction(Restrictions.eq("reportCode", rpSumForm.getReportCode().trim()));

		if (!Formater.isNull(rpSumForm.getFileName()))
			dao.addRestriction(
					Restrictions.like("fileName", rpSumForm.getFileName().trim(), MatchMode.ANYWHERE).ignoreCase());

		// Search by ma tctd
		/*
		 * if (!Formater.isNull(rpSumForm.getMatctd())) {
		 * dao.addRestriction(Restrictions.or(Restrictions.eq("tctdCode",
		 * rpSumForm.getMatctd().trim()), Restrictions.sqlRestriction(
		 * "exists (select 1 from partner_branch pb, partner p where pb.partner_id = p.id and pb.code = {alias}.tctd_code and p.code = ?)"
		 * , rpSumForm.getMatctd().trim(), StringType.INSTANCE))); }
		 */
		// search by ma chi nhanh
		if (!Formater.isNull(rpSumForm.getBranch())) {
			dao.addRestriction(Restrictions.eq("branch", rpSumForm.getBranch()));
		}

		// Search by trang thai
		if (!Formater.isNull(rpSumForm.getStatus()))
			dao.addRestriction(Restrictions.eq("status", rpSumForm.getStatus()));

		// Search by trang thai redo
		if (rpSumForm.getRe_do() != null && rpSumForm.getRe_do())
			dao.addRestriction(Restrictions.eq("redo", Short.valueOf("1")));

		if (rpSumForm.getPartnerCancel() != null && rpSumForm.getPartnerCancel()) {
			dao.addRestriction(Restrictions.eq("redo", Short.valueOf("1")));
			dao.addRestriction(Restrictions.eq("status", Short.valueOf("3")));
			dao.addRestriction(Restrictions.sqlRestriction(
					"exists (select 1 from rp_sum_error e where e.RP_SUM_ID = {alias}.id and e.ERROR_CODE=?)",
					"RPF_169", StringType.INSTANCE));
		}

		// Order by thoigiannhan
		dao.addOrder(Order.desc("createdDate"));

		return dao;
	}

	public void ExportFileExcel(ModelMap model, HttpServletRequest request, HttpServletResponse rs, RpSumForm rpSumForm)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String tuNgay = request.getParameter("tuNgay");
		String denNgay = request.getParameter("denNgay");
		String partnerCode = request.getParameter("partnerCode");
		String fileName = request.getParameter("fileName");
		String status = request.getParameter("status");
		String userReport = request.getParameter("userReport");
		String reportCode = request.getParameter("reportCode");
		String reDo = request.getParameter("reDo");
		List<RpSum> list = rpSumDao.reports(tuNgay, denNgay, partnerCode, fileName, status, userReport, reportCode,
				reDo);
		for (RpSum file : list) {
			if (file.getReportDate() != null)
				file.setReportDateStr(Formater.date2str(file.getReportDate()));
			if (file.getStatus() != null) {
				if (0 == file.getStatus()) {
					file.setStatusStr("Chưa xử lý");
				} else if (1 == file.getStatus()) {
					file.setStatusStr("1- Đang xử lý");
				} else if (2 == file.getStatus()) {
					file.setStatusStr("2 - Đã xử lý H2H");
				} else if (3 == file.getStatus()) {
					file.setStatusStr("3 - Lỗi tại H2H");
				} else if (4 == file.getStatus()) {
					file.setStatusStr("4- Đã chuyển sang M1");
				} else if (5 == file.getStatus()) {
					file.setStatusStr("5 - M1 đã hoàn thành");
				}
			}
			if (!Formater.isNull(file.getErrorCode())) {
				String error = mapError.get(file.getErrorCode());
				if ("0".equals(file.getFileFormat())) {
					file.setErrorCode(file.getErrorCode() + (Formater.isNull(error) ? "" : " - " + error));
				} else if ("1".equals(file.getFileFormat())) {
					file.setErrorCode(file.getErrorCode() + (Formater.isNull(error) ? "" : " - " + error) + " - "
							+ (Formater.isNull(file.getErrorDes()) ? "" : " - " + file.getErrorDes()));
				}
			}
			if (file.getProcessEndTime() != null && file.getProcessStartTime() != null) {
				long diffInMillies = Math
						.abs(file.getProcessStartTime().getTime() - file.getProcessEndTime().getTime());
				long diff = TimeUnit.SECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
				file.setCreateDateStr(FormatNumber.num2Str(diff));
			}
			if(file.getFileSize() != null)
				file.setFileSizeStr(file.getFileSize().toString() + " KB");
		}
		map.put("reports", list);
		exportExcel.export("To_chuc_tin_dung_gui_bao_cao", rs, map);
	}

	@Autowired
	private CatErrorDAO catErrorDAO;
	@Autowired
	private SysUsersDao sysUsersDao;

	@Override
	public void pushToJa(JSONArray ja, RpSum r, RpSumForm modal) throws Exception {
		ja.put(r.getUserReport());
		ja.put(r.getReportCode());
		RpSum bcDoiUng = Formater.isNull(r.getReverseReportId()) ? null
				: rpSumDao.getObject(RpSum.class, r.getReverseReportId());
		boolean bRedo = (r.getRedo() != null && r.getRedo().intValue() == 1);
		if (r.getZipReport() == null || !r.getZipReport()) {
			if (r.getStatus() == null || r.getStatus() != 3) {
				ja.put("<a class='characterwrap' href='#' onclick='detailValidate(\"" + r.getId() + "\")'>" + "<font"
						+ (bRedo ? " title=\"TCTD g&#7917;i l&#7841;i file\" color=\"red\"" : "") + ">"
						+ (r.getFileName() + (bcDoiUng == null ? "" : "/" + bcDoiUng.getFileName())) + "</font>"
						+ "</a>");

			} else {
				if (rpSumErrorDao.hadCancel(r)) {
					ja.put("<a class='characterwrap' href='#' onclick='detailValidate(\"" + r.getId() + "\")'>"
							+ "<font" + (bRedo ? " title=\"TCTD h&#x1EE7;y b&#xE1;o c&#xE1;o\" color=\"orange\"" : "")
							+ ">" + (r.getFileName() + (bcDoiUng == null ? "" : "/" + bcDoiUng.getFileName()))
							+ "</font>" + "</a>");
				} else {
					ja.put("<a class='characterwrap' href='#' onclick='detailValidate(\"" + r.getId() + "\")'>"
							+ "<font" + (bRedo ? " title=\"TCTD g&#7917;i l&#7841;i file\" color=\"red\"" : "") + ">"
							+ (r.getFileName() + (bcDoiUng == null ? "" : "/" + bcDoiUng.getFileName())) + "</font>"
							+ "</a>");
				}
			}

		} else {
			ja.put(r.getFileName());
		}

		ja.put(r.getBranch());
		ja.put(Formater.date2str(r.getReportDate()));
		ja.put(r.getReporterName());

		if (r.getStatus() == null)
			ja.put("");
		else {
			switch (r.getStatus().intValue()) {
			case 0:
				ja.put(r.getStatus() + " - Ch&#432;a g&#7917;i");
				break;
			case 1:
				ja.put(r.getStatus() + " - &#272;&#227; g&#7917;i");
				break;
			case 2:
				ja.put(r.getStatus() + " - &#272;&#227; c&#243; k&#7871;t qu&#7843;");
				break;
			case 3:
				ja.put(r.getStatus() + " - &#272;&#227; h&#7911;y");
				break;
			default:
				ja.put("");
				break;
			}
		}
		if (Formater.isNull(r.getErrorCode()))
			ja.put("");
		else {
			String error = mapError.get(r.getErrorCode());
			if ("0".equals(r.getFileFormat())) {
				ja.put(r.getErrorCode() + (Formater.isNull(error) ? "" : " - " + error));
			} else if ("1".equals(r.getFileFormat())) {
				ja.put(r.getErrorCode() + (Formater.isNull(error) ? "" : " - " + error) + " - "
						+ (Formater.isNull(r.getErrorDes()) ? "" : " - " + r.getErrorDes()));
			} else
				ja.put("");
		}

		ja.put(FormatNumber.num2Str(r.getErrorPer())
				+ (r.getNumOfRecord() == null ? "" : " (" + r.getNumOfRecord() + ")"));

		ja.put(Formater.date2ddsmmsyyyspHHmmss(r.getCreatedDate()));
		ja.put(r.getFileSize() == null ? "" : r.getFileSize() + " KB");
		String actionBtns = "";
		if (r.getStatus() == 3) {
			if (r.getZipReport() == null || !r.getZipReport()) {
				// Thuc hien lai
				if (r.getEnableReExec() != null && r.getEnableReExec())
					actionBtns += genHtmlBtn(HTML_REEXE_BTN, r.getId());

				// Chuyen sang M1
//				if ((r.getRedo() == null || r.getRedo().intValue() == 0)) {
//					actionBtns += genHtmlBtn(HTML_RESND_BTN, r.getId());
//					actionBtns += genHtmlBtn(HTML_SND_M1_BTN, r.getId());
//				}
			}
		}

		if (!Formater.isNull(r.getFilePath()))
			actionBtns += genHtmlBtn(HTML_DOWNLOAD_BTN, r.getId());

		if (!Formater.isNull(r.getAttachmentName()))
			actionBtns += genHtmlBtn(HTML_DOWNLOAD_ATT_BTN, r.getId());

		ja.put(actionBtns);

	}

	@Autowired
	private RpSumErrorDao rpSumErrorDao;

	private static final String HTML_DOWNLOAD_BTN = "<button type=\"button\" onclick=\"taiFile('%s')\" "
			+ "style=\"font-size: 10px; padding: 0px 8px; border-radius: 0px; margin: 5px; height: 22px !important; display: initial;\" "
			+ "class=\"btn blue\">T&#7843;i file</button>";
	private static final String HTML_DOWNLOAD_ATT_BTN = "<button type=\"button\" onclick=\"taiFileAttach('%s')\" "
			+ "style=\"font-size: 10px; padding: 0px 8px; border-radius: 0px; margin: 5px; height: 22px !important; display: initial;\" "
			+ "class=\"btn blue\">T&#7843;i file đính kèm</button>";
	private static final String HTML_RESND_BTN = "<button type=\"button\" onclick=\"traLaiTctd('%s')\" style=\"font-size: 10px; padding: 0px 8px; margin: 5px; border-radius: 0px; height: 22px !important; display: initial;\" class=\"btn red\">Tr&#7843; l&#7841;i TCTD</button>";
	private static final String HTML_SND_M1_BTN = "<button type=\"button\" onclick=\"chuyenSangM1('%s')\" "
			+ "style=\"font-size: 10px; padding: 0px 8px; border-radius: 0px; margin: 5px; height: 22px !important; display: initial;\" "
			+ "class=\"btn blue\">Chuy&#7875;n sang M1</button>";
	private static final String HTML_REEXE_BTN = "<button type=\"button\" onclick=\"redo('%s')\" style=\"font-size: 10px; padding: 0px 8px; border-radius: 0px; margin: 5px; height: 22px !important; display: initial;\" class=\"btn blue\">Th&#7921;c hi&#7879;n l&#7841;i</button>";

	private static String genHtmlBtn(String action, String recordId) {
		return String.format(action, recordId);
	}

	@Override
	public String getJsp() {
		return "rp_sum/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RpSumForm form)
			throws Exception {
		lstRpType = rpTypeDao.getAll();
		dsDoiTac = sysPartnerDao.getAll();
		model.addAttribute("dsDoiTac", dsDoiTac);
		model.addAttribute("lstRpType", lstRpType);
		List<CatError> errors = catErrorDAO.getAll(CatError.class);
		for (CatError error : errors)
			mapError.put(error.getCode(), error.getName());
		List<PartnerBranch> dsDonVi = partnerBranchDao.getBranch(partner);
		model.addAttribute("dsDonVi", dsDonVi);
	}

	private static final Map<String, String> mapError = new HashMap<String, String>();

	public void updateRedo(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, RpSumForm form)
			throws Exception {
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		rpSumDao.reject(id);
	}

	public void transfeFileToM1(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, RpSumForm form)
			throws IOException {
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		try {
			RpSum rpSum = rpSumDao.getObject(RpSum.class, id);

			FTPUtils.copyFileBeetWeen2Server(ftpContext.getFtpInf(), rpSum.getFilePath(), m1FtpContext.getFtpInf(),
					m1FtpContext.getFtpM1InfFld() == null || m1FtpContext.getFtpM1InfFld().trim().isEmpty()
							? rpSum.getFilePath()
							: m1FtpContext.getFtpM1InfFld().trim() + "/" + rpSum.getFilePath());
			if (rpSum.getAttachmentName() != null && !rpSum.getAttachmentName().isEmpty())
				FTPUtils.copyFileBeetWeen2Server(ftpContext.getFtpInf(), rpSum.getAttachmentPath(),
						m1FtpContext.getFtpInf(),
						m1FtpContext.getFtpM1InfFld() == null || m1FtpContext.getFtpM1InfFld().trim().isEmpty()
								? rpSum.getAttachmentPath()
								: m1FtpContext.getFtpM1InfFld().trim() + "/" + rpSum.getAttachmentPath());

			rpSum.setStatus(RightConstants.STATUS_4);
			rpSum.setSendToM1Time(Calendar.getInstance().getTime());
			if (rpSum.getWaitResource() != null && rpSum.getWaitResource())
				rpSum.setWaitResource(null);

			rpSumDao.save(rpSum);

			// Chuyen bao cao doi ung sang M1
			if (Formater.isNull(rpSum.getReverseReportId()))
				return;
			rpSum = rpSumDao.getObject(RpSum.class, rpSum.getReverseReportId());
			FTPUtils.copyFileBeetWeen2Server(ftpContext.getFtpInf(), rpSum.getFilePath(), m1FtpContext.getFtpInf(),
					m1FtpContext.getFtpM1InfFld() == null || m1FtpContext.getFtpM1InfFld().trim().isEmpty()
							? rpSum.getFilePath()
							: m1FtpContext.getFtpM1InfFld().trim() + "/" + rpSum.getFilePath());
			if (rpSum.getAttachmentName() != null && !rpSum.getAttachmentName().isEmpty())
				FTPUtils.copyFileBeetWeen2Server(ftpContext.getFtpInf(), rpSum.getAttachmentPath(),
						m1FtpContext.getFtpInf(),
						m1FtpContext.getFtpM1InfFld() == null || m1FtpContext.getFtpM1InfFld().trim().isEmpty()
								? rpSum.getAttachmentPath()
								: m1FtpContext.getFtpM1InfFld().trim() + "/" + rpSum.getAttachmentPath());
			rpSum.setStatus(RightConstants.STATUS_4);
			if (rpSum.getWaitResource() != null && rpSum.getWaitResource())
				rpSum.setWaitResource(null);
			rpSum.setSendToM1Time(Calendar.getInstance().getTime());

			rpSumDao.save(rpSum);

		} catch (Exception e) {
			lg.error(e);
		}
	}

	public void downloadFileFtp(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, RpSumForm form)
			throws Exception {
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		String type = rq.getParameter("type");
		RpSum rpSum = rpSumDao.getObject(RpSum.class, id);
		String fileName = "fa".equals(type) ? rpSum.getAttachmentName() : rpSum.getFileName();

		String filePath = "fa".equals(type) ? rpSum.getAttachmentPath() : rpSum.getFilePath();
		if (!FTPUtils.existFile(ftpContext.getFtpInf(), filePath)) {
			rp.setContentType("text/html;charset=utf-8");
			PrintWriter pw = rp.getWriter();
			pw.print("File kh&#244;ng t&#7891;n t&#7841;i!");
			pw.flush();
			pw.close();
			return;
		}
		rp.addHeader("Content-Disposition", "attachment; filename=" + fileName);
		OutputStream responseOutputStream = rp.getOutputStream();
		FTPUtils.downloadFile(ftpContext.getFtpInf(), responseOutputStream, filePath);
	}

	public void redo(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RpSumForm form) throws Exception {
		String reportId = rq.getParameter("reportId");
		rpSumDao.redo(reportId);
	}

}
