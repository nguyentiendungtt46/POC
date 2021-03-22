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
		value="Danh mục lớp học" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/cfgClass" var="formAction" />
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
							<h1>Danh mục lớp học</h1>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								lớp</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="keyword_code" cssClass="form-control"></form:input>
							</div>
							<div class="Empty"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tỉnh/Thành phố</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<%-- <form:input path="keyword_name" cssClass="form-control"></form:input> --%>
								<form:select path="keyword_name" >
									<form:option value="">- Chọn -</form:option>
										<c:forEach items="#{lstProvince}" var="item">
											<form:option value="${item.id}">
												<c:out value="${item.value}" /> -
												<c:out value="${item.description}" />
											</form:option>
										</c:forEach>
								</form:select>
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
			<form:hidden path="catClass.id" id="id" />
			<div class="box-custom">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Thông tin chi tiết lớp học</h1>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã lớp học<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catClass.code" cssClass="required uppercase ascii"
							title="Mã câu hỏi không được để trống" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Tỉnh/Thành phố<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="catClass.tinhthanh" cssClass=" provincecode" title="Chọn một Tỉnh/thành phố">
							<form:option value="">- Chọn -</form:option>
								<c:forEach items="#{lstProvince}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.value}" /> -
										<c:out value="${item.description}" />
									</form:option>
								</c:forEach>
						</form:select>
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
					"sTitle" : 'Mã lớp'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tỉnh/Thành phố'
				} ];
			}

			$(document).ready(function() {
				$(".btnDtDelete").hide();
			});
			
			
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>