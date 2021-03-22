package cic.h2h.dao.hibernate;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.SessionImpl;
import org.hibernate.jdbc.Work;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import common.sql.DataSourceConfiguration;
import common.util.FormatNumber;
import common.util.Formater;
import common.util.ResourceException;
import constants.RightConstants;
import entity.CatError;
import entity.RpSum;
import entity.RpSumError;
import entity.RpType;
import frwk.dao.hibernate.sys.SysDao;
import oracle.jdbc.OracleTypes;

@Repository(value = "rpSumDao")
public class RpSumDao extends SysDao<RpSum> {
	static Logger lg = LogManager.getLogger(RpSumDao.class);

	public void redo(String reportId) throws Exception {
		RpSum rpSum = getCurrentSession().load(RpSum.class, reportId);
		deleteOldData(rpSum.getId());
		getCurrentSession().flush();
		getCurrentSession().delete(rpSum);
		getCurrentSession().save(rpSum.clone());
		// Thuc hien lai ca bao cao lien quan
		if (!Formater.isNull(rpSum.getReverseReportId())) {
			rpSum = getCurrentSession().load(RpSum.class, rpSum.getReverseReportId());
			deleteOldData(rpSum.getId());
			getCurrentSession().flush();
			getCurrentSession().delete(rpSum);
			getCurrentSession().save(rpSum.clone());
		}
	}

	private void deleteOldData(String id) {
		getCurrentSession().doWork(new Work() {
			@Override
			public void execute(Connection connection) throws SQLException {
				CallableStatement call = null;
				call = connection.prepareCall("{call cic.deleteOldData(?) }");
				call.setString(1, id);
				call.execute();
			}
		});

	}

	@Autowired
	private RpTypeDao rpTypeDao;

	public HashMap<String, List<?>> reportGroup(RpSum rpSum) throws SQLException {
		HashMap<String, List<?>> mRs = new HashMap<String, List<?>>();
		Session ss = openNewSession();
		SessionImpl ssImpl = (SessionImpl) ss;
		Connection connection = null;
		CallableStatement call = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		ResultSet rs2 = null;
		try {
			connection = ssImpl.connection();
			call = connection.prepareCall("{call cic.reportGroup(?,?,?,?,?,?) }");
			call.setString("p_report_id", rpSum.getId());
			RpType rpType = rpTypeDao.get(RpType.class, rpSum.getReportCode());
			call.setString("p_table_name", rpType.getTableName());
			call.registerOutParameter("rCursorTotalBal", OracleTypes.CURSOR);
			call.registerOutParameter("rCursorTotalBalAll", OracleTypes.CURSOR);
			call.registerOutParameter("rCursorTotalBalByCusNCurrency", OracleTypes.CURSOR);
			call.registerOutParameter("rCursorK5", OracleTypes.CURSOR);
			call.execute();
			// Tong du no theo loai tien
			List<String[]> rCursorTotalBal = new ArrayList<String[]>();
			mRs.put("totalBalByCurrency", rCursorTotalBal);
			try {
				rs = (ResultSet) call.getObject("rCursorTotalBal");
				if (rs != null) {
					while (rs.next()) {
						String code = rs.getString("LOAI_TIEN");
						BigDecimal value = rs.getBigDecimal("TONG_DUNO_CN");
						rCursorTotalBal.add(new String[] { code, FormatNumber.num2Str(value) });
					}
				}
			} catch (Exception e1) {
				lg.error(e1.getMessage(), e1);
			}
			// Du no noi bang, ngoai bang, cam ket
			List<String[]> rCursorTotalBalAll = new ArrayList<String[]>();
			mRs.put("totalBalAll", rCursorTotalBalAll);
			try {
				rs1 = (ResultSet) call.getObject("rCursorTotalBalAll");
				if (rs1 != null) {
					while (rs1.next()) {
						String code = rs1.getString("VALUE_LOAITIEN");
						BigDecimal duNoNoiBang = rs1.getBigDecimal("DUNO_NOIBANG");
						BigDecimal duNoNgoaiBang = rs1.getBigDecimal("DUNO_NGOAIBANG");
						BigDecimal duNoCamKet = rs1.getBigDecimal("DUNO_CAMKET");
						rCursorTotalBalAll.add(new String[] { code, FormatNumber.num2Str(duNoNoiBang),
								FormatNumber.num2Str(duNoNgoaiBang), FormatNumber.num2Str(duNoCamKet) });
					}
				}
			} catch (Exception e) {
				lg.error(e.getMessage(), e);
			}
			// K5
			List<String[]> rCursorK5 = new ArrayList<String[]>();
			mRs.put("rCursorK5", rCursorK5);
			try {
				rs2 = (ResultSet) call.getObject("rCursorK5");
				if (rs2 != null) {
					while (rs2.next()) {
						String loaitien = rs2.getString("TP010");
						BigDecimal tp = rs2.getBigDecimal("TONG_TRAIPHIEU");
						BigDecimal dt = rs2.getBigDecimal("TONG_DAUTTU");
						rCursorK5.add(new String[] { loaitien, FormatNumber.num2Str(tp), FormatNumber.num2Str(dt) });
					}
				}
			} catch (Exception e) {
				lg.error(e.getMessage(), e);
			}
		} catch (Exception ex) {
			lg.error("Loi", ex);
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs, rs1, rs2, call, connection);
			ss.close();
		}
		// Tong du no theo ma khach hang va loai tien
//		List<String[]> totalBalByCusAndVND = new ArrayList<String[]>();
//		mRs.put("totalBalByCusAndVND", totalBalByCusAndVND);
//		try {
//			rs = (ResultSet) call.getObject("rCursorTotalBalByCusNCurrency");
//			if (rs != null) {
//				while (rs.next()) {
//					String code = rs.getString("MA_KH");
//					String currencyId = rs.getString("loai_tien");
//					String currencyCode = rs.getString("ma_loai_tien");
//					BigDecimal amout = rs.getBigDecimal("SO_TIEN");
//					rCursorTotalBalAll.add(new String[] {code,currencyId, currencyCode, FormatNumber.num2Str(amout) });
//				}
//			}
//		} catch (Exception e) {
//			lg.error(e.getMessage(), e);
//		}
		return mRs;
	}

	public HashMap<String, List<?>> listCustomerError(RpSum rpSum, String pType) throws SQLException {
		HashMap<String, List<?>> mRs = new HashMap<String, List<?>>();
		Session ss = openNewSession();
		SessionImpl ssImpl = (SessionImpl) ss;
		Connection connection = null;
		CallableStatement call = null;
		ResultSet rs = null;
		try {
			connection = ssImpl.connection();
			call = connection.prepareCall("{call cic.listCustomerError(?,?,?,?) }");
			call.setString("p_report_id", rpSum.getId());
			RpType rpType = rpTypeDao.get(RpType.class, rpSum.getReportCode());
			call.setString("p_table_name", rpType.getTableName());
			call.setString("p_type", pType);
			call.registerOutParameter("rCursor", OracleTypes.CURSOR);
			call.execute();
			// Tong du no theo loai tien
			List<String[]> rCursor = new ArrayList<String[]>();
			mRs.put("rCursor", rCursor);
			try {
				rs = (ResultSet) call.getObject("rCursor");
				if (rs != null) {
					while (rs.next()) {
						String maCN = rs.getString("MACN");
						String maKH = rs.getString("MA_KH");
						String valK31 = rs.getString("VAL_K31");
						String valK32 = rs.getString("VAL_K32");
						rCursor.add(new String[] { maCN, maKH, valK31, valK32 });
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

	@Autowired
	private CatErrorDAO catErrorDAO;
	@Autowired
	private MessageSource messageSource;

	public void reject(String id) throws Exception {
		RpSum s = getObject(RpSum.class, id);
		s.setRedo(RightConstants.REDO_1);// tra lai tctd
		// Danh dau waitResource cho toan bo cac bao cao doi ung voi bao cao nay
		Criteria c = getCurrentSession().createCriteria(RpSum.class);
		c.add(Restrictions.eq("reverseReportId", s.getId()));
		List<RpSum> lstRp = c.list();
		if (Formater.isNull(lstRp))
			return;

		CatError e148 = catErrorDAO.getByCode("RPF_148");
		for (RpSum r : lstRp) {
			r.setWaitResource(Boolean.TRUE);// tra lai tctd
			r.setWaitStartTime(Calendar.getInstance().getTime());

			// Tinh toan lai redo
			if (!Short.valueOf((short) 1).equals(r.getRedo())) {
				if (Short.valueOf((short) 3).equals(r.getStatus()) || Short.valueOf((short) 2).equals(r.getRedo())) {
					if (e148.getReDo() != null && e148.getReDo() == 1)
						r.setRedo((short) 1);
				} else if (e148.getReDo() != null)
					r.setRedo(e148.getReDo());
			}

			// Ghi them loi
			RpSumError rpe148 = new RpSumError();
			rpe148.setErrorCode("RPF_148");
			rpe148.setErrorCause(String.format(messageSource.getMessage("RPF_148_D", null, "Default", null),
					(r.getReportCode().startsWith("K31") ? "K32" : "K31")));
			rpe148.setErrorDes(messageSource.getMessage("RPF_148", null, "Default", null));
			rpe148.setRpSum(r);
			getCurrentSession().save(rpe148);

		}

	}

	public List<RpSum> reports(String tuNgay, String denNgay, String partnerCode, String fileName, String status,
			String userReport, String reportCode, String reDo) {
		// TODO Auto-generated method stub
		List<RpSum> rpSums = new ArrayList<RpSum>();
		try {
			Criteria criteria = getCurrentSession().createCriteria(RpSum.class);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

			if (!Formater.isNull(tuNgay)) {
				criteria.add(Restrictions.ge("createdDate", df.parse(tuNgay)));
			}
			if (!Formater.isNull(denNgay)) {
				Date currentDate = df.parse(denNgay);
				Calendar c = Calendar.getInstance();
				c.setTime(currentDate);
				c.add(Calendar.DATE, 1);
				Date dateAdd = c.getTime();
				criteria.add(Restrictions.le("createdDate", dateAdd));
			}

			// Search by user bao cao
			if (!Formater.isNull(userReport)) {
				criteria.add(Restrictions.like("userReport", userReport.trim(), MatchMode.ANYWHERE).ignoreCase());
			}
			// Search by ma bao cao rut gon
			if (!Formater.isNull(reportCode))
				criteria.add(Restrictions.eq("reportCode", reportCode.trim()));

			if (!Formater.isNull(fileName))
				criteria.add(Restrictions.like("fileName", fileName.trim(), MatchMode.ANYWHERE).ignoreCase());

			// Search by ma tctd
			if (!Formater.isNull(partnerCode)) {
				criteria.add(Restrictions.or(Restrictions.eq("tctdCode", partnerCode.trim()),
						Restrictions.sqlRestriction(
								"exists (select 1 from partner_branch pb, partner p where pb.partner_id = p.id and pb.code = {alias}.tctd_code and p.code = ?)",
								partnerCode.trim(), StringType.INSTANCE)));
			}

			// Search by trang thai
			if (!Formater.isNull(status))
				criteria.add(Restrictions.eq("status", Short.valueOf(status)));

			// Search by trang thai redo
			if (!Formater.isNull(reDo) && "1".equals(reDo))
				criteria.add(Restrictions.eq("redo", Short.valueOf("1")));
			criteria.addOrder(Order.desc("createdDate"));
			rpSums = criteria.list();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e.getMessage(), e);
		}
		return rpSums;
	}

}
