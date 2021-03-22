package cic.ws.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.WebServiceMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

@Configuration
public class WsClientConfiguration {
//	@Bean
//	public Jaxb2Marshaller marshaller() {
//		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
//		// this package must match the package in the <generatePackage> specified in
//		// pom.xml
//		marshaller.setContextPaths("vn.org.cic.h2h.ws.endpoint.cicuserinfo","cic.ws.model", "vn.org.cic.h2h.ws.endpoint.cicauthen", "vn.org.cic.h2h.ws.endpoint.cicqa", "vn.org.cic.h2h.ws.endpoint.cicqaprod", "cic.ws.login");
//		return marshaller;
//	}
	
	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(packageName(vn.org.cic.h2h.ws.endpoint.cicauthen.ObjectFactory.class),
				packageName(vn.org.cic.h2h.ws.endpoint.cicuserinfo.ObjectFactory.class),
				packageName(vn.org.cic.h2h.ws.endpoint.cicqa.ObjectFactory.class),
				packageName(vn.org.cic.h2h.ws.endpoint.cicqaprod.ObjectFactory.class),
				packageName(vn.org.cic.h2h.ws.endpoint.cicqafile.ObjectFactory.class),
				packageName(vn.org.cic.h2h.ws.endpoint.cicqafix.ObjectFactory.class),
				packageName(vn.org.cic.h2h.ws.endpoint.cicstatisticalreport.ObjectFactory.class),
				packageName(vn.org.cic.h2h.ws.endpoint.cicreport.ObjectFactory.class),
				packageName(cic.ws.model.ObjectFactory.class),
				packageName(cic.ws.login.ObjectFactory.class),
				packageName(vn.org.cic.h2h.ws.endpoint.cicqr.ObjectFactory.class));
		return marshaller;
	}
	
	private static String packageName(final Class<?> jaxbClass) {
		return jaxbClass.getPackage().getName();
	}

	@Bean
	public WsClient wsClient(Jaxb2Marshaller marshaller) {
		WsClient client = new WsClient();
		client.setDefaultUri("http://localhost:8081/CoreServiceApp/service/report");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		client.setMessageSender(httpComponentsMessageSender());
		return client;
	}

	@Bean
	public HttpComponentsMessageSender httpComponentsMessageSender() {
		HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
		// timeout for creating a connection
		httpComponentsMessageSender.setConnectionTimeout(300 * 1000);
		// when you have a connection, timeout the read blocks for
		httpComponentsMessageSender.setReadTimeout(300 * 1000);
		return httpComponentsMessageSender;
	}
	
//	@Bean
//    public WebServiceTemplate webServiceTemplate() {
//        WebServiceTemplate wsTemplate = new WebServiceTemplate();
//        wsTemplate.setMarshaller(marshaller());
//        wsTemplate.setUnmarshaller(marshaller());
//        wsTemplate.setDefaultUri("http://localhost:8081/CoreServiceApp/service/report");
//        wsTemplate.setMessageSender(httpComponentsMessageSender());
//        
//        return wsTemplate;
//    }

//	@Bean
//	public WebServiceMessageSender webServiceMessageSender() {
//		HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
//		// timeout for creating a connection
//		httpComponentsMessageSender.setConnectionTimeout(60000);
//		// when you have a connection, timeout the read blocks for
//		httpComponentsMessageSender.setReadTimeout(60000);
//		return httpComponentsMessageSender;
//	}
}
