//package vn.com.cmc.ws.endpoint;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.xml.bind.JAXBException;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.MessageSource;
//import org.springframework.ws.context.MessageContext;
//import org.springframework.ws.server.endpoint.annotation.Endpoint;
//import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
//import org.springframework.ws.server.endpoint.annotation.RequestPayload;
//import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
//
//import com.google.gson.Gson;
//
//import vn.com.cmc.entity.LogService;
//import vn.com.cmc.entity.ServiceInfo;
//import vn.com.cmc.entity.SysUsers;
//import vn.com.cmc.service.QuestionAnswerUtils;
//import vn.com.cmc.service.SysParamService;
//import vn.com.cmc.service.ValidateService;
//import vn.com.cmc.utils.Constants;
//import vn.com.cmc.utils.Utils;
//import vn.com.cmc.ws.config.SOAPConnector;
//import vn.org.cic.h2h.ws.endpoint.cicreport.DSLoi;
//import vn.org.cic.h2h.ws.endpoint.cicreport.DanhSachTepBaoCao;
//import vn.org.cic.h2h.ws.endpoint.cicreport.Loi;
//import vn.org.cic.h2h.ws.endpoint.cicreport.PHDanhSachTepBaoCao;
//import vn.org.cic.h2h.ws.endpoint.cicreport.PHTepBaoCao;
//import vn.org.cic.h2h.ws.endpoint.cicreport.PHTepBaoCaoVanTin;
//import vn.org.cic.h2h.ws.endpoint.cicreport.PHTepPhiCauTruc;
//import vn.org.cic.h2h.ws.endpoint.cicreport.TTPhanHoi;
//import vn.org.cic.h2h.ws.endpoint.cicreport.TepBaoCao;
//import vn.org.cic.h2h.ws.endpoint.cicreport.TepBaoCaoTinDung;
//import vn.org.cic.h2h.ws.endpoint.cicreport.TepBaoCaoVanTin;
//import vn.org.cic.h2h.ws.endpoint.cicreport.TepPhiCauTruc;
//import vn.org.cic.h2h.ws.endpoint.cicreport.DanhSachTepTraSoat;
//import vn.org.cic.h2h.ws.endpoint.cicreport.PHDanhSachTepTraSoat;
//import vn.org.cic.h2h.ws.endpoint.cicreport.DanhSachTepPhiCauTruc;
//import vn.org.cic.h2h.ws.endpoint.cicreport.PHDanhSachTepPhiCauTruc;
//import vn.org.cic.h2h.ws.endpoint.cicreport.NhanTepTraSoat;
//import vn.org.cic.h2h.ws.endpoint.cicreport.PHNhanTepTraSoat;
//import vn.org.cic.h2h.ws.endpoint.cicreport.HuyBaoCao;
//import vn.org.cic.h2h.ws.endpoint.cicreport.PHHuyBaoCao;
//
//@Endpoint
//public class CicReportEndpoint {
//	private static final Logger logger = LogManager.getLogger(CicReportEndpoint.class);
//
////	@Value("${url.core.service}")
////	private String sysParamService.getUrlCoreService();
//
//	@Autowired
//	private QuestionAnswerUtils questionAnswerUtils;
//
//	@Autowired
//	MessageSource messageSource;
//
//	@Autowired
//	ValidateService validateService;
//
//	@Autowired
//	private HttpServletRequest httpRequest;
//
//	@Autowired
//	private SOAPConnector soapConnector;
//
//	@Autowired
//	SysParamService sysParamService;
//
//	private static final String NAMESPACE_URI = "http://www.endpoint.ws.h2h.cic.org.vn/cicreport";
//
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "TepBaoCao")
//	@ResponsePayload
//	public PHTepBaoCao sendReport(@RequestPayload TepBaoCao request, MessageContext messageContext)
//			throws JAXBException {
//		logger.info("START: sendReport ");
//
//		PHTepBaoCao response = new PHTepBaoCao();
//		LogService logService = null;
//		SysUsers userAuthen = null;
//		ServiceInfo serviceInfo = null;
//
//		if (!Utils.isNullObject(messageContext.getProperty("logService")))
//			logService = (LogService) messageContext.getProperty("logService");
//		if (!Utils.isNullObject(messageContext.getProperty("userAuthen")))
//			userAuthen = (SysUsers) messageContext.getProperty("userAuthen");
//		if (!Utils.isNullObject(messageContext.getProperty("serviceInfo")))
//			serviceInfo = (ServiceInfo) messageContext.getProperty("serviceInfo");
//
//		try {
//			// validate
//			String error = null;
//			if (!Utils.isNullObject(userAuthen)) {
//				error = validateService.validateAll(userAuthen, httpRequest, serviceInfo, false, false);
//				if (!Utils.isNullObject(error)) {
//					response.setTTPhanHoi(buildTTPhanHoi(true, error, null));
//					questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_BLOCK,
//							messageSource.getMessage(error, null, null), error, response, null);
//					return response;
//				}
//			}
//
//			// map request
//			Gson gson = new Gson();
//			String jsonRequest = gson.toJson(request);
//			vn.com.cmc.ws.cicReport.TepBaoCao requestCore = gson.fromJson(jsonRequest,
//					vn.com.cmc.ws.cicReport.TepBaoCao.class);
//			if (userAuthen != null)
//				requestCore.setNguoiYC(userAuthen.getUsername());
//			// call service
//			vn.com.cmc.ws.cicReport.PHTepBaoCao responseClient = (vn.com.cmc.ws.cicReport.PHTepBaoCao) soapConnector
//					.callWebService(sysParamService.getUrlCoreService(), requestCore);
//
//			// map response
//			String jsonResponse = gson.toJson(responseClient);
//			response = gson.fromJson(jsonResponse, PHTepBaoCao.class);
//
//			// set TTPhanHoi success
//			// response.setTTPhanHoi(buildTTPhanHoi(false, null, null));
//
//			// end log
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_SUCCESS, Constants.LOG_MESSAGE_SUCCESS,
//					Constants.ValidateCodeService.CMM_000, response, null);
//		} catch (Exception e) {
//			logger.error("sendReport: ", e);
//			response.setTTPhanHoi(buildTTPhanHoi(true, Constants.ValidateCodeService.CMM_999, null));
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_ERROR, e.getMessage(),
//					Constants.ValidateCodeService.CMM_999, response, Utils.convertExceptionToString(e));
//		}
//
//		return response;
//	}
//
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "TepBaoCaoVanTin")
//	@ResponsePayload
//	public PHTepBaoCaoVanTin getReport(@RequestPayload TepBaoCaoVanTin request, MessageContext messageContext)
//			throws Exception {
//		logger.info("START: getReport ");
//		PHTepBaoCaoVanTin response = new PHTepBaoCaoVanTin();
//
//		LogService logService = null;
//		SysUsers userAuthen = null;
//		ServiceInfo serviceInfo = null;
//
//		if (!Utils.isNullObject(messageContext.getProperty("logService")))
//			logService = (LogService) messageContext.getProperty("logService");
//		if (!Utils.isNullObject(messageContext.getProperty("userAuthen")))
//			userAuthen = (SysUsers) messageContext.getProperty("userAuthen");
//		if (!Utils.isNullObject(messageContext.getProperty("serviceInfo")))
//			serviceInfo = (ServiceInfo) messageContext.getProperty("serviceInfo");
//
//		try {
//			// validate
//			String error = null;
//			if (!Utils.isNullObject(userAuthen)) {
//				error = validateService.validateAll(userAuthen, httpRequest, serviceInfo, false, false);
//				if (!Utils.isNullObject(error)) {
//					response.setTTPhanHoi(buildTTPhanHoi(true, error, null));
//					questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_BLOCK,
//							messageSource.getMessage(error, null, null), error, response, null);
//					return response;
//				}
//			}
//
//			// map request
//			Gson gson = new Gson();
//			String jsonRequest = gson.toJson(request);
//			vn.com.cmc.ws.cicReport.TepBaoCaoVanTin requestCore = gson.fromJson(jsonRequest,
//					vn.com.cmc.ws.cicReport.TepBaoCaoVanTin.class);
//			requestCore.setNguoiYC(userAuthen.getUsername());
//			// call service
//			vn.com.cmc.ws.cicReport.PHTepBaoCaoVanTin responseClient = (vn.com.cmc.ws.cicReport.PHTepBaoCaoVanTin) soapConnector
//					.callWebService(sysParamService.getUrlCoreService(), requestCore);
//
//			// map response
//			String jsonResponse = gson.toJson(responseClient);
//			response = gson.fromJson(jsonResponse, PHTepBaoCaoVanTin.class);
//
//			// set TTPhanHoi success
//			// response.setTTPhanHoi(buildTTPhanHoi(false, null, null));
//
//			// end log
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_SUCCESS, Constants.LOG_MESSAGE_SUCCESS,
//					Constants.ValidateCodeService.CMM_000, response, null);
//
//		} catch (Exception e) {
//			logger.error("getReport: ", e);
//			response.setTTPhanHoi(buildTTPhanHoi(true, Constants.ValidateCodeService.CMM_999, null));
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_ERROR, e.getMessage(),
//					Constants.ValidateCodeService.CMM_999, response, Utils.convertExceptionToString(e));
//		}
//		return response;
//	}
//
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "TepPhiCauTruc")
//	@ResponsePayload
//	public PHTepPhiCauTruc cicFileUnstructured(@RequestPayload TepPhiCauTruc request, MessageContext messageContext)
//			throws Exception {
//		logger.info("START: cicFileUnstructured Gateway");
//		PHTepPhiCauTruc response = new PHTepPhiCauTruc();
//		LogService logService = null;
//		SysUsers userAuthen = null;
//		ServiceInfo serviceInfo = null;
//
//		if (!Utils.isNullObject(messageContext.getProperty("logService")))
//			logService = (LogService) messageContext.getProperty("logService");
//		if (!Utils.isNullObject(messageContext.getProperty("userAuthen")))
//			userAuthen = (SysUsers) messageContext.getProperty("userAuthen");
//		if (!Utils.isNullObject(messageContext.getProperty("serviceInfo")))
//			serviceInfo = (ServiceInfo) messageContext.getProperty("serviceInfo");
//		try {
//			String error = null;
//			if (!Utils.isNullObject(userAuthen)) {
//				error = validateService.validateAll(userAuthen, httpRequest, serviceInfo, false, false);
//				if (!Utils.isNullObject(error)) {
//					response.setTTPhanHoi(commonTTPhanHoi(true, error, null));
//					questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_BLOCK,
//							messageSource.getMessage(error, null, null), error, response, null);
//					return response;
//				}
//			}
//			Gson gson = new Gson();
//			String jsonRequest = gson.toJson(request);
//			vn.com.cmc.ws.cicReport.TepPhiCauTruc requestCore = gson.fromJson(jsonRequest,
//					vn.com.cmc.ws.cicReport.TepPhiCauTruc.class);
//			requestCore.setNguoiYC(userAuthen.getUsername());
//			vn.com.cmc.ws.cicReport.PHTepPhiCauTruc responseCore = (vn.com.cmc.ws.cicReport.PHTepPhiCauTruc) soapConnector
//					.callWebService(sysParamService.getUrlCoreService(), requestCore);
//			// map response
//			String jsonResponse = gson.toJson(responseCore);
//			response = gson.fromJson(jsonResponse, PHTepPhiCauTruc.class);
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_SUCCESS, Constants.LOG_MESSAGE_SUCCESS,
//					Constants.ValidateCodeService.CMM_000, response, null);
//		} catch (Exception e) {
//			logger.error("cicFileUnstructured: ", e);
//			response.setTTPhanHoi(commonTTPhanHoi(true, Constants.ValidateCodeService.CMM_999, null));
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_ERROR, e.getMessage(),
//					Constants.ValidateCodeService.CMM_999, response, Utils.convertExceptionToString(e));
//		}
//		logger.info("END: cicFileUnstructured Gateway");
//		return response;
//	}
//
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "DanhSachTepBaoCao")
//	@ResponsePayload
//	public PHDanhSachTepBaoCao cicListFileReport(@RequestPayload DanhSachTepBaoCao request,
//			MessageContext messageContext) throws Exception {
//		logger.info("START: cicListFileReport Gateway");
//		LogService logService = null;
//		SysUsers userAuthen = null;
//		ServiceInfo serviceInfo = null;
//		PHDanhSachTepBaoCao response = new PHDanhSachTepBaoCao();
//		if (!Utils.isNullObject(messageContext.getProperty("logService")))
//			logService = (LogService) messageContext.getProperty("logService");
//		if (!Utils.isNullObject(messageContext.getProperty("userAuthen")))
//			userAuthen = (SysUsers) messageContext.getProperty("userAuthen");
//		if (!Utils.isNullObject(messageContext.getProperty("serviceInfo")))
//			serviceInfo = (ServiceInfo) messageContext.getProperty("serviceInfo");
//		try {
//			String error = null;
//			if (!Utils.isNullObject(userAuthen)) {
//				error = validateService.validateAll(userAuthen, httpRequest, serviceInfo, false, false);
//				if (!Utils.isNullObject(error)) {
//					response.setTTPhanHoi(commonTTPhanHoi(true, error, null));
//					questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_BLOCK,
//							messageSource.getMessage(error, null, null), error, response, null);
//					return response;
//				}
//			}
//			Gson gson = new Gson();
//			String jsonRequest = gson.toJson(request);
//			vn.com.cmc.ws.cicReport.DanhSachTepBaoCao requestCore = gson.fromJson(jsonRequest,
//					vn.com.cmc.ws.cicReport.DanhSachTepBaoCao.class);
//			requestCore.setNguoiYC(userAuthen.getUsername());
//			vn.com.cmc.ws.cicReport.PHDanhSachTepBaoCao responseCore = (vn.com.cmc.ws.cicReport.PHDanhSachTepBaoCao) soapConnector
//					.callWebService(sysParamService.getUrlCoreService(), requestCore);
//			// map response
//			String jsonResponse = gson.toJson(responseCore);
//			response = gson.fromJson(jsonResponse, PHDanhSachTepBaoCao.class);
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_SUCCESS, Constants.LOG_MESSAGE_SUCCESS,
//					Constants.ValidateCodeService.CMM_000, response, null);
//		} catch (Exception e) {
//			logger.error("cicFileUnstructured: ", e);
//			response.setTTPhanHoi(commonTTPhanHoi(true, Constants.ValidateCodeService.CMM_999, null));
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_ERROR, e.getMessage(),
//					Constants.ValidateCodeService.CMM_999, response, Utils.convertExceptionToString(e));
//		}
//		logger.info("END: cicListFileReport Gateway");
//		return response;
//	}
//
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "DanhSachTepTraSoat")
//	@ResponsePayload
//	public PHDanhSachTepTraSoat cicStatusFileUnstructured(@RequestPayload DanhSachTepTraSoat request,
//			MessageContext messageContext) throws Exception {
//		logger.info("START: cicStatusFileUnstructured Gateway");
//		LogService logService = null;
//		SysUsers userAuthen = null;
//		ServiceInfo serviceInfo = null;
//		PHDanhSachTepTraSoat response = new PHDanhSachTepTraSoat();
//		if (!Utils.isNullObject(messageContext.getProperty("logService")))
//			logService = (LogService) messageContext.getProperty("logService");
//		if (!Utils.isNullObject(messageContext.getProperty("userAuthen")))
//			userAuthen = (SysUsers) messageContext.getProperty("userAuthen");
//		if (!Utils.isNullObject(messageContext.getProperty("serviceInfo")))
//			serviceInfo = (ServiceInfo) messageContext.getProperty("serviceInfo");
//		try {
//			String error = null;
//			if (!Utils.isNullObject(userAuthen)) {
//				error = validateService.validateAll(userAuthen, httpRequest, serviceInfo, false, false);
//				if (!Utils.isNullObject(error)) {
//					response.setTTPhanHoi(commonTTPhanHoi(true, error, null));
//					questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_BLOCK,
//							messageSource.getMessage(error, null, null), error, response, null);
//					return response;
//				}
//			}
//			Gson gson = new Gson();
//			String jsonRequest = gson.toJson(request);
//			vn.com.cmc.ws.cicReport.DanhSachTepTraSoat requestCore = gson.fromJson(jsonRequest,
//					vn.com.cmc.ws.cicReport.DanhSachTepTraSoat.class);
//			requestCore.setNguoiYC(userAuthen.getUsername());
//			vn.com.cmc.ws.cicReport.PHDanhSachTepTraSoat responseCore = (vn.com.cmc.ws.cicReport.PHDanhSachTepTraSoat) soapConnector
//					.callWebService(sysParamService.getUrlCoreService(), requestCore);
//			// map response
//			String jsonResponse = gson.toJson(responseCore);
//			response = gson.fromJson(jsonResponse, PHDanhSachTepTraSoat.class);
//
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_SUCCESS, Constants.LOG_MESSAGE_SUCCESS,
//					Constants.ValidateCodeService.CMM_000, response, null);
//		} catch (Exception e) {
//			logger.error("cicFileUnstructured: ", e);
//			response.setTTPhanHoi(commonTTPhanHoi(true, Constants.ValidateCodeService.CMM_999, null));
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_ERROR, e.getMessage(),
//					Constants.ValidateCodeService.CMM_999, response, Utils.convertExceptionToString(e));
//		}
//		logger.info("END: cicStatusFileUnstructured Gateway");
//		return response;
//	}
//
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "DanhSachTepPhiCauTruc")
//	@ResponsePayload
//	public PHDanhSachTepPhiCauTruc cicStatusFileUnstructuredZero(@RequestPayload DanhSachTepPhiCauTruc request,
//			MessageContext messageContext) throws Exception {
//		logger.info("START: DanhSachTepPhiCauTruc Gateway");
//		LogService logService = null;
//		SysUsers userAuthen = null;
//		ServiceInfo serviceInfo = null;
//		PHDanhSachTepPhiCauTruc response = new PHDanhSachTepPhiCauTruc();
//		if (!Utils.isNullObject(messageContext.getProperty("logService")))
//			logService = (LogService) messageContext.getProperty("logService");
//		if (!Utils.isNullObject(messageContext.getProperty("userAuthen")))
//			userAuthen = (SysUsers) messageContext.getProperty("userAuthen");
//		if (!Utils.isNullObject(messageContext.getProperty("serviceInfo")))
//			serviceInfo = (ServiceInfo) messageContext.getProperty("serviceInfo");
//		try {
//			String error = null;
//			if (!Utils.isNullObject(userAuthen)) {
//				error = validateService.validateAll(userAuthen, httpRequest, serviceInfo, false, false);
//				if (!Utils.isNullObject(error)) {
//					response.setTTPhanHoi(commonTTPhanHoi(true, error, null));
//					questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_BLOCK,
//							messageSource.getMessage(error, null, null), error, response, null);
//					return response;
//				}
//			}
//			Gson gson = new Gson();
//			String jsonRequest = gson.toJson(request);
//			vn.com.cmc.ws.cicReport.DanhSachTepPhiCauTruc requestCore = gson.fromJson(jsonRequest,
//					vn.com.cmc.ws.cicReport.DanhSachTepPhiCauTruc.class);
//			requestCore.setNguoiYC(userAuthen.getUsername());
//			vn.com.cmc.ws.cicReport.PHDanhSachTepPhiCauTruc responseCore = (vn.com.cmc.ws.cicReport.PHDanhSachTepPhiCauTruc) soapConnector
//					.callWebService(sysParamService.getUrlCoreService(), requestCore);
//			// map response
//			String jsonResponse = gson.toJson(responseCore);
//			response = gson.fromJson(jsonResponse, PHDanhSachTepPhiCauTruc.class);
//
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_SUCCESS, Constants.LOG_MESSAGE_SUCCESS,
//					Constants.ValidateCodeService.CMM_000, response, null);
//		} catch (Exception e) {
//			logger.error("DanhSachTepPhiCauTruc: ", e);
//			response.setTTPhanHoi(commonTTPhanHoi(true, Constants.ValidateCodeService.CMM_999, null));
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_ERROR, e.getMessage(),
//					Constants.ValidateCodeService.CMM_999, response, Utils.convertExceptionToString(e));
//		}
//		logger.info("END: DanhSachTepPhiCauTruc Gateway");
//		return response;
//	}
//
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "NhanTepTraSoat")
//	@ResponsePayload
//	public PHNhanTepTraSoat downloadFileUnstructured(@RequestPayload NhanTepTraSoat request,
//			MessageContext messageContext) throws Exception {
//		logger.info("START: downloadFileUnstructured Gateway");
//		LogService logService = null;
//		SysUsers userAuthen = null;
//		ServiceInfo serviceInfo = null;
//		PHNhanTepTraSoat response = new PHNhanTepTraSoat();
//		if (!Utils.isNullObject(messageContext.getProperty("logService")))
//			logService = (LogService) messageContext.getProperty("logService");
//		if (!Utils.isNullObject(messageContext.getProperty("userAuthen")))
//			userAuthen = (SysUsers) messageContext.getProperty("userAuthen");
//		if (!Utils.isNullObject(messageContext.getProperty("serviceInfo")))
//			serviceInfo = (ServiceInfo) messageContext.getProperty("serviceInfo");
//		try {
//			String error = null;
//			if (!Utils.isNullObject(userAuthen)) {
//				error = validateService.validateAll(userAuthen, httpRequest, serviceInfo, false, false);
//				if (!Utils.isNullObject(error)) {
//					response.setTTPhanHoi(commonTTPhanHoi(true, error, null));
//					questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_BLOCK,
//							messageSource.getMessage(error, null, null), error, response, null);
//					return response;
//				}
//			}
//			Gson gson = new Gson();
//			String jsonRequest = gson.toJson(request);
//			vn.com.cmc.ws.cicReport.NhanTepTraSoat requestCore = gson.fromJson(jsonRequest,
//					vn.com.cmc.ws.cicReport.NhanTepTraSoat.class);
//			requestCore.setNguoiYC(userAuthen.getUsername());
//			vn.com.cmc.ws.cicReport.PHNhanTepTraSoat responseCore = (vn.com.cmc.ws.cicReport.PHNhanTepTraSoat) soapConnector
//					.callWebService(sysParamService.getUrlCoreService(), requestCore);
//			// map response
//			String jsonResponse = gson.toJson(responseCore);
//			response = gson.fromJson(jsonResponse, PHNhanTepTraSoat.class);
//
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_SUCCESS, Constants.LOG_MESSAGE_SUCCESS,
//					Constants.ValidateCodeService.CMM_000, response, null);
//		} catch (Exception e) {
//			logger.error("cicFileUnstructured: ", e);
//			response.setTTPhanHoi(commonTTPhanHoi(true, Constants.ValidateCodeService.CMM_999, null));
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_ERROR, e.getMessage(),
//					Constants.ValidateCodeService.CMM_999, response, Utils.convertExceptionToString(e));
//		}
//		logger.info("END: downloadFileUnstructured Gateway");
//		return response;
//	}
//
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "TepBaoCaoTinDung")
//	@ResponsePayload
//	public PHTepBaoCao sendReportK31K32(@RequestPayload TepBaoCaoTinDung request, MessageContext messageContext)
//			throws JAXBException {
//		logger.info("START: sendReportK31K32 ");
//
//		PHTepBaoCao response = new PHTepBaoCao();
//		LogService logService = null;
//		SysUsers userAuthen = null;
//		ServiceInfo serviceInfo = null;
//
//		if (!Utils.isNullObject(messageContext.getProperty("logService")))
//			logService = (LogService) messageContext.getProperty("logService");
//		if (!Utils.isNullObject(messageContext.getProperty("userAuthen")))
//			userAuthen = (SysUsers) messageContext.getProperty("userAuthen");
//		if (!Utils.isNullObject(messageContext.getProperty("serviceInfo")))
//			serviceInfo = (ServiceInfo) messageContext.getProperty("serviceInfo");
//
//		try {
//			// validate
//			String error = null;
//			if (!Utils.isNullObject(userAuthen)) {
//				error = validateService.validateAll(userAuthen, httpRequest, serviceInfo, false, false);
//				if (!Utils.isNullObject(error)) {
//					response.setTTPhanHoi(buildTTPhanHoi(true, error, null));
//					questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_BLOCK,
//							messageSource.getMessage(error, null, null), error, response, null);
//					return response;
//				}
//			}
//
//			// map request
//			Gson gson = new Gson();
//			String jsonRequest = gson.toJson(request);
//			vn.com.cmc.ws.cicReport.TepBaoCaoTinDung requestCore = gson.fromJson(jsonRequest,
//					vn.com.cmc.ws.cicReport.TepBaoCaoTinDung.class);
//			if (userAuthen != null)
//				requestCore.setNguoiYC(userAuthen.getUsername());
//			// call service
//			vn.com.cmc.ws.cicReport.PHTepBaoCao responseClient = (vn.com.cmc.ws.cicReport.PHTepBaoCao) soapConnector
//					.callWebService(sysParamService.getUrlCoreService(), requestCore);
//
//			// map response
//			String jsonResponse = gson.toJson(responseClient);
//			response = gson.fromJson(jsonResponse, PHTepBaoCao.class);
//
//			// set TTPhanHoi success
//			// response.setTTPhanHoi(buildTTPhanHoi(false, null, null));
//
//			// end log
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_SUCCESS, Constants.LOG_MESSAGE_SUCCESS,
//					Constants.ValidateCodeService.CMM_000, response, null);
//		} catch (Exception e) {
//			logger.error("sendReportK31K32: ", e);
//			response.setTTPhanHoi(buildTTPhanHoi(true, Constants.ValidateCodeService.CMM_999, null));
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_ERROR, e.getMessage(),
//					Constants.ValidateCodeService.CMM_999, response, Utils.convertExceptionToString(e));
//		}
//
//		return response;
//	}
//
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "HuyBaoCao")
//	@ResponsePayload
//	public PHHuyBaoCao huyBaoCao(@RequestPayload HuyBaoCao request, MessageContext messageContext)
//			throws JAXBException {
//		logger.info("START: removeReport Gateway ");
//
//		PHHuyBaoCao response = new PHHuyBaoCao();
//		LogService logService = null;
//		SysUsers userAuthen = null;
//		ServiceInfo serviceInfo = null;
//
//		if (!Utils.isNullObject(messageContext.getProperty("logService")))
//			logService = (LogService) messageContext.getProperty("logService");
//		if (!Utils.isNullObject(messageContext.getProperty("userAuthen")))
//			userAuthen = (SysUsers) messageContext.getProperty("userAuthen");
//		if (!Utils.isNullObject(messageContext.getProperty("serviceInfo")))
//			serviceInfo = (ServiceInfo) messageContext.getProperty("serviceInfo");
//
//		try {
//			// validate
//			String error = null;
//			if (!Utils.isNullObject(userAuthen)) {
//				error = validateService.validateAll(userAuthen, httpRequest, serviceInfo, false, false);
//				if (!Utils.isNullObject(error)) {
//					response.setTTPhanHoi(buildTTPhanHoi(true, error, null));
//					questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_BLOCK,
//							messageSource.getMessage(error, null, null), error, response, null);
//					return response;
//				}
//			}
//
//			// map request
//			Gson gson = new Gson();
//			String jsonRequest = gson.toJson(request);
//			vn.com.cmc.ws.cicReport.HuyBaoCao requestCore = gson.fromJson(jsonRequest,
//					vn.com.cmc.ws.cicReport.HuyBaoCao.class);
//			if (userAuthen != null)
//				requestCore.setNguoiYC(userAuthen.getUsername());
//			// call service
//			vn.com.cmc.ws.cicReport.PHHuyBaoCao responseClient = (vn.com.cmc.ws.cicReport.PHHuyBaoCao) soapConnector
//					.callWebService(sysParamService.getUrlCoreService(), requestCore);
//
//			// map response
//			String jsonResponse = gson.toJson(responseClient);
//			response = gson.fromJson(jsonResponse, PHHuyBaoCao.class);
//
//			// set TTPhanHoi success
//			// response.setTTPhanHoi(buildTTPhanHoi(false, null, null));
//
//			// end log
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_SUCCESS, Constants.LOG_MESSAGE_SUCCESS,
//					Constants.ValidateCodeService.CMM_000, response, null);
//		} catch (Exception e) {
//			logger.error("removeReport Gateway: ", e);
//			response.setTTPhanHoi(buildTTPhanHoi(true, Constants.ValidateCodeService.CMM_999, null));
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_ERROR, e.getMessage(),
//					Constants.ValidateCodeService.CMM_999, response, Utils.convertExceptionToString(e));
//		}
//		logger.info("END: removeReport Gateway ");
//		return response;
//	}
//
//	// build thong tin phan hoi
//	public TTPhanHoi buildTTPhanHoi(Boolean isError, String code, Object[] param) {
//		TTPhanHoi phanHoi = new TTPhanHoi();
//		Loi loi = new Loi();
//		DSLoi dsLoi = new DSLoi();
//		if (isError) {
//			phanHoi.setTrangThai(Constants.ValidateCodeService.STATUS_ERROR);
//			loi.setMa(code);
//			loi.setMoTa(messageSource.getMessage(code, param, null));
//			dsLoi.getLoi().add(loi);
//			phanHoi.setDSLoi(dsLoi);
//		} else {
//			phanHoi.setTrangThai(Constants.ValidateCodeService.STATUS_SUCCESS);
//			loi.setMa(Constants.ValidateCodeService.CMM_000);
//			loi.setMoTa(messageSource.getMessage(Constants.ValidateCodeService.CMM_000, param, null));
//			dsLoi.getLoi().add(loi);
//			phanHoi.setDSLoi(dsLoi);
//		}
//		return phanHoi;
//	}
//
//	public TTPhanHoi commonTTPhanHoi(Boolean isError, String code, Object[] param) {
//		TTPhanHoi phanHoi = new TTPhanHoi();
//		if (isError) {
//			phanHoi.setTrangThai(Constants.ValidateCodeService.STATUS_ERROR);
//			phanHoi.setMa(code);
//			phanHoi.setMoTa(messageSource.getMessage(code, param, null));
//		} else {
//			phanHoi.setTrangThai(Constants.ValidateCodeService.STATUS_SUCCESS);
//			phanHoi.setMa(Constants.ValidateCodeService.CMM_000);
//			phanHoi.setMoTa(messageSource.getMessage(Constants.ValidateCodeService.CMM_000, param, null));
//		}
//		return phanHoi;
//	}
//}
