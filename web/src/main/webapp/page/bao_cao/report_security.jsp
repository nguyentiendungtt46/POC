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
	<tiles:putAttribute name="title" value="Báo cáo rule an toàn bảo mật" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/reportSecurity" var="formAction" />
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
							<h1>Báo cáo rule an toàn bảo mật</h1>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								TCTD</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select class="form-control" path="job_name"
									title="Chọn đơn vị">
									<option value="">- Chọn -</option>
									<c:forEach items="#{dsDonVi}" var="item">
										<option value="${item.id}">
											<c:out value="${item.code}" /> -
											<c:out value="${item.name}" />
										</option>
									</c:forEach>
								</form:select>
							</div>
							<div class="Empty"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Dịch
								vụ</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="serviceInfos">
									<option value="">- Chọn -</option>
									<c:forEach items="#{serviceInfos}" var="item">
										<option value="${item.id}">
											<c:out value="${item.serviceName}" />
										</option>
									</c:forEach>
								</form:select>
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Địa
								chỉ IP</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="ipAddress" />
							</div>
							<div class="Empty"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								chính sách bảo mật</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="codeSecurity" />
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại
							</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="type">
									<option value="">- Chọn -</option>
									<form:option value="1">Địa chỉ IP</form:option>
									<form:option value="2">Dịch vụ</form:option>
									<form:option value="3">Chính sách bảo mật</form:option>
								</form:select>
							</div>

						</div>
						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="exportExcel();"
								value="Export" />
						</div>
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
			});

			function exportExcel() {
				var serviceInfos = $('#serviceInfos').val();
				var ipAddress = $('#ipAddress').val();
				var partner = $('#job_name').val();
				var codeSecurity = $('#codeSecurity').val();
				var type = $('#type').val();
				window.open($('#theForm').attr('action')
						+ '?method=exportExcel&partner=' + partner + '&serviceInfos=' + serviceInfos +
								'&ipAddress=' + ipAddress + '&codeSecurity=' + codeSecurity + '&type=' + type);
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>