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
		value="Danh mục câu hỏi phỏng vấn" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/cfgQuestion" var="formAction" />
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
							<h1>Danh mục câu hỏi phỏng vấn</h1>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								câu hỏi</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="keyword_code" cssClass="form-control"></form:input>
							</div>
							<div class="Empty"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Nội dung câu hỏi</div>
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
			<form:hidden path="catQuestion.id" id="id" />
			<div class="box-custom">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Thông tin chi tiết câu hỏi</h1>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã câu hỏi<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catQuestion.maCauHoi" cssClass="required uppercase ascii"
							title="Mã câu hỏi không được để trống" />
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Câu hỏi phỏng vấn<font color="red">*</font>
					</div>
					<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
						<form:input path="catQuestion.cauHoi" cssClass="required"
							title="Câu hỏi phỏng vấn không được để trống" />
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
					"sTitle" : 'Mã câu hỏi'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Câu hỏi phỏng vấn'
				} ];
			}

			$(document).ready(function() {
				$(".btnDtDelete").hide();
			});
			
			
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>