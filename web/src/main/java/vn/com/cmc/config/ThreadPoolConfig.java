/**
 * 
 */
package vn.com.cmc.config;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.util.ErrorHandler;

/**
 * @author nvtiep
 *
 *         created: Jan 13, 2020 6:05:29 PM
 */
@Configuration
@EnableScheduling
public class ThreadPoolConfig implements SchedulingConfigurer{
	private static final Logger LOGGER = LogManager.getLogger(ThreadPoolConfig.class);
	private AtomicInteger counter = new AtomicInteger(0);

	@Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(3);
        taskScheduler.initialize();
        taskRegistrar.setTaskScheduler(taskScheduler);
    }
	
	@Bean
	public ThreadPoolTaskScheduler threadPoolTaskScheduler() {

		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
//		threadPoolTaskScheduler.setPoolSize(2);
		threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
		threadPoolTaskScheduler.setErrorHandler(new ScheduledTaskErrorHandler());
		return threadPoolTaskScheduler;
	}
	
//	@Scheduled(fixedRate = 3000)
//	public void fixedRateJob() {
//		int jobId = counter.incrementAndGet();
//		System.out.println("Job @ fixed rate " + new Date() + ", jobId: " + jobId);
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		System.out.println("Job " + jobId + " done");
//	}

	class ScheduledTaskErrorHandler implements ErrorHandler {

		@Override
		public void handleError(Throwable t) {
			LOGGER.error(t.getMessage(), t);
		}
	}
}
