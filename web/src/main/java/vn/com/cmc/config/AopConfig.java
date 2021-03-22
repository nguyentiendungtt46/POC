package vn.com.cmc.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class AopConfig {
//	private static final Logger logger = LogManager.getLogger(AopConfig.class);
//
//	@Autowired
//	private HttpServletRequest httpRequest;
//
//	@Autowired
//	private LogServicesDao logServicesDao;
//
//	@Autowired
//	private ServiceInfoDao serviceInfoDao;
//
////	@Autowired
////	QuestionAnswerUtils questionAnswerUtils;
//
//	// Around -> Any method within resource annotated with @Controller annotation
//	@Around(value = "execution(* vn.com.cmc.ws.endpoint.*.*(..))")
//	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
//		LogService logService = null;
//		SysUsers userAuthen = null;
//		ServiceInfo serviceInfo = null;
//		Object input = null;
//		Object result = null;
//
//		Date startDate = new Date();
//		String userName = "";
//		Partner partner = null;
//
//		try {
//			// log start
//			Object[] listArgs = joinPoint.getArgs();
//
//			for (Object arg : listArgs) {
//				if (!arg.getClass().equals(DefaultMessageContext.class)) {
//					input = arg;
//					// service login
//					if (joinPoint.getSignature().getName().equals("sendAuthen")) {
//						DangNhap req = (DangNhap) arg;
//						userName = req.getTenDangNhap();
//					} else {
//						Principal principal = httpRequest.getUserPrincipal();
//						if (principal != null) {
//							userName = principal.getName();
//						}
//					}
//
//					String ip = Utils.getClientIpAddr(httpRequest);
//					serviceInfo = serviceInfoService.findServiceByRequestClass(arg.getClass().getName());
//					if (!Utils.isNullObject(serviceInfo)) {
//						System.out.println("content volumn: " + httpRequest.getContentLength() + " bytes : "
//								+ Utils.bytesToMeg(httpRequest.getContentLength()) + " MB");
////						if (arg.getClass().equals(vn.org.cic.h2h.ws.endpoint.cicqa.TepHoiTin.class)) {
////							vn.org.cic.h2h.ws.endpoint.cicqa.TepHoiTin req = (vn.org.cic.h2h.ws.endpoint.cicqa.TepHoiTin) arg;
////							vn.org.cic.h2h.ws.endpoint.cicqa.TepHoiTin request = new vn.org.cic.h2h.ws.endpoint.cicqa.TepHoiTin();
////							request.setMaSP(req.getMaSP());
////							request.setTenTep(req.getTenTep());
////							String requestContent = Utils.convertObjToXML(request, request.getClass());
////							logService = new LogService(serviceInfo, null, ip, null,
////									startDate, null, null, null, null, requestContent);
////						} else {
////							
////						}
//						String requestContent = Utils.convertObjToXML(arg, arg.getClass());
//						logService = new LogService(serviceInfo, null, ip, null,
//								startDate, null, null, null, null, requestContent);
//						logService.setType((short) 1);
//						logService = logServiceService.save(logService);
//					}
//				} else {
//					if (!Utils.isNullObject(logService)) {
//						DefaultMessageContext messageContext = (DefaultMessageContext) arg;
//						messageContext.setProperty("logService", logService);
//						messageContext.setProperty("serviceInfo", serviceInfo);
//						messageContext.setProperty("userAuthen", userAuthen);
//					}
//				}
//			}
//			// check chan partner
////			if (!Utils.isNullObject(partner) && !Utils.isNullObject(partner.getIsNotifi()) && partner.getIsNotifi()) {
////				if (!Utils.isNullObject(serviceInfo)) {
////					result = questionAnswerUtils.getNotifiContent(partner, serviceInfo);
////					if (!Utils.isNullObject(result)) {
////						questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_BLOCK, "Send notifi error",
////								Constants.ValidateCodeService.CMM_999, result, null);
////						new Thread(new UpdateLogRunnable(questionAnswerUtils, input, result, logService)).start();
////						return result;
////					}
////				}
////			}
//			// end check chan partner
//			// proceed
//			result = joinPoint.proceed();
//			new Thread(new UpdateLogRunnable(questionAnswerUtils, input, result, logService)).start();
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_ERROR, e.getMessage(),
//					Constants.ValidateCodeService.CMM_999, result, Utils.convertExceptionToString(e));
//		}
//		return result;
//	}
//
//	//
//	private byte[] convertToBytes(Object object) throws IOException {
//		String xmlString = "";
//		try {
//			xmlString = Utils.convertObjToXML(object, object.getClass());
//		} catch (JAXBException e) {
//			logger.error(e.getMessage(), e);
//		}
//		return xmlString.getBytes();
//	}

}
