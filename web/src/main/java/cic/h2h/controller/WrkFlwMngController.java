package cic.h2h.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.WrkFlwMngDAO;
import cic.h2h.form.WrkFlwMngForm;
import entity.WrkFlwMng;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping("/wrkFlwMng")
public class WrkFlwMngController extends CatalogController<WrkFlwMngForm, WrkFlwMng> {

	@Autowired
	private WrkFlwMngDAO wrkFlwMngDAO;

	@Override
	public BaseDao<WrkFlwMng> createSearchDAO(HttpServletRequest request, WrkFlwMngForm form) throws Exception {
		
		WrkFlwMngDAO dao = new WrkFlwMngDAO();
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, WrkFlwMng e, WrkFlwMngForm modelForm) throws Exception {
		
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getCode() + "</a>");
		ja.put(e.getName());
		ja.put(e.getCaseType());
		ja.put(e.getDescription());
		ja.put("<a href = '#' onclick = 'window.open(\"appflow?parentId=" + e.getId()
		+ "\").focus()'>Worklow</a>");
	}

	@Override
	public BaseDao<WrkFlwMng> getDao() {
		
		return wrkFlwMngDAO;
	}

	@Override
	public String getJsp() {
		
		return "wrkFlwMng/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, WrkFlwMngForm form)
			throws Exception {
		
		
		
	}

}
