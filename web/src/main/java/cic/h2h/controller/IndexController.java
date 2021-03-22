package cic.h2h.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import cic.h2h.dao.hibernate.CatAgencyStructureDao;
import cic.h2h.dao.hibernate.CatProductDao;
import cic.h2h.dao.hibernate.PartnerDao;
import cic.h2h.dao.hibernate.QnaInDetailDao;
import cic.h2h.form.CatAgencyStructureForm;
import cic.h2h.form.QnaInDetailForm;
import common.util.Formater;
import constants.RightConstants;
import dto.ExcelParamDTO;
import dto.ExcelTempDTO;
import entity.CatProduct;
import entity.Partner;
import entity.PartnerBranch;
import entity.QnaInDetail;
import entity.frwk.SysSecurity;
import entity.frwk.SysUsers;
import frwk.controller.CommonController;
import frwk.dao.hibernate.sys.PartnerBranchDao;
import frwk.dao.hibernate.sys.SysSecurityDao;
import frwk.form.SysRolesForm;
import frwk.utils.ApplicationContext;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/index")
public class IndexController extends CommonController<QnaInDetailForm, QnaInDetail> {

	@Autowired
	PartnerDao partnerDao;

	@Autowired
	private QnaInDetailDao qnaInDetailDao;

	@Autowired
	private PartnerBranchDao partnerBranchDao;
	@Autowired
	private Partner partner;
	@Value("${QNA_SERVICE}")
	private String qnaServiceCode;
	@Autowired
	private CatProductDao catProductDao;

	@Autowired
	private CatAgencyStructureDao catAgencyStructureDao;
	
	@Override
	public String getJsp() {
		return "base/indexMBAL";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, QnaInDetailForm form)
			throws Exception {
//		Date lastCreatedDate = qnaInDetailDao.getLastCreatedDate();
//		if (lastCreatedDate == null)
//			form.setNgaytaotu(Formater.date2str(Calendar.getInstance().getTime()));
//		else
//			form.setNgaytaotu(Formater.date2str(lastCreatedDate));
//		form.setNgaytaoden(Formater.date2str(Calendar.getInstance().getTime()));
//		List<PartnerBranch> dsDonVi = partnerBranchDao.getBranch(partner);
//		model.addAttribute("dsDonVi", dsDonVi);
//		List<CatProduct> lstProcuct = catProductDao.getAllSvCode(qnaServiceCode);
//		model.addAttribute("lstProcuct", lstProcuct);
		model.addAttribute("treeAll", catAgencyStructureDao.getAllRoot());
	}

	public void loadFilter(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			QnaInDetailForm form) throws Exception {
		String p_fromDate = request.getParameter("fromDate");
		String p_toDate = request.getParameter("toDate");
		String p_product = request.getParameter("product");
		String p_matctd = request.getParameter("tctd");
		String p_status = request.getParameter("status");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		HashMap<String, List<?>> maps = qnaInDetailDao.getDashBroad(p_product, Formater.str2date(p_fromDate),
				Formater.str2date(p_toDate), p_matctd, p_status);
		List<String[]> qaDashBroad = (List<String[]>) maps.get("rCursor");
		JSONArray array = new JSONArray(new ObjectMapper().writeValueAsString(qaDashBroad));
		pw.print(array);
		pw.close();
	}

	@Autowired
	ExportExcel exportExcel;

	public void ExportFileExcel(ModelMap model, HttpServletRequest request, HttpServletResponse rs,
			QnaInDetailForm form) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String p_product = request.getParameter("product");
		String p_fromDate = request.getParameter("fromDate");
		if (!Formater.isNull(p_fromDate))
			p_fromDate += " 00:00:00";
		String p_toDate = request.getParameter("toDate");
		if (!Formater.isNull(p_toDate))
			p_toDate += " 23:59:59";
		String p_matctd = request.getParameter("tctd");
		String p_status = request.getParameter("status");
		HashMap<String, List<ExcelTempDTO>> maps = qnaInDetailDao.getDashBroadExcel(p_product, p_fromDate, p_toDate,
				p_matctd, p_status);
		List<ExcelTempDTO> qaDashBroad = (List<ExcelTempDTO>) maps.get("rCursor");
		ExcelParamDTO param = new ExcelParamDTO();
		param.setParam1(p_fromDate);
		param.setParam2(p_toDate);
		if (Formater.isNull(p_product))
			param.setParam3("Tất cả");
		else
			param.setParam3(p_product);
		if (Formater.isNull(p_matctd))
			param.setParam4("Tất cả");
		else
			param.setParam4(p_matctd);
		String status_txt = "";
		if (Formater.isNull(p_status))
			status_txt = "Tất cả";
		else if (p_status.equals("1"))
			status_txt = "Chưa có kết quả";
		else if (p_status.equals("2"))
			status_txt = "Đã có kết quả";
		else if (p_status.equals("3"))
			status_txt = "Đã trả cho TCTD";
		else if (p_status.equals("4"))
			status_txt = "Không thành công";
		param.setParam5(status_txt);
		map.put("reports", qaDashBroad);
		map.put("param", param);
		exportExcel.export("qa_dashbroad", rs, map);
	}

	@Autowired
	private SysSecurityDao sysSecurityDao;

	private String expiredPassWordDate(Date pwDate) {
		List<SysSecurity> expriredDays = sysSecurityDao.getAll(SysSecurity.class);
		String expriredIn = null, expriredDay = null;
		Date date = new Date();
		if (!Formater.isNull(expriredDays)) {
			for (SysSecurity item : expriredDays) {
				if (RightConstants.PW_EXPIRED_DAY.equalsIgnoreCase(item.getCode())) {
					expriredDay = item.getValue();
					continue;
				}
				if (RightConstants.PW_EXPIRED_IN.equalsIgnoreCase(item.getCode())) {
					expriredIn = item.getValue();
					continue;
				}
			}
			Calendar c = Calendar.getInstance();
			c.setTime(pwDate);
			c.add(Calendar.DATE, Integer.parseInt(expriredIn));
			long time = 24 * 60 * 60 * 1000;
			long diff = 0;
			if (c.getTime().after(date)) {
				diff = (c.getTime().getTime() - date.getTime()) / time;
				if (diff <= Long.parseLong(expriredDay)) {
					return String.valueOf(diff);
				}
				return "";
			} else {
				return "";
			}
		}
		return "";
	}
	
	public void getTree(ModelMap model, HttpServletRequest request, HttpServletResponse response, QnaInDetailForm form)
			throws Exception {
		String parentId = request.getParameter("parentId");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		JSONArray array = null;
		array = catAgencyStructureDao.getTreeRoot(parentId, false);
		pw.print(array);
		pw.close();
	}
	
	public void treeRight(ModelMap map, HttpServletRequest rq, HttpServletResponse rp, QnaInDetailForm form)
			throws Exception {
		rp.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = rp.getWriter();
		JSONArray array = null;
		array = catAgencyStructureDao.getTreeObjectsData();
		pw.print(array);
		pw.close();
	}
}
