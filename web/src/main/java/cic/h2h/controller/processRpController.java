package cic.h2h.controller;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.RpSumDao;
import cic.h2h.form.ProcessRpFrom;
import common.util.Formater;
import common.util.ResourceException;
import dto.ProcessRp;
import entity.Partner;
import entity.RpSum;
import frwk.controller.jdbc.SearchController;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/reportProcessRp")
public class processRpController extends SearchController<ProcessRpFrom, ProcessRp>{

	@Override
	public String getProcedure() {
		// TODO Auto-generated method stub
		return "jdbc_catalog.reportProcessRp(?,?,?,?,?,?,?)";
	}

	@Override
	public void pushParam(ModelMap model, CallableStatement cStmt, ProcessRpFrom form) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushParamExcel(ModelMap model, CallableStatement cStmt, HttpServletRequest request, ProcessRpFrom form)
			throws Exception {
		// TODO Auto-generated method stub
		cStmt.setString("p_partner_id", request.getParameter("partnerId"));
		if (!Formater.isNull(request.getParameter("fromDate")))
			cStmt.setDate("p_start_time", new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fromDate")).getTime()));
		else cStmt.setDate("p_start_time",null);
		if (!Formater.isNull(request.getParameter("toDate")))
			cStmt.setDate("p_end_time", new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("toDate")).getTime()));
		else cStmt.setDate("p_end_time", null);
	}

	@Override
	public void pushToJa(JSONArray ja, ResultSet rs1) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Autowired
	private ExportExcel exportExcel;
	
	@Override
	public void pushToExcel(HttpServletRequest request, HttpServletResponse rs, ResultSet rs1) throws Exception {
		// TODO Auto-generated method stub
		List<ProcessRp> list = new ArrayList<ProcessRp>();
		ProcessRp status = null;
		while (rs1.next()) {
			status = new ProcessRp();
			status.setMaTCTD(rs1.getString(1));
			status.setTenTCTD(rs1.getString(2));
			status.setTotalReport(rs1.getString(3));
			status.setTotalData(rs1.getString(4));
			status.setTotalAutoReport(rs1.getString(5));
			status.setTotalmanualReport(rs1.getString(6));
			status.setTotalInProcess(rs1.getString(10));
			status.setTotalNotProcess(rs1.getString(7));
			status.setTotalReturnReport(rs1.getString(8));
			status.setTotalPassToM1(rs1.getString(10));
			status.setWaitProcess(rs1.getString(9));
			list.add(status);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fromDate", request.getParameter("fromDate"));
		map.put("toDate", request.getParameter("toDate"));
		if (!Formater.isNull(request.getParameter("partnerId")))
			map.put("maTCTD", sysPartnerDao.getObject(Partner.class, request.getParameter("partnerId")).getCode());
		map.put("reports", list);
		exportExcel.export("Bao_cao_tinh_hinh_xu_ly_Bao_cao_theo_tung_TCTD", rs, map);
	}
	
	@Autowired
	RpSumDao rpSumDao;
	
	public void exportExcelDetail(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ProcessRpFrom form) throws Exception {
		String fromDate = rq.getParameter("fromDate");
		String toDate = rq.getParameter("toDate");
		String partnerCode = rq.getParameter("partnerId");
		Partner partner = sysPartnerDao.getObject(Partner.class, partnerCode);
		if (partner == null) {
			throw new ResourceException("");
		}
		List<RpSum> list = rpSumDao.reports(fromDate, toDate, partner.getCode(), null, null, null, null, null);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fromDate", fromDate);
		map.put("toDate", toDate);
		map.put("tenTCTD", partner.getName());
		for (RpSum rpSum : list) {
			if (rpSum.getTuDong() == null) {
				rpSum.setTuDongStr("");
			} else if (!rpSum.getTuDong()){
				rpSum.setTuDongStr("Tự động");
			} else {
				rpSum.setTuDongStr("Thủ công");
			}
			if (rpSum.getStatus() != null) {
				if (0 == rpSum.getStatus()) {
					rpSum.setStatusStr("Chưa xử lý");
				} else if (1 == rpSum.getStatus()) {
					rpSum.setStatusStr("1- Đang xử lý");
				} else if (2 == rpSum.getStatus()) {
					rpSum.setStatusStr("2 - Đã xử lý H2H");
				} else if (3 == rpSum.getStatus()) {
					rpSum.setStatusStr("3 - Lỗi tại H2H");
				} else if (4 == rpSum.getStatus()) {
					rpSum.setStatusStr("4- Đã chuyển sang M1");
				} else if (5 == rpSum.getStatus()) {
					rpSum.setStatusStr("5 - M1 đã hoàn thành");
				}
			}
			if (rpSum.getRedo() == null || rpSum.getRedo() == 0) {
				rpSum.setRepoStr("Không");
			} else {
				rpSum.setRepoStr("Có");
			}
		}
		map.put("reports", list);
		exportExcel.export("Bao_cao_tinh_hinh_xu_ly_Bao_cao_theo_tung_TCTD_chi_tiet", rs, map);
		
	}

	@Override
	public String getResulSetName() {
		// TODO Auto-generated method stub
		return "cResult";
	}

	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "bao_cao/report_process_rp";
	}

	@Autowired
	SysPartnerDao sysPartnerDao;
	
	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ProcessRpFrom form)
			throws Exception {
		// TODO Auto-generated method stub
		model.addAttribute("dsDoiTac", sysPartnerDao.getAll());
	}

}
