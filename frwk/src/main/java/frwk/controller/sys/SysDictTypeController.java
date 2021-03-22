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
import common.util.ResourceException;
import entity.frwk.SysDictParam;
import entity.frwk.SysDictType;
import entity.frwk.SysUsers;
import frwk.constants.Constants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysDictTypeDao;
import frwk.form.ModelForm;
import frwk.form.SysDictParamForm;
import frwk.form.SysDictTypeForm;
import frwk.utils.ApplicationContext;

@Controller
@RequestMapping("/sysType")
public class SysDictTypeController extends CatalogController<SysDictTypeForm,SysDictType> {

	private static Logger lg = LogManager.getLogger(SysDictTypeController.class);
	
	@Autowired
	MessageSource messageSource;
	
	
	@Autowired
	private SysDictTypeDao sysDictTypeDao;
	
	
	
	@Override
	public BaseDao<SysDictType> createSearchDAO(HttpServletRequest request, SysDictTypeForm form) throws Exception {
		SysDictTypeDao dao = new SysDictTypeDao();
		SysDictTypeForm typeForm = (SysDictTypeForm) form;
		if (!Formater.isNull(typeForm.getCode())) {
			dao.addRestriction(Restrictions.like("code", typeForm.getCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(typeForm.getName())) {
			dao.addRestriction(Restrictions.like("name", typeForm.getName().trim(), MatchMode.ANYWHERE).ignoreCase());
		}	
		return dao;
	}

	@Override
	public void pushToJa(JSONArray ja, SysDictType temp, SysDictTypeForm modal) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + temp.getId() + "\")'>"
				+ temp.getName() + "</a>");
		ja.put(temp.getCode());
		ja.put(temp.getDescription());
		
		
	}

	@Override
	public BaseDao<SysDictType> getDao() {
		return sysDictTypeDao;
	}

	@Override
	public String getJsp() {
		return "sys_dict/view_type";
	}

//	
//	@Override
//	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysDictTypeForm form)
//			throws Exception {
//		SysDictTypeForm paramForm = (SysDictTypeForm) form;
//		SysDictType dictType = paramForm.getSysDictType();
//		if (!Formater.isNull(dictType.getId())) {
//			SysDictType oldDdictType = sysDictTypeDao.get(SysDictType.class, dictType.getId());
//			if (!oldDdictType.equals(dictType.getCode()) && sysDictTypeDao.getSysDictTypeByCode(dictType.getCode()) != null) {
//				throw new ResourceException(dictType.getCode() + messageSource.getMessage(Constants.DUPLICATE_CODE, null, "Default", null));
//			}
//		} else {
//			if (sysDictTypeDao.getSysDictTypeByCode(dictType.getCode()) != null) {
//				throw new ResourceException(dictType.getCode() + messageSource.getMessage(Constants.DUPLICATE_CODE, null, "Default", null));
//			}
//		}
//		sysDictTypeDao.save(dictType);
//	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysDictTypeForm form)
			throws Exception {
		// TODO Auto-generated method stub
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		if (appContext != null) {
			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
			lg.info(user);
		}
	}

}
