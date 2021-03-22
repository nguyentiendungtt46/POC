package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.PartnerService;
import entity.ServiceInfo;
import entity.ServiceProduct;

@Repository(value = "serviceInfoDao")
public class ServiceInfoDao extends H2HBaseDao<ServiceInfo> {

	private static final Logger log = LogManager.getLogger(ServiceInfoDao.class);

	public List<ServiceInfo> getAll() {
		log.info("BEGIN getAll serviceinfo");
		List<ServiceInfo> infos = new ArrayList<ServiceInfo>();
		Criteria cr = getCurrentSession().createCriteria(ServiceInfo.class).addOrder(Order.desc("serviceName"));
		try {
			infos = cr.list();
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
		}
		log.info("END getAll serviceinfo");
		return infos;
	}

	public List<ServiceInfo> getAllGWService() {
		return getCurrentSession().createCriteria(ServiceInfo.class).add(Restrictions.eq("appType", Short.valueOf("1")))
				.addOrder(Order.desc("serviceName")).list();
	}

	public List<ServiceInfo> getServiceInCore(Short appType) {
		return getCurrentSession().createCriteria(ServiceInfo.class)
				.add(Restrictions.or(Restrictions.isNull("appType"), Restrictions.eq("appType", appType)))
				.addOrder(Order.desc("serviceName")).list();
	}

	public ServiceInfo getServiceInFo(String code) {
		return (ServiceInfo) getCurrentSession().createCriteria(ServiceInfo.class).add(Restrictions.eq("code", code))
				.uniqueResult();
	}

	public void save(ServiceInfo serviceInfo) throws Exception {
		if (Formater.isNull(serviceInfo.getId())) {
			super.save(serviceInfo);
			return;
		}
		ServiceInfo serviceDb = getObject(serviceInfo);
		// delete service product not exsting
		Map<String, String> map = new HashMap<String, String>();
		if (!Formater.isNull(serviceInfo.getServiceProducts())) {
			for (ServiceProduct item : serviceInfo.getServiceProducts()) {
				if (item.getId() == null)
					continue;
				map.put(item.getId(), item.getId());
			}
		}
		if (!Formater.isNull(serviceDb.getServiceProducts())) {
			for (ServiceProduct serProduct : serviceDb.getServiceProducts()) {
				if (serProduct == null)
					continue;
				String checkDel = null;
				checkDel = map.get(serProduct.getId());
				if (checkDel == null) {
					// delete partnerService when delete product in service
					List<PartnerService> partnerService = getPartnerService(serProduct.getServiceInfoId().getId(),
							serProduct.getCatProductId().getId());
					if (!Formater.isNull(partnerService)) {
						for (PartnerService service : partnerService) {
							getCurrentSession().delete(service);
						}
					}
					getCurrentSession().delete(serProduct);
				}

			}
		}
		super.save(serviceInfo);
		// getCurrentSession().merge(serviceInfo);
	}

	public List<PartnerService> getPartnerService(String serviceId, String productId) {
		List<PartnerService> partnerService = new ArrayList<PartnerService>();
		Criteria criteria = getCurrentSession().createCriteria(PartnerService.class);
		criteria.add(Restrictions.eq("serviceInfoId.id", serviceId));
		criteria.add(Restrictions.eq("catProductId.id", productId));
		partnerService = criteria.list();
		return partnerService;
	}

	public List<ServiceInfo> getServiceByOperation() {
		List<ServiceInfo> infos = new ArrayList<ServiceInfo>();
		Criteria cr = getCurrentSession().createCriteria(ServiceInfo.class).add(Restrictions.or(
				Restrictions.in("publishOperation", "VanTinChung"), Restrictions.eq("publishOperation", "TimKiemKH")));
		try {
			infos = cr.list();
		} catch (Exception e) {
			// TODO: handle exception
			log.error(e);
		}
		return infos;
	}

	public ServiceInfo findServiceByRequestClass(String name) {
		return (ServiceInfo) getCurrentSession().createCriteria(ServiceInfo.class)
				.add(Restrictions.eq("requestClass", name)).add(Restrictions.eq("appType", (short) 1)).uniqueResult();
	}

	public ServiceInfo getByCode(String svcode) {
		return (ServiceInfo) getCurrentSession().createCriteria(ServiceInfo.class).add(Restrictions.eq("code", svcode))
				.uniqueResult();
	}
}
