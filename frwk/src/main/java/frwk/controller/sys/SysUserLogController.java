package frwk.controller.sys;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import common.util.Formater;
import entity.Partner;
import entity.frwk.LogAction;
import entity.frwk.SysUsers;
import entity.frwk.SysUsersLog;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.LogActionDao;
import frwk.dao.hibernate.sys.SysPartnerDao;
import frwk.dao.hibernate.sys.SysUsersDao;
import frwk.dao.hibernate.sys.UserLogDao;
import frwk.form.SysUserLogForm;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/audit")
public class SysUserLogController extends CatalogController<SysUserLogForm, SysUsersLog> {

	private static Logger lg = LogManager.getLogger(SysUserLogController.class);

	@Autowired
	private UserLogDao userLogDao;

	@Autowired
	private SysUsersDao sysUsersDao;

	@Autowired
	private SysPartnerDao sysPartnerDao;

	@Autowired
	private LogActionDao logActionDao;

	@Autowired
	private ExportExcel exportExcel;

	@Override
	public BaseDao<SysUsersLog> getDao() {
		return userLogDao;
	}

	private SysUsersLog userlog = new SysUsersLog();
	ArrayList<LogAction> logAction;
	private List<ActionItem> actionList;
	private List<Partner> danhSachDoiTac;

	@Override
	public BaseDao<SysUsersLog> createSearchDAO(HttpServletRequest request, SysUserLogForm form) throws Exception {
		UserLogDao dao = new UserLogDao();
		SysUserLogForm sysUserLogForm = (SysUserLogForm) form;

		if (!Formater.isNull(sysUserLogForm.getFromdate())) {
			try {
				dao.addRestriction(Restrictions.ge("modifyTime", Formater.str2date(sysUserLogForm.getFromdate())));
			} catch (ParseException e) {
				lg.error(e);
			}
		}
		if (!Formater.isNull(sysUserLogForm.getTodate())) {
			try {
				Calendar c = Calendar.getInstance();
				c.setTime(Formater.str2date(sysUserLogForm.getTodate()));
				c.add(Calendar.DATE, 1);
				dao.addRestriction(Restrictions.le("modifyTime", c.getTime()));
			} catch (ParseException e) {
				lg.error(e);
			}
		}
		// Search by username
		if (!Formater.isNull(sysUserLogForm.getUsername())) {
			dao.addRestriction(
					Restrictions.like("userId", sysUserLogForm.getUsername().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		// Search by keyWord
		if (!Formater.isNull(sysUserLogForm.getKeyWord())) {
			dao.addRestriction(
					Restrictions.like("recordId", sysUserLogForm.getKeyWord().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		// Search by action
		if (!Formater.isNull(sysUserLogForm.getActionLists())) {
			// like because edit_entity.XXXX_$$javasiss
			dao.addRestriction(Restrictions.like("action", sysUserLogForm.getActionLists().trim(), MatchMode.ANYWHERE)
					.ignoreCase());
			if ("entity.frwk.SysUsers".equals(sysUserLogForm.getActionLists())) {
				if (!Formater.isNull(sysUserLogForm.getKeyWord())) {
					String sql = "exists (select 1 from sys_users u where u.id = \"RECORD ID\" and upper(u.username) like ?)";
					dao.addRestriction(Restrictions.sqlRestriction(sql,
							"'%" + sysUserLogForm.getKeyWord().toUpperCase() + "%'", StringType.INSTANCE));
				}
			}
		}
		// Search by partner
		if (!Formater.isNull(sysUserLogForm.getSpartnerId())) {
			sysUserLogForm.setSpartnerId(StringEscapeUtils.escapeSql(sysUserLogForm.getSpartnerId()));
			String sql = "exists (select 1 from sys_users u where u.username = USER_ID and u.company_id = ?)";
			dao.addRestriction(Restrictions.sqlRestriction(sql, sysUserLogForm.getSpartnerId(), StringType.INSTANCE));
		}
		// Order by modifytime
		dao.addOrder(Order.desc("modifyTime"));
		return dao;
	}

	@Override
	public void pushToJa(JSONArray ja, SysUsersLog r, SysUserLogForm sysUserLogForm) throws Exception {
		if (r.getUserId() != null) {
			ja.put("<span class='characterwrap'>" + r.getUserId() + "</span>");
		} else {
			ja.put("");
		}

		if ("login".equals(r.getAction())) {
			ja.put("Login");
		} else {
			try {
				String[] className = r.getAction().split("_");
				LogAction logaction = logActionDao.getFunctionNameByClassName(className[1]);
				if (logaction != null) {
					String str = getFunctionName(className[0]);
					ja.put(str + logaction.getFncName());
				} else
					ja.put("");

			} catch (Exception e) {
				ja.put("");
				lg.error(e);
			}
		}


		if (!"hibernatedto.SysUsers".equals(sysUserLogForm.getActionLists()))
			ja.put(r.getRecordId());
		else {
			if (Formater.isNull(r.getRecordId()))
				ja.put("");
			else {
				SysUsers u = sysUsersDao.getUserById(r.getRecordId());
				if (u != null)
					ja.put(u.getUsername());
				else
					ja.put("");
			}
		}

		if (!Formater.isNull(r.getDetail())) {
			ja.put("<a data-toggle=\"modal\" data-target=\"#myModal\" href = '#' onclick = 'chiTietLog(\"" + r.getId()
					+ "\")'>Chi ti&#7871;t</a>");
		} else {
			ja.put("");
		}

		try {
			ja.put(Formater.date2ddsmmsyyyspHHmmss(r.getModifyTime()));
		} catch (Exception e) {
			ja.put("");
			lg.error(e);
		}
	}

	@Override
	public String getJsp() {
		return "qtht/danh_muc_hoat_dong";
	}

	public void ExportFileExcel(ModelMap model, HttpServletRequest request, HttpServletResponse rs, SysUserLogForm form)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String tuNgay = request.getParameter("tuNgay");
		String denNgay = request.getParameter("denNgay");
		String catelogy = request.getParameter("catelogy");
		String username = request.getParameter("username");
		String obj = request.getParameter("obj");
		List<SysUsersLog> list = userLogDao.reports(tuNgay, denNgay, catelogy, username, obj);
		for (SysUsersLog file : list) {
			if (file.getModifyTime() != null)
				file.setModifyTimeStr(Formater.date2ddsmmsyyyspHHmmss(file.getModifyTime()));
			if ("login".equals(file.getAction())) {
				file.setAction("Login");
			} else {
				try {
					String[] className = file.getAction().split("_");
					LogAction logaction = logActionDao.getFunctionNameByClassName(className[1]);
					if (logaction != null) {
						String str = getFunctionName(className[0]);
						file.setAction(str + logaction.getFncName());
					} else
						file.setAction("");

				} catch (Exception e) {
					file.setAction("");
					lg.error(e);
				}
			}

		}
		map.put("reports", list);
		map.put("username", username);
		map.put("fromDate", tuNgay);
		map.put("toDate", denNgay);
		if (Formater.isNull(catelogy))
			map.put("catelogy", "T\u1EA5t c\u1EA3");
		else
			map.put("catelogy", catelogy);
		map.put("obj", obj);
		exportExcel.export("Audit_he_thong", rs, map);
		userLogDao.clear();
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysUserLogForm sysUserLogForm)
			throws Exception {
		actionList = new ArrayList<ActionItem>();
		logAction = logActionDao.getAll();
		for (LogAction log : logAction)
			actionList.add(new ActionItem(log.getClassName(), log.getFncName()));
		actionList.add(new ActionItem("Login", "Login"));
		danhSachDoiTac = sysPartnerDao.getAll();
		model.addAttribute("dsDoiTac", danhSachDoiTac);
		model.addAttribute("actionLists", actionList);
	}

	public void setDanhSachDoiTac(List<Partner> danhSachDoiTac) {
		this.danhSachDoiTac = danhSachDoiTac;
	}

	public List<Partner> getDanhSachDoiTac() {
		return danhSachDoiTac;
	}

	private String getFunctionName(String fncName) {
		if (fncName.equals("insert"))
			return "Th\u00EAm m\u1EDBi ";
		if (fncName.equals("edit"))
			return "C\u1EADp nh\u1EADt ";
		if (fncName.equals("del"))
			return "X\u00F3a ";
		return "";
	}

	public SysUsersLog getModel() {
		return userlog;
	}

	public SysUsersLog getUserlog() {
		return userlog;
	}

	public void setUserlog(SysUsersLog userlog) {
		this.userlog = userlog;
	}

	public void setActionList(List<SysUserLogController.ActionItem> actionList) {
		this.actionList = actionList;
	}

	public List<SysUserLogController.ActionItem> getActionList() {
		return actionList;
	}

	public class ActionItem {
		private String code, name;

		public ActionItem(String code, String name) {
			this.code = code;
			this.name = name;
		}

		private ActionItem() {
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getCode() {
			return code;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	public void loadDetail(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, SysUserLogForm form)
			throws IOException {
		rp.setContentType("text/plan;charset=utf-8");
		PrintWriter out = rp.getWriter();
		String id = Formater.isNull(rq.getParameter("id")) ? "" : rq.getParameter("id");
		SysUsersLog userLog = null;
		try {
			userLog = userLogDao.get(SysUsersLog.class, id);
		} catch (Exception e) {
			lg.error(e);
		}
		out.println(Formater.isNull(userLog.getDetail()) ? "" : userLog.getDetail());
		out.flush();
		out.close();
	}

}