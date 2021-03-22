package cic.h2h.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.PartnerDao;
import cic.h2h.form.PartnerBranchForm;
import common.util.Formater;
import entity.Partner;
import entity.PartnerBranch;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.PartnerBranchDao;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping(value = "/partnerBranch")
public class PartnerBranchController extends CatalogController<PartnerBranchForm, PartnerBranch> {

	private static final Logger log = LogManager.getLogger(PartnerBranchController.class);

	@Autowired
	private PartnerBranchDao partnerBranchDao;

	@Autowired
	private Partner partner;

	@Override
	public BaseDao<PartnerBranch> createSearchDAO(HttpServletRequest request, PartnerBranchForm form) throws Exception {
		PartnerBranchDao dao = new PartnerBranchDao();
		dao.addRestriction(Restrictions.eq("partnerId", partner));
		if (!Formater.isNull(form.getCodeSearch())) {
			dao.addRestriction(Restrictions.like("code", form.getCodeSearch(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(form.getNameSearch())) {
			dao.addRestriction(Restrictions.like("name", form.getNameSearch(), MatchMode.ANYWHERE).ignoreCase());
		}
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, PartnerBranch e, PartnerBranchForm modelForm) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getCode() + "</a>");
		ja.put(e.getName());
		ja.put(e.getAddress());
	}

	@Override
	public BaseDao<PartnerBranch> getDao() {
		return partnerBranchDao;
	}

	@Override
	public String getJsp() {
		return "partner_branch/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PartnerBranchForm form)
			throws Exception {
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PartnerBranchForm form)
			throws Exception {
		form.getPartnerBranch().setPartnerId(partner);
		super.save(model, rq, rs, form);
	}

	@Autowired
	private ExportExcel exportExcel;
	@Autowired
	private PartnerDao partnerDao;
	public void exportFileExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, PartnerBranchForm form) {
		try {
			String code = rq.getParameter("code");
			String value = rq.getParameter("value");
			String paramType = rq.getParameter("paramType");
			List<PartnerBranch> branchs = partnerBranchDao.getbranchs(code, value, paramType);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("branchs", branchs);
			map.put("code", code);
			map.put("value", value);
			if (!Formater.isNull(paramType))
				map.put("valueType", partnerDao.getObject(Partner.class, paramType).getName());
			exportExcel.export("Danh_sach_chi_nhanh_he_thong", rs, map);
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
		}
	}

}
