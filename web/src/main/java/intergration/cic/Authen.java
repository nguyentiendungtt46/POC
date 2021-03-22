package intergration.cic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import common.util.AESUtils;
import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.SysParam;
import frwk.dao.hibernate.sys.SysParamDao;
import vn.com.cmc.ws.config.SOAPConnector;
import vn.org.cic.h2h.ws.endpoint.cicauthen.DangNhap;
import vn.org.cic.h2h.ws.endpoint.cicauthen.PHDangNhap;
import vn.org.cic.h2h.ws.endpoint.cicuserinfo.DoiMatKhau;
import vn.org.cic.h2h.ws.endpoint.cicuserinfo.PHDoiMatKhau;

@Service
public class Authen {
	private PHDangNhap phDangNHap;
	private Date thoiDiemDangNhap;
	private static Logger log = LogManager.getLogger(Authen.class);
	private static final AESUtils encode = new AESUtils();
	@Autowired
	private SysParamDao sysParamDao;

	@Value("${H2H_GEATWAY_SVR_ADD}")
	private String h2hGatewaySvrAdd;
	@Value("${H2H_USERNAME}")
	private String h2hUsername;
	@Value("${H2H_PASSWORD}")
	private String h2hPassword;
	@Autowired
	SOAPConnector soapConnector;

	public void dangNhap() throws Exception {
		try {
			SysParam H2H_GEATWAY_SVR_ADD = sysParamDao.getSysParamByCode(h2hGatewaySvrAdd);
			if (H2H_GEATWAY_SVR_ADD == null || Formater.isNull(H2H_GEATWAY_SVR_ADD.getValue()))
				throw new ResourceException("Chua nhap dia chi Host2Host geatway");
			String srvAddress = H2H_GEATWAY_SVR_ADD.getValue() + "/ServiceGatewayApp/service/authen";
			log.info("start call sang gateway: " + srvAddress);
			SysParam username = sysParamDao.getSysParamByCode(h2hUsername);
			SysParam password = sysParamDao.getSysParamByCode(h2hPassword);
			DangNhap _inlg = new DangNhap();
			_inlg.setTenDangNhap(username.getValue());
			_inlg.setMatKhau(encode.decrypt(password.getValue()));
			//System.out.println(encode.decrypt(password.getValue()));
			// phDangNHap = (PHDangNhap)
			// getWebServiceTemplate().marshalSendAndReceive(srvAddress, _inlg);
			phDangNHap = (PHDangNhap) soapConnector.callWebService(srvAddress, _inlg);
			thoiDiemDangNhap = Calendar.getInstance().getTime();
		} catch (Exception e) {
			phDangNHap = null;
			throw e;
		}

	}
	
	public PHDangNhap dangNhap(DangNhap obj) throws Exception {
		PHDangNhap response = new PHDangNhap();
		try {
			SysParam H2H_GEATWAY_SVR_ADD = sysParamDao.getSysParamByCode(h2hGatewaySvrAdd);
			if (H2H_GEATWAY_SVR_ADD == null || Formater.isNull(H2H_GEATWAY_SVR_ADD.getValue()))
				throw new ResourceException("Chua nhap dia chi Host2Host geatway");
			String srvAddress = H2H_GEATWAY_SVR_ADD.getValue() + "/ServiceGatewayApp/service/authen";
			log.info("start call sang gateway: " + srvAddress);
			DangNhap _inlg = new DangNhap();
			_inlg.setTenDangNhap(obj.getTenDangNhap());
			_inlg.setMatKhau(obj.getMatKhau());
			response = (PHDangNhap) soapConnector.callWebService(srvAddress, _inlg);
		} catch (Exception e) {
			throw e;
		}
		return response;
	}

	public void checkToken() throws Exception {
		if (getPhDangNHap() == null)
			dangNhap();
		// chk token con hieu luc
		if (!validateToken())
			dangNhap();
		if (phDangNHap == null || Formater.isNull(phDangNHap.getToken()))
			throw new Exception("Chua dang nhap duoc CIC");
	}

	public boolean validateToken() throws ParseException {
		if(phDangNHap.getTokenHetHan() == null) {
			return false;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(thoiDiemDangNhap);
		c.add(Calendar.SECOND, Integer.valueOf(phDangNHap.getTokenHetHan().toString()));
		return c.getTime().after(Calendar.getInstance().getTime());
	}

	public PHDoiMatKhau doiMatKhau(DoiMatKhau obj) throws Exception {
		// lan dau call Null
		if (getPhDangNHap() == null)
			dangNhap();
		// chk token con hieu luc
		if (!validateToken())
			dangNhap();
		log.info("BEGIN call sang CIC: DoiMatKhau ");
		SysParam H2H_GEATWAY_SVR_ADD = sysParamDao.getSysParamByCode(h2hGatewaySvrAdd);
		if (H2H_GEATWAY_SVR_ADD == null || Formater.isNull(H2H_GEATWAY_SVR_ADD.getValue()))
			throw new ResourceException("Chua nhap dia chi Host2Host geatway");
		String srvAddress = H2H_GEATWAY_SVR_ADD.getValue() + "/ServiceGatewayApp/service/userinfo";
		log.info("start call sang gateway: " + srvAddress);
		PHDoiMatKhau rs = (PHDoiMatKhau) soapConnector.callWebServiceAuthen(srvAddress, obj,
				getPhDangNHap().getToken());
		log.info("END call sang CIC: DoiMatKhau ");
		return rs;
	}

	public PHDangNhap getPhDangNHap() {
		return phDangNHap;
	}

	public void setPhDangNHap(PHDangNhap phDangNHap) {
		this.phDangNHap = phDangNHap;
	}

}
