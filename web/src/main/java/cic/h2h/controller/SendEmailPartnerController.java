package cic.h2h.controller;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.H2hEmailDao;
import cic.h2h.dao.hibernate.PartnerDao;
import cic.h2h.form.H2hEmailForm;
import common.util.Formater;
import entity.H2hEmail;
import entity.Partner;
import frwk.controller.CommonController;

@Controller
@RequestMapping("/sendEmailPartner")
public class SendEmailPartnerController extends CommonController<H2hEmailForm, H2hEmail> {

	static Logger lg = LogManager.getLogger(SendEmailPartnerController.class);
	
	@Autowired
	private H2hEmailDao h2hEmailDao;
	
	@Autowired
	private PartnerDao partnerDao;
	
//	@Autowired
//	private JavaMailSender mailSender;

	@Override
	public String getJsp() {
		return "send_email_partner/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, H2hEmailForm form)
			throws Exception {
		List<Partner> listParent = partnerDao.getListTCTD("");
		model.addAttribute("listParent", listParent);
	}
	
	
	public void getEmail(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, H2hEmailForm form)
			throws Exception {
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		PrintWriter out = rp.getWriter();
		try {
			Partner partner = partnerDao.getPartnerById(id);
			if(Formater.isNull(partner.getEmail())) {
				out.print("");
			}else {
				out.print(partner.getEmail());
			}
		} catch (Exception e) {
			lg.error("getEmail: "+e);
		}
		out.flush();
		out.close();
	}
	
	public void sendMail(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, H2hEmailForm form)
			throws Exception {
		rp.setContentType("text/plan;charset=utf-8");
		String emailStr = Formater.isNull(rq.getParameter("email")) ? "" : rq.getParameter("email");
		String subject = Formater.isNull(rq.getParameter("subject")) ? "" : rq.getParameter("subject");
		String contentEmail = Formater.isNull(rq.getParameter("contentEmail")) ? "" : rq.getParameter("contentEmail");
		String tctdId = Formater.isNull(rq.getParameter("parentId")) ? "" : rq.getParameter("parentId");
		PrintWriter out = rp.getWriter();
		
		Pattern pattern = Pattern.compile("^[a-z][a-z0-9_\\.]{3,32}@[a-z0-9]{2,}(\\.[a-z0-9]{2,4}){1,2}$");
		Matcher matcher;
		matcher = pattern.matcher(emailStr);
		if (matcher.find()) {
			H2hEmail h2hEmail =  new H2hEmail();
			h2hEmail.setEmailTo(emailStr);
			h2hEmail.setEmailSubject(subject);
			h2hEmail.setEmailBody(contentEmail);
			h2hEmail.setMessage("");
			h2hEmail.setType((short) 1);
			h2hEmail.setCreateTime(new Date());
			h2hEmail.setStatus((short)0);
			h2hEmail.setTctdId(tctdId);
			h2hEmailDao.save(h2hEmail);
//			out.print("Thêm email thành công!");
		}else {
			out.print("Email sai định dạng!");
			lg.error("Email sai dinh dang");
		}
		out.flush();
		out.close();
	}
}
