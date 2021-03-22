package cic.h2h.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.ReportDao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import cic.h2h.form.ReportDTOForm;
import cic.h2h.form.ReportSecurityDTOForm;
import common.util.DateTimeUtil;
import common.util.Formater;
import dto.ReportDTO;
import entity.Partner;
import frwk.controller.CommonController;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/reportPartnerViolate")
public class ReportPartnerViolateController extends CommonController<ReportDTOForm, ReportDTO> {

	private static Logger lg = LogManager.getLogger(ReportPartnerViolateController.class);

	@Autowired
	private SysPartnerDao sysPartnerDao;

	@Autowired
	private ServiceInfoDao serviceInfoDao;

	@Autowired
	private ExportExcel exportExcel;
	//
	// @Override
	// public BaseDao<ReportSecurityDTO> createSearchDAO(HttpServletRequest request,
	// ReportSecurityDTOForm form)
	// throws Exception {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// protected void pushToJa(JSONArray ja, ReportSecurityDTO e,
	// ReportSecurityDTOForm modelForm) throws Exception {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public BaseDao<ReportSecurityDTO> getDao() {
	// // TODO Auto-generated method stub
	// return reportSecurityDTODao;
	// }

	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "bao_cao/report_partner_violate";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ReportDTOForm form)
			throws Exception {
		// TODO Auto-generated method stub
		List<Partner> dsDonVi = sysPartnerDao.getAll();
		Date now = new Date();
		Date preMonth = DateTimeUtil.addDate(now, -1, 2);
		model.addAttribute("endDate", DateTimeUtil.dateTime2String(now, "dd/MM/yyyy"));
		model.addAttribute("startDate", DateTimeUtil.dateTime2String(preMonth, "dd/MM/yyyy"));
		model.addAttribute("dsDonVi", dsDonVi);
		model.addAttribute("serviceInfos", serviceInfoDao.getAllGWService());
	}

	@Autowired
	private ReportDao reportDao;

	public void exportExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ReportDTOForm form)
			throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String branchCode = rq.getParameter("branchCode");
			String violateCode = rq.getParameter("violateCode");
			String fromDate = rq.getParameter("fromDate");
			String toDate = rq.getParameter("toDate");
			List<ReportDTO> lst = reportDao.getReportPartnerViolate(branchCode, violateCode, fromDate, toDate);
			if(Formater.isNull(branchCode))
				map.put("branchCode", "T\u1EA5t c\u1EA3");
			else
				map.put("branchCode", branchCode);
			if(Formater.isNull(violateCode))
				map.put("violateCode", "T\u1EA5t c\u1EA3");
			else
				map.put("violateCode", violateCode);
			map.put("fromDate", fromDate);
			map.put("toDate", toDate);
			map.put("reports", lst);
			exportExcel.export("Bao_cao_danh_sach_tctd_vi_pham", rs, map);
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
		}
	}

}
