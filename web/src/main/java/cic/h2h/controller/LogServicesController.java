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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.CatProductDao;
import cic.h2h.dao.hibernate.H2HBaseDao;
import cic.h2h.dao.hibernate.LogServicesDao;
import cic.h2h.dao.hibernate.PartnerDao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import cic.h2h.form.LogServicesForm;
import common.util.DateTimeUtil;
import common.util.Formater;
import constants.RightConstants;
import entity.CatProduct;
import entity.LogService;
import entity.LogServiceTransient;
import entity.Partner;
import entity.ServiceInfo;
import entity.ServiceProduct;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/logServices")
public class LogServicesController extends CatalogController<LogServicesForm, LogServiceTransient> {

	static Logger lg = LogManager.getLogger(LogServicesController.class);

	@Autowired
	private LogServicesDao logServicesDao;

	@Autowired
	private ServiceInfoDao serviceInfoDao;

	@Autowired
	private ExportExcel exportExcel;

	@Override
	public BaseDao<LogServiceTransient> createSearchDAO(HttpServletRequest request, LogServicesForm logServicesForm)
			throws Exception {
		LogServicesDao dao = new LogServicesDao();

		if (!Formater.isNull(logServicesForm.getFromDate()))
			dao.addRestriction(Restrictions.ge("startTime",
					new SimpleDateFormat("dd/MM/yyyy").parse(logServicesForm.getFromDate())));
		if (!Formater.isNull(logServicesForm.getToDate())) {
			Date currentDate = new SimpleDateFormat("dd/MM/yyyy").parse(logServicesForm.getToDate());
			Calendar c = Calendar.getInstance();
			c.setTime(currentDate);
			c.add(Calendar.DATE, 1);
			Date dateAdd = c.getTime();
			dao.addRestriction(Restrictions.or(Restrictions.isNull("endTime"), Restrictions.le("endTime", dateAdd)));
		}

		if (!Formater.isNull(logServicesForm.getServiceId()))
			dao.addRestriction(Restrictions.eq("serviceInfo.id", logServicesForm.getServiceId()));
		if (!Formater.isNull(logServicesForm.getProdId())) {
			CatProduct pr = catProductDao.getObject(CatProduct.class, logServicesForm.getProdId());
			dao.addRestriction(Restrictions.eq("productCode", pr.getCode()));
		}

		if (!Formater.isNull(logServicesForm.getPartnerId()))
			dao.addRestriction(Restrictions.eq("partner.id", logServicesForm.getPartnerId()));

		if (!Formater.isNull(logServicesForm.getType())) {
			dao.addRestriction(Restrictions.eq("type", Short.valueOf(logServicesForm.getType())));
		}

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
	protected void pushToJa(JSONArray ja, LogServiceTransient e, LogServicesForm modelForm) throws Exception {
		DecimalFormat myFormat = new DecimalFormat("#,###.##");
		myFormat.setGroupingUsed(true);
		myFormat.setGroupingSize(3);
		if (e.getServiceInfo() == null || Formater.isNull(e.getServiceInfo().getServiceName()))
			ja.put("Service");
		else
			ja.put(e.getServiceInfo().getServiceName());
		ja.put(e.getProductCode());
		ja.put(e.getUserRequest());
		ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getStartTime()));
		ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getEndTime()));
		if (e.getElapsedTime() == null)
			ja.put("");
		else
			ja.put(myFormat.format(new BigDecimal(e.getElapsedTime()).divide(new BigDecimal(1000))));
		try {
			ja.put(getStatusName(e.getStatus()));
		} catch (Exception e1) {
			ja.put("");
		}
		ja.put(e.getMessage());
		if (e.getRequestVol() != null) {
			Long volumn = e.getRequestVol() == null ? 0L : e.getRequestVol();
			ja.put(myFormat.format(new BigDecimal(volumn).divide(new BigDecimal(1000))));
		} else
			ja.put("");
		if (!Formater.isNull(e.getRequestContent())) {
			ja.put("<a data-toggle=\"modal\" data-target=\"#myModal\" href = '#' onclick = 'inputDetail(\"" + e.getId()
					+ "\")'>Chi ti&#7871;t input</a>");
		} else {
			ja.put("");
		}
		if (!Formater.isNull(e.getResponseContent())) {
			ja.put("<a data-toggle=\"modal\" data-target=\"#myModal\" href = '#' onclick = 'outputDetail(\"" + e.getId()
					+ "\")'>Chi ti&#7871;t output</a>");
		} else {
			ja.put("");
		}
		
		//printtrace
		if (e.getIsStackTrace() != null) {
			ja.put("<a data-toggle=\"modal\" data-target=\"#myModal\" href = '#' onclick = 'outputPrintTrace(\"" + e.getId()
			+ "\")'>Chi ti&#7871;t l&#7895;i</a>");
		} else {
			ja.put("");
		}
	}

	@Override
	public BaseDao<LogServiceTransient> getDao() {
		return logServicesDao;
	}

	@Override
	public String getJsp() {
		return "log_core_services/log_service";
	}

	@Autowired
	PartnerDao partnerDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, LogServicesForm form)
			throws Exception {
		List<ServiceInfo> LstService = serviceInfoDao.getServiceInCore((short) 1);
		Date now = new Date();
		Date preMonth = DateTimeUtil.addDate(now, -1, 2);
		model.addAttribute("endDate", DateTimeUtil.dateTime2String(now, "dd/MM/yyyy"));
		model.addAttribute("startDate", DateTimeUtil.dateTime2String(preMonth, "dd/MM/yyyy"));
		model.addAttribute("LstService", LstService);
		model.addAttribute("catProducts", catProductDao.getAll(CatProduct.class));
		model.addAttribute("dsDoiTac", partnerDao.getAll(Partner.class));
	}
	@Autowired
	private H2HBaseDao<LogService> h2hBaseDao;
	public void loadInAndOut(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, LogServicesForm form)
			throws IOException {
		lg.info("loadInAndOut");
		rp.setContentType(MediaType.TEXT_HTML_VALUE);
		PrintWriter out = rp.getWriter();
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		String type = Formater.isNull(rq.getParameter("type")) ? "" : rq.getParameter("type");
		LogService logCoreService = null;
		try {
			logCoreService = h2hBaseDao.get(LogService.class, id);
		} catch (Exception e) {
			lg.error(e);
		}
		if (type.equals(RightConstants.INPUT)) {
			out.println(logCoreService.getRequestContent());
		} else if (type.equals(RightConstants.OUTPUT)) {
			out.println(logCoreService.getResponseContent());
		}  else if (type.equalsIgnoreCase(RightConstants.PRINTSTRACE)) {
			out.println(logCoreService.getStackTrace());
		}
		out.flush();
		out.close();
	}

	@Autowired
	private CatProductDao catProductDao;

	public void reloadProduct(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, LogServicesForm form)
			throws IOException {
		String serviceId = rq.getParameter("serviceId");
		ServiceInfo sv = serviceInfoDao.getObject(ServiceInfo.class, serviceId);
		List<CatProduct> lstProduct = new ArrayList<CatProduct>();
		for (ServiceProduct sp : sv.getServiceProducts()) {
			lstProduct.add(sp.getCatProductId());

		}
		// List<CatProduct> lstProduct =
		// catProductDao.getCatProductByServiceId(serviceId);
		returnJsonArray(rs, lstProduct);
	}

	public void exportExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, LogServicesForm form)
			throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String fromDate = rq.getParameter("fromDate");
			String toDate = rq.getParameter("toDate");
			String serviceId = rq.getParameter("serviceId");
			String partnerId = rq.getParameter("partnerId");
			LogServicesDao dao = new LogServicesDao();
			// LogServicesForm LogServicesForm = (LogServicesForm) form;

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
			if (!Formater.isNull(partnerId))
				dao.addRestriction(Restrictions.eq("partner.id", partnerId));

			if (!Formater.isNull(serviceId))
				dao.addRestriction(Restrictions.eq("serviceInfo.id", serviceId));

			dao.addOrder(Order.desc("startTime"));
			List<LogServiceTransient> logCoreServices = dao.search();
			logCoreServices = logCoreServices == null ? new ArrayList<LogServiceTransient>() : logCoreServices;
			// logCoreServices.forEach(e -> {
			//
			// });
			// LogService logCoreService = logCoreServices.get(0);
			// logCoreServices.clear();
			// logCoreServices = logCoreServices.subList(0, 100);
			map.put("fromDate", fromDate);
			map.put("toDate", toDate);
			if (Formater.isNull(serviceId)) {
				ServiceInfo sv = serviceInfoDao.getObject(ServiceInfo.class, serviceId);
				if (sv != null)
					map.put("serviceId", sv.getServiceName());
			} else {
				map.put("serviceId", "");
			}
			for (LogServiceTransient e : logCoreServices) {
				e.setElapsedTimeReport(new BigDecimal(e.getElapsedTime()).divide(new BigDecimal(1000)));
				Long volumn = e.getRequestVol() == null ? 0L : e.getRequestVol();
				e.setDataVolumnReport(new BigDecimal(volumn).divide(new BigDecimal(1000)));
				if (e.getPartner() != null)
					e.setTctdCode(e.getPartner().getCode());
			}
			map.put("reports", logCoreServices);
			exportExcel.export("Bao_cao_giam_sat_service", rs, map);
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
		}
	}
}
