package cic.h2h.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cic.h2h.form.VanTinChungForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.SysUsers;
import frwk.controller.CommonController;
import frwk.utils.ApplicationContext;
import vn.com.cmc.service.ValidateService;
import vn.org.intergration.ws.endpoint.cicqr.DSTHamSo;
import vn.org.intergration.ws.endpoint.cicqr.PHVanTinChung;
import vn.org.intergration.ws.endpoint.cicqr.ThamSo;
import vn.org.intergration.ws.endpoint.cicqr.VanTinChung;

@Controller
@RequestMapping("/inquiry")
public class VanTinChungContrpller extends CommonController<VanTinChungForm , VanTinChung>{
	
	private static Logger lg = LogManager.getLogger(VanTinChungContrpller.class);

	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "qtht/van_tin_chung";
	}
	
	@Autowired
	ValidateService validateService;
	
	@RequestMapping(value = "s37", method = { RequestMethod.POST })
	public void searchCus(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("MaCIC") String MaCIC) throws Exception {
		VanTinChung request = new VanTinChung();
		PHVanTinChung response = null;
		try {
			ApplicationContext appContext = (ApplicationContext) rq.getSession()
					.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
			if (appContext == null) {
				throw new ResourceException("Can dang nhap truoc");

			}
			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
			request.setNguoiYeuCau(user.getUsername());
			request.setMaSP("S37");
			DSTHamSo dstHamSo = new DSTHamSo();
			ThamSo thamSo = new ThamSo();
			thamSo.setTenTS("MaCIC");
			thamSo.setGiaTriTS(MaCIC);
			dstHamSo.getThamSo().add(thamSo);
			request.setDSTHamSo(dstHamSo);
			response = validateService.inquiryS37(request, rq);
			rs.setContentType("text/html;charset=utf-8");
			PrintWriter pw = rs.getWriter();
			
			StringWriter sw = new StringWriter();
			JAXB.marshal(response, sw);
			String xmlString01 = sw.toString();
			//xmlString01 = StringEscapeUtils.escapeHtml(xmlString01);
			lg.info(xmlString01);
			pw.print(xmlString01);
			pw.flush();
			pw.close();
		} catch (ResourceException ex) {
			// TODO: handle exception
			lg.error(ex.getMessage(), ex);
			if (!Formater.isNull(ex.getMessage())) {
				rs.setContentType("text/plan;charset=utf-8");
				PrintWriter pw = rs.getWriter();
				if (!Formater.isNull(ex.getParam()))
					pw.print(String.format(getText(ex.getMessage()), ex.getParam()));
				else if (Formater.isNull(ex.getParams()))
					pw.print(String.format(getText(ex.getMessage()), ex.getParams()));
				else
					pw.print(getText(ex.getMessage()));
				pw.flush();
				pw.close();
			}
			throw ex;
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e.getMessage(), e);
		}
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, VanTinChungForm form)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
