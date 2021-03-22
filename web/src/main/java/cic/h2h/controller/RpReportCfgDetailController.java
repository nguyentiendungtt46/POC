package cic.h2h.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import cic.h2h.dao.hibernate.H2HBaseDao;
import cic.h2h.dao.hibernate.PartnerDao;
import cic.h2h.dao.hibernate.ReportRuleDao;
import cic.h2h.dao.hibernate.RpReportCfgDetailDao;
import cic.h2h.dao.hibernate.RpReportCfgMasterDao;
import cic.h2h.form.QnaInDetailForm;
import cic.h2h.form.RpReportCfgDetailForm;
import common.util.Formater;
import common.util.ResourceException;
import dto.ExcelParamDTO;
import dto.ExcelTempDTO;
import entity.Partner;
import entity.RpReportCfgDetail;
import entity.RpReportCfgMaster;
import entity.RpRule;
import entity.RpType;
import entity.RpTypeMapping;
import entity.frwk.SysDictType;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.dao.hibernate.sys.SysDictTypeDao;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.form.ModelForm;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/configCfgDetail")
public class RpReportCfgDetailController extends CatalogController<RpReportCfgDetailForm,RpReportCfgDetail> {

	private static Logger lg = LogManager.getLogger(RpReportCfgDetailController.class);

	@Autowired
	private RpReportCfgDetailDao rpReportCfgDetailDao;

	@Autowired
	private RpReportCfgMasterDao rpReportCfgMasterDao;

	@Autowired
	private SysDictTypeDao sysDictTypeDao;
	
	@Autowired
	private ReportRuleDao reportRuleDao;

	@Autowired
	PartnerDao partnerDao;

	@Override
	public BaseDao<RpReportCfgDetail> createSearchDAO(HttpServletRequest request, RpReportCfgDetailForm form) throws Exception {
		RpReportCfgDetailDao dao = new RpReportCfgDetailDao();
		RpReportCfgDetailForm rpReportCfgDetailForm = (RpReportCfgDetailForm) form;
		if (!Formater.isNull(rpReportCfgDetailForm.getKeyword_code()))
			dao.addRestriction(Restrictions.sqlRestriction(
					"exists (select 1 from RP_REPORT_CFG_MASTER where id = {alias}.REPORT_CFG_ID and REPORT_CODE = '"
							+ rpReportCfgDetailForm.getKeyword_code() + "')"));
		if (!Formater.isNull(rpReportCfgDetailForm.getKeyword_name()))
			dao.addRestriction(Restrictions
					.like("errCode", rpReportCfgDetailForm.getKeyword_name().trim(), MatchMode.ANYWHERE).ignoreCase());
		// dao.addRestriction(Restrictions.eq("status", BigDecimal.ONE));
		return dao;
	}

	@Override
	public void pushToJa(JSONArray ja, RpReportCfgDetail temp, RpReportCfgDetailForm modal) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'configValidate(\""
				+ temp.getReportCfgId().getReportCode() + "\")'>" + temp.getReportCfgId().getReportCode() + "</a>");
		ja.put(temp.getReportCfgId().getPropertyCode());
		//ja.put(temp.getConfigKey());
		RpRule rpRule = reportRuleDao.get(RpRule.class, temp.getConfigKey());
		ja.put(rpRule.getCode());
		ja.put(temp.getConfigValue());
		ja.put(temp.getErrCode());
	}

	@Override
	public BaseDao<RpReportCfgDetail> getDao() {
		return rpReportCfgDetailDao;
	}

	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "rp_report-cfg/detail/view";
	}

	@Autowired
	SysPartnerDao sysPartnerDao;
	
	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RpReportCfgDetailForm form)
			throws Exception {
		// TODO Auto-generated method stub
		model.addAttribute("listTypes", sysDictTypeDao.getAll(SysDictType.class));
		model.addAttribute("dsDonvi", sysPartnerDao.getAll());
	}

	public void loadDataReportCode(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			RpReportCfgDetailForm form) throws Exception {
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		JSONArray array = null;
		List<RpTypeMapping> listRpType = new H2HBaseDao<RpTypeMapping>().getAll(RpTypeMapping.class);
		array = new JSONArray(new ObjectMapper().writeValueAsString(listRpType));
		pw.print(array);
		pw.close();
	}

	public void loadDataCfgDetail(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			RpReportCfgDetailForm form) throws Exception {
		String reportCode = request.getParameter("reportCode");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		List<RpReportCfgMaster> lst = rpReportCfgMasterDao.getByReportCode(reportCode);
		List<RpRule> rule = new H2HBaseDao<RpRule>().getAll(RpRule.class);
		JSONObject jsonObject = new JSONObject();
		JSONArray array = new JSONArray(new ObjectMapper().writeValueAsString(lst));
		JSONArray arrayRule = new JSONArray(new ObjectMapper().writeValueAsString(rule));
		jsonObject.put("reportCode", array);
		jsonObject.put("rpRule", arrayRule);
		pw.print(jsonObject);
		pw.close();
	}

	public void loadDataPropertyCode(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			RpReportCfgDetailForm form) throws Exception {
		String reportCode = request.getParameter("reportCode");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		JSONArray array = null;
		List<RpReportCfgMaster> lst = rpReportCfgMasterDao.getByReportCode(reportCode);
		array = new JSONArray(new ObjectMapper().writeValueAsString(lst));
		pw.print(array);
		pw.close();
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RpReportCfgDetailForm form)
			throws Exception {
		RpReportCfgDetailForm reportCfgDetailForm = (RpReportCfgDetailForm) form;

		// Chuc nay nay quan trong khong the khong phan quyen
		boolean insert = rpReportCfgDetailDao.isExist(reportCfgDetailForm.getReportCode());
		if (insert) {
			if (!RightUtils.haveAction(rq, "method=save&saveType=createNew"))
				throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n th&#234;m m&#7899;i!");

		} else {
			if (!RightUtils.haveAction(rq, "method=save&saveType=update"))
				throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n s&#7917;a!");
		}

		if (reportCfgDetailForm.getRpReportCfgDetails() != null)
			rpReportCfgDetailDao.save(reportCfgDetailForm.getRpReportCfgDetails(), reportCfgDetailForm.getReportCode());
	}

	public void copy(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RpReportCfgDetailForm form)
			throws Exception {
		
		if (!RightUtils.haveAction(rq, "method=save&saveType=createNew"))
			throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n th&#234;m m&#7899;i!");
		String reportCopy = rq.getParameter("reportCopy");
		String reportPaste = rq.getParameter("reportPaste");
		List<RpReportCfgMaster> lstMasterPaste = rpReportCfgMasterDao.getByReportCode(reportPaste);
		if (lstMasterPaste.size() > 0) {
			for (RpReportCfgMaster rpReportCfgMaster : lstMasterPaste) {
				if (rpReportCfgMaster.getReportCfgDetails().size() > 0)
					throw new ResourceException("B&#225;o c&#225;o: " + reportPaste
							+ " &#273;&#227; t&#7891;n t&#7841;i c&#7845;u h&#236;nh.");
			}
		}
		List<RpReportCfgMaster> lstMasterCopy = rpReportCfgMasterDao.getByReportCode(reportCopy);
		for (RpReportCfgMaster pMaster : lstMasterPaste) {
			for (RpReportCfgMaster cMaster : lstMasterCopy) {
				if (pMaster.getPropertyCode().equals(cMaster.getPropertyCode())
						&& cMaster.getReportCfgDetails().size() > 0) {
					for (RpReportCfgDetail iDetail : cMaster.getReportCfgDetails()) {
						RpReportCfgDetail obj = new RpReportCfgDetail();
						obj.setReportCfgId(pMaster);
						obj.setConfigKey(iDetail.getConfigKey());
						obj.setConfigValue(iDetail.getConfigValue());
						obj.setErrCode(iDetail.getErrCode());
						obj.setErrDesc(iDetail.getErrDesc());
						obj.setCreatedDate(new Date());
						obj.setStatus(BigDecimal.ONE);
						rpReportCfgDetailDao.save(obj);
					}
				}
			}
		}
		throw new ResourceException("success");
	}
	@InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.setAutoGrowCollectionLimit(2000);
    }
	
	@Autowired
	ExportExcel exportExcel;
	
	public void ExportFileExcel(ModelMap model, HttpServletRequest request, HttpServletResponse rs, RpReportCfgDetailForm form) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		String reportCode = request.getParameter("reportCode");
//		if(Formater.isNull(reportCode))
//			throw new ResourceException("Ch&#7885;n m&#227; b&#225;o c&#225;o &#273;&#7875; Export");
		String errCode = request.getParameter("errCode");
		List<RpReportCfgDetail> lst = rpReportCfgDetailDao.getByReportDetails(reportCode, errCode);
		List<ExcelTempDTO> temp = new ArrayList<ExcelTempDTO>();
			for (RpReportCfgDetail details : lst) {
				ExcelTempDTO obj = new ExcelTempDTO();
				obj.setCol1(reportCode);
				obj.setCol2(details.getReportCfgId().getReportCode());
				obj.setCol3(details.getConfigKey());
				obj.setCol4(details.getConfigValue());
				obj.setCol5(details.getErrCode());
				temp.add(obj);
			}
			if(Formater.isNull(reportCode))
				map.put("reportCode", "T\u1EA5t c\u1EA3");
			else
				map.put("reportCode", reportCode);
			map.put("errCode", errCode);
		map.put("reports", temp);
		exportExcel.export("Cau_hinh_validate_bao_cao", rs, map);
	}
}
