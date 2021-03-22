package cic.h2h.dao.hibernate;

import java.io.StringWriter;
import java.math.BigInteger;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXB;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

import common.sql.DataSourceConfiguration;
import common.util.Formater;
import dto.ExcelTempDTO;
import entity.CatProduct;
import entity.Partner;
import entity.QnaInDetail;
import entity.QnaInDetailEr;
import entity.QnaInMaster;
import entity.ServiceProduct;
import frwk.dao.hibernate.EscapingLikeExpression;
import frwk.dao.hibernate.sys.SysDao;
import frwk.dao.hibernate.sys.SysPartnerDao;
import intergration.cic.QuestionAnswer;
import oracle.jdbc.OracleTypes;
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

@Repository(value = "qnaInDetailDao")
public class QnaInDetailDao extends SysDao<QnaInDetail> {
	static Logger lg = LogManager.getLogger(QnaInDetailDao.class);

	@Autowired
	private SysPartnerDao sysPartnerDao;

//	@Autowired
//	QuestionAnswer questionAnswer;

	@Autowired
	CatProductDao catProductDao;

	@Autowired
	QnaInMasterDao qnaInMasterDao;

	@Autowired
	QnaInDetailErDao qnaInDetailErDao;

	public Long getCountQnaInDetail(String qnaInDetailId) throws Exception {
		return (Long) getThreadSession().createCriteria(QnaInDetail.class)
				.add(Restrictions.isNotNull("thoidiemtraloidoitac")).add(Restrictions.eq("qnaInMsId.id", qnaInDetailId))
				.setProjection(Projections.rowCount()).uniqueResult();
	}

	public Long getCountQnaInDetailAll(String qnaInDetailId) throws Exception {
		return (Long) getThreadSession().createCriteria(QnaInDetail.class)
				.add(Restrictions.eq("qnaInMsId.id", qnaInDetailId)).setProjection(Projections.rowCount())
				.uniqueResult();
	}

	public QnaInDetail getQuesAndAnswerById(String qnaInDetailId) throws Exception {
		return (QnaInDetail) getThreadSession().get(QnaInDetail.class, qnaInDetailId);
	}

	public HashMap<String, List<?>> getDashBroad(String p_product, Date p_fromDate, Date p_toDate, String p_matctd,
			String p_status) throws SQLException {
		HashMap<String, List<?>> mRs = new HashMap<String, List<?>>();
		Session ss = openNewSession();
		SessionImpl ssImpl = (SessionImpl) ss;
		Connection connection = null;
		CallableStatement call = null;
		ResultSet rs = null;
		try {
			connection = ssImpl.connection();
			call = connection.prepareCall("{call pck_h2h_report.getDashBroad(?,?,?,?,?,?) }");
			call.setString("p_product", p_product);
			call.setString("p_matctd", p_matctd);
			call.setString("p_trangthai", p_status);
			if (p_fromDate == null)
				call.setNull("p_fromDate", Types.DATE);
			else
				call.setDate("p_fromDate", new java.sql.Date(p_fromDate.getTime()));
			if (p_toDate == null)
				call.setNull("p_toDate", Types.DATE);
			else {
				Calendar c = Calendar.getInstance();
				c.setTime(p_toDate);
				c.add(Calendar.DATE, 1);
				call.setDate("p_toDate", new java.sql.Date(c.getTime().getTime()));
			}

			call.registerOutParameter("rCursor", OracleTypes.CURSOR);
			call.execute();
			// Tong du no theo loai tien
			List<String[]> rCursor = new ArrayList<String[]>();
			mRs.put("rCursor", rCursor);
			rs = (ResultSet) call.getObject("rCursor");
			if (rs != null) {
				while (rs.next()) {
					String loaisp = rs.getString("LOAISP");
					String trangthai = rs.getString("TRANGTHAI");
					String soluong = rs.getString("SOLUONG");
					rCursor.add(new String[] { loaisp, trangthai, soluong });
				}
			}
		} catch (Exception ex) {
			lg.error("Loi", ex);
			throw ex;
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs, call, connection);
			ss.close();
		}
		return mRs;
	}

	public HashMap<String, List<ExcelTempDTO>> getDashBroadExcel(String p_product, String p_fromDate, String p_toDate,
			String p_matctd, String p_status) throws SQLException {
		HashMap<String, List<ExcelTempDTO>> mRs = new HashMap<String, List<ExcelTempDTO>>();
		Session ss = openNewSession();
		SessionImpl ssImpl = (SessionImpl) ss;
		Connection connection = null;
		CallableStatement call = null;
		ResultSet rs = null;
		try {
			connection = ssImpl.connection();
			call = connection.prepareCall("{call cic.getDashBroadExcel(?,?,?,?,?,?) }");
			call.setString("p_product", p_product);
			call.setString("p_matctd", p_matctd);
			call.setString("p_trangthai", p_status);
			call.setString("p_fromDate", p_fromDate);
			call.setString("p_toDate", p_toDate);
			call.registerOutParameter("rCursor", OracleTypes.CURSOR);
			call.execute();
			List<ExcelTempDTO> rCursorObj = new ArrayList<ExcelTempDTO>();
			mRs.put("rCursor", rCursorObj);
			try {
				rs = (ResultSet) call.getObject("rCursor");
				if (rs != null) {
					while (rs.next()) {
						ExcelTempDTO obj = new ExcelTempDTO();
						obj.setCol1(rs.getString("MATCTD"));
						obj.setCol2(rs.getString("NAMETCTD"));
						obj.setCol3(rs.getString("LOAISP"));
						obj.setCol4(rs.getString("TENSP"));
						obj.setCol5(rs.getString("SOLUONG"));
						if (rs.getString("TRANGTHAI").equals("1"))
							obj.setCol6("Chưa có kết quả");
						else if (rs.getString("TRANGTHAI").equals("2"))
							obj.setCol6("Đã có kết quả");
						else if (rs.getString("TRANGTHAI").equals("3"))
							obj.setCol6("Đã trả cho TCTD");
						else if (rs.getString("TRANGTHAI").equals("4"))
							obj.setCol6("Không thành công");
						rCursorObj.add(obj);
					}
				}
			} catch (Exception e1) {
				lg.error(e1.getMessage(), e1);
			}
		} catch (Exception ex) {
			lg.error("Loi", ex);
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs, call, connection);
			ss.close();
		}
		return mRs;
	}

	public List<QnaInDetail> reports(String qnaInMsId, String partnerId, String thoigiantraloitu,
			String thoigiantraloiden, String maphieu, String macic, String makh, String tenkh, String socmnd,
			String masothue, String hasResult, String cicErr) {
		List<QnaInDetail> LstQnaInDetail = new ArrayList<QnaInDetail>();
		try {

			Criteria c = getCurrentSession().createCriteria(QnaInDetail.class);
			if (!Formater.isNull(qnaInMsId)) {
				c.add(Restrictions.eq("qnaInMsId.id", qnaInMsId));
			}

			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			if (!Formater.isNull(thoigiantraloitu)) {
				c.add(Restrictions.ge("thoidiemtraloidoitac", df.parse(thoigiantraloitu)));
			}
			if (!Formater.isNull(thoigiantraloiden)) {
				Date currentDate = df.parse(thoigiantraloiden);
				Calendar ca = Calendar.getInstance();
				ca.setTime(currentDate);
				ca.add(Calendar.DATE, 1);
				Date dateAdd = ca.getTime();
				c.add(Restrictions.le("thoidiemtraloidoitac", dateAdd));
			}
			if (!Formater.isNull(maphieu)) {
				EscapingLikeExpression likeExpression = new EscapingLikeExpression("msphieu", maphieu.trim(),
						MatchMode.ANYWHERE, Boolean.TRUE);
				c.add(likeExpression);
			}
			if (!Formater.isNull(macic)) {
				EscapingLikeExpression likeExpression = new EscapingLikeExpression("macic", macic.trim(),
						MatchMode.ANYWHERE, Boolean.TRUE);
				c.add(likeExpression);
			}
			if (!Formater.isNull(makh)) {
				EscapingLikeExpression likeExpression = new EscapingLikeExpression("makh", makh.trim(),
						MatchMode.ANYWHERE, Boolean.TRUE);
				c.add(likeExpression);
			}
			if (!Formater.isNull(tenkh)) {
				EscapingLikeExpression likeExpression = new EscapingLikeExpression("tenkh", tenkh.trim(),
						MatchMode.ANYWHERE, Boolean.TRUE);
				c.add(likeExpression);
			}
			if (!Formater.isNull(socmnd)) {
				EscapingLikeExpression likeExpression = new EscapingLikeExpression("socmt", socmnd.trim(),
						MatchMode.ANYWHERE, Boolean.TRUE);
				c.add(likeExpression);
			}
			if (!Formater.isNull(masothue)) {
				EscapingLikeExpression likeExpression = new EscapingLikeExpression("msthue", masothue.trim(),
						MatchMode.ANYWHERE, Boolean.TRUE);
				c.add(likeExpression);
			}

			if (!Formater.isNull(hasResult) && hasResult.equals("1")) {
				c.add(Restrictions.isNotNull("noidungtraloi"));
			}
			if (!Formater.isNull(cicErr) && cicErr.equals("1")) {
				c.add(Restrictions.eq("status", Short.valueOf((short) 4)));
			}

			if (!Formater.isNull(partnerId) && !partnerId.equals("undefined")) {
				Partner partner = sysPartnerDao.get(Partner.class, partnerId);
				c.add(Restrictions.sqlRestriction("matctd='" + partner.getCode() + "'"));
			}

			c.addOrder(Order.desc("ngaytao"));

			LstQnaInDetail = c.list();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e.getMessage(), e);
			return null;
		}
		return LstQnaInDetail;
	}

	@Autowired
	private ServiceProductDao serviceProductDao;

	@Override
	public void save(QnaInDetail detail) throws Exception {
		Criteria c = getCurrentSession().createCriteria(QnaInDetail.class);
		if (!Formater.isNull(detail.getMacic()))
			c.add(Restrictions.eq("macic", detail.getMacic()));
		if (!Formater.isNull(detail.getMakh()))
			c.add(Restrictions.eq("makh", detail.getMakh()));
		if (!Formater.isNull(detail.getSocmt()))
			c.add(Restrictions.eq("socmt", detail.getSocmt()));
		List<ServiceProduct> serviceProducts = serviceProductDao.getListbyProductId(detail.getServiceProduct().getId());
		ServiceProduct serviceProduct = null;
		if (serviceProducts.size() != 0)
			serviceProduct = serviceProducts.get(0);
		c.add(Restrictions.eq("serviceProduct.id", detail.getServiceProduct().getId()));
		c.addOrder(Order.desc("ngaytao"));
		c.setMaxResults(1);
		List<QnaInDetail> tmp = c.list();
		if (tmp.size() == 0) {
			super.save(detail);
			return;
		}
		QnaInDetail latest = tmp.get(0);
		Calendar clast = Calendar.getInstance();
		clast.setTime(
				latest.getThoidiemtraloidoitac() == null ? latest.getNgaytao() : latest.getThoidiemtraloidoitac());

		if (serviceProduct.getCatProductId().getTimeToLive() != null)
			clast.add(Calendar.DATE, serviceProduct.getCatProductId().getTimeToLive().intValue());
		else
			clast.add(Calendar.DATE, serviceProduct.getServiceInfoId().getTimeToLive().intValue());
		// Lay thoi gian hieu luc
		Calendar now = Calendar.getInstance();

		if (now.after(clast)) {
			super.save(detail);
			return;
		} else {
			detail = latest;
		}

	}

	@SuppressWarnings("deprecation")
	public QnaInDetail getQnaByMsphieu(String msp) throws Exception {
		return (QnaInDetail) getThreadSession().createCriteria(QnaInDetail.class).add(Restrictions.eq("msphieu", msp))
				.uniqueResult();
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<QnaInDetail> getNotSentQnaByProduct(String catProductId) throws Exception {
		List<QnaInDetail> lstQnaInDetails = getCurrentSession().createCriteria(QnaInDetail.class)
				.add(Restrictions.eq("serviceProduct.id", catProductId))
				.add(Restrictions.isNull("thoiDiemGuiVaoCoreCIC")).list();
		return lstQnaInDetails;
	}

	public void getDataJobHoiTin(QnaInMaster qnaInMaster) throws Exception {
		Gson gson = new Gson();
		// build object HoiTin
		HoiTin obj = new HoiTin();
		// build object TTChung
		TTChung ttChung = new TTChung();
		ttChung.setLoaiSP(qnaInMaster.getLoaisp());
		ttChung.setSLPhieu(BigInteger.valueOf(qnaInMaster.getQnaInDetails().size()));
		// build object DSKhachHang
		DSKhachHang dsKhachHang = new DSKhachHang();
		for (QnaInDetail qnaInDetail : qnaInMaster.getQnaInDetails()) {
			if (qnaInDetail.getQnaInMsId() == null)
				qnaInDetail.setQnaInMsId(qnaInMaster);
			Dong dong = new Dong();
			dong.setMaSoPhieu(qnaInDetail.getMsphieu());
			dong.setMaKH(qnaInDetail.getMakh());
			dong.setTenKH(qnaInDetail.getTenkh());
			dong.setMaCIC(qnaInDetail.getMacic());
			dong.setSoCMT(qnaInDetail.getSocmt());
			dong.setDiaChi(qnaInDetail.getDiachi());
			dsKhachHang.getDong().add(dong);
			qnaInDetail.setThoiDiemGuiVaoCoreCIC(new Date());
		}
		obj.setTTChung(ttChung);
		obj.setDSKhachHang(dsKhachHang);
		// call service cic
		PHHoiTin response = null;
		try {
			response = null;// questionAnswer.hoiTin(obj);
		} catch (Exception e) {
			lg.error(e);
			qnaInMaster.setMaLoi("CMM_999");
			qnaInMaster.setMoTaLoi("Lỗi call service tích hợp CIC");
			if (Utils.isNullObject(qnaInMaster.getRetryTime()))
				qnaInMaster.setRetryTime(Short.valueOf("0"));
			else
				qnaInMaster.setRetryTime((short) (qnaInMaster.getRetryTime() + Short.valueOf("1")));
			qnaInMasterDao.save(qnaInMaster);
		}
		// xu ly data service cic tra ve
		if (!Utils.isNullObject(response)) {
			qnaInMaster.setNumOfErrorCus(response.getTTPhanHoi().getSLThatBai() == null ? Long.valueOf("0")
					: response.getTTPhanHoi().getSLThatBai().longValue());
			if (response.getTTPhanHoi().getMa().equalsIgnoreCase("CMM_999")) {
				qnaInMaster.setMaLoi("CMM_999");
				qnaInMaster.setMoTaLoi("Lỗi call service tích hợp CIC");
				if (Utils.isNullObject(qnaInMaster.getRetryTime()))
					qnaInMaster.setRetryTime(Short.valueOf("0"));
				else
					qnaInMaster.setRetryTime((short) (qnaInMaster.getRetryTime() + Short.valueOf("1")));
				qnaInMasterDao.save(qnaInMaster);
			} else if (response.getTTPhanHoi().getMa().equalsIgnoreCase("CMM_005")) {
				qnaInMaster.setMaLoi("CMM_005");
				qnaInMaster.setMoTaLoi(response.getTTPhanHoi().getMoTa());
				if (!Utils.isNullObject(response.getDSKhachHangKQ())
						&& !response.getDSKhachHangKQ().getDongKQ().isEmpty()) {
					for (DongKQ dongKQ : response.getDSKhachHangKQ().getDongKQ()) {
						QnaInDetail qna = getQnaByMsphieu(dongKQ.getMaSoPhieu());
						for (Loi loi : dongKQ.getDSLoi().getLoi()) {
							QnaInDetailEr er = new QnaInDetailEr();
							er.setMaloi(loi.getMaLoi());
							er.setMotaloi(loi.getMoTaLoi());
							qna.setStatus(Short.valueOf("4"));
							qna.setNoidungtraloi(gson.toJson(dongKQ));
							er.setQnaInDetailId(qna);
							qnaInDetailErDao.save(er);
						}

					}
				}
			}
		}
		StringWriter sw = new StringWriter();
		JAXB.marshal(response, sw);
		String xmlString01 = sw.toString();
		xmlString01 = StringEscapeUtils.escapeHtml(xmlString01);
		lg.info(xmlString01);
	}

	public void getDataJobHoiTin(String maSanPham) throws Exception {
		lg.info("START job getDataJobHoiTin :: Ma san pham ::" + maSanPham);
		List<QnaInMaster> msToSend = getSentErrMs(maSanPham);

		CatProduct catProduct = catProductDao.getCatProductByCode(maSanPham);
		List<QnaInDetail> lstQnaInDetailsNotSend = getNotSentQnaByProduct(catProduct.getId());
		QnaInMaster qnaInMaster = null;
		if (!Formater.isNull(lstQnaInDetailsNotSend)) {
			qnaInMaster = new QnaInMaster();
			qnaInMaster.setLoaisp(maSanPham);
			qnaInMaster.setThoigianyc(Calendar.getInstance().getTime());
			qnaInMaster.setStatus(Short.valueOf("1"));
			qnaInMaster.setTongSoPhieu(Long.valueOf(lstQnaInDetailsNotSend.size()));
			qnaInMasterDao.save(qnaInMaster);
			for (QnaInDetail detail : lstQnaInDetailsNotSend)
				detail.setQnaInMsId(qnaInMaster);
			msToSend.add(qnaInMaster);
		}
		getDataJobHoiTin(msToSend);
	}

	private void getDataJobHoiTin(List<QnaInMaster> msToSend) throws Exception {
		if (msToSend.isEmpty())
			return;

		Gson gson = new Gson();
		// build object HoiTin
		HoiTin obj = new HoiTin();
		// build object TTChung
		TTChung ttChung = new TTChung();
		ttChung.setLoaiSP(msToSend.get(0).getLoaisp());
		ttChung.setSLPhieu(BigInteger.valueOf(0));
		// build object DSKhachHang
		DSKhachHang dsKhachHang = new DSKhachHang();
		for (QnaInMaster ms : msToSend) {
			ttChung.setSLPhieu(ttChung.getSLPhieu().add(BigInteger.valueOf(ms.getQnaInDetails().size())));
			for (QnaInDetail qnaInDetail : ms.getQnaInDetails()) {
				Dong dong = new Dong();
				dong.setMaSoPhieu(qnaInDetail.getMsphieu());
				dong.setMaKH(qnaInDetail.getMakh());
				dong.setTenKH(qnaInDetail.getTenkh());
				dong.setMaCIC(qnaInDetail.getMacic());
				dong.setSoCMT(qnaInDetail.getSocmt());
				dong.setDiaChi(qnaInDetail.getDiachi());
				dsKhachHang.getDong().add(dong);
				qnaInDetail.setThoiDiemGuiVaoCoreCIC(new Date());
			}
		}

		obj.setTTChung(ttChung);
		obj.setDSKhachHang(dsKhachHang);
		// call service cic
		PHHoiTin response = null;
		try {
			response = null;// questionAnswer.hoiTin(obj);
		} catch (Exception e) {
			lg.error(e);
			for (QnaInMaster qnaInMaster : msToSend) {
				qnaInMaster.setMaLoi("CMM_999");
				qnaInMaster.setMoTaLoi("Lỗi call service tích hợp CIC");
				if (Utils.isNullObject(qnaInMaster.getRetryTime()))
					qnaInMaster.setRetryTime(Short.valueOf("0"));
				else
					qnaInMaster.setRetryTime((short) (qnaInMaster.getRetryTime() + Short.valueOf("1")));
				qnaInMasterDao.save(qnaInMaster);
			}

		}
		// xu ly data service cic tra ve
		if (!Utils.isNullObject(response)) {
			if (response.getTTPhanHoi().getMa().equalsIgnoreCase("CMM_999")) {
				for (QnaInMaster qnaInMaster : msToSend) {
					qnaInMaster.setMaLoi("CMM_999");
					qnaInMaster.setMoTaLoi("Lỗi call service tích hợp CIC");
					if (Utils.isNullObject(qnaInMaster.getRetryTime()))
						qnaInMaster.setRetryTime(Short.valueOf("0"));
					else
						qnaInMaster.setRetryTime((short) (qnaInMaster.getRetryTime() + Short.valueOf("1")));
					qnaInMasterDao.save(qnaInMaster);
				}

			} else if (response.getTTPhanHoi().getMa().equalsIgnoreCase("CMM_005")) {
				for (QnaInMaster qnaInMaster : msToSend) {
					qnaInMaster.setMaLoi("CMM_005");
					qnaInMaster.setMoTaLoi(response.getTTPhanHoi().getMoTa());
				}
				if (!Utils.isNullObject(response.getDSKhachHangKQ())
						&& !response.getDSKhachHangKQ().getDongKQ().isEmpty()) {
					Map<String, QnaInMaster> processMs = new HashMap<String, QnaInMaster>();
					for (DongKQ dongKQ : response.getDSKhachHangKQ().getDongKQ()) {
						if (!dongKQ.getDSLoi().getLoi().isEmpty()) {
							QnaInDetail qna = getQnaByMsphieu(dongKQ.getMaSoPhieu());
							if (processMs.get(qna.getQnaInMsId().getId()) == null) {
								qna.getQnaInMsId().setNumOfErrorCus(0l);
								processMs.put(qna.getQnaInMsId().getId(), qna.getQnaInMsId());
							}
							QnaInMaster ms = processMs.get(qna.getQnaInMsId().getId());

							for (Loi loi : dongKQ.getDSLoi().getLoi()) {
								QnaInDetailEr er = new QnaInDetailEr();
								er.setMaloi(loi.getMaLoi());
								er.setMotaloi(loi.getMoTaLoi());
								qna.setStatus(Short.valueOf("4"));
								qna.setNoidungtraloi(gson.toJson(dongKQ));
								er.setQnaInDetailId(qna);
								qnaInDetailErDao.save(er);
							}
							Long numOfError = ms.getNumOfErrorCus();
							numOfError++;
							ms.setNumOfErrorCus(numOfError);
						}
					}
				}
			}
		}
		StringWriter sw = new StringWriter();
		JAXB.marshal(response, sw);
		String xmlString01 = sw.toString();
		xmlString01 = StringEscapeUtils.escapeHtml(xmlString01);
		lg.info(xmlString01);

	}

	public List<QnaInMaster> getSentErrMs(String maSanPham) {
		List<QnaInMaster> temp = getCurrentSession().createCriteria(QnaInMaster.class)
				.add(Restrictions.eq("loaisp", maSanPham)).add(Restrictions.eq("maLoi", "CMM_999")).list();
		return temp;
	}

	public List<String> getMspJobVanTinDsPhieu() {
		List<String> rCursor = new ArrayList<String>();
		Session ss = openNewSession();
		SessionImpl ssImpl = (SessionImpl) ss;
		Connection connection = null;
		CallableStatement call = null;
		ResultSet rs = null;
		try {
			connection = ssImpl.connection();
			call = connection.prepareCall("{call cic.listMaSoPhieuVanTin(?) }");
			call.registerOutParameter("rCursor", OracleTypes.CURSOR);
			call.execute();

			try {
				rs = (ResultSet) call.getObject("rCursor");
				if (rs != null) {
					while (rs.next()) {
						rCursor.add(rs.getString("MSPHIEU"));
					}
				}
			} catch (Exception e1) {
				lg.error(e1.getMessage(), e1);
			}
		} catch (Exception ex) {
			lg.error("Loi", ex);
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs, call, connection);
			ss.close();
		}
		return rCursor;
	}

	public Date getLastCreatedDate() {
		Criteria query = openNewSession().createCriteria(QnaInDetail.class);
		query.setProjection(Projections.max("ngaytao"));
		return (Date) query.uniqueResult();
	}
}
