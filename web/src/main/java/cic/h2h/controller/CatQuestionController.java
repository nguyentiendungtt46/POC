package cic.h2h.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.CatCustomerDAO;
import cic.h2h.dao.hibernate.CatQuestionDao;
import cic.h2h.form.CatCustomerForm;
import cic.h2h.form.CatQuestionForm;
import common.util.Formater;
import entity.CatQuestion;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping("/cfgQuestion")
public class CatQuestionController extends CatalogController<CatQuestionForm, CatQuestion> {
	
	private static Logger lg = LogManager.getLogger(CatQuestionController.class);

	@Autowired
	CatQuestionDao catQuestionDao;
	@Override
	public BaseDao<CatQuestion> createSearchDAO(HttpServletRequest request, CatQuestionForm form) throws Exception {
		CatQuestionDao dao = new CatQuestionDao();
		CatQuestionForm catQuestionForm = (CatQuestionForm) form;
		if (!Formater.isNull(catQuestionForm.getKeyword_code()))
			dao.addRestriction(Restrictions.like("maCauHoi", catQuestionForm.getKeyword_code().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(catQuestionForm.getKeyword_name()))
			dao.addRestriction(Restrictions.like("cauHoi", catQuestionForm.getKeyword_name().trim(), MatchMode.ANYWHERE).ignoreCase());
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, CatQuestion e, CatQuestionForm modelForm) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getMaCauHoi() + "</a>");
		ja.put(e.getCauHoi());
	}

	@Override
	public BaseDao<CatQuestion> getDao() {
		return catQuestionDao;
	}

	@Override
	public String getJsp() {
		return "cat_question/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatQuestionForm form)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
