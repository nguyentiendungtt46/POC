package frwk.dao.hibernate.sys;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.SysUsers;
import entity.frwk.UserLog;
import entity.frwk.Company;
import entity.frwk.SysRoles;
import frwk.utils.ApplicationContext;

import java.io.StringWriter;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
@Repository(value = "companyDao")
public class CompanyDao extends SysDao<Company> {

	private static final Logger logger = LogManager.getLogger(CompanyDao.class);

	public List<Company> getChild(String parentId) throws ResourceException, Exception {
		if (Formater.isNull(parentId))
			throw new ResourceException("parentId khong ton tai");
		ArrayList<Company> rs = new ArrayList<Company>();
		Session ss = openNewSession();
		Transaction tx = null;
		try {
			tx = ss.beginTransaction();
			Criteria c = ss.createCriteria(Company.class);
			c.add(Restrictions.eq("parent.id", parentId));
			rs = (ArrayList<Company>) c.list();
			tx.commit();

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			throw ex;
		} finally {
			ss.close();
		}
		return rs;
	}

	public Company getById(String id) throws Exception {
		return (Company) getThreadSession().get(Company.class, id);
	}

	public Company getCompanyRoot() throws Exception {
		Company rs = null;
		ArrayList<Company> lrs = new ArrayList<Company>();
		Session ss = openNewSession();
		Transaction tx = null;
		try {
			tx = ss.beginTransaction();
			Criteria c = ss.createCriteria(Company.class);
			c.add(Restrictions.eq("path", "1"));
			lrs = (ArrayList<Company>) c.list();
			if (lrs != null && lrs.size() > 0)
				rs = lrs.get(0);
			tx.commit();
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			throw ex;
		} finally {
			ss.close();
		}
		return rs;
	}

	public ArrayList<Company> get(Company parent, Byte level) throws Exception {
		ArrayList<Company> rs = new ArrayList<Company>();
		Session ss = openNewSession();
		Transaction tx = null;
		try {
			tx = ss.beginTransaction();
			Criteria c = ss.createCriteria(Company.class);
			if (parent != null)
				c.add(Restrictions.eq("parent", parent));
			if (level != null && level.intValue() != 0)
				c.add(Restrictions.eq("cqlevel", level));
			c.addOrder(Order.asc("path"));
			rs = (ArrayList<Company>) c.list();
			tx.commit();

		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			throw ex;
		} finally {
			ss.close();
		}
		return rs;
	}

	public byte getMaxLevel() throws Exception {
		String sql = "select max(cqlevel) from company";
		Session ss = openNewSession();
		Transaction tx = null;
		try {
			tx = ss.beginTransaction();
			SQLQuery query = ss.createSQLQuery(sql);
			Object o = query.uniqueResult();
			tx.commit();
			if (o == null)
				return 0;
			return ((BigDecimal) o).byteValue();
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			throw ex;
		} finally {
			ss.close();
		}
	}

	public List<Company> getAllBranch() throws Exception {
		return getThreadSession().createCriteria(Company.class).add(Restrictions.isNull("parent"))
				.addOrder(Order.asc("code")).list();		
	}

	public ArrayList<Company> getAll() {
		ArrayList<Company> arrRet = new ArrayList<Company>();
		Session s = openNewSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			Criteria crit = s.createCriteria(Company.class);
			arrRet = (ArrayList<Company>) crit.list();
			tx.commit();
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
		} finally {
			s.close();
		}
		return arrRet;
	}

	public void save(Company company) throws Exception {
		Session session = openNewSession();
		Transaction tx = null;
		Company oldInDb = null;
		try {
			tx = session.beginTransaction();
			if (!Formater.isNull(company.getId())) {
				try {
					logger.info("Begin update");
					oldInDb = (Company) session.get(Company.class, company.getId());
					StringWriter writer = new StringWriter();
					logger.info("Thong tin ban ghi cu: ");
					new ObjectMapper().writeValue(writer, oldInDb);
					logger.info(writer.toString());
				} catch (Exception ex) {
					logger.error("Loi", ex);
					throw ex;
				}

			} else {
				logger.info("Begin insert");
			}

			// Ghi log
			UserLog log = new UserLog();
			log.setUserId(
					Formater.isNull(company.getId()) ? company.getCreator().getId() : company.getModifier().getId());
			if (!Formater.isNull(company.getId())) {
				if (oldInDb != null && company.getParentId() != null) {
					validateParent(oldInDb, company.getParentId());
				}
				session.merge(company);
				log.setAction("edit_" + company.getClass().getName());
			} else {
				log.setAction("insert_" + company.getClass().getName());

				session.save(company);
			}
			log.setRecordId(company.getId());
			session.save(log);

			// Gen lai path
			session.flush();
			Query exQuery = session.createSQLQuery("CALL sys_admin.updatePath()");
			exQuery.executeUpdate();

			tx.commit();

			StringWriter writer = new StringWriter();
			logger.info("Thong tin ban ghi: ");
			new ObjectMapper().writeValue(writer, company);
			logger.info(writer.toString());

		} catch (Exception ex) {
			logger.error(ex);
			if (tx != null)
				tx.rollback();
			throw ex;

		} finally {
			session.close();
		}

	}

	private void validateParent(Company oldInDb, String parentId) throws Exception {
		// Kt cq cha trung voi cq con
		if (oldInDb.getId().equals(parentId))
			throw new ResourceException("chon_cq_cha_trung_voi_minh_hoac_cq_con_chau");
		// Kiem tra cq cha trung voi cq con chau
		List<Company> children = null;
		Session ss = openNewSession();
		Transaction tx = null;
		try {
			tx = ss.beginTransaction();
			Criteria c = ss.createCriteria(Company.class);
			c.add(Restrictions.eq("parent", oldInDb));
			children = c.list();
			tx.commit();
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			throw ex;
		} finally {
			ss.close();
		}
		if (Formater.isNull(children))
			return;
		for (Company child : children)
			validateParent(child, parentId);
	}

	public Company getByCode(String code) throws ResourceException {
		List temp = getThreadSession().createCriteria(Company.class).add(Restrictions.eq("code", code)).list();
		if (Formater.isNull(temp))
			return null;
		if (temp.size() > 1)
			throw new ResourceException(String.format("hai_don_vi_co_cung_ma %s", code));
		return (Company) temp.get(0);
	}
}
