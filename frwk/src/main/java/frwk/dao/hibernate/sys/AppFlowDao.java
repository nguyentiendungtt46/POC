package frwk.dao.hibernate.sys;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import entity.AppFlow;

@Repository(value = "appFlowDAO")
public class AppFlowDao extends SysDao<AppFlow> {
	private static final Logger logger = LogManager.getLogger(SysObjectDao.class);
	

}
