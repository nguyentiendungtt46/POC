package vn.com.cmc.service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXB;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import cic.h2h.dao.hibernate.CatProductDao;
import cic.h2h.dao.hibernate.LogServicesDao;
import cic.h2h.dao.hibernate.QnaInDetailDao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import entity.LogService;
import entity.ServiceInfo;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.utils.ApplicationContext;
import intergration.cic.Authen;
import vn.com.cmc.entity.SysUsers;
import vn.com.cmc.utils.Constants;
import vn.com.cmc.utils.Utils;
import vn.com.cmc.ws.config.SOAPConnector;
import vn.com.cmc.ws.endpoint.CicSearchEndpoint;
import vn.org.intergration.ws.endpoint.cicqa.TepHoiTin;
import vn.org.intergration.ws.endpoint.cicqr.DSLoi;
import vn.org.intergration.ws.endpoint.cicqr.Loi;
import vn.org.intergration.ws.endpoint.cicqr.PHTimKiemKH;
import vn.org.intergration.ws.endpoint.cicqr.PHVanTinChung;
import vn.org.intergration.ws.endpoint.cicqr.TTPhanHoi;
import vn.org.intergration.ws.endpoint.cicqr.TimKiemKH;
import vn.org.intergration.ws.endpoint.cicqr.TimKiemKHKQ;
import vn.org.intergration.ws.endpoint.cicqr.VanTinChung;

@Service
public class validateServiceImpl implements ValidateService {

//	@Autowired
//	PartnerConnectIpRespository partnerConnectIpRespository;
//	
//	@Autowired
//	PartnerServiceRespository partnerServiceRespository;
//	
//	@Autowired
//	LogServiceRepository logServiceRepository;

	@Autowired
	CatProductDao catProductDao;

	@Autowired
	SysUsersDao sysUsersDao;

//	@Autowired
//	SysSecurityRespository sysSecurityRespository;

//	public boolean validateIpRequest(SysUsers userRequest, String ip) {
//		PartnerConnectIp partnerConnectIp = partnerConnectIpRespository.checkIpByPartnerId(userRequest.getPartnerCompany().getId(), ip);
//		if(Utils.isNullObject(partnerConnectIp)) {
//			return false;
//		}else {
//			return true;
//		}
//	}
//	
//	public String validateServiceRequest(SysUsers userRequest, String serviceId) {
//		String partnerId = userRequest.getPartnerCompany().getId();
//		PartnerService partnerService = null;
//		partnerService = partnerServiceRespository.checkServiceByPartnerIdAndServiceId(partnerId, serviceId);
//		if(Utils.isNullObject(partnerService) || !partnerService.getDisableStatus().equals(new BigDecimal("0"))) {
//			return Constants.ValidateCodeService.CMM_003;
//		}else {
//			if(!Utils.isNullObject(partnerService.getRate())) {
//				List<LogService> logService = logServiceRepository.lastCallService(partnerId, serviceId, null);
//				if(!logService.isEmpty()) {
//					Date lastTime = logService.get(0).getStartTime();
//					Date rateTime = getRateTime(lastTime, partnerService.getRate(), partnerService.getPeriod());
//					if(rateTime.after(new Date())) {
//						return Constants.ValidateCodeService.CMM_004;
//					}
//				}
//			}
//		}
//		if(!Utils.isNullObject(partnerService.getMaxOfDay())) {
//			BigDecimal maxOfDay = logServiceRepository.getMaxOfDay(new Date(), partnerId, serviceId, "");
//			if(partnerService.getMaxOfDay().compareTo(maxOfDay) <= 0)
//				return Constants.ValidateCodeService.CMM_010;
//		}
//		return null;
//	}
//	
//	@Override
//	public String validateServiceRequestAndProductCode(SysUsers userRequest, ServiceInfo serviceInfo, String productCode) {
//		String partnerId = userRequest.getPartnerCompany().getId();
//		PartnerService partnerService = null;
//		partnerService = partnerServiceRespository.checkServiceByPartnerIdAndProductId(partnerId, serviceInfo.getId(), productCode);
//		if(Utils.isNullObject(partnerService) || !partnerService.getDisableStatus().equals(new BigDecimal("0"))) {
//			return Constants.ValidateCodeService.CMM_003;
//		}else {
//			if(!Utils.isNullObject(partnerService.getRate())) {
//				List<LogService> logService = logServiceRepository.lastCallService(partnerId, serviceInfo.getId(), productCode);
//				if(!logService.isEmpty()) {
//					Date lastTime = logService.get(0).getStartTime();
//					Date rateTime = getRateTime(lastTime, partnerService.getRate(), partnerService.getPeriod());
//					if(rateTime.after(new Date())) {
//						return Constants.ValidateCodeService.CMM_004;
//					}
//				}
//			}
//		}
//		// CMM_010
//		if(!Utils.isNullObject(partnerService.getMaxOfDay())) {
//			BigDecimal maxOfDay = logServiceRepository.getMaxOfDay(new Date(), partnerId, serviceInfo.getId(), productCode);
//			if(partnerService.getMaxOfDay().compareTo(maxOfDay) <= 0)
//				return Constants.ValidateCodeService.CMM_010;
//		}
//		return null;
//	}
//	
////	@Override
////	public String validateAll(SysUsers userRequest, HttpServletRequest httpRequest, ServiceInfo serviceInfo, boolean isCheckPass, boolean isCheckProductCode) {
////		
////		// check IP
////		SysSecurity sysSecurity = sysSecurityRespository.findByCodeAndActive(Constants.KT_IP_TCTD);
////		if (!Utils.isNullObject(sysSecurity) && sysSecurity.getValue().equalsIgnoreCase("1")) {
////			String ip = Utils.getClientIpAddr(httpRequest);
////			if (!Utils.isNullObject(ip) && !validateIpRequest(userRequest, ip)) {
////				return Constants.ValidateCodeService.CMM_002;
////			}
////		}
////		
////		// check max size
////		if (!Utils.isNullObject(serviceInfo) && !Utils.isNullObject(serviceInfo.getMaxSizeApi())) {
////			Long maxSize = serviceInfo.getMaxSizeApi();
////			if (serviceInfo.getRequestClass().equalsIgnoreCase(vn.org.cic.h2h.ws.endpoint.cicqa.TepHoiTin.class.getName())
////					&& !serviceInfo.getRequestClass().equalsIgnoreCase(vn.org.cic.h2h.ws.endpoint.cicreport.TepPhiCauTruc.class.getName())
////					&& !serviceInfo.getRequestClass().equalsIgnoreCase(vn.org.cic.h2h.ws.endpoint.cicreport.TepBaoCao.class.getName())) {
////				if (Utils.bytesToMeg(httpRequest.getContentLength()) > maxSize) {
////					return Constants.ValidateCodeService.CMM_008;
////				}
////			}
////		}
////		
////		// validate password status
////		if (isCheckPass) {
////			if(Utils.isNullObject(userRequest.getPasswordStatus()) || !(new Long(1)).equals(userRequest.getPasswordStatus())) {
////				return Constants.ValidateCodeService.CMM_007;
////			}
////		}
////				
////		if(!serviceInfo.getStatus().equals(new Long("1"))) {
////			return Constants.ValidateCodeService.CMM_006;
////		}
////		
////		Partner partner = userRequest.getPartnerCompany();
////		if (!Utils.isNullObject(partner.getValidityContractFrom())) {
////			if (partner.getValidityContractFrom().after(new Date())) {
////				return Constants.ValidateCodeService.CMM_009;
////			}
////			
////			if (!Utils.isNullObject(partner.getValidityContractTo())) {
////				Date contractTo = DateUtils.addDate(partner.getValidityContractTo(), 1);
////				if (contractTo.before(new Date())) {
////					return Constants.ValidateCodeService.CMM_009;
////				}
////			}
////		}
////		
////		if(!Utils.isNullObject(serviceInfo)) {
////			if (!isCheckProductCode) {
////				String validate = validateServiceRequest(userRequest, serviceInfo.getId());
////				if(!Utils.isNullObject(validate))
////					return validate;
////			}
////		}
////		return null;
////	}
//	
//	private Date getRateTime(Date date, BigDecimal rate, BigDecimal period) {
//		if(period.equals(Constants.PERIOD_CONFIG.PERIOD_SECONDS)) {
//			return DateUtils.addSecond(date, rate.intValue());
//		}else if(period.equals(Constants.PERIOD_CONFIG.PERIOD_MINUTES)) {
//			return DateUtils.addMinute(date, rate.intValue());
//		}else if(period.equals(Constants.PERIOD_CONFIG.PERIOD_HOURS)) {
//			return DateUtils.addHour(date, rate.intValue());
//		}else if(period.equals(Constants.PERIOD_CONFIG.PERIOD_DAY)) {
//			return DateUtils.addDate(date, rate.intValue());
//		}else if(period.equals(Constants.PERIOD_CONFIG.PERIOD_MONTH)) {
//			return DateUtils.addMonth(date, rate.intValue());
//		}
//		return null;
//	}
//	
////	@Override
////	public String validateVanTinChung(VanTinChung request) {
////		if (Utils.isNullObject(request.getMaSP()) || request.getMaSP().length() > 10) {
////			return Constants.ValidateCodeService.QAC_001;
////		}
////		
////		// check product code
////		CatProduct product = catProductRepository.findByCode(request.getMaSP());
////		if (Utils.isNullObject(product)) {
////			return Constants.ValidateCodeService.QAC_002;
////		}
////		
////		return null;
////	}
//	
//	@Override
//	public String validateQuestionFile(vn.org.intergration.ws.endpoint.cicqa.TepHoiTin request) {
//		if (Utils.isNullObject(request.getMaSP()) || request.getMaSP().length() > 10) {
//			return Constants.ValidateCodeService.QNA_101;
//		}
//		
//		// check product code
////		CatProduct product = catProductRepository.findByCode(request.getLoaiSP());
////		if (Utils.isNullObject(product)) {
////			return Constants.ValidateCodeService.QAF_001;
////		}
//		
//		return null;
//	}

//	@Override
//	public String validateVanTinChung(String code) {
//		if (Utils.isNullObject(code) || code.length() > 20) {
//			return Constants.ValidateCodeService.QAC_001;
//		}
//		
//		// check product code
//		CatProduct product = catProductRepository.findByCode(code);
//		if (Utils.isNullObject(product)) {
//			return Constants.ValidateCodeService.QAC_999;
//		}
//		
//		return null;
//	}
//	
	@Override
	public String validateTTChung(vn.org.intergration.ws.endpoint.cicqa.HoiTin request) {
		if (Utils.isNullOrEmpty(request.getKhachHang().getNguoiYeuCau())
				|| !sysUsers(request.getKhachHang().getNguoiYeuCau())) {
			return Constants.ValidateCodeService.CIC_002;
		}

		if (Utils.isNullObject(request.getKhachHang().getMaSP()) || (request.getKhachHang().getMaSP().length() > 7)) {
			return Constants.ValidateCodeService.QNA_101;
		}
		if (!checkproductType(request.getKhachHang())) {
			return Constants.ValidateCodeService.QNA_102;
		}
		if (Utils.isNullOrEmpty(request.getKhachHang().getMaKH())
				&& Utils.isNullOrEmpty(request.getKhachHang().getSoCMT())) {
			return Constants.ValidateCodeService.CIC_001;
		}

		return null;
	}

	@Autowired
	private HttpSession session;

	@Override
	public boolean sysUsers(String username) {
		entity.frwk.SysUsers users = sysUsersDao.getByUserName(username);
		if (users != null) {
			ApplicationContext appContext = new ApplicationContext();
			appContext.setAttribute(ApplicationContext.USER, users);
			session.setAttribute(ApplicationContext.APPLICATIONCONTEXT, appContext);
			return true;
		} else
			return false;
	}

	// check product type
	private boolean checkproductType(vn.org.intergration.ws.endpoint.cicqaprod.KhachHang request) {
		if (Utils.isNullObject(request.getMaSP()) || request.getMaSP().isEmpty())
			return false;
		entity.CatProduct catProduct = catProductDao.getCatProductByCode(request.getMaSP());
		if (Utils.isNullObject(catProduct))
			return false;

		request.setMaSP(catProduct.getId());
		return true;
	}

	@Override
	public String validateVanTinChung(String code) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String validateQuestionFile(TepHoiTin request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Autowired
	QnaInDetailDao qnaInDetailDao;

	// check customer type
	public int checkCustomerType(String productType) {
		for (String product : Constants.QuestionAnswer.PHAP_NHAN) {
			if (productType.equalsIgnoreCase(product))
				return 1;
		}
		return 0;
	}

	@Override
	public String vailidateCustomer(vn.org.intergration.ws.endpoint.cicqaprod.KhachHang customer, String loaiSp) {
		if (Utils.isNullObject(customer.getTenKH()) || customer.getTenKH().length() > 200) {
			return Constants.ValidateCodeService.QNA_106;
		}
		if (!Utils.isNullObject(customer.getTenKH()) && customer.getTenKH().length() < 4) {
			return Constants.ValidateCodeService.QNA_131;
		}
		if (!Utils.isNullObject(customer.getMaKH()) && customer.getMaKH().length() > 50) {
			return Constants.ValidateCodeService.QNA_107;
		}
		String regex = "\\d+";
		if (!Utils.isNullObject(customer.getMaCIC())
				&& (customer.getMaCIC().length() != 10 || !customer.getMaCIC().matches(regex))) {
			return Constants.ValidateCodeService.QNA_108;
		}

		// trÃ¹ng mÃ£ CIC trong ngÃ y
		boolean existed = false;
//		if (!Utils.isNullObject(customer.getMaCIC())) {
//			List<QnaInDetail> outDetails = qnaInDetailDao.findDuplicateQuestionByCic(customer.getMaCIC(),
//					user.getPartnerCompany().getCode(), customer.getMaSP(), DateUtils.addDate(new Date(), -1));
//			if (outDetails != null && outDetails.size() > 0) {
//				lstErrorCode.add(Constants.ValidateCodeService.QNA_118);
//				existed = true;
//			}
//		}

		if (Utils.isNullObject(customer.getDiaChi()) || customer.getDiaChi().length() > 300) {
			return Constants.ValidateCodeService.QNA_109;
		}

		if (checkCustomerType(customer.getMaSP()) == 1) {
			// phap nhan
			boolean check = true;
			if (Utils.isNullObject(customer.getMSThue()) || customer.getMSThue().length() > 50) {
				if (!loaiSp.toUpperCase().equals("XH51") && !loaiSp.toUpperCase().equals("XH50")) {
					return Constants.ValidateCodeService.QNA_110;
				}
				check = false;
			}
			if (Utils.isNullObject(customer.getDKKD()) || customer.getDKKD().length() > 50) {
				if (!loaiSp.toUpperCase().equals("XH51") && !loaiSp.toUpperCase().equals("XH50")) {
					return Constants.ValidateCodeService.QNA_111;
				}
				check = false;
			}
//			if (check) {
//				if (!existed) {
//					List<QnaInDetail> outDetails = qnaInDetailDao.findDuplicateQuestionCorporationNotNull(
//							customer.getMSThue(), customer.getDKKD(), customer.getTenKH(),
//							user.getPartnerCompany().getCode(), customer.getMaSP(), DateUtils.addDate(new Date(), -1));
//					if (outDetails != null && outDetails.size() > 0) {
//						lstErrorCode.add(Constants.ValidateCodeService.QNA_118);
//					}
//				}
//			}

			// QNA_127 Há»�i tin PhÃ¡p nhÃ¢n nhÆ°ng láº¡i cÃ³ thÃ´ng tin CMND
			if (!Utils.isNullObject(customer.getSoCMT())) {
				return Constants.ValidateCodeService.QNA_127;
			}

			// QNA_128 Há»�i tin PhÃ¡p nhÃ¢n nhÆ°ng mÃ£ CIC lÃ  Thá»ƒ nhÃ¢n
			if (!Utils.isNullObject(customer.getMaCIC()) && customer.getMaCIC().length() > 4) {
				String maCic = customer.getMaCIC();
				String checkType = maCic.substring(2, 4);
				if (StringUtils.isNumeric(maCic)) {
					int intType = Integer.valueOf(checkType);
					if (intType > Constants.PN_END_INT) {
						return Constants.ValidateCodeService.QNA_128;
					}
				}
			}
		} else {
			// the nhan
			if (Utils.isNullObject(customer.getSoCMT()) || customer.getSoCMT().length() > 15) {
				return Constants.ValidateCodeService.QNA_112;
			} else {
//				if (!existed) {
//					List<QnaInDetail> outDetails = qnaInDetailDao.findDuplicateQuestionPersons(customer.getSoCMT(),
//							customer.getTenKH(), user.getPartnerCompany().getCode(), customer.getMaSP(),
//							DateUtils.addDate(new Date(), -1));
//					if (outDetails != null && outDetails.size() > 0) {
//						lstErrorCode.add(Constants.ValidateCodeService.QNA_118);
//					}
//				}
			}

			// QNA_126 Há»�i tin Thá»ƒ nhÃ¢n nhÆ°ng láº¡i cÃ³ thÃ´ng tin MST hoáº·c DKKD
			if (!Utils.isNullObject(customer.getMSThue()) || !Utils.isNullObject(customer.getDKKD())) {
				return Constants.ValidateCodeService.QNA_126;
			}

			// QNA_129 Há»�i tin Thá»ƒ nhÃ¢n nhÆ°ng mÃ£ CIC lÃ  PhÃ¡p nhÃ¢n
			if (!Utils.isNullObject(customer.getMaCIC()) && customer.getMaCIC().length() > 4) {
				String maCic = customer.getMaCIC();
				String checkType = maCic.substring(2, 4);
				if (StringUtils.isNumeric(maCic)) {
					int intType = Integer.valueOf(checkType);
					if (intType < Constants.TN_START_INT) {
						return Constants.ValidateCodeService.QNA_129;
					}
				}
			}
		}

		if (!Utils.isNullObject(customer.getGhiChu()) && customer.getGhiChu().length() > 200) {
			return Constants.ValidateCodeService.QNA_115;
		}

//		QnaInDetail detail = qnaInDetailDao.findByMsphieuAndTCTD(customer.getMaSoPhieu(),
//				user.getPartnerCompany().getCode());
//		if (detail != null) {
//			lstErrorCode.add(Constants.ValidateCodeService.QNA_104);
//		}

		// Hoangtn update san pham co dinh TODO
		if (loaiSp.toUpperCase().equals("XH51") || loaiSp.toUpperCase().equals("XH50")) {
			if (Utils.isNullObject(customer.getNgonNgu())) {
				return Constants.ValidateCodeService.QNA_134;
			} else if (!customer.getNgonNgu().toUpperCase().equals("V")
					&& !customer.getNgonNgu().toUpperCase().equals("E")) {
				return Constants.ValidateCodeService.QNA_134;
			}

			if (Utils.isNullObject(customer.getNamTaiChinh())) {
				return Constants.ValidateCodeService.QNA_135;
			} else if (customer.getNamTaiChinh().length() > 20) {
				return Constants.ValidateCodeService.QNA_135;
			}
		} else {
			if (!Utils.isNullOrEmpty(customer.getNgonNgu()) && !"V".equalsIgnoreCase(customer.getNgonNgu())
					&& !"E".equalsIgnoreCase(customer.getNgonNgu())) {
				return Constants.ValidateCodeService.QNA_134;
			}
			if (!Utils.isNullOrEmpty(customer.getNamTaiChinh()) && customer.getNamTaiChinh().length() > 20) {
				return Constants.ValidateCodeService.QNA_134;
			}
		}
		return null;
	}

	@Autowired
	MessageSource messageSource;

	@Autowired
	Authen authen;

	@Autowired
	private SOAPConnector soapConnector;

	@Value("${url.gateway.app}")
	private String urlGateWayApp;

	@Autowired
	SysParamDao sysParamDao;

	@Autowired
	LogServicesDao logServicesDao;

	@Autowired
	ServiceInfoDao serviceInfoDao;

	@Override
	public PHTimKiemKH sendSearchCus(TimKiemKH request, HttpServletRequest rq) {
		PHTimKiemKH response = new PHTimKiemKH();
		LogService logService = null;
		Date startDate = new Date();
		DSLoi dsLoi = new DSLoi();
		ServiceInfo serviceInfo = serviceInfoDao.findServiceByRequestClass(TimKiemKH.class.getName());
		try {
			// validate
			String error = null;
			if (rq == null) {
				if (!sysUsers(request.getNguoiYeuCau())) {
					response.setTTPhanHoi(buildTTPhanHoi(true, Constants.ValidateCodeService.CIC_002, null));
					return response;
				}
			}
			if (!Utils.isNullOrEmpty(request.getTenKH())) {
				if (request.getTenKH().length() < 4) {
					error = Constants.ValidateCodeService.TK11_001;
					Loi sloi = new Loi();
					sloi.setMaLoi(error);
					sloi.setMoTaLoi(messageSource.getMessage(error, null, null));
					dsLoi.getLoi().add(sloi);
				}
			}
			if (Utils.isNullObject(request.getLoaiKH())) {
				error = Constants.ValidateCodeService.TK11_002;
				Loi sloi = new Loi();
				sloi.setMaLoi(error);
				sloi.setMoTaLoi(messageSource.getMessage(error, null, null));
				dsLoi.getLoi().add(sloi);
			} else {

				switch (request.getLoaiKH().toString()) {
				case "1":// pháp nhân
					if (Utils.isNullOrEmpty(request.getMSThue()) && Utils.isNullOrEmpty(request.getDKKD())
							&& Utils.isNullOrEmpty(request.getTenKH()) && Utils.isNullOrEmpty(request.getMaCIC())) {
						error = Constants.ValidateCodeService.TK11_003;
						Loi sloi = new Loi();
						sloi.setMaLoi(error);
						sloi.setMoTaLoi(messageSource.getMessage(error, null, null));
						dsLoi.getLoi().add(sloi);
					}
					break;
				case "2":// thể nhân
					if (Utils.isNullOrEmpty(request.getSoCMT()) && Utils.isNullOrEmpty(request.getMaCIC())) {
						error = Constants.ValidateCodeService.TK11_004;
						Loi sloi = new Loi();
						sloi.setMaLoi(error);
						sloi.setMoTaLoi(messageSource.getMessage(error, null, null));
						dsLoi.getLoi().add(sloi);
					}
					break;
				default:
					error = Constants.ValidateCodeService.TK11_002;
					Loi sloi = new Loi();
					sloi.setMaLoi(error);
					sloi.setMoTaLoi(messageSource.getMessage(error, null, null));
					dsLoi.getLoi().add(sloi);
					break;
				}
			}

			if (!Utils.isNullObject(error)) {
				response.setTimKiemKHKQ(buildTimKiemKHKQ(dsLoi));
				response.setTTPhanHoi(buildTTPhanHoi(true, Constants.ValidateCodeService.CMM_005, null));
				logServicesDao.saveLog(logService, Constants.LOG_STATUS_SUCCESS,
						messageSource.getMessage(Constants.ValidateCodeService.CMM_005, null, null),
						Constants.ValidateCodeService.CMM_005, null, response, null, startDate, request, serviceInfo,
						Constants.LOG_STATUS_DEN, request.getNguoiYeuCau());
				return response;
			}

			StringWriter sw = new StringWriter();
			JAXB.marshal(request, sw);
			String xmlData2 = sw.toString();
			logger.info("requestCore: " + xmlData2);
			sw.close();
			// call service cic gateway
			authen.checkToken();
			String jsonRequest = new Gson().toJson(request);
			vn.org.cic.h2h.ws.endpoint.cicqr.TimKiemKH reCore = new Gson().fromJson(jsonRequest,
					vn.org.cic.h2h.ws.endpoint.cicqr.TimKiemKH.class);
			vn.org.cic.h2h.ws.endpoint.cicqr.PHTimKiemKH rs = (vn.org.cic.h2h.ws.endpoint.cicqr.PHTimKiemKH) soapConnector
					.callWebServiceAuthen(sysParamDao.getSysParamByCode(urlGateWayApp).getValue(), reCore,
							authen.getPhDangNHap().getToken());
			String jsonRs = new Gson().toJson(rs);
			response = new Gson().fromJson(jsonRs, PHTimKiemKH.class);
			logServicesDao.saveLog(logService, Constants.LOG_STATUS_SUCCESS,
					messageSource.getMessage(Constants.ValidateCodeService.CMM_000, null, null),
					Constants.ValidateCodeService.CMM_000, null, response, null, startDate, request, serviceInfo,
					Constants.LOG_STATUS_DEN, request.getNguoiYeuCau());

		} catch (Exception e) {
			logger.error("searchCustomer: ", e);
			response.setTTPhanHoi(buildTTPhanHoi(true, Constants.ValidateCodeService.CMM_999, null));
			try {
				logServicesDao.saveLog(logService, Constants.LOG_STATUS_ERROR,
						messageSource.getMessage(Constants.ValidateCodeService.CMM_999, null, null),
						Constants.ValidateCodeService.CMM_999, null, response, Utils.convertExceptionToString(e),
						startDate, request, serviceInfo, Constants.LOG_STATUS_DEN, request.getNguoiYeuCau());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				logger.error(e1.getMessage(), e1);
			}
			return response;
		}
		return response;
	}

	private static final Logger logger = LogManager.getLogger(validateServiceImpl.class);

	public TTPhanHoi buildTTPhanHoi(Boolean isError, String code, Object[] param) {
		TTPhanHoi phanHoi = new TTPhanHoi();
		if (isError) {
			phanHoi.setTrangThai(Constants.ValidateCodeService.STATUS_ERROR);
			phanHoi.setMa(code);
			phanHoi.setMoTa(messageSource.getMessage(code, param, null));
		} else {
			phanHoi.setTrangThai(Constants.ValidateCodeService.STATUS_SUCCESS);
			phanHoi.setMa(Constants.ValidateCodeService.CMM_000);
			phanHoi.setMoTa(messageSource.getMessage(Constants.ValidateCodeService.CMM_000, param, null));
		}
		return phanHoi;
	}

	public TimKiemKHKQ buildTimKiemKHKQ(DSLoi dSLoi) {
		TimKiemKHKQ timKiemKHKQ = new TimKiemKHKQ();
		timKiemKHKQ.setDSLoi(dSLoi);
		return timKiemKHKQ;
	}

	@Override
	public PHVanTinChung inquiryS37(VanTinChung request, HttpServletRequest rq) throws Exception {
		// TODO Auto-generated method stub
		logger.info("BEGIN interViewProducts MaSP : " + request.getMaSP());
		Date startDate = new Date();
		LogService logService = null;
		ServiceInfo serviceInfo = serviceInfoDao.findServiceByRequestClass(VanTinChung.class.getName());
		PHVanTinChung response = new PHVanTinChung();
		try {
			//response = validateService.inquiryS37(request, null);
			String error = null;
			if (rq == null) {
				if (!sysUsers(request.getNguoiYeuCau())) {
					response = buildPHVanTinChungCIC(false, Constants.ValidateCodeService.CIC_002, null);
					return response;
				}
			}
			StringWriter sw = new StringWriter();
			JAXB.marshal(request, sw);
			String xmlData2 = sw.toString();
			logger.info("request: " + xmlData2);
			sw.close();
			// call service cic gateway
			authen.checkToken();
			String jsonRequest = new Gson().toJson(request);
			vn.org.cic.h2h.ws.endpoint.cicqr.VanTinChung reCore = new Gson().fromJson(jsonRequest,
					vn.org.cic.h2h.ws.endpoint.cicqr.VanTinChung.class);
			vn.org.cic.h2h.ws.endpoint.cicqr.PHVanTinChung rs = (vn.org.cic.h2h.ws.endpoint.cicqr.PHVanTinChung) soapConnector
					.callWebServiceAuthen(sysParamDao.getSysParamByCode(urlGateWayApp).getValue(), reCore,
							authen.getPhDangNHap().getToken());
			String jsonRs = new Gson().toJson(rs);
			response = new Gson().fromJson(jsonRs, PHVanTinChung.class);
			sw = new StringWriter();
			JAXB.marshal(jsonRs, sw);
			String xmlDataRespones = sw.toString();
			logger.info("dataS37Response:" + xmlDataRespones);
			sw.close();
			logServicesDao.saveLog(logService, Constants.LOG_STATUS_SUCCESS,
					messageSource.getMessage(Constants.ValidateCodeService.CMM_000, null, null),
					Constants.ValidateCodeService.CMM_000, "S37", response, null,
					startDate, request, serviceInfo, Constants.LOG_STATUS_DEN, request.getNguoiYeuCau());
		} catch (Exception e) {
			logger.error("interViewProducts", e);
			response = buildPHVanTinChungCIC(false, Constants.ValidateCodeService.CMM_999, null);
			try {
				logServicesDao.saveLog(logService, Constants.LOG_STATUS_ERROR,
						messageSource.getMessage(Constants.ValidateCodeService.CMM_999, null, null),
						Constants.ValidateCodeService.CMM_999, "S37", response, Utils.convertExceptionToString(e),
						startDate, request, serviceInfo, Constants.LOG_STATUS_DEN, request.getNguoiYeuCau());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				logger.error(e1.getMessage(), e1);
			}
			return response;
		}
		
		logger.info("END interViewProducts MaSP : " + request.getMaSP());
		
		
		
		return response;
	}
	
	public PHVanTinChung buildPHVanTinChungCIC(boolean isError, String code,
			Object[] param) {
		PHVanTinChung vanTinChung = new PHVanTinChung();
		if (!isError) {
			vanTinChung.setTrangThai(Constants.ValidateCodeService.STATUS_ERROR);
			vanTinChung.setMa(code);
			vanTinChung.setMoTa(messageSource.getMessage(code, param, null));
		} else {
			vanTinChung.setTrangThai(Constants.ValidateCodeService.STATUS_SUCCESS);
			vanTinChung.setMa(code);
			vanTinChung.setMoTa(messageSource.getMessage(code, param, null));
		}
		return vanTinChung;
	}
}
