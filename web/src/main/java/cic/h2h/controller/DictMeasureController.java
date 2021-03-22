package cic.h2h.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.DictMeasureDao;
import cic.h2h.form.DictMeasureForm;
import common.util.Formater;
import entity.DictGrp;
import entity.DictMeasure;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping(value = "/dictMeasure")
public class DictMeasureController extends CatalogController<DictMeasureForm, DictMeasure> {

	@Autowired
	private DictMeasureDao dictMeasureDao;

	@Override
	public BaseDao<DictMeasure> createSearchDAO(HttpServletRequest request, DictMeasureForm form) throws Exception {
		DictMeasureDao dao = new DictMeasureDao();
		if (!Formater.isNull(form.getCodeSearch())) {
			dao.addRestriction(Restrictions.like("colCode", form.getCodeSearch().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(form.getPathSearch())) {
			dao.addRestriction(Restrictions.like("path", form.getPathSearch().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		return dao;
	}
	
	@Override
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, DictMeasureForm form)
			throws Exception {
		String id = rq.getParameter("id");
		returnObject(rs, dictMeasureDao.get(DictMeasure.class, Long.parseLong(id)));
	}
	

	@Override
	protected void pushToJa(JSONArray ja, DictMeasure e, DictMeasureForm modelForm) throws Exception {
		
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getColCode()
		+ "</a>");
		ja.put(e.getColDesc());
		ja.put(e.getDataType());
		ja.put(e.getPath());
	}

	@Override
	public BaseDao<DictMeasure> getDao() {
		
		return dictMeasureDao;
	}

	@Override
	public String getJsp() {
		
		return "dict_measure/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, DictMeasureForm form)
			throws Exception {

	}
	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, DictMeasureForm form)
			throws Exception {
		//TODO: fix, do framework dang dung dictgrp
		form.getModel().setDictGrp(new DictGrp(1l));
		super.save(model, rq, rs, form);

	}
}
