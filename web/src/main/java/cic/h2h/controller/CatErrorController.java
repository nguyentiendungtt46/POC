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
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.CatErrorDAO;
import cic.h2h.form.CatErrorForm;
import common.util.Formater;
import entity.CatError;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping("/catError")
public class CatErrorController extends CatalogController<CatErrorForm, CatError> {
	
	private static Logger lg = LogManager.getLogger(CatErrorController.class);
	
	@Autowired
	private CatErrorDAO catErrorDao;

	@Override
	public BaseDao<CatError> createSearchDAO(HttpServletRequest request, CatErrorForm form) throws Exception {
		// TODO Auto-generated method stub
		CatErrorDAO dao = new CatErrorDAO();
		CatErrorForm catErrorForm = (CatErrorForm) form;
		if (!Formater.isNull(catErrorForm.getKeyword_code()))
			dao.addRestriction(Restrictions.like("code", catErrorForm.getKeyword_code().trim(), MatchMode.ANYWHERE).ignoreCase());
		
		if (!Formater.isNull(catErrorForm.getKeyword_name()))
			dao.addRestriction(Restrictions.like("name", catErrorForm.getKeyword_name().trim(), MatchMode.ANYWHERE).ignoreCase());
		
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, CatError e, CatErrorForm modelForm) throws Exception {
		// TODO Auto-generated method stub
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getCode()
		+ "</a>");
		ja.put(e.getName());
		ja.put(e.getDescription());
	}

	@Override
	public BaseDao<CatError> getDao() {
		// TODO Auto-generated method stub
		return catErrorDao;
	}

	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "cat_error/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatErrorForm form)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
