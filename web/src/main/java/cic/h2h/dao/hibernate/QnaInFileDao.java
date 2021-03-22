package cic.h2h.dao.hibernate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.QnaInFile;

@Repository(value = "qnaInFileDao")
public class QnaInFileDao extends H2HBaseDao<QnaInFile> {
	private static final Logger lg = LogManager.getLogger(QnaInFileDao.class);

	public List<QnaInFile> reports(String tuNgay, String denNgay, String partnerCode, String fileName, String status) {
		// TODO Auto-generated method stub
		List<QnaInFile> list = new ArrayList<QnaInFile>();
		try {
			Criteria criteria = getCurrentSession().createCriteria(QnaInFile.class);
			if (!Formater.isNull(partnerCode))
				criteria.add(Restrictions.like("tctd.id", partnerCode, MatchMode.ANYWHERE).ignoreCase());
			if (!Formater.isNull(fileName))
				criteria.add(Restrictions.like("tenfile", fileName.trim(), MatchMode.ANYWHERE).ignoreCase());
			if (!Formater.isNull(tuNgay))
				criteria.add(Restrictions.ge("ngayHoi", new SimpleDateFormat("dd/MM/yyyy").parse(tuNgay)));
			if (!Formater.isNull(denNgay)) {
				Date currentDate = new SimpleDateFormat("dd/MM/yyyy").parse(denNgay);
				Calendar c = Calendar.getInstance();
				c.setTime(currentDate);
				c.add(Calendar.DATE, 1);
				Date dateAdd = c.getTime();
				criteria.add(Restrictions.le("ngayHoi", dateAdd));
			}

			if (!Formater.isNull(status)) {
				// Chua co ket qua
				if ("1".equals(status))
					criteria.add(
							Restrictions.or(Restrictions.isNull("status"), Restrictions.eq("status", Boolean.FALSE)));
				// Da tra cho tctd
				else if ("3".equals(status))
					criteria.add(Restrictions.isNotNull("ngayTraLoiCuoiCung"));
				else
					criteria.add(Restrictions.and(Restrictions.isNull("ngayTraLoiCuoiCung"), Restrictions.eq("status", Boolean.TRUE)));
			}
			criteria.addOrder(Order.desc("ngayHoi"));
			list = criteria.list();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e.getMessage(),e);
		}
		return list;
	}

}
