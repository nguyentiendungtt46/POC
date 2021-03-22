package cic.h2h.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.form.PocUserinfoFormForm;
import entity.PocUserinfoForm;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping("/erqCrt")
public class ErqCrtController extends CatalogController<PocUserinfoFormForm, PocUserinfoForm> {

	@Override
	public BaseDao<PocUserinfoForm> createSearchDAO(HttpServletRequest request, PocUserinfoFormForm form)
			throws Exception {
		
		return null;
	}

	@Override
	protected void pushToJa(JSONArray ja, PocUserinfoForm e, PocUserinfoFormForm modelForm) throws Exception {
		
		
	}

	@Override
	public BaseDao<PocUserinfoForm> getDao() {
		
		return null;
	}

	@Override
	public String getJsp() {
		
		return null;
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PocUserinfoFormForm form)
			throws Exception {
		
		
	}

}
