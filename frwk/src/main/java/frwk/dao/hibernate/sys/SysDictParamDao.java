package frwk.dao.hibernate.sys;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.frwk.SysDictParam;
import entity.frwk.SysDictType;

@Repository(value = "sysDictParamDao")
public class SysDictParamDao extends SysDao<SysDictParam> {
	private static final Logger logger = LogManager.getLogger(SysDictParamDao.class);
	public List<SysDictParam> getByType(String sysTypeCode) {
		List<SysDictType> tmpDicTypes = (List<SysDictType>) getCurrentSession().createCriteria(SysDictType.class)
				.add(Restrictions.eq("code", sysTypeCode)).list();
		return (List<SysDictParam>) getCurrentSession().createCriteria(SysDictParam.class)
				.add(Restrictions.eq("sysDictType", tmpDicTypes.get(0))).list();
	}
	@Override
	public void save(SysDictParam s) throws Exception {
		SysDictParam sysDictParam = (SysDictParam)s;
		if(sysDictParam.getSysDictType()!=null && Formater.isNull(sysDictParam.getSysDictType().getId()))
			sysDictParam.setSysDictType(null);
		super.save(sysDictParam);
	}
	
	@SuppressWarnings("unchecked")
	public List<SysDictParam> sysDictParams(String code, String value, String paramType) {
		Criteria criteria = getCurrentSession().createCriteria(SysDictParam.class);
		if (!Formater.isNull(code)) {
			criteria.add(Restrictions.like("code", code, MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(value)) {
			criteria.add(Restrictions.like("value", value, MatchMode.ANYWHERE).ignoreCase());
		}
		if (!Formater.isNull(paramType)) {
			criteria.add(Restrictions.eq("sysDictType.id", paramType));
		}
		return criteria.list();
	}
}
