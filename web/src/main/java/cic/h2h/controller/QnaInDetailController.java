package cic.h2h.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
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

import cic.h2h.dao.hibernate.CatProductDao;
import cic.h2h.dao.hibernate.PartnerDao;
import cic.h2h.dao.hibernate.QnaInDetailDao;
import cic.h2h.dao.hibernate.QnaInDetailErDao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import cic.h2h.dao.hibernate.ServiceProductDao;
import cic.h2h.form.QnaInDetailForm;
import common.util.Formater;
import constants.RightConstants;
import dto.ExcelParamDTO;
import entity.CatProduct;
import entity.Partner;
import entity.PartnerBranch;
import entity.QnaInDetail;
import entity.QnaInDetailEr;
import entity.ServiceProduct;
import entity.frwk.SysUsers;
import frwk.constants.Constants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.EscapingLikeExpression;
import frwk.dao.hibernate.sys.PartnerBranchDao;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.utils.ExportExcel;
import intergration.cic.QuestionAnswer;
import vn.com.cmc.schedule.CicQna;

@Controller
@RequestMapping("/qnaInDetail")
public class QnaInDetailController extends CatalogController<QnaInDetailForm, QnaInDetail> {

	static Logger lg = LogManager.getLogger(QnaInDetailController.class);

	@Autowired
	private QnaInDetailDao qnaInDetailDao;

	@Autowired
	private SysPartnerDao sysPartnerDao;

	@Autowired
	ExportExcel exportExcel;

	@Autowired
	QuestionAnswer questionAnswer;

	@Autowired
	QnaInDetailErDao qnaInDetailErDao;
	private List<Partner> dsDoiTac;

	@Override
	public BaseDao<QnaInDetail> getDao() {
		return qnaInDetailDao;
	}

	@Override
	public BaseDao<QnaInDetail> createSearchDAO(HttpServletRequest request, QnaInDetailForm form) throws Exception {
		QnaInDetailDao dao = new QnaInDetailDao();
		QnaInDetailForm qnaInDetailForm = (QnaInDetailForm) form;
		if (!Formater.isNull(qnaInDetailForm.getBranch())) {
			dao.addRestriction(Restrictions.eq("branch", qnaInDetailForm.getBranch()));
		}
		// code
		if (!Formater.isNull(qnaInDetailForm.getQnaInMsId())) {
			dao.addRestriction(Restrictions.eq("qnaInMsId.id", qnaInDetailForm.getQnaInMsId()));
		}

		if (!Formater.isNull(qnaInDetailForm.getNgaytaotu())) {
			dao.addRestriction(Restrictions.ge("ngaytao", Formater.str2date(qnaInDetailForm.getNgaytaotu())));
		}
		if (!Formater.isNull(qnaInDetailForm.getNgaytaoden())) {
			Date currentDate = Formater.str2date(qnaInDetailForm.getNgaytaoden());
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE, 1);
			Date dateAdd = c.getTime();
			dao.addRestriction(Restrictions.le("ngaytao", dateAdd));
		}
		// Search by malo
//		if (!Formater.isNull(qnaInDetailForm.getMalo())) {
//			QnaInMaster qnaInMaster = qnaInMasterDao.getQnaInMasterByMalo(qnaInDetailForm.getMalo());
//			dao.addRestriction(Restrictions.like("qnaInMsId", qnaInMaster.getId(), MatchMode.ANYWHERE).ignoreCase());
//		}
		// Search by maphieu
		if (!Formater.isNull(qnaInDetailForm.getMaphieu())) {

			EscapingLikeExpression likeExpression = new EscapingLikeExpression("msphieu",
					qnaInDetailForm.getMaphieu().trim(), MatchMode.ANYWHERE, Boolean.TRUE);
			dao.addRestriction(likeExpression);

		}

		// Search by macic
		if (!Formater.isNull(qnaInDetailForm.getMacic())) {
			EscapingLikeExpression likeExpression = new EscapingLikeExpression("macic",
					qnaInDetailForm.getMacic().trim(), MatchMode.ANYWHERE, Boolean.TRUE);
			dao.addRestriction(likeExpression);
		}
		// Search by makh
		if (!Formater.isNull(qnaInDetailForm.getMakh())) {
			EscapingLikeExpression likeExpression = new EscapingLikeExpression("makh", qnaInDetailForm.getMakh().trim(),
					MatchMode.ANYWHERE, Boolean.TRUE);
			dao.addRestriction(likeExpression);
		}
		// Search by tenkh
		if (!Formater.isNull(qnaInDetailForm.getTenkh())) {
			EscapingLikeExpression likeExpression = new EscapingLikeExpression("tenkh",
					qnaInDetailForm.getTenkh().trim(), MatchMode.ANYWHERE, Boolean.TRUE);
			dao.addRestriction(likeExpression);
		}
		// Search by socmnd
		if (!Formater.isNull(qnaInDetailForm.getSocmnd())) {
//			EscapingLikeExpression likeExpression = new EscapingLikeExpression("socmt",
//					qnaInDetailForm.getSocmnd().trim(), MatchMode.ANYWHERE, Boolean.TRUE);
			dao.addRestriction(
					Restrictions.or(Restrictions.like("socmt", qnaInDetailForm.getSocmnd().trim(), MatchMode.ANYWHERE),
							Restrictions.like("dkkd", qnaInDetailForm.getSocmnd().trim(), MatchMode.ANYWHERE)));

		}
		// Search by masothue
		if (!Formater.isNull(qnaInDetailForm.getMasothue())) {

			EscapingLikeExpression likeExpression = new EscapingLikeExpression("msthue",
					qnaInDetailForm.getMasothue().trim(), MatchMode.ANYWHERE, Boolean.TRUE);
			dao.addRestriction(likeExpression);
		}

		// Search by ma tctd
		if (!Formater.isNull(qnaInDetailForm.getMatctd()))
			dao.addRestriction(Restrictions.eq("matctd.code", qnaInDetailForm.getMatctd()));

		if (qnaInDetailForm.getHasResult() != null && qnaInDetailForm.getHasResult()) {
			dao.addRestriction(Restrictions.isNotNull("noidungtraloi"));
		}
		if (qnaInDetailForm.getCicErr() != null && qnaInDetailForm.getCicErr()) {
			dao.addRestriction(Restrictions.eq("status", Short.valueOf((short) 4)));
		}

//		if (!Formater.isNull(qnaInDetailForm.getPartnerId())) {
//			Partner partner = sysPartnerDao.get(Partner.class, qnaInDetailForm.getPartnerId());
//			dao.addRestriction(Restrictions.sqlRestriction("matctd='" + partner.getCode() + "'"));
//		}

		// Order by id
		dao.addOrder(Order.desc("ngaytao"));

		return dao;
	}

	@Autowired
	private SysUsersDao sysUsersDao;

	@Override
	public void pushToJa(JSONArray ja, QnaInDetail r, QnaInDetailForm modal) throws Exception {
//		if (Formater.isNull(modal.getQnaInMsId()))
//			ja.put(r.getQnaInMsId().getMatochuctindung().getName());
		ja.put("<span class='characterwrap'>" + r.getMsphieu() + "</span>");
		// ja.put("<a href = '#' onclick = 'edit(\"" + r.getId() + "\")'>" +
		// r.getMsphieu() + "</a>");
		ja.put(r.getMakh() + " - " + r.getTenkh());// ma kh - ten kh
		ja.put(r.getMacic());// ma cic
		// ja.put(r.getMsthue());// ma so thue
		// ja.put(r.getDkkd());// so dang ky kinh doanh
		// so cmtnd
		if (!Formater.isNull(r.getSocmt())) {
			ja.put(r.getSocmt());
		} else {
			ja.put(r.getDkkd());
		}
		ja.put(r.getDiachi());
		if (!Formater.isNull(r.getBranch())) {
			PartnerBranch br = partnerBranchDao.get(PartnerBranch.class, r.getBranch());
			if (br != null)
				ja.put(br.getCode());
			else {
				ja.put("");
			}
		} else {
			ja.put("");
		}

		if (!Formater.isNull(r.getNguoihoi())) {
			SysUsers su = sysUsersDao.get(SysUsers.class, r.getNguoihoi());
			if (su != null)
				ja.put(su.getUsername());
			else {
				ja.put("");
			}
		} else {
			ja.put("");
		}

		ja.put(Formater.date2ddsmmsyyyspHHmmss(r.getNgaytao()));
		ja.put(Formater.date2ddsmmsyyyspHHmmss(r.getThoiDiemGuiVaoCoreCIC()));
		if(r.getError() == null || r.getError()) {
			ja.put("L&#7895;i Input");
			ja.put("");
			ja.put("<a  href = '#' onclick = 'chiTietLoiInput(\""
					+ r.getId() + "\")'>Chi ti&#7871;t l&#7895;i</a>");
		}else if (Formater.isNull(r.getNoidungtraloi())) {
			ja.put("Ch&#432;a c&#243; k&#7871;t qu&#7843;");// chua co ket qua
			ja.put(StringEscapeUtils.escapeHtml(r.getThoidiemtraloidoitac() == null ? ""
					: Formater.date2ddsmmsyyyspHHmmss(r.getThoidiemtraloidoitac())));
			ja.put("");
		} else {
			if (r.getThoidiemtraloidoitac() == null) {// da co ket qua
				if (r.getStatus() != null && r.getStatus() == 4) {
					ja.put("L&#7895;i t&#7841;i CIC - &#272;&#227; c&#243; k&#7871;t qu&#7843;");
					ja.put(StringEscapeUtils.escapeHtml(r.getThoidiemtraloidoitac() == null ? ""
							: Formater.date2ddsmmsyyyspHHmmss(r.getThoidiemtraloidoitac())));
					ja.put("<a data-toggle=\"modal\" data-target=\"#myModal\" href = '#' onclick = 'chiTietCauTraLoi(\""
							+ r.getId() + "\")'>Chi ti&#7871;t l&#7895;i</a>");
				}

				else {
					ja.put("&#272;&#227; c&#243; k&#7871;t qu&#7843;");
					ja.put(StringEscapeUtils.escapeHtml(r.getThoidiemtraloidoitac() == null ? ""
							: Formater.date2ddsmmsyyyspHHmmss(r.getThoidiemtraloidoitac())));
					ja.put("<a data-toggle=\"modal\" data-target=\"#myModal\" href = '#' onclick = 'chiTietCauTraLoi(\""
							+ r.getId() + "\")'>Chi ti&#7871;t c&#226;u tr&#7843; l&#7901;i</a>");
					ja.put("");
				}

			} else if (r.getThoidiemtraloidoitac() != null) {// da tra cho tctd
				if (r.getStatus() != null && r.getStatus() == 4) {
					ja.put("L&#7895;i t&#7841;i CIC - CIC &#273;&#227; tr&#7843;");
					ja.put(StringEscapeUtils.escapeHtml(r.getThoidiemtraloidoitac() == null ? ""
							: Formater.date2ddsmmsyyyspHHmmss(r.getThoidiemtraloidoitac())));
					ja.put("<a data-toggle=\"modal\" data-target=\"#myModal\" href = '#' onclick = 'chiTietCauTraLoi(\""
							+ r.getId() + "\")'>Chi ti&#7871;t l&#7895;i</a>");
				} else {
					ja.put("CIC &#273;&#227; tr&#7843;");
					ja.put(StringEscapeUtils.escapeHtml(r.getThoidiemtraloidoitac() == null ? ""
							: Formater.date2ddsmmsyyyspHHmmss(r.getThoidiemtraloidoitac())));
					ja.put("<a data-toggle=\"modal\" data-target=\"#myModal\" href = '#' onclick = 'chiTietCauTraLoi(\""
							+ r.getId() + "\")'>Chi ti&#7871;t c&#226;u tr&#7843; l&#7901;i</a>");
				}

			}
		}
		// ja.put("<input type=\"checkbox\" name=\"qnaInDetails\" value=\"" +
		// r.getMsphieu() + "\"/>");
//		if (r.getTudong() != null) {
//			if (r.getTudong() != null && r.getTudong())
//				ja.put("T&#7921; &#273;&#7897;ng");
//			else
//				ja.put("Th&#7911; c&#244;ng");
//		} else
//			ja.put("");
	}

	@Override
	public String getJsp() {
		return "qna_in_detail/view";
	}

	// private static final Map<String, String> phapNhanMap = new HashMap<String,
	// String>();

	@Autowired
	private ServiceProductDao serviceProductDao;

	@Autowired
	private ServiceInfoDao serviceInfoDao;
	@Autowired
	private PartnerBranchDao partnerBranchDao;
	@Autowired
	private Partner partner;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaInDetailForm form)
			throws Exception {
		if (Formater.isNull(form.getQnaInMsId())) {
			dsDoiTac = sysPartnerDao.getAll();
			model.addAttribute("dsDoiTac", dsDoiTac);
		}
		List<ServiceProduct> list = serviceProductDao
				.getListbyServiceId(serviceInfoDao.getServiceInFo(Constants.QA_CODE).getId());
		model.addAttribute("lstServicePro", list);
		List<PartnerBranch> dsDonVi = partnerBranchDao.getBranch(partner);
		model.addAttribute("dsDonVi", dsDonVi);
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaInDetailForm form)
			throws Exception {
		QnaInDetail qnaInDetail = form.getQnaInDetail();
		qnaInDetail.setNgaytao(Calendar.getInstance().getTime());
		SysUsers su = getSessionUser();
		qnaInDetail.setNguoihoi(su.getId());
		qnaInDetail.setBranch(su.getDepartmentId());
		if (Formater.isNull(form.getQnaInDetail().getId())) {
			form.getQnaInDetail().setId(null);
			CatProduct catProduct = catProductDao.get(CatProduct.class,
					form.getQnaInDetail().getServiceProduct().getId());
			form.getQnaInDetail().setMsphieu(catProduct.getCode() + Calendar.getInstance().getTimeInMillis());
			if (catProduct.getTypeProduct() != null && catProduct.getTypeProduct() == (short) 1) {
				String dkkd = form.getQnaInDetail().getSocmt();
				form.getQnaInDetail().setDkkd(dkkd);
				form.getQnaInDetail().setSocmt("");
			}
		}
		super.save(model, rq, rs, form);
		if (!Formater.isNull(form.getQnaInDetail().getId())) {
			returnObject(rs, form.getQnaInDetail());
		}
	}

	@Autowired
	private CatProductDao catProductDao;

	public void loadQuesAndAns(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, QnaInDetailForm form)
			throws IOException {
		rp.setContentType("text/plan;charset=utf-8");
		PrintWriter out = rp.getWriter();
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		String type = Formater.isNull(rq.getParameter("type")) ? "" : rq.getParameter("type");
		QnaInDetail qnaInDetail = null;
		try {
			qnaInDetail = qnaInDetailDao.getQuesAndAnswerById(id);
		} catch (Exception e) {
			lg.error(e);
		}
		if (type.equals(RightConstants.QNAINDETAIL_QUESTION)) {
			out.println(Formater.isNull(qnaInDetail.getNoidungcauhoi()) ? RightConstants.QNAINDETAIL_NULL_DATA
					: qnaInDetail.getNoidungcauhoi());
		} else if (type.equals(RightConstants.QNAINDETAIL_ANSWER)) {
			out.println(Formater.isNull(qnaInDetail.getNoidungtraloi()) ? RightConstants.QNAINDETAIL_NULL_DATA
					: qnaInDetail.getNoidungtraloi());
		}
		out.flush();
		out.close();
	}

	public void ExportFileExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaInDetailForm form)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String qnaInMsId = rq.getParameter("qnaInMsId");
		String partnerId = rq.getParameter("partnerId");
		String thoigiantraloitu = rq.getParameter("thoigiantraloitu");
		String thoigiantraloiden = rq.getParameter("thoigiantraloiden");
		String maphieu = rq.getParameter("maphieu");
		String macic = rq.getParameter("macic");
		String makh = rq.getParameter("makh");
		String tenkh = rq.getParameter("tenkh");
		String socmnd = rq.getParameter("socmnd");
		String masothue = rq.getParameter("masothue");
		String hasResult = rq.getParameter("hasResult");
		String cicErr = rq.getParameter("cicErr");

		List<QnaInDetail> LstQnaInDetail = qnaInDetailDao.reports(qnaInMsId, partnerId, thoigiantraloitu,
				thoigiantraloiden, maphieu, macic, makh, tenkh, socmnd, masothue, hasResult, cicErr);
		for (QnaInDetail qna : LstQnaInDetail) {

			qna.setThoidiemtraloidoitacStr(StringEscapeUtils.escapeHtml(qna.getThoidiemtraloidoitac() == null ? ""
					: Formater.date2ddsmmsyyyspHHmmss(qna.getThoidiemtraloidoitac())));

			qna.setThoiDiemGuiVaoCoreCICStr(StringEscapeUtils.escapeHtml(qna.getThoiDiemGuiVaoCoreCIC() == null ? ""
					: Formater.date2ddsmmsyyyspHHmmss(qna.getThoiDiemGuiVaoCoreCIC())));

			if (Formater.isNull(qna.getNoidungtraloi())) {
				qna.setStatusStr("Ch\u01B0a c\u00F3 k\u1EBFt qu\u1EA3");
			} else {
				if (qna.getThoidiemtraloidoitac() == null) {// da co ket qua
					if (qna.getStatus() != null && qna.getStatus() == 4) {
						qna.setStatusStr("L\u1ED7i t\u1EA1i CIC - \u0110\u00E3 c\u00F3 k\u1EBFt qu\u1EA3");
					} else {
						qna.setStatusStr("\u0110\u00E3 c\u00F3 k\u1EBFt qu\u1EA3");
					}

				} else if (qna.getThoidiemtraloidoitac() != null) {// da tra cho tctd
					if (qna.getStatus() != null && qna.getStatus() == 4) {
						qna.setStatusStr("L\u1ED7i t\u1EA1i CIC - \u0110\u00E3 tr\u1EA3 cho TCTD");
					} else {
						qna.setStatusStr("\u0110\u00E3 tr\u1EA3 cho TCTD");
					}
				}
			}
			if (qna.getTudong() != null) {
				if (qna.getTudong() != null && qna.getTudong()) {
					qna.setTudongStr("T\u1EF1 \u0111\u1ED9ng");
				} else {
					qna.setTudongStr("Th\u1EE7 c\u00F4ng");
				}
			} else {
				qna.setTudongStr("");
			}

		}
		// map.put("arg0", arg1);
		ExcelParamDTO param = new ExcelParamDTO();
		if (Formater.isNull(partnerId))
			param.setParam1("T\u1EA5t c\u1EA3");
		else {
			Partner obj = partnerDao.getObject(Partner.class, partnerId);
			param.setParam1(obj.getCode() + " - " + obj.getName());
		}

		param.setParam2(thoigiantraloitu);
		param.setParam3(thoigiantraloiden);
		param.setParam4(maphieu);
		param.setParam5(macic);
		param.setParam6(makh);
		param.setParam7(tenkh);
		param.setParam8(socmnd);
		param.setParam9(masothue);
		if (hasResult.equals("1"))
			param.setParam10("\u0110\u00E3 c\u00F3 k\u1EBFt qu\u1EA3");
		else
			param.setParam10("Ch\u01B0a c\u00F3 k\u1EBFt qu\u1EA3");
		if (cicErr.equals("1"))
			param.setParam11("C\u00F3");
		else
			param.setParam11("Kh\u00F4ng");
		map.put("param", param);
		map.put("reports", LstQnaInDetail);
		exportExcel.export("Chi_tiet_hoi_tin", rs, map);
	}

	@Autowired
	private PartnerDao partnerDao;
	@Autowired
	private CicQna cicQna;

	public void callServiceHoiTin(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaInDetailForm form)
			throws Exception {
		String maSP = rq.getParameter("qnaMaSP");
		String str = "Th&#7921;c hi&#7879;n th&#224;nh c&#244;ng";
		try {
			//cicQna.getDataJobHoiTin(maSP);
		} catch (Exception e) {
			lg.info(e);
			str = "Th&#7921;c hi&#7879;n kh&#244;ng th&#224;nh c&#244;ng";
		}
		rs.setContentType("text/html;charset=utf-8");
		PrintWriter pw = rs.getWriter();
		pw.print(str);
		pw.flush();
		pw.close();
	}

	public void callServiceDsVanTin(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaInDetailForm form)
			throws IOException {
		String str = "Th&#7921;c hi&#7879;n th&#224;nh c&#244;ng";
		try {
			//cicQna.getDataJobVanTinDsPhieu();
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
			str = "Th&#7921;c hi&#7879;n kh&#244;ng th&#224;nh c&#244;ng";
		}
		rs.setContentType("text/html;charset=utf-8");
		PrintWriter pw = rs.getWriter();
		pw.print(str);
		pw.flush();
		pw.close();
	}
	
	public void loadQnaDetailsError(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			QnaInDetailForm form) {
		try {
			String qnaInOutDetailsId = rq.getParameter("qnaInOutDetailsId");
			rs.setContentType("text/plan;charset=utf-8");
			List<QnaInDetailEr> lst = qnaInDetailErDao.findByDetailsId(qnaInOutDetailsId);
			PrintWriter pw = rs.getWriter();
			JSONArray array = null;
			array = new JSONArray(new ObjectMapper().writeValueAsString(lst));
			pw.print(array);
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e);
		}
	}
}
