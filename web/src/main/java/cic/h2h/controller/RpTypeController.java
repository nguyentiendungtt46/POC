package cic.h2h.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cic.h2h.dao.hibernate.RpTypeDao;
import cic.h2h.form.RptypeForm;
import common.util.Formater;
import entity.RpRule;
import entity.RpType;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.OrderBySqlFormula;
import frwk.form.ModelForm;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/rpType")
public class RpTypeController extends CatalogController<RptypeForm, RpType> {
	private static final Logger logger = LogManager.getLogger(RpTypeController.class);
	private Map<String, String> mapFileType;
	private Map<String, String> mapDataType;
	private Map<Short, String> mapCusType;
	private Map<Short, String> mapReportType;

	private void makeData() {
		mapFileType = new HashMap<String, String>();
		mapFileType.put("K", "l\u00E0 t\u1EC7p d\u1EEF li\u1EC7u TCTD g\u1EEDi CIC");
		mapFileType.put("P", "l\u00E0 t\u1EC7p d\u1EEF li\u1EC7u CIC g\u1EEDi l\u1EA1i TCTD");
		mapFileType.put("D", "l\u00E0 t\u1EC7p d\u1EEF li\u1EC7u \u0111i\u1EC1u ch\u1EC9nh TCTD g\u1EEDi CIC");
		mapDataType = new HashMap<String, String>();
		mapDataType.put("10",
				"l\u00E0 lo\u1EA1i d\u1EEF li\u1EC7u nh\u1EADn d\u1EA1ng kh\u00E1ch h\u00E0ng vay");
		mapDataType.put("11",
				"l\u00E0 lo\u1EA1i d\u1EEF li\u1EC7u nh\u1EADn d\u1EA1ng ch\u1EE7 th\u1EBB");
		mapDataType.put("20",
				"l\u00E0 lo\u1EA1i d\u1EEF li\u1EC7u t\u00E0i ch\u00EDnh kh\u00E1ch h\u00E0ng vay");
		mapDataType.put("31",
				"l\u00E0 lo\u1EA1i d\u1EEF li\u1EC7u h\u1EE3p \u0111\u1ED3ng t\u00EDn d\u1EE5ng kh\u00E1ch h\u00E0ng vay");
		mapDataType.put("32",
				"l\u00E0 lo\u1EA1i d\u1EEF li\u1EC7u quan h\u1EC7 t\u00EDn d\u1EE5ng c\u1EE7a kh\u00E1ch h\u00E0ng vay");
		mapDataType.put("33",
				"l\u00E0 lo\u1EA1i  d\u1EEF li\u1EC7u v\u1EC1 t\u00ECnh tr\u1EA1ng t\u00E0i kho\u1EA3n th\u1EBB t\u00EDn d\u1EE5ng");
		mapDataType.put("40",
				"l\u00E0 lo\u1EA1i  d\u1EEF li\u1EC7u b\u1EA3o \u0111\u1EA3m ti\u1EC1n vay");
		mapDataType.put("50",
				"l\u00E0 lo\u1EA1i d\u1EEF li\u1EC7u \u0111\u1EA7u t\u01B0 tr\u00E1i phi\u1EBFu v\u00E0o doanh nghi\u1EC7p");

		mapCusType = new HashMap<Short, String>();
		mapCusType.put(Short.valueOf("1"),
				"l\u00E0 lo\u1EA1i  kh\u00E1ch h\u00E0ng vay doanh nghi\u1EC7p, t\u1ED5 ch\u1EE9c");
		mapCusType.put(Short.valueOf("2"),
				"l\u00E0 lo\u1EA1i  kh\u00E1ch h\u00E0ng vay c\u00E1 nh\u00E2n, h\u1ED9 kinh doanh c\u00E1 th\u1EC3");
		mapCusType.put(Short.valueOf("3"), "l\u00E0 lo\u1EA1i ch\u1EE7 th\u1EBB t\u00EDn d\u1EE5ng");

		mapReportType = new HashMap<Short, String>();
		mapReportType.put(Short.valueOf("1"),
				"l\u00E0 lo\u1EA1i b\u00E1o c\u00E1o d\u1EEF li\u1EC7u l\u1EA7n \u0111\u1EA7u");
		mapReportType.put(Short.valueOf("2"), "l\u00E0 lo\u1EA1i b\u00E1o c\u00E1o d\u1EEF li\u1EC7u ph\u00E1t sinh");
		mapReportType.put(Short.valueOf("3"),
				"l\u00E0 lo\u1EA1i  b\u00E1o c\u00E1o d\u1EEF li\u1EC7u \u0111\u1ECBnh k\u1EF3 (th\u00E1ng /qu\u00FD/n\u0103m)");
	}

	@Override
	public String getJsp() {
		return "qtht/danh_muc_bao_cao";
	}


	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RptypeForm form)
			throws Exception {
		makeData();
		model.addAttribute("mapFileType", mapFileType);
		model.addAttribute("mapDataType", mapDataType);
		model.addAttribute("mapCusType", mapCusType);
		model.addAttribute("mapReportType", mapReportType);
		RptypeForm rptypeForm = (RptypeForm) form;
		rptypeForm.setActive(Boolean.TRUE);
		//rpTypeDao.makeData(mapFileType, mapDataType, mapCusType, mapReportType);
	}

	
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RptypeForm form) throws Exception {
		RptypeForm rptypeForm = (RptypeForm) form;
		RpType type = rptypeForm.getRpType();
		type.setFileTypeDesc(mapFileType.get(type.getFileType()));
		type.setDataTypeDesc(mapDataType.get(type.getDataType()));
		type.setCusTypeDesc(mapCusType.get(type.getCusType()));
		type.setReportTypeDesc(mapReportType.get(type.getReportType()));
		rpTypeDao.save(type);
	}
	
	@Autowired
	ExportExcel exportExcel;
	
	public void ExportFileExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RptypeForm form) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String tempCode = rq.getParameter("templateCode");
		String reportCode = rq.getParameter("reportCode");
		String fileType = rq.getParameter("fileType");
		String dataTye = rq.getParameter("dataType");
		String cusType = rq.getParameter("customrtType");
		String reportType = rq.getParameter("reportType");
		String active = rq.getParameter("active");
		List<RpType> rpTypes = rpTypeDao.reports(tempCode, reportCode, fileType, dataTye, cusType, reportType, active);
		for (RpType type : rpTypes) {
			if (type.getInActive() != null && type.getInActive())
				type.setStatus("Không hoạt động");
			else
				type.setStatus("Hoạt động");
		}
		map.put("reports", rpTypes);
		exportExcel.export("Danh_muc_bao_cao", rs, map);
	}

	@Override
	public BaseDao<RpType> createSearchDAO(HttpServletRequest request, RptypeForm form) throws Exception {
		RpTypeDao dao = new RpTypeDao();
		if (!Formater.isNull(form.getKeyWord())) {
			Criterion searchFull = Restrictions.like(RpType.ID, form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase();

			Short temp = null;
			try {
				temp = Short.valueOf(form.getKeyWord());
			} catch (Exception e) {
				logger.info("Keyword not is short type");
			}
			if (temp != null) {
				searchFull = Restrictions.or(searchFull, Restrictions
						.sqlRestriction("upper(to_char({alias}.CUS_TYPE)) like '%" + form.getKeyWord() + "%'"));
				searchFull = Restrictions.or(searchFull, Restrictions
						.sqlRestriction("upper(to_char({alias}.DATA_TYPE)) like '%" + form.getKeyWord() + "%'"));
				searchFull = Restrictions.or(searchFull, Restrictions
						.sqlRestriction("upper(to_char({alias}.REPORT_TYPE)) like '%" + form.getKeyWord() + "%'"));
			}

			searchFull = Restrictions.or(searchFull,
					Restrictions.like(RpType.FILE_TYPE, form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase());
			searchFull = Restrictions.or(searchFull,
					Restrictions.like(RpType.CUS_TYPE_DESC, form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase());
			searchFull = Restrictions.or(searchFull,
					Restrictions.like(RpType.FILE_TYPE_DESC, form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase());
			searchFull = Restrictions.or(searchFull,
					Restrictions.like(RpType.DATA_TYPE_DESC, form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase());
			searchFull = Restrictions.or(searchFull,
					Restrictions.like(RpType.REPORT_TYPE_DESC, form.getKeyWord(), MatchMode.ANYWHERE).ignoreCase());
			dao.addRestriction(searchFull);
		}
		RptypeForm rptypeForm = (RptypeForm) form;
		if (!Formater.isNull(rptypeForm.getCusType()))
			dao.addRestriction(Restrictions.eq(RpType.CUS_TYPE, rptypeForm.getCusType()));
		if (!Formater.isNull(rptypeForm.getDataType()))
			dao.addRestriction(Restrictions.eq(RpType.DATA_TYPE, rptypeForm.getDataType()));
		if (!Formater.isNull(rptypeForm.getFileType()))
			dao.addRestriction(Restrictions.eq(RpType.FILE_TYPE, rptypeForm.getFileType()));
		if (!Formater.isNull(rptypeForm.getReportType()))
			dao.addRestriction(Restrictions.eq("reportType", rptypeForm.getReportType()));
		if (!Formater.isNull(rptypeForm.getId()))
			dao.addRestriction(Restrictions.like(RpType.ID, rptypeForm.getId(), MatchMode.ANYWHERE).ignoreCase());
		if (rptypeForm.getActive() != null && rptypeForm.getActive())
			dao.addRestriction(Restrictions.or(Restrictions.isNull(RpType.INACTIVE),
					Restrictions.eq(RpType.INACTIVE, Boolean.FALSE)));		
		if (!Formater.isNull(rptypeForm.getTemplateCode()))
			dao.addRestriction(Restrictions.like(RpType.TEMPLATE_CODE, rptypeForm.getTemplateCode(), MatchMode.ANYWHERE).ignoreCase());
		
		// Order
		dao.addOrder(OrderBySqlFormula.sqlFormula("CASE FILE_TYPE WHEN 'K' THEN 0 WHEN 'P' THEN 1 WHEN 'D' THEN 2 ELSE 3 END asc"));
		dao.addOrder(Order.asc(RpType.DATA_TYPE));
		dao.addOrder(Order.asc(RpType.CUS_TYPE));
		dao.addOrder(Order.asc(RpType.REPORT_TYPE));
		return dao;
	}

	@Override
	public void pushToJa(JSONArray ja, RpType rpType, RptypeForm modal) throws Exception {
		ja.put(rpType.getTemplateCode());
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + rpType.getId() + "\")'>" + rpType.getId()
				+ "</a>");
		ja.put(rpType.getFileType() + "-" + rpType.getFileTypeDesc());
		ja.put(rpType.getDataType() + "-" + rpType.getDataTypeDesc());
		ja.put(rpType.getCusType() + "-" + rpType.getCusTypeDesc());
		ja.put(rpType.getReportType() + "-" + rpType.getReportTypeDesc());
		if (rpType.getInActive() != null && rpType.getInActive())
			ja.put("Kh\u00F4ng ho\u1EA1t \u0111\u1ED9ng");
		else
			ja.put("Ho\u1EA1t \u0111\u1ED9ng");

	}

	@Autowired
	private RpTypeDao rpTypeDao;

	@Override
	public BaseDao<RpType> getDao() {
		return rpTypeDao;
	}

}
