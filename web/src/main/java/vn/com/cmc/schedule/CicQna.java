package vn.com.cmc.schedule;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import cic.h2h.dao.hibernate.CatProductDao;
import cic.h2h.dao.hibernate.LogServicesDao;
import cic.h2h.dao.hibernate.QnaInDetailDao;
import cic.h2h.dao.hibernate.QnaInDetailErDao;
import cic.h2h.dao.hibernate.QnaInMasterDao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import common.util.Formater;
import entity.CatProduct;
import entity.LogService;
import entity.QnaInDetail;
import entity.QnaInDetailEr;
import entity.QnaInMaster;
import entity.ServiceInfo;
import intergration.cic.QuestionAnswer;
import vn.com.cmc.utils.Constants;
import vn.com.cmc.utils.Utils;
import vn.org.cic.h2h.ws.endpoint.cicqa.HoiTin;
import vn.org.cic.h2h.ws.endpoint.cicqa.PHHoiTin;
import vn.org.cic.h2h.ws.endpoint.cicqa.PHVanTinDSPhieu;
import vn.org.cic.h2h.ws.endpoint.cicqa.VanTinDSPhieu;
import vn.org.cic.h2h.ws.endpoint.cicqaprod.DSKhachHang;
import vn.org.cic.h2h.ws.endpoint.cicqaprod.DSMaSoPhieu;
import vn.org.cic.h2h.ws.endpoint.cicqaprod.Dong;
import vn.org.cic.h2h.ws.endpoint.cicqaprod.DongKQ;
import vn.org.cic.h2h.ws.endpoint.cicqaprod.Loi;
import vn.org.cic.h2h.ws.endpoint.cicqaprod.TTChung;

@Service("cicQna")
public class CicQna {
	static Logger lg = LogManager.getLogger(QnaInDetailDao.class);
	@Autowired
	private CatProductDao catProductDao;
	@Autowired
	private QnaInMasterDao qnaInMasterDao;
	@Autowired
	private QnaInDetailDao qnaInDetailDao;
	@Autowired
	private QnaInDetailErDao qnaInDetailErDao;

//	public Runnable makeExeThead(String maSanPham) {
//		return new QnARunnable(maSanPham);
//	}
//
//	private class QnARunnable implements Runnable {
//		String maSanPham;
//
//		public QnARunnable(String maSanPham) {
//			this.maSanPham = maSanPham;
//		}
//
//		@Override
//		public void run() {
//			lg.info("Start send to CIC, san pham: " + maSanPham);
//			try {
//				getDataJobHoiTin(this.maSanPham);
//				qnaInMasterDao.getCurrentSession().getTransaction().commit();
//			} catch (Exception e) {
//				qnaInMasterDao.getCurrentSession().getTransaction().rollback();
//				lg.error(e.getMessage(), e);
//			} finally {
//				qnaInMasterDao.getCurrentSession().close();
//			}
//			lg.info("End send to CIC, san pham: " + maSanPham);
//		}
//
//	}
//
//	@Value("${QNA_SERVICE}")
//	private String qnaSvCode;
//
//	public void getDataJobHoiTin(String maSanPham) throws Exception {
//		List<QnaInMaster> msToSend = qnaInDetailDao.getSentErrMs(maSanPham);
//		CatProduct catProduct = catProductDao.getCatProductByCodeAndSvCode(maSanPham, qnaSvCode);
//		List<QnaInDetail> lstQnaInDetailsNotSend = qnaInDetailDao.getNotSentQnaByProduct(catProduct.getId());
//		if (!Formater.isNull(lstQnaInDetailsNotSend)) {
//			QnaInMaster qnaInMaster = new QnaInMaster();
//			qnaInMaster.setLoaisp(maSanPham);
//			qnaInMaster.setThoigianyc(Calendar.getInstance().getTime());
//			qnaInMaster.setStatus(Short.valueOf("1"));
//			qnaInMaster.setTongSoPhieu(Long.valueOf(lstQnaInDetailsNotSend.size()));
//			qnaInMaster.getQnaInDetails().addAll(lstQnaInDetailsNotSend);
//			qnaInMasterDao.save(qnaInMaster);
//			for (QnaInDetail detail : qnaInMaster.getQnaInDetails())
//				detail.setQnaInMsId(qnaInMaster);
//
//			msToSend.add(qnaInMaster);
//		}
//		getDataJobHoiTin(msToSend);
//	}
//
//	@Autowired
//	private QuestionAnswer questionAnswer;
//
//	@Autowired
//	private LogServicesDao logServicesDao;
//
//	@Autowired
//	private ServiceInfoDao serviceInfoDao;
//
//	public void getDataJobHoiTin(List<QnaInMaster> msToSend) throws Exception {
//		if (msToSend.isEmpty())
//			return;
//
//		Gson gson = new Gson();
//		// build object HoiTin
//		HoiTin obj = new HoiTin();
//		// build object TTChung
//		TTChung ttChung = new TTChung();
//		ttChung.setLoaiSP(msToSend.get(0).getLoaisp());
//		ttChung.setSLPhieu(BigInteger.valueOf(0));
//		// build object DSKhachHang
//		DSKhachHang dsKhachHang = new DSKhachHang();
//		for (QnaInMaster ms : msToSend) {
//			ttChung.setSLPhieu(ttChung.getSLPhieu().add(BigInteger.valueOf(ms.getQnaInDetails().size())));
//			for (QnaInDetail qnaInDetail : ms.getQnaInDetails()) {
//				Dong dong = new Dong();
//				dong.setMaSoPhieu(qnaInDetail.getMsphieu());
//				dong.setMaKH(qnaInDetail.getMakh());
//				dong.setTenKH(qnaInDetail.getTenkh());
//				dong.setMaCIC(qnaInDetail.getMacic());
//				dong.setSoCMT(qnaInDetail.getSocmt());
//				dong.setDiaChi(qnaInDetail.getDiachi());
//				dsKhachHang.getDong().add(dong);
//				qnaInDetail.setThoiDiemGuiVaoCoreCIC(new Date());
//			}
//		}
//
//		obj.setTTChung(ttChung);
//		obj.setDSKhachHang(dsKhachHang);
//		// call service cic
//		PHHoiTin response = null;
//		ServiceInfo serviceInfo = serviceInfoDao
//				.findServiceByRequestClass(vn.org.intergration.ws.endpoint.cicqa.HoiTin.class.getName());
//		Date startDate = new Date();
//		LogService logService = new LogService();
//		try {
//			response = questionAnswer.hoiTin(obj);
//			logServicesDao.saveLog(logService, Constants.LOG_STATUS_SUCCESS,
//					messageSource.getMessage(Constants.ValidateCodeService.CMM_000, null, null),
//					Constants.ValidateCodeService.CMM_000, msToSend.get(0).getLoaisp(), response, null, startDate, obj,
//					serviceInfo, Constants.LOG_STATUS_DI, "job_rq");
//		} catch (Exception e) {
//			lg.error(e.getMessage(), e);
//			for (QnaInMaster qnaInMaster : msToSend) {
//				qnaInMaster.setMaLoi("CMM_999");
//				qnaInMaster.setMoTaLoi("Lỗi call service tích hợp CIC");
//				if (Utils.isNullObject(qnaInMaster.getRetryTime()))
//					qnaInMaster.setRetryTime(Short.valueOf("0"));
//				else
//					qnaInMaster.setRetryTime((short) (qnaInMaster.getRetryTime() + Short.valueOf("1")));
//				qnaInMasterDao.save(qnaInMaster);
//			}
//			logServicesDao.saveLog(logService, Constants.LOG_STATUS_ERROR,
//					messageSource.getMessage(Constants.ValidateCodeService.CMM_999, null, null),
//					Constants.ValidateCodeService.CMM_999, msToSend.get(0).getLoaisp(), response,
//					Utils.convertExceptionToString(e), startDate, obj, serviceInfo, Constants.LOG_STATUS_DI,"job_rq");
//		}
//
//		if (Utils.isNullObject(response))
//			return;
//
//		// xu ly data service cic tra ve
//		if (response.getTTPhanHoi().getMa().equalsIgnoreCase("CMM_999")) {
//			for (QnaInMaster qnaInMaster : msToSend) {
//				qnaInMaster.setMaLoi("CMM_999");
//				qnaInMaster.setMoTaLoi("Lỗi call service tích hợp CIC");
//				if (Utils.isNullObject(qnaInMaster.getRetryTime()))
//					qnaInMaster.setRetryTime(Short.valueOf("0"));
//				else
//					qnaInMaster.setRetryTime((short) (qnaInMaster.getRetryTime() + Short.valueOf("1")));
//				qnaInMasterDao.save(qnaInMaster);
//			}
//
//		} else if (response.getTTPhanHoi().getMa().equalsIgnoreCase("CMM_005")) {
//			if (!Utils.isNullObject(response.getDSKhachHangKQ())
//					&& !response.getDSKhachHangKQ().getDongKQ().isEmpty()) {
//				Map<String, QnaInMaster> processMs = new HashMap<String, QnaInMaster>();
//				for (DongKQ dongKQ : response.getDSKhachHangKQ().getDongKQ()) {
//					if (!dongKQ.getDSLoi().getLoi().isEmpty()) {
//						QnaInDetail qna = qnaInDetailDao.getQnaByMsphieu(dongKQ.getMaSoPhieu());
//						if (processMs.get(qna.getQnaInMsId().getId()) == null) {
//							qna.getQnaInMsId().setNumOfErrorCus(0l);
//							qna.getQnaInMsId().setMaLoi("CMM_005");
//							qna.getQnaInMsId().setMoTaLoi(response.getTTPhanHoi().getMoTa());
//							processMs.put(qna.getQnaInMsId().getId(), qna.getQnaInMsId());
//						}
//						QnaInMaster ms = processMs.get(qna.getQnaInMsId().getId());
//						for (Loi loi : dongKQ.getDSLoi().getLoi()) {
//							QnaInDetailEr er = new QnaInDetailEr();
//							er.setMaloi(loi.getMaLoi());
//							er.setMotaloi(loi.getMoTaLoi());
//							qna.setStatus(Short.valueOf("4"));
//							qna.setError(true);
//							qna.setNoidungtraloi(gson.toJson(dongKQ));
//							er.setQnaInDetailId(qna);
//							qnaInDetailErDao.save(er);
//						}
//						Long numOfError = ms.getNumOfErrorCus();
//						numOfError++;
//						ms.setNumOfErrorCus(numOfError);
//						qnaInMasterDao.save(qna.getQnaInMsId());
//					}
//				}
//			}
//		}
//		StringWriter sw = new StringWriter();
//		JAXB.marshal(response, sw);
//		String xmlString01 = sw.toString();
//		xmlString01 = StringEscapeUtils.escapeHtml(xmlString01);
//		lg.info(xmlString01);
//		sw.close();
//	}
//
//	public Runnable makeQnaAnswExeThead() {
//		return new QnaAnswRunnable();
//	}
//
//	private class QnaAnswRunnable implements Runnable {
//		public void run() {
//			lg.info("Start get qna answer from CIC");
//			try {
//				getDataJobVanTinDsPhieu();
//				qnaInMasterDao.getCurrentSession().getTransaction().commit();
//			} catch (Exception e) {
//				qnaInMasterDao.getCurrentSession().getTransaction().rollback();
//				lg.error(e.getMessage(), e);
//			} finally {
//				qnaInMasterDao.getCurrentSession().close();
//			}
//			lg.info("End get qna answer from CIC");
//		}
//
//	}
//
//	@Autowired
//	MessageSource messageSource;
//
//	public void getDataJobVanTinDsPhieu() throws Exception {
//		List<String> lstMasophieu = qnaInDetailDao.getMspJobVanTinDsPhieu();
//		if (Formater.isNull(lstMasophieu)) {
//			lg.info("Khong co Masophieu de van tin");
//			return;
//		}
//		VanTinDSPhieu vantinDsPhieu = new VanTinDSPhieu();
//		DSMaSoPhieu dsMaSoPhieu = new DSMaSoPhieu();
//		dsMaSoPhieu.getMaSoPhieu().addAll(lstMasophieu);
//		vantinDsPhieu.setDSMaSoPhieu(dsMaSoPhieu);
//		// call service cic
//		PHVanTinDSPhieu response = null;
//		ServiceInfo serviceInfo = serviceInfoDao.findServiceByRequestClass(VanTinDSPhieu.class.getName());
//		Date startDate = new Date();
//		LogService logService = new LogService();
//		try {
//			response = questionAnswer.vantinDsPhieu(vantinDsPhieu);
//			logServicesDao.saveLog(logService, Constants.LOG_STATUS_SUCCESS,
//					messageSource.getMessage(Constants.ValidateCodeService.CMM_005, null, null),
//					Constants.ValidateCodeService.CMM_000, null, response, null, startDate, vantinDsPhieu, serviceInfo,
//					Constants.LOG_STATUS_DI, "job_rq");
//		} catch (Exception e) {
//			logServicesDao.saveLog(logService, Constants.LOG_STATUS_ERROR,
//					messageSource.getMessage(Constants.ValidateCodeService.CMM_999, null, null),
//					Constants.ValidateCodeService.CMM_999, null, response, Utils.convertExceptionToString(e), startDate,
//					vantinDsPhieu, serviceInfo, Constants.LOG_STATUS_DI, "job_rq");
//		}
//
//		StringWriter sw = new StringWriter();
//		JAXB.marshal(response, sw);
//		String xmlString01 = sw.toString();
//		sw.close();
//		xmlString01 = StringEscapeUtils.escapeHtml(xmlString01);
//		lg.info(xmlString01);
//		// xu ly output service tra ve
//		Gson gson = new Gson();
//		if (response.getDSKhachHangKQ() != null) {
//			if (response.getDSKhachHangKQ().getDongKQ() != null) {
//				for (DongKQ _dong : response.getDSKhachHangKQ().getDongKQ()) {
//					QnaInDetail qna = qnaInDetailDao.getQnaByMsphieu(_dong.getMaSoPhieu());
//					qna.setStatus(Short.valueOf("1"));
//					qna.setNoidungtraloi(gson.toJson(_dong));
//					qna.setThoidiemtraloidoitac(new Date());
//					qnaInDetailDao.save(qna);
//				}
//			}
//
//		}
//	}
}
