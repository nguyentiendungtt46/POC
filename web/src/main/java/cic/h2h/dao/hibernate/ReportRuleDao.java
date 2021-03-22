package cic.h2h.dao.hibernate;

import java.util.List;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import entity.RpRule;

@Repository(value = "reportRuleDao")
public class ReportRuleDao extends H2HBaseDao<RpRule>{
	
	private static final Logger log = LogManager.getLogger(ReportRuleDao.class);
	
	public List<RpRule> getAll() throws Exception {
		return getThreadSession().createCriteria(RpRule.class).list();		
	}

}
