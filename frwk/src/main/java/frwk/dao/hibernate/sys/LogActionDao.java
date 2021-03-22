package frwk.dao.hibernate.sys;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.frwk.LogAction;

@Repository(value = "logActionDao")
public class LogActionDao extends SysDao<LogAction> {

	public LogAction getFunctionNameByClassName(String ClassName) {
		List<LogAction> temp = getThreadSession().createCriteria(LogAction.class)
				.add(Restrictions.eq("className", ClassName)).list();
		if (Formater.isNull(temp))
			return null;
		return temp.get(0);
	}

	public ArrayList<LogAction> getAll() {
//        GenericHibernateDAO<LogAction, String> dao = new GenericHibernateDAO<LogAction, String>();
//        // Loai tru log cho transaction, ghi rieng
//        dao.addRestriction(Restrictions.ne("tenClass", "hibernatedto.Transaction"));
//        return dao.searchByCriteria(LogAction.class);

		return (ArrayList<LogAction>) getThreadSession().createCriteria(LogAction.class)
				.add(Restrictions.ne("className", "hibernatedto.Transaction")).list();
	}

}
