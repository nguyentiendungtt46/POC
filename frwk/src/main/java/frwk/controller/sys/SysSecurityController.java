package frwk.controller.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import common.util.Formater;
import entity.frwk.SysSecurity;
import frwk.constants.Constants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysSecurityDao;
import frwk.form.ModelForm;
import frwk.form.SysSecurityForm;

@Controller
@RequestMapping(value = "/security")
public class SysSecurityController extends CatalogController<SysSecurityForm,SysSecurity> {
	private static Logger lg = LogManager.getLogger(SysSecurityController.class);

	@Autowired
	private SysSecurityDao sysSecurityDao;
	
	@Autowired
	private MessageSource messageSource;


	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "qtht/quan_ly_bao_mat";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysSecurityForm form)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public BaseDao<SysSecurity> createSearchDAO(HttpServletRequest request, SysSecurityForm form) throws Exception {
		SysSecurityForm securityForm = (SysSecurityForm) form;
		SysSecurityDao dao = new SysSecurityDao();
		if (!Formater.isNull(securityForm.getsCode())) {
			dao.addRestriction(Restrictions.like("code", securityForm.getsCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(securityForm.getsName())) {
			dao.addRestriction(Restrictions.like("name", securityForm.getsName().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		return dao;
	}
	
	@Override
	public void pushToJa(JSONArray ja, SysSecurity temp, SysSecurityForm modal) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + temp.getId() + "\")'>"
				+ temp.getCode() + "</a>");
		ja.put(temp.getName());
		if (!Formater.isNull(temp.getValue()))
			ja.put(temp.getValue());
		else ja.put("");
		if (temp.isActive()) {
			ja.put(messageSource.getMessage(Constants.ACTIVE, null, "Default", null));
		} else {
			ja.put(messageSource.getMessage(Constants.INACTIVE, null, "Default", null));
		}
		
	}

	@Override
	public BaseDao<SysSecurity> getDao() {
		return sysSecurityDao;
	}

}
