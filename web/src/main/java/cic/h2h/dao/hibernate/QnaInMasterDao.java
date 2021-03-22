package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import common.util.Formater;
import entity.QnaInDetail;
import entity.QnaInMaster;
import frwk.dao.hibernate.sys.SysDao;
import vn.org.cic.h2h.ws.endpoint.cicqaprod.DongKQ;

@Repository(value = "qnaInMasterDao")
public class QnaInMasterDao extends SysDao<QnaInMaster> {
	static Logger lg = LogManager.getLogger(QnaInMasterDao.class);

	public QnaInMaster getQnaInMasterByMalo(String mabantin) throws Exception {
		return (QnaInMaster) getThreadSession().createCriteria(QnaInMaster.class)
				.add(Restrictions.eq("mabantin", mabantin));
	}

	public void save(QnaInMaster ms) {
		getCurrentSession().save(ms);
	}

	public void updateSts(QnaInMaster r) {
		// Da tra loi doi tac
		Criteria c = getThreadSession().createCriteria(QnaInDetail.class);
		c.add(Restrictions.eq("qnaInMsId.id", r.getId()));
		c.add(Restrictions.isNotNull("thoidiemtraloidoitac"));
		r.setDaTra((Long) c.setProjection(Projections.rowCount()).uniqueResult());

		// Da co ket qua
		c = getThreadSession().createCriteria(QnaInDetail.class);
		c.add(Restrictions.eq("qnaInMsId.id", r.getId()));
		c.add(Restrictions.isNotNull("noidungtraloi"));
		r.setDaCoKetQua((Long) c.setProjection(Projections.rowCount()).uniqueResult());

		if (r.getTongSoPhieu() == null) {
			// Chua co ket qua
			c = getThreadSession().createCriteria(QnaInDetail.class);
			c.add(Restrictions.eq("qnaInMsId.id", r.getId()));
			c.add(Restrictions.isNull("noidungtraloi"));
			r.setChuaCoKetQua((Long) c.setProjection(Projections.rowCount()).uniqueResult());
			r.setTongSoPhieu(r.getChuaCoKetQua() + r.getDaCoKetQua());
		}

		// Chua xu ly
		if (r.getDaCoKetQua().intValue() == 0)
			r.setStatus(Short.valueOf("0"));
		// Hoan thanh
		else if (r.getDaTra().intValue() == r.getTongSoPhieu())
			r.setStatus(Short.valueOf("2"));
		// Da co ket qua
		else
			r.setStatus(Short.valueOf("1"));

	}

	public void redo(String recordId) {
		QnaInMaster ms = this.getObject(QnaInMaster.class, recordId);
		ms.setRetryTime(Short.valueOf("0"));
		getCurrentSession().update(ms);
	}

	public void sendCic(String recordId) {
		QnaInMaster ms = this.getObject(QnaInMaster.class, recordId);
		ms.setStatus(Short.valueOf("2"));
//		for(QnaInDetail d : ms.getQnaInDetails()) {
//			d.setStatus(Short.valueOf("2"));
//		}
		getCurrentSession().update(ms);
	}

	@SuppressWarnings("unchecked")
	public JSONArray getTreeRoot(String id) throws Exception, IllegalArgumentException, IllegalAccessException {
		JSONArray jsonResult = new JSONArray();
		QnaInMaster obj = get(QnaInMaster.class, id);
		ObjectMapper mapper = new ObjectMapper();
		Object o = mapper.readValue(obj.getCusErrorDescription(), Object.class);
		JSONObject jsonObject = new JSONObject(mapper.writeValueAsString(o));
		Gson gson = new Gson();
		JSONArray jsonArr = jsonObject.getJSONArray("dongKQ");
		Type listType = new TypeToken<List<DongKQ>>() {
		}.getType();
		List<DongKQ> listDongKQ = gson.fromJson(jsonArr.toString(), listType);
		JSONObject root = new JSONObject();
		root.put("id", "-1");
		root.put("text", "dongKQ");
		for (DongKQ dongKQ : listDongKQ) {
			java.lang.reflect.Field[] fs = DongKQ.class.getDeclaredFields();
			JSONArray jsonArrChil = new JSONArray();
			for (Field f : fs) {
				// make the attribute accessible if it's a private one
				f.setAccessible(true);

				// Get the value of the attibute of the instance received as parameter
				// Object value = f.get(DongKQ.class);
				// if(value == null) {
				JSONObject chilOne = new JSONObject();
				chilOne.put("id", UUID.randomUUID());
				chilOne.put("text", f.getName() + " :");
				chilOne.put("parentId", "-1");
				jsonArrChil.put(chilOne);
				// }
			}
			root.put("children", jsonArrChil);

		}
		jsonResult.put(root);

		return jsonResult;
	}

	public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
		Map<String, Object> retMap = new HashMap<String, Object>();

		if (json != JSONObject.NULL) {
			retMap = toMap(json);
		}
		return retMap;
	}

	public static Map<String, Object> toMap(JSONObject object) throws JSONException {
		Map<String, Object> map = new HashMap<String, Object>();

		Iterator<String> keysItr = object.keys();
		while (keysItr.hasNext()) {
			String key = keysItr.next();
			Object value = object.get(key);

			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			// System.out.println(key+"::"+ value);
			map.put(key, value);
		}
		return map;
	}

	public static List<Object> toList(JSONArray array) throws JSONException {
		List<Object> list = new ArrayList<Object>();
		for (int i = 0; i < array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				value = toList((JSONArray) value);
			}

			else if (value instanceof JSONObject) {
				value = toMap((JSONObject) value);
			}
			list.add(value);
		}
		return list;
	}

	public List<QnaInMaster> reports(String tuNgay, String denNgay, String partnerCode, String productType,
			String maSoPhieu, String hasResult, String username, String hasRealTimeError) {
		List<QnaInMaster> masters = new ArrayList<QnaInMaster>();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Criteria criteria = getCurrentSession().createCriteria(QnaInMaster.class);
			if (!Formater.isNull(tuNgay)) {
				criteria.add(Restrictions.ge("thoigianyc", df.parse(tuNgay)));
			}
			if (!Formater.isNull(denNgay)) {
				Date currentDate = df.parse(denNgay);
				Calendar c = Calendar.getInstance();
				c.setTime(currentDate);
				c.add(Calendar.DATE, 1);
				Date dateAdd = c.getTime();
				criteria.add(Restrictions.le("thoigianyc", dateAdd));
			}
			if (!Formater.isNull(productType))
				criteria.add(Restrictions.eq("loaisp", productType));
			if (!Formater.isNull(partnerCode))
				criteria.add(Restrictions.eq("matochuctindung.code", partnerCode));
			if (!Formater.isNull(maSoPhieu)) {
				criteria.add(Restrictions.sqlRestriction(
						"exists (select 1 from QNA_IN_OUT_DETAIL d where d.QNA_IN_MS_ID = {alias}.id and lower(d.MSPHIEU) like lower(?) escape '!')",
						"%" + maSoPhieu.trim().replace("_", "!_") + "%", StringType.INSTANCE));
			}

			if (!Formater.isNull(hasResult) && "1".equals(hasResult)) {
				criteria.add(Restrictions.sqlRestriction(
						"exists (select 1 from QNA_IN_OUT_DETAIL d where d.QNA_IN_MS_ID = {alias}.id and NGAYTRALOI is not null)"));
			}

			if (!Formater.isNull(hasRealTimeError) && "1".equals(hasRealTimeError)) {
				criteria.add(Restrictions.isNotNull("numOfErrorCus"));
				criteria.add(Restrictions.ge("numOfErrorCus", Long.valueOf(1l)));
			}

			if (!Formater.isNull(username)) {
				criteria.add(Restrictions.eq("userid", username));
			}
			criteria.addOrder(Order.desc("thoigianyc"));
			masters = criteria.list();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e.getMessage(), e);
		}
		return masters;
	}

	public boolean isJSONValid(String test) {
		try {
			new JSONObject(test);
		} catch (JSONException ex) {
			// edited, to include @Arthur's comment
			// e.g. in case JSONArray is valid as well...
			try {
				new JSONArray(test);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}
}
