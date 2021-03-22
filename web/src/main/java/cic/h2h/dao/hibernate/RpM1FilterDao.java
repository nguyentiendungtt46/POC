package cic.h2h.dao.hibernate;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import entity.RpM1Filter;
import frwk.dao.hibernate.sys.SysDao;

@Repository(value = "rpM1FilterDao")
public class RpM1FilterDao extends SysDao<RpM1Filter> {
	static Logger lg = LogManager.getLogger(RpM1FilterDao.class);

}
