package cic.h2h.dao.hibernate;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import entity.LogCoreServicesDetail;
import frwk.dao.hibernate.sys.SysDao;

@Repository(value = "logCoreServicesDetailDao")
public class LogCoreServicesDetailDao extends SysDao<LogCoreServicesDetail> {
	static Logger lg = LogManager.getLogger(LogCoreServicesDetailDao.class);
	
	public LogCoreServicesDetail getLogCoreServicesDetailById(String Id) throws Exception {
		return (LogCoreServicesDetail) getThreadSession().get(LogCoreServicesDetail.class, Id);
	}
}
