package cic.h2h.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.databind.ObjectMapper;

import cic.h2h.dao.hibernate.RpSumBranchDao;
import cic.h2h.dao.hibernate.RpSumDao;
import cic.h2h.dao.hibernate.RpTypeDao;
import cic.h2h.dao.hibernate.RpValidateDetailDao;
import cic.h2h.form.RpValidateDetailForm;
import common.util.Formater;
import common.util.ResourceException;
import entity.RpSum;
import entity.RpSumBranch;
import entity.RpSumBranchErr;
import entity.RpType;
import entity.RpValidateDetail;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/rpValidate")
public class RpValidateDetailController extends CatalogController<RpValidateDetailForm, RpValidateDetail> {
	private static Logger lg = LogManager.getLogger(RpValidateDetailController.class);
	@Autowired
	private RpValidateDetailDao rpValidateDetailDao;
	@Autowired
	private RpSumBranchDao rpSumBranchDao;
	@Autowired
	private RpSumDao rpSumDao;

	@Autowired
	private RpTypeDao rpTypeDao;

	@Override
	public BaseDao<RpValidateDetail> createSearchDAO(HttpServletRequest request, RpValidateDetailForm form)
			throws Exception {
		RpValidateDetailDao rpValidateDetailDao = new RpValidateDetailDao();
		RpValidateDetailForm detailForm = (RpValidateDetailForm) form;
		rpValidateDetailDao.addRestriction(Restrictions.eq("rpSum.id", detailForm.getReportId()));
		if (!Formater.isNull(detailForm.getErrorCode())) {
			rpValidateDetailDao.addRestriction(
					Restrictions.like("maLoi", detailForm.getErrorCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(detailForm.getCustomerCode())) {
			rpValidateDetailDao.addRestriction(
					Restrictions.like("maKH", detailForm.getCustomerCode().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(detailForm.getMaCT())) {
			rpValidateDetailDao.addRestriction(
					Restrictions.like("maChiTieu", detailForm.getMaCT().trim(), MatchMode.ANYWHERE).ignoreCase());
		}
		return rpValidateDetailDao;
	}

	@Override
	public void pushToJa(JSONArray ja, RpValidateDetail temp, RpValidateDetailForm modal) throws Exception {
		ja.put(temp.getMaCN());
		ja.put(temp.getTenCN());
		ja.put(temp.getMaKH());
		ja.put(temp.getTenKH());
		ja.put(temp.getDongLoi());
		ja.put(temp.getMaChiTieu());
		ja.put(temp.getGiaTriLoi());
		ja.put(temp.getMaLoi());
		ja.put(temp.getMoTaLoi());
	}

	@Override
	public BaseDao<RpValidateDetail> getDao() {
		return rpValidateDetailDao;
	}

	@Override
	public String getJsp() {
		return "rp_sum/validate_view";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, RpValidateDetailForm form)
			throws Exception {
		String id = rq.getParameter("reportId");
		lg.info("ReportId : " + id);
		RpSum rpSum = rpSumDao.getObject(RpSum.class, id);
		if (rpSum != null) {
			model.addAttribute("errors", rpSum.getRpSumErrors());

			if (rpSumBranchDao.existsError(rpSum)) {
				model.addAttribute("branchs", rpSum.getRpSumBranchs());

			}
			if (rpValidateDetailDao.exists(rpSum)) {
				model.addAttribute("errorDetail", true);

			}

			if (rpSum.getReportDate() != null) {
				rpSum.setStrReportDate(Formater.date2str(rpSum.getReportDate()));
			}
			if (rpSum.getAst() != null) {
				rpSum.setStrAst(Formater.num2str(rpSum.getAst()));
			}
			if (rpSum.getBal() != null) {
				rpSum.setStrBal(Formater.num2str(rpSum.getBal()));
			}
			if (rpSum.getBondAmt() != null) {
				rpSum.setStrBondAmt(Formater.num2str(rpSum.getBondAmt()));
			}
			if (rpSum.getIvst() != null) {
				rpSum.setStrInst(Formater.num2str(rpSum.getIvst()));
			}
			if (rpSum.getPmtAmt() != null) {
				rpSum.setStrPmtAmt(Formater.num2str(rpSum.getPmtAmt()));
			}
			model.addAttribute("rpSum", rpSum);
		}
		HashMap<String, List<?>> abc = rpSumDao.reportGroup(rpSum);
		// totalBalAll: Loai tien, so tien noi bang, so tien ngoai bang, so tien cam ket
		List<String[]> totalBalAll = (List<String[]>) abc.get("totalBalAll");
		if (totalBalAll != null && totalBalAll.size() > 0)
			model.addAttribute("totalBalAll", totalBalAll);

		// totalBalByCurrency: Loai tien, so tien
		List<String[]> totalBalByCurrency = (List<String[]>) abc.get("totalBalByCurrency");
		if (totalBalByCurrency != null && totalBalByCurrency.size() > 0)
			model.addAttribute("totalBalByCurrency", totalBalByCurrency);

		// k5
		List<String[]> rCursorK5 = (List<String[]>) abc.get("rCursorK5");
		if (totalBalByCurrency != null && rCursorK5.size() > 0)
			model.addAttribute("rCursorK5", rCursorK5);

		RpType rpType = rpTypeDao.get(RpType.class, rpSum.getReportCode());
		if (rpType != null)
			model.addAttribute("nameTable", rpType.getTableName());

	}

	@Autowired
	private ExportExcel exportExcel;

	public void exportFileExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			RpValidateDetailForm form) {
		try {
			String reportId = rq.getParameter("reportId");
			String typeReport = rq.getParameter("typeReport");
			if (Formater.isNull(reportId) || Formater.isNull(typeReport)) {
				throw new ResourceException("Khong ton tai bao bao");
			}
			RpSum rpSum = rpSumDao.getObject(RpSum.class, reportId);
			if (rpSum == null) {
				throw new ResourceException("Khong ton tai bao bao");
			}
			Map<String, Object> map = new HashMap<String, Object>();
			if ("detail".equalsIgnoreCase(typeReport)) {
				List<RpValidateDetail> details = rpValidateDetailDao.getRpValidateDetailDaoByReportId(reportId);
				map.put("details", details);
				map.put("reportCode", rpSum.getReportCode());
				map.put("reportDate", rpSum.getReportDate() + "");
				map.put("reportName", rpSum.getFileName());
				exportExcel.export("Danh_sach_loi_bao_cao", rs, map);
			} else if ("errorBranch".equalsIgnoreCase(typeReport)) {
				map.put("reportCode", rpSum.getReportCode());
				map.put("reportDate", rpSum.getReportDate() + "");
				List<RpSumBranchErr> branchErrs = new ArrayList<RpSumBranchErr>();
				for (RpSumBranch branch : rpSum.getRpSumBranchs()) {
					branchErrs.addAll(branch.getRpSumBranchErrs());
				}
				map.put("details", branchErrs);
				exportExcel.export("Danh_sach_loi_chi_nhanh", rs, map);
			} else if ("errorReport".equalsIgnoreCase(typeReport)) {
				map.put("reportCode", rpSum.getReportCode());
				map.put("reportDate", rpSum.getReportDate() + "");
				map.put("details", rpSum.getRpSumErrors());
				exportExcel.export("Danh_sach_loi_chung", rs, map);
			}

		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e);
		}
	}

	public void listKhachHangError(ModelMap model, HttpServletRequest rq, HttpServletResponse rs,
			RpValidateDetailForm form) {
		try {
			String reportId = rq.getParameter("reportId");
			String pType = rq.getParameter("p_type");
			if (Formater.isNull(reportId)) {
				throw new ResourceException("khong tim thay reportId theo request");
			}
			RpSum rpSum = rpSumDao.getObject(RpSum.class, reportId);
			if (rpSum == null) {
				throw new ResourceException("khong tim thay rp_sum theo id");
			}
			HashMap<String, List<?>> abc = rpSumDao.listCustomerError(rpSum, pType);
			// totalBalAll: Loai tien, so tien noi bang, so tien ngoai bang, so tien cam ket
			List<String[]> rCursor = (List<String[]>) abc.get("rCursor");
			if (rCursor != null && rCursor.size() > 0)
				model.addAttribute("rCursor", rCursor);
			PrintWriter pw = rs.getWriter();
			JSONArray array = null;
			array = new JSONArray(new ObjectMapper().writeValueAsString(rCursor));
			pw.print(array);
			pw.close();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e);
		}
	}
}
