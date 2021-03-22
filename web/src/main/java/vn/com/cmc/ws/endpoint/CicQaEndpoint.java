package vn.com.cmc.ws.endpoint;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.JAXBException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.w3c.dom.NodeList;

import cic.h2h.dao.hibernate.LogServicesDao;
import cic.h2h.dao.hibernate.QnaInDetailDao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import common.util.Formater;
import entity.CatProduct;
import entity.LogService;
import entity.QnaInDetail;
import entity.ServiceInfo;
import entity.frwk.SysUsers;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import vn.com.cmc.service.ValidateService;
import vn.com.cmc.utils.Constants;
import vn.com.cmc.utils.Utils;
import vn.org.intergration.ws.endpoint.cicqa.HoiTin;
import vn.org.intergration.ws.endpoint.cicqa.PHHoiTin;
import vn.org.intergration.ws.endpoint.cicqaprod.KhachHang;
import vn.org.intergration.ws.endpoint.cicqaprod.TTPhanHoi;

@Endpoint
public class CicQaEndpoint {
	private static final Logger logger = LogManager.getLogger(CicQaEndpoint.class);

	@Autowired
	ValidateService validateService;

	@Autowired
	MessageSource messageSource;

	@Autowired
	SysParamDao sysParamDao;

	@Autowired
	QnaInDetailDao qnaInDetailDao;

	@Autowired
	SysUsersDao sysUsersDao;

	@Autowired
	LogServicesDao logServicesDao;

	@Autowired
	ServiceInfoDao serviceInfoDao;

	private static final String NAMESPACE_URI = "http://www.endpoint.ws.intergration.org.vn/cicqa";

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "HoiTin")
	@ResponsePayload
	public PHHoiTin sendQuestion(@RequestPayload HoiTin request, MessageContext messageContext) throws JAXBException {
		logger.info("START: sendQuestion ");

		PHHoiTin response = new PHHoiTin();
		Date startDate = new Date();
		LogService logService = null;
		ServiceInfo serviceInfo = serviceInfoDao.findServiceByRequestClass(HoiTin.class.getName());
		String productCode = request.getKhachHang().getMaSP();
		try {
			// validate
			String error = null;
			// validate TTChung

			error = validateService.validateTTChung(request);
			if (!Utils.isNullObject(error)) {
				if (!Utils.isNullObject(error)) {
					response.setTTPhanHoi(buildTTPhanHoi(true, error, null));
					logService = new LogService();
					logServicesDao.saveLog(logService, Constants.LOG_STATUS_SUCCESS, Constants.LOG_MESSAGE_SUCCESS,
							Constants.ValidateCodeService.CMM_005, productCode, response, productCode, startDate,
							request, serviceInfo, Constants.LOG_STATUS_DEN, request.getKhachHang().getNguoiYeuCau());
					return response;
				}
			}
			error = validateService.vailidateCustomer(request.getKhachHang(), productCode);
			if (!Utils.isNullObject(error)) {
				if (!Utils.isNullObject(error)) {
					response.setTTPhanHoi(buildTTPhanHoi(true, error, null));
					logServicesDao.saveLog(logService, Constants.LOG_STATUS_SUCCESS,
							messageSource.getMessage(Constants.ValidateCodeService.CMM_005, null, null),
							Constants.ValidateCodeService.CMM_005, productCode, response, productCode, startDate,
							request, serviceInfo, Constants.LOG_STATUS_DEN, request.getKhachHang().getNguoiYeuCau());
					return response;
				}
			}
			// save customer question in DB
			QnaInDetail detail = new QnaInDetail();
			convertResquestToQnaInDetail(request.getKhachHang(), detail, productCode);
			TTPhanHoi phanHoi = buildTTPhanHoi(false, Constants.ValidateCodeService.STATUS_SUCCESS, null);
			response.setTTPhanHoi(phanHoi);
			SysUsers sysUsers = sysUsersDao.getByUserName(request.getKhachHang().getNguoiYeuCau());
			detail.setNguoihoi(sysUsers.getId());
			detail.setBranch(sysUsers.getDepartmentId());
			qnaInDetailDao.save(detail);
			if (detail != null) {
				if (!Formater.isNull(detail.getNoidungtraloi()))
					response.setDaTa(detail.getNoidungtraloi());
				else
					response.setDaTa("");
			}
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_SUCCESS,
//					response.getTTPhanHoi().getMoTa(), response.getTTPhanHoi().getMa(), response, null);
			logServicesDao.saveLog(logService, Constants.LOG_STATUS_SUCCESS,
					messageSource.getMessage(Constants.ValidateCodeService.CMM_000, null, null),
					Constants.ValidateCodeService.CMM_000, productCode, response, productCode, startDate, request,
					serviceInfo, Constants.LOG_STATUS_DEN, request.getKhachHang().getNguoiYeuCau());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.setTTPhanHoi(buildTTPhanHoi(true, Constants.ValidateCodeService.CMM_999, null));
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_ERROR, e.getMessage(),
//					Constants.ValidateCodeService.CMM_999, response, Utils.convertExceptionToString(e));
			try {
				logServicesDao.saveLog(logService, Constants.LOG_STATUS_ERROR,
						messageSource.getMessage(Constants.ValidateCodeService.CMM_999, null, null),
						Constants.ValidateCodeService.CMM_999, null, response, Utils.convertExceptionToString(e),
						startDate, request, serviceInfo, Constants.LOG_STATUS_DEN, request.getKhachHang().getNguoiYeuCau());
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				logger.error(e1.getMessage(), e1);
			}
			return response;
		}
		return response;
	}

	private void convertResquestToQnaInDetail(KhachHang request, QnaInDetail detail, String productCode) {
		// TODO Auto-generated method stub
		detail.setServiceProduct(new CatProduct(request.getMaSP()));
		detail.setDiachi(request.getDiaChi());
		detail.setDkkd(request.getDKKD());
		detail.setGhichu(request.getGhiChu());
		detail.setMacic(request.getMaCIC());
		detail.setMakh(request.getMaKH());
		detail.setMsphieu(productCode + Calendar.getInstance().getTimeInMillis());
		detail.setMsthue(request.getMSThue());
		detail.setNgaytao(new Date());
		detail.setSocmt(request.getSoCMT());
		detail.setTenkh(request.getTenKH());
	}

//	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "VanTinDSPhieu")
//	@ResponsePayload
//	public PHVanTinDSPhieu sendListAnswer(@RequestPayload VanTinDSPhieu request, MessageContext messageContext)
//			throws JAXBException {
//		logger.info("START: sendListAnswer ");
//		PHVanTinDSPhieu response = new PHVanTinDSPhieu();
//		LogService logService = null;
//		try {
//			// call service
//			response = (PHVanTinDSPhieu) soapConnector.callWebServiceAuthen(sysParamService.getUrlGatewayApp(), request, "df750d74-667d-45ee-a07c-d409fa4bac6d");
//			//verify checksum
//			if (response.getDSKhachHangKQ() != null && !checksum(response, "/PHVanTinDSPhieu/DSKhachHangKQ", response.getTTPhanHoi().getCheckSum())) {
//				//TODO check sum faile
//				response.getTTPhanHoi().setMa(Constants.ValidateCodeService.ERROR_CHECK_SUM);
//				response.getTTPhanHoi().setMoTa(messageSource.getMessage(Constants.ValidateCodeService.ERROR_CHECK_SUM, null, null));
//			}
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_SUCCESS,
//					response.getTTPhanHoi().getMoTa(), response.getTTPhanHoi().getMa(), response, null);
//		} catch (Exception e) {
//			logger.error(e.getMessage(), e);
//			response.setTTPhanHoi(
//					questionAnswerUtils.buildTTPhanHoi(true, Constants.ValidateCodeService.CMM_999, null));
//			questionAnswerUtils.updateLog(logService, null, Constants.LOG_STATUS_ERROR, e.getMessage(),
//					Constants.ValidateCodeService.CMM_999, response, Utils.convertExceptionToString(e));
//			return response;
//		}
//		return response;
//	}

	private boolean checksum(Object response, String expression, String strChecksum) throws Exception {
		String content = Utils.convertObjToXML(response, response.getClass());
		org.w3c.dom.Document document = Utils.loadXMLFromString(content);
		document.getDocumentElement().normalize();
		XPathFactory xPathfactory = XPathFactory.newInstance();
		XPath xpath = xPathfactory.newXPath();
		// String expression = "/PHVanTinDSPhieu/DSKhachHangKQ";
		NodeList nodeList = (NodeList) xpath.compile(expression).evaluate(document, XPathConstants.NODESET);
		String listKQ = Utils.nodeListToString(nodeList);
		if (!Utils.isNullObject(listKQ)) {
			String checksum = Utils.getSHA256Hash(Utils.nodeListToString(nodeList));
			if (!strChecksum.equals(checksum))
				return false;
		}
		return true;
	}

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
}
