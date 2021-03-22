package cic.h2h.dao.hibernate;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import common.util.ResourceException;
import entity.AppFlow;
import entity.WrkFlwMng;
import frwk.dao.hibernate.sys.SysDao;

@Repository(value = "appFlowDao")
public class AppFlowDao extends SysDao<AppFlow> {
	private static final Logger logger = LogManager.getLogger(AppFlowDao.class);

	public AppFlow getByStatus(Short status, Short subStatus, Class<?> modelClass) throws ResourceException {
		WrkFlwMng flwMng = (WrkFlwMng) getCurrentSession().createCriteria(WrkFlwMng.class)
				.add(Restrictions.eq("caseType", modelClass.getCanonicalName())).uniqueResult();
		if (flwMng == null)
			throw new ResourceException("Not exists workflow for the case type");
		Criteria c = getCurrentSession().createCriteria(AppFlow.class).add(Restrictions.eq("wrkFlwMng", flwMng))
				.add(Restrictions.eq("status", status));
		if (subStatus != null)
			c.add(Restrictions.eq("subStatus", subStatus));
		return (AppFlow) c.uniqueResult();
	}

	@Autowired
	private WrkFlwMngDAO wrkFlwMngDAO;

	public List<AppFlow> getAllOrder(Class<?> modelClass) throws ResourceException {
		WrkFlwMng flwMng = (WrkFlwMng) getCurrentSession().createCriteria(WrkFlwMng.class)
				.add(Restrictions.eq("caseType", modelClass.getCanonicalName())).uniqueResult();
		if (flwMng == null)
			throw new ResourceException("Not exists workflow for the case type");
		return getCurrentSession().createCriteria(AppFlow.class).add(Restrictions.eq("wrkFlwMng", flwMng))
				.addOrder(Order.asc("status")).addOrder(Order.asc("subStatus")).list();
	}

}