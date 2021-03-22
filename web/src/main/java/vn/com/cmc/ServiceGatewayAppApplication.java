package vn.com.cmc;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;

import vn.com.cmc.job.JobSchedule;

public class ServiceGatewayAppApplication {
//	@Autowired
//	JobSchedule jobSchedule;
//
//	public static void main(String[] args) {
//		SpringApplication.run(ServiceGatewayAppApplication.class, args);
//	}
//
//	@EventListener(ApplicationReadyEvent.class)
//	public void doSomethingAfterStartup() throws Exception {
////		jobSchedule.startJob();
//	}
//	
////	 @Autowired
////	 ServiceInfoRepository serviceInfoRepository;
////	
////	 @Bean
////	 CommandLineRunner lookup() {
////		 return args -> {
////			 ServiceInfo serviceInfo = new ServiceInfo();
////			 serviceInfo.setServiceName("Báo cáo thống kê");
////			 serviceInfo.setDescription("Service Tổ chức tín dụng lấy báo cáo thống kê");
////			 serviceInfo.setStatus(new Long(1));
////			 serviceInfo.setPublishHost("http://localhost:8080/");
////			 serviceInfo.setPublishUri("ServiceGatewayApp/service/statisticalReport");
////			 serviceInfo.setDestinationHost("http://localhost:9999/");
////			 serviceInfo.setDestinationUri("CoreServiceApp/service/statisticalReport");
////			 serviceInfo.setRequestClass("vn.org.cic.h2h.ws.endpoint.cicstatisticalreport.BaoCaoThongKe");
////			 serviceInfo.setPublishOperation("BaoCaoThongKe");
////			 serviceInfoRepository.saveAndFlush(serviceInfo);
////		 };
////	 }

}
