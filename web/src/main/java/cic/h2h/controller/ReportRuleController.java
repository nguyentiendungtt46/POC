package cic.h2h.controller;

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

import cic.h2h.dao.hibernate.ReportRuleDao;
import cic.h2h.form.ReportRuleForm;
import common.util.Formater;
import entity.RpRule;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.form.ModelForm;
import frwk.utils.ApplicationContext;

@Controller
@RequestMapping("/reportRule")
public class ReportRuleController extends CatalogController<ReportRuleForm, RpRule> {

	private static Logger lg = LogManager.getLogger(ReportRuleController.class);

	@Autowired
	private ReportRuleDao reportRuleDao;

	
	@Override
	public BaseDao<RpRule> createSearchDAO(HttpServletRequest request, ReportRuleForm form) throws Exception {
		ReportRuleDao dao = new ReportRuleDao();
		ReportRuleForm ruleForm = (ReportRuleForm) form;
		if (!Formater.isNull(ruleForm.getCode()))
			dao.addRestriction(Restrictions.like("code", ruleForm.getCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		
		if (!Formater.isNull(ruleForm.getName()))
			dao.addRestriction(Restrictions.like("name", ruleForm.getName().trim(), MatchMode.ANYWHERE).ignoreCase());
		
		return dao;
	}

	@Override
	public void pushToJa(JSONArray ja, RpRule temp, ReportRuleForm modal) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + temp.getId() + "\")'>"
				+ temp.getCode() + "</a>");
		ja.put(temp.getName());
		ja.put(temp.getDescripstion());
		ja.put(temp.getSample());
	}

	@Override
	public BaseDao<RpRule> getDao() {
		return reportRuleDao;
	}

	@Override
	public String getJsp() {
		return "rq_rule/view";
	}


	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ReportRuleForm form)
			throws Exception {
		
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		if (appContext != null) {
			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
			lg.info(user);
		}
	}

}
