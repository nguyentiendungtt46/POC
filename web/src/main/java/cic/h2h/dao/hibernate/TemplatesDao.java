package cic.h2h.dao.hibernate;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.util.Formater;
import common.util.ResourceException;
import entity.DictMeasure;
import entity.DictTemp;
import entity.Templates;
import entity.frwk.UserLog;
import frwk.utils.ApplicationContext;

@Repository(value = "templatesDao")
public class TemplatesDao extends H2HBaseDao<Templates> {
	private static final Logger lg = LogManager.getLogger(TemplatesDao.class);

	public Templates getByCode(String code) throws Exception {
		List<Templates> temmps = (List<Templates>) getThreadSession().createCriteria(Templates.class)
				.add(Restrictions.eq("code", code.trim())).list();
		if (temmps.size() > 0)
			return temmps.get(0);
		return null;

	}

	public Templates getById(Long sid) throws Exception {
		Templates rs = null;
		Session ss = openNewSession();
		Transaction tx = null;
		try {
			tx = ss.beginTransaction();
			rs = (Templates) ss.get(Templates.class, sid);
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

	public Templates getByIdInThreadSession(Long sid) throws Exception {

		return (Templates) getThreadSession().get(Templates.class, sid);

	}

	public void saveNumberId(Templates temp) throws Exception {
		Transaction tx = null;
		Session session = openNewSession();
		try {
			tx = session.beginTransaction();

			// Dictemp
			for (DictTemp dictemp : temp.getLstDictTemps()) {
				// Cap nhat DictMeasure
				DictMeasure oldMs = (DictMeasure) session.load(DictMeasure.class, dictemp.getDictMeasure().getId());
				oldMs.setPath(dictemp.getDictMeasure().getPath() != null ? dictemp.getDictMeasure().getPath().trim()
						: dictemp.getDictMeasure().getPath());
				session.merge(oldMs);

				// Cap nhat DictTemp
				if (Formater.isNull(dictemp.getId())) {
					dictemp.setDictMeasure(oldMs);
					if (dictemp.getDictType() != null && Formater.isNull(dictemp.getDictType().getId()))
						dictemp.setDictType(null);
					dictemp.setId(null);
					session.save(dictemp);
				} else {
					DictTemp oldDictTemp = (DictTemp) session.load(DictTemp.class, dictemp.getId());
					oldDictTemp.setDictMeasure(oldMs);
					oldDictTemp.setColumnPosition(dictemp.getColumnPosition());
					oldDictTemp.setGroupName(
							dictemp.getGroupName() != null ? dictemp.getGroupName().trim() : dictemp.getGroupName());
					oldDictTemp.setGrpLevel(dictemp.getGrpLevel());
					if (dictemp.getDictType() != null && !Formater.isNull(dictemp.getDictType().getId()))
						oldDictTemp.setDictType(dictemp.getDictType());
					else
						oldDictTemp.setDictType(null);
					oldDictTemp.setCtrType(dictemp.getCtrType());
					session.merge(oldDictTemp);
				}
			}

			if (!Formater.isNull(temp.getId())) {
				Templates oldTemp = (Templates) session.load(Templates.class, temp.getId());

				// Xoa bo dictemp khong ton tai
				// Set<DictTemp> dels = new HashSet()<>(0);
				List<DictTemp> dels = new ArrayList<DictTemp>();
				for (DictTemp oldDictTemp : oldTemp.getLstDictTemps()) {
					boolean delOld = true;
					for (DictTemp currentDictTemp : temp.getLstDictTemps()) {
						if (oldDictTemp.getId().equals(currentDictTemp.getId())) {
							delOld = false;
							break;
						}

					}
					if (delOld)
						dels.add(oldDictTemp);
				}
				// deleted object would be re-saved by cascade (remove deleted object from
				// associations)
				oldTemp.getLstDictTemps().removeAll(dels);
				for (DictTemp dt : dels)
					session.delete(dt);

				for (DictTemp currentDictTemp : temp.getLstDictTemps()) {
					if (currentDictTemp.getTemplates()== null) {
						currentDictTemp.setTemplates(temp);
					}
				}
				session.merge(temp);
			} else {
				for (DictTemp currentDictTemp : temp.getLstDictTemps()) {
					if (currentDictTemp.getTemplates()== null) {
						currentDictTemp.setTemplates(temp);
					}
				}
				session.save(temp);
			}

			tx.commit();
		} catch (Exception ex) {
			lg.error("LoggerFactory.LOG_EROR", ex);
			if (tx != null)
				tx.rollback();
			throw ex;

		} finally {
			session.close();
		}

	}

	public void updateTemp(Templates template) throws Exception {
		Session session = getThreadSession();
		session.merge(template);
	}

	public void saveNumberId(Object object) throws Exception {
		// javax.transaction.UserTransaction tx1 =
		// (javax.transaction.UserTransaction)new
		// javax.naming.InitialContext().lookup("java:comp/UserTransaction");
		// tx1.setRollbackOnly();
		// tx1.getRollbackOnly();

		Session session = openNewSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Templates temp = (Templates) object;
			String id = temp.getId();
			if (!Formater.isNull(id)) {
				try {
					lg.info("Begin update");
					Templates oldInDb = (Templates) session.get(temp.getClass(), id);
					StringWriter writer = new StringWriter();
					lg.info("Thong tin ban ghi cu: ");
					new ObjectMapper().writeValue(writer, oldInDb);
					lg.info(writer.toString());
					writer.close();

					// Delete quan he dictTemp ko ton tai
					Set<DictTemp> dels = new HashSet<DictTemp>(0);
					for (DictTemp dtOld : oldInDb.getLstDictTemps()) {
						boolean delOld = true;
						for (DictTemp cfNow : temp.getLstDictTemps()) {
							if (dtOld.getId().equals(cfNow.getId())) {
								delOld = false;
								break;
							}

						}
						if (delOld)
							dels.add(dtOld);
					}
					for (DictTemp dt : dels) {
						// deleted object would be re-saved by cascade (remove deleted object from
						// associations)
						oldInDb.getLstDictTemps().remove(dt);
						session.delete(dt);
					}

					// Delete quan he ProdTemp ko ton tai
//					Set<ProdTemp> delProdTemp = new HashSet<ProdTemp>(0);
//					for (ProdTemp ptOld : oldInDb.getProdTemps()) {
//						boolean delOld = true;
//						for (ProdTemp cfNow : temp.getProdTemps()) {
//							if (ptOld.getId().equals(cfNow.getId())) {
//								delOld = false;
//								break;
//							}
//						}
//						if (delOld)
//							delProdTemp.add(ptOld);
//					}
//					for (ProdTemp pt : delProdTemp) {
//						// deleted object would be re-saved by cascade (remove deleted object from
//						// associations)
//						boolean isDel = true;
//						if (temp.getIsAllProd()) {
//							if (!Formater.isNull(pt.getFastInput()) && pt.getFastInput()) {
//								isDel = false;
//							}
//						}
//
//						if (isDel) {
//							oldInDb.getProdTemps().remove(pt);
//							session.delete(pt);
//						}
//					}

				} catch (Exception ex) {
					lg.error("Loi", ex);
					throw ex;
				}

			} else {
				lg.info("Begin insert");
			}

			// Ghi log
			UserLog log = new UserLog();
//			ApplicationContext app = (ApplicationContext) ServletActionContext.getRequest().getSession()
//					.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
//			log.setUserId(((SysUsers) app.getAttribute(ApplicationContext.USER)).getUsername());
//
//			if (id != null) {
//				// inser update dictTemps
//				for (DictTemp dt : temp.getDictTemps()) {
//					if (Formater.isNull(dt.getId()))
//						session.save(dt);
//					else
//						session.merge(dt);
//				}
//
//				// inser update prodTemps
//				for (ProdTemp pt : temp.getProdTemps()) {
//					if (Formater.isNull(pt.getId()))
//						session.save(pt);
//					else
//						session.merge(pt);
//				}
//
//				session.merge(temp);
//				log.setAction("edit_" + temp.getClass().getName());
//
//			} else {
//				log.setAction("insert_" + temp.getClass().getName());
//				session.save(temp);
//			}
//			log.setRecordId(String.valueOf(temp.getId()));
//			StringWriter writer = new StringWriter();
//			lg.info("Thong tin ban ghi: ");
//			new ObjectMapper().writeValue(writer, object);
//			// log.setDetail(writer.toString());
//			writer.close();
//			lg.info(log.getDetail());

			session.save(log);
			tx.commit();

		} catch (Exception ex) {
			lg.error("LoggerFactory.LOG_EROR", ex);
			if (tx != null)
				tx.rollback();
			throw ex;

		} finally {
			session.close();
		}

	}

//	public static void check(Product product, String templateId) throws ResourceException {
//		Long lTempId = Long.parseLong(templateId);
//		Templates template = (Templates) getThreadSession().load(Templates.class, lTempId);
//		if (template == null)
//			throw new ResourceException("Bieu mau " + templateId + " khong ton tai");
//
//		ProdTemp tmp = (ProdTemp) getThreadSession().createCriteria(ProdTemp.class)
//				.add(Restrictions.eq("product", product)).add(Restrictions.eq("templates", template)).uniqueResult();
//		if (tmp == null)
//			throw new ResourceException(
//					"San pham " + product.getPrdNm() + ", Khong ton tai bieu mau " + template.getTempNm());
//
//	}
}
