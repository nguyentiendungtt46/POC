package cic.h2h.dao.hibernate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import common.util.Formater;
import entity.FunEndpoint;
import entity.QrEndpoint;

@Repository(value = "qrEndpointDao")
public class QrEndpointDao extends H2HBaseDao<QrEndpoint>{
	
	public void save(QrEndpoint endpoint) throws Exception {
		if (Formater.isNull(endpoint.getId())) {
			if (endpoint.getFunEndpoints() != null) {
				for (FunEndpoint item : endpoint.getFunEndpoints()) {
					if (item == null)
						continue;
					item.setId(null);
					item.setQrEndpoint(endpoint);
				}
			}
		} else {
			QrEndpoint oldEndpoint = getObject(QrEndpoint.class, endpoint.getId());
			
			
			List<FunEndpoint> delObj = new ArrayList<FunEndpoint>();
			Map<String, FunEndpoint> map  = new HashMap<String, FunEndpoint>();
			for (FunEndpoint item : endpoint.getFunEndpoints()) {
				item.setQrEndpoint(endpoint);
				if (Formater.isNull(item.getId()))
					continue;
				map.put(item.getId(), item);
			}
			if (oldEndpoint.getFunEndpoints() != null) {
				for (FunEndpoint item : oldEndpoint.getFunEndpoints()) {
					if (map.get(item.getId()) == null) {
						delObj.add(item);
					}
				}
			}
			for (FunEndpoint funEndpoint : delObj) {
				getCurrentSession().delete(funEndpoint);
			}
		}
		super.save(endpoint);
	}
}
