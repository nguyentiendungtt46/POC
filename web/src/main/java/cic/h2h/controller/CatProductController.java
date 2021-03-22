package cic.h2h.controller;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StringType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

import cic.h2h.dao.hibernate.CatParIndexDao;
import cic.h2h.dao.hibernate.CatProductDao;
import cic.h2h.dao.hibernate.H2HBaseDao;
import cic.h2h.dao.hibernate.PartnerDao;
import cic.h2h.dao.hibernate.ServiceInfoDao;
import cic.h2h.dao.hibernate.ServiceProductDao;
import cic.h2h.form.CatParIndexForm;
import cic.h2h.form.CatProductForm;
import common.util.FormatNumber;
import common.util.Formater;
import config.CicThreadPoolTaskScheduler;
import entity.CatParIndex;
import entity.CatProduct;
import entity.CatProductCfg;
import entity.Partner;
import entity.ServiceInfo;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.form.ModelForm;
import frwk.utils.ExportExcel;

@Controller
@RequestMapping("/catProduct")
public class CatProductController extends CatalogController<CatProductForm, CatProduct> {

	private static Logger lg = LogManager.getLogger(CatProductController.class);

	@Autowired
	private CatParIndexDao entParIndexDao;

	@Autowired
	private PartnerDao partnerDao;

	@Autowired
	private CatProductDao catProductDao;

	@Autowired
	private ServiceInfoDao serviceInfoDao;

	@Autowired
	private ExportExcel exportExcel;

	@Override
	public BaseDao<CatProduct> createSearchDAO(HttpServletRequest request, CatProductForm form) throws Exception {

		CatProductDao dao = new CatProductDao();
		CatProductForm catProductForm = (CatProductForm) form;
		if (!Formater.isNull(catProductForm.getKeyword_code()))
			dao.addRestriction(Restrictions.like("code", catProductForm.getKeyword_code().trim(), MatchMode.ANYWHERE)
					.ignoreCase());
		if (!Formater.isNull(catProductForm.getKeyword_name()))
			dao.addRestriction(Restrictions.like("name", catProductForm.getKeyword_name().trim(), MatchMode.ANYWHERE)
					.ignoreCase());
		if (!Formater.isNull(catProductForm.getServer_name()))
			dao.addRestriction(Restrictions.sqlRestriction(
					"exists(select 1 from SERVICE_PRODUCT sp where sp.SERVICE_ID=? and sp.PRODUCT_ID ={alias}.id)",
					catProductForm.getServer_name(), StringType.INSTANCE));
		return dao;
	}

	@Override
	public void pushToJa(JSONArray ja, CatProduct temp, CatProductForm modal) throws Exception {
		ja.put("<a class='characterwrap' href = '#' onclick = 'edit(\"" + temp.getId() + "\")'>" + temp.getCode()
				+ "</a>");
		ja.put(temp.getName());
		ja.put(FormatNumber.num2Str(temp.getTimeToLive()));
		ja.put(FormatNumber.num2Str(temp.getMaxSizeApi()));
		ja.put(FormatNumber.num2Str(temp.getFrequency()));
		if (temp.getStatus() != null && temp.getStatus())
			ja.put("X");
		else
			ja.put("");
		if (temp.getCallByJob() != null && temp.getCallByJob())
			ja.put("X");

		else
			ja.put("");
	}

	@Override
	public String getJsp() {

		return "cat_product/view";
	}

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatProductForm form)
			throws Exception {
		List<ServiceInfo> LstService = serviceInfoDao.getAll();
		model.addAttribute("LstService", LstService);
	}

	public void getTree(ModelMap model, HttpServletRequest request, HttpServletResponse response, CatProductForm form)
			throws Exception {
		String parentId = request.getParameter("parentId");
		response.setContentType("text/plain;charset=utf-8");
		PrintWriter pw = response.getWriter();
		JSONArray array = null;
		array = entParIndexDao.getTreeRoot(parentId, true);
		pw.print(array);
		pw.close();
	}

	public void getListTCTD(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			CatProductForm form) throws Exception {
		List<Partner> listTCTD = partnerDao.getListTCTD("");
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray(new ObjectMapper().writeValueAsString(listTCTD));
		out.print(jsonArray);
		out.flush();
		out.close();
	}

	public void getListTCTDById(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			CatProductForm form) throws Exception {
		String id = request.getParameter("id");
		List<Partner> listTCTD = partnerDao.getListTCTD(id);
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-store");
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray(new ObjectMapper().writeValueAsString(listTCTD));
		out.print(jsonArray);
		out.flush();
		out.close();
	}

	/**
	 * Action tinh phi
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @param form
	 * @throws Exception
	 */
	public void caculatFee(ModelMap model, HttpServletRequest request, HttpServletResponse response,
			CatProductForm form) throws Exception {
		CatProductForm productForm = (CatProductForm) form;
		BigDecimal totalFeee = caculatFee(productForm);
		productForm.getCatProduct().setPrice(totalFeee);
	}

	/**
	 * Tinh phi
	 * 
	 * @param productForm
	 * @return
	 */
	private BigDecimal caculatFee(CatProductForm productForm) {
		BigDecimal totalFeee = new BigDecimal(0);
		for (String s : productForm.getArrIdxId()) {
			if (!parentNotAvail(s, productForm.getArrIdxId())) {
				CatParIndex catInx = entParIndexDao.get(CatParIndex.class, s);
				if (catInx.getPrice() != null)
					totalFeee = totalFeee.add(catInx.getPrice());
			}
		}
		return totalFeee;
	}

	private boolean parentNotAvail(String idxId, String[] arrIdxId) {
		CatParIndex idx = entParIndexDao.get(CatParIndex.class, idxId);

		// Khong co parent
		if (idx.getParent() == null)
			return true;

		// Co parent trong danh sach
		for (String s : arrIdxId) {
			if (s.equals(idx.getParent().getId()))
				return false;

		}

		// parent khong co trong danh sach, kiem tra tiep
		return parentNotAvail(idx.getParent().getId(), arrIdxId);
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatProductForm form)
			throws Exception {
		if (form.getCatProduct().getStrFixProduct().equals("1"))
			form.getCatProduct().setFixProduct(true);
		if (form.getCatProduct().getStrFixProduct().equals("0"))
			form.getCatProduct().setFixProduct(false);
		catProductDao.save(form.getModel());

		// Schedule
		List<ServiceInfo> lstService = serviceProductDao.getByProduct(form.getCatProduct());
		for (ServiceInfo s : lstService) {
			if (qnaServiceCode.equals(s.getCode())) {
				cicThreadPoolTaskScheduler.setUpSchedule(CicThreadPoolTaskScheduler.QNA_SVR_CODE,
						form.getCatProduct().getCode(),
						form.getCatProduct().getFrequency() == null
								|| form.getCatProduct().getFrequency().intValue() <= 0 ? 30l
										: form.getCatProduct().getFrequency().longValue(),
						form.getCatProduct().getStatus());
			}

		}
	}

	@Autowired
	private CicThreadPoolTaskScheduler cicThreadPoolTaskScheduler;

	@Override
	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatProductForm form)
			throws Exception {
		String id = rq.getParameter("id");
		CatProduct o = catProductDao.getObject(CatProduct.class, id);
		rs.setContentType("application/json;charset=utf-8");
		rs.setHeader("Cache-Control", "no-store");
		PrintWriter out = rs.getWriter();
		JSONObject jsonObject = new JSONObject(new ObjectMapper().writeValueAsString(o));
		out.print(jsonObject);
		out.flush();
		out.close();

	}

	@Value("${QNA_SERVICE}")
	private String qnaServiceCode;

	@Override
	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatProductForm form)
			throws Exception {
		super.del(model, rq, rs, form);

		List<ServiceInfo> lstService = serviceProductDao.getByProduct(form.getCatProduct());
		for (ServiceInfo s : lstService) {
			if (qnaServiceCode.equals(s.getCode()))
				cicThreadPoolTaskScheduler.detroySchedule(s.getCode(), form.getCatProduct().getCode());

		}
	}

	@Autowired
	private ServiceProductDao serviceProductDao;

	@Override
	public BaseDao<CatProduct> getDao() {
		return catProductDao;
	}

	public void exportExcel(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, CatProductForm form)
			throws Exception {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			String code = rq.getParameter("code");
			String name = rq.getParameter("name");
			String serverId = rq.getParameter("serverId");

			CatProductDao dao = new CatProductDao();

			if (!Formater.isNull(code))
				dao.addRestriction(Restrictions.like("code", code, MatchMode.ANYWHERE).ignoreCase());
			if (!Formater.isNull(name))
				dao.addRestriction(Restrictions.like("name", name, MatchMode.ANYWHERE).ignoreCase());
			if (!Formater.isNull(serverId))
				dao.addRestriction(Restrictions.eq("serviceId", serverId));

			List<CatProduct> LstCatProduct = dao.search();
			LstCatProduct = LstCatProduct == null ? new ArrayList<CatProduct>() : LstCatProduct;

			for (CatProduct catProduct : LstCatProduct) {
				if (catProduct.getFixProduct() != null && catProduct.getFixProduct()) {
					catProduct.setStatusStr("Cố định");
				} else {
					catProduct.setStatusStr("Không phải cố định");
				}
			}

			map.put("reports", LstCatProduct);
			exportExcel.export("Tao_lap_san_pham", rs, map);
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
		}
	}
}
