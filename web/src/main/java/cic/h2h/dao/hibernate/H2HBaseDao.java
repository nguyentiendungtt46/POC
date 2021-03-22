package cic.h2h.dao.hibernate; 

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import frwk.dao.hibernate.BaseDao;

@Repository(value = "h2hBaseDao")
public class H2HBaseDao<T> extends BaseDao<T> {

	@Autowired
	@Qualifier("sessionFactory")
	private SessionFactory sessionFactory;

	@Override
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
