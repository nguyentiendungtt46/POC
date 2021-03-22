package vn.com.cmc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
@Order(1)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String RESOURCE_ID = "resource_id";

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) {
		resources.resourceId(RESOURCE_ID).stateless(false);
	}
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/service/**").authorizeRequests().antMatchers("/service/authen").permitAll()
		.antMatchers("/service/cicAuthenWsdl.wsdl").permitAll().antMatchers("service/questionAnswer").permitAll()
		.antMatchers("/service/cicUserInfoWsdl.wsdl").permitAll()
		.antMatchers("/service/cicQuestionAnswerWsdl.wsdl").permitAll()
		.antMatchers("/service/cicInterViewProductsWsdl.wsdl").permitAll()
		.antMatchers("/service/cicSearchWsdl.wsdl").permitAll().antMatchers("/service/search").permitAll()
		.antMatchers("/service/cicFxFileWsdl.wsdl").permitAll()
		.antMatchers("/service/cicStatusQuestionFileWsdl.wsdl").permitAll()	
		.antMatchers("/service/cicReportWsdl.wsdl").permitAll()	
		.antMatchers("/service/cicSheetQuestionInfoWsdl.wsdl").permitAll()
		.antMatchers("/service/cicStatisticalReportWsdl.wsdl").permitAll()
		.anyRequest().authenticated();
	}

}