package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;
public class ThreadPoolReportConfig implements SchedulingConfigurer {
	private static final Logger LOGGER = LogManager.getLogger(ThreadPoolReportConfig.class);

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
		taskScheduler.setPoolSize(10);
		taskScheduler.initialize();
		taskRegistrar.setTaskScheduler(taskScheduler);
	}

	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(10);
		threadPoolTaskScheduler.setThreadNamePrefix("CIC_ThreadPool");
		threadPoolTaskScheduler.setErrorHandler(new ScheduledTaskErrorHandler());
		return threadPoolTaskScheduler;
	}

	class ScheduledTaskErrorHandler implements ErrorHandler {
		@Override
		public void handleError(Throwable t) {
			LOGGER.error("CIC_ThreadPool: " + t.getMessage(), t);
		}
	}
}
