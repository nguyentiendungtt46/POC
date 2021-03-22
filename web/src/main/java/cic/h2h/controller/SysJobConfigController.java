package cic.h2h.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.util.StringUtil;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import cic.h2h.dao.hibernate.SysJobConfigDao;
import cic.h2h.form.SysJobConfigForm;
import cic.ws.client.WsClient;
import common.util.Formater;
import common.util.ResourceException;
import dto.ExcelTempDTO;
import entity.SysJobConfig;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/jobConfig")
public class SysJobConfigController  extends CatalogController<SysJobConfigForm, SysJobConfig> {

	private static Logger lg = LogManager.getLogger(ServiceInfoController.class);
	
	@Autowired
	private SysJobConfigDao sysJobConfigDao;
	
	@Autowired
	private WsClient wsClient;
	
	@Override
	public BaseDao<SysJobConfig> createSearchDAO(HttpServletRequest request, SysJobConfigForm form) throws Exception {
		SysJobConfigDao dao = new SysJobConfigDao();
		SysJobConfigForm sysJobConfigForm = (SysJobConfigForm) form;

		if (!Formater.isNull(sysJobConfigForm.getCode()))
			dao.addRestriction(Restrictions.like("jobCode", sysJobConfigForm.getCode().trim(), MatchMode.ANYWHERE)
					.ignoreCase());
		
		if (!Formater.isNull(sysJobConfigForm.getName()))
			dao.addRestriction(Restrictions.like("jobName", sysJobConfigForm.getName().trim(), MatchMode.ANYWHERE)
					.ignoreCase());
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, SysJobConfig e, SysJobConfigForm modelForm) throws Exception {
		// TODO Auto-generated method stub
		ja.put("<a href = '#' onclick = 'edit(\"" + e.getId() + "\")'>" + e.getJobCode() + "</a>");
		ja.put(e.getJobName());
		ja.put(e.getRate());
		if(e.getPeriod() == null)
			ja.put("");
		else if(e.getPeriod().compareTo(BigDecimal.valueOf(1)) == 0)
			ja.put("Gi&#7901;");
		else if(e.getPeriod().compareTo(BigDecimal.valueOf(2)) == 0)
			ja.put("Ph&#250;t");
		else if(e.getPeriod().compareTo(BigDecimal.valueOf(3)) == 0)
			ja.put("Gi&#226;y");
		if(e.getActive())
			ja.put("Ho&#7841;t &#273;&#7897;ng");
		else
			ja.put("D&#7915;ng");
	}

	@Override
	public BaseDao<SysJobConfig> getDao() {
		// TODO Auto-generated method stub
		return sysJobConfigDao;
	}

	@Override
	public String getJsp() {
		// TODO Auto-generated method stub
		return "sys_job_config/view";
	}
	
	@Autowired
	ExportExcel exportExcel;
	
	public void exportFileExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysJobConfigForm form) {
		try {
			String code = rq.getParameter("code");
			String name = rq.getParameter("name");
			Map<String, Object> map = new HashMap<String, Object>();
			List<SysJobConfig> lst = sysJobConfigDao.reports(code, name);
			List<ExcelTempDTO> lstExcel = new ArrayList<ExcelTempDTO>();
			
			for (SysJobConfig obj : lst) {
				ExcelTempDTO elm = new ExcelTempDTO();
				elm.setCol1(obj.getJobCode());
				elm.setCol2(obj.getJobName());
				elm.setCol3(obj.getRate() == null ? "" : obj.getRate().toString());
				if(obj.getPeriod() == null)
					elm.setCol4("");
				else if(obj.getPeriod().compareTo(new BigDecimal("1")) == 0)
					elm.setCol4("Giờ");
				else if(obj.getPeriod().compareTo(new BigDecimal("2")) == 0)
					elm.setCol4("Phút");
				else if(obj.getPeriod().compareTo(new BigDecimal("3")) == 0)
					elm.setCol4("Giây");
				if(obj.getActive())
					elm.setCol5("Hoạt động");
				else 
					elm.setCol5("Dừng");
				lstExcel.add(elm);
			}
			map.put("reports", lstExcel);
			exportExcel.export("Danh_sach_dat_lich_job", rs, map);
		} catch (Exception e) {
			lg.error(e);
		}
	}
	

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysJobConfigForm form)
			throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, SysJobConfigForm form)
			throws Exception {
		SysJobConfigForm infoForm = (SysJobConfigForm) form;
		SysJobConfig sysJobConfig = infoForm.getSysJobConfig();
		if(sysJobConfigDao.reports(sysJobConfig.getJobCode(), "").size() > 0 && Formater.isNull(sysJobConfig.getId()))
			throw new ResourceException("M&#227; l&#7883;ch &#273;&#227; t&#7891;n t&#7841;i");
		BigDecimal fixDelay = BigDecimal.ZERO;
		if(sysJobConfig.getPeriod() != null) {
			if(sysJobConfig.getPeriod().compareTo(BigDecimal.valueOf(1)) == 0) {//gio
				fixDelay = sysJobConfig.getRate().multiply(BigDecimal.valueOf(3600000)) ;
			}else if(sysJobConfig.getPeriod().compareTo(BigDecimal.valueOf(2)) == 0) {//phut
				fixDelay = sysJobConfig.getRate().multiply(BigDecimal.valueOf(60000)) ;
			}else if(sysJobConfig.getPeriod().compareTo(BigDecimal.valueOf(3)) == 0) {//giay
				fixDelay = sysJobConfig.getRate().multiply(BigDecimal.valueOf(1000)) ;
			}
			sysJobConfig.setFixDelay(fixDelay);
		}
		
		sysJobConfigDao.save(sysJobConfig);
		try {
			wsClient.resetJob(sysJobConfig);
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e);
			rs.setContentType("text/html;charset=utf-8");
			PrintWriter pw = rs.getWriter();
			pw.print("L&#7895;i call service reset Job");
			pw.flush();
			pw.close();
			return;
		}
	}
}
