package cic.h2h.controller;

import java.io.IOException;
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

import cic.h2h.form.TimKiemKHForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.SysUsers;
import frwk.controller.CommonController;
import frwk.utils.ApplicationContext;
import vn.com.cmc.service.ValidateService;
import vn.org.intergration.ws.endpoint.cicqr.PHTimKiemKH;
import vn.org.intergration.ws.endpoint.cicqr.TimKiemKH;

@Controller
@RequestMapping("/search")
public class SearchCustomerController extends CommonController<TimKiemKHForm, TimKiemKH> {

	private static Logger lg = LogManager.getLogger(SearchCustomerController.class);
	
	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "qtht/tim_kiem_kh";
	}

	@Autowired
	ValidateService validateService;

	@RequestMapping(value = "customer", method = { RequestMethod.POST })
	public void searchCus(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("cusName") String cusName,
			@RequestParam("address") String address, @RequestParam("cusCode") String cusCode,
			@RequestParam("cicCode") String cicCode, @RequestParam("cusType") String cusType,
			@RequestParam("msThue") String msThue, @RequestParam("dkkd") String dkkd,
			@RequestParam("soCMT") String soCMT) throws IOException, ResourceException {
		TimKiemKH request = new TimKiemKH();
		PHTimKiemKH response = null;
		try {
			ApplicationContext appContext = (ApplicationContext) rq.getSession()
					.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
			if (appContext == null) {
				throw new ResourceException("Can dang nhap truoc");

			}
			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
			request.setTenKH(cusName);
			request.setDiaChi(address);
			request.setDKKD(dkkd);
			request.setLoaiKH(cusType);
			request.setMaCIC(cicCode);
			request.setMaKH(cusCode);
			request.setMSThue(msThue);
			request.setSoCMT(soCMT);
			request.setNguoiYeuCau(user.getUsername());

			response = validateService.sendSearchCus(request, rq);
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
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, TimKiemKHForm form)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
