package cic.h2h.dao.hibernate;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.CatAgencyStructure;

@Repository(value = "catAgencyStructureDao")
public class CatAgencyStructureDao extends H2HBaseDao<CatAgencyStructure> {
	private static final Logger lg = LogManager.getLogger(CatAgencyStructureDao.class);

	@SuppressWarnings("unchecked")
	public JSONArray getTreeRoot(String parent, boolean showPrice) throws Exception{
		Criteria cr = getCurrentSession().createCriteria(CatAgencyStructure.class);
		if (Formater.isNull(parent)) 
			cr.add(Restrictions.isNull("parent"));
		else
			cr.add(Restrictions.eq("parent", get(CatAgencyStructure.class, parent)));
		ArrayList<CatAgencyStructure> objs = (ArrayList<CatAgencyStructure>) cr.list();
        JSONArray ja = new JSONArray();
        for (CatAgencyStructure item : objs) {
        	JSONObject jo = new JSONObject();
            jo.put("id", item.getId());
            jo.put("text", item.getCode() + " - " + item.getName());
            jo.put("fullpath", item.getFullPath());
            if(item.getParent() == null) {
            	jo.put("parentId", "");
            }else {
            	jo.put("parentId", item.getParent().getId());
            	jo.put("state", "open");
            }
            if (item.getCatAgencyStructures() != null && item.getCatAgencyStructures().size() > 0) {
                jo.put("state", "closed");
            }
            
            ja.put(jo);
        }
            
        return ja;
	}
	
	public JSONArray CreateObjectsTreeNode(ArrayList<CatAgencyStructure> list, String state) throws Exception {
        JSONArray ja = new JSONArray();
        for (CatAgencyStructure item : list) {
            JSONObject jo = CreateObjectsTreeChildenNode(item, state);
            ja.put(jo);
        }
        return ja;
    }
	
	public JSONObject CreateObjectsTreeChildenNode(CatAgencyStructure obj, String state) throws Exception {
        JSONObject jo = new JSONObject();
        jo.put("id", obj.getId());
        jo.put("text", obj.getCode() + " - " + obj.getName());
        if (obj.getCatAgencyStructures() != null && obj.getCatAgencyStructures().size() > 0) {
            jo.put("state", state);
            JSONArray ja = new JSONArray();
            for (CatAgencyStructure sysChildenItem : obj.getCatAgencyStructures()) {
                JSONObject joChildren = new JSONObject();
                joChildren = CreateObjectsTreeChildenNode(sysChildenItem, state);
                if (joChildren.length() > 0) {
                    ja.put(joChildren);
                }
            }
            jo.put("children", ja);
        }
        return jo;
    }
	
	public CatAgencyStructure getCatAgencyStructureByCode(String code) {
		Criteria criteria = getCurrentSession().createCriteria(CatAgencyStructure.class);
		criteria.add(Restrictions.eq("code", code));
		return (CatAgencyStructure) criteria.uniqueResult();
	}
	
	public CatAgencyStructure getCatAgencyStructureByParent(String id) {
		Criteria criteria = getCurrentSession().createCriteria(CatAgencyStructure.class);
		criteria.add(Restrictions.eq("parent.id", id));
		return (CatAgencyStructure) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<CatAgencyStructure> getAllRoot() {
		return (ArrayList<CatAgencyStructure>) getThreadSession().createCriteria(CatAgencyStructure.class)
				.add(Restrictions.isNull("parent")).list();
	}
	
	@SuppressWarnings("unchecked")
	public JSONArray getTreeObjectsData() throws Exception {
		ArrayList<CatAgencyStructure> objs = (ArrayList<CatAgencyStructure>) getThreadSession().createCriteria(CatAgencyStructure.class)
				.add(Restrictions.isNull("parent")).list();
		JSONArray _result = CreateObjectsTreeNode(objs, "open");
		return _result;
	}
	
}
