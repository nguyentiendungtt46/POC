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
	<tiles:putAttribute name="title" value="Cấu hình validate báo cáo" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/configCfgDetail" var="formAction" />
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
								<h1>Cấu hình validate báo cáo</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								báo cáo</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="keyword_code" id="keyword_code"></form:select>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã lỗi</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="keyword_name" id="keyword_name"></form:input>
							</div>
						</div>

						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="findData();"
								value="Tìm kiếm" />
								<input class="btn blue" type="button" onclick="exportExcel();" value="Export" />
						</div>
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>


			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
		<div id="selectMapping" style="display: none;">
			<form:select class="hidden" path="dictTypes">
				<option value="">-Chọn-</option>
			<c:forEach items="#{listTypes}" var="item">
					<option value="${item.code}">
						<c:out value="${item.name}" />
					</option>
				</c:forEach>
		</form:select>
		<select class="hidden" id="selectConfigKey">
		</select>
		</div>
		<form:hidden path="reportCode" id="reportCode" />
			<div class="box-custom">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Cấu hình validate báo cáo</h1>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
						báo cáo<font color="red">*</font></div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<%-- <form:input path="rpReportCfgDetail.propertyCode" cssClass="required" title="Mã chỉ tiêu không được để trống"></form:input> --%>
						<select class="listRp"></select>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn vị
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="partnerId" cssClass="" >
							<option value="">-Chọn-</option>
							<c:forEach items="#{dsDonvi}" var="item">
								<option value="${item.id}">
									<c:out value="${item.code}" /> - <c:out value="${item.name}" />
								</option>
							</c:forEach>
						</form:select>
					</div>
				</div>
			</div>
			<div class="box-custom" id="div-table" style="display:none;">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Danh sách thuộc tính</h1>
					</div>
				</div>
				<div class="table-responsive" style="margin-top: -40px;" >
					<table class="table table-bordered" id="table-cfgDetail">
						<thead>
							<tr>
								<th width="200px">Mã thuộc tính</th>
								<th width="135px">Loại Validate</th>
								<th width="200px">Giá trị validate</th>
								<th width="100px">Mã lỗi</th>
								<th width="350px">Mô tả mã lỗi</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					<div align="right" style="text-align: right">
						<input type="button" id="table-product-customer_copy" value="Copy báo cáo"
							class="btn blue" onclick="copyReport();"
							style="font-size: 10px; height: auto; padding: 0px 8px; height: 22px !important; border-radius: 4px;"
							aria-invalid="false">
						<input type="button" id="table-product-customer_add" value="THÊM"
							class="btn blue" onclick="addRowDetails();"
							style="font-size: 10px; height: auto; padding: 0px 8px; height: 22px !important; border-radius: 4px;"
							aria-invalid="false">
						<input type="button"
							id="table-product-customer_del" value="Xóa" class="btn red" onclick="delRowDetails();"
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
			var strPropertyCode = "";
			var strTypeValid = "";
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
					"sTitle" : 'Loại Validate '
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Giá trị Validate'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã lỗi'
				} ];
			}
			
			function defaultValue(){
				if(tableObject != null){
					tableObject.deleteAllRow();
				}
			}
			
			$(document).ready(function () {
				loadDataReportCode();
				$('.btnDtDelete').hide();
				$('.btnDtAdd').text('C\u1EA5u h\u00ECnh');
			});
			var myJSON;
			
			function configValidate(reportCode){
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
				initCfgComboboxDetail(reportCode);
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
					initCfgComboboxDetail(this.value);
				}else{
					$(".data-table").css("display", "none");
				}
				
				});
			
			$(document.body).on("change","select.configKey",function(){
				var valueSelect = this.value;
				if ("CHECK_MAPPING" === valueSelect) {
					var nameValue = this.name.replace('configKey','configValue');
					changeCheckMapping(null,nameValue,null);
				}
				//changeCheckMapping(0);
			});
			
			function initCfgComboboxDetail(reportCode){
				$("#reportCode").val(reportCode);
				strPropertyCode = "";
				strTypeValid = "";
				$.ajax({
					url : $('#theForm').attr('action') + '?method=loadDataCfgDetail',
					data : {
						reportCode : reportCode
					},
					method : 'GET',
					success : function(_result) {
						if (_result != null) {
							try {
								myJSON = JSON.parse(_result);
							}catch(e) {
								alert(_result);
								return;
							}
							for ( var idx in myJSON.reportCode) {
								strPropertyCode += '<option value="' + myJSON.reportCode[idx].id + '">' + myJSON.reportCode[idx].propertyCode + '-' + myJSON.reportCode[idx].propertyDes + '</option>';
							}
							for ( var idx in myJSON.rpRule) {
								strTypeValid += '<option value="' + myJSON.rpRule[idx].code + '">' + myJSON.rpRule[idx].code + '</option>';
								$("#selectConfigKey").append('<option value="' + myJSON.rpRule[idx].code + '">' + myJSON.rpRule[idx].code + '</option>');
							}
 
							initDataTableCfg(myJSON.reportCode);
						}
					}
				});
			}
			
			function initDataTableCfg(result) {
				if(result != null){
					var idx = 0;
					var _row = '';
					if(result.length == 0){
						$("#table-product-customer_copy").css("display", "none");
					}else{
						$("#table-product-customer_copy").css("display", "initial");
					}
					var choose = '<option>-Chọn-</option>';
					for (var i = 0; i < result.length; i++) {
						if(result[i].reportCfgDetails.length > 0){
							for (var j = 0; j < result[i].reportCfgDetails.length; j++) {
								var strHidden = '<input type="hidden" class="form-control" name="rpReportCfgDetails[].id" value="' + result[i].reportCfgDetails[j].id + '"/>';
								_row 	+= '<tr>';
								_row 	+= '<td><div class="line-table" onclick="fillDataCombobox(this, \'propertyCode\');"><select class="propertyCode notnull"  name="rpReportCfgDetails[].reportCfgId.id"><option value="' + result[i].id + '">' + result[i].propertyCode + "-" + result[i].propertyDes +  '</option>	</select></div></td>';
								$("#selectConfigKey").find("option").removeAttr('selected');
								$('select#selectConfigKey option[value=' + result[i].reportCfgDetails[j].configKey + ']').attr('selected','selected');
								_row 	+= '<td><div class="line-table" ><select class="configKey notnull" name="rpReportCfgDetails[].configKey">' +$('#selectConfigKey').html()  +  '</select></div></td>';
								if ('CHECK_MAPPING' === result[i].reportCfgDetails[j].configKey) {
									//$('#dictTypes').val(result[i].reportCfgDetails[j].configValue).change();
									$('#dictTypes').find("option").removeAttr('selected');
									$('select#dictTypes option[value=' + result[i].reportCfgDetails[j].configValue + ']').attr('selected','selected');
								_row 	+= '<td><div class="line-table"><select class="notnull" name="rpReportCfgDetails[].configValue">' + $('#dictTypes').html() + '</select></div></td>';
								}else{
								_row 	+= '<td><div class="line-table"><input type="text" class="form-control" name="rpReportCfgDetails[].configValue" value=\'' + (result[i].reportCfgDetails[j].configValue == null ? '' : result[i].reportCfgDetails[j].configValue) +'\' /></div></td>';
								}
								_row	+= '<td><div class="line-table"><input type="text" class="form-control notnull" name="rpReportCfgDetails[].errCode" value="' + (result[i].reportCfgDetails[j].errCode == null ? '' : result[i].reportCfgDetails[j].errCode) +'" /></div></td>';
								_row 	+= '<td><div class="line-table"><input type="text" class="form-control notnull" name="rpReportCfgDetails[].errDesc" value="' + (result[i].reportCfgDetails[j].errDesc == null ? '' : result[i].reportCfgDetails[j].errDesc) +'" /></div>' + strHidden +'</td>';
								_row 	+= '</tr>';
							}
						}
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
					 $('#table-cfgDetail select').select2().on("select2-click", function(e) { log ("click");});
				}
				$("select").select2();
				$("#div-table").css("display", "block");
			}
			
			function changeCheckMapping(idx, name, value) {
				var nameValue = null;
				if (idx == null) {
					nameValue = name;
				} else {
					nameValue = 'rpReportCfgDetails[' + idx +'].configValue';
				}
				var divRemove = $('input[type="text"][name="'+nameValue+'"]').parent();
				$('input[type="text"][name="'+nameValue+'"]').remove();
				var select = $("<select>"+$('#dictTypes').html()+"</select>").attr("id", nameValue).attr("name", nameValue);
				divRemove.append(select);
				//load
				$('select[name="'+nameValue+'"]').val(value);
				$('select[name="'+nameValue+'"]').select2();
			}
			function initRowTableCfg() {
				var choose = '<option>-Chọn-</option>';
				var rowTemp = [];
				var strHidden = '<input type="hidden" class="form-control" name="rpReportCfgDetails[].id" />';
					//strHidden += '<input type="hidden" class="form-control" name="rpReportCfgMaster[].id" />';
				rowTemp = [
						'<div class="line-table" onclick="fillDataCombobox(this, \'propertyCode\');""><select class="propertyCode notnull"  name="rpReportCfgDetails[].reportCfgId.id">' + choose + strPropertyCode + '</select></div>',
						'<div class="line-table" ><select class="configKey notnull"  name="rpReportCfgDetails[].configKey">' + choose  + strTypeValid + '</select></div>',
						'<div class="line-table"><input type="text" class="form-control" name="rpReportCfgDetails[].configValue" /></div>',
						'<div class="line-table"><input type="text" class="form-control notnull" name="rpReportCfgDetails[].errCode" /></div>',
						'<div class="line-table"><input type="text" class="form-control notnull" name="rpReportCfgDetails[].errDesc" /></div>'
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
			
			function addRowDetails() {
				tableObject.addRow(tableObject.rowTemp);
				$("select").select2();
			}
			
			function delRowDetails(){
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
							if ("success" === _result) {
								alert("Copy th&#224;nh c&#244;ng");
								$("select.listRp").val($("#selectCopyReport").val());
								$("select.listRp").select2();
								if(tableObject != null) {
									tableObject.deleteAllRow();
									tableObject.dataTable.fnDestroy();
									$("#table-cfgDetail .checkAll").parent().remove();
								}
								initCfgComboboxDetail($("#selectCopyReport").val());
								$('#modal-view-report').modal('hide');
							} else {
								alert(_result);
							}
							
						}catch(e) {
							alert(_result);
							$("select.listRp").val($("#selectCopyReport").val());
							$("select.listRp").select2();
							initCfgComboboxDetail($("#selectCopyReport").val());
							$('#modal-view-report').modal('hide');
							return;
						}
					}
				});
				
			}
			
			function fillDataCombobox(val, code){
				var _select = $(val).find("select");
				var _valSelect = $(_select).val();	
				$(_select).select2('close');
				$(_select).children().remove(); 
				if(code == "propertyCode"){
					$(_select).append(strPropertyCode);
				/* }else if (code == "configKey111") {
					$(_select).append(strTypeValid); */
				}
				$(_select).val(_valSelect);
				$(_select).select2();
				$(_select).select2('open');
			}
			
			function exportExcel() {
				var reportCode = $('select[name="keyword_code"]').val();
				var errCode = $("#keyword_name").val()
				window.open($('#theForm').attr('action') + '?method=ExportFileExcel&reportCode='+reportCode+'&errCode='+errCode);
			}
			
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>