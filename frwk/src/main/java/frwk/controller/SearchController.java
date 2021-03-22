package frwk.controller;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import common.util.Formater;
import common.util.ResourceException;
import frwk.dao.hibernate.BaseDao;
import frwk.form.SearchForm;
/**
 * Base class controller cho tat cac cac controller co tim kiem
 * @author ntdung1 
 * 
 * @param <F> Form chuc nang
 * @param <T> Model chuc nang
 */
public abstract class SearchController<F extends SearchForm<T>, T> extends CommonController<F, T> {

	private static Logger lg = LogManager.getLogger(SearchController.class);
	/**
	 * Xu ly action tim kiem, tra ve client ket qua tim kiem theo trang va tong so ban ghi
	 * @param model
	 * @param request
	 * @param response
	 * @param form Du lieu tim kiem
	 * @throws Exception
	 */
	public void datatable(ModelMap model, HttpServletRequest request, HttpServletResponse response, F form)
			throws Exception {
		String keyWord = request.getParameter("sSearch");
		if (!Formater.isNull(keyWord))
			form.setKeyWord(keyWord);

		BaseDao<T> dao = createSearchDAO(request, form);

		int start = 0;
		String sStart = request.getParameter("iDisplayStart");
		if (sStart != null) {
			start = Integer.parseInt(sStart);
			if (start < 0) {
				start = 0;
			}
		}

		int amount = 10;
		String sAmount = request.getParameter("iDisplayLength");
		if (sAmount != null) {
			amount = Integer.parseInt(sAmount);
			if (amount < 0) {
				amount = 10;
			}
		}

		preSearch(request, response, form);

		SearchParam param = new SearchParam();
		param.setBeginIndex(start);
		param.setPageSize(amount);
		dao.setSearchParam(param);

		JSONObject result = getData(request, dao, form);

		long total = -1;
		try {
			total = getTotalRecordCount(dao);
		} catch (Exception e1) {
			lg.error(e1);
			throw e1;
		}
		result.put("iTotalRecords", total);
		result.put("iTotalDisplayRecords", total);
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("utf-8");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		out.print(result);
		out.close();
	}

	public void preSearch(HttpServletRequest request, HttpServletResponse response, F form) throws Exception {

	}
	/**
	 * Dao object loc du lieu theo du lieu cua form submit len
	 * @param request
	 * @param form Chua du lieu client gui
	 * @return
	 * @throws Exception
	 */
	public abstract BaseDao<T> createSearchDAO(HttpServletRequest request, F form) throws Exception;
	/**
	 * Tao object json ket qua theo dieu kien tim kiem
	 * @param request
	 * @param dao
	 * @param form
	 * @return
	 * @throws Exception
	 */
	private JSONObject getData(HttpServletRequest request, BaseDao<T> dao, F form) throws Exception {
		JSONObject result = new JSONObject();

		pushData(result, dao, form);
		return result;
	}
	/**
	 * Thuc hien tim kiem database, va push vao mang json cua object result
	 * @param result Doi tuong chua du lieu lay tu database
	 * @param dao Object lay du lieu
	 * @param form Du lieu nsd submit
	 * @throws Exception
	 */
	private void pushData(JSONObject result, BaseDao<T> dao, F form) throws Exception {
		JSONArray array = new JSONArray();
		long iIndex = dao.getSearchParam().getBeginIndex();
		List<T> temp = (List<T>) dao.search();
		for (T e : temp) {
			JSONArray ja = new JSONArray();
			ja.put(++iIndex);
			this.pushToJa(ja, e, form);
			array.put(ja);
		}
		result.put("aaData", array);
	}

	protected abstract void pushToJa(JSONArray ja, T e, F modelForm) throws Exception;
	/**
	 * Tra ve tong so ban ghi tim duoc
	 * @param dao Doi tuong lay du lieu
	 * @return
	 * @throws Exception
	 */
	private long getTotalRecordCount(BaseDao<T> dao) throws Exception {
		return dao.count();
	}
	/**
	 * Don nhan tat ca cac request tu ben ngoai, va chuyen den controller tuong ung de thuc thi <br>
	 * Xu ly loi chung cho tat ca cac request
	 */
	@RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })	
	public String execute(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			@ModelAttribute("formDataModelAttr") F form) throws Exception {
		String method = rq.getParameter("method");
		if ("datatable".equals(method)) {
			try {
				datatable(model, rq, rs, form);
				return null;
			} catch (Exception ex) {

				lg.error("Loi", ex);
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
						rs.setContentType("text/plan;charset=utf-8");
						PrintWriter pw = rs.getWriter();
						if (!Formater.isNull(rse.getParam()))
							pw.print(String.format(getText(rse.getMessage()), rse.getParam()));
						else if (!Formater.isNull(rse.getParams()))
							pw.print(String.format(getText(rse.getMessage()), rse.getParams()));
						else
							pw.print(getText(rse.getMessage()));
						pw.flush();
						pw.close();
						throw rse;
					}
				}
				throw ex;
			}
		}
		return super.execute(model, rq, rs, form);
	}
	/**
	 *
	 * @return Doi tuong DAO ung voi chuc nang
	 */
	public abstract BaseDao<T> getDao();
}