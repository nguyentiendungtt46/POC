package vn.com.cmc.ws.endpoint;

import java.io.StringWriter;
import java.util.Date;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.google.gson.Gson;

import cic.h2h.dao.hibernate.LogServicesDao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import entity.LogService;
import entity.ServiceInfo;
import frwk.dao.hibernate.sys.SysParamDao;
import intergration.cic.Authen;
import vn.com.cmc.service.ValidateService;
import vn.com.cmc.utils.Constants;
import vn.com.cmc.utils.Utils;
import vn.com.cmc.ws.config.SOAPConnector;
import vn.org.intergration.ws.endpoint.cicstatisticalreport.BaoCaoThongKe;
import vn.org.intergration.ws.endpoint.cicstatisticalreport.PHBaoCaoThongKe;
import vn.org.intergration.ws.endpoint.cicstatisticalreport.TTPhanHoi;

@Endpoint
public class CicStatisticalReportEndpoint {
	private static final Logger logger = LogManager.getLogger(CicStatisticalReportEndpoint.class);

	@Autowired
	private SOAPConnector soapConnector;

	@Autowired
	ValidateService validateService;

	@Autowired
	MessageSource messageSource;

	@Autowired
	SysParamDao sysParamDao;

	@Value("${url.gateway.app}")
	private String urlGateWayApp;

	@Autowired
	Authen authen;

	@Autowired
	LogServicesDao logServicesDao;

	@Autowired
	ServiceInfoDao serviceInfoDao;

	private static final String NAMESPACE_URI = "http://www.endpoint.ws.intergration.org.vn/cicstatisticalreport";

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "BaoCaoThongKe")
	@ResponsePayload
	public PHBaoCaoThongKe statisticalReport(@RequestPayload BaoCaoThongKe request, MessageContext messageContext)
			throws JAXBException {
		logger.info("START: statisticalReport ");
		PHBaoCaoThongKe response = new PHBaoCaoThongKe();
		LogService logService = null;
		Date startDate = new Date();
		ServiceInfo serviceInfo = serviceInfoDao.findServiceByRequestClass(BaoCaoThongKe.class.getName());
		try {
			// call service
			if (!validateService.sysUsers(request.getNguoiYeuCau())) {
				response.setTTPhanHoi(buildTTPhanHoi(true, Constants.ValidateCodeService.CIC_002, null));
				return response;
			}
			authen.checkToken();
			StringWriter sw = new StringWriter();
			JAXB.marshal(request, sw);
			String xmlData2 = sw.toString();
			logger.info("requestCore: " + xmlData2);
			sw.close();
			vn.org.cic.h2h.ws.endpoint.cicstatisticalreport.BaoCaoThongKe rqCore = new vn.org.cic.h2h.ws.endpoint.cicstatisticalreport.BaoCaoThongKe();
			rqCore.setMaBaoCao(request.getMaBaoCao());
			rqCore.setTuNgay(request.getTuNgay());
			rqCore.setDenNgay(request.getDenNgay());
			vn.org.cic.h2h.ws.endpoint.cicstatisticalreport.PHBaoCaoThongKe resCore = (vn.org.cic.h2h.ws.endpoint.cicstatisticalreport.PHBaoCaoThongKe) soapConnector
					.callWebServiceAuthen(sysParamDao.getSysParamByCode(urlGateWayApp).getValue(), rqCore,
							authen.getPhDangNHap().getToken());
			String jsonRs = new Gson().toJson(resCore);
			response = new Gson().fromJson(jsonRs, PHBaoCaoThongKe.class);
			// end log
			logServicesDao.saveLog(logService, Constants.LOG_STATUS_SUCCESS,
					messageSource.getMessage(Constants.ValidateCodeService.CMM_000, null, null),
					Constants.ValidateCodeService.CMM_000, null, response, null, startDate, request, serviceInfo,
					Constants.LOG_STATUS_DEN, request.getNguoiYeuCau());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
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

	// build thong tin phan hoi
	private TTPhanHoi buildTTPhanHoi(Boolean isError, String code, Object[] param) {
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
