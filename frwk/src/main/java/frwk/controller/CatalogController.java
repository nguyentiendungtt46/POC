package frwk.controller;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import common.util.Formater;
import common.util.ResourceException;
import frwk.dao.hibernate.sys.RightUtils;
import frwk.form.SearchForm;

/**
 * Class base cho tat ca cac controller co chuc nang search add del delete
 * 
 * @author ntdung1
 *
 * @param <F> Form chuc nang
 * @param <T> Model chuc nang
 */
public abstract class CatalogController<F extends SearchForm<T>, T> extends SearchController<F, T> {

	private static final Logger logger = LogManager.getLogger(CatalogController.class);

	/**
	 * Luu ban ghi: Tao moi neu chua ton tai, cap nhat neu da ton tai, kiem tra quyen luu du lieu <br>
	 * Ghi log cho chuc nang audit
	 * 
	 * @param model
	 * @param rq
	 * @param rs
	 * @param form  form.getModel() tra ve doi tuong can luu
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form) throws Exception {
		Serializable id = getDao().getId(form.getModel());

		boolean insert = false;
		if (id == null)
			insert = true;
		else {
			if (id.getClass().equals(String.class))
				insert = Formater.isNull((String) id);
			else if (id.getClass().equals(Long.class))
				insert = ((Long) id).intValue() == 0;

		}

		if (insert) {
			if (!RightUtils.haveAction(rq, "method=save&saveType=createNew"))
				throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n th&#234;m m&#7899;i!");

		} else {
			if (!RightUtils.haveAction(rq, "method=save&saveType=update"))
				throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n s&#7917;a!");
		}

		getDao().save(form.getModel());
	}

	@SuppressWarnings("unchecked")
	/**
	 * Xoa ban ghi, kiem tra quyen xoa du lieu
	 * 
	 * @param model
	 * @param rq
	 * @param rs
	 * @param form  form.getModel() chua du lieu can xoa
	 * @throws Exception
	 */
	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form) throws Exception {
		if (!RightUtils.haveAction(rq, "method=del"))
			throw new ResourceException("B&#7841;n kh&#244;ng c&#243; quy&#7873;n x&#243;a!");
		getDao().del(form.getModel());

	}

	@Autowired
	/**
	 * Session web
	 */
	private HttpSession session;

	/**
	 * Don nhan tat ca cac request tu ben ngoai, va chuyen den controller tuong ung de thuc thi <br>
	 * Xu ly loi chung cho tat ca cac request
	 */
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
	public String execute(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@ModelAttribute("formDataModelAttr") F form) throws Exception {
		String method = rq.getParameter("method");
		if (Formater.isNull(method)) {
			if (RightUtils.haveAction(rq, "method=save&saveType=update")
					|| RightUtils.haveAction(rq, "method=save&saveType=createNew"))
				model.addAttribute("save", true);
			if (RightUtils.haveAction(rq, "method=del"))
				model.addAttribute("del", true);
			if (RightUtils.haveAction(rq, "method=save&saveType=createNew"))
				model.addAttribute("add", true);
		}
		return super.execute(model, rq, rs, form);
	}

	/**
	 * Lay du lieu theo id (id duoc lay tu request) cua mot ban ghi duoic db, tra lai cho client theo dinh dang json
	 * 
	 * @param model
	 * @param rq
	 * @param rs
	 * @param form  form.getModel().getClass() loai du lieu can lay
	 * @throws Exception
	 */
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form) throws Exception {
		String id = rq.getParameter("id");
		getDao().load(form.getModel(), id);
		returnObject(rs, form.getModel());
	}

	@SuppressWarnings("unchecked")
	public void edit1(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form) throws Exception {
		String id = rq.getParameter("id");
		T o = getDao().get((Class<T>) form.getModel().getClass(), id);
		getDao().load(form.getModel(), id);
		returnObject(rs, o);
	}

	/**
	 * Copy 1 doi tuong duoi database, tra lai cho client doi tuong moi
	 * 
	 * @param model
	 * @param rq
	 * @param rs
	 * @param form  form.getModel().getClass(): kieu du lieu can copy<br>
	 *              getDao().getId(form.getModel()): Id ban ghi can copy
	 * @throws Exception Khi class form.getModel().getClass() chua co phuong thuc copy
	 */
	@SuppressWarnings("unchecked")
	public void copy(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, F form) throws Exception {
		getDao().load(form.getModel());
		try {
			Method copy = form.getModel().getClass().getDeclaredMethod("copy");
			T o = (T) copy.invoke(form.getModel());
			getDao().save(o);
			returnObject(rs, o);
		} catch (NoSuchMethodException ex) {
			throw new ResourceException(
					"Class " + form.getModel().getClass().toString() + " chua co phuong thuc copy khong doi");
		}
	}
}
