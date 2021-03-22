package cic.h2h.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import cic.h2h.dao.hibernate.CatCustomerDAO;
import cic.h2h.dao.hibernate.H2HBaseDao;
import cic.h2h.dao.hibernate.QnaInDetailDao;
import cic.h2h.dao.hibernate.QnaInMasterDao;
import cic.h2h.form.QnaInMasterForm;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import constants.RightConstants;
import entity.CatCustomer;
import entity.Partner;
import entity.QnaInDetail;
import entity.QnaInMaster;
import entity.frwk.SysSecurity;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.dao.hibernate.sys.SysSecurityDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.utils.ApplicationContext;
import frwk.utils.ExportExcel;
import vn.com.cmc.schedule.CicQna;
import vn.org.cic.h2h.ws.endpoint.cicqa.HoiTin;
import vn.org.cic.h2h.ws.endpoint.cicqa.PHHoiTin;
import vn.org.cic.h2h.ws.endpoint.cicqaprod.DSKhachHang;
import vn.org.cic.h2h.ws.endpoint.cicqaprod.Dong;
import vn.org.cic.h2h.ws.endpoint.cicqaprod.TTChung;

@Controller
@RequestMapping("/qnaInMaster")
public class QnaInMasterController extends CatalogController<QnaInMasterForm, QnaInMaster> {

	static Logger lg = LogManager.getLogger(QnaInMasterController.class);

	@Autowired
	private QnaInMasterDao qnaInMasterDao;

	@Autowired
	private SysPartnerDao sysPartnerDao;

	@Autowired
	private SysParamDao sysParamDao;

	@Autowired
	private SysUsersDao sysUsersDao;

	@Autowired
	private CatCustomerDAO catCustomerDao;

	@Autowired
	private CicQna cicQna;

	@Autowired
	private QnaInDetailDao qnaInDetailDao;

	private List<Partner> dsDoiTac;

	@Override
	public BaseDao<QnaInMaster> getDao() {
		return qnaInMasterDao;
	}

	@Override
	public BaseDao<QnaInMaster> createSearchDAO(HttpServletRequest request, QnaInMasterForm form) throws Exception {
		QnaInMasterDao dao = new QnaInMasterDao();
		QnaInMasterForm qnaInMasterForm = (QnaInMasterForm) form;

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		if (!Formater.isNull(qnaInMasterForm.getNgayhoitintu())) {
			dao.addRestriction(Restrictions.ge("thoigianyc", df.parse(qnaInMasterForm.getNgayhoitintu())));
		}
		if (!Formater.isNull(qnaInMasterForm.getNgayhoitinden())) {
			Date currentDate = df.parse(qnaInMasterForm.getNgayhoitinden());
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE, 1);
			Date dateAdd = c.getTime();
			dao.addRestriction(Restrictions.le("thoigianyc", dateAdd));
		}
		// Search by malo
//		if (!Formater.isNull(qnaInMasterForm.getMalo())) {
//			dao.addRestriction(
//					Restrictions.like("mabantin", qnaInMasterForm.getMalo().trim(), MatchMode.ANYWHERE).ignoreCase());
//		}
		// Search by loaisp
		if (!Formater.isNull(qnaInMasterForm.getLoaisp()))
			dao.addRestriction(Restrictions.eq("loaisp", qnaInMasterForm.getLoaisp()));

		// Search by ma tctd
		// if (!Formater.isNull(qnaInMasterForm.getMatctd()))
		// dao.addRestriction(Restrictions.eq("matochuctindung.code",
		// qnaInMasterForm.getMatctd()));

		if (!Formater.isNull(qnaInMasterForm.getMaSoPhieu())) {
			dao.addRestriction(Restrictions.sqlRestriction(
					"exists (select 1 from QNA_IN_OUT_DETAIL d where d.QNA_IN_MS_ID = {alias}.id and lower(d.MSPHIEU) like lower(?) escape '!')",
					"%" + qnaInMasterForm.getMaSoPhieu().trim().replace("_", "!_") + "%", StringType.INSTANCE));
		}

		if (qnaInMasterForm.getHasResult() != null && qnaInMasterForm.getHasResult()) {
			dao.addRestriction(Restrictions.sqlRestriction(
					"exists (select 1 from QNA_IN_OUT_DETAIL d where d.QNA_IN_MS_ID = {alias}.id and NGAYTRALOI is not null)"));
		}
		if (qnaInMasterForm.getHasRealTimeError() != null && qnaInMasterForm.getHasRealTimeError()) {
			dao.addRestriction(Restrictions.isNotNull("numOfErrorCus"));
			dao.addRestriction(Restrictions.ge("numOfErrorCus", Long.valueOf(1l)));
		}

		if (!Formater.isNull(form.getUsername())) {
			dao.addRestriction(Restrictions.eq("userid", form.getUsername()));
		}

		// Order by thoigiannhan
		dao.addOrder(Order.desc("thoigianyc"));

		return dao;
	}

	@Autowired
	ExportExcel exportExcel;

	public void ExportFileExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaInMasterForm form) {
		Map<String, Object> map = new HashMap<String, Object>();
		String tuNgay = rq.getParameter("tuNgay");
		String denNgay = rq.getParameter("denNgay");
		String partnerCode = rq.getParameter("partnerCode");
		String productType = rq.getParameter("productType");
		String maSoPhieu = rq.getParameter("maSoPhieu");
		String hasResult = rq.getParameter("hasResult");
		String username = rq.getParameter("username");
		String hasRealTimeError = rq.getParameter("hasRealTimeError");
		List<QnaInMaster> list = qnaInMasterDao.reports(tuNgay, denNgay, partnerCode, productType, maSoPhieu, hasResult,
				username, hasRealTimeError);
		for (QnaInMaster master : list) {
			if (Formater.isNull(master.getStatus())) {
				master.setStrStatus("ChÆ°a xá»­ lÃ½");
			} else if (master.getStatus() == 0) {
				master.setStrStatus("ChÆ°a xá»­ lÃ½");
			} else if (master.getStatus() == 1) {
				master.setStrStatus("Ä�ang xá»­ lÃ½");
			} else if (master.getStatus() == 2) {
				master.setStrStatus("Ä�Ã£ cÃ³ káº¿t quáº£");
			} else if (master.getStatus() == 3) {
				master.setStrStatus("HoÃ n thÃ nh");
			}
			if (!Short.valueOf((short) 3).equals(master.getStatus()))
				qnaInMasterDao.updateSts(master);
		}
		map.put("reports", list);
		exportExcel.export("Theo_doi_hoi_tin", rs, map);
	}

	@Override
	public void pushToJa(JSONArray ja, QnaInMaster r, QnaInMasterForm modal) throws Exception {
		ja.put(r.getLoaisp());
		ja.put(Formater.date2ddsmmsyyyspHHmmss(r.getThoigianyc()));

		if (!Short.valueOf((short) 3).equals(r.getStatus()))
			qnaInMasterDao.updateSts(r);

		switch (r.getStatus().intValue()) {
		case 0:
			ja.put("Ch&#432;a c&#243; k&#7871;t qu&#7843");
			break;
		case 2:
			ja.put("Ho&#224;n th&#224;nh");
			break;
		default:
			ja.put("&#272;&#227; c&#243; k&#7871;t qu&#7843");
			break;

		}
		ja.put(FormatNumber.num2Str(r.getTongSoPhieu()));
		ja.put(r.getMaLoi());
		ja.put(r.getMoTaLoi());
		// ja.put(r.getTongSoPhieu());
		// sl khach hang
//		String viewDetail = "";
//		if (r.getNumOfErrorCus() != null && r.getNumOfErrorCus() > 0 &&)
//			viewDetail = "<a href='#' onclick = \"v_CusErrorDescription ='"+ StringEscapeUtils.escapeHtml(r.getCusErrorDescription())+"'; viewClob(v_CusErrorDescription)\">" + "S&#7889; l&#432;&#7907;ng kh&#225;ch h&#224;ng l&#7895;i: " + (r.getNumOfErrorCus() == null ? "" : FormatNumber.num2Str(r.getNumOfErrorCus())) + "</a>" ;
//		else
//			viewDetail = "S&#7889; l&#432;&#7907;ng kh&#225;ch h&#224;ng l&#7895;i: ";
		ja.put("S&#7889; l&#432;&#7907;ng kh&#225;ch h&#224;ng l&#7895;i: " + r.getNumOfErrorCus()
				+ "<br/>S&#7889; l&#432;&#7907;ng kh&#225;ch h&#224;ng th&#224;nh c&#244;ng: " + r.getTongSoPhieu()
				+ "<br/>S&#7889; l&#432;&#7907;ng kh&#225;ch h&#224;ng &#273;&#227; c&#243; k&#7871;t qu&#7843;: "
				+ r.getDaCoKetQua()
				+ "<br/>S&#7889; l&#432;&#7907;ng kh&#225;ch h&#224;ng &#273;&#227; tr&#7843; k&#7871;t qu&#7843;: "
				+ r.getDaTra());
		String action = "";
		if (Formater.isNull(modal.getMaSoPhieu())) //
			action = "<a href='qnaInDetail?qnaInMsId=" + r.getId()
					+ "' class=\" btn blue\" style=\"font-size: 10px;padding: 4px 8px;border-radius: 0px;\">Xem chi ti&#7871;t</a>";
		else
			action = "<a href='qnaInDetail?qnaInMsId=" + r.getId() + "&maphieu=" + modal.getMaSoPhieu().trim()
					+ "' class=\" btn blue\" style=\"font-size: 10px;padding: 4px 8px;border-radius: 0px;\">Xem chi ti&#7871;t</a>";
		ja.put(action);

	}

	@Override
	public String getJsp() {
		return "qna_in_master/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaInMasterForm form)
			throws Exception {
		dsDoiTac = sysPartnerDao.getAll();
		model.addAttribute("dsDoiTac", dsDoiTac);
		List<CatCustomer> lstCustomer = new ArrayList<CatCustomer>();
		lstCustomer = new H2HBaseDao<CatCustomer>().getAll(CatCustomer.class);
		model.addAttribute("lstCustomer", lstCustomer);
		// show popup notifile if pw date <= 10
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		if (appContext != null) {
			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
			lg.info(user);
			String day = expiredPassWordDate(user.getPwdDate());
			model.addAttribute("expriredDay", day);
		}

		List<SysUsers> sysUsers = sysUsersDao.getAll(SysUsers.class);
		model.addAttribute("sysUsers", sysUsers);
//		model.addAttribute("dsSanPham", dsSanPham);
	}

	@Autowired
	private SysSecurityDao sysSecurityDao;

	private String expiredPassWordDate(Date pwDate) {
		List<SysSecurity> expriredDays = new H2HBaseDao<SysSecurity>().getAll(SysSecurity.class);
		String expriredIn = null, expriredDay = null;
		Date date = new Date();
		if (!Formater.isNull(expriredDays)) {
			for (SysSecurity item : expriredDays) {
				if (RightConstants.PW_EXPIRED_DAY.equalsIgnoreCase(item.getCode())) {
					expriredDay = item.getValue();
					continue;
				}
				if (RightConstants.PW_EXPIRED_IN.equalsIgnoreCase(item.getCode())) {
					expriredIn = item.getValue();
					continue;
				}
			}
			Calendar c = Calendar.getInstance();
			c.setTime(pwDate);
			c.add(Calendar.DATE, Integer.parseInt(expriredIn));
			long time = 24 * 60 * 60 * 1000;
			long diff = 0;
			if (c.getTime().after(date)) {
				diff = (c.getTime().getTime() - date.getTime()) / time;
				if (diff <= Long.parseLong(expriredDay)) {
					return String.valueOf(diff);
				}
				return "";
			} else {
				return "";
			}
		}
		return "";
	}

	public void redo(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaInMasterForm form) {
		qnaInMasterDao.redo(rq.getParameter("qnaInMsId"));
	}

	public void getTree(ModelMap model, HttpServletRequest request, HttpServletResponse response, QnaInMasterForm form)
			throws Exception {
		String id = request.getParameter("id");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		JSONArray array = null;
		array = qnaInMasterDao.getTreeRoot(id);
		pw.print(array);
		pw.close();
	}

	public void loadDataCustomer(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QnaInMasterForm form) throws Exception {
		String id = request.getParameter("id");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		JSONObject jsonObject = new JSONObject(
				new ObjectMapper().writeValueAsString(catCustomerDao.get(CatCustomer.class, id)));
		pw.print(jsonObject);
		pw.close();
	}

	public void retryCallCic(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaInMasterForm form)
			throws Exception {
		String qnaInMsId = rq.getParameter("qnaInMsId");
		String str = "Th&#7921;c hi&#7879;n th&#224;nh c&#244;ng";
		try {
			List<QnaInMaster> lst = new ArrayList<QnaInMaster>();
			lst.add(qnaInMasterDao.get(QnaInMaster.class, qnaInMsId));
			//cicQna.getDataJobHoiTin(lst);
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

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaInMasterForm form)
			throws Exception {
		for (QnaInDetail d : form.getQnaInMaster().getQnaInDetailArrayList()) {
			if (Formater.isNull(d.getId())) {
				d.setId(null);
				d.setQnaInMsId(form.getQnaInMaster());
			}
		}
		form.getQnaInMaster().getQnaInDetails().addAll(form.getQnaInMaster().getQnaInDetailArrayList());
		super.save(model, rq, rs, form);
	}
}
