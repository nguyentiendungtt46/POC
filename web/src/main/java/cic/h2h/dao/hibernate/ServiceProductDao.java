package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import entity.CatProduct;
import entity.ServiceInfo;
import entity.ServiceProduct;
import frwk.constants.Constants;

@Repository(value = "serviceProductDao")
public class ServiceProductDao extends H2HBaseDao<ServiceProduct> {

	// @Cacheable(value = "getListbyServiceId", unless="#result == null")
	public List<ServiceProduct> getListbyServiceId(String serviceId) {
		List<ServiceProduct> products = new ArrayList<ServiceProduct>();
		Criteria criteria = getCurrentSession().createCriteria(ServiceProduct.class);
		criteria.add(Restrictions.eq("serviceInfoId.id", serviceId));
		products = criteria.list();
		return products;
	}

	@Autowired
	private ServiceInfoDao serviceInfoDao;

	public List<ServiceProduct> getListbyProductId(String productId) {
		List<ServiceProduct> products = new ArrayList<ServiceProduct>();
		Criteria criteria = getCurrentSession().createCriteria(ServiceProduct.class);
		criteria.add(Restrictions.eq("catProductId.id", productId));
		criteria.add(Restrictions.eq("serviceInfoId.id", serviceInfoDao.getServiceInFo(Constants.QA_CODE).getId()));
		products = criteria.list();
		return products;
	}

	public List<ServiceInfo> getByProduct(CatProduct product) {
		List<ServiceInfo> rs = new ArrayList<ServiceInfo>();
		List<ServiceProduct> lstTemp = (List<ServiceProduct>) getCurrentSession().createCriteria(ServiceProduct.class)
				.add(Restrictions.eq("catProductId", product)).list();
		for (ServiceProduct sp : lstTemp) {
			if (!rs.contains(sp.getServiceInfoId()))
				rs.add(sp.getServiceInfoId());
		}
		return rs;
	}

}
