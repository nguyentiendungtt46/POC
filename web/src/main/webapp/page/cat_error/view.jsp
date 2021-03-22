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
		value="Danh mục lỗi" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/catError" var="formAction" />
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
							<h1>Danh mục lỗi</h1>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								lỗi</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="keyword_code" cssClass="form-control"></form:input>
							</div>
							<div class="Empty"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên
								lỗi</div>
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
			<form:hidden path="catError.id" id="id" />
			<form:hidden path="catError.enableReExec" id="enableReExec" />
			<form:hidden path="catError.enableResend" id="enableResend" />
			<div class="box-custom">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Thông tin chi tiết Danh mục lỗi</h1>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã lỗi<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catError.code" cssClass="required uppercase ascii"
							title="Mã không được để trống" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Tên lỗi<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catError.name" cssClass="required"
							title="Tên không được để trống" />
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Loại lỗi
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="catError.errorType">
							<form:option value="">Lỗi chung</form:option>
							<form:option value="1">Lỗi báo cáo</form:option>
							<form:option value="2">Lỗi hỏi tin</form:option>
						</form:select>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Nghiệp vụ lỗi
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="catError.reDo">
							<form:option value="1">Phải gửi lại báo cáo</form:option>
							<form:option value="0">Không phải gửi lại báo cáo</form:option>
							<form:option value="2">Để người sử dụng quyết định</form:option>
						</form:select>
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Chạy lại
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<%-- <form:checkbox path="catProduct.fixProduct" id="fixProduct" value="false" onclick="displayGird();"></form:checkbox> --%>
						<input type="checkbox" id="enableReExecCombobox" onclick="fillComboboxExec();"/>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Dùng lại tên
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<%-- <form:checkbox path="catProduct.fixProduct" id="fixProduct" value="false" onclick="displayGird();"></form:checkbox> --%>
						<input type="checkbox" id="enableResendCombobox" onclick="fillComboboxResend();"/>
					</div>
				</div>
				<div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mô tả lỗi
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catError.description" cssClass="required"
							title="mô tả không được để trống" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Lỗi phải dừng 
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:checkbox path="catError.stop"/>
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
					"sTitle" : 'Mã lỗi'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên lỗi'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mô tả'
				} ];
			}

			$(document).ready(function() {
				$(".btnDtDelete").hide();
			});
			
			$(document).on('click', '.btnDtAdd', function () {
				document.getElementById("catError.code").disabled = false;
	        });
			
			function beforeSave(){
				document.getElementById("catError.code").disabled = false;
			}
			
			function beforeEdit(res) {
				document.getElementById("catError.code").disabled = true;
				
				console.log(res);
				if(res.enableReExec == "1"){
					$("#enableReExecCombobox").prop("checked", "checked");
				}
				if(res.enableResend == "1"){
					$("#enableResendCombobox").prop("checked", "checked");
				}
			}
			
			function fillComboboxExec(){
				var checkBox = document.getElementById("enableReExecCombobox");
				if (checkBox.checked == true) {
					$("#enableReExec").val("1");
				} else {
					$("#enableReExec").val("0");
				}
			}
			
			function fillComboboxResend(){
				var checkBox = document.getElementById("enableResendCombobox");
				if (checkBox.checked == true) {
					$("#enableResend").val("1");
				} else {
					$("#enableResend").val("0");
				}
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>