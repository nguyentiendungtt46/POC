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
		value="Báo cáo tình hình xử lý yêu cầu báo cáo" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/reportProcessRp" var="formAction" />
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
							<h1>Báo cáo tình hình xử lý yêu cầu báo cáo</h1>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian kết nối từ</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="fromDate" name="fromDate" cssClass="currentDate required" value="${startDate}"></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian kết nối đến</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <form:input path="toDate" name="toDate" cssClass="currentDate required" value="${endDate}"></form:input>
		                    </div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								TCTD</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select class="form-control" path="partnerId"
									title="Chọn đơn vị">
									<option value="">- Chọn -</option>
									<c:forEach items="#{dsDoiTac}" var="item">
										<option value="${item.id}">
											<c:out value="${item.code}" /> -
											<c:out value="${item.name}" />
										</option>
									</c:forEach>
								</form:select>
							</div>
						</div>
						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="exportExcel();" value="Export" />
							<input class="btn blue" type="button" onclick="exportExcelDetail();" value="Export Detail" />
						</div>
						<!-- <div align="center" class="HeaderText">&#8203;</div> -->

					</div>
				</div>
			</div>
		</tiles:putAttribute>

	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">

			$(document).ready(function() {
				$(".btnDtDelete").hide();
				$(".btnDtAdd").hide();
				$(".currentDate").datepicker({
					dateFormat : 'dd/mm/yy',
					changeMonth : false,
					changeYear : false,
					showButtonPanel : false
			});
			});
			
			function exportExcel() {
				var fromDate = $('#fromDate').val();
				var toDate = $('#toDate').val();
				var partnerId = $('#partnerId').val();
				window.open($('#theForm').attr('action')
						+ '?method=exportExcel&fromDate=' + fromDate + '&toDate=' + toDate + '&partnerId='+ partnerId);
			}
			function exportExcelDetail() {
				var fromDate = $('#fromDate').val();
				var toDate = $('#toDate').val();
				var partnerId = $('#partnerId').val();
				if (partnerId == '' || partnerId == null || partnerId == "") {
					alert("Bạn phải chọn tổ chức tín dụng với báo cáo chi tiết");
					return;
				}
				window.open($('#theForm').attr('action')
						+ '?method=exportExcelDetail&fromDate=' + fromDate + '&toDate=' + toDate + '&partnerId='+ partnerId);
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>