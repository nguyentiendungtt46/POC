package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import entity.Partner;

@Configuration
public class ApplicationConfig {
	@Value("${TCTD_CODE}")
	private String tctdCode;
	@Autowired
	private frwk.dao.hibernate.sys.SysPartnerDao SysPartnerDao;
	@Bean(name = "partner")
	public Partner partner() {
		return SysPartnerDao.getPartnerByCode(tctdCode);
	}
}
