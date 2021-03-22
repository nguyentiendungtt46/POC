package dto;

import java.io.Serializable;

public class ProcessRp implements Serializable {

	private static final long serialVersionUID = 1L;

	private String maTCTD;
	private String tenTCTD;
	private String totalReport;
	private String totalData;
	private String totalAutoReport;
	private String totalmanualReport;
	private String totalInProcess;
	private String totalNotProcess;
	private String totalReturnReport;
	private String totalPassToM1;
	private String waitProcess;

	public ProcessRp() {
		super();
	}

	public String getMaTCTD() {
		return maTCTD;
	}

	public void setMaTCTD(String maTCTD) {
		this.maTCTD = maTCTD;
	}

	public String getTenTCTD() {
		return tenTCTD;
	}

	public void setTenTCTD(String tenTCTD) {
		this.tenTCTD = tenTCTD;
	}

	public String getTotalReport() {
		return totalReport;
	}

	public void setTotalReport(String totalReport) {
		this.totalReport = totalReport;
	}

	public String getTotalData() {
		return totalData;
	}

	public void setTotalData(String totalData) {
		this.totalData = totalData;
	}

	public String getTotalAutoReport() {
		return totalAutoReport;
	}

	public void setTotalAutoReport(String totalAutoReport) {
		this.totalAutoReport = totalAutoReport;
	}

	public String getTotalmanualReport() {
		return totalmanualReport;
	}

	public void setTotalmanualReport(String totalmanualReport) {
		this.totalmanualReport = totalmanualReport;
	}

	public String getTotalInProcess() {
		return totalInProcess;
	}

	public void setTotalInProcess(String totalInProcess) {
		this.totalInProcess = totalInProcess;
	}

	public String getTotalNotProcess() {
		return totalNotProcess;
	}

	public void setTotalNotProcess(String totalNotProcess) {
		this.totalNotProcess = totalNotProcess;
	}

	public String getTotalReturnReport() {
		return totalReturnReport;
	}

	public void setTotalReturnReport(String totalReturnReport) {
		this.totalReturnReport = totalReturnReport;
	}

	public String getTotalPassToM1() {
		return totalPassToM1;
	}

	public void setTotalPassToM1(String totalPassToM1) {
		this.totalPassToM1 = totalPassToM1;
	}

	public String getWaitProcess() {
		return waitProcess;
	}

	public void setWaitProcess(String waitProcess) {
		this.waitProcess = waitProcess;
	}

}
