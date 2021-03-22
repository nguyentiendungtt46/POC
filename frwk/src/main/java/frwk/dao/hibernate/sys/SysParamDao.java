package frwk.dao.hibernate.sys;

import java.util.List;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.frwk.SysParam;

@Repository(value = "sysParamDao")
public class SysParamDao extends SysDao<SysParam> {
	private static final Logger logger = LogManager.getLogger(SysParamDao.class);

	public SysParam getById(String id) {

		return (SysParam) getThreadSession().get(SysParam.class, id);
	}

	public SysParam getSysParamByCode(String code) {
		Session session = openNewSession();
		Transaction tx = session.beginTransaction();
		SysParam sysParam = null;
		try {
			sysParam = (SysParam) session.createCriteria(SysParam.class).add(Restrictions.eq("code", code)).uniqueResult();
			tx.commit();
		} catch (Exception e) {
			tx.rollback();
		} finally {
			session.close();
		}
		return sysParam;
	}
	
	public SysParam getSysParamByCode2(String code) {
		Session ss = openNewSession();
		Transaction tx = null;
		SysParam sysParam = new SysParam();
		
		try {
			tx = ss.beginTransaction();
			Criteria c = ss.createCriteria(SysParam.class);
			c.add(Restrictions.eq("code", code));
			sysParam = (SysParam) c.uniqueResult();
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			throw ex;
		} finally {
			ss.close();
		}
		return sysParam;
	}

	public List<SysParam> getByType(String dictType) {
		return (List<SysParam>) getThreadSession().createCriteria(SysParam.class)
				.add(Restrictions.eq("code", dictType)).list();
	}

}