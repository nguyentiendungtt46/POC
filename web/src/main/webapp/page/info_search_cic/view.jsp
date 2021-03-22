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
<!-- Modal Bo sung dich vu -->
<div class="modal fade" id="modal-view-file" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">

				<h6 class="modal-title" id="modal-title">Tìm kiếm khách hàng trên kho CIC</h6>
				<button type="button" class="close" data-dismiss="modal"
					style="color: #fff;">&times;</button>
			</div>
			<div class="modal-body">
				<div class="row">
					<div class="Row">
						<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">TCTD</div>
						<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							<select id="selectTCTD">
								<option value="">- Chọn -</option>
								<c:forEach items="#{lstPartner}" var="item">
									<option value="${item.code}">
										<c:out value="${item.code}" /> -
										<c:out value="${item.name}" />
									</option>
								</c:forEach>
							</select>
						</div>

					</div>
					<div class="Row">
						<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">File
							tra soát</div>
						<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							<input type="file" id="file-select" class="file-modal"
								name="inputFile" title="Chọn file" />
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn blue" onclick="uploadFile();">Chọn</button>
					<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
				</div>
				<spring:url var="sendReportAction" value="/rpFileUnstructured/uploadFile"></spring:url>
			</div>
		</div>
	</div>
	</div>
	<tiles:insertDefinition name="catalog">
		<tiles:putAttribute name="title"
			value="Tìm kiếm khách hàng trên kho CIC" />
		<tiles:putAttribute name="formInf">
			<spring:url value="/infoSearchCic" var="formAction" />
			<c:set var="commandName" scope="request" value="formDataModelAttr" />
		</tiles:putAttribute>

		<form:form cssClass="form-horizontal" id="theForm"
			enctype="multipart/form-data" modelAttribute="${commandName}"
			method="post" action='${formAction}'>
			<tiles:putAttribute name="catGrid">
				<div id="divGrid" align="left">
					<div class="row search-style">
						<div class="Table" id="divSearchInf">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Tìm kiếm khách hàng trên kho CIC</h1>
							</div>
							<div class="Row">
								<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tổ
									chức tín dụng</div>
								<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
									<%-- <form:input path="keyword_name" ></form:input> --%>
									<form:select class="form-control" path="maTCTD"
										id="maTCTD" title="Chọn đơn vị">
										<option value="">- Chọn -</option>
										<c:forEach items="#{lstPartner}" var="item">
											<form:option value="${item.id}">
												<c:out value="${item.code}" /> -
											<c:out value="${item.name}" />
											</form:option>
										</c:forEach>
									</form:select>
								</div>
								 <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại khách hàng</div>
								<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">		
								<form:select class="form-control" path="loaiKh" >
										<option value="">- Chọn -</option>
										<option value="1">Pháp nhân</option>
										<option value="2">Thể nhân</option>
									</form:select>
								</div>
							</div>
							<div class="divaction" align="center">
								<input class="btn blue" type="button" onclick="findData();"
									value="Tìm kiếm" /> 
<!-- 								<input class="btn blue" type="button" onclick="exportExcel();" -->
<!-- 									value="Export" />  -->
							</div>

						</div>
					</div>
					<%@ include file="/page/include/data_table.jsp"%>
				</div>
			</tiles:putAttribute>

			<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="infoSearchCic.id" id="id" />
			<form:hidden path="infoSearchCic.loaiDulieu" id="loaiDulieu" />
			<form:hidden path="infoSearchCic.yearOrand" id="yearOrand" />
			<div class="box-custom">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Thông tin tìm kiếm khách hàng trên kho CIC</h1>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
						TCTD</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<%-- <form:input path="rpReportCfgMaster.propertyCode" cssClass="required" title="Mã chỉ tiêu không được để trống"></form:input> --%>
						<form:select class="form-control" path="infoSearchCic.matctd"
										id="matctd" title="Chọn đơn vị">
										<option value="">- Chọn -</option>
										<c:forEach items="#{lstPartner}" var="item">
											<form:option value="${item.id}">
												<c:out value="${item.code}" /> -
											<c:out value="${item.name}" />
											</form:option>
										</c:forEach>
									</form:select>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại khách hàng</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<%-- <form:input path="rpReportCfgMaster.propertyCode" cssClass="required" title="Mã chỉ tiêu không được để trống"></form:input> --%>
						<form:select class="form-control" path="infoSearchCic.loaikh"
										id="loaikh" title="Chọn đơn vị">
										<option value="">- Chọn -</option>
										<option value="1">Pháp nhân</option>
										<option value="2">Thể nhân</option>
									</form:select>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tỉnh/Thành phố</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select class="form-control" path="infoSearchCic.thanhpho" >
										<option value="">- Chọn -</option>
										<c:forEach items="#{listProvince}" var="item">
											<form:option value="${item.id}">
												<c:out value="${item.code}" /> -
											<c:out value="${item.name}" />
											</form:option>
										</c:forEach>
									</form:select>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Địa bàn</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select class="form-control" path="infoSearchCic.diaban" >
										<option value="">- Chọn -</option>
										<c:forEach items="#{lstDiaBan}" var="item">
											<form:option value="${item.id}">
												<c:out value="${item.code}" /> -
											<c:out value="${item.value}" />
											</form:option>
										</c:forEach>
									</form:select>
					</div>
				</div>
				<div class="row">
				    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
				    <div class="label-static col-md-5 col-lg-5 col-sm-12 col-xs-12">
					<div class="form-check form-check-inline " style="float: left;">
					  <input class="form-check-input" type="radio" name="inlineRadioOptions" id="strRadioTCTD" value="1" onclick="viewTable(0);" />
					  <label class="form-check-label label-static" for="strRadioTCTD" style="font-size: 13px !important;">Theo chi nhánh TCTD</label>
					</div>
					<div class="form-check form-check-inline" style="float: left;">
					  <input class="form-check-input" type="radio" name="inlineRadioOptions" id="strRadiBank" value="2" onclick="viewTable(1);" />
					  <label class="form-check-label label-static" for="strRadiBank" style="font-size: 13px !important;">Theo hệ thống ngân hàng</label>
					</div>
					</div>
				</div>
				<div class="row" id="chiNhanhTCTD">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
					<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
						<form:select class="form-control" path="infoSearchCic.strDulieu" multiple="true" id="strDulieu">
										
						</form:select>
					</div>
				</div>
			</div>
			<div class="box-custom data-table">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h5 style="color: #339;">Phạm vi quan hệ tín dụng</h5>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
						TCTD</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<%-- <form:input path="rpReportCfgMaster.propertyCode" cssClass="required" title="Mã chỉ tiêu không được để trống"></form:input> --%>
						<select class="form-control" disabled="disabled"
										id="matctdReadOnly" title="Chọn đơn vị">
										<option value="">- Chọn -</option>
										<c:forEach items="#{lstPartner}" var="item">
											<option value="${item.id}">
												<c:out value="${item.code}" /> -
											<c:out value="${item.name}" />
											</option>
										</c:forEach>
									</select>
					</div>
					 <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="margin-top: 5px;" >Có quan hệ</div>
		             <div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
		                <form:checkbox id="coQh" path="infoSearchCic.coQh" value="true" onchange="setReadOnly(this);"></form:checkbox>
		             </div>
		             <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="margin-top: 5px;"  >Số tháng quan hệ</div>
		             <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
		                <form:input id="soThangQh" path="infoSearchCic.soThangQh" readonly="true"></form:input>
		             </div>
		             <div class="label-static col-md-5 col-lg-5 "></div>
					 <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="margin-top: 5px;">Không có quan hệ</div>
		             <div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.koQh" value="true"></form:checkbox>
		             </div>
		             <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="margin-top: 7px;">Chưa từng quan hệ</div>
		             <div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.ctQh" value="true"></form:checkbox>
		             </div>
		            
				</div>
				
			</div>
			
			<div class="box-custom data-table">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h5 style="color: #339;">Các thông tin khác</h5>
					</div>
					 <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="margin-top: 5px;">Có TSDB</div>
		             <div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.coTsdb"></form:checkbox>
		             </div>
		             <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="margin-top: 5px;">Không có TSDB</div>
		             <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.koTsdb"></form:checkbox>
		             </div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="margin-top: 5px;">Thông tin xếp hạng</div>
		             <div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.ttXephang"></form:checkbox>
		             </div>
		             <div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
		                <form:select id="ttXephangStr" class="form-control" path="infoSearchCic.ttXephangStr" multiple="true" >
										<c:forEach items="#{lstXepHang}" var="i">
											<form:option value="${i.id}">
												<c:out value="${i.code}" /> -
											<c:out value="${i.value}" />
											</form:option>
										</c:forEach>
									</form:select>
		             </div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="margin-top: 5px;">Điểm tín dụng cá nhân</div>
		             <div class="col-md-1 col-lg-1 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.tdCanhan"></form:checkbox>
		             </div>
		             <div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
		                <form:select id="tdCanhanStr" class="form-control" path="infoSearchCic.tdCanhanStr" multiple="true" >
										<c:forEach items="#{lstTDCN}" var="i">
											<form:option value="${i.id}">
												<c:out value="${i.code}" /> -
											<c:out value="${i.value}" />
											</form:option>
										</c:forEach>
									</form:select>
		             </div>
				</div>
				
			</div>
			<div class="row">
				<div class="col-md-6 col-lg-6" style=" padding: 0 5px 0 0;">
					<div class="box-custom data-table">
						<div class="row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h5 style="color: #339;">Thông tin tài chính doanh nghiệp</h5>
							</div>
				             <div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
				                <form:checkbox path="infoSearchCic.dnghiep" cssStyle="float: left;"></form:checkbox>
				                <span style="font-weight: 600;margin: 2px 10px;float: left;">Doanh nghiệp</span>
				             </div>
				             <div class="col-md-6 col-lg-6 col-sm-12 col-xs-12" style="    display: flex;">
									<form:input path="infoSearchCic.dnghiepTu" cssClass="form-control" style="border-bottom-right-radius: 0px;border-top-right-radius: 0px;margin-top: 0px;"/>
									<span class="input-group-text" id="basic-addon3" style="padding: 2px 10px;font-size: 13px;border-radius: 1px !important;font-weight: bold;">đến</span>
									<form:input path="infoSearchCic.dnghiepDen" cssClass="form-control" style="border-bottom-left-radius: 0px;border-top-left-radius: 0px;margin-top: 0px;"/>
							</div>
							 <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
				                <form:checkbox path="infoSearchCic.dnghiepSym" cssStyle="float: left;"></form:checkbox>
				                <span style="font-weight: 600;margin: 2px 10px;float: left;">></span>
				             </div>
						</div>
						<div class="row">
				             <div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
				                <form:checkbox path="infoSearchCic.ttsan" cssStyle="float: left;"></form:checkbox>
				                <span style="font-weight: 600;margin: 2px 10px;float: left;">Tổng tài sản</span>
				             </div>
				             <div class="col-md-6 col-lg-6 col-sm-12 col-xs-12" style="    display: flex;">
									<form:input path="infoSearchCic.ttsanTu" cssClass="form-control" style="border-bottom-right-radius: 0px;border-top-right-radius: 0px;margin-top: 0px;"/>
									<span class="input-group-text" id="basic-addon3" style="padding: 2px 10px;font-size: 13px;border-radius: 1px !important;font-weight: bold;">đến</span>
									<form:input path="infoSearchCic.ttsanDen" cssClass="form-control" style="border-bottom-left-radius: 0px;border-top-left-radius: 0px;margin-top: 0px;"/>
							</div>
							 <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
				                <form:checkbox path="infoSearchCic.ttsanSym" cssStyle="float: left;"></form:checkbox>
				                <span style="font-weight: 600;margin: 2px 10px;float: left;">></span>
				             </div>
						</div>
						<div class="row">
				             <div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
				                <form:checkbox path="infoSearchCic.lnhuan" cssStyle="float: left;"></form:checkbox>
				                <span style="font-weight: 600;margin: 2px 10px;float: left;">Lợi nhuận</span>
				             </div>
				             <div class="col-md-6 col-lg-6 col-sm-12 col-xs-12" style="    display: flex;">
									<form:input path="infoSearchCic.lnhuanTu" cssClass="form-control" style="border-bottom-right-radius: 0px;border-top-right-radius: 0px;margin-top: 0px;"/>
									<span class="input-group-text" id="basic-addon3" style="padding: 2px 10px;font-size: 13px;border-radius: 1px !important;font-weight: bold;">đến</span>
									<form:input path="infoSearchCic.lnhuanDen" cssClass="form-control" style="border-bottom-left-radius: 0px;border-top-left-radius: 0px;margin-top: 0px;"/>
							</div>
							 <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
				                <form:checkbox path="infoSearchCic.lnhuanSym" cssStyle="float: left;"></form:checkbox>
				                <span style="font-weight: 600;margin: 2px 10px;float: left;">></span>
				             </div>
						</div>
						
						<div class="row">
							<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
								<div class="form-check form-check-inline " style="float: left;">
								  <input class="form-check-input" type="radio" name="radiOGroup" id="strOr" value="1" onclick="setYearAndOr(0);" />
								  <label class="form-check-label label-static" for="strRadioTCTD" style="font-size: 13px !important;">Hoặc</label>
								</div>
								<div class="form-check form-check-inline" style="float: left;">
								  <input class="form-check-input" type="radio" name="radiOGroup" id="strAnd" value="2" onclick="setYearAndOr(1);" />
								  <label class="form-check-label label-static" for="strRadiBank" style="font-size: 13px !important;">Và</label>
								</div>
							</div>
							<div class="row col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12" style="margin-top: 5px;">Số năm</div>
								<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
									<form:input path="infoSearchCic.yearCount" id="yearCount"></form:input>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="col-md-6 col-lg-6" style="padding: 0 0 0 5px;">
					<div class="box-custom data-table">
						<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h5 style="color: #339;">Dư nợ/Nhóm dư nợ hiện tại</h5>
					</div>
					<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12" style="margin-top: 5px;">Nhóm 1</div>
		             <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.nhomno1Yes" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Có</span>
		             </div>
		             <div class="col-md-6 col-lg-6 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.nhomno1No" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Không</span>
		             </div>
				</div>
				<div class="row">
					<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12" style="margin-top: 5px;">Nhóm 2</div>
		             <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.nhomno2Yes" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Có</span>
		             </div>
		             <div class="col-md-6 col-lg-6 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.nhomno2No" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Không</span>
		             </div>
				</div>
				<div class="row">
					<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12" style="margin-top: 5px;">Nhóm 3</div>
		             <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.nhomno3Yes" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Có</span>
		             </div>
		             <div class="col-md-6 col-lg-6 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.nhomno3No" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Không</span>
		             </div>
				</div>
				<div class="row">
					<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12" style="margin-top: 5px;">Nhóm 4</div>
		             <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.nhomno4Yes" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Có</span>
		             </div>
		             <div class="col-md-6 col-lg-6 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.nhomno4No" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Không</span>
		             </div>
				</div>
				<div class="row">
					<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12" style="margin-top: 5px;">Nhóm 5</div>
		             <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.nhomno5Yes" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Có</span>
		             </div>
		             <div class="col-md-6 col-lg-6 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.nhomno5No" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Không</span>
		             </div>
				</div>
				<div class="row">
					<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12" style="margin-top: 5px;">VAMC</div>
		             <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.vamcYes" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Có</span>
		             </div>
		             <div class="col-md-6 col-lg-6 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.vamcNo" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Không</span>
		             </div>
				</div>
				<div class="row">
					<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12" style="margin-top: 5px;">Nợ xấu khác</div>
		             <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.noxauYes" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Có</span>
		             </div>
		             <div class="col-md-6 col-lg-6 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.noxauNo" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Không</span>
		             </div>
				</div>
				<div class="row">
					<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12" style="margin-top: 5px;">Dư nợ ngoại tệ</div>
		             <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.dnNgoaiteYes" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Có</span>
		             </div>
		             <div class="col-md-6 col-lg-6 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.dnNgoaiteNo" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">Không</span>
		             </div>
				</div>
				<div class="row">
					<div class="label-static col-md-4 col-lg-4 col-sm-12 col-xs-12" style="margin-top: 5px;">Dư nợ</div>
		             <div class="col-md-6 col-lg-6 col-sm-12 col-xs-12">
		                <div class="col-md-12 col-lg-12 col-sm-12 col-xs-12" style="    display: flex;">
									<form:input path="infoSearchCic.dunoTu" cssClass="form-control" style="border-bottom-right-radius: 0px;border-top-right-radius: 0px;margin-top: 0px;"/>
									<span class="input-group-text" id="basic-addon3" style="padding: 2px 10px;font-size: 13px;border-radius: 1px !important;font-weight: bold;">đến</span>
									<form:input path="infoSearchCic.dunoDen" cssClass="form-control" style="border-bottom-left-radius: 0px;border-top-left-radius: 0px;margin-top: 0px;"/>
									</div>
		             </div>
		             <div class="col-md-2 col-lg-2 col-sm-12 col-xs-12">
		                <form:checkbox path="infoSearchCic.dunoSym" cssStyle="float: left;"></form:checkbox>
		                <span style="font-weight: 600;margin: 2px 10px;float: left;">></span>
		             </div>
				</div>
					</div>
				</div>
			</div>
			
				
		</tiles:putAttribute>
		</form:form>

		<tiles:putAttribute name="extra-scripts">
			<script type="text/javascript">
				function initParam(tblCfg) {
					tblCfg.bFilter = false;
					tblCfg.aoColumns = [ {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'STT'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Mã tổ chức tín dụng'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Loại khách hàng'
					} , {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'hành động'
					}];
				}

				$(document).ready(function() {
					//$("input[name=inlineRadioOptions][value=" + value + "]").attr('checked', 'checked');
					//$(".btnDtAdd, .btnDtDelete").hide();

					// 		     $(".currentDate").datepicker({
					// 					dateFormat : 'dd/mm/yy',
					// 					changeMonth : false,
					// 					changeYear : false,
					// 					showButtonPanel : false
					// 			});
				});
				function beforeEdit(res){
					$("input[name=inlineRadioOptions][value=" + res.loaikh + "]").prop('checked', 'checked');
					$("#matctd").val(res.matctd);
					viewTable(res.loaikh);
				}
				
				function afterEdit(id, res){
					var strDulieu = res.strDulieu;
					var ttXephangStr = res.ttXephangStr;
					var tdCanhanStr = res.tdCanhanStr;
					if (strDulieu != null && strDulieu != '' && strDulieu != "") {
						$.each(strDulieu.split(","), function(i,e){
						    $("#strDulieu option[value='" + e + "']").prop("selected", true);
						});
					}
					if (ttXephangStr != null && ttXephangStr != '' && ttXephangStr != "") {
						$.each(ttXephangStr.split(","), function(i,e){
						    $("#ttXephangStr option[value='" + e + "']").prop("selected", true);
						});
					}
					if (tdCanhanStr != null && tdCanhanStr != '' && tdCanhanStr != "") {
						$.each(tdCanhanStr.split(","), function(i,e){
						    $("#tdCanhanStr option[value='" + e + "']").prop("selected", true);
						});
					}
					$("#strDulieu").select2();
					$("#ttXephangStr").select2();
					$("#tdCanhanStr").select2();
				}

				function viewTable(val){
					$("#loaiDulieu").val(val);
					$('#strDulieu').empty();
					if(val == 0){
						var partnerId = $("#matctd").val();
						if(partnerId == null || partnerId == ''){
							document.getElementById('strRadioTCTD').checked = false;
							alert('Hãy chọn tổ chức tín dụng');
						}else
							loadPartnerBranch(partnerId);
					} else if(val == 1){
						loadPartner();
					}
				}
				
				function setYearAndOr(val){
					$("#yearOrand").val(val);
				}
				
				function loadPartnerBranch(partnerId){
					$.ajax({
						url : $('#theForm').attr('action') + '?method=loadPartnerBranch',
						data : {
							partnerId : partnerId
						},
						method : 'GET',
						async : false,
						success : function(_result) {
							if (_result != null) {
								myJSON = JSON.parse(_result);
								console.log(myJSON);
								$.each(myJSON, function(i, item) {
									$('#strDulieu').append(
											$('<option>', {
												value : item.id,
												text : item.code + " - " + item.name
											}));
								});
							}
						}
					});
				}
				
				function loadPartner(partnerId){
					$.ajax({
						url : $('#theForm').attr('action') + '?method=loadPartner',
						data : {
							//partnerId : partnerId
						},
						method : 'GET',
						async : false,
						success : function(_result) {
							if (_result != null) {
								myJSON = JSON.parse(_result);
								console.log(myJSON);
								$.each(myJSON, function(i, item) {
									$('#strDulieu').append(
											$('<option>', {
												value : item.id,
												text : item.code + " - " + item.name
											}));
								});
							}
						}
					});
				}
				
				$(document.body).on("change","select#matctd",function(){	
					$("#matctdReadOnly").val(this.value);
					$("#matctdReadOnly").select2();
				});
				
				function setReadOnly(val){
					if ($(val).is(':checked')) {
						$('#soThangQh').prop('readonly', false);
					}else{
						$('#soThangQh').val('');
						$('#soThangQh').attr('readonly', "true");
					}
				}
			</script>
		</tiles:putAttribute>
	</tiles:insertDefinition>