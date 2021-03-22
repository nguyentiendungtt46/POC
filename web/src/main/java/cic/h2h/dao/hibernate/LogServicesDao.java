package cic.h2h.dao.hibernate;

import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.LogService;
import entity.LogServiceTransient;
import entity.ServiceInfo;
import frwk.dao.hibernate.sys.SysDao;
import vn.com.cmc.utils.DateUtils;
import vn.com.cmc.utils.Utils;

@Repository(value = "logServicesDao")
public class LogServicesDao extends SysDao<LogServiceTransient> {
	static Logger lg = LogManager.getLogger(LogServicesDao.class);

	public LogService getLogCoreServiceById(String Id) throws Exception {
		return (LogService) getThreadSession().get(LogService.class, Id);
	}

	public void saveLog(LogService logService, Long status, String message, String responseCode, String productCode,
			Object responseContent, String printStackTrace, Date startDate, Object inputContent,
			ServiceInfo serviceInfo, String type, String userRequest) throws Exception {Date endDate = new Date();
			if (logService == null)
				logService = new LogService();

			long elapsedTime = DateUtils.minusDate(endDate, startDate);
			logService.setEndTime(endDate);
			logService.setElapsedTime(elapsedTime);
			logService.setStatus(status);
			logService.setMessage(message);
			logService.setResponseCode(responseCode);
			logService.setProductCode(productCode);
			logService.setUserRequest(userRequest);
			if (!Utils.isNullObject(responseContent))
				logService.setResponseContent(Utils.convertObjToXML(responseContent, responseContent.getClass()));
			if (!Utils.isNullObject(inputContent))
				logService.setRequestContent(Utils.convertObjToXML(inputContent, inputContent.getClass()));
			logService.setStartTime(startDate);
			logService.setServiceInfo(serviceInfo);
			if (!Formater.isNull(type))
				logService.setType(Short.valueOf(type));
			if (!Utils.isNullOrEmpty(printStackTrace))
				logService.setStackTrace(printStackTrace);
			//getCurrentSession().beginTransaction();
			getCurrentSession().save(logService);
			//getCurrentSession().getTransaction().commit();
			lg.info("End saveLog");
	}
}
