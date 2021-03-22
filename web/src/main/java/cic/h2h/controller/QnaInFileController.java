package cic.h2h.controller;

import java.io.OutputStream;
import java.io.PrintWriter;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import cic.h2h.dao.hibernate.PartnerDao;
import cic.h2h.dao.hibernate.QnaInFileDao;
import cic.h2h.form.QnaInFileForm;
import cic.utils.FTPUtils;
import cic.utils.QnAFtpContext;
import common.util.Formater;
import entity.Partner;
import entity.PartnerBranch;
import entity.QnaInFile;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.PartnerBranchDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/qnaInFile")
public class QnaInFileController extends CatalogController<QnaInFileForm, QnaInFile> {

	private static Logger lg = LogManager.getLogger(QnaInFileController.class);

	@Autowired
	private PartnerDao partnerDao;

	@Autowired
	private QnaInFileDao qnaInFileDao;

	@Autowired
	private PartnerBranchDao partnerBranchDao;
	@Autowired
	private Partner partner;

	@Override
	public BaseDao<QnaInFile> getDao() {
		return qnaInFileDao;
	}

	@Override
	public BaseDao<QnaInFile> createSearchDAO(HttpServletRequest request, QnaInFileForm form) throws Exception {
		QnaInFileDao dao = new QnaInFileDao();
		QnaInFileForm qnaInFileForm = (QnaInFileForm) form;
		if (!Formater.isNull(qnaInFileForm.getKeyword_code()))
			dao.addRestriction(Restrictions.like("branch", qnaInFileForm.getKeyword_code().trim(), MatchMode.ANYWHERE)
					.ignoreCase());
		if (!Formater.isNull(qnaInFileForm.getKeyword_name()))
			dao.addRestriction(Restrictions.like("tenfile", qnaInFileForm.getKeyword_name().trim(), MatchMode.ANYWHERE)
					.ignoreCase());
		if (!Formater.isNull(qnaInFileForm.getFromDate()))
			dao.addRestriction(
					Restrictions.ge("ngayHoi", new SimpleDateFormat("dd/MM/yyyy").parse(qnaInFileForm.getFromDate())));
		if (!Formater.isNull(qnaInFileForm.getToDate())) {
			Date currentDate = new SimpleDateFormat("dd/MM/yyyy").parse(qnaInFileForm.getToDate());
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE, 1);
			Date dateAdd = c.getTime();
			dao.addRestriction(Restrictions.le("ngayHoi", dateAdd));
		}

		if (form.getStatus() != null) {
			// Chua co ket qua
			if (form.getStatus() == 1)
				dao.addRestriction(
						Restrictions.or(Restrictions.isNull("status"), Restrictions.eq("status", Boolean.FALSE)));
			// Da tra cho tctd
//			else if (form.getStatus() == 3)
//				dao.addRestriction(Restrictions.isNotNull("ngayTraLoiCuoiCung"));
			else
				dao.addRestriction(Restrictions.and(Restrictions.isNull("ngayTraLoiCuoiCung"),
						Restrictions.eq("status", Boolean.TRUE)));
		}

		dao.addOrder(Order.desc("ngayHoi"));

		return dao;
	}

	@Override
	public void pushToJa(JSONArray ja, QnaInFile temp, QnaInFileForm modal) throws Exception {
		ja.put(temp.getBranch());
		ja.put(temp.getMasp());
		ja.put(temp.getTenfile());
		ja.put(temp.getUserHoiTin());
		ja.put(Formater.date2ddsmmsyyyspHHmmss(temp.getNgayHoi()));
		// Trang thai
		if (temp.getNgayTraLoiCuoiCung() != null)
			ja.put("&#272;&#227; tr&#7843; cho TCTD");
		else if (temp.getStatus() == null || !temp.getStatus())
			ja.put("Ch&#432;a c&#243; k&#7871;t qu&#7843;");
		else
			ja.put("&#272;&#227; c&#243; k&#7871;t qu&#7843;");
		// Mo ta loi
		ja.put(temp.getDescripton());
		ja.put(Formater.date2ddsmmsyyyspHHmmss(temp.getNgayTraLoiCuoiCung()));
		ja.put(temp.getFileSize() == null ? "" : temp.getFileSize());
		if (!Formater.isNull(temp.getDuongdanfilecauhoi())) {
			String action = "<button type=\"button\" onclick=\"taiFile('" + temp.getId() + "')\" "
					+ "style=\"font-size: 10px; padding: 0px 8px; border-radius: 0px; margin: 5px; height: 22px !important; display: initial;\" "
					+ "class=\"btn blue\">T&#7843;i t&#7879;p h&#7887;i tin</button>";
			if (FTPUtils.existFile(qnaFtpContext.getFtpInf(), getDuongDanFileTraLoi(temp))) {
				action += "<button type=\"button\" onclick=\"taiFileTl('" + temp.getId() + "')\" "
						+ "style=\"font-size: 10px; padding: 0px 8px; border-radius: 0px; margin: 5px; height: 22px !important; display: initial;\" "
						+ "class=\"btn blue\">T&#7843;i file k&#7871;t qu&#7843;</button>";
			}
			if (FTPUtils.existFile(qnaFtpContext.getFtpInf(), getDuongDanFileTraLoiError(temp))) {
				action += "<button type=\"button\" onclick=\"taiFileTlEr('" + temp.getId() + "')\" "
						+ "style=\"font-size: 10px; padding: 0px 8px; border-radius: 0px; margin: 5px; height: 22px !important; display: initial;\" "
						+ "class=\"btn red\">T&#7843;i file l&#7895;i</button>";
			}

			ja.put(action);
		} else {
			ja.put("");
		}
	}

	@Override
	public String getJsp() {
		return "qna_in_file/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaInFileForm form)
			throws Exception {
		List<PartnerBranch> dsDonVi = partnerBranchDao.getBranch(partner);
		model.addAttribute("dsDonVi", dsDonVi);
	}
	
	@Autowired
	ExportExcel exportExcel;
	
	public void ExportFileExcel(ModelMap model, HttpServletRequest request, HttpServletResponse rs, QnaInFileForm form) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String tuNgay = request.getParameter("tuNgay");
		String denNgay = request.getParameter("denNgay");
		String partnerCode = request.getParameter("partnerCode");
		String fileName = request.getParameter("fileName");
		String status = request.getParameter("status");
		List<QnaInFile> list = qnaInFileDao.reports(tuNgay, denNgay, partnerCode, fileName, status);
		for (QnaInFile file : list) {
			if (file.getNgayHoi() != null)
				file.setNgayHoiStr(Formater.date2ddsmmsyyyspHHmmss(file.getNgayHoi()));
			if (file.getNgayTraLoiCuoiCung() != null)
				file.setNgayTraLoiCuoiCungStr(Formater.date2ddsmmsyyyspHHmmss(file.getNgayTraLoiCuoiCung()));
			if (file.getStatus() == null || !file.getStatus()) {
				file.setStatusStr("Chưa có kết quả");
			} else {
				file.setStatusStr("Đã có kết quả");
			}
		}
		map.put("reports", list);
		exportExcel.export("Hoi_tin_file", rs, map);
	}

	public void getTCTD(ModelMap model, HttpServletRequest request, HttpServletResponse response, QnaInFileForm form)
			throws Exception {
		List<Partner> listProvince = partnerDao.getListTCTD("");
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray(new ObjectMapper().writeValueAsString(listProvince));
		out.print(jsonArray);
		out.flush();
		out.close();
	}

	
	@Autowired
	private QnAFtpContext qnaFtpContext;

	public void downloadFileQuestion(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, QnaInFileForm form)
			throws Exception {
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		QnaInFile qnaInFile = qnaInFileDao.getObject(QnaInFile.class, id);
		String fileName = qnaInFile.getTenfile();
		String filePath = qnaInFile.getDuongdanfilecauhoi();
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

	public void downloadFileAnswer(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, QnaInFileForm form)
			throws Exception {
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		QnaInFile qnaInFile = qnaInFileDao.getObject(QnaInFile.class, id);
		String fileName = qnaInFile.getTenfile();
		String filePath = getDuongDanFileTraLoi(qnaInFile);
		if (!FTPUtils.existFile(qnaFtpContext.getFtpInf(), filePath)) {
			rp.setContentType("text/html;charset=utf-8");
			PrintWriter pw = rp.getWriter();
			pw.print("File kh&#244;ng t&#7891;n t&#7841;i!");
			pw.flush();
			pw.close();
			return;
		}
		rp.addHeader("Content-Disposition", "attachment; filename=" + "TL_" + fileName);
		OutputStream responseOutputStream = rp.getOutputStream();
		FTPUtils.downloadFile(qnaFtpContext.getFtpInf(), responseOutputStream, filePath);
	}
	
	public void downloadFileAnswerEr(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, QnaInFileForm form)
			throws Exception {
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		QnaInFile qnaInFile = qnaInFileDao.getObject(QnaInFile.class, id);
		String fileName = qnaInFile.getTenfile();
		String filePath = getDuongDanFileTraLoiError(qnaInFile);
		if (!FTPUtils.existFile(qnaFtpContext.getFtpInf(), filePath)) {
			rp.setContentType("text/html;charset=utf-8");
			PrintWriter pw = rp.getWriter();
			pw.print("File kh&#244;ng t&#7891;n t&#7841;i!");
			pw.flush();
			pw.close();
			return;
		}
		rp.addHeader("Content-Disposition", "attachment; filename=" + "ER_" + fileName);
		OutputStream responseOutputStream = rp.getOutputStream();
		FTPUtils.downloadFile(qnaFtpContext.getFtpInf(), responseOutputStream, filePath);
	}

	private String getDuongDanFileTraLoi(QnaInFile qnaInFile) {
		String duongDanFileTraLoi = qnaFtpContext.getFtpQnaOutFld() + "/" + qnaInFile.getTctd().getCode() + "/TL_"
				+ qnaInFile.getTenfile();
		return duongDanFileTraLoi;
	}
	
	private String getDuongDanFileTraLoiError(QnaInFile qnaInFile) {
		String duongDanFileTraLoi = qnaFtpContext.getFtpQnaOutFld() + "/" + qnaInFile.getTctd().getCode() + "/ER_"
				+ qnaInFile.getTenfile();
		return duongDanFileTraLoi;
	}
}
