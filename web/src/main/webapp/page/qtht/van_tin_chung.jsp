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
%>

<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title"
		value="Call Service Vấn tin chung" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<%-- 	<form:form cssClass="form-horizontal" id="theForm" --%>
	<%-- 		enctype="multipart/form-data" modelAttribute="${commandName}" --%>
	<%-- 		method="post" action='${formAction}'> --%>
	<tiles:putAttribute name="catGrid">

		<div id="divGrid" align="left">
			<div class="row search-style">
				<div class="Table" id="divSearchInf">
					<div class="Row">
						<div class="row title-page" style="adding-bottom: 20px;">
							<h1>Call Service Vấn tin chung</h1>
						</div>
						<div class="label-static col-md-2 col-lg-2 col-sm-4 col-xs-4">Mã CIC<font color="red">*</font></div>
						<div class="col-md-4 col-lg-4 col-sm-8 col-xs-8">
							<input type="text" id="MaCIC" class="form-control required" maxlength="10" name="MaCIC" />
						</div>
					</div>
					
					<div class="divaction" align="center">
						<input class="btn blue" type="button" onclick="callServiceTHT();"
							value="Vấn tin" />
					</div>
					<spring:url var="sendTepHoiTinAction" value="/inquiry/s37"></spring:url>
					<div class="row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Kết
							quả</div>
						<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
							<textarea id="result" rows="30" style="height: auto"></textarea>
						</div>
					</div>
				</div>
			</div>
		</div>
	</tiles:putAttribute>

	<tiles:putAttribute name="catDetail" cascade="true">
		<div class="box-custom"></div>
	</tiles:putAttribute>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function callServiceTHT() {
				var formData = new FormData();
				formData.append("MaCIC", $('#MaCIC').val());
				
				formData.append("tokenIdKey", $('#tokenIdKey').val());
				formData.append("tokenId", $('#tokenId').val());
				formData.append("tokenGateWay", $('#tokenGateWay').val());
				var xhr = new XMLHttpRequest();
				xhr.open('POST', '${sendTepHoiTinAction}', true);
				$.loader({
					className : "blue-with-image-2"
				});
				xhr.onload = function() {
					$.loader("close");
					if (xhr.readyState == 4 && xhr.status == 200) {
						if (xhr.responseText == '') {
							alert('Thực hiện thành công!', function() {
							});
						} else {
							//alert(xhr.responseText)
							$('#result').val(xhr.responseText);
						}
					} else {
						alert('Import không thành công');
					}
				};
				xhr.send(formData);
			}
			function initParam(tblCfg){		
				tblCfg.bFilter = true;
	            tblCfg.bScrollX = true;
	            tblCfg.aoColumns = [];
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>