package cic.h2h.controller;

import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cic.ws.client.WsClient;
import common.util.DefinedException;
import common.util.Formater;
import common.util.RandomPassWord;
import common.util.ResourceException;
import entity.frwk.SysObjects;
import entity.frwk.SysParam;
import entity.frwk.SysSecurity;
import entity.frwk.SysUsers;
import entity.frwk.UserLog;
import frwk.constants.Constants;
import frwk.controller.CommonController;
import frwk.controller.HttpMethod;
import frwk.dao.hibernate.sys.SysObjectDao;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.dao.hibernate.sys.SysSecurityDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.dao.hibernate.sys.UserDao;
import frwk.dao.hibernate.sys.UserLogDao;
import frwk.form.LoginForm;
import frwk.utils.ApplicationContext;

@Controller
@RequestMapping("/login")
public class LoginController extends CommonController<LoginForm, SysUsers> {

	@Autowired
	private WsClient wsClient;

	@Override
	public String getJsp() {
		return "qtht/login";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, LoginForm form)
			throws Exception {

		Object object = rq.getSession().getAttribute("login");
		String login = object == null ? "" : object.toString();
		if (login == "false") {
			model.addAttribute("status", "false");
		} else if (login == "exprired") {
			model.addAttribute("status", "exprired");
		} else if (login == "firstLogin") {
			model.addAttribute("status", "firstLogin");
		} else {
			model.addAttribute("status", "");
		}

	}

	private static Logger lg = LogManager.getLogger(LoginController.class);
	@Autowired
	private UserDao userDao;
	@Autowired
	private SysObjectDao sysObjectDao;

	@Autowired
	private SysSecurityDao sysSecurityDao;

	@Autowired
	private MessageSource messageSource;
	@HttpMethod(method = RequestMethod.POST)
	public void loginProcess(ModelMap map, HttpServletRequest rq, HttpServletResponse rs, LoginForm form)
			throws Exception {

		// Authenticate
		String username = rq.getParameter("username");
		String password = rq.getParameter("password");
		SysUsers u = userDao.getActiveUserByUserName(username);
		if (u == null) {
			throw new ResourceException(Constants.LOGIN_FAIL);
		}
		if (!u.isActive()) {
			throw new ResourceException(Constants.INACTIVE);
		}

		// Password fail
		if (!validatePassword(u, password)) {
			SysSecurity security = sysSecurityDao.findByCodeAndActive(Constants.BM003, true);
			if (security != null && !Formater.isNull(security.getValue())) {
				long countLoginFaild = u.getLoginErrorTimes();
				if (countLoginFaild >= Long.parseLong(security.getValue())) {
					u.setBlockTime(new Date());
				} else {
					if (countLoginFaild == 0)
						countLoginFaild = 1;
					u.setLoginErrorTimes(++countLoginFaild);
				}
				// Khong throw exception, neu throw se khong commit duoc
				rs.setContentType("text/plan;charset=utf-8");
				PrintWriter pw = rs.getWriter();
				pw.print(Constants.LOGIN_FAIL);
				pw.flush();
				pw.close();
				return;
			}

		} else {
			if (u.getLoginErrorTimes() > 0)
				u.setLoginErrorTimes(1);
		}

		// Password bi khoa
		if (Formater.isNull(u.getPasswordStatus())) {
			// Cache user infor
			ApplicationContext appContext = new ApplicationContext();
			appContext.setAttribute(ApplicationContext.USER, u);
			rq.getSession().setAttribute(ApplicationContext.APPLICATIONCONTEXT, appContext);
			throw new ResourceException(Constants.FIRSTS_LOGIN);
		}

		if (u.getBlockTime() != null) {
			Calendar temp = Calendar.getInstance();
			temp.setTime(u.getBlockTime());
			temp.add(Calendar.MINUTE, 5);
			// Chua het thoi gian cho 5 phut
			if (temp.compareTo(Calendar.getInstance()) > 0)
				throw new ResourceException(Constants.LOGIN_EXPRIRED);
		}

		// Ghi log dang nhap
		UserLog lg = new UserLog();
		lg.setAction("login");
		lg.setUserId(u.getUsername());
		userLogDao.writeLoginLog(lg);

		// Cache user infor
		ApplicationContext appContext = new ApplicationContext();
		appContext.setAttribute(ApplicationContext.USER, u);
		sysObjectDao.getUserRight(u);
		appContext.setAttribute(ApplicationContext.SYS_RIGHT, sysObjectDao.getAll(SysObjects.class));
		rq.getSession().setAttribute(ApplicationContext.APPLICATIONCONTEXT, appContext);

		// Mat khau het han, bat cua so thay doi mat khau
		if (passWordExpired(u.getPwdDate())) {
			rs.setContentType("text/plan;charset=utf-8");
			PrintWriter pw = rs.getWriter();
			pw.print(Constants.EXPRIRED);
			pw.flush();
			pw.close();
		}

	}

	private boolean passWordExpired(Date pwDate) {
		SysSecurity expriredDays = sysSecurityDao.findByCodeAndActive("PW_EXPIRED_IN", true);
		if (expriredDays == null || Formater.isNull(expriredDays.getValue()))
			return false;
		Calendar c = Calendar.getInstance();
		c.setTime(pwDate);
		c.add(Calendar.DATE, Integer.parseInt(expriredDays.getValue()));
		if (c.after(Calendar.getInstance())) {
			return false;
		} else {
			return true;
		}
	}

	public void changepassword(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, LoginForm form)
			throws Exception {

		String oldPass = rq.getParameter("oldPassWord");
		String newPass = rq.getParameter("newPassWord");
		String firstLogin = rq.getParameter("firstLogin");

		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
		if (!validatePassword(user, oldPass)) {
			rp.setContentType("text/plain;charset=utf-8");
			PrintWriter pw = rp.getWriter();
			pw.print(getText("M&#7853;t kh&#7849;u c&#361; kh&#244;ng &#273;&#250;ng"));
			pw.flush();
			pw.close();
		} else if (newPass.equals(oldPass)) {
			rp.setContentType("text/plain;charset=utf-8");
			PrintWriter pw = rp.getWriter();
			pw.print(getText("M&#7853;t kh&#7849;u m&#7899;i kh&#244;ng h&#7907;p l&#7879;"));
			pw.flush();
			pw.close();
		} else if (!new RandomPassWord(8, 20).check(newPass)) {
			rp.setContentType("text/plain;charset=utf-8");
			PrintWriter pw = rp.getWriter();
			pw.print(getText("Sai &#273;&#7883;nh d&#7841;ng m&#7853;t kh&#7849;u"));
			pw.flush();
			pw.close();
		} else {
			// new encode
			newPass = encode.encode(newPass);
			sysUsersDao.changePassword(user, newPass);
			if ("firstLogin".equalsIgnoreCase(firstLogin)) {
				user.setPasswordStatus("1");
				sysUsersDao.save(user);
				rp.setContentType("text/plain;charset=utf-8");
				PrintWriter pw = rp.getWriter();
				pw.print(getText("success"));
				pw.flush();
				pw.close();
			}
		}

	}

	public void logout(ModelMap map, HttpServletRequest rq, HttpServletResponse rs, LoginForm form) throws Exception {
		rq.getSession().removeAttribute(ApplicationContext.APPLICATIONCONTEXT);

	}

	@Autowired
	private UserLogDao userLogDao;
	@Autowired
	private SysUsersDao sysUsersDao;

	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public String execute(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@ModelAttribute("loginForm") LoginForm form) throws Exception {

		// form.setUser("ntdung");
		// form.setPass("123");
		// loginProcess(model, rq, rs, form);
		// model.addAttribute("loginForm", form);
		// return getJsp();
		System.out.println(form.getUser());
		return super.execute(model, rq, rs, form);

	}

	private boolean adAuthenticate(String user, String pass) {
		Hashtable env = new Hashtable(11);

		boolean b = false;

		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, "ldap://ovd.vietinbank.vn:389");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, "uid=" + user + ",ou=system");
		env.put(Context.SECURITY_CREDENTIALS, pass);

		try {
			// Create initial context
			DirContext ctx = new InitialDirContext(env);

			// Close the context when we're done
			b = true;
			ctx.close();

		} catch (NamingException e) {
			b = false;
		}
		return b;
	}

	@Autowired
	private SysParamDao sysParamDao;

	private boolean validatePassword(SysUsers u, String pass) throws Exception {
		SysParam LDAP_AUTHEN = sysParamDao.getSysParamByCode("LDAP_AUTHEN");

		if (LDAP_AUTHEN == null || !"true".equalsIgnoreCase(LDAP_AUTHEN.getValue())) {
			// return u.getPassword().equals(userDao.getHash(u.getUsername(), pass));
			// new encode
			return encode.matches(pass, u.getPassword());
		}
		if (!"true".equalsIgnoreCase(LDAP_AUTHEN.getValue()))
			// return u.getPassword().equals(userDao.getHash(u.getUsername(), pass));
			return encode.matches(pass, u.getPassword());
		return ldapAuthen(u.getUsername(), pass);
	}

	private static final BCryptPasswordEncoder encode = new BCryptPasswordEncoder();

	private boolean ldapAuthen(String userName, String passWord) throws Exception {
		if (Formater.isNull(passWord))
			throw new DefinedException("password is null");

		return wsClient.loginM6(userName, passWord);
//		try {
//		} catch (Exception nex) {
//			System.out.println("LDAP Connection: FAILED");
//			nex.printStackTrace();
//			return false;
//		}

	}

}
