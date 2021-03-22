package vn.com.cmc.ws.config;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.SoapFaultClientException;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;

public class SOAPConnector extends WebServiceGatewaySupport {

	public Object callWebService(String url, Object requestPayload) {
		return getWebServiceTemplate().marshalSendAndReceive(url, requestPayload);
	}

	public Object callWebServiceAuthen(String url, Object requestPayload, String token) {
		WebServiceTemplate webServiceTemplate = getWebServiceTemplate();
		Object res = null;
		try {
			res = webServiceTemplate.marshalSendAndReceive(url, requestPayload, new WebServiceMessageCallback() {

				@Override
				public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {
					try {
						TransportContext context = TransportContextHolder.getTransportContext();
						HttpUrlConnection connection = (HttpUrlConnection) context.getConnection();
						String authenValue = "bearer " + token;
						connection.addRequestHeader("Authorization", authenValue);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			});
		} catch (SoapFaultClientException ex) {
			logger.error(ex.getMessage());
		}
		return res;
	}
}