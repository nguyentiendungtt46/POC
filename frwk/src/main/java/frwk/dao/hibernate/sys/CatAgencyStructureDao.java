package frwk.dao.hibernate.sys;

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

@Repository(value = "catAgencyStructureDAO")
public class CatAgencyStructureDao extends SysDao<CatAgencyStructure> {
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
	
	public JSONArray CreateObjectsTreeNode(ArrayList<CatAgencyStructure> list) throws Exception {
        JSONArray ja = new JSONArray();
        for (CatAgencyStructure item : list) {
            JSONObject jo = CreateObjectsTreeChildenNode(item);
            ja.put(jo);
        }
        return ja;
    }
	
	public JSONObject CreateObjectsTreeChildenNode(CatAgencyStructure obj) throws Exception {
        JSONObject jo = new JSONObject();
        jo.put("id", obj.getId());
        jo.put("text", obj.getCode() + " - " + obj.getName());
        if (obj.getCatAgencyStructures() != null && obj.getCatAgencyStructures().size() > 0) {
            jo.put("state", "closed");
            JSONArray ja = new JSONArray();
            for (CatAgencyStructure sysChildenItem : obj.getCatAgencyStructures()) {
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
	
}

