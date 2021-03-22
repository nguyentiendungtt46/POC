package cic.h2h.controller;

import java.io.IOException;
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

import cic.h2h.dao.hibernate.H2hEmailDao;
import cic.h2h.dao.hibernate.PartnerDao;
import cic.h2h.form.H2hEmailForm;
import common.util.Formater;
import constants.RightConstants;
import entity.H2hEmail;
import entity.Partner;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/h2hEmail")
public class H2hEmailController extends CatalogController<H2hEmailForm, H2hEmail> {

	static Logger lg = LogManager.getLogger(H2hEmailController.class);
	
	@Autowired
	private H2hEmailDao h2hEmailDao;
	
	@Autowired
	private PartnerDao partnerDao;

	@Override
	public BaseDao<H2hEmail> createSearchDAO(HttpServletRequest request, H2hEmailForm form) throws Exception {
		H2hEmailDao dao = new H2hEmailDao();
		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

		if (!Formater.isNull(form.getFormDate())) {
			dao.addRestriction(Restrictions.ge("sendTime", df.parse(form.getFormDate())));
		}
		if (!Formater.isNull(form.getToDate())) {
			Date currentDate = df.parse(form.getToDate());
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE, 1);
			Date dateAdd = c.getTime();
			dao.addRestriction(Restrictions.le("sendTime", dateAdd));
		}
		
		// Search by trang thai
		if (!Formater.isNull(form.getStatus()))
			dao.addRestriction(Restrictions.eq("status", form.getStatus()));
		
		if (!Formater.isNull(form.getType()))
			dao.addRestriction(Restrictions.eq("type", form.getType()));
		
		if (!Formater.isNull(form.getEmail()))
			dao.addRestriction(
					Restrictions.like("emailTo", form.getEmail().trim(), MatchMode.ANYWHERE).ignoreCase());
		
		if (!Formater.isNull(form.getParentId()))
			dao.addRestriction(Restrictions.eq("tctdId", form.getParentId()));
		
		dao.addOrder(Order.desc("createTime"));
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, H2hEmail r, H2hEmailForm modelForm) throws Exception {
		if(Formater.isNull(r.getTctdId())) {
			ja.put("");
			ja.put("");
		}else {
			Partner parent = partnerDao.getPartnerById(r.getTctdId());
			ja.put(parent.getCode());
			ja.put(parent.getName());
		}
		ja.put(r.getEmailSubject());
		ja.put(r.getEmailTo());
		switch (r.getType()) {
		case 0:
			ja.put("T&#7921; &#273;&#7897;ng");
			break;
		case 1:
			ja.put("Th&#7911; c&#244;ng");
			break;
		}
		
		switch (r.getStatus()) {
		case 0:
			ja.put("Ch&#432;a g&#7917;i");
			break;
		case 1:
			ja.put("&#272;&#227; g&#7917;i");
			break;
		case 2:
			ja.put("Th&#7853;t b&#7841;i");
			break;
		}
		ja.put(Formater.date2ddsmmsyyyspHHmmss(r.getSendTime()));
		ja.put("<a data-toggle=\"modal\" data-target=\"#myModal\" href = '#' onclick = 'chiTietEmail(\""
				+ r.getId() + "\")'>Chi ti&#7871;t</a>");
	}

	@Override
	public BaseDao<H2hEmail> getDao() {
		return h2hEmailDao;
	}

	@Override
	public String getJsp() {
		return "h2h_email/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, H2hEmailForm form)
			throws Exception {
		List<Partner> listParent = partnerDao.getListTCTD("");
		model.addAttribute("listParent", listParent);
	}
	
	public void loadEmailBody(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, H2hEmailForm form)
			throws IOException {
		rp.setContentType("text/plan;charset=utf-8");
		PrintWriter out = rp.getWriter();
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		H2hEmail h2hEmail = null;
		try {
			h2hEmail = h2hEmailDao.getById(id);
		} catch (Exception e) {
			lg.error(e);
		}
		
		out.println(Formater.isNull(h2hEmail.getEmailBody()) ? RightConstants.QNAINDETAIL_NULL_DATA: h2hEmail.getEmailBody());
		out.flush();
		out.close();
	}
	
	@Autowired
	private ExportExcel exportExcel;
	
	public void exportFileExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, H2hEmailForm form)
			throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String fromDate = rq.getParameter("formDate");
			String toDate = rq.getParameter("toDate");
			String partnerId = rq.getParameter("partnerId");
			String email = rq.getParameter("email");
			String type = rq.getParameter("type");
			String status = rq.getParameter("status");
			
			List<H2hEmail> list = h2hEmailDao.reports(fromDate, toDate, partnerId, email, type, status);
			Partner partner = null;
			if (!Formater.isNull(partnerId)) {
				partner = partnerDao.getObject(Partner.class, partnerId);
			}
			for (H2hEmail item : list) {
				if (item.getSendTime() != null)
					item.setSendTimeStr(Formater.date2ddsmmsyyyspHHmmss(item.getSendTime()));
				if (item.getStatus() != null) {
					if (0 == item.getStatus()) {
						item.setStatusStr("Chưa gửi");
					} else if (1 == item.getStatus()) {
						item.setStatusStr("Đã gửi");
					} else if (2 == item.getStatus()) {
						item.setStatusStr("Gửi thất bại");
					}
				} else {
					item.setStatusStr("Chưa gửi");
				}
				if (item.getType() == null || 0 == item.getType()) {
					item.setTypeStr("Tự động");
				} else {
					item.setTypeStr("Thủ công");
				}
				if (partner != null) {
					item.setPartnerCode(partner.getCode());
					item.setPartnerName(partner.getName());
				} else {
					partner = partnerDao.get(Partner.class, item.getTctdId());
					if (partner != null) {
						item.setPartnerCode(partner.getCode());
						item.setPartnerName(partner.getName());
					}
				}
			}
			map.put("fromDate", fromDate);
			map.put("toDate", toDate);
			if (partner != null)
				map.put("partnerCode", partner.getCode() + " - " + partner.getName());
			map.put("mail", email);
			if (!Formater.isNull(type)) {
				if ("1".equals(type))
					map.put("type", "Th\u1EE7 c\u00F4ng");
				else map.put("type", "T\u1EF1 \u0111\u1ED9ng");
			}
			if (!Formater.isNull(status)) {
				if ("0".equals(status))
					map.put("status", "Ch\u01B0a g\u1EEDi");
				else if ("1".equals(status))
					map.put("status", "\u0110\u00E3 g\u1EEDi");
				else map.put("status", "Th\u1EADt b\u1EA1i");
			}
			map.put("reports", list);
			exportExcel.export("Giam_sat_gui_email", rs, map);
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
		}
	}
}
