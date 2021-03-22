package cic.ws.client;

import java.io.IOException;
import java.util.Arrays;

import javax.xml.bind.JAXBElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpComponentsConnection;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cic.ws.login.DangNhapResponse;
import cic.ws.model.PHTepBaoCao;
import cic.ws.model.PHTepBaoCaoVanTin;
import cic.ws.model.TepBaoCao;
import cic.ws.model.TepBaoCaoTinDung;
import cic.ws.model.TepBaoCaoVanTin;
import common.util.Formater;
import common.util.ResourceException;
import entity.SysJobConfig;
import entity.frwk.SysParam;
import frwk.constants.Constants;
import frwk.dao.hibernate.sys.SysParamDao;
import vn.org.cic.h2h.ws.endpoint.cicauthen.DangNhap;
import vn.org.cic.h2h.ws.endpoint.cicauthen.PHDangNhap;
import vn.org.cic.h2h.ws.endpoint.cicqa.HoiTin;
import vn.org.cic.h2h.ws.endpoint.cicqa.PHHoiTin;
import vn.org.cic.h2h.ws.endpoint.cicqa.PHTepHoiTin;
import vn.org.cic.h2h.ws.endpoint.cicqa.TepHoiTin;

public class WsClient extends WebServiceGatewaySupport {

	private static Logger log = LogManager.getLogger(WsClient.class);

	@Autowired
	private SysParamDao sysParamDao;
	@Value("${H2H_CORE_SVR_ADD}")
	// @Autowired
	// @Qualifier("H2H_CORE_SVR_ADD")
	private String h2hCoreSvrAdd;

	@Value("${H2H_GEATWAY_SVR_ADD}")
	private String h2hGatewaySvrAdd;

	@Value("${H2H_LOGIN_M6_ADD}")
	private String h2hLoginM6Add;

	public boolean loginM6(String username, String pwd) throws ResourceException {
		cic.ws.login.DangNhap dangNhap = new cic.ws.login.DangNhap();
		dangNhap.setArg0(username);
		dangNhap.setArg1(pwd);
		SysParam h2hLoginM6AddParam = sysParamDao.getSysParamByCode(h2hLoginM6Add);
		if (h2hLoginM6AddParam == null || Formater.isNull(h2hLoginM6AddParam.getValue()))
			throw new ResourceException("Chua nhap dia chi Host2Host Login");
		DangNhapResponse dangNhapResponse = null;
		try {
			String srvAddress = h2hLoginM6AddParam.getValue() + "/CICService/LoginService";
			try {
				JAXBElement<DangNhapResponse> jaxbElement = (JAXBElement<DangNhapResponse>) getWebServiceTemplate()
						.marshalSendAndReceive(srvAddress, dangNhap);
				dangNhapResponse = jaxbElement.getValue();
			} catch (Exception e) {
				// TODO: handle exception
				dangNhapResponse = (DangNhapResponse) getWebServiceTemplate()
						.marshalSendAndReceive(srvAddress, dangNhap);
			}
			Integer returnCode = dangNhapResponse.getReturn().getReturnCode();
			if (Constants.STATUS_LOGIN_M6_0 == returnCode) {
				return true;
			} else if (Constants.STATUS_LOGIN_M6_1 == returnCode) {
				// User hoặc pass trống
				throw new ResourceException(Constants.LOGIN_M6_1);
			} else if (Constants.STATUS_LOGIN_M6_2 == returnCode) {
				// Không đúng user hoặc pass
				throw new ResourceException(Constants.LOGIN_M6_2);
			} else {
				// loi he thong
				throw new ResourceException(Constants.LOGIN_M6);
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(), e);
			throw e;
		}
//		return true;
	}

	public PHTepBaoCao sendReport(TepBaoCao tepbaocao) throws ResourceException {
		SysParam H2H_CORE_SVR_ADD = sysParamDao.getSysParamByCode(h2hCoreSvrAdd);
		if (H2H_CORE_SVR_ADD == null || Formater.isNull(H2H_CORE_SVR_ADD.getValue()))
			throw new ResourceException("Chua nhap dia chi Host2Host core");
		String srvAddress = H2H_CORE_SVR_ADD.getValue() + "/CoreServiceApp/service/report";
		PHTepBaoCao rs = (PHTepBaoCao) getWebServiceTemplate().marshalSendAndReceive(srvAddress, tepbaocao);
		// throw new ResourceException(rs.getTTPhanHoi().getMa() + "::" +
		// rs.getTTPhanHoi().getMoTa());
		return rs;

	}

	public PHTepBaoCao sendReportK31K32(TepBaoCaoTinDung tepbaocao) throws ResourceException {
		System.out.print("start sendReportK31K32");
		SysParam H2H_CORE_SVR_ADD = sysParamDao.getSysParamByCode(h2hCoreSvrAdd);
		if (H2H_CORE_SVR_ADD == null || Formater.isNull(H2H_CORE_SVR_ADD.getValue()))
			throw new ResourceException("Chua nhap dia chi Host2Host core");
		String srvAddress = H2H_CORE_SVR_ADD.getValue() + "/CoreServiceApp/service/report";
		PHTepBaoCao rs = (PHTepBaoCao) getWebServiceTemplate().marshalSendAndReceive(srvAddress, tepbaocao);
		// throw new ResourceException(rs.getTTPhanHoi().getMa() + "::" +
		// rs.getTTPhanHoi().getMoTa());
		System.out.print("end sendReportK31K32");
		return rs;
	}

	public PHTepBaoCaoVanTin chkReport(TepBaoCaoVanTin tepbaocao) throws ResourceException {
		SysParam H2H_CORE_SVR_ADD = sysParamDao.getSysParamByCode(h2hCoreSvrAdd);
		if (H2H_CORE_SVR_ADD == null || Formater.isNull(H2H_CORE_SVR_ADD.getValue()))
			throw new ResourceException("Chua nhap dia chi Host2Host core");
		String srvAddress = H2H_CORE_SVR_ADD.getValue() + "/CoreServiceApp/service/report";
		PHTepBaoCaoVanTin rs = (PHTepBaoCaoVanTin) getWebServiceTemplate().marshalSendAndReceive(srvAddress, tepbaocao);
		// rs.getTepTraLoi().getTTChung().setMoTa("xxxx");
		return rs;

	}

	public PHDangNhap dangNhap(DangNhap dangNhap) throws ResourceException {
		SysParam H2H_GEATWAY_SVR_ADD = sysParamDao.getSysParamByCode(h2hGatewaySvrAdd);
		if (H2H_GEATWAY_SVR_ADD == null || Formater.isNull(H2H_GEATWAY_SVR_ADD.getValue()))
			throw new ResourceException("Chua nhap dia chi Host2Host geatway");
		String srvAddress = H2H_GEATWAY_SVR_ADD.getValue() + "/ServiceGatewayApp/service/authen";
		log.info("start call sang gateway: " + srvAddress);
		PHDangNhap rs = (PHDangNhap) getWebServiceTemplate().marshalSendAndReceive(srvAddress, dangNhap);
		return rs;

	}

	public PHTepHoiTin tepHoiTin(TepHoiTin tepHoiTin, String tokenGateWay) throws ResourceException {

		SysParam H2H_GEATWAY_SVR_ADD = sysParamDao.getSysParamByCode(h2hGatewaySvrAdd);
		if (H2H_GEATWAY_SVR_ADD == null || Formater.isNull(H2H_GEATWAY_SVR_ADD.getValue()))
			throw new ResourceException("Chua nhap dia chi Host2Host geatway");
		String srvAddress = H2H_GEATWAY_SVR_ADD.getValue() + "/ServiceGatewayApp/service/questionAnswer";
		log.info("start call sang gateway: " + srvAddress);
		PHTepHoiTin rs = (PHTepHoiTin) getWebServiceTemplate().marshalSendAndReceive(srvAddress, tepHoiTin,
				new WebServiceMessageCallback() {
					public void doWithMessage(WebServiceMessage message) throws IOException {
						TransportContext context = TransportContextHolder.getTransportContext();
						HttpComponentsConnection connection = (HttpComponentsConnection) context.getConnection();
						connection.addRequestHeader("Authorization", "Bearer " + tokenGateWay);
					}

				});
		log.info("end call sang gateway: ");
		return rs;

	}
	
	public PHHoiTin hoiTin(HoiTin hoiTin, String tokenGateWay) throws ResourceException {
		log.info("BEGIN call sang gateway: HoiTin ");
		SysParam H2H_GEATWAY_SVR_ADD = sysParamDao.getSysParamByCode(h2hGatewaySvrAdd);
		if (H2H_GEATWAY_SVR_ADD == null || Formater.isNull(H2H_GEATWAY_SVR_ADD.getValue()))
			throw new ResourceException("Chua nhap dia chi Host2Host geatway");
		String srvAddress = H2H_GEATWAY_SVR_ADD.getValue() + "/ServiceGatewayApp/service/questionAnswer";
		log.info("start call sang gateway: " + srvAddress);
		PHHoiTin rs = (PHHoiTin) getWebServiceTemplate().marshalSendAndReceive(srvAddress, hoiTin,
				new WebServiceMessageCallback() {
					public void doWithMessage(WebServiceMessage message) throws IOException {
						TransportContext context = TransportContextHolder.getTransportContext();
						HttpComponentsConnection connection = (HttpComponentsConnection) context.getConnection();
						connection.addRequestHeader("Authorization", "Bearer " + tokenGateWay);
					}

				});
		log.info("END call sang gateway: HoiTin ");
		return rs;

	}

	public void resetJob(SysJobConfig sysJobConfig) throws Exception {
		SysParam H2H_CORE_SVR_ADD = sysParamDao.getSysParamByCode(h2hCoreSvrAdd);
		if (H2H_CORE_SVR_ADD == null || Formater.isNull(H2H_CORE_SVR_ADD.getValue()))
			throw new ResourceException("Chua nhap dia chi Host2Host core");
		String srvAddress = H2H_CORE_SVR_ADD.getValue() + "/CoreServiceApp/sysJobConfig/resetJob";
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON_UTF8));
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
			Gson gson = new Gson();
			JsonObject jsonObject = new JsonParser().parse(gson.toJson(sysJobConfig)).getAsJsonObject();
			jsonObject.addProperty("active", sysJobConfig.getActive() ? 1 : 0);
			HttpEntity<String> request;
			request = new HttpEntity<String>(gson.toJson(jsonObject), headers);
			restTemplate.exchange(srvAddress, HttpMethod.POST, request, String.class);
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}

}
