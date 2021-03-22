package dto;

import java.io.Serializable;

public class ProcessQA implements Serializable {

	private static final long serialVersionUID = 1L;

	private String maTCTD;
	private String tenTCTD;
	private String maSP;
	private String tenSP;
	private String soLuongKH;
	private String soLuongYeuCauHoi;
	private String soLuongYeuChuaXuLy;
	private String soLuongYeuCauDaXuLy;
	private String soLuongYeuCauTCTDNhan;
	private String soLuongYeuCauTCTDChuaNhan;

	public ProcessQA() {
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

	public String getMaSP() {
		return maSP;
	}

	public void setMaSP(String maSP) {
		this.maSP = maSP;
	}

	public String getTenSP() {
		return tenSP;
	}

	public void setTenSP(String tenSP) {
		this.tenSP = tenSP;
	}

	public String getSoLuongYeuCauHoi() {
		return soLuongYeuCauHoi;
	}

	public void setSoLuongYeuCauHoi(String soLuongYeuCauHoi) {
		this.soLuongYeuCauHoi = soLuongYeuCauHoi;
	}

	public String getSoLuongYeuChuaXuLy() {
		return soLuongYeuChuaXuLy;
	}

	public void setSoLuongYeuChuaXuLy(String soLuongYeuChuaXuLy) {
		this.soLuongYeuChuaXuLy = soLuongYeuChuaXuLy;
	}

	public String getSoLuongYeuCauDaXuLy() {
		return soLuongYeuCauDaXuLy;
	}

	public void setSoLuongYeuCauDaXuLy(String soLuongYeuCauDaXuLy) {
		this.soLuongYeuCauDaXuLy = soLuongYeuCauDaXuLy;
	}

	public String getSoLuongYeuCauTCTDNhan() {
		return soLuongYeuCauTCTDNhan;
	}

	public void setSoLuongYeuCauTCTDNhan(String soLuongYeuCauTCTDNhan) {
		this.soLuongYeuCauTCTDNhan = soLuongYeuCauTCTDNhan;
	}

	public String getSoLuongYeuCauTCTDChuaNhan() {
		return soLuongYeuCauTCTDChuaNhan;
	}

	public void setSoLuongYeuCauTCTDChuaNhan(String soLuongYeuCauTCTDChuaNhan) {
		this.soLuongYeuCauTCTDChuaNhan = soLuongYeuCauTCTDChuaNhan;
	}

	public String getSoLuongKH() {
		return soLuongKH;
	}

	public void setSoLuongKH(String soLuongKH) {
		this.soLuongKH = soLuongKH;
	}

}
