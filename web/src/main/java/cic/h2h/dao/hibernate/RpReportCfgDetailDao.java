package cic.h2h.dao.hibernate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.RpReportCfgDetail;
import entity.RpReportCfgMaster;

@Repository(value = "rpReportCfgDetailDao")
public class RpReportCfgDetailDao extends H2HBaseDao<RpReportCfgDetail> {
	private static final Logger lg = LogManager.getLogger(RpReportCfgDetailDao.class);

	public void save(ArrayList<RpReportCfgDetail> lstDetails, String reportCode) throws Exception {
		List<RpReportCfgMaster> lstOldMaster = getByReportCode(reportCode);
		Set<RpReportCfgDetail> dels = new HashSet<RpReportCfgDetail>(0);
		for (RpReportCfgMaster oldMaster : lstOldMaster) {
			for (RpReportCfgDetail oldDetail : oldMaster.getReportCfgDetails()) {
				boolean del = true;
				for (RpReportCfgDetail newDetail : lstDetails) {
					if (oldDetail.getId().equals(newDetail.getId())) {
						del = false;
						// Kiem tra du lieu co thay doi khong
						if (!oldDetail.equals(newDetail)) {
							newDetail.setUpdatedDate(new Date());
							super.save(newDetail);
							newDetail.setProcess(Boolean.TRUE);
						}
						break;
					}
					if (Boolean.TRUE.equals(newDetail.getProcess()))
						continue;
					
					// Them moi newDetail luon
					if (Formater.isNull(newDetail.getId())) {
						newDetail.setStatus(BigDecimal.valueOf(1));
						newDetail.setCreatedDate(new Date());
						super.save(newDetail);
					}
					newDetail.setProcess(Boolean.TRUE);

				}
				if (del) {
					dels.add(oldDetail);
				}
			}
		}

		for (RpReportCfgDetail oldDetail : dels) {
			oldDetail.getReportCfgId().getReportCfgDetails().remove(oldDetail);
			super.del(oldDetail);
		}

		// Xu ly not cac ban ghi con lai
		for (RpReportCfgDetail newDetail : lstDetails) {
			// Da duoc xu ly o buoc truoc
			if (Boolean.TRUE.equals(newDetail.getProcess()))
				continue;
			if (Formater.isNull(newDetail.getId())) {
				newDetail.setStatus(BigDecimal.valueOf(1));
				newDetail.setCreatedDate(new Date());
			} else {
				newDetail.setUpdatedDate(new Date());
			}
			super.save(newDetail);
		}
	}

	@SuppressWarnings("unchecked")
	public List<RpReportCfgDetail> getByCfgMasterId(String cfgMasterId) {
		Criteria cr = getCurrentSession().createCriteria(RpReportCfgDetail.class);
		cr.add(Restrictions.eq("reportCfgId.id", cfgMasterId));
		return cr.list();
	}

	@SuppressWarnings("unchecked")
	public List<RpReportCfgMaster> getByReportCode(String reportCode) {
		Criteria cr = getCurrentSession().createCriteria(RpReportCfgMaster.class);
		if (!Formater.isNull(reportCode)) {
			cr.add(Restrictions.eq("reportCode", reportCode));
			cr.addOrder(Order.asc("position"));
			return cr.list();
		}
		return null;
	}

	public boolean isExist(String reportCode) {
		Criteria criteria = getCurrentSession().createCriteria(RpReportCfgDetail.class).add(Restrictions.sqlRestriction(
				"exists (select 1 from RP_REPORT_CFG_MASTER m where m.id = {alias}.REPORT_CFG_ID and m.REPORT_CODE = '"
						+ reportCode + "' )"))
				.setMaxResults(1).setProjection(Projections.rowCount());
		int iRs = ((Long) criteria.uniqueResult()).intValue();
		return iRs > 0;
	}
	
	@SuppressWarnings("unchecked")
	public List<RpReportCfgDetail> getByReportDetails(String reportCode, String errCode) {
		Criteria cr = getCurrentSession().createCriteria(RpReportCfgDetail.class);
		if (!Formater.isNull(reportCode)) {
			cr.add(Restrictions.sqlRestriction(
						"exists (select 1 from RP_REPORT_CFG_MASTER m where m.id = {alias}.REPORT_CFG_ID and m.REPORT_CODE = '"
								+ reportCode + "')"));
		}
		if (!Formater.isNull(errCode))
			cr.add(Restrictions
					.like("errCode", errCode.trim(), MatchMode.ANYWHERE).ignoreCase());
		//cr.addOrder(Order.asc("position"));
		return cr.list();
	}
}
