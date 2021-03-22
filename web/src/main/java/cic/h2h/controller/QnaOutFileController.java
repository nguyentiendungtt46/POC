package cic.h2h.controller;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import cic.h2h.dao.hibernate.QnaOutFileDao;
import cic.h2h.form.QnaOutFileForm;
import cic.utils.FTPUtils;
import cic.utils.QnAFtpContext;
import common.util.Formater;
import entity.Partner;
import entity.QnaOutFile;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/qnaOutFile")
public class QnaOutFileController extends CatalogController<QnaOutFileForm, QnaOutFile> {

	static Logger lg = LogManager.getLogger(QnaOutFileController.class);

	@Autowired
	private QnaOutFileDao qnaOutFileDao;

	@Autowired
	private SysPartnerDao sysPartnerDao;

	private List<Partner> dsDoiTac;

	@Override
	public BaseDao<QnaOutFile> getDao() {
		return qnaOutFileDao;
	}

	@Autowired
	ExportExcel exportExcel;

	public void ExportFileExcel(ModelMap model, HttpServletRequest request, HttpServletResponse rs, QnaOutFileForm form)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String tuNgay = request.getParameter("tuNgay");
		String denNgay = request.getParameter("denNgay");
		String partnerCode = request.getParameter("partnerCode");
		String fileName = request.getParameter("fileName");
		String status = request.getParameter("status");
		List<QnaOutFile> list = qnaOutFileDao.reports(tuNgay, denNgay, partnerCode, fileName, status);
		for (QnaOutFile file : list) {
			if (file.getNgayCoKetQua() != null) {
				file.setNgayCoKetQuaStr(Formater.date2strDateTime(file.getNgayCoKetQua()));
			}
			if (file.getThoidiemtrachotctd() != null) {
				file.setThoidiemtrachotctdStr(Formater.date2strDateTime(file.getThoidiemtrachotctd()));
			}
		}
		map.put("fromDate", tuNgay);
		map.put("toDate", denNgay);
		map.put("fileName", fileName);
		map.put("maTCTD", partnerCode);
		if (!Formater.isNull(status)) {
			if ("1".equals(status))
				map.put("status", "Chưa trả cho TCTD");
			else map.put("status", "Đã trả cho TCTD");
		} else {
			map.put("status", "");
		}
		map.put("reports", list);
		exportExcel.export("Tra_loi_dinh_ky", rs, map);
	}

	@Override
	public BaseDao<QnaOutFile> createSearchDAO(HttpServletRequest request, QnaOutFileForm form) throws Exception {
		QnaOutFileDao dao = new QnaOutFileDao();
		QnaOutFileForm qnaOutFileForm = (QnaOutFileForm) form;

		if (!Formater.isNull(qnaOutFileForm.getNgaytraloitu())) {
			dao.addRestriction(
					Restrictions.ge("thoidiemtrachotctd", Formater.str2date(qnaOutFileForm.getNgaytraloitu())));
		}
		if (!Formater.isNull(qnaOutFileForm.getNgaytraloiden())) {
			Date currentDate = Formater.str2date(qnaOutFileForm.getNgaytraloiden());
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE, 1);
			dao.addRestriction(Restrictions.lt("thoidiemtrachotctd", c.getTime()));
		}

		// Search by file name
		if (!Formater.isNull(qnaOutFileForm.getOutFileName())) {
			dao.addRestriction(Restrictions.like("tenfile", qnaOutFileForm.getOutFileName().trim(), MatchMode.ANYWHERE)
					.ignoreCase());
		}

		// Search by ma tctd
		if (!Formater.isNull(qnaOutFileForm.getMatctd()))
			dao.addRestriction(Restrictions.eq("maToChucTinDung", qnaOutFileForm.getMatctd()));

		if (form.getStatus() != null) {
			if (form.getStatus() == 1)
				dao.addRestriction(Restrictions.isNull("thoidiemtrachotctd"));
			else if (form.getStatus() == 2)
				dao.addRestriction(Restrictions.isNotNull("thoidiemtrachotctd"));

		}

		// Order by id
		dao.addOrder(Order.desc("ngayCoKetQua"));

		return dao;
	}

	@Override
	public void pushToJa(JSONArray ja, QnaOutFile r, QnaOutFileForm modal) throws Exception {
		ja.put(r.getTenfile());
		ja.put(r.getMaToChucTinDung());
		ja.put(Formater.date2strDateTime(r.getNgayCoKetQua()));
		ja.put(Formater.date2strDateTime(r.getThoidiemtrachotctd()));

		if (!Formater.isNull(r.getTenfile())) {
			String action = "<button type=\"button\" onclick=\"taiFile('" + r.getId() + "')\" "
					+ "style=\"font-size: 10px; padding: 0px 8px; border-radius: 0px; margin: 5px; height: 22px !important; display: initial;\" "
					+ "class=\"btn blue\">T&#7843;i file</button>";
			ja.put(action);
		} else {
			ja.put("");
		}
	}

	@Override
	public String getJsp() {
		return "qna_out_file/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaOutFileForm form)
			throws Exception {
		dsDoiTac = sysPartnerDao.getAll();
		model.addAttribute("dsDoiTac", dsDoiTac);
	}

	@Autowired
	private QnAFtpContext qnaFtpContext;

	public void downloadFileFtp(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, QnaOutFileForm form)
			throws Exception {
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		QnaOutFile qnaOutFile = qnaOutFileDao.getObject(QnaOutFile.class, id);
		String fileName = qnaOutFile.getTenfile();
		String filePath = getDuongDanFileTraLoi(qnaOutFile);
		if (!FTPUtils.existFile(qnaFtpContext.getFtpInf(), filePath)) {
			rp.setContentType("text/html;charset=utf-8");
			PrintWriter pw = rp.getWriter();
			pw.print("File kh&#244;ng t&#7891;n t&#7841;i!");
			pw.flush();
			pw.close();
			return;
		}
		rp.addHeader("Content-Disposition", "attachment; filename=" + fileName);
		OutputStream responseOutputStream = rp.getOutputStream();
		FTPUtils.downloadFile(qnaFtpContext.getFtpInf(), responseOutputStream, filePath);
	}

	private String getDuongDanFileTraLoi(QnaOutFile qnaOutFile) {
		String duongDanFileTraLoi = qnaFtpContext.getFtpQnaFxFld() + "/" + qnaOutFile.getMaToChucTinDung() + "/"
				+ qnaOutFile.getTenfile();
		return duongDanFileTraLoi;
	}
}
