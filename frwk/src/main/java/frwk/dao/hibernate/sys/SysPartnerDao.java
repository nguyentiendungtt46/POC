package frwk.dao.hibernate.sys;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import entity.Partner;

@Repository(value = "sysPartnerDao")
public class SysPartnerDao extends SysDao<Partner> {

	@SuppressWarnings("unchecked")
	public List<Partner> getAll() {
		return getCurrentSession().createCriteria(Partner.class).list();
	}

	public Partner getPartnerByCode(String code) {
		Partner rs = null;
		Session ss = openNewSession();
		Transaction tx = ss.beginTransaction();
		try {
			Criteria criteria = ss.createCriteria(Partner.class);
			criteria.add(Restrictions.eq("code", code));
			rs = (Partner) criteria.uniqueResult();
		} catch (HibernateException e) {
			tx.rollback();
		} finally {
			ss.close();
		}
		return rs;
	}

}
