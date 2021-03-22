package cic.h2h.dao.hibernate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.RpType;
import frwk.dao.hibernate.OrderBySqlFormula;

@Repository(value = "rpTypeDao")
public class RpTypeDao extends H2HBaseDao<RpType> {

	private static final Logger log = LogManager.getLogger(ServiceInfoDao.class);

	public List<RpType> getAll() throws Exception {
		return getThreadSession().createCriteria(RpType.class).list();
	}

	public List<RpType> getAllActive() throws Exception {
		return getThreadSession().createCriteria(RpType.class)
				.add(Restrictions.or(Restrictions.isNull("inActive"), Restrictions.eq("inActive", Boolean.FALSE)))
				.list();
	}

	public List<RpType> getAllFilterReport() throws Exception {

		String[] hasFilterRp = new String[] { "K3122A", "K3121A", "K3111A", "K3112A", "K3113", "K3123", "K3223",
				"K3213", "K3333", "K3331", "K3332", "K4011", "K4012", "K4013", "K4021", "K4022", "K4023", "K5011", "K5012", "K5013",
				"T02G1", "T02G2", "T02DS", "K1011", "K1012", "K1013", "K1021", "K1022", "K1023", "K3111", "K3112",
				"K3121", "K3122", "K2011", "K2012", "K2013", "K2B011", "K2B012", "K2B013", "K1131", "K1132", "K1133" };
		return getThreadSession().createCriteria(RpType.class)
				.add(Restrictions.or(Restrictions.isNull("inActive"), Restrictions.eq("inActive", Boolean.FALSE)))
				.add(Restrictions.in("id", hasFilterRp)).addOrder(Order.asc("id")).list();
	}

	public void save(RpType type) throws Exception {
		RpType oldType = getCurrentSession().get(RpType.class, type.getId());
		if (oldType == null) {
			
			super.save(type);
		} else {
			if ((type.getInActive() != null && oldType.getInActive() == null)
					|| (Boolean.compare(type.getInActive(), oldType.getInActive()) != 0)) {
				oldType.setInActive(type.getInActive());
			}
			super.save(type);
		}

	}
	
	public List<RpType> reports(String tempCode, String reportCode, String fileType, String dataTye, String cusType,
			String reportType, String active) {
		List<RpType> rpTypes = new ArrayList<RpType>();
		try {
			Criteria c = getCurrentSession().createCriteria(RpType.class);
			if (!Formater.isNull(tempCode)) {
				c.add(Restrictions.like("templateCode", tempCode, MatchMode.ANYWHERE).ignoreCase());
			}
			if (!Formater.isNull(reportCode)) {
				c.add(Restrictions.like("id", reportCode, MatchMode.ANYWHERE).ignoreCase());
			}
			if (!Formater.isNull(fileType)) {
				c.add(Restrictions.eq("fileType", fileType));
			}
			if (!Formater.isNull(dataTye)) {
				c.add(Restrictions.eq("dataType", dataTye));
			}
			if (!Formater.isNull(cusType)) {
				c.add(Restrictions.eq("cusType", Short.valueOf(cusType)));
			}
			if (!Formater.isNull(reportType)) {
				c.add(Restrictions.eq("reportType", Short.valueOf(reportType)));
			}
			if (!Formater.isNull(active)) {
				if ("1".equals(active)) {
					c.add(Restrictions.or(Restrictions.isNull("inActive"),
							Restrictions.eq("inActive", Boolean.FALSE)));
				} else c.add(Restrictions.eq("inActive", Boolean.TRUE));
			}
			c.addOrder(OrderBySqlFormula.sqlFormula("CASE FILE_TYPE WHEN 'K' THEN 0 WHEN 'P' THEN 1 WHEN 'D' THEN 2 ELSE 3 END asc"));
			c.addOrder(Order.asc(RpType.DATA_TYPE));
			c.addOrder(Order.asc(RpType.CUS_TYPE));
			c.addOrder(Order.asc(RpType.REPORT_TYPE));
			rpTypes = c.list();
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
			return null;
		}
		return rpTypes;
	}

	public void makeData(Map<String, String> mapFileType, Map<String, String> mapDataType,
			Map<Short, String> mapCusType, Map<Short, String> mapReportType) {
		for (Map.Entry<String, String> fileType : mapFileType.entrySet()) {
			for (Map.Entry<String, String> dataType : mapDataType.entrySet()) {
				for (Map.Entry<Short, String> cusType : mapCusType.entrySet()) {
					for (Map.Entry<Short, String> reportType : mapReportType.entrySet()) {
						RpType rpType = new RpType(fileType, dataType, cusType, reportType);
						getCurrentSession().saveOrUpdate(rpType);
					}
				}
			}

		}

	}

}
