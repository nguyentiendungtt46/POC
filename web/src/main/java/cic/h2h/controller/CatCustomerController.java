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
import cic.h2h.form.CatCustomerForm;
import common.util.Formater;
import entity.CatCustomer;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping("/catCustomer")
public class CatCustomerController extends CatalogController<CatCustomerForm, CatCustomer> {
	
	private static Logger lg = LogManager.getLogger(CatCustomerController.class);
	
	@Autowired
	private CatCustomerDAO catCustomerDao;
	
	@Override
	public BaseDao<CatCustomer> createSearchDAO(HttpServletRequest request, CatCustomerForm form) throws Exception {
		CatCustomerDAO dao = new CatCustomerDAO();
		CatCustomerForm catCustomerForm = (CatCustomerForm) form;
		if (!Formater.isNull(catCustomerForm.getKeyword_code()))
			dao.addRestriction(Restrictions.like("code", catCustomerForm.getKeyword_code().trim(), MatchMode.ANYWHERE).ignoreCase());
		
		if (!Formater.isNull(catCustomerForm.getKeyword_name()))
			dao.addRestriction(Restrictions.like("name", catCustomerForm.getKeyword_name().trim(), MatchMode.ANYWHERE).ignoreCase());
		
		return dao;
	}

	@Override
	public BaseDao<CatCustomer> getDao() {
		// TODO Auto-generated method stub
		return catCustomerDao;
	}

	@Override
	protected void pushToJa(JSONArray ja, CatCustomer e, CatCustomerForm modelForm) throws Exception {
		// TODO Auto-generated method stub
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getMaKh()
		+ "</a>");
		ja.put(e.getMaCic());
		ja.put(e.getTenKh());
		ja.put(e.getSoCmt());
	}

	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "cat_customer/view";
	}

	@Override
	public void initData(ModelMap arg0, HttpServletRequest arg1, HttpServletResponse arg2, CatCustomerForm arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	

}
