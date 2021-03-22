package intergration.cic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.SysParam;
import frwk.dao.hibernate.sys.SysParamDao;
import vn.com.cmc.ws.config.SOAPConnector;
import vn.org.cic.h2h.ws.endpoint.cicqa.HoiTin;
import vn.org.cic.h2h.ws.endpoint.cicqa.PHHoiTin;
import vn.org.cic.h2h.ws.endpoint.cicqa.PHVanTinDSPhieu;
import vn.org.cic.h2h.ws.endpoint.cicqa.VanTinDSPhieu;

@Service
public class QuestionAnswer {
	private static Logger log = LogManager.getLogger(QuestionAnswer.class);
	@Autowired
	private SysParamDao sysParamDao;

	@Value("${H2H_GEATWAY_SVR_ADD}")
	private String h2hGatewaySvrAdd;

	@Autowired
	Authen cicAuthen;
	
	@Autowired
	SOAPConnector soapConnector;
	public PHHoiTin hoiTin(HoiTin hoiTin) throws Exception {
		cicAuthen.checkToken();
		log.info("BEGIN call sang gateway: HoiTin ");
		SysParam H2H_GEATWAY_SVR_ADD = sysParamDao.getSysParamByCode(h2hGatewaySvrAdd);
		if (H2H_GEATWAY_SVR_ADD == null || Formater.isNull(H2H_GEATWAY_SVR_ADD.getValue()))
			throw new ResourceException("Chua nhap dia chi Host2Host geatway");
		String srvAddress = H2H_GEATWAY_SVR_ADD.getValue() + "/ServiceGatewayApp/service/questionAnswer";
		log.info("start call sang gateway: " + srvAddress);
		PHHoiTin rs = (PHHoiTin) soapConnector.callWebServiceAuthen(srvAddress, hoiTin, cicAuthen.getPhDangNHap().getToken());
		log.info("END call sang gateway: HoiTin ");
		return rs;

	}
	
	public PHVanTinDSPhieu vantinDsPhieu(VanTinDSPhieu obj) throws Exception {
		cicAuthen.checkToken();
		log.info("BEGIN call sang gateway: VanTinDSPhieu ");
		SysParam H2H_GEATWAY_SVR_ADD = sysParamDao.getSysParamByCode(h2hGatewaySvrAdd);
		if (H2H_GEATWAY_SVR_ADD == null || Formater.isNull(H2H_GEATWAY_SVR_ADD.getValue()))
			throw new ResourceException("Chua nhap dia chi Host2Host geatway");
		String srvAddress = H2H_GEATWAY_SVR_ADD.getValue() + "/ServiceGatewayApp/service/questionAnswer";
		log.info("start call sang gateway: " + srvAddress);
		PHVanTinDSPhieu rs = (PHVanTinDSPhieu) soapConnector.callWebServiceAuthen(srvAddress, obj, cicAuthen.getPhDangNHap().getToken());
		log.info("END call sang gateway: VanTinDSPhieu ");
		return rs;

	}
	
}
