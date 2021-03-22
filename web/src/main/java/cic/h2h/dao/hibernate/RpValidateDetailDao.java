package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.hibernate.transform.DistinctRootEntityResultTransformer;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.RpSum;
import entity.RpValidateDetail;
import entity.VRpValidateDetail;
import frwk.dao.hibernate.BaseDao;

@Repository("rpValidateDetailDao")
public class RpValidateDetailDao extends H2HBaseDao<RpValidateDetail> {
	private static Logger lg = LogManager.getLogger(RpValidateDetailDao.class);

	public List<RpValidateDetail> getRpValidateDetailDaoByReportId(String id) {
		List<RpValidateDetail> details = new ArrayList<RpValidateDetail>();
		Criteria criteria = getCurrentSession().createCriteria(RpValidateDetail.class);
		try {
			criteria.add(Restrictions.eq("rpSum.id", id));
			details = criteria.list();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e);
		}
		return details;
	}

	@Override
	public long count() throws Exception {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(RpValidateDetail.class);

		// Dieu kien tim kiem
		for (Criterion criterion : getRestrictions())
			criteria.add(criterion);

		 
		criteria.setProjection(Projections.countDistinct("concated"));
		long iRs = 0;
		iRs = (long) criteria.uniqueResult();
		return iRs;
	}

	@Override
	public ArrayList<RpValidateDetail> search() {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(RpValidateDetail.class);

		// Dieu kien tim kiem
		for (Criterion criterion : getRestrictions())
			criteria.add(criterion);

		// Tham so tim kiem
		if (getSearchParam() != null) {
			criteria.setFirstResult(getSearchParam().getBeginIndex().intValue());
			criteria.setMaxResults(getSearchParam().getPageSize());
		}

		// Order
		if (!Formater.isNull(getOrders())) {
			for (Order order : getOrders())
				criteria.addOrder(order);
		}

		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("maCN"));
		projList.add(Projections.property("tenCN"));
		projList.add(Projections.property("maKH"));
		projList.add(Projections.property("tenKH"));
		projList.add(Projections.property("dongLoi"));
		projList.add(Projections.property("maChiTieu"));
		projList.add(Projections.property("giaTriLoi"));
		projList.add(Projections.property("maLoi"));
		projList.add(Projections.property("moTaLoi"));

		criteria.setProjection(Projections.distinct(projList)).setResultTransformer(new ResultTransformer() {
			@Override
			public Object transformTuple(Object[] tuple, String[] aliases) {
				return new RpValidateDetail(tuple);
			}

			@Override
			public List transformList(List collection) {
				return collection;
			}
		});

		ArrayList<RpValidateDetail> temp123 = (ArrayList<RpValidateDetail>) criteria.list();
		return temp123;

	}

	public boolean exists(RpSum rpSum) {
		List temp = getCurrentSession().createCriteria(RpValidateDetail.class).add(Restrictions.eq("rpSum", rpSum))
				.setMaxResults(1).list();
		return temp.size() > 0;
	}
}
