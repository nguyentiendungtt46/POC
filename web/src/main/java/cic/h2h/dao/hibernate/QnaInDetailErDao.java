package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import entity.QnaInDetailEr;
import frwk.dao.hibernate.sys.SysDao;

@Repository(value = "qnaInDetailErDao")
public class QnaInDetailErDao extends SysDao<QnaInDetailEr> {
	
	static Logger lg = LogManager.getLogger(QnaInDetailErDao.class);
	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public List<QnaInDetailEr> findByDetailsId(String id) {
		List<QnaInDetailEr> lst = new ArrayList<QnaInDetailEr>();
		try {
			
			Criteria c = getCurrentSession().createCriteria(QnaInDetailEr.class);
			c.add(Restrictions.eq("qnaInDetailId.id", id));
			lst = c.list();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e.getMessage(),e);
			return null;
		}
		return lst;
	}
}
