package cic.h2h.dao.hibernate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.QnaOutFile;
import frwk.dao.hibernate.sys.SysDao;

@Repository(value = "qnaOutFileDao")
public class QnaOutFileDao extends SysDao<QnaOutFile> {
	static Logger lg = LogManager.getLogger(QnaOutFileDao.class);

	
	public List<QnaOutFile> reports(String tuNgay, String denNgay, String partnerCode, String fileName, String status) {
		List<QnaOutFile> outFiles = new ArrayList<QnaOutFile>();
		try {
			Criteria criteria = getCurrentSession().createCriteria(QnaOutFile.class);
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

			if (!Formater.isNull(tuNgay)) {
				criteria.add(Restrictions.ge("thoidiemtrachotctd", df.parse(tuNgay)));
			}
			if (!Formater.isNull(denNgay)) {
				Date currentDate = new SimpleDateFormat("dd/MM/yyyy").parse(denNgay);
				Calendar c = Calendar.getInstance();
				c.setTime(currentDate);
				c.add(Calendar.DATE, 1);
				Date dateAdd = c.getTime();
				criteria.add(Restrictions.le("thoidiemtrachotctd", dateAdd));
			}

			// Search by file name
			if (!Formater.isNull(fileName)) {
				criteria.add(Restrictions.like("tenfile", fileName.trim(), MatchMode.ANYWHERE)
						.ignoreCase());
			}

			// Search by ma tctd
			if (!Formater.isNull(partnerCode))
				criteria.add(Restrictions.eq("maToChucTinDung", partnerCode));

			if (!Formater.isNull(status)) {
				if ("1".equals(status))
					criteria.add(Restrictions.isNull("thoidiemtrachotctd"));
				else if ("2".equals(status))
					criteria.add(Restrictions.isNotNull("thoidiemtrachotctd"));

			}
			criteria.addOrder(Order.desc("ngayCoKetQua"));
			outFiles = criteria.list();
		} catch (Exception e) {
			// TODO: handle exception
			lg.error(e.getMessage(),e);
		}
		return outFiles;
	}
}
