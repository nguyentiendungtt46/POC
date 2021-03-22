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
	<tiles:putAttribute name="title" value="Trạng thái kết nối" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/logCheckConnect" var="formAction" />
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
								<h1>Trạng thái kết nối</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tổ chức tín dụng</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="parentId">
								<option value=""> -Chọn-</option>
								<c:forEach items="#{listParent}" var="item">
									<option value="${item.id}">
										<c:out value="${item.code}" /> - <c:out value="${item.name}" />
									</option>
								</c:forEach>
								</form:select>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trạng thái</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<select class="form-control" name="status" id="status">
									<option value=""> -Chọn- </option>
									<option value="0">Không thông</option>
									<option value="1">Thông</option>
								</select>
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Địa chỉ IP</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="ipAddres" id="ipAddres"></form:input>
							</div>
						</div>

						<!-- <div align="center" class="HeaderText">&#8203;</div> -->
						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="findData();"
								value="Tìm kiếm" />
								<input class="btn blue" type="button" onclick="exportExcel();"
								value="Export" />
						</div>
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>


			</div>
		</tiles:putAttribute>

		<%-- <tiles:putAttribute name="catDetail" cascade="true">
		</tiles:putAttribute> --%>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			var createNew = false;
			var tableIp, tableAccount = null;
			function initParam(tblCfg) {
				tblCfg.bFilter = false;
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã TCTD'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên TCTD'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Địa chỉ IP'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Trạng thái'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Thời điểm kiểm tra'
				} ];
			}
			
			$(document).ready(function() {
				$(".btnDtAdd").hide();
			});
			
			function exportExcel(){
				var partnerId =  $('#parentId').val();
				var status = $('#status').val();
				var ipAddres = $('#ipAddres').val();;
				window.open($('#theForm').attr('action') + '?method=exportFileExcel&partnerId=' +partnerId+ '&status=' +status+ '&ipAddress='+ipAddres);
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>