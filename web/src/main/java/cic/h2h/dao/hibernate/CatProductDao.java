package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import common.util.ResourceException;
import entity.CatParIndex;
import entity.CatProduct;
import entity.CatProductCfg;
import entity.PartnerService;
import entity.ServiceInfo;

@Repository(value = "catProductDao")
public class CatProductDao extends H2HBaseDao<CatProduct> {
	private static final Logger lg = LogManager.getLogger(CatProductDao.class);

	@Override
	public void save(CatProduct obj) throws Exception {
		if (Formater.isNull(obj.getId())) {
			if (checkCodeProductExists(obj.getCode()))
				throw new ResourceException("M&#227; s&#7843;n ph&#7849;m: " + obj.getCode()
						+ " &#273;&#227; t&#7891;n t&#7841;i trong h&#7879; th&#7889;ng.");
			obj.setCreateDate(new Date());
			obj.setId(null);
			super.save(obj);
			return;
		}

		CatProduct objIndb = getObject(obj);
		if (obj.getPartnerId() == null)
			obj.setPartnerId(objIndb.getPartnerId());

		// Xoa chi tieu ko ton tai
//		List<CatProductCfg> delList = new ArrayList<CatProductCfg>();
//		for (CatProductCfg oldCat : objIndb.getCatProductCfgs()) {
//			boolean delOldAcc = true;
//			for (CatProductCfg newCat : obj.getCatProductCfgs()) {
//				if (oldCat.getIndexId().equals(newCat.getIndexId())) {
//					delOldAcc = false;
//					newCat.setId(oldCat.getId());
//					break;
//				}
//
//			}
//			if (delOldAcc)
//				delList.add(oldCat);
//		}
//		for (CatProductCfg oldCat : delList) {
//			objIndb.getCatProductCfgs().remove(oldCat);
//			getCurrentSession().delete(oldCat);
//		}
		if (obj.getStatus() != null && obj.getStatus()) {
			List<PartnerService> services = getServiceProduct(obj.getId());
			if (!Formater.isNull(services)) {
				for (PartnerService service : services) {
					service.setDisableStatus(true);
					partnerServiceDao.save(service);
				}
			}
		}
		obj.setCode(objIndb.getCode());
		// getCurrentSession().merge(obj);
		super.save(obj);
	}

	@Autowired
	private PartnerServiceDao partnerServiceDao;

	public List<PartnerService> getServiceProduct(String productId) {
		lg.info("BEGIN getServiceProduct productId = " + productId);
		List<PartnerService> catProducts = new ArrayList<PartnerService>();
		Criteria criteria = getCurrentSession().createCriteria(PartnerService.class);
		try {
			criteria.add(Restrictions.eq("catProductId.id", productId).ignoreCase());
			catProducts = criteria.list();
		} catch (Exception e) {
			lg.error(e);
		}
		lg.info("END getServiceProduct productId = " + productId);
		return catProducts;
	}

	public List<CatProduct> getCatProductByServiceId(String serviceId) {
		lg.info("BEGIN getCatProductByServiceId serviceId = " + serviceId);
		List<CatProduct> catProducts = new ArrayList<CatProduct>();
		Criteria criteria = getCurrentSession().createCriteria(CatProduct.class);
		try {
			criteria.add(Restrictions.eq("serviceId", serviceId).ignoreCase());
			catProducts = criteria.list();
		} catch (Exception e) {
			lg.error(e);
		}
		lg.info("END getCatProductByServiceId serviceId = " + serviceId);
		return catProducts;
	}

	@SuppressWarnings("unchecked")
	public boolean checkCodeProductExists(String code) {
		lg.info("BEGIN checkCodeProductExists code = " + code);
		List<CatProduct> catProducts = new ArrayList<CatProduct>();
		Criteria criteria = getCurrentSession().createCriteria(CatProduct.class);
		try {
			criteria.add(Restrictions.eq("code", code).ignoreCase());
			catProducts = criteria.list();
			if (catProducts.size() > 0)
				return true;
		} catch (Exception e) {
			lg.error(e);
		}
		lg.info("END checkCodeProductExists code = " + code);
		return false;
	}

	public CatProduct getCatProductByCode(String code) {
		return (CatProduct) getCurrentSession().createCriteria(CatProduct.class)
				.add(Restrictions.eq("code", code).ignoreCase()).uniqueResult();
	}

	public CatProduct getCatProductByCodeAndSvCode(String code, String svCode) {
		return (CatProduct) getCurrentSession().createCriteria(CatProduct.class)
				.add(Restrictions.eq("code", code).ignoreCase())
				.add(Restrictions
						.sqlRestriction("exists(select 1 from SERVICE_INFO s, SERVICE_PRODUCT sp where s.code = '"
								+ svCode + "' and s.id = sp.SERVICE_ID and sp.PRODUCT_ID = {alias}.id)"))
				.uniqueResult();
	}

	@Autowired
	private ServiceInfoDao serviceInfoDao;

	public List<CatProduct> getAllSvCode(String svcode) {
		Criteria criteria = getCurrentSession().createCriteria(CatProduct.class);
		criteria.add(
				Restrictions.sqlRestriction("exists(select 1 from SERVICE_INFO s, SERVICE_PRODUCT sp where s.code = '"
						+ svcode + "' and s.id = sp.SERVICE_ID and sp.PRODUCT_ID = {alias}.id)"));
		return criteria.list();
	}

}
