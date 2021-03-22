package cic.h2h.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.criterion.LikeExpression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.QrEndpointDao;
import cic.h2h.form.QrEnpointForm;
import common.util.Formater;
import entity.FunEndpoint;
import entity.QrEndpoint;
import entity.frwk.SysDictParam;
import frwk.constants.Constants;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.EscapingLikeExpression;
import frwk.dao.hibernate.sys.SysDictParamDao;

@Controller
@RequestMapping(value = "/endpoint")
public class QrEnpointController extends CatalogController<QrEnpointForm, QrEndpoint> {

	@Autowired
	private QrEndpointDao qrEndpointDao;

	@Autowired
	private SysDictParamDao sysDictParamDao;

	@Override
	public BaseDao<QrEndpoint> createSearchDAO(HttpServletRequest request, QrEnpointForm form) throws Exception {
		// TODO Auto-generated method stub
		QrEndpointDao dao = new QrEndpointDao();
		if (!Formater.isNull(form.getCode())) {
			EscapingLikeExpression likeExpression = new EscapingLikeExpression("productCode",
					form.getCode().trim(), MatchMode.ANYWHERE, Boolean.TRUE);
			
			dao.addRestriction(likeExpression);
		}
		if (!Formater.isNull(form.getEndpointSearch())) {
			dao.addRestriction(
					Restrictions.like("endpoint", form.getEndpointSearch().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		return dao;
	}

	
	@Override
	protected void pushToJa(JSONArray ja, QrEndpoint e, QrEnpointForm modelForm) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getProductCode()
				+ "</a>");
		ja.put(e.getEndpoint());
		ja.put(e.getFunName());

	}

	@Override
	public BaseDao<QrEndpoint> getDao() {
		return qrEndpointDao;
	}

	@Override
	public String getJsp() {
		return "qr_endpoint/view";
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QrEnpointForm form)
			throws Exception {
		// TODO Auto-generated method stub

		qrEndpointDao.save(form.getEndpoint());
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QrEnpointForm form)
			throws Exception {
		// TODO Auto-generated method stub
		List<SysDictParam> dictParams = sysDictParamDao.getByType(Constants.CIC_PARAM);
		model.addAttribute("dictParams", dictParams);
		List<SysDictParam> dataTypes = sysDictParamDao.getByType(Constants.DATA_TYPE);
		model.addAttribute("dataTypes", dataTypes);

	}

}
