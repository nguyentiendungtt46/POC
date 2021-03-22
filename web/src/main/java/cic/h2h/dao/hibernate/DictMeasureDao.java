package cic.h2h.dao.hibernate;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.json.JSONArray;
import org.springframework.stereotype.Repository;

import entity.DictMeasure;

@Repository(value = "dictMeasureDao")
public class DictMeasureDao extends H2HBaseDao<DictMeasure> {
	public List<DictMeasure> getAllDictMeasure() throws Exception {
		List<DictMeasure> lstResult = null;
		Session ss = openNewSession();
		Transaction tx = null;
		try {
			tx = ss.beginTransaction();
			Criteria c = ss.createCriteria(DictMeasure.class);
			c.addOrder(Order.asc("id"));
			lstResult = c.list();
			tx.commit();
		} catch (Exception ex) {
			if (tx != null)
				tx.rollback();
			throw ex;
		} finally {
			ss.close();
		}
		return lstResult;
	}

	public JSONArray getListOptions(String proName) {
		JSONArray listOptions = new JSONArray();
		/*
		 * Session ss = openNewSession(); SessionImpl ssImpl = (SessionImpl) ss;
		 * Connection conn = ssImpl.connection(); CallableStatement callableStatement =
		 * null; ResultSet rs = null;
		 * 
		 * try { String sqlQuery = "{Call " + proName + "(?)}"; callableStatement =
		 * conn.prepareCall(sqlQuery); callableStatement.registerOutParameter(1,
		 * OracleTypes.CURSOR); callableStatement.executeUpdate(); rs = (ResultSet)
		 * callableStatement.getObject(1); while (rs.next()) { JSONObject object = new
		 * JSONObject(); object.put("value", rs.getString("VALUE"));
		 * object.put("display", rs.getString("DISPLAY")); listOptions.put(object); } }
		 * catch (Exception ex) { logger.error("Loi", ex); } finally {
		 * DataSourceConfiguration.releaseSqlResources(rs, callableStatement, conn);
		 * ss.close(); }
		 */
        return listOptions;
    }

}
