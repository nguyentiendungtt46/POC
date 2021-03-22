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
	<select id = "cbRule" style="display:none;">
		<option value="">- Chọn -</option>
		<c:forEach items="#{rules}" var="item">
			<option value="${item.code}">
				<c:out value="${item.value}" />
			</option>
		</c:forEach>
	</select>
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Cấu hình báo cáo" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/configCfgMaster" var="formAction" />
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
								<h1>Cấu hình báo cáo</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								báo cáo</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="keyword_code" id="keyword_code"></form:select>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã thuộc tính</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="keyword_name" id="keyword_name"></form:input>
							</div>
						</div>

						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm" />
							<input class="btn blue" type="button" onclick="exportExcel();" value="Export" />
						</div>
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>


			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="reportCode" id="reportCode" />
			<div class="box-custom">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Cấu hình báo cáo</h1>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
						báo cáo<font color="red">*</font></div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<%-- <form:input path="rpReportCfgMaster.propertyCode" cssClass="required" title="Mã chỉ tiêu không được để trống"></form:input> --%>
						<select class="listRp"></select>
					</div>
				</div>
			</div>
			<div class="box-custom data-table">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Danh sách thuộc tính</h1>
					</div>
				</div>
				<div class="table-responsive" style="margin-top: -40px;">
					<table class="table table-bordered" id="table-cfgDetail">
						<thead>
							<tr>
								<th width="200px">Mã thuộc tính</th>
								<th width="285px">Mô tả thuộc tính</th>
								<th width="120px">Độ dài thuộc tính</th>
								<th width="200px">Kiểu dữ liệu</th>
								<th width="50px">Position</th>
								<th width="80px">Position Excel</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					<div align="right" style="text-align: right">
						<input type="button" id="table-product-customer_add" value="THÊM"
							class="btn blue" onclick="addRowCfg();"
							style="font-size: 10px; height: auto; padding: 0px 8px; height: 22px !important; border-radius: 4px;"
							aria-invalid="false">
						<input type="button"
							id="table-product-customer_del" value="Xóa" class="btn red" onclick="delRowCfg();"
							style="font-size: 10px; height: auto; padding: 0px 8px; height: 22px !important; border-radius: 4px;">
					</div>
				</div>
			</div>
			
			<!-- Modal Bo sung dich vu -->
			<div class="modal fade" id="modal-view-report" role="dialog">
				<div class="modal-dialog">
					<!-- Modal content-->
					<div class="modal-content" >
						<div class="modal-header">
							
							<h6 class="modal-title" id="modal-title">Chọn báo cáo copy</h6>
								<button type="button" class="close" data-dismiss="modal" style="color: #fff;">&times;</button>
						</div>
						<div class="modal-body">
							<div class="form-group">
								<select id="selectCopyReport">
								</select>
							</div>
						</div>
							<div class="modal-footer">
								<button type="button" class="btn blue"
									onclick="fillDataReportCopy();">Chọn</button>
								<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
							</div>
						</div>
					</div>
				</div>
				
		</tiles:putAttribute>
	</form:form>
	
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			var createNew = false;
			var tableObject = null;
			function initParam(tblCfg) {
				tblCfg.bFilter = false;
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã báo cáo'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã thuộc tính'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mô tả thuộc tính'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'position'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'position excel'
				} ];
			}
			
			function defaultValue(){
				if (tableObject != null)
					tableObject.deleteAllRow();
			}
			
			$(document).ready(function () {
				$('.btnDtDelete').hide();
				$('.btnDtAdd').text('C\u1EA5u h\u00ECnh');
				loadDataReportCode();
				//$(".data-table").css("display", "none");
				/*var rowTempIp = initRowTableCfg();
				 tableObject = new TFOnline.DataTable({
					id : 'table-cfgDetail',
					jQueryUI : true,
					rowTemp : rowTempIp,
					hasCheck : true,
					maxRow : 100
				}); */
			});
			var myJSON;
			
			function configValidate(reportCode){
				//loadDataReportCode();

				$("select.listRp").val(reportCode);
				$("select.listRp").select2();
				$('#btnDel').css('display', 'none');
			     $('#divGrid').css('display', 'none');
				$('#divDetail').css('display', 'inline');
				if(tableObject != null) {
					tableObject.deleteAllRow();
					tableObject.dataTable.fnDestroy();
					$("#table-cfgDetail .checkAll").parent().remove();
				}
				initCfgDetail(reportCode);
			}
			
			$(document.body).on("change","select.listRp",function(){	
				if(this.value != ''){
					if(tableObject != null) {
						tableObject.deleteAllRow();
						tableObject.dataTable.fnDestroy();
						$("#table-cfgDetail .checkAll").parent().remove();
					}
					$('#divDetail').css('display', 'inline');
					$(".data-table").css("display", "block");
					initCfgDetail(this.value);
				}else{
					$(".data-table").css("display", "none");
				}
			});
			
			function initCfgDetail(reportCode){
				$("#reportCode").val(reportCode);
				$.ajax({
					url : $('#theForm').attr('action') + '?method=loadDataCfgDetail',
					data : {
						reportCode : reportCode
					},
					method : 'GET',
					success : function(_result) {
						if (_result != null) {
							myJSON = JSON.parse(_result);
							console.log(myJSON);
							initDataTableCfg(myJSON.reportCode);
						}
					}
				});
				
			}
			
			function initDataTableCfg(result) {
				console.log(result);
				if(result != null){
					var _row = '';
					if(result.length == 0){
						$("#table-product-customer_copy").css("display", "none");
					}else{
						$("#table-product-customer_copy").css("display", "initial");
					}
					for (var idx = 0; idx < result.length; idx++) {
						var strHidden = '<input type="hidden" class="form-control rpReportCfgMasterId" name="rpReportCfgMaster[].reportCode" />';
						strHidden += '<input type="hidden" class="form-control" name="rpReportCfgMaster[].id" value="' + result[idx].id + '"/>';
						_row 	+= '<tr>';
						_row 	+= '<td>' + strHidden +'<div class="line-table"><input type="text" class="form-control notnull" name="rpReportCfgMaster[].propertyCode" value="' + result[idx].propertyCode + '" /></div></td>';
						_row 	+= '<td><div class="line-table"><input type="text" class="form-control notnull" name="rpReportCfgMaster[].propertyDes" value="' + result[idx].propertyDes + '" /></div></td>';
						_row 	+= '<td><div class="line-table"><input type="text" class="form-control notnull" name="rpReportCfgMaster[].propertyLength" value="' + result[idx].propertyLength + '" /></div></td>';
						_row	+= '<td><div class="line-table"><select class="notnull" name="rpReportCfgMaster[].dataType">'  + $('#cbRule').html() + '</select></div></td>';
						_row 	+= '<td><div class="line-table"><input type="text" class="form-control notnull" name="rpReportCfgMaster[].position" value="' + result[idx].position + '" /></div></td>';
						_row 	+= '<td><div class="line-table"><input type="text" class="form-control" name="rpReportCfgMaster[].positionExcel" value="' + (result[idx].positionExcel == null ? "" : result[idx].positionExcel) + '" /></div></td>';
						_row 	+= '</tr>';
					}
					$("#table-cfgDetail tbody").append(_row);
					 var rowTempIp = initRowTableCfg();
					 tableObject = new TFOnline.DataTable({
						id : 'table-cfgDetail',
						jQueryUI : true,
						rowTemp : rowTempIp,
						hasCheck : true,
						addOveride:true,
	                    delOveride:true,
	                    addButton: 'tableAllAst_add',
	                    delButton: 'tableAllAst_del',
						maxRow : 1000
					}); 
					 for (var idx = 0; idx < result.length; idx++) {
						 $('select[name="rpReportCfgMaster[' + idx +'].dataType"]').val(result[idx].dataType);					 
					 }
					 $('#table-cfgDetail select').select2();
				}
			}
			function initRowTableCfg() {
				var rowTemp = [];
				var strHidden = '<input type="hidden" class="form-control rpReportCfgMasterId" name="rpReportCfgMaster[].reportCode" />';
					strHidden += '<input type="hidden" class="form-control" name="rpReportCfgMaster[].id" />';
				rowTemp = [
						'<div class="line-table"><input type="text" class="form-control notnull" name="rpReportCfgMaster[].propertyCode" /></div>',
						'<div class="line-table"><input type="text" class="form-control notnull" name="rpReportCfgMaster[].propertyDes" /></div>',
						'<div class="line-table"><input type="text" class="form-control notnull" name="rpReportCfgMaster[].propertyLength" /></div>',
						'<div class="line-table"><select class="notnull" name="rpReportCfgMaster[].dataType">'  + $('#cbRule').html() + '</select></div>',
						'<div class="line-table"><input type="text" class="form-control notnull" name="rpReportCfgMaster[].position" /></div>',
						'<div class="line-table"><input type="text" class="form-control" name="rpReportCfgMaster[].positionExcel" /></div>'
						 + strHidden];
				return rowTemp;
			}
			
			function beforeSave(){
				$("input.rpReportCfgMasterId").val($("select.listRp").val());
			}
			
			function loadDataReportCode(){
				$.ajax({
					url : $('#theForm').attr('action') + '?method=loadDataReportCode',
					data : {},
					method : 'GET',
					success : function(_result) {
						if (_result != null) {
							myJSON = JSON.parse(_result);
							$('.listRp, #selectCopyReport').append(
									$('<option>', {
										value : '',
										text : 'Chọn'
									}));
							$('#keyword_code').append(
									$('<option>', {
										value : '',
										text : 'Chọn'
									}));
							$.each(myJSON, function(i, item) {
								$('.listRp, #selectCopyReport').append(
										$('<option>', {
											value : item.code,
											text : item.code
										}));
							});
							if($('#keyword_code option').length == 1){
								$.each(myJSON, function(i, item) {
									$('#keyword_code').append(
											$('<option>', {
												value : item.code,
												text : item.code
											}));
								});
							}
							
						}
					}
				});
			}
			
			function addRowCfg(){
				tableObject.addRow(tableObject.rowTemp);
				$('#table-cfgDetail select').select2();
			}
			function delRowCfg(){
				tableObject._deleteRow();
			}
			
			function copyReport(){
				$('#modal-view-report').modal('show');
			}
			
			function fillDataReportCopy(){
				$.ajax({
					url : $('#theForm').attr('action') + '?method=copy',
					data : {
						reportCopy : $("select.listRp").val(),
						reportPaste:  $("#selectCopyReport").val()
					},
					method : 'GET',
					success : function(_result) {
						try {
							$.loader("close");
							JSON.parse(_result);
						}catch(e) {
							if(_result == "COPY_SUCCESS"){
								alert("Copy th\u00E0nh c\u00F4ng");
								$("select.listRp").val($("#selectCopyReport").val());
								$("select.listRp").select2();
								if(tableObject != null) {
									tableObject.deleteAllRow();
									tableObject.dataTable.fnDestroy();
									$("#table-cfgDetail .checkAll").parent().remove();
								}
								initCfgDetail($("#selectCopyReport").val());
								$('#modal-view-report').modal('hide');
							}else{
								alert(_result);
							}
							return;
						}
					}
				});
				
			}
			function exportExcel() {
				var reportCode = $('select[name="keyword_code"]').val();
				var reportValue = $('input[name="keyword_name"]').val();
				window.open($('#theForm').attr('action') + '?method=exportFileExcel&reportCode='+reportCode+'&reportValue='+reportValue);
			}
			
			/* funciton checkedDataType(val){
				var lstOption = strDataType.split("<option ");
				
				
			} */
			
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>