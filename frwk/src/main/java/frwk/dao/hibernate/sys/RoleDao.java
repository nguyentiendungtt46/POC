package frwk.dao.hibernate.sys;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;

import common.util.Formater;
import common.util.ResourceException;
import entity.frwk.JobcodeRole;
import entity.frwk.SysObjects;
import entity.frwk.SysRights;
import entity.frwk.SysRightsId;
import entity.frwk.SysRoles;
import entity.frwk.SysRolesUsers;
import entity.frwk.SysUsers;
import entity.frwk.UserLog;

@Repository(value = "roleDao")
public class RoleDao extends SysDao<SysRoles> {

	static Logger lg = LogManager.getLogger(RoleDao.class);

	public void save(SysRoles role1) throws Exception {
		Session session = openNewSession();
		Transaction tx = null;
		SysRoles oldInDb = null;
		try {
			// Ghi log
			tx = session.beginTransaction();
			UserLog log = new UserLog(getSessionUser());
			

			String rights = role1.getRights();
			if (!Formater.isNull(role1.getParentId())) {
				role1.setSysRoles((SysRoles) session.load(SysRoles.class, role1.getParentId()));
			}
			List<SysRolesUsers> oldRolesUsers = new ArrayList<SysRolesUsers>();
			Map<String, String> map = new HashMap<String, String>();
			if (!Formater.isNull(role1.getId())) {
				// check quan he cha con
				oldInDb = (SysRoles) session.get(SysRoles.class, role1.getId());
				if (oldInDb != null && role1.getSysRoles() != null) {
					validateParent(oldInDb, role1.getSysRoles().getId());
				}
				oldRolesUsers.addAll(oldInDb.getSysRolesUserses());
				SysRoles role = (SysRoles) session.load(SysRoles.class, role1.getId());
				// log.setAction("edit_" + role.getClass().getName());
				log.setAction("edit_" + role1.getClass().getName());
				// Lay cac rights bi xoa
				List<SysRights> delRight = new ArrayList<SysRights>();
				for (SysRights right : role.getSysRightses()) {
					if (rights.indexOf(right.getId() + ",") < 0) {
						session.delete(right);
						delRight.add(right);
					}
				}
				// danh sach nguoi dung
				if (!Formater.isNull(role1.getSysRolesUserses())) {
					for (SysRolesUsers rolesUsers : role1.getSysRolesUserses()) {
						if (rolesUsers == null)
							continue;
						if (Formater.isNull(rolesUsers.getId().getRoleId())
								&& Formater.isNull(rolesUsers.getId().getUserId())) {
							rolesUsers.getId().setRoleId(role1.getId());
							rolesUsers.getId().setUserId(rolesUsers.getSysUsers().getId());
							session.save(rolesUsers);
						} else {
							map.put(rolesUsers.getId().getRoleId() + rolesUsers.getId().getUserId(),
									rolesUsers.getId().getUserId());
						}
					}
				}
				role.getSysRightses().remove(delRight);
				session.merge(role1);
			} else {
				log.setAction("insert_" + role1.getClass().getName());
				session.save(role1);
				// Nsd co role cha thi cung co role con
				if (role1.getSysRoles() != null) {
					SysRoles parent = session.load(SysRoles.class, role1.getSysRoles().getId());
					for (SysRolesUsers ru : parent.getSysRolesUserses())
						session.save(new SysRolesUsers(ru.getSysUsers(), role1));

				}

			}

			if (!Formater.isNull(oldRolesUsers)) {
				for (SysRolesUsers oldUsers : oldRolesUsers) {
					if (map.get(oldUsers.getId().getRoleId() + oldUsers.getId().getUserId()) == null) {
						session.delete(oldUsers);
					}
				}
			}

			// Danh sach quyen
			if (!Formater.isNull(rights)) {
				// Khoi tao danh sach quyen
				String arrObj[] = rights.split(",");
				for (String objId : arrObj) {
					if (isNew(objId, role1.getSysRightses())) {
						SysObjects sysObj = (SysObjects) session.load(SysObjects.class, objId);
						SysRights right = new SysRights(new SysRightsId(role1.getId(), sysObj.getId()), sysObj, role1);
						right.setSysRoles(role1);
						session.save(right);
					}
				}
			}

			log.setRecordId(String.valueOf(
					getClassMetadata(role1.getClass()).getIdentifier(role1, (SessionImplementor) getCurrentSession())));
			StringWriter writer = new StringWriter();
			SysRoles roles = role1;
//			roles.setSysRightses(null);
//			roles.setSysRoleses(null);
//			roles.setSysRolesUserses(null);
			roles.setSysRoles(null);
			new ObjectMapper().writeValue(writer, roles);
			log.setDetail(writer.toString());
			session.save(log);
			tx.commit();
		} catch (Exception ex) {
			logger.error(ex);
			if (tx != null)
				tx.rollback();
			throw ex;
		} finally {
			session.close();
		}
	}

	private void validateParent(SysRoles oldInDb, String parentId) throws Exception {
		// trung cha chon cha, con, chau
		if (oldInDb.getId().equals(parentId)) {
			throw new ResourceException(
					"Nh&#243;m quy&#7873;n cha tr&#249;ng v&#7899;i m&#236;nh ho&#7863;c nh&#243;m quy&#7873;n con, ch&#225;u");
		}
		// Kiem tra cq cha trung voi cq con chau
		List<SysRoles> children = null;
		Session ss = openNewSession();
		Transaction tx = null;
		try {
			tx = ss.beginTransaction();
			Criteria c = ss.createCriteria(SysRoles.class);
			c.add(Restrictions.eq("sysRoles", oldInDb));
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
		for (SysRoles child : children)
			validateParent(child, parentId);
	}

	private static final Logger logger = LogManager.getLogger(RoleDao.class);

	public SysRoles getWithRight(String id) throws Exception {
		Session ss = openNewSession();
		Transaction tx = null;
		SysRoles role = null;
		try {
			role = (SysRoles) ss.get(SysRoles.class, id);
			for (SysRights right : role.getSysRightses()) {
				role.setRights(role.getRights() + "," + right.getId().getObjectId());
			}
			if (role.getSysRoles() != null)
				role.setParentId(role.getSysRoles().getId());
		} catch (Exception ex) {
			logger.error(ex);
			if (tx != null)
				tx.rollback();
			throw ex;

		} finally {
			ss.close();
		}

		return role;
	}

	public JSONArray getTreeRolesData(boolean bNotPublicRole, String parentRoleId, SysUsers su) throws Exception {
		JSONArray _result = new JSONArray();
		Session ss = openNewSession();
		Criteria cr = ss.createCriteria(SysRoles.class);
		if (!bNotPublicRole)
			cr.add(Restrictions.or(Restrictions.isNull("notPublic"), Restrictions.eq("notPublic", Boolean.FALSE)));
		if (!Formater.isNull(parentRoleId))
			cr.add(Restrictions.eq("sysRoles", new SysRoles(parentRoleId)));
		else
			cr.add(Restrictions.isNull("sysRoles"));
		ArrayList<SysRoles> objs = (ArrayList<SysRoles>) cr.list();
		// for (SysRoles o : objs) {
		// htmlReturn += createHtmlOfRoot(o, isVietTin);
		_result = CreateRolesTreeNode(objs, bNotPublicRole, su);
		// }
		ss.flush();
		ss.close();
		return _result;
	}

	public JSONArray CreateRolesTreeNode(ArrayList<SysRoles> list, boolean bNotPublicRole, SysUsers su)
			throws Exception {
		JSONArray ja = new JSONArray();
		for (SysRoles sysItem : list) {
			JSONObject jo = CreateRolesTreeChildenNode(sysItem, bNotPublicRole, su);
			ja.put(jo);
		}
		return ja;
	}

	public JSONObject CreateRolesTreeChildenNode(SysRoles obj, boolean bNotPublicRole, SysUsers su) throws Exception {
		JSONObject jo = new JSONObject();

		jo.put("id", obj.getId());
		jo.put("text", obj.getCode() + " - " + obj.getDescriptionVi());
		if (su != null && su.getRoles() != null) {
			String[] arrRoles = su.getRoles().split(",");
			for (String arrItem : arrRoles) {
				if (arrItem.equals(obj.getId())) {
					if (obj.getSysRoleses(bNotPublicRole) != null && obj.getSysRoleses(bNotPublicRole).size() == 0)
						jo.put("checked", true);
					break;
				}
			}
		}
		if (obj.getSysRoleses(bNotPublicRole) != null && obj.getSysRoleses(bNotPublicRole).size() > 0) {
			jo.put("state", "closed");
			JSONArray ja = new JSONArray();
			for (SysRoles sysChildenItem : obj.getSysRoleses()) {
				JSONObject joChildren = new JSONObject();
				joChildren = CreateRolesTreeChildenNode(sysChildenItem, bNotPublicRole, su);
				if (joChildren.length() > 0) {
					ja.put(joChildren);
				}
			}
			jo.put("children", ja);
		}

		return jo;
	}

	public JSONArray getTreeObjectsData() throws Exception {
		ArrayList<SysObjects> objs = (ArrayList<SysObjects>) getThreadSession().createCriteria(SysObjects.class)
				.add(Restrictions.isNull("sysObjects")).list();
		JSONArray _result = CreateObjectsTreeNode(objs);
		return _result;
	}

	public JSONArray CreateObjectsTreeNode(ArrayList<SysObjects> list) throws Exception {
		JSONArray ja = new JSONArray();
		for (SysObjects sysItem : list) {
			JSONObject jo = CreateObjectsTreeChildenNode(sysItem);
			ja.put(jo);
		}
		return ja;
	}

	public JSONObject CreateObjectsTreeChildenNode(SysObjects obj) throws Exception {
		JSONObject jo = new JSONObject();
		jo.put("id", obj.getId());
		jo.put("text", obj.getObjectId() + " - " + obj.getName());
		if (obj.getSysObjectses() != null && obj.getSysObjectses().size() > 0) {
			jo.put("state", "closed");
			JSONArray ja = new JSONArray();
			for (SysObjects sysChildenItem : obj.getSysObjectses()) {
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

	public String getRgtHtml(boolean isVietTin, String parentRoleId) {
		String htmlReturn = "";
		Session ss = openNewSession();
		Criteria cr = ss.createCriteria(SysRoles.class);
		if (!isVietTin)
			cr.add(Restrictions.eq("usedbypartner", Boolean.TRUE));
		if (!Formater.isNull(parentRoleId))
			cr.add(Restrictions.eq("sysRoles", new SysRoles(parentRoleId)));
		else
			cr.add(Restrictions.isNull("sysRoles"));
		ArrayList<SysRoles> objs = (ArrayList<SysRoles>) cr.list();
		for (SysRoles o : objs) {
			htmlReturn += createHtmlOfRoot(o, isVietTin);
		}
		ss.flush();
		ss.close();
		return htmlReturn;
	}

	private static String createHtmlOfRoot(SysRoles o, boolean isVietTin) {
		String htmlReturn = "";
		if (o.getSysRoleses(isVietTin) != null && o.getSysRoleses(isVietTin).size() > 0)
			htmlReturn = "<tr class='trparent open " + o.getParentId() + "'  id='tr" + o.getPath() + "'><td>";
		else
			htmlReturn = "<tr class = '" + o.getParentId() + "'id='tr" + o.getPath() + "'><td>";

		// td checkbox
		htmlReturn += "<input type='checkbox' onclick = 'selectRight(\"" + o.getId() + "\",\"" + o.getPath()
				+ "\", this.checked)' class='" + o.getId() + "' id = 'chk" + o.getPath() + "'/>";
		htmlReturn += "</td><td>";
		// td content
		// So luong khoang trong
		if (o.getSysRoles() == null) {
			o.setILevel(0);
		} else {
			o.setILevel(o.getSysRoles().getILevel() + 1);
		}
		String sEmpty = "";
		for (int i = 0; i < o.getILevel(); i++)
			sEmpty += "&nbsp;&nbsp;&nbsp;";

		if (o.getSysRoleses(isVietTin) != null && o.getSysRoleses(isVietTin).size() > 0) {

			htmlReturn += sEmpty + "<a href='#' onclick='expand(\"" + o.getId() + "\",\"" + o.getPath()
					+ "\")'><font id='fnt" + o.getPath()
					+ "' style='font-weight:bold'>(-)&nbsp;</font><font style='font-weight:bold'>" + o.getCode() + "-"
					+ o.getDescriptionVi() + "</font></a>";
		} else {
			htmlReturn += sEmpty + "<font>" + o.getCode() + "-" + o.getDescriptionVi() + "</font>";
		}

		htmlReturn += "</td></tr>";
		if (o.getSysRoleses(isVietTin) != null && o.getSysRoleses(isVietTin).size() > 0) {
			for (SysRoles o1 : o.getSysRoleses())
				htmlReturn += createHtmlOfRoot(o1, isVietTin);
		}
		return htmlReturn;
	}

	private static boolean isNew(String objId, List<SysRights> delRights) {
		if (Formater.isNull(delRights))
			return true;
		for (SysRights r : delRights) {
			if (r.getId().equals(objId))
				return false;
		}
		return true;
	}

	public ArrayList<SysRoles> getAllRoot() {
		return (ArrayList<SysRoles>) getThreadSession().createCriteria(SysRoles.class)
				.add(Restrictions.isNull("sysRoles")).list();
	}

	public ArrayList<SysRoles> getParentRighList(String id) {
		return (ArrayList<SysRoles>) getThreadSession().createCriteria(SysRoles.class)
				.add(Restrictions.isNull("sysRoles")).add(Restrictions.ne("id", id)).list();
	}

	public SysRoles getRolesByID(String ID) {
		Session s = openNewSession();
		SysRoles c = (SysRoles) s.get(SysRoles.class, ID);
		return c;
	}

	public SysRoles getByCodeJobCode(String jobCode) throws Exception {
		List<SysRoles> tmp = null;
		List<JobcodeRole> tmpJobcodeRole = null;
		Session s = openNewSession();
		Transaction tx = null;
		try {
			tx = s.beginTransaction();
			tmpJobcodeRole = s.createCriteria(JobcodeRole.class).add(Restrictions.eq("jobcode", jobCode)).list();
			if (Formater.isNull(tmpJobcodeRole))
				return null;
			tmp = s.createCriteria(SysRoles.class).add(Restrictions.eq("code", tmpJobcodeRole.get(0).getRole())).list();
			tx.commit();
		} catch (Exception ex) {
			tx.rollback();
			throw ex;
		} finally {
			s.close();
		}
		if (Formater.isNull(tmp))
			return null;
		return tmp.get(0);
	}

	public boolean checkCode(String code) {
		long count = (long) getThreadSession().createCriteria(SysRoles.class)
				.add(Restrictions.eq("code", code).ignoreCase()).setProjection(Projections.rowCount()).uniqueResult();
		return (count > 0 ? true : false);
	}

	public void checkUse(String parent) throws ResourceException {
		long count = (long) getThreadSession().createCriteria(SysRoles.class)
				.add(Restrictions.eq("sysRoles.id", parent).ignoreCase()).setProjection(Projections.rowCount())
				.uniqueResult();
		if (count > 0) {
			throw new ResourceException(
					"Kh&#244;ng x&#243;a &#273;&#432;&#7907;c nh&#243;m quy&#7873;n v&#236; c&#243; nh&#243;m quy&#7873;n con");// KhÃ´ng
																																// xÃ³a
																																// Ä‘Æ°á»£c
																																// nhÃ³m
																																// quyá»�n
																																// vÃ¬
																																// cÃ³
																																// nhÃ³m
																																// quyá»�n
																																// con
		}
	}

	public void checkUseByUser(String roleId) throws ResourceException {
		long count = (long) getThreadSession().createCriteria(SysRolesUsers.class)
				.add(Restrictions.eq("sysRoles.id", roleId).ignoreCase()).setProjection(Projections.rowCount())
				.uniqueResult();
		if (count > 0) {
			// KhÃ´ng xÃ³a Ä‘Æ°á»£c nhÃ³m quyá»�n vÃ¬ Ä‘ang Ä‘Æ°á»£c sá»­ dá»¥ng
			throw new ResourceException(
					"Kh&#244;ng x&#243;a &#273;&#432;&#7907;c nh&#243;m quy&#7873;n v&#236; &#273;ang &#273;&#432;&#7907;c s&#7917; d&#7909;ng");
		}
	}
}
