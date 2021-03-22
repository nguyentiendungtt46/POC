package cic.h2h.controller;

import java.util.List;

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

import cic.h2h.dao.hibernate.CatClassDao;
import cic.h2h.form.CatClassForm;
import common.util.Formater;
import entity.CatClass;
import entity.frwk.SysDictParam;
import entity.frwk.SysDictType;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysDictTypeDao;

@Controller
@RequestMapping("/cfgClass")
public class CatClassController extends CatalogController<CatClassForm, CatClass> {
	
	private static Logger lg = LogManager.getLogger(CatClassController.class);

	@Autowired
	CatClassDao catClassDao;

	@Autowired
	SysDictTypeDao sysDictTypeDao;
	
	@Autowired
	SysDictParamDao sysDictParamDao;
	
	@Override
	public BaseDao<CatClass> createSearchDAO(HttpServletRequest request, CatClassForm form) throws Exception {
		CatClassDao dao = new CatClassDao();
		CatClassForm catClassForm = (CatClassForm) form;
		if (!Formater.isNull(catClassForm.getKeyword_code()))
			dao.addRestriction(Restrictions.like("code", catClassForm.getKeyword_code().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(catClassForm.getKeyword_name()))
			dao.addRestriction(Restrictions.like("tinhthanh", catClassForm.getKeyword_name().trim(), MatchMode.ANYWHERE).ignoreCase());
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, CatClass e, CatClassForm modelForm) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getCode() + "</a>");
		if(!Formater.isNull(e.getTinhthanh())) {
			SysDictParam sysdictParam = new SysDictParam();
			sysdictParam = sysDictParamDao.getObject(SysDictParam.class, e.getTinhthanh());
			if(sysdictParam != null) ja.put(sysdictParam.getValue() + " - " + sysdictParam.getDescription());
			else ja.put("");	
		}else ja.put("");
		
	}

	@Override
	public BaseDao<CatClass> getDao() {
		return catClassDao;
	}

	@Override
	public String getJsp() {
		return "cat_class/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatClassForm form)
			throws Exception {
		SysDictType typeProvince = sysDictTypeDao.getSysDictTypeByCode("01");
		List<SysDictParam> lstProvince = sysDictParamDao.sysDictParams("", "", typeProvince.getId());
		model.addAttribute("lstProvince", lstProvince);
		
	}

}
