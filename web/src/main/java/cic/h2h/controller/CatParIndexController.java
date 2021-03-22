package cic.h2h.controller;

import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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

import cic.h2h.dao.hibernate.CatParIndexDao;
import cic.h2h.dao.hibernate.CatProductCfgDao;
import cic.h2h.form.CatParIndexForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.CatParIndex;
import frwk.constants.Constants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/entParIdx")
public class CatParIndexController extends CatalogController<CatParIndexForm, CatParIndex> {

	private static Logger lg = LogManager.getLogger(CatParIndexController.class);

	@Autowired
	private CatParIndexDao entParIndexDao;
	
	@Autowired
	private CatProductCfgDao catProductCfgDao;
	
	@Autowired
	MessageSource messageSource;
	
	@Override
	public BaseDao<CatParIndex> createSearchDAO(HttpServletRequest request, CatParIndexForm form) throws Exception {
		CatParIndexDao dao = new CatParIndexDao();
		CatParIndexForm entParIndexForm = (CatParIndexForm) form;
		if (!Formater.isNull(entParIndexForm.getKeyword_code()))
			dao.addRestriction(
					Restrictions.like("code", entParIndexForm.getKeyword_code().trim(), MatchMode.ANYWHERE).ignoreCase());
		if (!Formater.isNull(entParIndexForm.getKeyword_name()))
			dao.addRestriction(
					Restrictions.like("name", entParIndexForm.getKeyword_name().trim(), MatchMode.ANYWHERE).ignoreCase());
		return dao;
	}
	
	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatParIndexForm form)
			throws Exception {
		CatParIndexForm entParIndexForm = (CatParIndexForm) form;
		CatParIndex parIndex = entParIndexForm.getEntParIndex();
		if (parIndex.getParent() == null || Formater.isNull(parIndex.getParent().getId()))
			parIndex.setParent(null);
		entParIndexDao.save(parIndex);
	}

	@Override
	public void pushToJa(JSONArray ja, CatParIndex temp, CatParIndexForm modal) throws Exception {
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "VN"));
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + temp.getId() + "\")'>"
				+ temp.getCode() + "</a>");
		if (temp.getParent() != null) {
			ja.put(temp.getParent().getCode() + "-" + temp.getParent().getName());
		} else {
			ja.put("");
		}
		
		ja.put(temp.getName());
		//ja.put(String.format("%10.0f", temp.getPrice()));
		if (temp.getPrice() != null)
			ja.put(nf.format(temp.getPrice()));
		else ja.put("");
		//ja.put("<input type='checkbox' name='cboxAction' value='\"" + temp.getId() + "\"'/>");
	}

	@Override
	public String getJsp() {
		
		return "ent_par_index/view";
	}


	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatParIndexForm form)
			throws Exception {
	}
	

	
	@Override
	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatParIndexForm form)
			throws Exception {
		CatParIndexForm infoForm = (CatParIndexForm)form;
		CatParIndex parIndex = infoForm.getEntParIndex();
		if (catProductCfgDao.getCatProductCfgDaoByProductId(parIndex.getId()))
			entParIndexDao.del(parIndex);
		else 
			throw new ResourceException(messageSource.getMessage(Constants.CMM_002, null, "Defautl", null));
		
	}
	
	public void getTree(ModelMap model, HttpServletRequest request, HttpServletResponse response, CatParIndexForm form)
			throws Exception {
		String parentId = request.getParameter("parentId");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		JSONArray array = null;
		array = entParIndexDao.getTreeRoot(parentId, false);
		pw.print(array);
		pw.close();
	}

	@Override
	public BaseDao<CatParIndex> getDao() {
		return entParIndexDao;
	}
	
	@Autowired
	private ExportExcel exportExcel;
	
	public void exportExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatParIndexForm form)
			throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String code = rq.getParameter("code");
			String name = rq.getParameter("name");
			
			CatParIndexDao dao = new CatParIndexDao();

			if (!Formater.isNull(code))
				dao.addRestriction(Restrictions.like("code", code, MatchMode.ANYWHERE).ignoreCase());
			if (!Formater.isNull(name))
				dao.addRestriction(Restrictions.like("name", name, MatchMode.ANYWHERE).ignoreCase());

			List<CatParIndex> LstCatParIndex = dao.search();
			LstCatParIndex = LstCatParIndex == null ? new ArrayList<CatParIndex>() : LstCatParIndex;
			
			map.put("reports", LstCatParIndex);
			exportExcel.export("Danh_muc_chi_tieu", rs, map);
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
		}
	}
}
