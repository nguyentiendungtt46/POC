package frwk.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.util.Formater;
import common.util.LocaleUtils;
import common.util.Messages;
import common.util.ResourceException;
import entity.frwk.SysUsers;
import frwk.form.ModelForm;
import frwk.utils.ApplicationContext;

/**
 * Base controller cho tat ca controller co trong he thong
 * 
 * @author ntdung1
 *
 * @param <F>
 * @param <T>
 */
public abstract class CommonController<F extends ModelForm<T>, T> {
	private static Logger lg = LogManager.getLogger(CommonController.class);

	public abstract String getJsp();

	/**
	 * Form chua du lieu client submit len
	 */
	private F modelForm;

	public F getModelForm() {
		return modelForm;
	}

	@Autowired
	/**
	 * Session web
	 */
	private HttpSession session;

	public SysUsers getSessionUser() {
		ApplicationContext appContext = (ApplicationContext) session
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		return (SysUsers) appContext.getAttribute(ApplicationContext.USER);
	}

	/**
	 * Don nhan tat ca cac request tu ben ngoai, va chuyen den controller tuong ung de thuc thi <br>
	 * Xu ly loi chung cho tat ca cac request
	 * 
	 * @param model
	 * @param rq
	 * @param rs
	 * @param form
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public String execute(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@ModelAttribute("formDataModelAttr") F form) throws Exception {
		try {
			String method = rq.getParameter("method");
			if (!validToken(rq, rs, form, method))
				return null;
			this.modelForm = form;
			if (!Formater.isNull(method)) {
				redirectMethod(model, rq, rs, form, method);
				return null;
			}
			initData(model, rq, rs, form);
			long time = Calendar.getInstance().getTimeInMillis();
			String tokenId = StringEscapeUtils.escapeHtml(UUID.randomUUID().toString()) + "---" + time;
			String tokenIdKey = StringEscapeUtils.escapeHtml(UUID.randomUUID().toString()) + "---" + time;
			rq.getSession().setAttribute(tokenIdKey, tokenId);
			rq.setAttribute("tokenId", tokenId);
			rq.setAttribute("tokenIdKey", tokenIdKey);
			model.addAttribute("formDataModelAttr", form);
			return getJsp();
		} catch (Exception ex) {
			ResourceException rse = null;
			if (ex instanceof ResourceException)
				rse = (ResourceException) ex;
			else {
				Throwable cause = ex.getCause();
				while (cause != null) {
					if (cause instanceof ResourceException) {
						rse = (ResourceException) cause;
						break;
					}
					cause = cause.getCause();
				}
			}
			if (rse != null) {
				if (!Formater.isNull(rse.getMessage())) {
					rs.setContentType(MediaType.TEXT_HTML_VALUE);
					PrintWriter pw = rs.getWriter();
					if (!Formater.isNull(rse.getParam()))
						pw.print(String.format(getText(rse.getMessage()), rse.getParam()));
					else if (!Formater.isNull(rse.getParams()))
						pw.print(String.format(getText(rse.getMessage()), rse.getParams()));
					else
						pw.print(getText(rse.getMessage()));
					pw.flush();
					pw.close();
					// Throw to rollback transaction
					throw rse;
				}
			}
			lg.error("Loi", ex);
			throw ex;
		}

	}

	protected boolean validToken(HttpServletRequest rq, HttpServletResponse rs, F form, String method)
			throws IOException {
		if (Formater.isNull(method) || byPassToken(rq))
			return true;

		String tokenIdKey = form.getTokenIdKey();
		if (Formater.isNull(tokenIdKey)) {
			if (!"XMLHttpRequest".equals(rq.getHeader("X-Requested-With"))) {
				rs.sendRedirect("login");
				return false;
			} else {
				rs.setContentType(MediaType.TEXT_HTML_VALUE);
				PrintWriter pw = rs.getWriter();
				pw.print("invalid token");
				pw.flush();
				pw.close();
				return false;
			}
		} else {
			if (form.getTokenId() == null || !form.getTokenId().equals(session.getAttribute(tokenIdKey))) {
				if (!"XMLHttpRequest".equals(rq.getHeader("X-Requested-With"))) {
					rs.sendRedirect("login");
					return false;
				} else {
					rs.setContentType(MediaType.TEXT_HTML_VALUE);
					PrintWriter pw = rs.getWriter();
					pw.print("invalid token");
					pw.flush();
					pw.close();
					return false;
				}
			}

		}
		return true;
	}

	protected boolean byPassToken(HttpServletRequest req) {
		String uri = req.getRequestURI();
		String contextPath = req.getContextPath(); // /web
		String URI = uri.substring(uri.indexOf(contextPath) + contextPath.length() + 1);

		if (URI.equals("help"))
			return true;

		if (URI.equals("checkright"))
			return true;
		if (URI.equals("locale"))
			return true;
		if (URI.equals("common"))
			return true;

		return false;
	}

	/**
	 * Thuc hien cac noi dung khi nsd bat dau vao chuc nang
	 * 
	 * @param model
	 * @param rq
	 * @param rs
	 * @param form
	 * @throws Exception
	 */
	public abstract void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form)
			throws Exception;

	/**
	 * Tim kiem phuong thuc xu ly request trong controller, thuc thi phuong thuc
	 * 
	 * @param model
	 * @param rq
	 * @param rs
	 * @param form
	 * @param method Ten phuong thuc thuc thi thi
	 * @throws Exception
	 */

	/**
	 * Tim kiem phuong thuc xu ly request trong controller, thuc thi phuong thuc
	 * 
	 * @param model
	 * @param rq
	 * @param rs
	 * @param form
	 * @param method Ten phuong thuc thuc thi thi
	 * @throws Exception
	 */
	public void redirectMethod(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form, String method)
			throws Exception {
		Method mt = getMethod(method,
				new Class[] { ModelMap.class, HttpServletRequest.class, HttpServletResponse.class, form.getClass() });
		if (mt == null)
			throw new ResourceException("Khong ton tai phuong thuc xu ly");

		if (mt.isAnnotationPresent(HttpMethod.class)) {
			Annotation anotation = mt.getAnnotation(HttpMethod.class);
			HttpMethod httpMethod = (HttpMethod) anotation;
			String smtmethod = httpMethod.method() == RequestMethod.GET ? "GET" : "POST";
			if (!smtmethod.equals(rq.getMethod()))
				throw new ResourceException("action only support " + smtmethod);
		}
		mt.invoke(this, model, rq, rs, form);
	}

	public void redirectMethod1(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form, String method)
			throws Exception {
		Method mt = null;
		try {
			mt = this.getClass().getMethod(method, new Class[] { ModelMap.class, HttpServletRequest.class,
					HttpServletResponse.class, form.getClass() });
		} catch (NoSuchMethodException ex) {
			throw new ResourceException(
					String.format("Khong ton tai phuong thuc %s(%s,%s,%s,%s)", new Object[] { method, "ModelMap.class",
							"HttpServletRequest.class", "HttpServletResponse.class", form.getClass().getName() }));
		}
		mt.invoke(this, model, rq, rs, form);

	}

	private Method getMethod(String methodName, Class<?>[] parameterTypes) {
		for (Method method : this.getClass().getMethods()) {
			if (!method.getName().equals(methodName))
				continue;
			Class<?>[] clsParameterTypes = method.getParameterTypes();
			if (clsParameterTypes.length != parameterTypes.length)
				continue;
			for (int i = 0; i < clsParameterTypes.length; i++) {
				boolean iOk = true;
				if (!clsParameterTypes[i].equals(parameterTypes[i])) {
					iOk = false;
					break;
				}
				if (!iOk)
					continue;
			}
			return method;
		}
		return null;

	}

	private static final String MESSAGES_BUNDLE = Messages.BUNDLE_MESSAGE_NAME;

	protected String getText(String key, final Object... args) {
		// return getCurrentMessages(MESSAGES_BUNDLE).get(key, args);
		return key;
	}

	private Messages getCurrentMessages(String baseName) {
		return Messages.buildMessages(baseName, getCurrentLocale());
	}

	protected Locale getCurrentLocale() {
		Locale locale = LocaleContextHolder.getLocale();
		return LocaleUtils.fullLocale(locale);
	}

	/**
	 * Tra lai cho client mot mang json cua tap hop dau vao
	 * 
	 * @param rs
	 * @param lst
	 * @throws IOException
	 */
	public void returnJsonArray(HttpServletResponse rs, Collection<?> lst) throws IOException {
		String lstRmAsJson = new ObjectMapper().writeValueAsString(lst);
		rs.setContentType(MediaType.APPLICATION_JSON_VALUE);
		rs.setHeader("Cache-Control", "no-store");
		PrintWriter out = rs.getWriter();
		JSONArray jsonArray = new JSONArray(lstRmAsJson);
		out.print(jsonArray);
		out.close();
	}

	public void returnObject(HttpServletResponse rs, Object obj) throws IOException {
		rs.setContentType(MediaType.APPLICATION_JSON_VALUE);
		rs.setHeader("Cache-Control", "no-store");
		PrintWriter out = rs.getWriter();
		JSONObject jsonObject = new JSONObject(new ObjectMapper().writeValueAsString(obj));
		out.print(jsonObject);
		out.close();
	}

	public void returnJsonObj(HttpServletResponse rs, JSONObject obj) throws IOException {
		rs.setContentType(MediaType.APPLICATION_JSON_VALUE);
		rs.setHeader("Cache-Control", "no-store");
		PrintWriter out = rs.getWriter();
		out.print(obj);
		out.close();
	}
}
