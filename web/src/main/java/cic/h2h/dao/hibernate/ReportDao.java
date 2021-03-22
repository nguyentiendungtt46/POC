package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import common.util.DateTimeUtil;
import common.util.Formater;
import dto.ReportDTO;

@Repository(value = "reportDao")
public class ReportDao extends H2HBaseDao<ReportDTO> {
	static Logger lg = LogManager.getLogger(ReportDao.class);

	public List<ReportDTO> getReportConnectStatus(String serviceInfos, String startTime, String endTime) {
		List<ReportDTO> lst = new ArrayList<ReportDTO>();
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("select a.DESTINATION_OPERATION as serviceId, a.service_name as serviceName,");
			sb.append("    (select count(1) from log_services b where a.id = b.service_code ");
			sb.append("        and b.start_time <= TO_DATE(:endTime, 'DD/MM/YYYY HH24:MI:SS')");
			sb.append("        and b.start_time >= TO_DATE(:startTime, 'DD/MM/YYYY HH24:MI:SS') and ROWNUM = 1 ");
			sb.append("    ) as serviceStatus");
			sb.append(" from service_info a where a.app_type = 1 ");
			if (!Formater.isNull(serviceInfos)) {
				sb.append(" and a.id = :id ");
			}
			SQLQuery sqlQuery = getCurrentSession().createSQLQuery(sb.toString());
			if (!Formater.isNull(serviceInfos)) {
				sqlQuery.setParameter("id", serviceInfos);
			}
			sqlQuery.setParameter("startTime", startTime);
			sqlQuery.setParameter("endTime", endTime);

			lst = sqlQuery.addScalar("serviceId").addScalar("serviceName").addScalar("serviceStatus", new StringType())
					.setResultTransformer(Transformers.aliasToBean(ReportDTO.class)).list();
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
		}
		return lst;
	}

	public List<ReportDTO> getReportPartnerViolate(String branchCode, String violateCode, String startTime,
			String endTime) {
		List<ReportDTO> lst = new ArrayList<ReportDTO>();
		StringBuilder sb = new StringBuilder();
		SQLQuery sqlQuery = null;
		try {
			sb.append(
					"select c.code as partnerCode, c.name as partnerName, a.user_request as userRequest, a.ip as ipAddress, \r\n"
							+ "    to_char(a.start_time,'DD/MM/YYYY HH24:MI:SS') as startTime, b.id as serviceId, b.service_name as serviceName, \r\n"
							+ "    a.response_code as violateCode, a.message as violateDesc\r\n"
							+ "from log_services a\r\n" + "left join service_info b on a.service_code = b.id\r\n"
							+ "left join partner c on a.tctd_code = c.id\r\n"
							+ "where trunc(a.start_time) <= trunc(:endTime)\r\n"
							+ "    and trunc(a.start_time) >= trunc(:startTime)\r\n"
							+ "    and a.response_code in ( :lstResCode ) ");
			if (!Formater.isNull(branchCode)) {
				sb.append(" and c.code = :branchCode ");
			}
			sqlQuery = getCurrentSession().createSQLQuery(sb.toString());
			List<String> lstResCode = new ArrayList<>();
			if (!Formater.isNull(branchCode)) {
				sqlQuery.setParameter("branchCode", branchCode);
			}
			if (!Formater.isNull(violateCode)) {
				lstResCode.add(violateCode);
			} else {
				lstResCode = Arrays
						.asList(new String[] { "CMM_002", "CMM_003", "CMM_004", "CMM_006", "CMM_007", "CMM_008" });
			}
			sqlQuery.setParameterList("lstResCode", lstResCode);
			sqlQuery.setParameter("startTime", DateTimeUtil.string2Date(startTime, "dd/MM/yyyy"));
			sqlQuery.setParameter("endTime", DateTimeUtil.string2Date(endTime, "dd/MM/yyyy"));
			lst = sqlQuery.addScalar("serviceName", new StringType())
					.addScalar("serviceId", new StringType())
					.addScalar("partnerCode", new StringType())
					.addScalar("partnerName", new StringType())
					.addScalar("userRequest", new StringType())
					.addScalar("ipAddress", new StringType())
					.addScalar("startTime", new StringType())
					.addScalar("violateCode", new StringType())
					.addScalar("violateDesc", new StringType())
					.setResultTransformer(Transformers.aliasToBean(ReportDTO.class)).list();
		} catch (Exception e) {
			lg.error(e.getMessage(), e);
		}
		return lst;
	}
}
