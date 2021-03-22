package cic.h2h.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import cic.h2h.dao.hibernate.H2HBaseDao;
import cic.h2h.dao.hibernate.InfoSearchCicDao;
import cic.h2h.dao.hibernate.PartnerDao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import cic.h2h.form.InfoSearchCicForm;
import common.util.Formater;
import entity.InfoSearchCic;
import entity.Partner;
import entity.PartnerBranch;
import entity.Province;
import entity.frwk.SysDictParam;
import entity.frwk.SysDictType;
import entity.frwk.SysUsers;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.PartnerBranchDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysDictTypeDao;
import frwk.utils.ApplicationContext;

@Controller
@RequestMapping("/infoSearchCic")
public class InfoSearchController extends CatalogController<InfoSearchCicForm, InfoSearchCic> {
	
	private static Logger lg = LogManager.getLogger(InfoSearchController.class);
	
	@Autowired
	private InfoSearchCicDao infoSearchCicDao;
	
	@Autowired
	PartnerDao partnerDao;
	
	@Autowired
	ServiceInfoDao serviceInfoDao;
	
	@Autowired
	SysDictParamDao sysDictParamDao;
	
	@Autowired
	SysDictTypeDao  sysDictTypeDao;
	
	@Autowired
	PartnerBranchDao partnerBranchDao;
	
	@Override
	public BaseDao<InfoSearchCic> createSearchDAO(HttpServletRequest request, InfoSearchCicForm form) throws Exception {
		InfoSearchCicDao dao = new InfoSearchCicDao();
		InfoSearchCicForm logServicesForm = (InfoSearchCicForm) form;
		if (!Formater.isNull(logServicesForm.getMaTCTD()))
		dao.addRestriction(
				Restrictions.like("matctd", logServicesForm.getMaTCTD().trim(), MatchMode.ANYWHERE)
						.ignoreCase());
		if (!Formater.isNull(logServicesForm.getLoaiKh()))
		dao.addRestriction(
				Restrictions.eq("loaikh", new BigDecimal(logServicesForm.getLoaiKh())));
//		List<ServiceInfo> infos = serviceInfoDao.getServiceByOperation();
//		List<String> arr = new ArrayList<String>();
//		for (ServiceInfo serviceInfo : infos) {
//			arr.add(serviceInfo.getId());
//		}
//		dao.addRestriction(Restrictions.or(Restrictions.in("serviceCode.id", arr)));
//		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//		if (!Formater.isNull(logServicesForm.getFromDate())) {
//			dao.addRestriction(
//					Restrictions.ge("startTime", df.parse(logServicesForm.getFromDate())));
//		}
//		if (!Formater.isNull(logServicesForm.getToDate())) {
//			Date currentDate = df.parse(logServicesForm.getToDate());
//			Calendar c = Calendar.getInstance();
//			c.setTime(currentDate);
//			c.add(Calendar.DATE, 1);
//			Date dateAdd = c.getTime();
//			dao.addRestriction(Restrictions.le("startTime", dateAdd));
//		}
//		if (!Formater.isNull(logServicesForm.getMaTCTD()))
//			dao.addRestriction(
//					Restrictions.like("tctdCode", logServicesForm.getMaTCTD().trim(), MatchMode.ANYWHERE)
//							.ignoreCase());
//
//		if (!Formater.isNull(logServicesForm.getMaSP()))
//			dao.addRestriction(
//					Restrictions.like("productCode", logServicesForm.getMaSP().trim(), MatchMode.ANYWHERE)
//							.ignoreCase());
//		if (!Formater.isNull(logServicesForm.getUserRequest()))
//			dao.addRestriction(
//					Restrictions.like("userRequest", logServicesForm.getUserRequest().trim(), MatchMode.ANYWHERE)
//							.ignoreCase());
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, InfoSearchCic e, InfoSearchCicForm modelForm) throws Exception {
//		ja.put(Formater.date2ddsmmsyyyspHHmmss(e.getStartTime()));
		if (Formater.isNull(e.getMatctd()))
			ja.put("");
		else {
			Partner o = partnerDao.get(Partner.class, e.getMatctd());
			ja.put( o.getCode() + " - " + o.getName());
		}
		if (e.getLoaikh() == null)
			ja.put("");
		else if(e.getLoaikh().compareTo(new BigDecimal("1")) == 0){
			ja.put("Pháp nhân");
		}else if(e.getLoaikh().compareTo(new BigDecimal("2")) == 0){
			ja.put("Thể nhân");
		}
		ja.put("<a class='characterwrap btn blue' href = '#' onclick = 'edit(\""
				+ e.getId() + "\")'>Xem</a>");
	}

	@Override
	public BaseDao<InfoSearchCic> getDao() {
		// TODO Auto-generated method stub
		return infoSearchCicDao;
	}

	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "info_search_cic/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, InfoSearchCicForm form)
			throws Exception {
		// TODO Auto-generated method stub
		ApplicationContext appContext = (ApplicationContext) rq.getSession()
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		if (appContext != null) {
			SysUsers user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
			lg.info(user);
		}
		List<Partner> lstPartner = partnerDao.getAll(Partner.class);
		List<Province> listProvince = new H2HBaseDao<Province>().getAll(Province.class);
		SysDictType sysDictType = sysDictTypeDao.getSysDictTypeByCode("DIA_BAN");
		SysDictType typeXepHang = sysDictTypeDao.getSysDictTypeByCode("XEP_HANG");
		SysDictType typeTDCN = sysDictTypeDao.getSysDictTypeByCode("TINDUNG_CANHAN");
		List<SysDictParam> lstDiaBan = sysDictParamDao.sysDictParams("", "", sysDictType.getId());
		List<SysDictParam> lstXepHang = sysDictParamDao.sysDictParams("", "", typeXepHang.getId());
		List<SysDictParam> lstTDCN = sysDictParamDao.sysDictParams("", "", typeTDCN.getId());
		model.addAttribute("lstPartner", lstPartner);
		model.addAttribute("listProvince", listProvince);
		model.addAttribute("lstDiaBan", lstDiaBan);
		model.addAttribute("lstXepHang", lstXepHang);
		model.addAttribute("lstTDCN", lstTDCN);
	}
	
	public void loadPartnerBranch(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, InfoSearchCicForm form)
			throws Exception {
		String partnerId = rq.getParameter("partnerId");
		rs.setContentType("text/plain;charset=utf-8");
		List<PartnerBranch> lstPartnerBranch = partnerBranchDao.getbranchs(null, null, partnerId);
		PrintWriter pw = rs.getWriter();
		JSONArray array = null;
		array = new JSONArray(new ObjectMapper().writeValueAsString(lstPartnerBranch));
		pw.print(array);
		pw.close();
	}
	
	public void loadPartner(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, InfoSearchCicForm form)
			throws Exception {
		rs.setContentType("text/plain;charset=utf-8");
		List<Partner> lstPartner = partnerDao.getAll(Partner.class);
		PrintWriter pw = rs.getWriter();
		JSONArray array = null;
		array = new JSONArray(new ObjectMapper().writeValueAsString(lstPartner));
		pw.print(array);
		pw.close();
	}

}
