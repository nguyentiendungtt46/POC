package frwk.dao.hibernate;

import java.io.Serializable;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.management.IntrospectionException;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.persister.collection.OneToManyPersister;
import org.hibernate.persister.entity.AbstractEntityPersister;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.hibernate.type.IdentifierType;
import org.hibernate.type.Type;
import org.jfree.util.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.util.Formater;
import common.util.ResourceException;
import common.util.Util;
import entity.frwk.SysUsers;
import entity.frwk.UserLog;
import frwk.controller.SearchParam;
import frwk.utils.ApplicationContext;

public abstract class BaseDao<T> {

	private static final Logger logger = LogManager.getLogger(BaseDao.class);
	private static SessionFactory ssSessionFactory;

	@PostConstruct
	public void init() {
		ssSessionFactory = getSessionFactory();
	}

	public Session getThreadSession() {
		return getCurrentSession();
	}

	public Session openNewSession() {
		if (getSessionFactory() != null)
			return getSessionFactory().openSession();
		return ssSessionFactory.openSession();

	}

	public Session getCurrentSession() {
		Session ss = null;
		if (getSessionFactory() != null)
			ss = getSessionFactory().getCurrentSession();
		else
			ss = ssSessionFactory.getCurrentSession();
		if (!ss.getTransaction().getStatus().equals(TransactionStatus.ACTIVE))
			ss.beginTransaction();
		return ss;

	}

	@Autowired
	private HttpSession session;

	public SysUsers getSessionUser() {
		ApplicationContext appContext = (ApplicationContext) session
				.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
		return (SysUsers) appContext.getAttribute(ApplicationContext.USER);
	}

	@Qualifier("httpSession")
	public abstract SessionFactory getSessionFactory();

	public void save(T object) throws Exception {
		try {
			// ghi log
			UserLog log = new UserLog(getSessionUser());

			ClassMetadata m = getClassMetadata(object.getClass());
			Serializable id = m.getIdentifier(object, (SessionImplementor) getCurrentSession());

			if (id == null || "".equals(id) || "0".equals(id.toString())) {
				m.setIdentifier(object, null, (SessionImplementor) getCurrentSession());
				getCurrentSession().persist(object);
				log.setAction("insert_" + object.getClass().getName());

			} else {
				getCurrentSession().merge(object);
				log.setAction("edit_" + object.getClass().getName());
			}

			log.setRecordId(String.valueOf(getClassMetadata(object.getClass()).getIdentifier(object,
					(SessionImplementor) getCurrentSession())));
			StringWriter writer = new StringWriter();
			try {
				new ObjectMapper().writeValue(writer, object);
				log.setDetail(writer.toString());
				writer.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
			getCurrentSession().persist(log);
			getCurrentSession().flush();
		} catch (ConstraintViolationException e) {
			logger.error(e);
			if (e.getErrorCode() == 1)
				throw new ResourceException(messageSource.getMessage("ORA_0001", null, "Default", null));
			else if (e.getErrorCode() == 1400)
				throw new ResourceException(messageSource.getMessage("ORA_1400", null, "Default", null));
		}
	}

	public Serializable getId(Object object) {
		return getClassMetadata(object.getClass()).getIdentifier(object, (SessionImplementor) getCurrentSession());
	}

	public ClassMetadata getClassMetadata(Class<?> clss) {
		return getSessionFactory().getClassMetadata(clss);
	}

	@Autowired
	MessageSource messageSource;

	public void del(T entity) throws Exception {
		try {
			if (!getCurrentSession().contains(entity))
				load(entity);
			UserLog log = new UserLog(getSessionUser());
			log.setAction("del_" + entity.getClass().getName());
			Serializable identifier = getClassMetadata(entity.getClass()).getIdentifier(entity,
					(SessionImplementor) getCurrentSession());
			log.setRecordId(String.valueOf(identifier));

			StringWriter writer = new StringWriter();
			new ObjectMapper().writeValue(writer, entity);
			log.setDetail(writer.toString());
			writer.close();
			getCurrentSession().persist(log);
			getCurrentSession().delete(entity);
			getCurrentSession().flush();
		} catch (ConstraintViolationException ex) {
			logger.error(ex);
			Throwable ex1 = ex.getCause();
			if (ex1 instanceof SQLIntegrityConstraintViolationException) {
				SQLIntegrityConstraintViolationException ex2 = (SQLIntegrityConstraintViolationException) ex1;
				if (2292 == ex2.getErrorCode())
					throw new ResourceException(messageSource.getMessage("ORA_2292", null, "Default", null));
			}
		}

	}

	public T get(Class<T> c, Serializable recordId) {
		return getCurrentSession().get(c, recordId);
	}

	@SuppressWarnings("unchecked")
	public List<T> getAll(Class<T> clss) {
		return getCurrentSession().createCriteria(clss).list();
	}

	// Tim kiem

	private ArrayList<Criterion> restrictions = new ArrayList<Criterion>();
	private ArrayList<Order> orders = new ArrayList<Order>();

	public ArrayList<Criterion> getRestrictions() {
		return restrictions;
	}

	public void setRestrictions(ArrayList<Criterion> restrictions) {
		this.restrictions = restrictions;
	}

	public ArrayList<Order> getOrders() {
		return orders;
	}

	public void addRestriction(Criterion restriction) {
		restrictions.add(restriction);

	}

	public void addOrder(Order od) {
		orders.add(od);

	}

	public void clear() {
		getCurrentSession().clear();
	}

	@SuppressWarnings("unchecked")
	public ArrayList<T> search() {
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(getGenericClass());

		// Dieu kien tim kiem
		for (Criterion criterion : getRestrictions())
			criteria.add(criterion);

		// Tham so tim kiem
		if (getSearchParam() != null) {
			criteria.setFirstResult(getSearchParam().getBeginIndex().intValue());
			criteria.setMaxResults(getSearchParam().getPageSize());
		}

		// Order
		if (!Formater.isNull(orders)) {
			for (Order order : orders)
				criteria.addOrder(order);
		}

		return (ArrayList<T>) criteria.list();

	}

	public long count() throws Exception {
		Class<T> cls = getGenericClass();
		long iRs = 0;
		Session session = getCurrentSession();
		Criteria criteria = session.createCriteria(cls);
		// Dieu kien tim kiem
		for (Criterion criterion : this.getRestrictions())
			criteria.add(criterion);
		criteria.setProjection(Projections.rowCount());

		iRs = ((Long) criteria.uniqueResult()).intValue();
		return iRs;
	}

	private SearchParam searchParam;

	public SearchParam getSearchParam() {
		return searchParam;
	}

	public void setSearchParam(SearchParam searchParam) {
		this.searchParam = searchParam;
	}

	public T getObject(Class<T> clss, Serializable id) {
		return (T) getCurrentSession().get(clss, id);
	}

	/**
	 * Get object by thead session
	 * 
	 * 
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public T getObject(T o) throws Exception {
		if (o == null)
			throw new Exception("Object is null");
		ClassMetadata tmp = getClassMetadata(o.getClass());
		if (tmp == null)
			return o;
		Serializable identifier = tmp.getIdentifier(o, (SessionImplementor) getCurrentSession());
		return (T) getCurrentSession().get(o.getClass(), identifier);
	}

	public T load(Class<T> cls, Serializable id) throws Exception {
		return getCurrentSession().load(cls, id);
	}

	public void load(T o, Serializable id) throws Exception {
		getCurrentSession().load(o, id);
	}

	@SuppressWarnings("unchecked")
	private Class<T> getGenericClass() {
		ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
		return (Class<T>) type.getActualTypeArguments()[0];
	}

	public void endCurrentSession() {
		try {
			Session sess = getCurrentSession();
			if (sess.getTransaction().getStatus() == TransactionStatus.ACTIVE)
				sess.getTransaction().commit();

		} catch (Exception ex1) {
			logger.error(ex1);
			Session sess = getCurrentSession();
			if (sess.getTransaction().getStatus() == TransactionStatus.ACTIVE)
				sess.getTransaction().rollback();
			throw ex1;
		} finally {
			getCurrentSession().close();
		}
		getCurrentSession().beginTransaction();
	}

	public void load(T model) throws Exception {
		load(model, getId(model));

	}

	public String getIdentifierPropertyName(Class<?> cls) {
		ClassMetadata m = getClassMetadata(cls);
		IdentifierType t = (IdentifierType) m.getIdentifierType();
		return m.getIdentifierPropertyName();
	}
	
	public  Method getHashSetGeter(Class clsParent, Class clsChild)
			throws Exception {
		String propName = getHashSetRelationPropName(clsParent, clsChild);
		if (Formater.isNull(propName))
			return null;
		return Util.getGetterMethod(clsParent, clsParent.getDeclaredField(propName));
	}
	
	private  String getHashSetRelationPropName(Class clsParent, Class clsChild) {
		ClassMetadata classMetadata = getClassMetadata(clsParent);
		@SuppressWarnings("unchecked")
		Map<String, CollectionMetadata> allCollectionMetadata = getSessionFactory().getAllCollectionMetadata();

		// Retrieve all properties of the first class
		String[] propertyNames = classMetadata.getPropertyNames();

		// Loop through the retrieved properties
		for (String propName : propertyNames) {

			// Retrieve type of each property
			Type type = classMetadata.getPropertyType(propName.trim());

			// Check if the type is association type
			if (type.isAssociationType()) {

				// Check if it is collection type.
				if (type.isCollectionType()) {

					// From retrieved collection metadata (Strp 3) get value of the property we are
					// refering to.
					CollectionMetadata collectionMetadata = allCollectionMetadata
							.get(clsParent.getCanonicalName() + "." + propName);

					// Check if the elements of the collection are of desiered type
					if (collectionMetadata.getElementType().getReturnedClass().equals(clsChild)) {
						if (collectionMetadata instanceof OneToManyPersister) {
							return propName;
						}
					}

				}
			}
		}
		return null;
	}
	
	public String getTblName(ClassMetadata classMetadata) {
		AbstractEntityPersister persister = (AbstractEntityPersister) classMetadata;
		String fullTblName = persister.getTableName();
		int iBeginTblName = fullTblName.lastIndexOf(".");
		if (iBeginTblName >= 0)
			fullTblName = fullTblName.substring(iBeginTblName + 1);
		return fullTblName;
	}
	public Method getSetterParent(Class clsChild, Class clsParent) throws Exception {
        ClassMetadata hibernateClssChild = getClassMetadata(clsChild);
        String[] propNames = hibernateClssChild.getPropertyNames();
        for (String propName : propNames) {
            org.hibernate.type.Type propType = hibernateClssChild.getPropertyType(propName);
            if (propType.getReturnedClass().equals(clsParent)) {
                Field f = clsChild.getDeclaredField(propName);
                return Util.getSetterMethod(clsChild, f);
            }

        }
        return null;
    }
}
