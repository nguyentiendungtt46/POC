<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="entity.frwk.SysUsers"%>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@page import="constants.RightConstants"%>
<%@page
	import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="frwk.dao.hibernate.sys.SysParamDao"%>
<%@ page import="frwk.utils.ApplicationContext"%>
<%
	ApplicationContext appContext = (ApplicationContext) request.getSession()
			.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
	SysUsers user = null;
	if (appContext != null)
		user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
	WebApplicationContext ac = RequestContextUtils.findWebApplicationContext(request, null);
	SysParamDao sysParamDao = (SysParamDao) ac.getBean("sysParamDao");
	entity.frwk.SysParam LDAP_AUTHEN = sysParamDao.getSysParamByCode("LDAP_AUTHEN");
%>

<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Tham số xử lý báo cáo" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/rpM1Filter" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm"
		enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id="divSearchInf">
						<div class="row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Tham số xử lý báo cáo</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã báo cáo</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <select class="form-control" name="reportCode" id="reportCode">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstRpType}" var="item">
										<option value="${item.id}">
											<c:out value="${item.id}" />
										</option>
									</c:forEach>
								</select>
		                    </div>
		                    <div class="Empty"></div>
						</div>
						<!-- <div align="center" class="HeaderText">&#8203;</div> -->
						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="findData();"
								value="Tìm kiếm" />
						</div>
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="rpM1Filter.id" id="id" />
			<div class="Row box-custom">
						<div class="row title-page" style="adding-bottom: 20px;">
							<h1>Thông tin chi tiết</h1>
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							Mã báo cáo<font color="red">*</font>
						</div>
						<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
							<form:select path="rpM1Filter.rpCode.id" onchange="displayReportDiv($(this).val())" cssClass="from-control required" title="Mã báo cáo không được để trống">
								<option value="">- Chọn -</option>
								<c:forEach items="#{lstRpType}" var="item">
									<option value="${item.id}">
										<c:out value="${item.id}" />
									</option>
								</c:forEach>
							</form:select>
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
							Trạng thái
						</div>
						<div class="col-md-6 col-lg-6 col-sm-12 col-xs-12">
							<form:select path="rpM1Filter.disableFilter">
										<option value="">- Chọn -</option>
										<option value="0">Chuyển file sang M1 nếu không có lỗi hoặc số lượng lỗi nằm trong ngưỡng</option>
										<option value="1">Luôn chuyển file sang M1</option>
							</form:select>
						</div>
					</div>
			<div class="box-custom">
				<div id="K31X1A_K31X2A">
					<div class="Row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Lỗi cho phép đẩy sang M1
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<input name="rpM1Filter.errThreshld" class="decimal" type="text" precision="4" scale ="2">
						</div>
					</div>
					<div class="row">
						<h2>1. Sai số so với số liệu TCTD gửi</h2>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file với số liệu TCTD gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsPartnerVnd" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file với số liệu TCTD gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsPartnerG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file với số liệu TCTD gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsPartnerCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<h2>2. Sai số so với số liệu kỳ trước</h2>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file với số liệu kỳ trước gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsPpVnd" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file với số liệu kỳ trước gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsPpG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file với số liệu kỳ trước gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsPpCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
				</div>
				
				<div id="K31X3">
					<div class="Row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Lỗi cho phép đẩy sang M1
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<input name="rpM1Filter.errThreshld" precision="4" scale ="2" class="decimal" type="text">
						</div>
					</div>
					
					<div class="row">
						<h2>1. Sai số so với số liệu TCTD gửi</h2>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file với số liệu TCTD gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsPartnerVnd" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file với số liệu TCTD gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsPartnerG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file với số liệu TCTD gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsPartnerCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<h2>2. Sai số so với số liệu kỳ trước</h2>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file với số liệu kỳ trước gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsPpVnd" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file với số liệu kỳ trước gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsPpG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file với số liệu kỳ trước gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsPpCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<h2>3. Sai số so với số liệu file K32 tương ứng</h2>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file K31X3 với số liệu K32 tương ứng theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.k31x3VsK32BalInVndThreshld" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file K31X3 với số liệu K32 tương ứng  theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsK31x3WthK32G" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file K31X3 với số liệu K32 tương ứng theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.k31x3VsK32BalInForThreshld" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12" style="PADDING-LEFT: 0px;">
							<h2 style="text-align: left;">4. Kiểm tra chi tiết từng KH giữa K31X3 và K32 cùng thời điểm báo cáo</h2>
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="rpM1Filter.disableCheckK31x3K32">
										<option value="">- Chọn -</option>
										<option value="0">Có</option>
										<option value="1">Không</option>
							</form:select>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số dư nợ theo khách hàng trong file K31X3 với số liệu K32 tương ứng theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.k3vx3WthOtherThreshld" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số dư nợ theo khách hàng trong file K31X3 với số liệu K32 tương ứng  theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.loanCusK31x3WthK32G" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số dư nợ theo khách hàng trong file K31X3 với số liệu K32 tương ứng theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.loanCusK31x3WthK32Currency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Hồ sơ sai lệch mã loại vay trong file K31X3 so với số liệu file K32
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.k31x3VsK32DifThreshld" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Hồ sơ sai lệch mã nhóm nợ trong file K31X3 so với số liệu file K32
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.k31x3VsK32DifLnTypeThreshld" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
				</div>
				
				
				
				<div id="K32">
					
					
					<div class="Row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Lỗi cho phép đẩy sang M1
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<input name="rpM1Filter.errThreshld" precision="4" scale ="2" class="decimal" type="text">
						</div>
					</div>
					
					<div class="row">
						<h2>1. Sai số so với số liệu TCTD gửi</h2>
					</div>
					
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ nội bảng trong file với số liệu TCTD gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.t02gxExternal" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ ngoại bảng trong file với số liệu TCTD gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.t02gxInternal" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ cam kết ngoại bảng trong file với số liệu TCTD gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.t02gxCmt" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ nội bảng trong file với số liệu TCTD gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsIntPartnerG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ ngoại bảng trong file với số liệu TCTD gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsExtPartnerG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ cam kết ngoại bảng trong file với số liệu TCTD gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsComExtPartnerG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ nội bảng trong file với số liệu TCTD gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsIntPartnerCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ ngoại bảng trong file với số liệu TCTD gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsExtPartnerCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ cam kết ngoại bảng trong file với số liệu TCTD gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsComExtPartnerCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					
					<div class="row">
						<h2>2. Sai số so với số liệu kỳ trước</h2>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ nội bảng trong file với số liệu kỳ trước gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsIntPpVnd" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ ngoại bảng trong file với số liệu kỳ trước gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsExtPpVnd" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ cam kết ngoại bảng trong file với số liệu kỳ trước gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsComExtPpVnd" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ nội bảng trong file với số liệu kỳ trước gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsIntPpG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ ngoại bảng trong file với số liệu kỳ trước gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsExtPpG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ cam kết ngoại bảng trong file với số liệu kỳ trước gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsComExtPpG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ nội bảng trong file với số liệu kỳ trước gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsIntPpCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ ngoại bảng trong file với số liệu kỳ trước gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsExtPpCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ cam kết ngoại bảng trong file với số liệu kỳ trước gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsComExtPpCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<h2>3. Sai số so với số liệu file K32 tương ứng</h2>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file K31X3 với số liệu K32 tương ứng theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.k31x3VsK32BalInVndThreshld" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file K31X3 với số liệu K32 tương ứng  theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsK31x3WthK32G" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ trong file K31X3 với số liệu K32 tương ứng theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.k31x3VsK32BalInForThreshld" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12" style="PADDING-LEFT: 0px;">
							<h2 style="text-align: left;">4. Kiểm tra chi tiết từng KH giữa K31X3 và K32 cùng thời điểm báo cáo</h2>
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="rpM1Filter.disableCheckK31x3K32">
										<option value="">- Chọn -</option>
										<option value="0">Có</option>
										<option value="1">Không</option>
							</form:select>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số dư nợ theo khách hàng trong file K31X3 với số liệu K32 tương ứng theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.k3vx3WthOtherThreshld" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số dư nợ theo khách hàng trong file K31X3 với số liệu K32 tương ứng  theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.loanCusK31x3WthK32G" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số dư nợ theo khách hàng trong file K31X3 với số liệu K32 tương ứng theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.loanCusK31x3WthK32Currency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Hồ sơ sai lệch mã loại vay trong file K31X3 so với số liệu file K32
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.k31x3VsK32DifThreshld" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Hồ sơ sai lệch mã nhóm nợ trong file K31X3 so với số liệu file K32
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.k31x3VsK32DifLnTypeThreshld" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
				</div>
				
				<div id="K3333">
					
					
					<div class="Row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Lỗi cho phép đẩy sang M1
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<input name="rpM1Filter.errThreshld" precision="4" scale ="2" class="decimal" type="text">
						</div>
					</div>
					
					<div class="row">
						<h2>1. Sai số so với số liệu TCTD gửi</h2>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng số tiền phải thanh toán trong file với số liệu TCTD gửi
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.pmtAmtThreshld" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng số khách hàng trong file với số liệu TCTD
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.cusAmtThreshld" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					
					<div class="row">
						<h2>2. Sai số so với số liệu kỳ trước</h2>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng số tiền phải thanh toán trong file với số liệu kỳ trước
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tamPayPp" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng số khách hàng trong file với số liệu kỳ trước
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tcuPp" precision="4" scale ="2" class="decimal"/>
						</div>
					</div> 
				</div>
				
				<div id="K40X">
					
					<div class="Row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Lỗi cho phép đẩy sang M1
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<input name="rpM1Filter.errThreshld" precision="4" scale ="2" class="decimal" type="text">
						</div>
					</div>
					<div class="Row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng  giá trị tài sản trong file với số liệu kỳ trước
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<input name="rpM1Filter.astThreshld" precision="4" scale ="2" class="decimal" type="text">
						</div>
					</div>
				</div>
				
				<div id="K5">
					
					<div class="Row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Lỗi cho phép đẩy sang M1
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<input name="rpM1Filter.errThreshld" precision="4" scale ="2" class="decimal" type="text">
						</div>
					</div>
					
					<div class="row">
						<h2>1. Sai số so với số liệu TCTD gửi</h2>
					</div>
					<%-- <div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng giá trị đầu tư trong file với số liệu TCTD gửi
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.ivstThreshld" precision="4" scale ="2" class="decimal"/>
						</div>
					</div> --%>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số giá trị đầu tư trong file với số liệu TCTD với loại tiền VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.ivstThreshldVnd" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số giá trị đầu tư trong file với số liệu TCTD với loại tiền Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.ivstThreshldG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số giá trị đầu tư trong file với số liệu TCTD với loại tiền Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.ivstThreshldCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng số lượng trái phiếu trong file với số liệu TCTD
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.bondAmtThreshld" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					
					<div class="row">
						<h2>2. Sai số so với số liệu kỳ trước</h2>
					</div>
					<%-- <div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng giá trị đầu tư trong file với số liệu kỳ trước
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tinPp" precision="4" scale ="2" class="decimal"/>
						</div>
					</div> --%>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng giá trị đầu tư trong file với số liệu kỳ trước với loại tiền VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tinPpVnd" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng giá trị đầu tư trong file với số liệu kỳ trước với loại tiền Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tinPpG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng giá trị đầu tư trong file với số liệu kỳ trước với loại tiền Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tinPpCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng số lượng trái phiếu trong file với số liệu kỳ trước
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tboPp" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
				</div>
				
				<div id="T02G1_T02G2">
					
					<div class="Row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Lỗi cho phép đẩy sang M1
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<input name="rpM1Filter.errThreshld" precision="4" scale ="2" class="decimal" type="text">
						</div>
					</div>
					
					<div class="row">
						<h2>1. Sai số so với số liệu TCTD gửi</h2>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ nội bảng trong file với số liệu TCTD gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.t02gxExternal" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ ngoại bảng trong file với số liệu TCTD gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.t02gxInternal" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ cam kết ngoại bảng trong file với số liệu TCTD gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.t02gxCmt" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ nội bảng trong file với số liệu TCTD gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsIntPartnerG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ ngoại bảng trong file với số liệu TCTD gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsExtPartnerG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ cam kết ngoại bảng trong file với số liệu TCTD gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsComExtPartnerG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ nội bảng trong file với số liệu TCTD gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsIntPartnerCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ ngoại bảng trong file với số liệu TCTD gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsExtPartnerCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ cam kết ngoại bảng trong file với số liệu TCTD gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsComExtPartnerCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<h2>2. Sai số so với số liệu kỳ trước</h2>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ nội bảng trong file với số liệu kỳ trước gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsIntPpVnd" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ ngoại bảng trong file với số liệu kỳ trước gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsExtPpVnd" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ cam kết ngoại bảng trong file với số liệu kỳ trước gửi theo VND
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsComExtPpVnd" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ nội bảng trong file với số liệu kỳ trước gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsIntPpG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ ngoại bảng trong file với số liệu kỳ trước gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsExtPpG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ cam kết ngoại bảng trong file với số liệu kỳ trước gửi theo Vàng
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsComExtPpG" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ nội bảng trong file với số liệu kỳ trước gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsIntPpCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ ngoại bảng trong file với số liệu kỳ trước gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsExtPpCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
					<div class="row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Sai số tổng dư nợ cam kết ngoại bảng trong file với số liệu kỳ trước gửi theo Ngoại tệ
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="rpM1Filter.tlsComExtPpCurrency" precision="4" scale ="2" class="decimal"/>
						</div>
					</div>
				</div>
				
				<div id="K102_K101_K113">
					<!-- them K31X1, K31X2 -->
					<div class="Row">
						<div style="text-align: left;" class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							% Lỗi cho phép đẩy sang M1
						</div>
						<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<input name="rpM1Filter.errThreshld" precision="4" scale ="2" class="decimal" type="text">
						</div>
					</div>
				</div>
				
				
				
			</div>
		</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg){	
	            tblCfg.aoColumns = [			 
		            {"sClass": "left","bSortable" : false,"sTitle":'STT'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Mã báo cáo'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Trạng thái'}
	            ];
			}
			$(document).ready(function() {
			     $('.btnDtDelete').hide();
			});
			function beforeSearch(){
				hideAllDiv();
			}
			function hideAllDiv(){
				$("#K31X1A_K31X2A").hide();disabledDiv('K31X1A_K31X2A',true);
				$("#K31X3").hide();disabledDiv('K31X3',true);
				$("#K32").hide();disabledDiv('K32',true);
				$("#K3333").hide();disabledDiv('K3333',true);
				$("#K40X").hide();disabledDiv('K40X',true);
				$("#K5").hide();disabledDiv('K5',true);
				$("#T02G1_T02G2").hide();disabledDiv('T02G1_T02G2',true);
				$("#K102_K101_K113").hide();disabledDiv('K102_K101_K113',true);
			}
			function beforeEdit(data){
				hideAllDiv();
			}
			function defaultValue(){
				hideAllDiv();
				
			}
			function displayReportDiv(code){
				hideAllDiv();
				switch(code) {
				  case "K3122A":
				  case "K3121A":
				  case "K3111A":
				  case "K3112A":
					  disabledDiv('K31X1A_K31X2A',false);
					  $("#K31X1A_K31X2A").show();
				    break;
				  case "K3113":
				  case "K3123":
					  disabledDiv('K31X3',false);
					  $("#K31X3").show();
				    break;
				  case "K3223":
				  case "K3213":
					  disabledDiv('K32',false);
					  $("#K32").show();
					break;
				  case "K3333":
				  case "K3331":
				  case "K3332":
					  disabledDiv('K3333',false);
					  $("#K3333").show();
					break;
				  case "K4011":
				  case "K4012":
				  case "K4013":
				  case "K4021":
				  case "K4022":
				  case "K4023":
					  disabledDiv('K40X',false);
					  $("#K40X").show();
					break;
				  case "K5011":
				  case "K5012":
				  case "K5013":
					  disabledDiv('K5',false);
					  $("#K5").show();
					break;
				  case "T02G1":
				  case "T02G2":
					  disabledDiv('T02G1_T02G2',false);
					  $("#T02G1_T02G2").show();
					break;
				  case "K1011":
				  case "K1131":
				  case "K1132":
				  case "K1133":
				  case "K2011":
				  case "K2012":
				  case "K2013": 
				  case "K2B011":
				  case "K2B012":
				  case "K2B013":
				  case "K1012":
				  case "K1013":
				  case "K1021":
				  case "K1022":
				  case "K1023":
				  case "K3111":
				  case "K3112":
				  case "K3121":
				  case "K3122":
				  case "T02DS":
					  disabledDiv('K102_K101_K113',false);
					  $("#K102_K101_K113").show();
					break;
				}
			}
			function afterEdit(id, data){
				var code = data.rpCode.id;
				displayReportDiv(code);
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
	
	
