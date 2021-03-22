package frwk.dao.hibernate.sys;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import common.util.Formater;
import entity.Partner;
import entity.PartnerBranch;

@Repository("partnerBranchDao")
public class PartnerBranchDao extends SysDao<PartnerBranch>{

	@SuppressWarnings("unchecked")
	public List<PartnerBranch> getbranchs(String code, String value, String paramType) {
		Criteria criteria = getCurrentSession().createCriteria(PartnerBranch.class);
		if (!Formater.isNull(code)) {
			criteria.add(Restrictions.like("code", code, MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(value)) {
			criteria.add(Restrictions.like("name", value, MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(paramType)) {
			criteria.add(Restrictions.eq("partnerId.id", paramType));
		}
		return criteria.list();
	}

	public List<PartnerBranch> getBranch(Partner partner) {
		return getCurrentSession().createCriteria(PartnerBranch.class).add(Restrictions.eq("partnerId", partner)).list();
	}

}
