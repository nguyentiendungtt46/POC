package cic.h2h.controller;

import java.io.PrintWriter;
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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import cic.h2h.dao.hibernate.CatProductDao;
import cic.h2h.dao.hibernate.LogCoreServicesDao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import cic.h2h.form.LogCoreServicesForm;
import cic.h2h.form.ServiceInfoForm;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import entity.CatProduct;
import entity.LogCoreService;
import entity.ServiceInfo;
import entity.ServiceProduct;
import entity.ServiceProductClient;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.form.ModelForm;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/serviceInfo")
public class ServiceInfoController extends CatalogController<ServiceInfoForm, ServiceInfo> {

	private static Logger lg = LogManager.getLogger(ServiceInfoController.class);

	@Autowired
	private ServiceInfoDao serviceInfoDao;

	@Autowired
	private CatProductDao catProductDao;

	@Override
	public BaseDao<ServiceInfo> getDao() {
		return serviceInfoDao;
	}

	@Override
	public BaseDao<ServiceInfo> createSearchDAO(HttpServletRequest request, ServiceInfoForm form) throws Exception {
		ServiceInfoDao dao = new ServiceInfoDao();
		ServiceInfoForm serviceInfoForm = (ServiceInfoForm) form;

		if (!Formater.isNull(serviceInfoForm.getName()))
			dao.addRestriction(Restrictions.like("serviceName", serviceInfoForm.getName().trim(), MatchMode.ANYWHERE)
					.ignoreCase());

		if (!Formater.isNull(serviceInfoForm.getApiPublishName()))
			dao.addRestriction(Restrictions
					.like("publishOperation", serviceInfoForm.getApiPublishName().trim(), MatchMode.ANYWHERE)
					.ignoreCase());

		if (!Formater.isNull(serviceInfoForm.getApiServerName()))
			dao.addRestriction(Restrictions
					.like("destinationOperation", serviceInfoForm.getApiServerName().trim(), MatchMode.ANYWHERE)
					.ignoreCase());

		if (!Formater.isNull(serviceInfoForm.getStatus()))
			dao.addRestriction(Restrictions.eq("status", Long.parseLong(serviceInfoForm.getStatus())));

		if (!Formater.isNull(serviceInfoForm.getType()))
			dao.addRestriction(Restrictions.eq("type", serviceInfoForm.getType()));

		dao.addOrder(Order.asc("type"));
		dao.addOrder(Order.asc("order"));
		return dao;
	}

	@Override
	public void pushToJa(JSONArray ja, ServiceInfo temp, ServiceInfoForm modal) throws Exception {
		String serviceName = null;
		if (temp.getType() != null)
			if (temp.getType().intValue() == 1)
				serviceName = "X&#225;c th&#7921;c";
			else if (temp.getType().intValue() == 2)
				serviceName = "H&#7887;i v&#224; tr&#7843; l&#7901;i";
			else if (temp.getType().intValue() == 3)
				serviceName = "B&#225;o c&#225;o";
			else if (temp.getType().intValue() == 4)
				serviceName = "V&#7845;n tin";
		ja.put(serviceName);
		ja.put("<a href = '#' onclick = 'edit(\"" + temp.getId() + "\")'>" + temp.getServiceName() + "</a>");
		ja.put(temp.getDescription());
		ja.put(FormatNumber.num2Str(temp.getFrequency()));
		if (temp.getStatus() != null && temp.getStatus())
			ja.put("X");

		else
			ja.put("");
		if (temp.getCallByJob() != null && temp.getCallByJob())
			ja.put("X");

		else
			ja.put("");

	}

	@Autowired
	ExportExcel exportExcel;

	public void exportExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ServiceInfoForm form)
			throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String status = rq.getParameter("status");
			String name = rq.getParameter("name");
			String apiServerName = rq.getParameter("apiServerName");
			String apiPublishName = rq.getParameter("apiPublishName");
			String servierType = rq.getParameter("servierType");
			ServiceInfoDao dao = new ServiceInfoDao();
			// LogCoreServicesForm logCoreServicesForm = (LogCoreServicesForm) form;

			ServiceInfoForm serviceInfoForm = (ServiceInfoForm) form;

			if (!Formater.isNull(serviceInfoForm.getName()))
				dao.addRestriction(Restrictions.like("serviceName", name.trim(), MatchMode.ANYWHERE).ignoreCase());

			if (!Formater.isNull(serviceInfoForm.getApiPublishName()))
				dao.addRestriction(
						Restrictions.like("publishOperation", apiPublishName.trim(), MatchMode.ANYWHERE).ignoreCase());

			if (!Formater.isNull(serviceInfoForm.getApiServerName()))
				dao.addRestriction(Restrictions.like("destinationOperation", apiServerName.trim(), MatchMode.ANYWHERE)
						.ignoreCase());

			if (!Formater.isNull(serviceInfoForm.getStatus()))
				dao.addRestriction(Restrictions.eq("status", Long.parseLong(status)));

			if (!Formater.isNull(serviceInfoForm.getType()))
				dao.addRestriction(Restrictions.eq("type", servierType));

			dao.addOrder(Order.asc("type"));
			dao.addOrder(Order.asc("serviceName"));
			List<ServiceInfo> logCoreServices = dao.search();
			logCoreServices = logCoreServices == null ? new ArrayList<>() : logCoreServices;
			logCoreServices.forEach(e -> {
				String typeName = null;
				if (e.getType() != null)
					if (e.getType().intValue() == 1)
						typeName = "Xác thực";
					else if (e.getType().intValue() == 2)
						typeName = "Hỏi và trả lời";
					else if (e.getType().intValue() == 3)
						typeName = "Báo cáo";
					else if (e.getType().intValue() == 4)
						typeName = "Vấn tin";
				e.setTypeName(typeName);
			});
			map.put("reports", logCoreServices);
			exportExcel.export("Danh_muc_service", rs, map);
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
		}
	}

	@Override
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ServiceInfoForm form)
			throws Exception {
		String id = rq.getParameter("id");
		ServiceInfo serviceInfo = serviceInfoDao.getObject(ServiceInfo.class, id);
		if (serviceInfo != null && !Formater.isNull(serviceInfo.getServiceProducts())) {
			serviceInfo.setServiceProductArrayListClient(new ArrayList<ServiceProductClient>());
			for (ServiceProduct productClient : serviceInfo.getServiceProducts()) {
				if (productClient == null)
					continue;
				ServiceProductClient client = new ServiceProductClient();
				client.setId(productClient.getId());
				client.setServiceId(id);
				if (productClient.getCatProductId() != null)
					client.setProductId(productClient.getCatProductId().getId());
				serviceInfo.getServiceProductArrayListClient().add(client);
			}
		}
		rs.setContentType("application/json;charset=utf-8");
		rs.setHeader("Cache-Control", "no-store");
		PrintWriter out = rs.getWriter();
		JSONObject jsonObject = new JSONObject(new ObjectMapper().writeValueAsString(serviceInfo));
		out.print(jsonObject);
		out.flush();
		out.close();

	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ServiceInfoForm form)
			throws Exception {
		ServiceInfoForm infoForm = (ServiceInfoForm) form;
		ServiceInfo serviceInfo = infoForm.getServiceInfo();
		if (!Formater.isNull(serviceInfo.getServiceProductArrayList())) {
			for (ServiceProduct product : serviceInfo.getServiceProductArrayList()) {
				if (product == null)
					continue;
				if (Formater.isNull(product.getId())) {
					product.setId(null);
					product.setServiceInfoId(serviceInfo);
				}
			}
			serviceInfo.getServiceProducts().addAll(serviceInfo.getServiceProductArrayList());
		}
		serviceInfoDao.save(serviceInfo);
	}

	@Override
	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ServiceInfoForm form)
			throws Exception {
		ServiceInfoForm infoForm = (ServiceInfoForm) form;
		ServiceInfo serviceInfo = infoForm.getServiceInfo();
		serviceInfoDao.del(serviceInfo);
	}

	@Override
	public String getJsp() {
		return "cat_service/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ServiceInfoForm form)
			throws Exception {
		model.addAttribute("products", catProductDao.getAll(CatProduct.class));
	}
}
