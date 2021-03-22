package vn.com.cmc.controller;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.com.cmc.utils.Utils;
import vn.com.cmc.ws.config.SOAPConnector;
import vn.org.cic.h2h.ws.endpoint.cicauthen.DangNhap;
import vn.org.cic.h2h.ws.endpoint.cicauthen.PHDangNhap;
import vn.org.cic.h2h.ws.endpoint.cicqa.HoiTin;
import vn.org.cic.h2h.ws.endpoint.cicqa.PHHoiTin;

@RestController
public class TestController {
	
	@Autowired
	SOAPConnector soapConnector;
	
	@Autowired
	HttpServletRequest httpRequest;
	
	@RequestMapping(value="test/{token}/{numTest}", method=RequestMethod.GET, produces=MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<String> getCaseMemberInfo(@PathVariable("token") String token,
			@PathVariable("numTest") Integer numTest) throws JAXBException {
		if (Utils.isNullObject(token) || Utils.isNullObject(numTest)) {
			return new ResponseEntity<String>("Bắt buộc nhập token và numTest", HttpStatus.OK);
		}
		
		String address = httpRequest.getScheme() + "://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort();
		String result = null;
//		double msp = getRandomDoubleBetweenRange(1, 9999);
//		DecimalFormat df = new DecimalFormat("0000"); 
//		String strMSP = df.format(msp); 
//		double cmt = getRandomDoubleBetweenRange(1, 99999);
//		DecimalFormat df2 = new DecimalFormat("00000"); 
//		String strCMT = df2.format(cmt); 
//		if (!Utils.isNullObject(numTest)) {
//			
//			HoiTin hoiTin = new HoiTin();
//			// Thong tin chung
//			vn.org.cic.h2h.ws.endpoint.cicqaprod.TTChung ttChung = new vn.org.cic.h2h.ws.endpoint.cicqaprod.TTChung();
//			ttChung.setLoaiSP("06");
//			ttChung.setSLPhieu(BigInteger.valueOf(numTest));
//			hoiTin.setTTChung(ttChung);
//			
//			vn.org.cic.h2h.ws.endpoint.cicqaprod.DSKhachHang dsKhachHang = new vn.org.cic.h2h.ws.endpoint.cicqaprod.DSKhachHang();
//			for (int i = 0; i < numTest; i++) {
//				String code = df.format(i); 
//				// Danh sach khach hang
//				vn.org.cic.h2h.ws.endpoint.cicqaprod.Dong khachHang = new vn.org.cic.h2h.ws.endpoint.cicqaprod.Dong();
//				khachHang.setMaSoPhieu(strMSP + code);
//				khachHang.setTenKH("TEST PERFORMANCE");
//				khachHang.setSoCMT(strCMT + code);
//				dsKhachHang.getDong().add(khachHang);
//			}
//			hoiTin.setDSKhachHang(dsKhachHang);
//			// call service
//			PHHoiTin pHHoiTin = new PHHoiTin();//(PHHoiTin) soapConnector.callWebServiceAuthen( address + "/ServiceGatewayApp/service/questionAnswer", hoiTin, token);
//			result = Utils.convertObjToXML(pHHoiTin, pHHoiTin.getClass());
//		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
	
	protected HttpHeaders getHeaders(String accessToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("Authorization", "Bearer " + accessToken);
		return headers;
	}
	

	private static double getRandomDoubleBetweenRange(double min, double max){
	    double x = (Math.random()*((max-min)+1))+min;
	    return x;
	}
}
