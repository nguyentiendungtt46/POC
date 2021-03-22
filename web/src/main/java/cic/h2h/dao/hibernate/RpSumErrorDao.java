package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.RpSum;
import entity.RpSumError;

@Repository(value = "rpSumErrorDao")
public class RpSumErrorDao extends H2HBaseDao<RpSumError> {

	private static final Logger log = LogManager.getLogger(RpSumErrorDao.class);

	public List<RpSumError> getRpSumErrorByReportId(String id) {
		return getCurrentSession().createCriteria(RpSumError.class).add(Restrictions.eq("rpSum.id", id)).list();
	}

	public boolean hadCancel(RpSum rpSum) {
		return getCurrentSession().createCriteria(RpSumError.class).add(Restrictions.eq("rpSum", rpSum))
				.add(Restrictions.eq("errorCode", "RPF_169")).setMaxResults(1).list().size() > 0;
	}
}
