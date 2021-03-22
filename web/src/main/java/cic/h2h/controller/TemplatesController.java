package cic.h2h.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import cic.h2h.dao.hibernate.DictMeasureDao;
import cic.h2h.dao.hibernate.TemplatesDao;
import cic.h2h.form.TemplateForm;
import common.util.Formater;
import common.util.KeyValue;
import common.util.ResourceException;
import entity.DictMeasure;
import entity.DictTemp;
import entity.Templates;
import entity.frwk.SysDictParam;
import entity.frwk.SysDictType;
import entity.frwk.SysParam;
import frwk.controller.CatalogController;
import frwk.dao.hibernate.BaseDao;
import frwk.dao.hibernate.sys.SysDictParamDao;
import frwk.dao.hibernate.sys.SysDictTypeDao;
import frwk.dao.hibernate.sys.SysParamDao;
import frwk.utils.Utils;

@Controller
@RequestMapping(value = "/managerTemplate")
public class TemplatesController extends CatalogController<TemplateForm, Templates> {

	private static Logger log = LogManager.getLogger(TemplatesController.class);

	@Autowired
	private TemplatesDao templatesDao;

	@Override
	public BaseDao<Templates> createSearchDAO(HttpServletRequest request, TemplateForm form) throws Exception {

		TemplatesDao dao = new TemplatesDao();
		if (!Formater.isNull(form.getStempNm())) {
			dao.addRestriction(Restrictions.or(
					Restrictions.like("tempNm", form.getStempNm().trim(), MatchMode.ANYWHERE).ignoreCase(),
					Restrictions.like("code", form.getStempNm().trim(), MatchMode.ANYWHERE).ignoreCase()));
		}

		if (!Formater.isNull(form.getProductId())) {
//            ArrayList<ProdTemp> productTemp = ProductImpl.getTemp(new Product(Long.valueOf(productId)), null);
//            ArrayList<Long> tempIds = new ArrayList<Long>();
//            for (ProdTemp temp : productTemp)
//                tempIds.add(temp.getTemplates().getSid());
//            if (tempIds.size() > 0)
//                dao.addRestriction(Restrictions.in("sid", tempIds));
		}
		if (!Formater.isNull(form.getTemplateType()))
			dao.addRestriction(Restrictions.eq("type", Byte.valueOf(form.getTemplateType())));
		if (form.getForAll() != null && form.getForAll()) {
			dao.addRestriction(Restrictions.eq("type", Byte.valueOf((byte) 1)));
			dao.addRestriction(Restrictions.eq("isAllProd", form.getForAll()));
		}
		if (form.getHasMultiInstance() != null && form.getHasMultiInstance()) {
			dao.addRestriction(Restrictions.eq("multiInstance", form.getHasMultiInstance()));
		}

		dao.addOrder(Order.desc("code"));
		return dao;
	}

	@Override
	protected void pushToJa(JSONArray ja, Templates pn, TemplateForm modelForm) throws Exception {
		Templates temp = (Templates) pn;

		ja.put("<a href = '#' onclick = 'edit(\"" + temp.getId() + "\")'>"
				+ StringEscapeUtils.escapeHtml(
						!Formater.isNull(temp.getCode()) ? temp.getCode() + "-" + temp.getTempNm() : temp.getTempNm())
				+ "</a>");
		ja.put(StringEscapeUtils.escapeHtml(temp.getEffDate() == null ? "" : Formater.date2str(temp.getEffDate())));
		ja.put(StringEscapeUtils.escapeHtml(temp.getExpDate() == null ? "" : Formater.date2str(temp.getExpDate())));
	}

	@Override
	public BaseDao<Templates> getDao() {

		return templatesDao;
	}

	@Override
	public String getJsp() {

		return "qtht/quan_ly_bieu_mau";
	}

	@Autowired
	private SysDictParamDao sysDictParamDao;
	@Autowired
	private SysDictTypeDao sysDictTypeDao;

	@Override
	public void initData(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, TemplateForm form)
			throws Exception {
//		listDict = sysDictParamDao.getAllDictMeasure();
		List<KeyValue> listHeaderLevel = new ArrayList<KeyValue>();
		for (int j = 1; j < 5; j++)
			listHeaderLevel.add(new KeyValue(String.valueOf(j), String.valueOf(j)));
		model.addAttribute("listHeaderLevel", listHeaderLevel);
		SysDictType sysDictType = sysDictTypeDao.get(SysDictType.class, "5");
		model.addAttribute("dsControlType", sysDictType.getSysDictParams());
//		lstAppRoles = SysDictParamImpl.getByType(Autodoc.APP_RULE);
		List<SysDictType> dsDictType = sysDictTypeDao.getAll(SysDictType.class);
		model.addAttribute("dsDictType", dsDictType);
//		lstSubjectModel = SubjectModelImpl.getAll();
//		listDictTypes = SysDictParamImpl.getByType(Long.parseLong("38"));
//		lstTemplateType = SysDictParamImpl.getByType(Long.parseLong("40"));
		model.addAttribute("listDict", dictMeasureDao.getAllDictMeasure());
	}

	@Override
	public void save(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, TemplateForm form)
			throws Exception {
		Templates template = form.getTemplate();
		if (Formater.isNull(template.getId()))
			template.setId(null);
		if (Formater.isNull(template.getId()) && !Formater.isNull(template.getCode())
				&& templatesDao.getByCode(template.getCode()) != null)
			throw new ResourceException("M\u00E3 bi\u1EC3u m\u1EABu \u0111\u00E3 t\u1ED3n t\u1EA1i");

		if (!Formater.isNull(template.getLstDictTemps())) {
//			// Check trung dict temp
//			// templatesDao.checkDuplicates(template.getLstDictTemps());
//
			// Xu ly quan he
			for (DictTemp dt : template.getLstDictTemps()) {
				if (!Formater.isNull(dt.getGroupName()))
					dt.setGroupName(dt.getGroupName().trim());
				if (dt.getDictType() == null || dt.getDictType().getId() == null)
					dt.setDictType(null);
				dt.setTemplates(template);
			}

			template.getLstDictTemps().addAll(template.getLstDictTemps());
		}
		// Hop dong tsdb khong co san pham
//		if (template.getType() == 2)
//			template.setLstProdTemps(new ArrayList<ProdTemp>());
//		else {
//			List<Product> pros = ProductImpl.getAllByTemplateType(template.getType());
//			if (template.getIsAllProd() != null && template.getIsAllProd()) {
//				for (Product pro : pros) {
//					boolean bExist = false;
//					for (ProdTemp pt : template.getLstProdTemps()) {
//						if (pro.getId().equals(pt.getProduct().getId())) {
//							bExist = true;
//							template.getProdTemps().add(pt);
//							pt.setTemplates(template);
//							break;
//						}
//					}
//					if (!bExist)
//						template.getProdTemps().add(new ProdTemp(pro, template));
//				}
//
//				for (ProdTemp o : template.getProdTemps()) {
//					logger.info(o.getProduct().getId());
//				}
//			} else {
//				if (!Formater.isNull(template.getLstProdTemps())) {
//					// Check product
//					checkProduct(template, pros);
//
//					// xy ly quan he
//					for (ProdTemp pt : template.getLstProdTemps())
//						pt.setTemplates(template);
//					template.getProdTemps().addAll(template.getLstProdTemps());
//				}
//			}
//
//		}

		if (template.getCode() != null)
			template.setCode(template.getCode().trim());
		templatesDao.saveNumberId(template);
		returnObject(rs, template);
	}

	public void copy(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, TemplateForm form)
			throws Exception {
		Templates template = form.getTemplate();
		template.setCode("COPY OFF: " + template.getCode());
		// Copy set id null
		template.setId(null);
		template.setFastInput(Boolean.FALSE);
		if (!Formater.isNull(template.getLstDictTemps())) {
			// Check trung dict temp
			checkDuplicates(template.getLstDictTemps());

			// Xu ly quan he
			for (DictTemp dt : template.getLstDictTemps()) {
				// Copy set id null
				dt.setId(null);
				if (!Formater.isNull(dt.getGroupName()))
					dt.setGroupName(dt.getGroupName().trim());
				if (dt.getDictType() == null || dt.getDictType().getId() == null)
					dt.setDictType(null);
				dt.setTemplates(template);
			}

			template.getLstDictTemps().addAll(template.getLstDictTemps());
		}
		// Hop dong tsdb, xnsd khong co san pham
//		if (template.getType() == 2 || template.getType() == 3)
//			template.setLstProdTemps(new ArrayList<ProdTemp>());
//		else {
//			List<Product> pros = ProductImpl.getAllByTemplateType(template.getType());
//			if (!Formater.isNull(template.getLstProdTemps())) {
//				// Check trung product
//				checkProduct(template, pros);
//
//				// xy ly quan he
//				for (ProdTemp pt : template.getLstProdTemps()) {
//					pt.setTemplates(template);
//					// Khong copy thuoc tinh nhap nhanh
//					pt.setFastInput(Boolean.FALSE);
//				}
//
//				template.getProdTemps().addAll(template.getLstProdTemps());
//			}
//		}

		if (template.getCode() != null)
			template.setCode(template.getCode().trim());

		if (!Formater.isNull(template.getCode()) && templatesDao.getByCode(template.getCode()) != null)
			throw new ResourceException("M\u00E3 bi\u1EC3u m\u1EABu \u0111\u00E3 t\u1ED3n t\u1EA1i");
		// set null to copy
		template.setId(null);
//		for (ProdTemp t : template.getProdTemps()) {
//			t.setId(null);
//		}
		for (DictTemp t : template.getLstDictTemps()) {
			t.setId(null);
		}

		templatesDao.saveNumberId(template);
	}

	public void edit(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, TemplateForm form)
			throws Exception {
		String id2 = rq.getParameter("id");
		Templates template = templatesDao.get(Templates.class, id2);
		for (DictTemp dt : template.getLstDictTemps()) {
			if (!Formater.isNull(dt.getGroupName()) && dt.getGroupName().indexOf("{") > 0
					&& dt.getGroupName().indexOf("}") > 0) {
				String[] temp1 = dt.getGroupName().split("\\{");
				String realGroupName = temp1[0];
				String groupClass = temp1[1].split("}")[0];
				dt.setRealGroupName(realGroupName);
				dt.setGroupClass(groupClass);

			} else
				dt.setRealGroupName(dt.getGroupName());

		}
		Collections.sort(template.getLstDictTemps(), new Comparator<DictTemp>() {

			@Override
			public int compare(final DictTemp before, final DictTemp afer) {
				return before.compareTo(afer);
			}
		});
		Utils.jsonSerialize(rs, template);
	}

	public void del(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, TemplateForm form) throws Exception {
		// if (!RightUtils.haveRight(RightConstants.SYSPARAM_Delete,
		// (ApplicationContext)rq.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT)))
		// {
		// throw new ResourceException("not_access");
		// } else {
		// return super.del();
		// }
		super.del(model, rq, rs, form);
	}

//	public static void checkProduct(Templates template, List<Product> pros) throws ResourceException {
//		ArrayList<ProdTemp> lstProduct = template.getLstProdTemps();
//		Set<Long> lump = new HashSet<Long>();
//		for (ProdTemp productTemp : lstProduct) {
//			// Kiem tra san pham thuoc nhom san pham
//			boolean bFound = false;
//			for (Product pro : pros) {
//				if (productTemp.getProduct().getId().equals(pro.getId())) {
//					bFound = true;
//					break;
//				}
//			}
//			if (!bFound) {
//				SysDictParam productType = SysDictParamImpl.getByTypeAndCode(Long.valueOf("40"),
//						String.valueOf(template.getType()));
//				Product pro = (Product) BaseImpl.getObject(Product.class, productTemp.getProduct().getId());
//				String productPath = pro.getFullGroupPath() + "-->" + pro.getId() + "-" + pro.getPrdNm();
//				throw new ResourceException(String.format("S&#7843;n ph&#7849;m %s kh&#244;ng thu&#7897;c %s",
//						new Object[] { productPath, productType.getDescription() }));
//			}
//
//			// check trung
//			if (lump.contains(productTemp.getProduct().getId()))
//				throw new ResourceException("Tr\u00F9ng s\u1EA3n ph\u1EA9m");
//			lump.add(productTemp.getProduct().getId());
//		}
//	}

	@Autowired
	DictMeasureDao dictMeasureDao;

	public void checkDuplicates(List<DictTemp> lstDictTemp) throws ResourceException, Exception {
		for (DictTemp dt : lstDictTemp)
			dt.sparateClass();
		for (int i = 0; i < lstDictTemp.size() - 1; i++) {
			for (int j = i + 1; j < lstDictTemp.size(); j++) {
				if (lstDictTemp.get(i).getDictMeasure().getId().equals(lstDictTemp.get(j).getDictMeasure().getId())) {
					DictMeasure ms = dictMeasureDao.get(DictMeasure.class, lstDictTemp.get(i).getDictMeasure().getId());
					// .getByIdInThreadSession(lstDictTemp.get(i).getDictMeasure().getId());
					throw new ResourceException("Tr\u00F9ng ch\u1EC9 ti\u00EAu: " + ms.getId() + "-" + ms.getColDesc()
							+ ", d\u00F2ng: " + (i + 1) + ", " + (j + 1));
				}
				if (!Formater.isNull(lstDictTemp.get(i).getDictMeasure().getPath()) && lstDictTemp.get(i)
						.getDictMeasure().getPath().equals(lstDictTemp.get(j).getDictMeasure().getPath())) {
					if (!Formater.isNull(lstDictTemp.get(i).getGroupName())
							&& lstDictTemp.get(i).getGroupName().equals(lstDictTemp.get(j).getGroupName()))
						throw new ResourceException("Tr\u00F9ng path: " + lstDictTemp.get(i).getDictMeasure().getPath()
								+ ", d\u00F2ng: " + (i + 1) + ", " + (j + 1));
				}
			}
		}
	}

	@Autowired
	SysParamDao sysParamDao;

	public String upload(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, TemplateForm form)
			throws Exception {
		SysParam sysParam = sysParamDao.getSysParamByCode("UPLFLD");
		String folderPath = sysParam.getValue();
		String id = rq.getParameter("id");
//		if (!Formater.isNull(id)) {
//			JSONObject resultUpload = webservices.Utils.uploadFileServer(folderPath, inputFile);
//			if (resultUpload.get("status").equals("success")) {
//				String strResult = (String) resultUpload.get("path");
//				Long id2 = Long.parseLong(id);
//				template = ManagerTemplateImpl.getByIdInThreadSession(id2);
//				template.setPathFile(strResult);
//				template.setFileName(sfileName);
//				ManagerTemplateImpl.updateTemp(template);
//
//				// Tra duong dan lai cho client
//				HttpServletResponse response = ServletActionContext.getResponse();
//				response.setContentType("application/json;charset=utf-8");
//				response.setHeader("Cache-Control", "no-store");
//				PrintWriter out = response.getWriter();
//				out.print(resultUpload);
//				out.close();
//			} else {
//				throw new ResourceException("T&#x1EA3;i file kh&#xF4;ng th&#xE0;nh c&#xF4;ng");
//			}
//		} else {
//			throw new ResourceException(
//					"Ph&#x1EA3;i l&#x1B0;u bi&#x1EC3;u m&#x1EAB;u tr&#x1B0;&#x1EDB;c khi t&#x1EA3;i ph&#x1EA3;i l&#xEA;n");
//		}
		return null;
	}

	public String downloadTemplate(ModelMap model, HttpServletRequest rq, HttpServletResponse rs, TemplateForm form)
			throws FileNotFoundException, IOException, Exception {
		templatesDao.load(form.getTemplate());
		if (!Formater.isNull(form.getTemplate().getPathFile())) {
			// InputStream is =
			// webservices.Utils.downloadFileECM(constants.Utils.DOWNLOAD_FILE_ECM_URL,
			// temp.getPathFile());
			InputStream is = new FileInputStream(new File(form.getTemplate().getPathFile()));
			rs.addHeader("Content-Disposition", "attachment; filename=" + form.getTemplate().getFileName());
			OutputStream responseOutputStream = rs.getOutputStream();

			int bytes;
			while ((bytes = is.read()) != -1) {
				responseOutputStream.write(bytes);
			}
			is.close();
			responseOutputStream.close();
		} else {
			throw new ResourceException("Ch&#x1B0;a t&#x1EA3;i l&#xEA;n file m&#x1EAB;u");
		}
		return null;
	}

}
