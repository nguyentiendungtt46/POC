package cic.h2h.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.DefinedErrorDao;
import cic.h2h.form.DefinedErrorForm;
import entity.DefinedError;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping("/definedError")
public class DefinedErrorController extends CatalogController<DefinedErrorForm, DefinedError> {
	
	@Autowired
	private DefinedErrorDao errorPopupDao;

	@Override
	public BaseDao<DefinedError> createSearchDAO(HttpServletRequest request, DefinedErrorForm form) throws Exception {
		DefinedErrorDao dao = new DefinedErrorDao();
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, DefinedError e, DefinedErrorForm modelForm) throws Exception {
	}

	@Override
	public BaseDao<DefinedError> getDao() {
		return errorPopupDao;
	}

	@Override
	public String getJsp() {
		return "base/defined_error";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, DefinedErrorForm form)
			throws Exception {
	}
}
