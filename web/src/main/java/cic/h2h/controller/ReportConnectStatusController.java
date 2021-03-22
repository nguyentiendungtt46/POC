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
import cic.h2h.dao.hibernate.ReportSecurityDTODao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import cic.h2h.form.ReportSecurityDTOForm;
import common.util.DateTimeUtil;
import dto.ReportDTO;
import dto.ReportSecurityDTO;
import entity.Partner;
import frwk.controller.CommonController;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/reportConnectStatus")
public class ReportConnectStatusController extends CommonController<ReportSecurityDTOForm, ReportSecurityDTO> {

	private static Logger lg = LogManager.getLogger(ReportConnectStatusController.class);

	@Autowired
	private SysPartnerDao sysPartnerDao;

	@Autowired
	private ServiceInfoDao serviceInfoDao;

	@Autowired
	private ReportSecurityDTODao reportSecurityDTODao;

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
		return "bao_cao/report_connect_status";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ReportSecurityDTOForm form)
			throws Exception {
		// TODO Auto-generated method stub
//		List<Partner> dsDonVi = sysPartnerDao.getAll();
//		model.addAttribute("dsDonVi", dsDonVi);
		model.addAttribute("serviceInfos", serviceInfoDao.getAllGWService());
	}

	@Autowired
	private ReportDao reportDao;

	public void exportExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ReportSecurityDTOForm form)
			throws Exception {
		try {
			int minute = -1;
			String serviceInfos = rq.getParameter("serviceInfos");
			Date end = new Date();
			Date start = DateTimeUtil.addMinute(end, minute);
			String endTime = DateTimeUtil.dateTime2String(end);
			String startTime = DateTimeUtil.dateTime2String(start);
			Map<String, Object> map = new HashMap<String, Object>();

			List<ReportDTO> reports = reportDao.getReportConnectStatus(serviceInfos, startTime, endTime);
			if (reports != null) {
				reports.forEach(r->{
					if ("1".equals(r.getServiceStatus())) {
						r.setServiceStatus("Active");
					} else {
						r.setServiceStatus("Không active");
					}
				});
			}
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("reports", reports);
			exportExcel.export("Bao_cao_tinh_trang_ket_noi_cac_service", rs, map);
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
		}
	}

}
