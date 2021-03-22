package cic.h2h.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.AppFlowDao;
import cic.h2h.dao.hibernate.PocUserinfoFormDAO;
import cic.h2h.form.AppFlowForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.AppFlow;
import entity.WrkFlwMng;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping("/appflow")
public class AppFlowController extends CatalogController<AppFlowForm, AppFlow> {

	static Logger lg = LogManager.getLogger(AppFlowController.class);

	@Autowired
	private AppFlowDao appFlowDao;

	@Override
	public BaseDao<AppFlow> getDao() {
		return appFlowDao;
	}

	@Override
	public BaseDao<AppFlow> createSearchDAO(HttpServletRequest request, AppFlowForm form) throws Exception {
		AppFlowDao dao = new AppFlowDao();
		if (!Formater.isNull(form.getKeyWord())) {
			dao.addRestriction(
					Restrictions
							.or(Restrictions
									.sqlRestriction(
											"to_char(status) like ?", "%" + form.getKeyWord()
													+ "%",
											StringType.INSTANCE),
									Restrictions
											.or(Restrictions
													.sqlRestriction(
															"to_char(sub_Status) like ?", "%" + form.getKeyWord()
																	+ "%",
															StringType.INSTANCE),
													Restrictions.or(
															Restrictions.like("description", form.getKeyWord().trim(),
																	MatchMode.ANYWHERE).ignoreCase(),
															Restrictions.or(
																	Restrictions.like("name", form.getKeyWord().trim(),
																			MatchMode.ANYWHERE).ignoreCase(),
																	Restrictions
																			.like("action", form.getKeyWord().trim(),
																					MatchMode.ANYWHERE)
																			.ignoreCase())))));
			
		}
		dao.addRestriction(Restrictions.eq("wrkFlwMng.id", form.getAppFlow().getWrkFlwMng().getId()));
		dao.addOrder(Order.asc("status"));
		dao.addOrder(Order.asc("subStatus"));
		return dao;
	}
	
	

	@Override
	public void pushToJa(JSONArray ja, AppFlow r, AppFlowForm modal) throws Exception {
		ja.put("<a href = '#' onclick = 'edit(\"" + r.getId() + "\")'>" + r.getStatus()
				+ (r.getSubStatus() == null ? "" : ("." + r.getSubStatus())) + "</a>");
		ja.put(r.getName());
		ja.put(r.getDescription());
		ja.put(r.getAction());
	}

	@Override
	public String getJsp() {
		return "qtht/quy_trinh_phe_duyet";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, AppFlowForm form)
			throws Exception {
		model.addAttribute("type", form.getType());
		String parentId = rq.getParameter("parentId");
		if (Formater.isNull(parentId))
			throw new ResourceException("parentId khong ton tai");
		form.getAppFlow().setWrkFlwMng(new WrkFlwMng(parentId));
	}

	@Autowired
	private PocUserinfoFormDAO pocUserinfoFormDAO;

	@Override
	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, AppFlowForm form) throws Exception {
		// Kiem tra trang thai da duoc dung
		appFlowDao.load(form.getModel());
		if (pocUserinfoFormDAO.existItem(form.getModel()))
			throw new ResourceException("Can not delete, because of existing recruitments in this step!");
		super.del(model, rq, rs, form);
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, AppFlowForm form) throws Exception {
		// Truong hop sua status, subStatus thong bao du lieu da ton tai
		if (!Formater.isNull(form.getModel().getId())) {
			AppFlow oldInDB = appFlowDao.load(AppFlow.class, form.getModel().getId());
			if (!oldInDB.equalStatus(form.getModel())) {
				if (pocUserinfoFormDAO.existItem(oldInDB))
					throw new ResourceException(
							"Can not change to this status, because of existing recruitments in this step!");
			}
		}
		super.save(model, rq, rs, form);
	}
}