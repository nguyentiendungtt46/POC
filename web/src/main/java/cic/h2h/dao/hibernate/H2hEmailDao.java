package cic.h2h.dao.hibernate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.H2hEmail;
import frwk.dao.hibernate.sys.SysDao;

@Repository(value = "h2hEmailDao")
public class H2hEmailDao extends SysDao<H2hEmail> {
	static Logger lg = LogManager.getLogger(H2hEmailDao.class);

	public H2hEmail getById(String id) throws Exception {
		return (H2hEmail) getThreadSession().get(H2hEmail.class, id);
	}

	public List<H2hEmail> reports(String fromDate, String toDate, String partnerId, String email, String type,
			String status) {
		// TODO Auto-generated method stub
		List<H2hEmail> list = new ArrayList<H2hEmail>();
		try {
			Criteria c = getCurrentSession().createCriteria(H2hEmail.class);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			if (!Formater.isNull(fromDate)) {
				c.add(Restrictions.ge("sendTime", df.parse(fromDate)));
			}
			if (!Formater.isNull(toDate)) {
				Date currentDate = df.parse(toDate);
				Calendar cl = Calendar.getInstance();
				cl.setTime(currentDate);
				cl.add(Calendar.DATE, 1);
				Date dateAdd = cl.getTime();
				c.add(Restrictions.le("sendTime", dateAdd));
			}

			// Search by trang thai
			if (!Formater.isNull(status))
				c.add(Restrictions.eq("status", Short.valueOf(status)));

			if (!Formater.isNull(type))
				c.add(Restrictions.eq("type", Short.valueOf(type)));

			if (!Formater.isNull(email))
				c.add(Restrictions.like("emailTo", email.trim(), MatchMode.ANYWHERE).ignoreCase());

			if (!Formater.isNull(partnerId))
				c.add(Restrictions.eq("tctdId", partnerId));
			c.addOrder(Order.desc("createTime"));
			list = c.list();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e.getMessage(), e);
		}
		return list;
	}

}
