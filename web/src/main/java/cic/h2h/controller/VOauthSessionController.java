package cic.h2h.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.VOauthSessionDao;
import cic.h2h.form.LogCheckConnectForm;
import cic.h2h.form.VOauthSessionForm;
import common.util.FormatNumber;
import common.util.Formater;
import entity.LogCheckConnect;
import entity.VOauthSession;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/vOauthSession")
public class VOauthSessionController extends CatalogController<VOauthSessionForm, VOauthSession> {

	static Logger lg = LogManager.getLogger(VOauthSessionController.class);
	
	@Autowired
	private VOauthSessionDao vOauthSessionDao;

	@Override
	public BaseDao<VOauthSession> createSearchDAO(HttpServletRequest request, VOauthSessionForm form) throws Exception {
		VOauthSessionDao dao =  new VOauthSessionDao(); 
		
		if (!Formater.isNull(form.getUserName()))
			dao.addRestriction(Restrictions.like("userName", form.getUserName().trim(), MatchMode.ANYWHERE).ignoreCase());
		dao.addOrder(Order.desc("thoiDiemKetNoi"));
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, VOauthSession r, VOauthSessionForm modelForm) throws Exception {
		ja.put(r.getCode());
		ja.put(r.getName());
		ja.put(r.getSoLuongSession());
		ja.put(r.getUserName());
		ja.put(FormatNumber.num2Str(r.getThoiGianKetNoi()));
	}

	@Override
	public BaseDao<VOauthSession> getDao() {
		return vOauthSessionDao;
	}

	@Override
	public String getJsp() {
		return "v_oauth_session/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, VOauthSessionForm form)
			throws Exception {
	}
	
	@Autowired
	ExportExcel exportExcel;
	
	public void exportFileExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, VOauthSessionForm form)
			throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String userName = rq.getParameter("userName");
			List<VOauthSession> connects = vOauthSessionDao.reports(userName);
			map.put("reports", connects);
			exportExcel.export("Danh_sach_user_TCTD_dang_ket_noi", rs, map);
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
		}
	}
	
}
