package cic.h2h.controller;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

import cic.h2h.form.ExchangeStatusForm;
import common.util.Formater;
import dto.ExchangeStatus;
import entity.Partner;
import frwk.controller.jdbc.SearchController;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/dataStatus")
public class ExchangeStatusController extends SearchController<ExchangeStatusForm, ExchangeStatus> {

	@Override
	public String getProcedure() {

		return "jdbc_catalog.exchangeSts(?,?,?,?,?,?,?)";
	}

	@Override
	public void pushParam(ModelMap model, CallableStatement cStmt, ExchangeStatusForm form) throws Exception {
		cStmt.setString("p_partner_id", form.getPartnerId());
		if (!Formater.isNull(form.getFromDate()))
			cStmt.setDate("p_start_time", new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(form.getFromDate()).getTime()));
		else cStmt.setDate("p_start_time",null);
		if (!Formater.isNull(form.getToDate()))
			cStmt.setDate("p_end_time", new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(form.getToDate()).getTime()));
		else cStmt.setDate("p_end_time", null);

	}

	@Override
	public void pushToJa(JSONArray ja, ResultSet rs1) throws Exception {
		ja.put(rs1.getString(1));
		ja.put(rs1.getString(2));
		ja.put(rs1.getString(4));
		ja.put(rs1.getString(5));
		ja.put(rs1.getString(3));
	}

	@Override
	public String getResulSetName() {
		return "cResult";
	}

	@Override
	public String getJsp() {
		return "bao_cao/data_status";
	}

	@Autowired
	SysPartnerDao sysPartnerDao;
	
	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ExchangeStatusForm form)
			throws Exception {
		model.addAttribute("dsDoiTac", sysPartnerDao.getAll());
	}
	
	@Autowired
	private ExportExcel exportExcel;

	@Override
	public void pushToExcel(HttpServletRequest request, HttpServletResponse rs, ResultSet rs1) throws Exception {
		List<ExchangeStatus> list = new ArrayList<ExchangeStatus>();
		ExchangeStatus status = null;
		while (rs1.next()) {
			status = new ExchangeStatus();
			status.setPartnerCode(rs1.getString(1));
			status.setPartnerName(rs1.getString(2));
			status.setFromDataTotal(rs1.getString(4));
			status.setToDataTotal(rs1.getString(5));
			status.setTotalRequest(rs1.getString(3));
			list.add(status);
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fromDate", request.getParameter("fromDate"));
		map.put("toDate", request.getParameter("toDate"));
		if (!Formater.isNull(request.getParameter("partnerId"))) {
			Partner partner = sysPartnerDao.get(Partner.class, request.getParameter("partnerId"));
			if (partner != null)
				map.put("partner", partner.getCode() + "- " + partner.getName());
		}
		map.put("reports", list);
		exportExcel.export("Trang_thai_truyen_du_lieu", rs, map);
		
	}

	@Override
	public void pushParamExcel(ModelMap model, CallableStatement cStmt, HttpServletRequest request,
			ExchangeStatusForm form) throws Exception {
		cStmt.setString("p_partner_id", request.getParameter("partnerId"));
		if (!Formater.isNull(request.getParameter("fromDate")))
			cStmt.setDate("p_start_time", new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("fromDate")).getTime()));
		else cStmt.setDate("p_start_time",null);
		if (!Formater.isNull(request.getParameter("toDate")))
			cStmt.setDate("p_end_time", new java.sql.Date(new SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("toDate")).getTime()));
		else cStmt.setDate("p_end_time", null);
	}

}
