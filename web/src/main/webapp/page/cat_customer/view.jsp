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

<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title"
		value="Danh mục khách hàng" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/catCustomer" var="formAction" />
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
							<h1>Danh mục khách hàng</h1>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								khách hàng</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="keyword_code" cssClass="form-control"></form:input>
							</div>
							<div class="Empty"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã Cic</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="keyword_name" cssClass="form-control"></form:input>
							</div>
						</div>
						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="findData();"
								value="Tìm kiếm" />
								<c:if test="${add}">
								<input class="btnDtAdd btn blue" type="button" id="btnDtAdd"
									onclick="addNew();" value="Thêm mới" />
							</c:if>
						</div>
						<!-- <div align="center" class="HeaderText">&#8203;</div> -->

					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="catCustomer.id" id="id" />
			<div class="box-custom">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Thông tin chi tiết khách hàng</h1>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã khách hàng<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catCustomer.maKh" cssClass="required uppercase ascii"
							title="Mã khách hàng không được để trống" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Tên khách hàng<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catCustomer.tenKh" cssClass="required"
							title="Tên khách hàng không được để trống" />
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã CIC<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catCustomer.maCic" cssClass="required"
							title="Mã CICkhông được để trống" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Điện thoại<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catCustomer.dienThoai" cssClass="required"
							title="Tên không được để trống" />
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số CMT<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catCustomer.soCmt" cssClass="required"
							title="Mã không được để trống" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã số thuế<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catCustomer.msThue" cssClass="required"
							title="Tên không được để trống" />
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số ĐKKD<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catCustomer.dkkd" cssClass="required"
							title="Mã không được để trống" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Ngôn ngữ<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catCustomer.ngonNgu" />
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Tên giám đốc<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catCustomer.tenGiamDoc" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Năm tài chính<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catCustomer.namTaiChinh" cssClass="required"
							title="Tên không được để trống" />
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Địa chỉ
					</div>
					<div class="col-md-10 col-lg-8 col-sm-12 col-xs-12">
						<form:input path="catCustomer.diaChi" />
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Ghi chú
					</div>
					<div class="col-md-10 col-lg-8 col-sm-12 col-xs-12">
						<form:input path="catCustomer.ghiChu" />
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
					"sTitle" : 'Mã Khách hàng'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã cic'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên khách hàng'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Số CMT'
				} ];
			}

			$(document).ready(function() {
				$(".btnDtDelete").hide();
			});
			
			
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>