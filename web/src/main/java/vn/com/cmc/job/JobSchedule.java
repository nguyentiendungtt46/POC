/**
 * 
 */
package vn.com.cmc.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

/**
 * @author nvtiep
 *
 *         created: Sep 3, 2020 1:47:44 PM
 */
@Service
public class JobSchedule {
	private static final Logger LOGGER = LoggerFactory.getLogger(JobSchedule.class);
	@Autowired
	ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Value("${pingip.fixrate.thread}")
	private Long fixRate = 6000L;
	@Value("${pingip.number.thread}")
	private int numOfThread = 1;

	public void startJob() {

		LOGGER.info("Start job");
//		for (int i = 0; i < numOfThread; i++) {
//			threadPoolTaskScheduler.scheduleAtFixedRate(pingIPRunnable, fixRate + i + numOfThread);
//		}
	}
}
