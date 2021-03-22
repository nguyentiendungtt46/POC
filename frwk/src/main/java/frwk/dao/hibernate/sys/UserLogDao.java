package frwk.dao.hibernate.sys;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.frwk.SysUsersLog;
import entity.frwk.UserLog;

@Repository(value = "userLogDao")
public class UserLogDao extends SysDao<SysUsersLog> {
	static Logger lg = LogManager.getLogger(UserLogDao.class);

	public void writeLoginLog(UserLog lg) {
		Date date = new Date();
		lg.setModifyTime(date);
		getCurrentSession().save(lg);

	}
	
	public List<SysUsersLog> findByRecordid(String recordId) {
		List<SysUsersLog> logs = new ArrayList<SysUsersLog>();
		try {
			Criteria criteria = getCurrentSession().createCriteria(SysUsersLog.class);
			criteria.add(Restrictions.eq("recordId", recordId));
			criteria.addOrder(Order.desc("modifyTime"));
			logs = criteria.list();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e.getMessage(), e);
			return null;
		}
		return logs;
	}

	public List<SysUsersLog> reports(String tuNgay, String denNgay, String catelogy, String username, String obj) {
		// TODO Auto-generated method stub
		List<SysUsersLog> logs = new ArrayList<SysUsersLog>();
		try {
			Criteria c = getCurrentSession().createCriteria(SysUsersLog.class);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			if (!Formater.isNull(tuNgay)) {
				try {
					c.add(Restrictions.ge("modifyTime", df.parse(tuNgay)));
				} catch (ParseException e) {
					lg.error(e);
				}
			}
			if (!Formater.isNull(denNgay)) {

				try {
					Calendar ca = Calendar.getInstance();
					ca.setTime(df.parse(denNgay));
					ca.add(Calendar.DATE, 1);
					c.add(Restrictions.le("modifyTime", ca.getTime()));
				} catch (ParseException e) {
					lg.error(e);
				}
			}
			// Search by username
			if (!Formater.isNull(username)) {
				c.add(Restrictions.like("userId", username.trim(), MatchMode.ANYWHERE).ignoreCase());
			}
			// Search by keyWord
			if (!Formater.isNull(obj)) {
				c.add(Restrictions.like("recordId", obj.trim(), MatchMode.ANYWHERE).ignoreCase());
			}
			// Search by action
			if (!Formater.isNull(catelogy)) {
				// like because edit_entity.XXXX_$$javasiss
				c.add(Restrictions
						.like("action", catelogy.trim(), MatchMode.ANYWHERE).ignoreCase());
				if ("entity.frwk.SysUsers".equals(catelogy)) {
					if (!Formater.isNull(catelogy)) {
						String sql = "exists (select 1 from sys_users u where u.id = \"RECORD ID\" and upper(u.username) like '%"
								+ StringEscapeUtils.escapeSql(catelogy.trim()).toUpperCase() + "%')";
						c.add(Restrictions.sqlRestriction(sql));
					}
				}
			}
			c.addOrder(Order.desc("modifyTime"));
			logs = c.list();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e.getMessage(),e);
		}
		return logs;
	}
}
