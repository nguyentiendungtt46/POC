package cic.h2h.dao.hibernate;

import java.math.BigDecimal;
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
import entity.RpFileUnstructured;
import frwk.dao.hibernate.sys.SysDao;

@Repository(value = "rpFileUnstructuredDao")
public class RpFileUnstructuredDao extends SysDao<RpFileUnstructured> {
	static Logger lg = LogManager.getLogger(RpFileUnstructuredDao.class);

	public List<RpFileUnstructured> reports(String partnerCode, String fileName, String cicOwner, String fromDate, String toDate) {
		// TODO Auto-generated method stub
		try {
			Criteria criteria = getCurrentSession().createCriteria(RpFileUnstructured.class);
			if (!Formater.isNull(fileName))
				criteria.add(Restrictions.like("fileName", fileName.trim(), MatchMode.ANYWHERE).ignoreCase());

			if (!Formater.isNull(partnerCode))
				criteria.add(Restrictions.like("tctdCode", partnerCode.trim(), MatchMode.ANYWHERE).ignoreCase());
			if (!Formater.isNull(cicOwner)) {
				criteria.add(Restrictions.eq("type", new BigDecimal(cicOwner)));
			}
			if (!Formater.isNull(fromDate)) {
				criteria.add(Restrictions.ge("createdDate", Formater.str2date(fromDate)));
			}
			if (!Formater.isNull(toDate)) {
				Date currentDate = Formater.str2date(toDate);
				Calendar c = Calendar.getInstance();
				c.setTime(currentDate);
				c.add(Calendar.DATE, 1);
				Date dateAdd = c.getTime();
				criteria.add(Restrictions.lt("createdDate", dateAdd));
			}
			criteria.addOrder(Order.desc("createdDate"));
			return criteria.list();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
