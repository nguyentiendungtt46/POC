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
//import vn.com.cmc.entity.LogService;
//import vn.com.cmc.entity.ServiceInfo;
//import vn.com.cmc.entity.SysUsers;
//import vn.com.cmc.service.AuthenService;
//import vn.com.cmc.service.QuestionAnswerUtils;
//import vn.com.cmc.service.ValidateService;
//import vn.com.cmc.utils.Constants;
//import vn.com.cmc.utils.Utils;
//import vn.org.cic.h2h.ws.endpoint.cicuserinfo.DoiMatKhau;
//import vn.org.cic.h2h.ws.endpoint.cicuserinfo.PHDoiMatKhau;
//
//@Endpoint
//public class CicUserInfoEndpoint {
//	private static final Logger logger = LogManager.getLogger(CicUserInfoEndpoint.class);
//	private static final String NAMESPACE_URI = "http://www.endpoint.ws.h2h.cic.org.vn/cicuserinfo";
//
//	@Value("${verify.enabled}")
//	private boolean verify;
//
//	@Autowired
//	HttpServletRequest httpRequest;
//
//	@Autowired
//	ValidateService validateService;
//
//	@Autowired
//	AuthenService authenService;
//
//	@Autowired
//	QuestionAnswerUtils questionAnswerUtils;
//
//	@Autowired
//	MessageSource messageSource;
//
//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "DoiMatKhau")
//	@ResponsePayload
//	public PHDoiMatKhau sendChangePassword(@RequestPayload DoiMatKhau request, MessageContext messageContext)
//			throws JAXBException {
//		PHDoiMatKhau response = new PHDoiMatKhau();
//		LogService logService = null;
//		SysUsers userAuthen = null;
//		ServiceInfo serviceInfo = null;
//		if (!Utils.isNullObject(messageContext.getProperty("logService")))
//			logService = (LogService) messageContext.getProperty("logService");
//		if (!Utils.isNullObject(messageContext.getProperty("userAuthen")))
//			userAuthen = (SysUsers) messageContext.getProperty("userAuthen");
//		if (!Utils.isNullObject(messageContext.getProperty("serviceInfo")))
//			serviceInfo = (ServiceInfo) messageContext.getProperty("serviceInfo");
//		try {
//			// validate
//			DoiMatKhau hiddenPass = new DoiMatKhau();
//			hiddenPass.setTenDangNhap(request.getTenDangNhap());
//			hiddenPass.setMatKhauCu("******");
//			hiddenPass.setMatKhauMoi("******");
//			logService.setRequestContent(Utils.convertObjToXML(hiddenPass, DoiMatKhau.class));
//			if (verify) {
//				if (!Utils.isNullObject(userAuthen)) {
//					String error = validateService.validateAll(userAuthen, httpRequest, serviceInfo, false, false);
//					if (!Utils.isNullObject(error)) {
//						PHDoiMatKhau res = authenService.buildChangePassResponse(error);
//						questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_BLOCK,
//								messageSource.getMessage(error, null, null), error, res, null);
//						return res;
//					}
//				}
//			}
//
//			response = authenService.sendChangePassword(userAuthen, request);
//			// end log
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_SUCCESS, Constants.LOG_MESSAGE_SUCCESS,
//					Constants.ValidateCodeService.CMM_000, response, null);
//		} catch (Exception e) {
//			logger.error(e);
//			PHDoiMatKhau res = authenService.buildChangePassResponse(Constants.ValidateCodeService.CMM_999);
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_ERROR, e.getMessage(),
//					Constants.ValidateCodeService.CMM_999, res, Utils.convertExceptionToString(e));
//			return res;
//		}
//		return response;
//	}
//
//}
