package frwk.dao.hibernate.sys;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.SysObjects;
import entity.frwk.SysUsers;
import frwk.utils.ApplicationContext;

@Repository(value = "RightUtils")
public class RightUtils {
	private static final Logger logger = LogManager.getLogger(RightUtils.class);
	private static UserDao userDao1;
	private static SysObjectDao sysObjectDao1;
	@Autowired
	private UserDao userDao;
	@Autowired
	private SysObjectDao sysObjectDao;

	@PostConstruct
	public void init() {
		userDao1 = userDao;
		sysObjectDao1 = sysObjectDao;
	}

	public static boolean haveRight(String rightCode, ApplicationContext context) {
		SysObjects itemp = sysObjectDao1.getByObjId(rightCode);
		if (itemp == null)
			return true;
		SysUsers u = (SysUsers) context.getAttribute(ApplicationContext.USER);
		for (SysObjects o : u.getLstObj())
			if (rightCode.equals(o.getObjectId()))
				return true;
		return false;
	}

	public static boolean haveAnyRight(String rightCode, ApplicationContext context) {
		SysUsers u = (SysUsers) context.getAttribute(ApplicationContext.USER);
		return userDao1.haveAnyRight(rightCode, u.getLstObj());
	}

	public static boolean haveRight(HttpServletRequest rq, String rightCode) {
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		SysUsers u = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
		for (SysObjects o : u.getLstObj())
			if (rightCode.equals(o.getObjectId()))
				return true;
		return false;
	}

	public static boolean haveMenuGroup(String mnGroupCode, ApplicationContext context) {
		SysUsers u = (SysUsers) context.getAttribute(ApplicationContext.USER);
		for (SysObjects o : u.getLstObj()) {
			if (o.getSysObjects().getObjectId().equals(mnGroupCode))
				return true;
		}

		return true;
	}

	public static void chkRight(HttpServletRequest rq, String rightNeedChk) throws ResourceException {
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		if (!haveRight(rightNeedChk, appContext))
			throw new ResourceException("not_access");
	}

	public static boolean haveAction(HttpServletRequest rq, String action) throws ResourceException {
		String uri = rq.getRequestURI();
		String contextPath = rq.getContextPath(); // /web
		String URI = uri.substring(uri.indexOf(contextPath) + contextPath.length() + 1);
		// Admin luon vao duoc 2 chuc nang quan ly nguoi dung, quan ly nhom quyen
		ApplicationContext ac = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		SysUsers user = (SysUsers) ac.getAttribute(ApplicationContext.USER);
		if ("admin".equals(user.getUsername())) {
			if (URI.endsWith("role") || URI.endsWith("manageUser"))
				return true;
		}

		if (byPassRight(URI))
			return true;

		ApplicationContext context = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);

		String requestUrl = rq.getRequestURL().toString();
		String fullContextPath = requestUrl.substring(0, requestUrl.indexOf(contextPath) + contextPath.length() + 1); // http://localhost:7101/web/

		// Kiem tra trong quyen nsd
		SysUsers u = (SysUsers) context.getAttribute(ApplicationContext.USER);
		// if (Formater.isNull(userRights))
		// return false;
		for (SysObjects obj : u.getLstObj()) {
			if (yesItis(obj, URI, contextPath, rq, fullContextPath, action))
				return true;
		}

		// Kiem tra quyen co duoc dinh nghia trong SysRights khong
		ArrayList<SysObjects> sysRights = (ArrayList<SysObjects>) context.getAttribute(ApplicationContext.SYS_RIGHT);
		if (Formater.isNull(sysRights))
			return true;
		for (SysObjects obj : sysRights) {
			if (yesItis(obj, URI, contextPath, rq, fullContextPath, action))
				return false;
		}
		return true;

	}

	private static boolean yesItis(SysObjects obj, String URI, String contextPath, HttpServletRequest req,
			String fullContextPath, String action) {
		URL objUrl;
		try {
			objUrl = new URL(fullContextPath + obj.getAction());
		} catch (MalformedURLException e) {
			return false;
		}
		String objUri = null;
		try {
			objUri = objUrl.toURI().getPath();
			objUri = objUri.substring(objUri.indexOf(contextPath) + contextPath.length() + 1);
			if (!objUri.endsWith(""))
				objUri += "";
		} catch (Exception e) {
			return false;
		}

		if (!objUri.equalsIgnoreCase(URI))
			return false;

		// kiem tra tham so tinh (tham so dong bo qua ex: id=124)
		String sActionParam = obj.getAction().split("\\?").length > 1 ? obj.getAction().split("\\?")[1] : "";
		String[] arrActionParam = Formater.isNull(sActionParam) ? new String[] {} : sActionParam.split("&");
		String[] actionParamPair = action.split("&");
		Map<String, String> mActionParam = new HashMap<String, String>();
		for (int i = 0; i < actionParamPair.length; i++) {
			String[] actionParamInf = actionParamPair[i].split("=");
			if (actionParamInf.length != 2)
				continue;
			mActionParam.put(actionParamInf[0], actionParamInf[1]);
		}

		// True neu moi tham so trong cau hinh deu ton tai trong request
		if (arrActionParam.length > 0) {
			for (String param : arrActionParam) {
				String[] paramWithValue = param.split("=");
				String paramKey = paramWithValue[0];
				String paramValue = paramWithValue[1];
				if (!Formater.isNull(paramValue))
					paramValue = paramValue.trim().toLowerCase();
				String requestValue = mActionParam.get(paramKey);
				if (!Formater.isNull(requestValue))
					requestValue = requestValue.trim().toLowerCase();
				if (Formater.isNull(paramValue) && Formater.isNull(requestValue))
					continue;
				if (Formater.isNull(paramValue) && !Formater.isNull(requestValue))
					return false;
				if (Formater.isNull(requestValue) && !Formater.isNull(paramValue))
					return false;
				if (!paramValue.equals(requestValue))
					return false;
			}
		}
// submit den method nhung action khai bao khong co tham so method
		// else if (req.getParameter("method") != null)
		else if (action.indexOf("method") >= 0)
			return false;
		return true;
	}

	private static boolean byPassRight(String URI) {
		return false;
	}

}
