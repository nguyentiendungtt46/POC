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
import vn.org.cic.h2h.ws.endpoint.cicreport.PHTepBaoCao;
import vn.org.cic.h2h.ws.endpoint.cicreport.TepBaoCao;

@Service
public class Report {
	private static Logger log = LogManager.getLogger(Report.class);
	
	@Value("${H2H_GEATWAY_SVR_ADD}")
	private String h2hGatewaySvrAdd;
	
	@Autowired
	private SysParamDao sysParamDao;
	
	@Autowired
	Authen cicAuthen;
	
	@Autowired
	SOAPConnector soapConnector;
	public PHTepBaoCao tepBaoCao(TepBaoCao obj) throws Exception {
		cicAuthen.checkToken();
		log.info("BEGIN call sang CIC: TepBaoCao ");
		SysParam H2H_GEATWAY_SVR_ADD = sysParamDao.getSysParamByCode(h2hGatewaySvrAdd);
		if (H2H_GEATWAY_SVR_ADD == null || Formater.isNull(H2H_GEATWAY_SVR_ADD.getValue()))
			throw new ResourceException("Chua nhap dia chi Host2Host geatway");
		String srvAddress = H2H_GEATWAY_SVR_ADD.getValue() + "/ServiceGatewayApp/service/questionAnswer";
		log.info("start call sang gateway: " + srvAddress);
		PHTepBaoCao rs = (PHTepBaoCao) soapConnector.callWebServiceAuthen(srvAddress, obj, cicAuthen.getPhDangNHap().getToken());
		log.info("END call sang CIC: TepBaoCao ");
		return rs;

	}
}
