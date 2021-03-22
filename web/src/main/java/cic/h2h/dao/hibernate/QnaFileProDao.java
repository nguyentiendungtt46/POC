package cic.h2h.dao.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import entity.QnaFilePro;
import frwk.dao.hibernate.sys.SysDao;

@Repository(value = "qnaFileProDao")
public class QnaFileProDao extends SysDao<QnaFilePro> {
	static Logger lg = LogManager.getLogger(QnaFileProDao.class);

}
