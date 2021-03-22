/**
 * 
 */
package vn.com.cmc.schedule;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * @author nvtiep
 *
 *         created: Sep 4, 2020 9:53:43 AM Đặt lịch xử lý dữ liệu đầu ra
 */
@Component
public class CicJob03Runnable implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(CicJob03Runnable.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		LOGGER.info("CicJob51Runnable");
	}

}
