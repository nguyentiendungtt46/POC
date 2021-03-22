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
	<tiles:putAttribute name="title" value="Giám sát gửi email" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/h2hEmail" var="formAction" />
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
								<h1>Giám sát gửi email</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <select class="form-control" name="type" id="type">
									<option value="">- Chọn -</option>
									<option value="0">Tự động</option>
									<option value="1">Thủ công</option>
								</select>
		                    </div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trạng thái</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <select class="form-control" name="status" id="status">
									<option value="">- Chọn -</option>
									<option value="0">Chưa gửi</option>
									<option value="1">Đã gửi</option>
									<option value="2">Thật bại</option>
								</select>
		                    </div>
						</div>
						
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian gửi từ</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="formDate" name="formDate" cssClass="currentDate"></form:input>
		                    </div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian gửi đến</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <form:input path="toDate" name="toDate" cssClass="currentDate"></form:input>
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
							
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Email</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="email" id="email"></form:input>
							</div>
						</div>
						
						<!-- <div align="center" class="HeaderText">&#8203;</div> -->
						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="findData();"
								value="Tìm kiếm" />
							<a class="btn blue" href="/cic/sendEmailPartner">Gửi email</a>
							<input class="btn blue" type="button" onclick="exportExcel();"
								value="Export" />
						</div>
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>


			</div>
			<div class="modal fade" id="myModal" role="dialog">
				<div class="modal-dialog">
					<!-- Modal content-->
					<div class="modal-content form-group" style="width: 900px;margin-left: -200px;">
						<div class="modal-header">
				          <h4 class="modal-title"></h4>
				        </div>
						<div class="modal-body question">
							<!-- <p>Some text in the modal.</p> -->
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Đóng</button>
						</div>
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
					"sTitle" : 'Mã TCTD'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên TCTD'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tiêu đề email'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Email đến'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Loại'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Trạng thái'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Thời gian gửi'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Nội dung Email'
				} ];
			}
			
			$(document).ready(function() {
				$(".btnDtAdd").hide();
				
				$(".currentDate").datepicker({
					dateFormat : 'dd/mm/yy',
					changeMonth : false,
					changeYear : false,
					showButtonPanel : false
				});
			});
			
			function getCkEditor(id){
				CKEDITOR.replace(id, {} );
			}
			
			function chiTietEmail(id){
				$.ajax({
	                url:$('#theForm').attr('action') + '?method=loadEmailBody&id=' + id,
	                method: 'GET',
	                success: function(data){
	                	$(".question").empty();
	                	$(".question").append("<textarea class=\"form-control\" rows=\"20\" id=\"content-email\">"+ data +"</textarea>");
	                	getCkEditor('content-email');
	                	$(".modal-title").empty();
	                	$(".modal-title").append("Nội dung email");
	                }
	            });
			}
			function exportExcel(){
				var type =  $('#type').val();
				var status = $('#status').val();
				var formDate = $('#formDate').val();
				var toDate = $('#toDate').val();
				var partnerId = $('#parentId').val();
				var email = $('#email').val();
				window.open($('#theForm').attr('action') + '?method=exportFileExcel&partnerId=' +partnerId+ '&type=' +type+ '&status='+status+
						'&formDate='+formDate+'&toDate='+toDate+'&email='+email);
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>