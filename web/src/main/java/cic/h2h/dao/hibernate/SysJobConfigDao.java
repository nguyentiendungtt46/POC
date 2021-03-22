package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.SysJobConfig;

@Repository(value = "sysJobConfigDao")
public class SysJobConfigDao extends H2HBaseDao<SysJobConfig> {

	private static final Logger log = LogManager.getLogger(SysJobConfigDao.class);

	
	public List<SysJobConfig> reports(String code, String name) {
		List<SysJobConfig> configs = new ArrayList<SysJobConfig>();
		try {
			Criteria criteria = getCurrentSession().createCriteria(SysJobConfig.class);
			if (!Formater.isNull(code)) {
				criteria.add(Restrictions.like("jobCode", code, MatchMode.ANYWHERE).ignoreCase());
			}
			if (!Formater.isNull(name)) {
				criteria.add(Restrictions.like("jobName", name, MatchMode.ANYWHERE).ignoreCase());
			}
			configs = criteria.list();
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e.getMessage(),e);
		}
		return configs;
	}
}
