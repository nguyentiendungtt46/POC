package cic.h2h.controller;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cic.ws.client.WsClient;
import common.util.Base64Utils;
import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.SysUsers;
import frwk.controller.CommonController;
import frwk.form.LoginForm;
import frwk.utils.ApplicationContext;
import vn.org.cic.h2h.ws.endpoint.cicauthen.DangNhap;
import vn.org.cic.h2h.ws.endpoint.cicauthen.PHDangNhap;
import vn.org.cic.h2h.ws.endpoint.cicqa.PHTepHoiTin;
import vn.org.cic.h2h.ws.endpoint.cicqa.TepHoiTin;

@Controller
@RequestMapping("/tepHoiTin")
public class TepHoiTinController extends CommonController<LoginForm, SysUsers> {
	private static Logger lg = LogManager.getLogger(TepHoiTinController.class);
	
	@Autowired
	private WsClient wsClient;
	
	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "qtht/tep_hoi_tin";
	}
	
	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, LoginForm form) throws Exception {
		// TODO Auto-generated method stub
	}
	
	@RequestMapping(value = "upload", method = { RequestMethod.POST })
	public void sendReport(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@RequestParam("inputFile") MultipartFile inputFile, @RequestParam("fileName") String fileName,
			@RequestParam("tokenGateWay") String tokenGateWay)
			throws Exception {
		System.out.print("start upload file");
		lg.info("start upload file");
		try {
			ApplicationContext appContext = (ApplicationContext) rq.getSession()
					.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
			if (appContext == null) {
				throw new ResourceException("Can dang nhap truoc");

			}
			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
			lg.info(user.getUsername() + " g&#7917;i t&#7879;p " + fileName);
			TepHoiTin tep = new TepHoiTin();
			//tep.setNguoiYC(user.getUsername());
			tep.setTenTep(fileName);
			if (!inputFile.isEmpty()) {
				System.out.print("start load file");
				lg.info("start load file");
				String noiDungTep = loadFile(inputFile);
				tep.setNoiDungTep(noiDungTep);
				System.out.print("end load file");
				lg.info("end load file");
			}
			else tep.setNoiDungTep("");
			//tep.setLoaiSP(fileName.substring(3, 5));
			lg.info("loai san pham: " + fileName.substring(3, 5));
			DangNhap _inlg = new DangNhap();
			_inlg.setTenDangNhap(user.getUsername());
			_inlg.setMatKhau(user.getPassword());
			PHDangNhap _rlg = wsClient.dangNhap(_inlg);
			lg.info("token: " + _rlg.getToken());
			PHTepHoiTin ph = wsClient.tepHoiTin(tep, _rlg.getToken());
			
			rs.setContentType("text/html;charset=utf-8");
			PrintWriter pw = rs.getWriter();
			
			StringWriter sw = new StringWriter();
			JAXB.marshal(ph, sw);
			String xmlString01 = sw.toString();
			//xmlString01 = StringEscapeUtils.escapeHtml(xmlString01);
			lg.info(xmlString01);
			pw.print(xmlString01);
			pw.flush();
			pw.close();
			
		} catch (ResourceException ex) {
			lg.error("Loi", ex);
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
		} catch (Exception ex) {
			lg.error(ex);
			throw ex;
		}

	}
	
	private String loadFile(MultipartFile inputFile) throws IOException {
		InputStream inputStream = new BufferedInputStream(inputFile.getInputStream());
		return Base64Utils.encodeFile(inputStream);
	}
}