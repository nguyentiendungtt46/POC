package cic.h2h.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXB;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import cic.h2h.form.QnaInDetailForm;
import common.util.AESUtils;
import common.util.Formater;
import common.util.RandomPassWord;
import common.util.ResourceException;
import entity.frwk.SysParam;
import frwk.controller.CommonController;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.form.SysParamForm;
import intergration.cic.Authen;
import vn.org.cic.h2h.ws.endpoint.cicauthen.DangNhap;
import vn.org.cic.h2h.ws.endpoint.cicauthen.PHDangNhap;
//import intergration.cic.Authen;
import vn.org.cic.h2h.ws.endpoint.cicuserinfo.DoiMatKhau;
import vn.org.cic.h2h.ws.endpoint.cicuserinfo.PHDoiMatKhau;

@Controller
@RequestMapping("/configUserCic")
public class ConfigUsersCicController extends CommonController<SysParamForm, SysParam> {
	
	private static Logger log = LogManager.getLogger(ConfigUsersCicController.class);
	
	private static final AESUtils encode = new AESUtils();
	
	@Value("${H2H_USERNAME}")
	private String h2hUsername;
	@Value("${H2H_PASSWORD}")
	private String h2hPassword;
	
	@Autowired
	private SysParamDao sysParamDao;
	
	@Autowired
	private Authen authen;
	@Override
	public String getJsp() {
		return "sys_dict/view_config_user_cic";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysParamForm form)
			throws Exception {
//		SysParam sysParamUserCic = sysParamDao.getSysParamByCode(h2hUsername);
//		SysParam sysParamPassCic = sysParamDao.getSysParamByCode(h2hPassword);
//		model.addAttribute("sysParamUserCic", sysParamUserCic);
//		model.addAttribute("sysParamPassCic", sysParamPassCic);
	}
	
	public void callCicUserInfo(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			SysParamForm form) throws Exception {
		SysParam sysParamUserCic = sysParamDao.getSysParamByCode(h2hUsername);
		SysParam sysParamPassCic = sysParamDao.getSysParamByCode(h2hPassword);
		String username = request.getParameter("username");
		String passwordOld = request.getParameter("passwordOld");
		String passwordNew = request.getParameter("passwordNew");
		response.setContentType("text/plain;charset=utf-8");
		if(Formater.isNull(username))
			throw new ResourceException("T&#224;i kho&#7843;n kh&#244;ng &#273;&#432;&#7907;c &#273;&#7875; tr&#7889;ng");
		if(Formater.isNull(passwordOld))
			throw new ResourceException("M&#7853;t kh&#7849;u kh&#244;ng &#273;&#432;&#7907;c &#273;&#7875; tr&#7889;ng");
		if(Formater.isNull(passwordNew))
			throw new ResourceException("M&#7853;t kh&#7849;u m&#7899;i kh&#244;ng &#273;&#432;&#7907;c &#273;&#7875; tr&#7889;ng");
		System.out.println(encode.encrypt(passwordOld));
		if(!username.equals(sysParamUserCic.getValue()))
			throw new ResourceException("T&#224;i kho&#7843;n kh&#244;ng &#273;&#250;ng");
		if(!encode.encrypt(passwordOld).equals(sysParamPassCic.getValue()))
			throw new ResourceException("M&#7853;t kh&#7849;u kh&#244;ng &#273;&#250;ng");
		if(passwordNew.equals(passwordOld))
			throw new ResourceException("M&#7853;t kh&#7849;u m&#7899;i ph&#7843;i kh&#225;c m&#7853;t kh&#7849;u c&#361;");
		if(!new RandomPassWord(8, 20).check(passwordNew))
			throw new ResourceException("Sai &#273;&#7883;nh d&#7841;ng m&#7853;t kh&#7849;u");
		// call service CIC :: cicUserInfoWsdl
		log.info("call sang service CIC :: cicUserInfoWsdl.wsdl"); 
		DoiMatKhau obj = new DoiMatKhau();
		obj.setTenDangNhap(username.trim());
		obj.setMatKhauCu(passwordOld.trim());
		obj.setMatKhauMoi(passwordNew.trim());
		PHDoiMatKhau phDoiMatKhau =authen.doiMatKhau(obj);
		response.setContentType("text/html;charset=utf-8");
		PrintWriter pw = response.getWriter();
		StringWriter sw = new StringWriter();
		JAXB.marshal(phDoiMatKhau, sw);
		String xmlString01 = sw.toString();
		xmlString01 = StringEscapeUtils.escapeHtml(xmlString01);
		log.info(xmlString01);
		if(phDoiMatKhau.getMa().equals("CMM_000"))
				xmlString01 = "Th&#7921;c hi&#7879;n th&#224;nh c&#244;ng";
		pw.print(xmlString01);
		pw.flush();
		pw.close();
		// Update password DB
		log.info("Update password DB");
		String passwordNewEnCode = encode.encrypt(passwordNew);
		sysParamPassCic.setValue(passwordNewEnCode);
		sysParamDao.save(sysParamPassCic);
		log.info("END update password DB");
		// END update password DB
	}
	
	public void callAuthenCic(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			SysParamForm form) throws Exception {
		SysParam sysParamUserCic = sysParamDao.getSysParamByCode(h2hUsername);
		SysParam sysParamPassCic = sysParamDao.getSysParamByCode(h2hPassword);
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		response.setContentType("text/plain;charset=utf-8");
		if(sysParamUserCic != null || sysParamPassCic != null)
			throw new ResourceException("&#272;&#227; t&#7891;n t&#7841;i th&#244;ng tin c&#7845;u h&#236;nh");
		if(Formater.isNull(username))
			throw new ResourceException("T&#224;i kho&#7843;n kh&#244;ng &#273;&#432;&#7907;c &#273;&#7875; tr&#7889;ng");
		if(Formater.isNull(password))
			throw new ResourceException("M&#7853;t kh&#7849;u kh&#244;ng &#273;&#432;&#7907;c &#273;&#7875; tr&#7889;ng");
		if(!new RandomPassWord(8, 20).check(password))
			throw new ResourceException("Sai &#273;&#7883;nh d&#7841;ng m&#7853;t kh&#7849;u");
		// call service CIC :: cicAuthenWsdl
		log.info("call sang service CIC :: cicAuthenWsdl.wsdl"); 
		DangNhap obj = new DangNhap();
		obj.setTenDangNhap(username.trim());
		obj.setMatKhau(password.trim());
		PHDangNhap phDangNhap = authen.dangNhap(obj);
		if(phDangNhap != null) {
			if(phDangNhap.getMa() == null) { // call service thanh cong
				// Update password DB
				log.info("INSERT username and password DB");
				sysParamUserCic = new SysParam();
				sysParamUserCic.setCode(h2hUsername);
				sysParamUserCic.setName("Username connect CIC config");
				sysParamUserCic.setValue(username);
				sysParamDao.save(sysParamUserCic);
				sysParamPassCic = new SysParam();
				sysParamPassCic.setCode(h2hPassword);
				sysParamPassCic.setName("Password connect CIC config");
				sysParamPassCic.setValue(encode.encrypt(password));
				sysParamDao.save(sysParamPassCic);
				log.info("END INSERT username and password DB");
				// END update password DB
			} else if(phDangNhap.getMa().equals("CMM_999")) {
				throw new ResourceException("CMM_999 - Sai th&#244;ng tin t&#234;n &#273;&#259;ng nh&#7853;p");
			}else {
				throw new ResourceException(phDangNhap.getMa() + " - " + phDangNhap.getMoTa());
			}
		}else {
			 new ResourceException("Call service x&#225;c th&#7921;c t&#224;i kho&#7843;n CIC kh&#244;ng th&#224;nh c&#244;ng");
		}
	}
}
