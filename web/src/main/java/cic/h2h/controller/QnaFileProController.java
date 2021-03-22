package cic.h2h.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.PartnerDao;
import cic.h2h.dao.hibernate.QnaFileProDao;
import cic.h2h.form.QnaFileProForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.Partner;
import entity.QnaFilePro;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;

@Controller
@RequestMapping("/qnaFilePro")
public class QnaFileProController extends CatalogController<QnaFileProForm, QnaFilePro> {

	static Logger lg = LogManager.getLogger(QnaFileProController.class);
	
	@Autowired
	private QnaFileProDao qnaFileProDao;
	
	@Autowired
	private PartnerDao partnerDao;

	@Override
	public BaseDao<QnaFilePro> createSearchDAO(HttpServletRequest request, QnaFileProForm form) throws Exception {
		QnaFileProDao dao = new QnaFileProDao();
		if (!Formater.isNull(form.getParentId()))
			dao.addRestriction(Restrictions.eq("partner.id", form.getParentId()));
		
		if (!Formater.isNull(form.getCode()))
			dao.addRestriction(Restrictions.like("code", form.getCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		
		if (!Formater.isNull(form.getName()))
			dao.addRestriction(Restrictions.like("name", form.getName().trim(), MatchMode.ANYWHERE).ignoreCase());
		dao.addOrder(Order.desc("createDate"));
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, QnaFilePro r, QnaFileProForm modelForm) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + r.getId() + "\")'>" + r.getCode() + "</a>");
		ja.put(r.getName());
		ja.put(r.getDescription());
		if(Formater.isNull(r.getPartner().getCode())) {
			ja.put("");
			ja.put("");
		}else {
			ja.put(r.getPartner().getCode());
			ja.put(r.getPartner().getName());
		}
	}

	@Override
	public BaseDao<QnaFilePro> getDao() {
		return qnaFileProDao;
	}

	@Override
	public String getJsp() {
		return "qna_file_pro/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaFileProForm form)
			throws Exception {
		List<Partner> listParent = partnerDao.getListTCTD("");
		model.addAttribute("listParent", listParent);
	}
	
	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaFileProForm form) throws Exception {
		lg.info("BEGIN save QnaFilePro");
		Date date = new Date();
		
		try {
			QnaFilePro qnaFilePro = new QnaFilePro();
			qnaFilePro = form.getQnaFilePro();
			qnaFilePro.setCreateDate(date);
			
			qnaFileProDao.save(qnaFilePro);
			lg.info("end save QnaFilePro");
		} catch (ConstraintViolationException ex) {
			lg.error(ex);
			throw new ResourceException("Du lieu trung");
		}
		catch (Exception ex) {
			throw ex;
		}
		
	}

}
