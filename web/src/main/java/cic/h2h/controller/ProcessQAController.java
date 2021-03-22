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

import cic.h2h.dao.hibernate.CatProductDao;
import cic.h2h.form.ProcessQAFrom;
import common.util.Formater;
import dto.ProcessQA;
import entity.CatProduct;
import entity.Partner;
import frwk.controller.jdbc.SearchController;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/processQA")
public class ProcessQAController extends SearchController<ProcessQAFrom, ProcessQA>{

	@Autowired
	SysPartnerDao sysPartnerDao;
	
	@Autowired
	CatProductDao catProductDao;
	
	@Override
	public String getProcedure() {
		// TODO Auto-generated method stub
		return "jdbc_catalog.reportProcessQA(?,?,?,?,?,?,?,?)";
	}

	@Override
	public void pushParam(ModelMap model, CallableStatement cStmt, ProcessQAFrom form) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pushParamExcel(ModelMap model, CallableStatement cStmt, HttpServletRequest request, ProcessQAFrom form)
			throws Exception {
		// TODO Auto-generated method stub
		cStmt.setString("p_partner_id", request.getParameter("partnerId"));
		cStmt.setString("p_product_code", request.getParameter("productCode"));
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
		List<ProcessQA> list = new ArrayList<ProcessQA>();
		ProcessQA status = null;
		while (rs1.next()) {
			status = new ProcessQA();
			status.setMaTCTD(rs1.getString(1));
			status.setTenTCTD(rs1.getString(2));
			status.setMaSP(rs1.getString(3));
			status.setTenSP(rs1.getString(4));
			status.setSoLuongYeuCauHoi(rs1.getString(5));
			status.setSoLuongYeuChuaXuLy(rs1.getString(6));
			status.setSoLuongYeuCauDaXuLy(rs1.getString(7));
			status.setSoLuongYeuCauTCTDNhan(rs1.getString(8));
			status.setSoLuongYeuCauTCTDChuaNhan(rs1.getString(9));
			status.setSoLuongKH(rs1.getString(10));
			list.add(status);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fromDate", request.getParameter("fromDate"));
		map.put("toDate", request.getParameter("toDate"));
		map.put("productCode", request.getParameter("productCode"));
		if (!Formater.isNull(request.getParameter("partnerId")))
			map.put("partnerCode", sysPartnerDao.getObject(Partner.class, request.getParameter("partnerId")).getCode());
		map.put("reports", list);
		exportExcel.export("Bao_cao_tinh_hinh_xu_ly_hoi_tin_theo_tung_TCTD", rs, map);
	}

	@Override
	public String getResulSetName() {
		// TODO Auto-generated method stub
		return "cResult";
	}

	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "bao_cao/report_process_qa";
	}

	
	
	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ProcessQAFrom form)
			throws Exception {
		// TODO Auto-generated method stub
		model.addAttribute("dsDoiTac", sysPartnerDao.getAll());
		model.addAttribute("products", catProductDao.getAll(CatProduct.class));
	}

}
