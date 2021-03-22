package vn.com.cmc.ws.endpoint;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import vn.com.cmc.service.ValidateService;
import vn.org.intergration.ws.endpoint.cicqr.PHTimKiemKH;
import vn.org.intergration.ws.endpoint.cicqr.TimKiemKH;

@Endpoint
public class CicSearchEndpoint {
	private static final Logger logger = LogManager.getLogger(CicSearchEndpoint.class);

	@Autowired
	ValidateService validateService;

	private static final String NAMESPACE_URI = "http://www.endpoint.ws.intergration.org.vn/cicqr";

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "TimKiemKH")
	@ResponsePayload
	public PHTimKiemKH searchCustomer(@RequestPayload TimKiemKH request, MessageContext messageContext)
			throws JAXBException {
		logger.info("START: searchCustomer ");
		return validateService.sendSearchCus(request, null);
	}

}
