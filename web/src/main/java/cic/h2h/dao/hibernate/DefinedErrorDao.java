package cic.h2h.dao.hibernate;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import entity.DefinedError;
import frwk.dao.hibernate.sys.SysDao;

@Repository(value = "errorPopupDao")
public class DefinedErrorDao extends SysDao<DefinedError> {
	static Logger lg = LogManager.getLogger(DefinedErrorDao.class);

}
