package cic.h2h.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.LogCoreServicesDao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import cic.h2h.form.LogCoreServicesForm;
import common.util.DateTimeUtil;
import common.util.Formater;
import constants.RightConstants;
import entity.LogCoreService;
import entity.ServiceInfo;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.utils.ExportExcel;

@RequestMapping("/logCoreServices")
public class LogCoreServicesController extends CatalogController<LogCoreServicesForm, LogCoreService> {

	static Logger lg = LogManager.getLogger(LogCoreServicesController.class);

	@Autowired
	private LogCoreServicesDao logCoreServicesDao;

	@Autowired
	private ServiceInfoDao serviceInfoDao;

	@Autowired
	private ExportExcel exportExcel;

	@Autowired
	private SysUsersDao sysUsersDao;

	@Override
	public BaseDao<LogCoreService> createSearchDAO(HttpServletRequest request, LogCoreServicesForm form)
			throws Exception {
		LogCoreServicesDao dao = new LogCoreServicesDao();
		LogCoreServicesForm logCoreServicesForm = (LogCoreServicesForm) form;

		if (!Formater.isNull(logCoreServicesForm.getFromDate()))
			dao.addRestriction(Restrictions.ge("startTime",
					new SimpleDateFormat("dd/MM/yyyy").parse(logCoreServicesForm.getFromDate())));
		if (!Formater.isNull(logCoreServicesForm.getToDate())) {
			Date currentDate = new SimpleDateFormat("dd/MM/yyyy").parse(logCoreServicesForm.getToDate());
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE, 1);
			Date dateAdd = c.getTime();
			dao.addRestriction(Restrictions.le("endTime", dateAdd));
		}

		if (!Formater.isNull(logCoreServicesForm.getServiceId()))
			dao.addRestriction(Restrictions.eq("serviceInfo.id", logCoreServicesForm.getServiceId()));
		if (!Formater.isNull(logCoreServicesForm.getUsername()))
			dao.addRestriction(Restrictions.eq("userRequest", logCoreServicesForm.getUsername()));
		if (!Formater.isNull(logCoreServicesForm.getStatus()))
			dao.addRestriction(Restrictions.eq("status", logCoreServicesForm.getStatus()));

		dao.addOrder(Order.desc("startTime"));
		return dao;
	}

	private String getStatusName(Long status) {
		if (1L == status) {
			return "Thành công";
		} else if (-1L == status) {
			return "Thất bại";
		}
		return "";
	}

	@Override
	protected void pushToJa(JSONArray ja, LogCoreService e, LogCoreServicesForm modelForm) throws Exception {
		DecimalFormat myFormat = new DecimalFormat("#,###.##");
		myFormat.setGroupingUsed(true);
		myFormat.setGroupingSize(3);
		// NumberFormat myFormat = NumberFormat.getInstance();
		// myFormat.setGroupingUsed(true);
		if (e.getServiceInfo() == null || Formater.isNull(e.getServiceInfo().getServiceName()))
			ja.put("<a href='logCoreServicesDetail?logCoreId=" + e.getId() + "' >Service</a>");
		else
			ja.put("<a href='logCoreServicesDetail?logCoreId=" + e.getId() + "' >" + e.getServiceInfo().getServiceName()
					+ "</a>");
		ja.put(e.getTctdCode());
		ja.put(e.getIp());
		ja.put(e.getUserRequest());
		ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getStartTime()));
		ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getEndTime()));
		// new BigDecimal(e.getElapsedTime()).divide(new BigDecimal(1000))
		ja.put(myFormat.format(new BigDecimal(e.getElapsedTime()).divide(new BigDecimal(1000))));
		ja.put(getStatusName(e.getStatus()));
		ja.put(e.getMessage());
		Long volumn = e.getDataVolumn() == null ? 0L : e.getDataVolumn();
		ja.put(myFormat.format(new BigDecimal(volumn).divide(new BigDecimal(1000))));
		if (!Formater.isNull(e.getInput())) {
			ja.put("<a data-toggle=\"modal\" data-target=\"#myModal\" href = '#' onclick = 'inputDetail(\"" + e.getId()
					+ "\")'>Chi ti&#7871;t input</a>");
		} else {
			ja.put("");
		}
		if (!Formater.isNull(e.getOutput())) {
			ja.put("<a data-toggle=\"modal\" data-target=\"#myModal\" href = '#' onclick = 'outputDetail(\"" + e.getId()
					+ "\")'>Chi ti&#7871;t output</a>");
		} else {
			ja.put("");
		}
	}

	@Override
	public BaseDao<LogCoreService> getDao() {
		return logCoreServicesDao;
	}

	@Override
	public String getJsp() {
		return "log_core_services/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, LogCoreServicesForm form)
			throws Exception {
		List<ServiceInfo> LstService = serviceInfoDao.getServiceInCore((short) 2);
		List<SysUsers> lstUser = sysUsersDao.getAll(SysUsers.class);
		Date now = new Date();
		Date preMonth = DateTimeUtil.addDate(now, -1, 2);
		model.addAttribute("endDate", DateTimeUtil.dateTime2String(now, "dd/MM/yyyy"));
		model.addAttribute("startDate", DateTimeUtil.dateTime2String(preMonth, "dd/MM/yyyy"));
		model.addAttribute("LstService", LstService);
		model.addAttribute("lstUser", lstUser);
	}

	public void loadInAndOut(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, LogCoreServicesForm form)
			throws IOException {
		lg.info("loadInAndOut");
		rp.setContentType("text/plan;charset=utf-8");
		PrintWriter out = rp.getWriter();
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		String type = Formater.isNull(rq.getParameter("type")) ? "" : rq.getParameter("type");
		LogCoreService logCoreService = null;
		try {
			logCoreService = logCoreServicesDao.getLogCoreServiceById(id);
		} catch (Exception e) {
			lg.error(e);
		}
		if (type.equals(RightConstants.INPUT)) {
			out.println(logCoreService.getInput());
		} else if (type.equals(RightConstants.OUTPUT)) {
			out.println(logCoreService.getOutput());
		}
		out.flush();
		out.close();
	}

	public void exportExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, LogCoreServicesForm form)
			throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String fromDate = rq.getParameter("fromDate");
			String toDate = rq.getParameter("toDate");
			String serviceId = rq.getParameter("serviceId");
			String username = rq.getParameter("username");
			String status = rq.getParameter("status");
			LogCoreServicesDao dao = new LogCoreServicesDao();
			// LogCoreServicesForm logCoreServicesForm = (LogCoreServicesForm) form;

			if (!Formater.isNull(fromDate))
				dao.addRestriction(Restrictions.ge("startTime", new SimpleDateFormat("dd/MM/yyyy").parse(fromDate)));
			if (!Formater.isNull(toDate)) {
				Date currentDate = new SimpleDateFormat("dd/MM/yyyy").parse(toDate);
				Calendar c = Calendar.getInstance();
				c.setTime(currentDate);
				c.add(Calendar.DATE, 1);
				Date dateAdd = c.getTime();
				dao.addRestriction(Restrictions.le("endTime", dateAdd));
			}

			if (!Formater.isNull(serviceId))
				dao.addRestriction(Restrictions.eq("serviceInfo.id", serviceId));
			if (!Formater.isNull(username))
				dao.addRestriction(Restrictions.eq("userRequest", username));
			if (!Formater.isNull(status))
				dao.addRestriction(Restrictions.eq("status", Long.valueOf(status)));

			dao.addOrder(Order.desc("startTime"));
			List<LogCoreService> logCoreServices = dao.search();
			logCoreServices = logCoreServices == null ? new ArrayList<LogCoreService>() : logCoreServices;
			// logCoreServices.forEach(e -> {
			//
			// });
			// LogCoreService logCoreService = logCoreServices.get(0);
			// logCoreServices.clear();
			// logCoreServices = logCoreServices.subList(0, 100);
			map.put("fromDate", fromDate);
			map.put("toDate", toDate);
			if (Formater.isNull(username))
				map.put("username", "T\u1EA5t c\u1EA3");
			else
				map.put("username", username);
			if (Formater.isNull(status))
				map.put("status", "T\u1EA5t c\u1EA3");
			else if (status.equals("-1"))
				map.put("status", "Th\u1EA5t b\u1EA1i");
			else if (status.equals("1"))
				map.put("status", "Th\u00E0nh c\u00F4ng");
			if (!Formater.isNull(serviceId)) {
				ServiceInfo sv = serviceInfoDao.getObject(ServiceInfo.class, serviceId);
				if (sv != null)
					map.put("serviceId", sv.getServiceName());
			} else {
				map.put("serviceId", "T\u1EA5t c\u1EA3");
			}
			for (LogCoreService e : logCoreServices) {
				e.setElapsedTimeReport(new BigDecimal(e.getElapsedTime()).divide(new BigDecimal(1000)));
				Long volumn = e.getDataVolumn() == null ? 0L : e.getDataVolumn();
				e.setDataVolumnReport(new BigDecimal(volumn).divide(new BigDecimal(1000)));
			}
			map.put("reports", logCoreServices);
			// ja.put(myFormat.format(new BigDecimal(e.getElapsedTime()).divide(new
			// BigDecimal(1000))));
			exportExcel.export("Bao_cao_giam_sat_service", rs, map);
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
		}
	}
}
