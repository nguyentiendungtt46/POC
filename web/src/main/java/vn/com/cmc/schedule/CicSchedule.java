/**
 * 
 */
package vn.com.cmc.schedule;

import java.util.concurrent.ScheduledFuture;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import frwk.dao.hibernate.sys.SysParamDao;


/**
 * @author nvtiep
 *
 *         created: Sep 4, 2020 9:31:28 AM Tat ca cac job xu ly them
 */
@Service
public class CicSchedule {
	private static final Logger LOGGER = LogManager.getLogger(CicSchedule.class);
	@Autowired
	ThreadPoolTaskScheduler threadPoolTaskSchedulerJob;

	@Autowired
	SysParamDao sysParamDao;

	// job create alert email: đặt lịch tạo email cảnh báo vi phạm
//	ScheduledFuture createAlertEmail;
//	CronTrigger triggerCreateAlertEmail;
//	@Autowired
//	CicJobCreateAlertEmailRunnable cicJobCreateAlertEmailRunnable;

	// job 01: dat lich lay du lieu san pham S11A
	ScheduledFuture sf01;
	CronTrigger trigger01;
	@Autowired
	CicSendQnaR14Job cicJob01Runnable;

	// job 02: dat lich lay du lieu san pham R14
	ScheduledFuture sf02;
	CronTrigger trigger02;
	@Autowired
	CicJob02Runnable cicJob02Runnable;
	
	// job 03: dat lich gui lai du lieu loi sang cic
	ScheduledFuture sf03;
	CronTrigger trigger03;
	@Autowired
	CicJob03Runnable cicJob03Runnable;

	/**
	 * 
	 * @param delay tan suat
	 */
	public void start01(long delay) {
		sf01 = threadPoolTaskSchedulerJob.scheduleWithFixedDelay(cicJob01Runnable, delay);
		LOGGER.info("Start job 01: fix delay " + delay + " ms");
	}

	public void stop01() {
		if (sf01 != null) {
			sf01.cancel(false);
		}
		LOGGER.info("Stop job 01");
	}
	/**
	 * 
	 * @param delay tan suat
	 */
	public void changeTrigger01(long delay) {
		LOGGER.info("Change job 01 to fix delay: " + delay);
		if (sf01 != null) {
			sf01.cancel(false);
		}
		start01(delay);
	}
	/**
	 * 
	 * @param delay tan suat
	 */
	public void start02(long delay) {
		sf02 = threadPoolTaskSchedulerJob.scheduleWithFixedDelay(cicJob02Runnable, delay);
		LOGGER.info("Start job 02: fix delay " + delay + " ms");
	}

	public void stop02() {
		if (sf02 != null) {
			sf02.cancel(false);
		}
		LOGGER.info("Stop job 01");
	}
	/**
	 * 
	 * @param delay tan suat
	 */
	public void changeTrigger02(long delay) {
		LOGGER.info("Change job 02 to fix delay: " + delay);
		if (sf02 != null) {
			sf02.cancel(false);
		}
		start02(delay);
	}
	
	/**
	 * 
	 * @param delay tan suat
	 */
	public void start03(long delay) {
		sf03 = threadPoolTaskSchedulerJob.scheduleWithFixedDelay(cicJob01Runnable, delay);
		LOGGER.info("Start job 03: fix delay " + delay + " ms");
	}

	public void stop03() {
		if (sf03 != null) {
			sf03.cancel(false);
		}
		LOGGER.info("Stop job 03");
	}
	/**
	 * 
	 * @param delay tan suat
	 */
	public void changeTrigger03(long delay) {
		LOGGER.info("Change job 03 to fix delay: " + delay);
		if (sf03 != null) {
			sf03.cancel(false);
		}
		start03(delay);
	}

}
