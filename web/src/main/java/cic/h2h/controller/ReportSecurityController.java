package cic.h2h.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.ReportSecurityDTODao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import cic.h2h.form.ReportSecurityDTOForm;
import common.util.Formater;
import dto.ReportSecurityDTO;
import entity.Partner;
import entity.PartnerConnectIp;
import entity.PartnerService;
import entity.frwk.SysSecurity;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.dao.hibernate.sys.SysSecurityDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/reportSecurity")
public class ReportSecurityController extends CatalogController<ReportSecurityDTOForm, ReportSecurityDTO> {
	
	private static Logger lg = LogManager.getLogger(ReportSecurityController.class);

	@Autowired
	private SysPartnerDao sysPartnerDao;
	
	@Autowired
	private ServiceInfoDao serviceInfoDao;
	
	@Autowired
	private ReportSecurityDTODao reportSecurityDTODao;
	
	@Autowired
	private ExportExcel exportExcel;
	
	@Override
	public BaseDao<ReportSecurityDTO> createSearchDAO(HttpServletRequest request, ReportSecurityDTOForm form)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void pushToJa(JSONArray ja, ReportSecurityDTO e, ReportSecurityDTOForm modelForm) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BaseDao<ReportSecurityDTO> getDao() {
		// TODO Auto-generated method stub
		return reportSecurityDTODao;
	}

	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "bao_cao/report_security";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ReportSecurityDTOForm form)
			throws Exception {
		// TODO Auto-generated method stub
		List<Partner> dsDonVi = sysPartnerDao.getAll();
		model.addAttribute("dsDonVi", dsDonVi);
		model.addAttribute("serviceInfos", serviceInfoDao.getAllGWService());
	}
	
	@Autowired
	private SysSecurityDao sysSecurityDao;
	
	public void exportExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, ReportSecurityDTOForm form)
			throws Exception {
		try {
			String partner = rq.getParameter("partner");
			String serviceInfos = rq.getParameter("serviceInfos");
			String ipAddress = rq.getParameter("ipAddress");
			String securityCode = rq.getParameter("codeSecurity");
			String type = rq.getParameter("type");
			List<ReportSecurityDTO> reportsIp = new ArrayList<ReportSecurityDTO>();
			List<ReportSecurityDTO> reportsCSBM = new ArrayList<ReportSecurityDTO>();
			List<ReportSecurityDTO> reportsService = new ArrayList<ReportSecurityDTO>();
			List<Partner> partners = new ArrayList<Partner>();
			if (!Formater.isNull(partner)) {
				partners.add(sysPartnerDao.getObject(Partner.class, partner));
			} else {
				partners = sysPartnerDao.getAll();
			}
			List<SysSecurity> securities = new ArrayList<SysSecurity>();
			if (!Formater.isNull(securityCode)) {
				securities.add(sysSecurityDao.findByCodeAndActive(securityCode, true));
			} else {
				securities = sysSecurityDao.getAll(SysSecurity.class);
			}
			if (Formater.isNull(type)) {
				//get all type
				for (Partner partnerItem : partners) {
					List<PartnerConnectIp> connectIps = new ArrayList<PartnerConnectIp>();
					connectIps.addAll(partnerItem.getPartnerConnectIps());
					if (Formater.isNull(ipAddress)) {
						for (PartnerConnectIp connectIp : connectIps) {
							reportsIp.add(new ReportSecurityDTO(partnerItem.getCode(), partnerItem.getName(),
										connectIp.getIpAddress(), null, null, null, null, null, null, null, "IP"));
						}
					} else {
						for (PartnerConnectIp connectIp : connectIps) {
							if (ipAddress.equalsIgnoreCase(connectIp.getIpAddress())) {
								reportsIp.add(new ReportSecurityDTO(partnerItem.getCode(), partnerItem.getName(),
										connectIp.getIpAddress(), null, null, null, null, null, null, null, "IP"));
							}
						}
					}
					List<PartnerService> partnerServices = new ArrayList<PartnerService>();
					partnerServices.addAll(partnerItem.getPartnerServices());
					if (Formater.isNull(serviceInfos)) {
						for (PartnerService service : partnerServices) {
							String product = "";
							if (service.getCatProductId() != null)
								product = service.getCatProductId().getName();
							reportsService.add(new ReportSecurityDTO(partnerItem.getCode(), partnerItem.getName(), 
									null, service.getServiceInfoId().getServiceName(), product,
									service.getPeriod()+"", service.getRate()+"", null, null, null, "Service"));
						}
					} else {
						for (PartnerService service : partnerServices) {
							String product = "";
							if (service.getCatProductId() != null)
								product = service.getCatProductId().getName();
							if (serviceInfos.equalsIgnoreCase(service.getServiceInfoId().getId())) {
								reportsService.add(new ReportSecurityDTO(partnerItem.getCode(), partnerItem.getName(), 
										null, service.getServiceInfoId().getServiceName(), product,
										service.getPeriod()+"", service.getRate()+"", null, null, null, "Service"));
							}
						}
					}
				}
				for (SysSecurity security : securities) {
					if (Formater.isNull(securityCode)) {
						for (Partner item : partners) {
							reportsCSBM.add(new ReportSecurityDTO(item.getCode(), item.getName(),
									null, null, null, null, null, security.getCode(), security.getName(), security.getValue(), "CSBM"));
						}
					} else {
						if (security != null && securityCode.equalsIgnoreCase(security.getCode())) {
							for (Partner item : partners) {
								reportsCSBM.add(new ReportSecurityDTO(item.getCode(), item.getName(),
										null, null, null, null, null, security.getCode(), security.getName(), security.getValue(), "CSBM"));
							}
						}
					}
				}
			} else {
				if (type.equals("1")) {
					for (Partner partnerItem : partners) {
						List<PartnerConnectIp> connectIps = new ArrayList<PartnerConnectIp>();
						connectIps.addAll(partnerItem.getPartnerConnectIps());
						if (Formater.isNull(ipAddress)) {
							for (PartnerConnectIp connectIp : connectIps) {
								reportsIp.add(new ReportSecurityDTO(partnerItem.getCode(), partnerItem.getName(),
											connectIp.getIpAddress(), null, null, null, null, null, null, null, "IP"));
							}
						} else {
							for (PartnerConnectIp connectIp : connectIps) {
								if (ipAddress.equalsIgnoreCase(connectIp.getIpAddress())) {
									reportsIp.add(new ReportSecurityDTO(partnerItem.getCode(), partnerItem.getName(),
											connectIp.getIpAddress(), null, null, null, null, null, null, null, "IP"));
								}
							}
						}
					}
				} else if (type.equals("2")) {
					for (Partner partnerItem : partners) {
						List<PartnerService> partnerServices = new ArrayList<PartnerService>();
						partnerServices.addAll(partnerItem.getPartnerServices());
						if (Formater.isNull(serviceInfos)) {
							for (PartnerService service : partnerServices) {
								String product = "";
								if (service.getCatProductId() != null)
									product = service.getCatProductId().getName();
								reportsService.add(new ReportSecurityDTO(partnerItem.getCode(), partnerItem.getName(), 
										null, service.getServiceInfoId().getServiceName(), product,
										service.getPeriod()+"", service.getRate()+"", null, null, null, "Service"));
							}
						} else {
							for (PartnerService service : partnerServices) {
								String product = "";
								if (service.getCatProductId() != null)
									product = service.getCatProductId().getName();
								if (serviceInfos.equalsIgnoreCase(service.getServiceInfoId().getId())) {
									reportsService.add(new ReportSecurityDTO(partnerItem.getCode(), partnerItem.getName(), 
											null, service.getServiceInfoId().getServiceName(), product,
											service.getPeriod()+"", service.getRate()+"", null, null, null, "Service"));
								}
							}
						}
					}
				} else if (type.equals("3")) {
					for (SysSecurity security : securities) {
						if (Formater.isNull(securityCode)) {
							for (Partner item : partners) {
								reportsCSBM.add(new ReportSecurityDTO(item.getCode(), item.getName(),
										null, null, null, null, null, security.getCode(), security.getName(), security.getValue(), "CSBM"));
							}
						} else {
							if (securityCode.equalsIgnoreCase(security.getCode())) {
								for (Partner item : partners) {
									reportsCSBM.add(new ReportSecurityDTO(item.getCode(), item.getName(),
											null, null, null, null, null, security.getCode(), security.getName(), security.getValue(), "CSBM"));
								}
							}
						}
					}
				}
			}
//			Collections.sort(dtos, new Comparator<ReportSecurityDTO>() {
//				@Override
//				public int compare(ReportSecurityDTO o1, ReportSecurityDTO o2) {
//					return o1.getType().compareTo(o2.getType());
//				}
//			});
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("reports1", reportsCSBM);
			map.put("reports2", reportsIp);
			map.put("reports3", reportsService);
			exportExcel.export("Bao_cao_rule_an_toan_bao_mat", rs, map);
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
		}
	}


}
