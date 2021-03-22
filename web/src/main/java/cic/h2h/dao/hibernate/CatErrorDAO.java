package cic.h2h.dao.hibernate;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import common.util.ResourceException;
import entity.CatError;

@Repository(value = "catErrorDAO")
public class CatErrorDAO extends H2HBaseDao<CatError> {
	public CatError getByCode(String errorCode) throws ResourceException {
		if (Formater.isNull(errorCode))
			throw new ResourceException("Ma hoac loai loi null");
		return (CatError) getThreadSession().createCriteria(CatError.class).add(Restrictions.eq("code", errorCode))
				.uniqueResult();
	}

}
