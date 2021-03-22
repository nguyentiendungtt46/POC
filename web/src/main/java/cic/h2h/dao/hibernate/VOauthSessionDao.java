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
import entity.VOauthSession;
import frwk.dao.hibernate.sys.SysDao;

@Repository(value = "vOauthSessionDao")
public class VOauthSessionDao extends SysDao<VOauthSession> {
	static Logger lg = LogManager.getLogger(VOauthSessionDao.class);

	public List<VOauthSession> reports(String userName) {
		// TODO Auto-generated method stub
		List<VOauthSession> list = new ArrayList<VOauthSession>();
		try {
			Criteria criteria = getCurrentSession().createCriteria(VOauthSession.class);
			if (!Formater.isNull(userName))
				criteria.add(Restrictions.like("userName", userName, MatchMode.ANYWHERE).ignoreCase());
			list = criteria.list();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e.getMessage(),e);
		}
		return list;
	}

}
