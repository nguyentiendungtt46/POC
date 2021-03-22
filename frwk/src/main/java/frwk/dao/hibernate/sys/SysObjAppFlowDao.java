package frwk.dao.hibernate.sys;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import entity.SysObjAppFlow;

@Repository(value = "sysObjAppFlowDAO")
public class SysObjAppFlowDao extends SysDao<SysObjAppFlow> {
	private static final Logger logger = LogManager.getLogger(SysObjAppFlowDao.class);

}
