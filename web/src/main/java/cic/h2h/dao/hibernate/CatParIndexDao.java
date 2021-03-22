package cic.h2h.dao.hibernate;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.CatParIndex;

@Repository(value = "entParIndexDao")
public class CatParIndexDao extends H2HBaseDao<CatParIndex> {
	private static final Logger lg = LogManager.getLogger(CatParIndexDao.class);

	@SuppressWarnings("unchecked")
	public JSONArray getTreeRoot(String parent, boolean showPrice) throws Exception{
		//ArrayList<CatParIndex> objs = (ArrayList<CatParIndex>)getThreadSession().createCriteria(CatParIndex.class).add(Restrictions.isNull("parent")).list();
		Criteria cr = getCurrentSession().createCriteria(CatParIndex.class);
		if (Formater.isNull(parent)) 
			cr.add(Restrictions.isNull("parent"));
		else
			cr.add(Restrictions.eq("parent", get(CatParIndex.class, parent)));
		ArrayList<CatParIndex> objs = (ArrayList<CatParIndex>) cr.list();
        JSONArray ja = new JSONArray();
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        for (CatParIndex item : objs) {
        	String txt = "";
        	JSONObject jo = new JSONObject();
            jo.put("id", item.getId());
            if(showPrice) txt = item.getCode() + " - " + item.getName() + (item.getPrice() == null ? "" : (" - " + formatter.format(item.getPrice()) + " VND"));
            else txt = item.getCode() + " - " + item.getName();
            jo.put("text", txt);
            jo.put("fullpath", item.getFullPath());
            jo.put("price", item.getPrice());
            if(item.getParent() == null) {
            	jo.put("parentId", "");
            }else {
            	jo.put("parentId", item.getParent().getId());
            	jo.put("state", "open");
            }
            if (item.getEntParIndexs() != null && item.getEntParIndexs().size() > 0) {
                jo.put("state", "closed");
                // TODO: Can xac dinh luon folder or not
            }
            
            ja.put(jo);
        }
            
        return ja;
	}
	
	public JSONArray CreateObjectsTreeNode(ArrayList<CatParIndex> list) throws Exception {
        JSONArray ja = new JSONArray();
        for (CatParIndex item : list) {
            JSONObject jo = CreateObjectsTreeChildenNode(item);
            ja.put(jo);
        }
        return ja;
    }
	
	public JSONObject CreateObjectsTreeChildenNode(CatParIndex obj) throws Exception {
        JSONObject jo = new JSONObject();
        jo.put("id", obj.getId());
        jo.put("text", obj.getCode() + " - " + obj.getName());
        if (obj.getEntParIndexs() != null && obj.getEntParIndexs().size() > 0) {
            jo.put("state", "closed");
            JSONArray ja = new JSONArray();
            for (CatParIndex sysChildenItem : obj.getEntParIndexs()) {
                JSONObject joChildren = new JSONObject();
                joChildren = CreateObjectsTreeChildenNode(sysChildenItem);
                if (joChildren.length() > 0) {
                    ja.put(joChildren);
                }
            }
            jo.put("children", ja);
        }
        return jo;
    }
	
	public CatParIndex getCatParIndexByCode(String code) {
		Criteria criteria = getCurrentSession().createCriteria(CatParIndex.class);
		criteria.add(Restrictions.eq("code", code));
		return (CatParIndex) criteria.uniqueResult();
	}
}
