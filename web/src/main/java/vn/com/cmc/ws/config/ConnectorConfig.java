package vn.com.cmc.ws.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class ConnectorConfig {

	@Value("${url.core.service}")
	private String urlCoreService;

	@Value("${url.cic.service}")
	private String urlCicService;

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(packageName(vn.org.cic.h2h.ws.endpoint.cicauthen.ObjectFactory.class),
				packageName(vn.org.cic.h2h.ws.endpoint.cicuserinfo.ObjectFactory.class),
				packageName(vn.org.cic.h2h.ws.endpoint.cicqa.ObjectFactory.class),
				packageName(vn.org.cic.h2h.ws.endpoint.cicqaprod.ObjectFactory.class),
				packageName(vn.org.cic.h2h.ws.endpoint.cicqafile.ObjectFactory.class),
				packageName(vn.org.cic.h2h.ws.endpoint.cicqafix.ObjectFactory.class),
				packageName(vn.org.cic.h2h.ws.endpoint.cicreport.ObjectFactory.class),
				packageName(vn.org.intergration.ws.endpoint.cicqr.ObjectFactory.class),
				packageName(vn.org.intergration.ws.endpoint.cicstatisticalreport.ObjectFactory.class));
		return marshaller;
	}

	private static String packageName(final Class<?> jaxbClass) {
		return jaxbClass.getPackage().getName();
	}

	@Bean
	public SOAPConnector soapConnector(Jaxb2Marshaller marshaller) {
		SOAPConnector client = new SOAPConnector();
		client.setDefaultUri(urlCoreService);
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}