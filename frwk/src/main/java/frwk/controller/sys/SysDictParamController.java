package frwk.controller.sys;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.util.Formater;
import entity.frwk.SysDictParam;
import entity.frwk.SysDictType;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysDictTypeDao;
import frwk.form.ModelForm;
import frwk.form.SysDictParamForm;
import frwk.utils.ApplicationContext;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/sysParam")
public class SysDictParamController extends CatalogController<SysDictParamForm, SysDictParam> {

	private static Logger lg = LogManager.getLogger(SysDictParamController.class);

	@Autowired
	private SysDictParamDao sysDictParamDao;

	@Autowired
	private SysDictTypeDao sysDictTypeDao;

	@Override
	public BaseDao<SysDictParam> createSearchDAO(HttpServletRequest request, SysDictParamForm form) throws Exception {
		SysDictParamDao dao = new SysDictParamDao();
		SysDictParamForm paramForm = (SysDictParamForm) form;
		if (!Formater.isNull(paramForm.getCodeSearch())) {
			dao.addRestriction(
					Restrictions.like("code", paramForm.getCodeSearch().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(paramForm.getValueSearch())) {
			dao.addRestriction(
					Restrictions.like("value", paramForm.getValueSearch().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(paramForm.getParamsType())) {
			dao.addRestriction(Restrictions.eq("sysDictType.id", paramForm.getParamsType().trim()));
		}
		return dao;
	}

	@Override
	public void pushToJa(JSONArray ja, SysDictParam temp, SysDictParamForm modal) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + temp.getId() + "\")'>" + temp.getCode()
				+ "</a>");
		ja.put(temp.getValue());
		ja.put(temp.getDescription());
		if (temp.getSysDictType() != null)
			ja.put(temp.getSysDictType().getName());
		else
			ja.put("");
	}

	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "sys_dict/view_param";
	}


	@Override
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysDictParamForm form)
			throws Exception {
		String id = rq.getParameter("id");
		SysDictParam o = sysDictParamDao.getObject(SysDictParam.class, id);
		rs.setContentType("application/json;charset=utf-8");
		rs.setHeader("Cache-Control", "no-store");
		PrintWriter out = rs.getWriter();
		JSONObject jsonObject = new JSONObject(new ObjectMapper().writeValueAsString(o));
		out.print(jsonObject);
		out.flush();
		out.close();

	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysDictParamForm form)
			throws Exception {
		// TODO Auto-generated method stub
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		if (appContext != null) {
			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
			lg.info(user);
		}
		model.addAttribute("sysTypes", sysDictTypeDao.getAll(SysDictType.class));
	}

	@Override
	public BaseDao<SysDictParam> getDao() {
		return sysDictParamDao;
	}
	
	@Autowired
	private ExportExcel exportExcel;
	
	public void exportFileExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysDictParamForm form) {
		try {
			String code = rq.getParameter("code");
			String value = rq.getParameter("value");
			String paramType = rq.getParameter("paramType");
			List<SysDictParam> dictParams = sysDictParamDao.sysDictParams(code, value, paramType);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dictParams", dictParams);
			map.put("code", code);
			map.put("value", value);
			if (!Formater.isNull(paramType)) 
				map.put("valueType", sysDictTypeDao.getObject(SysDictType.class, paramType).getName());
			exportExcel.export("Danh_sach_tu_dien_he_thong", rs, map);
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e);
		}
	}

}
