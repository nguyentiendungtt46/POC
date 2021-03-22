package cic.h2h.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cic.h2h.form.ReportForm;
import cic.ws.client.WsClient;
import cic.ws.model.PHTepBaoCaoVanTin;
import cic.ws.model.TepBaoCaoVanTin;
import common.util.Formater;
import entity.frwk.SysUsers;
import frwk.controller.CommonController;
import frwk.form.LoginForm;
import frwk.form.ModelForm;

@Controller
@RequestMapping("/rp")
public class ReportController extends CommonController<ReportForm, SysUsers> {

	@Override
	public String getJsp() {
		return "bao_cao/van_tin_kq";
	}

	public void queryRs(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ReportForm form)
			throws Exception {
		TepBaoCaoVanTin vt = new TepBaoCaoVanTin();
		String fileName = Formater.isNull(form.getFileName())?"":form.getFileName().trim();
		vt.setTenTep(fileName);
		vt.setNguoiYC(getSessionUser().getUsername());
		PHTepBaoCaoVanTin tl = wsClient.chkReport(vt);
		
		rs.setContentType("text/html;charset=utf-8");
		PrintWriter pw = rs.getWriter();
		
		StringWriter sw = new StringWriter();
		JAXB.marshal(tl, sw);
		String xmlString01 = sw.toString();
//		logger.info(xmlString01);
		
		pw.print(xmlString01);
		pw.flush();
		pw.close();

	}
	private static Logger logger = LogManager.getLogger(ReportController.class);

	private ReportForm reportForm;

	public ReportForm getReportForm() {
		return reportForm;
	}

	public void setReportForm(ReportForm reportForm) {
		this.reportForm = reportForm;
	}

	@Autowired
	private WsClient wsClient;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ReportForm form)
			throws Exception {
		
	}

	
	
}
