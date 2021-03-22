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
	<tiles:putAttribute name="title" value="Mã sản phẩm hỏi tin theo tệp" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/qnaFilePro" var="formAction" />
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
								<h1>Mã sản phẩm hỏi tin theo tệp</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="name" id="name"></form:input>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="code" id="code"></form:input>
							</div>
						</div>
						<div class="row">
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
			<form:hidden path="qnaFilePro.id" id="id" />
			<div class="box-custom">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Thông tin chi tiết</h1>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
						<font color="red">*</font></div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qnaFilePro.code"  cssClass="required" ></form:input>
					</div>
					
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên
						<font color="red">*</font></div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qnaFilePro.name"  cssClass="required" ></form:input>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qnaFilePro.description" ></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Tổ chức tín dụng<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="qnaFilePro.partner.id" cssClass="required"
							title="Tổ chức tín dụng">
							<option value=""> -Chọn-</option>
							<c:forEach items="#{listParent}" var="item">
								<option value="${item.id}">
									<c:out value="${item.code}" /> - <c:out value="${item.name}" />
								</option>
							</c:forEach>
						</form:select>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
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
					"sTitle" : 'Mã'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mô tả'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã TCTD'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên TCTD'
				} ];
			}

			$(document).on('click', '.btnDtAdd', function () {
				document.getElementById("qnaFilePro.code").disabled = false;
	        });
			
			function beforeEdit(res){
				document.getElementById("qnaFilePro.code").disabled = true;
			}
			
			function beforeSave(){
				document.getElementById("qnaFilePro.code").disabled = false;
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>