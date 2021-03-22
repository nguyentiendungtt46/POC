package config;

import java.math.BigDecimal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;

import cic.h2h.dao.hibernate.CatProductDao;
import cic.h2h.dao.hibernate.ServiceProductDao;
import entity.CatProduct;
import entity.ServiceInfo;
import vn.com.cmc.schedule.CicQna;

@Configuration
@EnableScheduling
public class CicThreadPoolReportConfig implements SchedulingConfigurer {
	private static final Logger LOGGER = LogManager.getLogger(CicThreadPoolReportConfig.class);

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		CicThreadPoolTaskScheduler taskScheduler = new CicThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(10);
		taskScheduler.initialize();
		taskRegistrar.setTaskScheduler(taskScheduler);
	}

	@Autowired
	private CatProductDao catProductDao;
	@Autowired
	ServiceProductDao serviceProductDao;
	@Autowired
	private CicQna cicQna;
	@Value("${QNA_SERVICE}")
	private String qnaServiceCode;

	@Bean
	public CicThreadPoolTaskScheduler cicThreadPoolTaskScheduler() {
		CicThreadPoolTaskScheduler threadPoolTaskScheduler = new CicThreadPoolTaskScheduler(cicQna);
		threadPoolTaskScheduler.setPoolSize(20);
		threadPoolTaskScheduler.setThreadNamePrefix("CIC_ThreadPool");
		threadPoolTaskScheduler.setErrorHandler(new ScheduledTaskErrorHandler());
		// Job hoi tin
		// Gui sang CIC
		List<CatProduct> lstProduct = catProductDao.getAllSvCode(qnaServiceCode);
		for (CatProduct product : lstProduct) {
			List<ServiceInfo> lstService = serviceProductDao.getByProduct(product);
			for (ServiceInfo s : lstService) {
				if (qnaServiceCode.equals(s.getCode())) {
					threadPoolTaskScheduler.setUpSchedule(CicThreadPoolTaskScheduler.QNA_SVR_CODE, product.getCode(),
							product.getFrequency() == null || product.getFrequency().intValue() <= 0 ? 30l
									: product.getFrequency().longValue(),
							product.getStatus());
				}

			}
		}

		// Lay ket qua hoi tin
		threadPoolTaskScheduler.setupQnaAns(60l);
		// Job bao cao
		// Job van tin
		return threadPoolTaskScheduler;
	}

	class ScheduledTaskErrorHandler implements ErrorHandler {
		@Override
		public void handleError(Throwable t) {
			LOGGER.error("CIC_ThreadPool: " + t.getMessage(), t);
		}
	}
}
