package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.CatProduct;
import entity.CatProductCfg;
import entity.RpReportCfgDetail;
import entity.RpReportCfgMaster;

@Repository(value = "rpReportCfgMasterDao")
public class RpReportCfgMasterDao extends H2HBaseDao<RpReportCfgMaster> {
	private static final Logger lg = LogManager.getLogger(RpReportCfgMasterDao.class);

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
		Criteria criteria = getCurrentSession().createCriteria(RpReportCfgMaster.class)
				.add(Restrictions.eq("reportCode", reportCode)).setMaxResults(1).setProjection(Projections.rowCount());
		int iRs = ((Long) criteria.uniqueResult()).intValue();
		return iRs > 0;
	}

	public List<RpReportCfgMaster> reports(String reportCode, String reportValue) {
		List<RpReportCfgMaster> list = new ArrayList<RpReportCfgMaster>();
		try {
			Criteria criteria = getCurrentSession().createCriteria(RpReportCfgMaster.class);
			if (!Formater.isNull(reportCode))
				criteria.add(Restrictions.like("reportCode", reportCode.trim(), MatchMode.ANYWHERE).ignoreCase());
			if (!Formater.isNull(reportValue))
				criteria.add(Restrictions.like("propertyDes", reportValue.trim(), MatchMode.ANYWHERE).ignoreCase());
			list = criteria.list();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e.getMessage(), e);
		}
		return list;
	}

	public void save(ArrayList<RpReportCfgMaster> lstMaster, String reportCode) throws Exception {
		// Lay danh sach cu
		List<RpReportCfgMaster> olds = getByReportCode(reportCode);
		Session ss = getCurrentSession();
		// Xoa nhung item khong co trong list
		for (RpReportCfgMaster old : olds) {
			boolean del = true;
			for (RpReportCfgMaster cfgMaster : lstMaster) {
				if (old.getId().equals(cfgMaster.getId())) {
					del = false;
					break;
				}
			}
			if (del)
				super.del(old);
		}

		// Cap nhat
		for (RpReportCfgMaster cfgMaster : lstMaster) {
			super.save(cfgMaster);
		}
	}

}
