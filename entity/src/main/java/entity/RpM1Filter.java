package entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RP_M1_FILTER")
public class RpM1Filter implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid")
	@Column(name = "ID")
	private String id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "RP_CODE")
	private RpType rpCode;

	public RpType getRpCode() {
		return rpCode;
	}

	public void setRpCode(RpType rpCode) {
		this.rpCode = rpCode;
	}

	@Column(name = "DISABLE_FILTER")
	private Short disableFilter;

	@Column(name = "ERR_THRESHLD")
	private BigDecimal errThreshld;

	@Column(name = "BAL_THRESHLD")
	private BigDecimal balThreshld;

	@Column(name = "IVST_THRESHLD")
	private BigDecimal ivstThreshld;

	@Column(name = "AST_THRESHLD")
	private BigDecimal astThreshld;

	@Column(name = "CUS_AMT_THRESHLD")
	private BigDecimal cusAmtThreshld;

	@Column(name = "K3VX3_WTH_OTHER_THRESHLD")
	private BigDecimal k3vx3WthOtherThreshld;

	@Column(name = "PMT_AMT_THRESHLD")
	private BigDecimal pmtAmtThreshld;

	@Column(name = "BOND_AMT_THRESHLD")
	private BigDecimal bondAmtThreshld;

	@Column(name = "K31X3_K32_BAL_IN_VND_THRESHLD")
	private BigDecimal k31x3VsK32BalInVndThreshld;

	@Column(name = "K31X3_K32_BAL_IN_FOR_THRESHLD")
	private BigDecimal k31x3VsK32BalInForThreshld;

	@Column(name = "K31X3_VS_K32_DIF_THRESHLD")
	private BigDecimal k31x3VsK32DifThreshld;
	@Column(name = "K31_K32_DIF_LN_TYPE_THRESHLD")
	private BigDecimal k31x3VsK32DifLnTypeThreshld;

	@Column(name = "T02GX_INTERNAL")
	private BigDecimal t02gxInternal;

	@Column(name = "T02GX_EXTERNAL")
	private BigDecimal t02gxExternal;

	@Column(name = "T02GX_CMT")
	private BigDecimal t02gxCmt;

	@Column(name = "DISABLE_CHECK_K31X3_K32")
	private Short disableCheckK31x3K32;
	@Column(name = "TLS_PARTNER_VND")
	private BigDecimal tlsPartnerVnd;
	@Column(name = "TLS_PARTNER_G")
	private BigDecimal tlsPartnerG;
	@Column(name = "TLS_PARTNER_CURRENCY")
	private BigDecimal tlsPartnerCurrency;
	@Column(name = "TLS_PP_VND")
	private BigDecimal tlsPpVnd;
	@Column(name = "TLS_PP_G")
	private BigDecimal tlsPpG;
	@Column(name = "TLS_PP_CURRENCY")
	private BigDecimal tlsPpCurrency;
	@Column(name = "TLS_K31X3_WTH_K32_G")
	private BigDecimal tlsK31x3WthK32G;
	@Column(name = "LOAN_CUS_K31X3_WTH_K32_G")
	private BigDecimal loanCusK31x3WthK32G;
	@Column(name = "LOAN_CUS_K31X3_K32_CURRENCY")
	private BigDecimal loanCusK31x3WthK32Currency;
	@Column(name = "TLS_INT_PARTNER_G")
	private BigDecimal tlsIntPartnerG;
	@Column(name = "TLS_EXT_PARTNER_G")
	private BigDecimal tlsExtPartnerG;
	@Column(name = "TLS_COM_EXT_PARTNER_G")
	private BigDecimal tlsComExtPartnerG;
	@Column(name = "TLS_INT_PARTNER_CURRENCY")
	private BigDecimal tlsIntPartnerCurrency;
	@Column(name = "TLS_EXT_PARTNER_CURRENCY")
	private BigDecimal tlsExtPartnerCurrency;
	@Column(name = "TLS_COM_EXT_PARTNER_CURRENCY")
	private BigDecimal tlsComExtPartnerCurrency;
	@Column(name = "TLS_INT_PP_VND")
	private BigDecimal tlsIntPpVnd;
	@Column(name = "TLS_EXT_PP_VND")
	private BigDecimal tlsExtPpVnd;
	@Column(name = "TLS_COM_EXT_PP_VND")
	private BigDecimal tlsComExtPpVnd;
	@Column(name = "TLS_INT_PP_G")
	private BigDecimal tlsIntPpG;
	@Column(name = "TLS_EXT_PP_G")
	private BigDecimal tlsExtPpG;
	@Column(name = "TLS_COM_EXT_PP_G")
	private BigDecimal tlsComExtPpG;
	@Column(name = "TLS_INT_PP_CURRENCY")
	private BigDecimal tlsIntPpCurrency;
	@Column(name = "TLS_EXT_PP_CURRENCY")
	private BigDecimal tlsExtPpCurrency;
	@Column(name = "TLS_COM_EXT_PP_CURRENCY")
	private BigDecimal tlsComExtPpCurrency;
	@Column(name = "TAM_PAY_PP")
	private BigDecimal tamPayPp;
	@Column(name = "TCU_PP")
	private BigDecimal tcuPp;
	@Column(name = "TIN_PP")
	private BigDecimal tinPp;
	@Column(name = "TBO_PP")
	private BigDecimal tboPp;
	@Column(name = "IVST_THRESHLD_VND")
	private BigDecimal ivstThreshldVnd;
	@Column(name = "IVST_THRESHLD_G")
	private BigDecimal ivstThreshldG;
	@Column(name = "IVST_THRESHLD_CURRENCY")
	private BigDecimal ivstThreshldCurrency;
	@Column(name = "TIN_PP_VND")
	private BigDecimal tinPpVnd;
	@Column(name = "TIN_PP_G")
	private BigDecimal tinPpG;
	@Column(name = "TIN_PP_CURRENCY")
	private BigDecimal tinPpCurrency;

	public RpM1Filter() {
		super();
	}

	public RpM1Filter(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Short getDisableFilter() {
		return disableFilter;
	}

	public void setDisableFilter(Short disableFilter) {
		this.disableFilter = disableFilter;
	}

	public BigDecimal getErrThreshld() {
		return errThreshld;
	}

	public void setErrThreshld(BigDecimal errThreshld) {
		this.errThreshld = errThreshld;
	}

	public BigDecimal getBalThreshld() {
		return balThreshld;
	}

	public void setBalThreshld(BigDecimal balThreshld) {
		this.balThreshld = balThreshld;
	}

	public BigDecimal getIvstThreshld() {
		return ivstThreshld;
	}

	public void setIvstThreshld(BigDecimal ivstThreshld) {
		this.ivstThreshld = ivstThreshld;
	}

	public BigDecimal getAstThreshld() {
		return astThreshld;
	}

	public void setAstThreshld(BigDecimal astThreshld) {
		this.astThreshld = astThreshld;
	}

	public BigDecimal getCusAmtThreshld() {
		return cusAmtThreshld;
	}

	public void setCusAmtThreshld(BigDecimal cusAmtThreshld) {
		this.cusAmtThreshld = cusAmtThreshld;
	}

	public BigDecimal getK3vx3WthOtherThreshld() {
		return k3vx3WthOtherThreshld;
	}

	public void setK3vx3WthOtherThreshld(BigDecimal k3vx3WthOtherThreshld) {
		this.k3vx3WthOtherThreshld = k3vx3WthOtherThreshld;
	}

	public BigDecimal getPmtAmtThreshld() {
		return pmtAmtThreshld;
	}

	public void setPmtAmtThreshld(BigDecimal pmtAmtThreshld) {
		this.pmtAmtThreshld = pmtAmtThreshld;
	}

	public BigDecimal getBondAmtThreshld() {
		return bondAmtThreshld;
	}

	public void setBondAmtThreshld(BigDecimal bondAmtThreshld) {
		this.bondAmtThreshld = bondAmtThreshld;
	}

	public BigDecimal getK31x3VsK32BalInVndThreshld() {
		return k31x3VsK32BalInVndThreshld;
	}

	public void setK31x3VsK32BalInVndThreshld(BigDecimal k31x3VsK32BalInVndThreshld) {
		this.k31x3VsK32BalInVndThreshld = k31x3VsK32BalInVndThreshld;
	}

	public BigDecimal getK31x3VsK32BalInForThreshld() {
		return k31x3VsK32BalInForThreshld;
	}

	public void setK31x3VsK32BalInForThreshld(BigDecimal k31x3VsK32BalInForThreshld) {
		this.k31x3VsK32BalInForThreshld = k31x3VsK32BalInForThreshld;
	}

	public BigDecimal getK31x3VsK32DifThreshld() {
		return k31x3VsK32DifThreshld;
	}

	public void setK31x3VsK32DifThreshld(BigDecimal k31x3VsK32DifThreshld) {
		this.k31x3VsK32DifThreshld = k31x3VsK32DifThreshld;
	}

	public BigDecimal getK31x3VsK32DifLnTypeThreshld() {
		return k31x3VsK32DifLnTypeThreshld;
	}

	public void setK31x3VsK32DifLnTypeThreshld(BigDecimal k31x3VsK32DifLnTypeThreshld) {
		this.k31x3VsK32DifLnTypeThreshld = k31x3VsK32DifLnTypeThreshld;
	}

	public BigDecimal getT02gxInternal() {
		return t02gxInternal;
	}

	public void setT02gxInternal(BigDecimal t02gxInternal) {
		this.t02gxInternal = t02gxInternal;
	}

	public BigDecimal getT02gxExternal() {
		return t02gxExternal;
	}

	public void setT02gxExternal(BigDecimal t02gxExternal) {
		this.t02gxExternal = t02gxExternal;
	}

	public BigDecimal getT02gxCmt() {
		return t02gxCmt;
	}

	public void setT02gxCmt(BigDecimal t02gxCmt) {
		this.t02gxCmt = t02gxCmt;
	}

	public Short getDisableCheckK31x3K32() {
		return disableCheckK31x3K32;
	}

	public void setDisableCheckK31x3K32(Short disableCheckK31x3K32) {
		this.disableCheckK31x3K32 = disableCheckK31x3K32;
	}

	public BigDecimal getTlsPartnerVnd() {
		return tlsPartnerVnd;
	}

	public void setTlsPartnerVnd(BigDecimal tlsPartnerVnd) {
		this.tlsPartnerVnd = tlsPartnerVnd;
	}

	public BigDecimal getTlsPartnerG() {
		return tlsPartnerG;
	}

	public void setTlsPartnerG(BigDecimal tlsPartnerG) {
		this.tlsPartnerG = tlsPartnerG;
	}

	public BigDecimal getTlsPartnerCurrency() {
		return tlsPartnerCurrency;
	}

	public void setTlsPartnerCurrency(BigDecimal tlsPartnerCurrency) {
		this.tlsPartnerCurrency = tlsPartnerCurrency;
	}

	public BigDecimal getTlsPpVnd() {
		return tlsPpVnd;
	}

	public void setTlsPpVnd(BigDecimal tlsPpVnd) {
		this.tlsPpVnd = tlsPpVnd;
	}

	public BigDecimal getTlsPpG() {
		return tlsPpG;
	}

	public void setTlsPpG(BigDecimal tlsPpG) {
		this.tlsPpG = tlsPpG;
	}

	public BigDecimal getTlsPpCurrency() {
		return tlsPpCurrency;
	}

	public void setTlsPpCurrency(BigDecimal tlsPpCurrency) {
		this.tlsPpCurrency = tlsPpCurrency;
	}

	public BigDecimal getTlsK31x3WthK32G() {
		return tlsK31x3WthK32G;
	}

	public void setTlsK31x3WthK32G(BigDecimal tlsK31x3WthK32G) {
		this.tlsK31x3WthK32G = tlsK31x3WthK32G;
	}

	public BigDecimal getLoanCusK31x3WthK32G() {
		return loanCusK31x3WthK32G;
	}

	public void setLoanCusK31x3WthK32G(BigDecimal loanCusK31x3WthK32G) {
		this.loanCusK31x3WthK32G = loanCusK31x3WthK32G;
	}

	public BigDecimal getLoanCusK31x3WthK32Currency() {
		return loanCusK31x3WthK32Currency;
	}

	public void setLoanCusK31x3WthK32Currency(BigDecimal loanCusK31x3WthK32Currency) {
		this.loanCusK31x3WthK32Currency = loanCusK31x3WthK32Currency;
	}

	public BigDecimal getTlsIntPartnerG() {
		return tlsIntPartnerG;
	}

	public void setTlsIntPartnerG(BigDecimal tlsIntPartnerG) {
		this.tlsIntPartnerG = tlsIntPartnerG;
	}

	public BigDecimal getTlsExtPartnerG() {
		return tlsExtPartnerG;
	}

	public void setTlsExtPartnerG(BigDecimal tlsExtPartnerG) {
		this.tlsExtPartnerG = tlsExtPartnerG;
	}

	public BigDecimal getTlsComExtPartnerG() {
		return tlsComExtPartnerG;
	}

	public void setTlsComExtPartnerG(BigDecimal tlsComExtPartnerG) {
		this.tlsComExtPartnerG = tlsComExtPartnerG;
	}

	public BigDecimal getTlsIntPartnerCurrency() {
		return tlsIntPartnerCurrency;
	}

	public void setTlsIntPartnerCurrency(BigDecimal tlsIntPartnerCurrency) {
		this.tlsIntPartnerCurrency = tlsIntPartnerCurrency;
	}

	public BigDecimal getTlsExtPartnerCurrency() {
		return tlsExtPartnerCurrency;
	}

	public void setTlsExtPartnerCurrency(BigDecimal tlsExtPartnerCurrency) {
		this.tlsExtPartnerCurrency = tlsExtPartnerCurrency;
	}

	public BigDecimal getTlsComExtPartnerCurrency() {
		return tlsComExtPartnerCurrency;
	}

	public void setTlsComExtPartnerCurrency(BigDecimal tlsComExtPartnerCurrency) {
		this.tlsComExtPartnerCurrency = tlsComExtPartnerCurrency;
	}

	public BigDecimal getTlsIntPpVnd() {
		return tlsIntPpVnd;
	}

	public void setTlsIntPpVnd(BigDecimal tlsIntPpVnd) {
		this.tlsIntPpVnd = tlsIntPpVnd;
	}

	public BigDecimal getTlsExtPpVnd() {
		return tlsExtPpVnd;
	}

	public void setTlsExtPpVnd(BigDecimal tlsExtPpVnd) {
		this.tlsExtPpVnd = tlsExtPpVnd;
	}

	public BigDecimal getTlsComExtPpVnd() {
		return tlsComExtPpVnd;
	}

	public void setTlsComExtPpVnd(BigDecimal tlsComExtPpVnd) {
		this.tlsComExtPpVnd = tlsComExtPpVnd;
	}

	public BigDecimal getTlsIntPpG() {
		return tlsIntPpG;
	}

	public void setTlsIntPpG(BigDecimal tlsIntPpG) {
		this.tlsIntPpG = tlsIntPpG;
	}

	public BigDecimal getTlsExtPpG() {
		return tlsExtPpG;
	}

	public void setTlsExtPpG(BigDecimal tlsExtPpG) {
		this.tlsExtPpG = tlsExtPpG;
	}

	public BigDecimal getTlsComExtPpG() {
		return tlsComExtPpG;
	}

	public void setTlsComExtPpG(BigDecimal tlsComExtPpG) {
		this.tlsComExtPpG = tlsComExtPpG;
	}

	public BigDecimal getTlsIntPpCurrency() {
		return tlsIntPpCurrency;
	}

	public void setTlsIntPpCurrency(BigDecimal tlsIntPpCurrency) {
		this.tlsIntPpCurrency = tlsIntPpCurrency;
	}

	public BigDecimal getTlsExtPpCurrency() {
		return tlsExtPpCurrency;
	}

	public void setTlsExtPpCurrency(BigDecimal tlsExtPpCurrency) {
		this.tlsExtPpCurrency = tlsExtPpCurrency;
	}

	public BigDecimal getTlsComExtPpCurrency() {
		return tlsComExtPpCurrency;
	}

	public void setTlsComExtPpCurrency(BigDecimal tlsComExtPpCurrency) {
		this.tlsComExtPpCurrency = tlsComExtPpCurrency;
	}

	public BigDecimal getTamPayPp() {
		return tamPayPp;
	}

	public void setTamPayPp(BigDecimal tamPayPp) {
		this.tamPayPp = tamPayPp;
	}

	public BigDecimal getTcuPp() {
		return tcuPp;
	}

	public void setTcuPp(BigDecimal tcuPp) {
		this.tcuPp = tcuPp;
	}

	public BigDecimal getTinPp() {
		return tinPp;
	}

	public void setTinPp(BigDecimal tinPp) {
		this.tinPp = tinPp;
	}

	public BigDecimal getTboPp() {
		return tboPp;
	}

	public void setTboPp(BigDecimal tboPp) {
		this.tboPp = tboPp;
	}

	public BigDecimal getIvstThreshldVnd() {
		return ivstThreshldVnd;
	}

	public void setIvstThreshldVnd(BigDecimal ivstThreshldVnd) {
		this.ivstThreshldVnd = ivstThreshldVnd;
	}

	public BigDecimal getIvstThreshldG() {
		return ivstThreshldG;
	}

	public void setIvstThreshldG(BigDecimal ivstThreshldG) {
		this.ivstThreshldG = ivstThreshldG;
	}

	public BigDecimal getIvstThreshldCurrency() {
		return ivstThreshldCurrency;
	}

	public void setIvstThreshldCurrency(BigDecimal ivstThreshldCurrency) {
		this.ivstThreshldCurrency = ivstThreshldCurrency;
	}

	public BigDecimal getTinPpVnd() {
		return tinPpVnd;
	}

	public void setTinPpVnd(BigDecimal tinPpVnd) {
		this.tinPpVnd = tinPpVnd;
	}

	public BigDecimal getTinPpG() {
		return tinPpG;
	}

	public void setTinPpG(BigDecimal tinPpG) {
		this.tinPpG = tinPpG;
	}

	public BigDecimal getTinPpCurrency() {
		return tinPpCurrency;
	}

	public void setTinPpCurrency(BigDecimal tinPpCurrency) {
		this.tinPpCurrency = tinPpCurrency;
	}
}
