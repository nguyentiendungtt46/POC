package cic.h2h.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cic.h2h.dao.hibernate.RpM1FilterDao;
import cic.h2h.dao.hibernate.RpTypeDao;
import cic.h2h.form.RpM1FilterForm;
import common.util.Formater;
import entity.RpM1Filter;
import entity.RpType;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.form.ModelForm;

@Controller
@RequestMapping("/rpM1Filter")
public class RpM1FilterController extends CatalogController<RpM1FilterForm, RpM1Filter> {

	static Logger lg = LogManager.getLogger(RpM1FilterController.class);

	@Autowired
	private RpM1FilterDao rpM1FilterDao;

	@Autowired
	private RpTypeDao rpTypeDao;

	private List<RpType> lstRpType;

	@Override
	public BaseDao<RpM1Filter> getDao() {
		return rpM1FilterDao;
	}

	@Override
	public BaseDao<RpM1Filter> createSearchDAO(HttpServletRequest request, RpM1FilterForm form) throws Exception {
		RpM1FilterDao dao = new RpM1FilterDao();
		RpM1FilterForm rpM1FilterForm = (RpM1FilterForm) form;
		if (!Formater.isNull(rpM1FilterForm.getReportCode()))
			dao.addRestriction(Restrictions.eq("rpCode.id", rpM1FilterForm.getReportCode()));
		String[] hasFilterRp = new String[] { "K3122A", "K3121A", "K3111A", "K3112A", "K3113", "K3123", "K3223",
				"K3213", "K3333", "K3331", "K3332", "K4011", "K4012", "K4013", "K4021", "K4022", "K4023", "K5011", "K5012", "K5013",
				"T02G1", "T02G2", "T02DS", "K1011", "K1012", "K1013", "K1021", "K1022", "K1023", "K3111", "K3112",
				"K3121", "K3122", "K2011", "K2012", "K2013", "K2B011", "K2B012", "K2B013", "K1131", "K1132", "K1133" };
		dao.addRestriction(Restrictions.in("rpCode.id", hasFilterRp));
		
		if (!Formater.isNull(form.getKeyWord()))
			dao.addRestriction(Restrictions.like("rpCode.id", form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase());
		return dao;
	}

	@Override
	public void pushToJa(JSONArray ja, RpM1Filter r, RpM1FilterForm modal) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + r.getId() + "\")'>" + r.getRpCode().getId()
				+ "</a>");
		if (r.getDisableFilter() != null && r.getDisableFilter().intValue() == 1)
			ja.put("Lu&#244;n chuy&#7875;n file sang M1");
		else
			ja.put("Chuy&#7875;n file sang M1 n&#7871;u kh&#244;ng c&#243; l&#7895;i ho&#7863;c s&#7889; l&#432;&#7907;ng l&#7895;i n&#7857;m trong ng&#432;&#7905;ng");
	}

	@Override
	public String getJsp() {
		return "rp_m1_filter/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RpM1FilterForm form)
			throws Exception {
		lstRpType = rpTypeDao.getAllFilterReport();
		model.addAttribute("lstRpType", lstRpType);
	}

}
