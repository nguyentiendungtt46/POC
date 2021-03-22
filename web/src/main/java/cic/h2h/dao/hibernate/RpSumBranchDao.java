package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.RpSum;
import entity.RpSumBranch;

@Repository(value = "rpSumBranchDao")
public class RpSumBranchDao extends H2HBaseDao<RpSumBranch> {
	private static final Logger log = LogManager.getLogger(RpSumBranchDao.class);

	public List<RpSumBranch> getRpSumBranchByReportId(String id) {
		log.info("BEGIN getRpSumErrorByReportId  reportId : " + id);
		List<RpSumBranch> infos = new ArrayList<RpSumBranch>();
		Criteria cr = getCurrentSession().createCriteria(RpSumBranch.class);
		cr.add(Restrictions.eq("rpSum.id", id));
		try {
			infos = cr.list();
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
		}
		log.info("END getRpSumErrorByReportId ");
		return infos;
	}

	public boolean existsError(RpSum rpSum) {
		String sqlQuery = "select * from rp_sum_branch_err be\r\n" + "   where exists (select 1\r\n"
				+ "            from rp_sum_branch b\r\n" + "           where b.rp_sum_id = :rp_sum_id\r\n"
				+ "             and b.id = be.rp_sum_branch_id)";
		SQLQuery q = getCurrentSession().createSQLQuery(sqlQuery);
		q.setMaxResults(1);
		q.setParameter("rp_sum_id", rpSum.getId());
		Object o = q.uniqueResult();
//		Long l = (Long) o;
//		return l > 0;
		return o != null;
	}
}
