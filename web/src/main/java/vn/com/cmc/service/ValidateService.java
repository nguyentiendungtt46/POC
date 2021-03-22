package vn.com.cmc.service;

import javax.servlet.http.HttpServletRequest;

import vn.com.cmc.entity.ServiceInfo;
import vn.com.cmc.entity.SysUsers;
import vn.org.intergration.ws.endpoint.cicqa.HoiTin;
import vn.org.intergration.ws.endpoint.cicqa.TepHoiTin;
import vn.org.intergration.ws.endpoint.cicqr.PHTimKiemKH;
import vn.org.intergration.ws.endpoint.cicqr.PHVanTinChung;
import vn.org.intergration.ws.endpoint.cicqr.TimKiemKH;
import vn.org.intergration.ws.endpoint.cicqr.VanTinChung;

public interface ValidateService {

	String validateVanTinChung(String code);

	String validateTTChung(HoiTin request);

	String validateQuestionFile(TepHoiTin request);

	
	String vailidateCustomer(vn.org.intergration.ws.endpoint.cicqaprod.KhachHang request, String loaiSp);
	
	boolean sysUsers(String username);
	
	PHTimKiemKH sendSearchCus(TimKiemKH request, HttpServletRequest rq);
	
	PHVanTinChung inquiryS37(VanTinChung request, HttpServletRequest rq) throws Exception;

}
