package cic.h2h.dao.hibernate;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import entity.LogCoreService;
import frwk.dao.hibernate.sys.SysDao;

@Repository(value = "logCoreServicesDao")
public class LogCoreServicesDao extends SysDao<LogCoreService> {
	static Logger lg = LogManager.getLogger(LogCoreServicesDao.class);
	
	public LogCoreService getLogCoreServiceById(String Id) throws Exception {
		return (LogCoreService) getThreadSession().get(LogCoreService.class, Id);
	}
}
