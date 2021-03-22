package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.Partner;
import entity.PartnerConnectIp;
import entity.PartnerService;

@Repository(value = "partnerDao")
public class PartnerDao extends H2HBaseDao<Partner> {
	private static final Logger lg = LogManager.getLogger(PartnerDao.class);

	@SuppressWarnings("unchecked")
	public List<Partner> getListTCTD(String getById) {
		Criteria cr = getCurrentSession().createCriteria(Partner.class);
		if (Formater.isNull(getById)) {
			cr.add(Restrictions.eq("intcde", "99901"));
			cr.add(Restrictions.or(Restrictions.isNull("foreignBank"), Restrictions.eq("foreignBank", 0)));
		} else {
			Partner parent = getObject(Partner.class, getById);
			String subcode = parent.getCode().substring(2, 5);
			cr.add(Restrictions.isNull("intcde"));
			// cr.add(Restrictions.like("code", subcode, MatchMode.ANYWHERE));
			cr.add(Restrictions.sqlRestriction("substr(code, 3, 3) = '" + subcode + "'"));
		}
		return cr.list();
	}
	
	public Partner getPartnerByCode(String code) {
		Criteria criteria = getCurrentSession().createCriteria(Partner.class);
		criteria.add(Restrictions.eq("code", code));
		return (Partner) criteria.uniqueResult();
	}
	
	public Partner getPartnerById(String id) {
		Criteria criteria = getCurrentSession().createCriteria(Partner.class);
		criteria.add(Restrictions.eq("id", id));
		return (Partner) criteria.uniqueResult();
	}

	public void save(Partner partner) throws Exception {
		if (Formater.isNull(partner.getId())) {
			super.save(partner);
			return;
		}

		Partner partnerIndb = getObject(partner);

		// Xoa IP khong ton tai
		List<PartnerConnectIp> delIp = new ArrayList<PartnerConnectIp>();
		for (PartnerConnectIp oldAcc : partnerIndb.getPartnerConnectIps()) {
			boolean delOldAcc = true;
			for (PartnerConnectIp newAcc : partner.getPartnerConnectIps()) {
				if (oldAcc.getId().equals(newAcc.getId())) {
					delOldAcc = false;
					break;
				}

			}
			if (delOldAcc)
				delIp.add(oldAcc);
		}
		for (PartnerConnectIp oldAcc : delIp) {
			partnerIndb.getPartnerConnectIps().remove(oldAcc);
			getCurrentSession().delete(oldAcc);
		}
		
		//delete partner service not exsting
		List<PartnerService> delPS = new ArrayList<PartnerService>();
		for (PartnerService oldService: partnerIndb.getPartnerServices()) {
			boolean delOldPS = true;
			for (PartnerService newService : partner.getPartnerServices()) {
				if (oldService.getId().equalsIgnoreCase(newService.getId())) {
					delOldPS = false;
					break;
				}
			}
			if (delOldPS)
				delPS.add(oldService);
		}
		for(PartnerService item : delPS) {
			partnerIndb.getPartnerServices().remove(item);
			getCurrentSession().delete(item);
		}

		super.save(partner);

	}

	public List<Partner> getPanters(String code, String name) {
		Criteria criteria = getCurrentSession().createCriteria(Partner.class);
		if (!Formater.isNull(code)) {
			criteria.add(Restrictions.like("code", code, MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(name)) {
			criteria.add(Restrictions.like("name", name, MatchMode.ANYWHERE).ignoreCase());
		}
		criteria.addOrder(Order.asc("name"));
		return criteria.list();
	}
	
	public boolean checkUsePhone(String phone) {
		long count = (long) getThreadSession().createCriteria(Partner.class)
				.add(Restrictions.like("phone", phone, MatchMode.ANYWHERE).ignoreCase()).setProjection(Projections.rowCount()).uniqueResult();
		return (count > 0 ? true : false);
	}

}
