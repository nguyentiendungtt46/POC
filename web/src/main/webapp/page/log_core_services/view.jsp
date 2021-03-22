<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Log Service Core" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/logCoreServices" var="formAction" />
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
								<h1>Giám sát service Core</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian nhận từ ngày</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="fromDate" id="fromDate" cssClass="start_date" value="${startDate}"></form:input>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến ngày</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:input path="toDate" id="toDate" cssClass="start_date" value="${endDate}"></form:input>
							</div>
						</div>
						
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Service</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
			                	<form:select class="form-control" path="serviceId" title="Chọn service">
									<option value="">- Chọn -</option>
									<c:forEach items="#{LstService}" var="item">
										<option value="${item.id}">
											<c:out value="${item.serviceName}" />
										</option>
									</c:forEach>
								</form:select>
			                </div>
			                <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Người yêu cầu</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
			                	<form:select class="form-control" path="username" id="username" title="Chọn">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstUser}" var="item">
										<option value="${item.username}">
											<c:out value="${item.username}" />
										</option>
									</c:forEach>
								</form:select>
			                </div>
						</div>
						
						<div class="row">
			                <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trạng thái</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
			                	<form:select class="form-control" path="status" id="status" title="Chọn">
									<option value="">- Chọn -</option>
									<option value="-1">Thất bại</option>
									<option value="1">Thành công</option>
								</form:select>
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
			
			<div class="modal fade" id="myModal" role="dialog">
				<div class="modal-dialog">
					<!-- Modal content-->
					<div class="modal-content form-group">
						<div class="modal-header">
				          <h4 class="modal-title"></h4>
				        </div>
						<div class="modal-body question">
							<!-- <p>Some text in the modal.</p> -->
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Close</button>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<%-- <tiles:putAttribute name="catDetail" cascade="true">
		</tiles:putAttribute> --%>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg){	
				tblCfg.bFilter = false;
	            tblCfg.aoColumns = [			 
		            {"sClass": "left","bSortable" : false,"sTitle":'STT'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Tên Service'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Mã TCTD'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Ip'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Người yêu cầu'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Thời gian bắt đầu'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Thời gian kết thúc'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Thời gian kết nối (Giây)'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Trạng thái'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Thông báo'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Kích thước dữ liệu (Kb)'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Input'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Output'}
	            ];
			}
			$(document).ready(function() {
			     $(".btnDtAdd, .btnDtDelete").hide();
					$(".start_date").datepicker({
						dateFormat : 'dd/mm/yy',
						changeMonth : false,
						changeYear : false,
						showButtonPanel : false
					});
			});
			
			function inputDetail(id){
				$.ajax({
	                url:$('#theForm').attr('action') + '?method=loadInAndOut&id=' + id + '&type=in',
	                method: 'GET',
	                success: function(data){
	                	$(".question").empty();
	                	$(".question").append("<textarea class=\"form-control\" rows=\"20\">"+ data +"</textarea>");
	                	
	                	$(".modal-title").empty();
	                	$(".modal-title").append("Chi tiết input");
	                	
	                }
	            });
			}
			
			function outputDetail(id){
				$.ajax({
	                url:$('#theForm').attr('action') + '?method=loadInAndOut&id=' + id + '&type=out',
	                method: 'GET',
	                success: function(data){
	                	$(".question").empty();
	                	$(".question").append("<textarea class=\"form-control\" rows=\"20\">"+ data +"</textarea>");
	                	
	                	$(".modal-title").empty();
	                	$(".modal-title").append("Chi tiết output");
	                	
	                }
	            });
			}
			
			
			function exportExcel() {
				var fromDate = $('#fromDate').val();
				var toDate = $('#toDate').val();
				var serviceId = $('#serviceId').val();
				var username = $('#username').val();
				var status = $('#status').val();
				if (fromDate == null || fromDate == '' || toDate == null || toDate == '') {
					alert('Bắt buộc nhập từ ngày, đến ngày');
					return;
				}
				window.open($('#theForm').attr('action')
						+ '?method=exportExcel&serviceId=' + serviceId + '&fromDate=' + fromDate + '&toDate=' + toDate+ '&username=' + username + '&status=' + status);
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
	
	
