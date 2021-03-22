package cic.h2h.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import cic.h2h.dao.hibernate.LogCheckConnectDao;
import cic.h2h.dao.hibernate.LogCoreServicesDao;
import cic.h2h.dao.hibernate.PartnerDao;
import cic.h2h.form.LogCheckConnectForm;
import cic.h2h.form.LogCoreServicesForm;
import common.util.Formater;
import entity.LogCheckConnect;
import entity.LogCoreService;
import entity.Partner;
import entity.ServiceInfo;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/logCheckConnect")
public class LogCheckConnectController extends CatalogController<LogCheckConnectForm, LogCheckConnect> {

	static Logger lg = LogManager.getLogger(LogCheckConnectController.class);
	
	@Autowired
	private LogCheckConnectDao logCheckConnectDao;
	
	@Autowired
	private PartnerDao partnerDao;

	@Override
	public BaseDao<LogCheckConnect> createSearchDAO(HttpServletRequest request, LogCheckConnectForm form)
			throws Exception {
		LogCheckConnectDao dao = new LogCheckConnectDao();
		
		if (!Formater.isNull(form.getParentId()))
			dao.addRestriction(Restrictions.eq("partnerId.id", form.getParentId()));
		
		// Search by trang thai
		if (!Formater.isNull(form.getStatus()))
			dao.addRestriction(Restrictions.eq("connectResult", form.getStatus()));
		
		if (!Formater.isNull(form.getIpAddres()))
			dao.addRestriction(Restrictions.like("ipAddress", form.getIpAddres().trim(), MatchMode.ANYWHERE).ignoreCase());
		
		dao.addOrder(Order.desc("createDate"));
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, LogCheckConnect r, LogCheckConnectForm modelForm) throws Exception {
		/*
		 * ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + r.getId() +
		 * "\")'>" + r.getPartnerId().getCode() + "</a>");
		 */
		ja.put(r.getPartnerId().getCode());
		ja.put(r.getPartnerId().getName());
		ja.put(r.getIpAddress());
		switch (r.getConnectResult()) {
		case 0:
			ja.put("Không thông");
			break;
		case 1:
			ja.put("Thông");
			break;
		}
		ja.put(Formater.date2ddsmmsyyyspHHmmss(r.getCreateDate()));
	}

	@Override
	public BaseDao<LogCheckConnect> getDao() {
		return logCheckConnectDao;
	}

	@Override
	public String getJsp() {
		return "log_check_connect/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, LogCheckConnectForm form)
			throws Exception {
		List<Partner> listParent = partnerDao.getListTCTD("");
		model.addAttribute("listParent", listParent);
	}
	
	@Autowired
	ExportExcel exportExcel;
	
	public void exportFileExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, LogCheckConnectForm form)
			throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String partnerid = rq.getParameter("partnerId");
			String status = rq.getParameter("status");
			String ipAddress = rq.getParameter("ipAddress");
			List<LogCheckConnect> connects = logCheckConnectDao.reports(partnerid, status, ipAddress);
			for (LogCheckConnect connect : connects) {
				if (connect == null)
					continue;
				if (connect.getConnectResult() != null) {
					if (0 == connect.getConnectResult()) {
						connect.setStatus("Không thông");
					} else if (1 == connect.getConnectResult()) {
						connect.setStatus("Thông");
					}
				}
				if (connect.getCreateDate() != null) 
					connect.setCreateDateStr(Formater.date2ddsmmsyyyspHHmmss(connect.getCreateDate()));
			}
			if (!Formater.isNull(status)) {
				if ("0".equals(status))
					map.put("status", "Không thông");
				else 
					map.put("Status", "Thông");
			} else 
				map.put("stauts", status);
			map.put("ipAddress", ipAddress);
			if (!Formater.isNull(partnerid)) {
				Partner partner = partnerDao.getObject(Partner.class, partnerid);
				if (partner != null)
					map.put("partner", partner.getCode() +" - " + partner.getName());
			}
			map.put("reports", connects);
			exportExcel.export("Trang_thai_ket_noi", rs, map);
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
		}
	}

}
