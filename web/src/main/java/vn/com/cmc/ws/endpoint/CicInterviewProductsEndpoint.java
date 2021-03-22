package vn.com.cmc.ws.endpoint;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import cic.h2h.dao.hibernate.LogServicesDao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import entity.LogService;
import entity.ServiceInfo;
import vn.com.cmc.service.ValidateService;
import vn.com.cmc.utils.Constants;
import vn.com.cmc.utils.Utils;
import vn.org.intergration.ws.endpoint.cicqr.PHVanTinChung;
import vn.org.intergration.ws.endpoint.cicqr.VanTinChung;

@Endpoint
public class CicInterviewProductsEndpoint {
	private static final Logger logger = LogManager.getLogger(CicInterviewProductsEndpoint.class);

	private static final String NAMESPACE_URI = "http://www.endpoint.ws.intergration.org.vn/cicqr";

	@Autowired
	ValidateService validateService;

	@Autowired
	MessageSource messageSource;

	@Autowired
	HttpServletRequest httpRequest;
	
	@Autowired
	LogServicesDao logServicesDao;
	
	@Autowired
	ServiceInfoDao serviceInfoDao;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "VanTinChung")
	@ResponsePayload
	public PHVanTinChung interViewProducts(@RequestPayload VanTinChung request, MessageContext messageContext)
			throws Exception {
		return validateService.inquiryS37(request, null);
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
