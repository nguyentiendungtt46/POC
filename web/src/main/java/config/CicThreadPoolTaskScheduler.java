package config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import cic.h2h.dao.hibernate.ServiceProductDao;
import common.util.Formater;
import entity.CatProduct;
import entity.ServiceInfo;
import vn.com.cmc.schedule.CicQna;

public class CicThreadPoolTaskScheduler extends ThreadPoolTaskScheduler {
	public static final String QNA_SVR_CODE = "QNA_SVR_CODE";
	public static final String QNA_ANSW_SVR_CODE = "QNA_ANSW_SVR_CODE";
	public static final String RPR_SVR_CODE = "RPR_SVR_CODE";
	public static final String QRY_SVR_CODE = "QRY_SVR_CODE";
	// private Map<String, Map<String,Runnable>> svMap = new HashMap<String,
	// Map<String,Runnable>>();
	private Map<String, Map<String, ScheduledFuture<?>>> svProductMap = new HashMap<String, Map<String, ScheduledFuture<?>>>();
	private Map<String, ScheduledFuture<?>> svMap = new HashMap<String, ScheduledFuture<?>>();

	public CicThreadPoolTaskScheduler() {
		super();
		this.initialize();
	}

	public CicThreadPoolTaskScheduler(CicQna cicQna) {
		this();
		this.cicQna = cicQna;
	}

	public void setUpSchedule(String svrCode, String maSp, long delay, Boolean disable) {
		if (!svProductMap.containsKey(svrCode))
			svProductMap.put(svrCode, new HashMap<String, ScheduledFuture<?>>());
		HashMap<String, ScheduledFuture<?>> prMap = (HashMap<String, ScheduledFuture<?>>) svProductMap.get(svrCode);
		ScheduledFuture<?> sf01 = null;
		if (!prMap.containsKey(maSp)) {
			//sf01 = this.scheduleWithFixedDelay(cicQna.makeExeThead(maSp), delay * 1000);
			prMap.put(maSp, sf01);
		} else {
			sf01 = prMap.get(maSp);
			sf01.cancel(false);
			//sf01 = this.scheduleWithFixedDelay(cicQna.makeExeThead(maSp), delay * 1000);
		}
//		if (disable != null && disable && !sf01.isCancelled())
//			sf01.cancel(false);

	}

	public void detroySchedule(String svrCode, String maSp) {
		HashMap<String, ScheduledFuture<?>> prMap = (HashMap<String, ScheduledFuture<?>>) svProductMap.get(svrCode);
		ScheduledFuture<?> sf01 = prMap.get(maSp);
		sf01.cancel(false);
		prMap.remove(sf01);

	}

	private void detroySchedule(String svrCode) {
		ScheduledFuture<?> sf01 = svMap.get(svrCode);
		sf01.cancel(false);
		svMap.remove(sf01);

	}

	@Value("${QNA_SERVICE}")
	private String qnaServiceCode;
	

	public void setUpqry(String maSp, long delay) {
		// setUpSchedule(QRY_SVR_CODE, maSp, delay);
	}

	public void setupRp(String maSp, long delay) {
		// setUpSchedule(RPR_SVR_CODE, maSp, delay);
	}

	@Autowired
	private CicQna cicQna;

	public void setupQnaAns(long delay) {
		if (!svMap.containsKey(QNA_ANSW_SVR_CODE)) {
			//ScheduledFuture<?> sf01 = this.scheduleWithFixedDelay(cicQna.makeQnaAnswExeThead(), delay * 1000);
			//svMap.put(QNA_ANSW_SVR_CODE, sf01);
		} else {
			ScheduledFuture<?> sf01 = svMap.get(QNA_ANSW_SVR_CODE);
			sf01.cancel(false);
			//sf01 = this.scheduleWithFixedDelay(cicQna.makeQnaAnswExeThead(), delay * 1000);
		}
	}

	

}
