package cic.h2h.dao.hibernate;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.CatProductCfg;

@Repository(value = "catProductCfgDao")
public class CatProductCfgDao extends H2HBaseDao<CatProductCfg> {
	
	private static final Logger lg = LogManager.getLogger(CatProductCfgDao.class);
	
	public boolean getCatProductCfgDaoByProductId(String id) {
		lg.info("BEGIN getCatProductCfgDaoByProductId id " + id);
		Criteria criteria = getCurrentSession().createCriteria(CatProductCfg.class);
		criteria.add(Restrictions.eq("indexId.id", id));
		lg.info("End getCatProductCfgDaoByProductId id " + id);
		if (Formater.isNull(criteria.list()))
			return true;
		return false;
	}
}
