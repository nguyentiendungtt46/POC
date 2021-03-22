package cic.h2h.controller;

import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import cic.h2h.dao.hibernate.CatAgencyStructureDao;
import cic.h2h.form.CatAgencyStructureForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.CatAgencyStructure;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping("/catAgencyStructure")
public class CatAgencyStructureController extends CatalogController<CatAgencyStructureForm, CatAgencyStructure> {

	private static Logger lg = LogManager.getLogger(CatAgencyStructureController.class);

	@Autowired
	private CatAgencyStructureDao catAgencyStructureDao;
	
	@Autowired
	MessageSource messageSource;
	
	@Override
	public BaseDao<CatAgencyStructure> createSearchDAO(HttpServletRequest request, CatAgencyStructureForm form) throws Exception {
		CatAgencyStructureDao dao = new CatAgencyStructureDao();
		CatAgencyStructureForm catAgencyStructureForm = (CatAgencyStructureForm) form;
		if (!Formater.isNull(catAgencyStructureForm.getKeyword_code()))
			dao.addRestriction(
					Restrictions.like("code", catAgencyStructureForm.getKeyword_code().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(catAgencyStructureForm.getKeyword_name()))
			dao.addRestriction(
					Restrictions.like("name", catAgencyStructureForm.getKeyword_name().trim(), MatchMode.ANYWHERE).ignoreCase());
		return dao;
	}
	
	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatAgencyStructureForm form)
			throws Exception {
		CatAgencyStructureForm entParIndexForm = (CatAgencyStructureForm) form;
		CatAgencyStructure obj = entParIndexForm.getCatAgencyStructure();
		if (obj.getParent() == null || Formater.isNull(obj.getParent().getId()))
			obj.setParent(null);
		catAgencyStructureDao.save(obj);
	}

	@Override
	public void pushToJa(JSONArray ja, CatAgencyStructure temp, CatAgencyStructureForm modal) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + temp.getId() + "\")'>"
				+ temp.getCode() + "</a>");
		ja.put(temp.getName());
		if (temp.getParent() != null) {
			ja.put(temp.getParent().getCode() + "-" + temp.getParent().getName());
		} else {
			ja.put("");
		}
	}

	@Override
	public String getJsp() {
		
		return "cat_agency_structure/view";
	}


	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatAgencyStructureForm form)
			throws Exception {
	}
	

	
	@Override
	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatAgencyStructureForm form)
			throws Exception {
		CatAgencyStructureForm infoForm = (CatAgencyStructureForm)form;
		CatAgencyStructure obj = infoForm.getCatAgencyStructure();
		if (catAgencyStructureDao.getCatAgencyStructureByParent(obj.getId()) == null)
			catAgencyStructureDao.del(obj);
		else 
			throw new ResourceException("T&#7891;n t&#7841;i d&#7919; li&#7879;u quan h&#7879;, kh&#244;ng th&#7875; x&#243;a");
		
	}
	
	public void getTree(ModelMap model, HttpServletRequest request, HttpServletResponse response, CatAgencyStructureForm form)
			throws Exception {
		String parentId = request.getParameter("parentId");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		JSONArray array = null;
		array = catAgencyStructureDao.getTreeRoot(parentId, false);
		pw.print(array);
		pw.close();
	}

	@Override
	public BaseDao<CatAgencyStructure> getDao() {
		return catAgencyStructureDao;
	}
	
}
