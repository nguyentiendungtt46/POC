package cic.h2h.controller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import cic.h2h.dao.hibernate.H2HBaseDao;
import cic.h2h.dao.hibernate.RpReportCfgDetailDao;
import cic.h2h.dao.hibernate.RpReportCfgMasterDao;
import cic.h2h.form.RpReportCfgMasterForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.RpReportCfgMaster;
import entity.RpRule;
import entity.RpTypeMapping;
import entity.frwk.SysDictParam;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/configCfgMaster")
public class RpReportCfgMasterController extends CatalogController<RpReportCfgMasterForm, RpReportCfgMaster> {

	private static Logger lg = LogManager.getLogger(RpReportCfgMasterController.class);

	@Autowired
	private RpReportCfgMasterDao rpReportCfgMasterDao;
	
	@Autowired
	SysDictParamDao sysDictParamDao;

	@Override
	public BaseDao<RpReportCfgMaster> createSearchDAO(HttpServletRequest request, RpReportCfgMasterForm form) throws Exception {
		RpReportCfgMasterDao dao = new RpReportCfgMasterDao();
		RpReportCfgMasterForm rpReportCfgMasterForm = (RpReportCfgMasterForm) form;
		if (!Formater.isNull(rpReportCfgMasterForm.getKeyword_code()))
			dao.addRestriction(
					Restrictions.like("reportCode", rpReportCfgMasterForm.getKeyword_code().trim(), MatchMode.ANYWHERE)
							.ignoreCase());
		if (!Formater.isNull(rpReportCfgMasterForm.getKeyword_name()))
			dao.addRestriction(
					Restrictions.like("propertyDes", rpReportCfgMasterForm.getKeyword_name().trim(), MatchMode.ANYWHERE)
							.ignoreCase());
		return dao;
	}

	@Override
	public void pushToJa(JSONArray ja, RpReportCfgMaster temp, RpReportCfgMasterForm modal) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'configValidate(\"" + temp.getReportCode() + "\")'>"
				+ temp.getReportCode() + "</a>");
		ja.put(temp.getPropertyCode());
		ja.put(temp.getPropertyDes());
		ja.put(temp.getPosition());
		ja.put(temp.getPositionExcel());
	}

	@Override
	public BaseDao<RpReportCfgMaster> getDao() {
		return rpReportCfgMasterDao;
	}

	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "rp_report-cfg/master/view";
	}

	@Autowired
	private RpReportCfgDetailDao rpReportCfgDetailDao;
	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RpReportCfgMasterForm form)
			throws Exception {
		
		
		List<SysDictParam> dictParam = sysDictParamDao.getByType("DATA_TYPE");
		rq.setAttribute("rules", dictParam);

	}

	public void loadDataCfgDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			RpReportCfgMasterForm form) throws Exception {
		String reportCode = request.getParameter("reportCode");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		//JSONArray array = null;
		List<RpReportCfgMaster> lst = rpReportCfgMasterDao.getByReportCode(reportCode);
		JSONObject jsonObject = new JSONObject();
		JSONArray array = new JSONArray(new ObjectMapper().writeValueAsString(lst));
		jsonObject.put("reportCode", array);
		pw.print(jsonObject);
		pw.close();
	}

	public void loadDataReportCode(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			RpReportCfgMasterForm form) throws Exception {
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		JSONArray array = null;
		List<RpTypeMapping> listRpType = new H2HBaseDao<RpTypeMapping>().getAll(RpTypeMapping.class);
		array = new JSONArray(new ObjectMapper().writeValueAsString(listRpType));
		pw.print(array);
		pw.close();
	}
	
	@Autowired
	private ExportExcel exportExcel;

	public void exportFileExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RpReportCfgMasterForm form) {
		try {
			String code = rq.getParameter("reportCode");
			String name = rq.getParameter("reportValue");
			Map<String, Object> map = new HashMap<String, Object>();
			List<RpReportCfgMaster> list = rpReportCfgMasterDao.reports(code, name);
			map.put("reports", list);
			if(Formater.isNull(code))
				map.put("code", "T\u1EA5t c\u1EA3");
			else
				map.put("code", code);
			map.put("name", name);
			exportExcel.export("Cau_hinh_bao_cao", rs, map);
		} catch (Exception e) {
			lg.error(e);
		}
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RpReportCfgMasterForm form)
			throws Exception {
		RpReportCfgMasterForm rpReportCfgMasterForm = (RpReportCfgMasterForm) form;
		
		// Chuc nay nay quan trong khong the khong phan quyen
		boolean insert = rpReportCfgMasterDao.isExist(rpReportCfgMasterForm.getReportCode());
		if (insert) {
			if (!RightUtils.haveAction(rq, "method=save&saveType=createNew"))
				throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n th&#234;m m&#7899;i!");

		} else {
			if (!RightUtils.haveAction(rq, "method=save&saveType=update"))
				throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n s&#7917;a!");
		}

		
		
		if (rpReportCfgMasterForm.getRpReportCfgMaster() != null)
			rpReportCfgMasterDao.save(rpReportCfgMasterForm.getRpReportCfgMaster(), rpReportCfgMasterForm.getReportCode());
	}

	public void copy(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RpReportCfgMasterForm form)
			throws Exception {
		if (!RightUtils.haveAction(rq, "method=save&saveType=createNew"))
			throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n th&#234;m m&#7899;i!");
		String reportCopy = rq.getParameter("reportCopy");
		String reportPaste = rq.getParameter("reportPaste");
		List<RpReportCfgMaster> lstMasterPaste = rpReportCfgMasterDao.getByReportCode(reportPaste);

		if (lstMasterPaste.size() > 0)
			throw new ResourceException(
					"B&#225;o c&#225;o: " + reportPaste + " &#273;&#227; t&#7891;n t&#7841;i c&#7845;u h&#236;nh.");

		List<RpReportCfgMaster> lstMasterCopy = rpReportCfgMasterDao.getByReportCode(reportCopy);
		for (RpReportCfgMaster cMaster : lstMasterCopy) {
			RpReportCfgMaster mst = new RpReportCfgMaster();
			mst.setReportCode(reportPaste);
			mst.setPropertyCode(cMaster.getPropertyCode());
			mst.setPropertyDes(cMaster.getPropertyDes());
			mst.setPropertyLength(cMaster.getPropertyLength());
			mst.setDataType(cMaster.getDataType());
			mst.setPosition(cMaster.getPosition());
			mst.setPositionExcel(cMaster.getPositionExcel());
			mst.setCreatedDate(new Date());
			rpReportCfgMasterDao.save(mst);
		}
		throw new ResourceException("COPY_SUCCESS");
	}
}
