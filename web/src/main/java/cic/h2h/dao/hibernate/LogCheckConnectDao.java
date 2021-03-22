package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.LogCheckConnect;
import frwk.dao.hibernate.sys.SysDao;

@Repository(value = "logCheckConnect")
public class LogCheckConnectDao extends SysDao<LogCheckConnect> {
	static Logger lg = LogManager.getLogger(LogCheckConnectDao.class);

	
	public List<LogCheckConnect> reports(String partnerId, String status, String ipAdress) {
		List<LogCheckConnect> list = new ArrayList<LogCheckConnect>();
		try {
			Criteria criteria = getCurrentSession().createCriteria(LogCheckConnect.class);
			if (!Formater.isNull(partnerId))
				criteria.add(Restrictions.eq("partnerId.id", partnerId));
			
			// Search by trang thai
			if (!Formater.isNull(status))
				criteria.add(Restrictions.eq("connectResult", Short.valueOf(status)));
			
			if (!Formater.isNull(ipAdress))
				criteria.add(Restrictions.like("ipAddress", ipAdress.trim(), MatchMode.ANYWHERE).ignoreCase());
			criteria.addOrder(Order.desc("createDate"));
			list = criteria.list();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e.getMessage(), e);
		}
		return list;
	}
}
