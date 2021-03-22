package cic.h2h.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.LogCoreServicesDetailDao;
import cic.h2h.form.LogCoreServicesDetailForm;
import common.util.Formater;
import entity.LogCoreServicesDetail;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping("/logCoreServicesDetail")
public class LogCoreServicesDetailController
		extends CatalogController<LogCoreServicesDetailForm, LogCoreServicesDetail> {

	static Logger lg = LogManager.getLogger(LogCoreServicesDetailController.class);

	@Autowired
	private LogCoreServicesDetailDao logCoreServicesDetailDao;

	@Override
	public BaseDao<LogCoreServicesDetail> createSearchDAO(HttpServletRequest request, LogCoreServicesDetailForm form)
			throws Exception {
		LogCoreServicesDetailDao dao = new LogCoreServicesDetailDao();
		LogCoreServicesDetailForm logCoreServicesDetailForm = (LogCoreServicesDetailForm) form;

		if (!Formater.isNull(logCoreServicesDetailForm.getLogCoreId())) {
			dao.addRestriction(Restrictions.eq("logCoreService.id", logCoreServicesDetailForm.getLogCoreId()));
		}

		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, LogCoreServicesDetail e, LogCoreServicesDetailForm modelForm)
			throws Exception {
		if (e.getLogCoreService() == null || e.getLogCoreService().getServiceInfo() == null
				|| Formater.isNull(e.getLogCoreService().getServiceInfo().getServiceName()))
			ja.put("");
		else
			ja.put(e.getLogCoreService().getServiceInfo().getServiceName());
		ja.put(e.getType());
		if (!Formater.isNull(e.getValue())) {
			ja.put("<a data-toggle=\"modal\" data-target=\"#myModal\" href = '#' onclick = 'valueDetail(\"" + e.getId()
					+ "\")'>Chi ti&#7871;t</a>");
		} else {
			ja.put("");
		}
	}

	@Override
	public BaseDao<LogCoreServicesDetail> getDao() {
		return logCoreServicesDetailDao;
	}

	@Override
	public String getJsp() {
		return "log_core_services_detail/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, LogCoreServicesDetailForm form)
			throws Exception {
	}

	public void loadValue(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, LogCoreServicesDetailForm form)
			throws IOException {

		PrintWriter out = rp.getWriter();
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		LogCoreServicesDetail logCoreServicesDetail = null;
		lg.info("loadValue id: " + id);
		try {
			logCoreServicesDetail = logCoreServicesDetailDao.getLogCoreServicesDetailById(id);
		} catch (Exception e) {
			lg.error(e);
		}
		out.println(logCoreServicesDetail.getValue());
		lg.info("loadValue value: " + logCoreServicesDetail.getValue());
		out.flush();
		out.close();
	}
}
