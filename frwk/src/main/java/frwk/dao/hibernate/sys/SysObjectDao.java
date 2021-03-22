package frwk.dao.hibernate.sys;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.internal.SessionImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.sql.DataSourceConfiguration;
import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.Company;
import entity.frwk.SysObjects;
import entity.frwk.SysParam;
import entity.frwk.SysRights;
import entity.frwk.SysRoles;
import entity.frwk.SysUsers;
import entity.frwk.UserLog;

@Repository(value = "sysObjectDao")
public class SysObjectDao extends SysDao<SysObjects> {
	private static final Logger logger = LogManager.getLogger(SysObjectDao.class);

	public Object getTreeRight() throws Exception {

		java.util.List<SysObjects> temp = new ArrayList<SysObjects>();
		String sql = "select id, object_id,\n"
				+ "       rpad('-', 2 * (level - 1), '-') || '[' || object_id || ']' || name as name\n"
				+ "  from sys_objects\n" + " start with parent_id is null\n" + "connect by prior id = parent_id";
		Connection myConnect = null;
		Statement cStmt = null;
		ResultSet rs1 = null;
		try {
			myConnect = getConnection();
			// Goi thu tuc
			cStmt = myConnect.createStatement();
			rs1 = cStmt.executeQuery(sql);

			while (rs1.next()) {
				SysObjects so = new SysObjects();
				so.setId(rs1.getString(1));
				so.setObjectId(rs1.getString(2));
				so.setName(rs1.getString(3));
				temp.add(so);
			}
		} catch (Exception ex) {
			logger.error("Loi", ex);
			throw ex;
		} finally {
			DataSourceConfiguration.releaseSqlResources(rs1, cStmt, myConnect);

		}
		return temp;
	}

	private Connection getConnection() {
		return ((SessionImpl) openNewSession()).connection();
	}

	public ArrayList<SysObjects> getUserRight(SysUsers u) {
		ArrayList<SysObjects> sRs = null;
		String userName = StringEscapeUtils.escapeSql(u.getUsername());
		String sql = "select distinct o.*\n" + "  from sys_objects     o,\n" + "       sys_roles       rl,\n"
				+ "       sys_rights      rg,\n" + "       sys_users       u,\n" + "       sys_roles_users ru\n"
				+ " where o.id = rg.object_id\n" + "   and rg.role_id = rl.id\n" + "   and rl.id = ru.role_id\n"
				+ "   and ru.user_id = u.id\n" + "   and u.username = '" + userName + "'";
		Session s = getCurrentSession();
		SQLQuery query = s.createSQLQuery(sql).addEntity(SysObjects.class);
		sRs = (ArrayList<SysObjects>) query.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
		u.setLstObj(sRs);
		return sRs;
	}

	public ArrayList<SysObjects> getSysRight() {
		return (ArrayList<SysObjects>) getCurrentSession().createSQLQuery("select * from sys_objects")
				.addEntity(SysObjects.class).list();
	}

	public boolean checkCode(String code) {
		long count = (long) getThreadSession().createCriteria(SysObjects.class)
				.add(Restrictions.eq("objectId", code).ignoreCase()).setProjection(Projections.rowCount())
				.uniqueResult();
		return (count > 0 ? true : false);
	}

	public void checkUse(String parentId) throws ResourceException {
		long count = (long) getCurrentSession().createCriteria(SysObjects.class)
				.add(Restrictions.eq("sysObjects.id", parentId).ignoreCase()).setProjection(Projections.rowCount())
				.uniqueResult();
		if (count > 0) {
			throw new ResourceException(
					"Kh&#244;ng x&#243;a &#273;&#432;&#7907;c quy&#7873;n v&#236; c&#243; quy&#7873;n con");
		}
	}

	public void checkUseTreeRole(String objectId) throws ResourceException {
		long count = (long) getThreadSession().createCriteria(SysRights.class)
				.add(Restrictions.eq("sysObjects.id", objectId).ignoreCase()).setProjection(Projections.rowCount())
				.uniqueResult();
		if (count > 0) {
			throw new ResourceException(
					"Kh&#244;ng x&#243;a &#273;&#432;&#7907;c quy&#7873;n v&#236; &#273;&#227; c&#243; nh&#243;m quy&#7873;n s&#7917; d&#7909;ng");
		}
	}

	public void validateParent(SysObjects oldInDb, String parentId) throws Exception {
		// trung cha chon cha, con, chau
		if (oldInDb.getId().equals(parentId)) {
			throw new ResourceException(
					"Quy&#7873;n cha tr&#249;ng v&#7899;i m&#236;nh ho&#7863;c quy&#7873;n con, ch&#225;u");
		}
		// Kiem tra cq cha trung voi cq con chau
		List<SysObjects> children = null;
		Session ss = openNewSession();
		Transaction tx = null;
		try {
			tx = ss.beginTransaction();
			Criteria c = ss.createCriteria(SysObjects.class);
			c.add(Restrictions.eq("sysObjects", oldInDb));
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
		for (SysObjects child : children)
			validateParent(child, parentId);
	}

	public SysObjects getSysObjectsById(String id) {
		return (SysObjects) getThreadSession().get(SysObjects.class, id);
	}

	@Override
	public void save(SysObjects so) throws Exception {
		// Role co nhom quyen cha se co luon nhom quyen vua tao
		if (so.getSysObjects() != null) {
			if (Formater.isNull(so.getId())) {
				super.save(so);
				SysObjects parent = getCurrentSession().load(SysObjects.class, so.getSysObjects().getId());
				for (SysRights r : parent.getSysRightses()) {
					getCurrentSession().save(new SysRights(so, r.getSysRoles()));
				}
				return;
			}
		}
		// Them moi hoac khong ton tai cha
		if (so.getSysObjects() == null || Formater.isNull(so.getId())) {
			super.save(so);
			return;
		}
		SysObjects oldIndBd = this.getObject(so);
		if (so.getSysObjects().isOffSpring(oldIndBd))
			throw new ResourceException(
					"Quy&#7873;n cha kh&#244;ng &#273;&#432;&#7907;c tr&#249;ng v&#7899;i quy&#7873;n con ch&#225;u!");
		super.save(so);
	}

	public SysObjects getByObjId(String objId) {

		try {
			return (SysObjects) getCurrentSession().createCriteria(SysObjects.class)
					.add(Restrictions.eq("objectId", objId)).uniqueResult();
		} catch (Exception e) {
			// TODO: handle exception
			Session session = openNewSession();
			Transaction tx = session.beginTransaction();
			SysObjects sysParam = null;
			try {
				sysParam = (SysObjects) session.createCriteria(SysObjects.class).add(Restrictions.eq("objectId", objId))
						.uniqueResult();
				tx.commit();
			} catch (Exception e1) {
				tx.rollback();
			} finally {
				session.close();
			}
			return sysParam;
		}

	}

	@Autowired
	MessageSource messageSource;

	public void del1(SysObjects so) throws Exception {
		try {
			UserLog log = new UserLog();
			Date date = new Date();
			log.setUserId(getSessionUser().getUsername());
			log.setModifyTime(date);
			log.setAction("del_" + so.getClass().getName());
			log.setRecordId(String.valueOf(
					getClassMetadata(so.getClass()).getIdentifier(so, (SessionImplementor) getCurrentSession())));
			StringWriter writer = new StringWriter();
			new ObjectMapper().writeValue(writer, so);
			log.setDetail(writer.toString());
			writer.close();
			so = getCurrentSession().load(so.getClass(), so.getId());
			getCurrentSession().delete(so);
			getCurrentSession().save(log);
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
}
