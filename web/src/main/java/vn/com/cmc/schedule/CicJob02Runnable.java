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
 *         created: Sep 4, 2020 9:53:43 AM Quản trị job tạo lập dữ liệu theo
 *         từng TCTD lần đầu
 * 
 */
@Component
public class CicJob02Runnable implements Runnable {
	private static final Logger LOGGER = LogManager.getLogger(CicJob02Runnable.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		LOGGER.info("52 Current Thread ID- " + Thread.currentThread().getId() + " For Thread- "
				+ Thread.currentThread().getName());
	}

}
