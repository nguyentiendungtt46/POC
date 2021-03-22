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
	<tiles:putAttribute name="title" value="Quản lý đơn vị thành viên" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/partner" var="formAction" />
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
								<h1>Quản lý đơn vị thành viên</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								đơn vị</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="keyword_code" id="keyword_code"></form:input>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên
								đơn vị</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="keyword_name" id="keyword_name"></form:input>
							</div>
						</div>
						
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày hiệu lực từ</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="formDate" name="formDate" cssClass="currentDate"></form:input>
		                    </div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày hiệu lực Đến</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <form:input path="toDate" name="toDate" cssClass="currentDate"></form:input>
		                    </div>
						</div>


						<!-- <div align="center" class="HeaderText">&#8203;</div> -->
						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="findData();"
								value="Tìm kiếm" />
							<input class="btn blue" type="button" onclick="exportExcel();" value="Export"/>
							<%-- Scripting elements ( &lt;%!, &lt;jsp:declaration, &lt;%=, &lt;jsp:expression, &lt;%, &lt;jsp:scriptlet ) are disallowed here.
                            
                            if(LDAP_AUTHEN == null || !"true".equals(LDAP_AUTHEN.getValue())){--%>
							<!-- <input type="button" onclick="addNew()" value="Thêm mới"
								class="btn blue" /> -->
							<%--} 
                        --%>



						</div>
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
				

			</div>
		</tiles:putAttribute>
		
		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="partner.id" id="id"/>
			<div style="display: none">
				<form:select style="display:none" id="lstSvrs" path="partner.serviceInfos">
				<option value=""></option>
				<c:forEach items="#{serviceInfos}" var="item">
					<option value="${item.id}">
						<c:out value="${item.serviceName}" />
					</option>
				</c:forEach>
			</form:select>
			</div>
			
			<div class="box-custom">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Thông tin chi tiết đơn vị thành viên</h1>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
						đơn vị<font color="red">*</font></div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="partner.code"  cssClass="partnerCode required uppercase ascii" ></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tỉnh/Thành
						phố</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="partner.provincecode" cssClass=" provincecode" title="Chọn một Tỉnh/thành phố">
							<form:option value="">- Chọn -</form:option>
								<c:forEach items="#{lstProvince}" var="item">
									<form:option value="${item.id}">
										<c:out value="${item.value}" /> -
										<c:out value="${item.description}" />
									</form:option>
								</c:forEach>
						</form:select>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên
						đơn vị<font color="red">*</font></div>
					<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
						<form:input path="partner.name" cssClass="required" title="Tên đơn vị không được để trống"></form:input>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên
						viết tắt</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="partner.sortName"></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Email</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="partner.email" cssClass="email" title="Email không hợp lệ" ></form:input>
					</div>
				</div>
				
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Hiệu lực HĐ từ
						<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="partner.validityContractFromStr" cssClass="fromDate date required" ></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Hiệu lực HĐ đến
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="partner.validityContractToStr" cssClass="toDate date"></form:input>
					</div>
				</div>
				
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Địa
						chỉ<font color="red">*</font></div>
					<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
						<form:input path="partner.address" cssClass="required" title="Địa chỉ không được để trống"></form:input>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số
						điện thoại</div>
					<div class="col-md-6 col-lg-6 col-sm-12 col-xs-12">
						<form:textarea path="partner.phone"></form:textarea>
					</div>
					<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
						<input type="button" class="btn blue" value="Kiểm tra" onclick="checkPhone();" style="margin: auto;" data-toggle="modal" data-target="#myModal"/>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ghi
						chú</div>
					<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
						<form:textarea path="partner.note" rows="3"></form:textarea>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Lý do hủy kết nối</div>
					<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
						<form:textarea id="reasonUnconnett" path="partner.reasonUnconnett" ></form:textarea>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Có gửi email cảnh báo</div>
	                <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	                	<form:checkbox path="partner.sendEmail" value="false"/>
	                </div>
	                <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
	                <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	                	<input type="button" id="newFtp" value="Tạo folder FTP"
							class="btn blue" onclick="createFTP();"
							style="font-size: 10px; height: auto; padding: 0px 8px; margin: 0px; border-radius: 4px;"
							aria-invalid="false">
	                </div>
				</div>
				<div class="row">
					 <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số lượng user tối đa</div>
	                <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	                	<form:input path="partner.maxUser" class="number" maxVal="999" />
	                </div>
				</div>
			</div>
			
			<div class="box-custom row">
				<div class="col-md-12">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Danh sách IP kết nối</h1>
					</div>
					<div class="table-responsive" >
						<table class="table table-bordered" id="table-partner-ip">
							<thead>
								<tr>
									<th width="850px">Địa chỉ IP</th>
									<th width="100px">Hành động</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
				<!-- <div class="col-md-6">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Danh sách tài khoản kết nối</h1>
					</div>
					<div class="table-responsive" >
						<table class="table table-bordered" id="table-partner-account">
							<thead>
								<tr>
									<th width="450px">Username</th>
									<th width="220px">Password</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
						<div align="right" style="text-align: right">
						<input type="button" id="table-product-customer_add" value="THÊM"
							class="btn blue" onclick="addRowAcc();"
							style="font-size: 10px; height: auto; padding: 0px 8px; height: 22px !important; border-radius: 4px;"
							aria-invalid="false">
						<input type="button"
							id="table-product-customer_del" value="Xóa" class="btn red" onclick="delRowAcc();"
							style="font-size: 10px; height: auto; padding: 0px 8px; height: 22px !important; border-radius: 4px;">
					</div>
				</div>
			</div> -->
			
			<div class="col-md-12">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Danh sách duy trì sử dụng sản phẩm theo Service của đối tác</h1>
				</div>
				<div class="table-responsive" >
					<table class="table table-bordered" id="table-service-product">
						<thead>
							<tr>
								<th width="180px">Service</th>
								<th width="180px">Sản phẩm</th>
								<th width="180px">Tần suất</th>
								<th width="180px">Chu kì</th>
								<th width="180px">Disable</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					<div align="right" style="text-align: right">
					<input type="button" id="table-product-customer_addAll" value="Add all"
						class="btn blue" onclick="addAllRowCustomer();"
						style="font-size: 10px; height: auto; padding: 0px 8px; height: 22px !important; border-radius: 4px;"
						aria-invalid="false">
					<input type="button" id="table-product-customer_add" value="THÊM"
						class="btn blue" onclick="addRowCustomer();"
						style="font-size: 10px; height: auto; padding: 0px 8px; height: 22px !important; border-radius: 4px;"
						aria-invalid="false">
					<input type="button"
						id="table-product-customer_del" value="Xóa" class="btn red" onclick="delRowCustomer();"
						style="font-size: 10px; height: auto; padding: 0px 8px; height: 22px !important; border-radius: 4px;">
				</div>
				</div>
			</div>
			<div class="Row">
				<div class="easyui-panel" style="padding: 5px">
					<ul id="danhSachQuyen" class="easyui-tree"
						data-options="animate:true,checkbox:true,cascadeCheck:true,lines:true"></ul>
				</div>
			</div>
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
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			var createNew = false;
			var tableIp, tableAccount, tableSerProduct = null;
			function initParam(tblCfg) {
				tblCfg.bFilter = false;
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã đơn vị'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên đơn vị'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Địa chỉ'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Hiệu lực hợp đồng từ'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Hiệu lực hợp đồng đến'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : ''
				} ];
			}
			
			function checkPhone(){
				var id = $('#id').val();
				var phone = $('textarea[name="partner.phone"]').val();
				$(".question").empty();
				$(".modal-title").empty();
				$(".modal-title").append("Kiểm tra số điện thoại");
				
				if(phone.length == 0){
                	$(".question").append("<p> <font color=\"red\">Chưa nhập số điện thoại!</font></p>");
				}else{
					$.ajax({
		                url:$('#theForm').attr('action') + '?method=checkPhone&phone=' + phone + "&id=" + id,
		                method: 'GET',
		                success: function(data){
		                	if(data.length == 0){
		                		$(".question").append("<p>Số điện thoại đúng định dạng.</p>");
		                	}else{
		                		$.each(data, function(i, item) {
				                	$(".question").append("<p> SĐT: "+ item.phone +", "+ item.msg +".</p>");
								});
		                	}
		                }
		            });
				}
			}
			
			
			function pingIpAddress(obj){
				var ipAddress = $('input[name="'+ obj +'"]').val();
				if(ipAddress.length == 0){
					alert("Chưa nhập địa chỉ IP!");
				}else if(!/^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/.test(ipAddress)){
					alert("Địa chỉ IP không đúng định dạng!");
				}else{
					$.ajax({
		                url:$('#theForm').attr('action') + '?method=pingIp&ipAddress=' + ipAddress,
		                method: 'GET',
		                success: function(data){
		                	alert(data);
		                }
		            });
				}
			}
			
			$(document).on('click', '.btnDtAdd', function () {
				document.getElementById("partner.code").disabled = false;
	        });
			
			function beforeEdit(res){
				document.getElementById("partner.code").disabled = true;
				$('.partnerCode').val(res.code)
			}
			
			function beforeSave(){
				document.getElementById("partner.code").disabled = false;
			}
			
			$(document).ready(function () {
				$('.btnDtDelete').hide();
				//loadProvinceCbox();
				initTableIP();
				
				$(".currentDate").datepicker({
					dateFormat : 'dd/mm/yy',
					changeMonth : false,
					changeYear : false,
					showButtonPanel : false,
				});
			});
			
			/* function getOptionStrServiceInfo() {
				var optionStr = "";
				$('.serviceInfos').find('option').each(function() {
					optionStr=optionStr+'<option value='+$(this).val()+'>' + $(this).text() + '<option>';
				});
			} */
			
			function loadProvinceCbox(){
				$.ajax({
					url : $('#theForm').attr('action')
							+ '?method=getProvince',
					data : {cifno :""},
					method : 'GET',
					success : function(_result, status, xhr) {
						$.each(_result, function(i, item) {
							$('.provincecode').append(
									$('<option>', {
										value : item.id,
										text : item.name
									}));
						});
					}
				});
			}
			
			function beforeValidate() {
				validateDate();
			}
			
			function validateDate() {
				$('#partnerDateError').remove();
				var fromDate = new Date(Date.parse($('.fromDate').val().replace(/([0-9]+)\/([0-9]+)/,'$2/$1')));
				var toDate = new Date(Date.parse($('.toDate').val().replace(/([0-9]+)\/([0-9]+)/,'$2/$1')));
				
				if(toDate < fromDate){
					var parent = $('.toDate').parent();
					var lable = $("<lable id='partnerDateError' class='error'>(Hiệu lực HĐ đến) không nhỏ hơn (Hiệu lực HĐ từ)</lable>");
					parent.append(lable);
					return;
				}
			}
			
			function validateCode() {
				$('#partnerCodeError').remove();
				var value = $('.partnerCode').val().replace(',','');
				if (value.length > 8) {
					var parent = $('.partnerCode').parent();
					var lable = $("<lable id='partnerCodeError' style='color:red'>Mã không quá tám kí tự số</lable>");
					parent.append(lable);
					$('.partnerCode').val(value);
					return;
				}
				
			}
			
			function loadServiceInfoCbox(nameInput){
				$.ajax({
					url : $('#theForm').attr('action')
							+ '?method=getServiceInfo',
					data : {cifno :""},
					method : 'GET',
					success : function(_result, status, xhr) {
						$.each(_result, function(i, item) {
							$('.serviceInfos').append(
									$('<option>', {
										value : item.id,
										text : item.serviceName
									}));
						});
					}
				});
			}
			
			function initTableIP(){
				var rowTempIp = initRowTableIp();
				var tableObject = new TFOnline.DataTable({
					id : 'table-partner-ip',
					jQueryUI : true,
					rowTemp : rowTempIp,
					hasCheck : true,
					delCaption : 'Xóa',
					addOveride:true,
                    delOveride:true,
					maxRow : 100 
				});
				tableIp = tableObject;
				/* var rowTempAccount = initRowTableAccount();
				var tableObject = new TFOnline.DataTable({
					id : 'table-partner-account',
					jQueryUI : true,
					rowTemp : rowTempAccount,
					hasCheck : true,
					addOveride:true,
                    delOveride:true,
                    addButton: 'tableAllAst_add',
                    delButton: 'tableAllAst_del',
					maxRow : 100 
				});
				tableAccount = tableObject; */
				
				var rowTempServiceProduct = initRowTableSerProduct();
				var tableObject = new TFOnline.DataTable({
					id : 'table-service-product',
					jQueryUI : true,
					rowTemp : rowTempServiceProduct,
					hasCheck : true,
					addOveride:true,
                    delOveride:true,
                    addButton: 'tableAllAst_add',
                    delButton: 'tableAllAst_del',
					maxRow : 100 
				});
				tableSerProduct = tableObject;
				
			}
			
			function initRowTableIp() {
				var rowTemp = [];
				var listObject = "partner";
				var strHidden  = '<input type="hidden" class="form-control" name="' + listObject + '.partnerConnectIpArrayList[].id" />';
					strHidden += '<input type="hidden" class="form-control" name="' + listObject + '.partnerConnectIpArrayList[].partnerId" />';
					rowTemp = [
							'<div class="line-table"><input type="text" class="form-control addressIp" name="' + listObject + '.partnerConnectIpArrayList[].ipAddress"  maxlength="40"/></div>',
							'<div class="line-table"><input type="button" class="btn blue" value="PING" name="' + listObject + '.partnerConnectIpArrayList[].ipAddress" onclick="pingIpAddress(this.name);"/></div>'
							+ strHidden
							];
				return rowTemp;
			}
			
			/* function initRowTableAccount() {
				var rowTemp = [];
				var listObject = "partner";
				var strHidden  = '<input type="hidden" class="form-control" name="' + listObject + '.partnerConnectAccountArrayList[].id" />';
					strHidden += '<input type="hidden" class="form-control" name="' + listObject + '.partnerConnectAccountArrayList[].partnerId" />';
					rowTemp = [
							'<div class="line-table"><select class="form-control sysUsers" name="' + listObject + '.partnerConnectAccountArrayList[].sysUserId.id" /></div>'
							+ strHidden
							];
				return rowTemp;
			} */
			
			function initRowTableSerProduct() {
				var rowTemp = [];
				var listObject = "partner";
				var strHidden  = '<input type="hidden" class="form-control" name="' + listObject + '.partnerServicesArrayList[].id" />';
					strHidden += '<input type="hidden" class="form-control" name="' + listObject + '.partnerServicesArrayList[].partnerId" />';
					rowTemp = [
							'<div class="line-table"><select onChange="loadProduct(this,null,null,null);" class="form-control serviceInfoId" name="' + listObject + '.partnerServicesArrayList[].serviceInfoId.id">' + $('#lstSvrs').html()+ '</></div>',
							'<div class="line-table"><select class="form-control catProductId" name="' + listObject + '.partnerServicesArrayList[].catProductId.id" /></div>',
							'<div class="line-table number"><input type="text" class="form-control number" name="' + listObject + '.partnerServicesArrayList[].rate" /></div>',
							'<div class="line-table"><select class="form-control period" name="' + listObject + '.partnerServicesArrayList[].period" ><option value="1">Giây</option><option value="2">Phút</option><option value="3">Giờ</option><option value="4">ngày</option><option value="5">Tháng</option></select></div>',
							'<div class="line-table"><input type="checkbox" class="" name="' + listObject + '.partnerServicesArrayList[].disableStatus" /></div>'
							+ strHidden
							];
					
					
				return rowTemp;
			}
			
			function addRowCustomer() {
				tableSerProduct.addRow(tableSerProduct.rowTemp);
				loadServiceInfoCbox('.serviceInfoId');
				$(".serviceInfoId").select2();
				$(".catProductId").select2();
				$(".period").select2();
			}
			
			/* function addRowAcc() {
				tableAccount.addRow(tableAccount.rowTemp);
				var index = tableAccount.rowTemp.length - 2;
				var id = $('#id').val();
				loadLstSysUser(index,null,id);
				/* loadSysUserCbox(id); 
			}*/
			/*function delRowAcc(){
				tableAccount._deleteRow();
			} */
			
			function validateIp(ipAddress) {
				var value = $(ipAddress).val();
				var name = $(ipAddress).attr("name")+"Error";
				name = name.replace('partner.partnerConnectIpArrayList[','');
				name = name.replace('].','');
				$('.'+name).remove();
				var check = value.replace(/[^0-9.]/g, '');
				if (value != check) {
					var parent = $(ipAddress).parent();
					var lableError = $("<lable class='"+name+"' style='color:red;'>Không được nhập số</lable>");
					parent.append(lableError);
				} else {
					$('.'+name).remove();
				}
			}
			
			function validateNumber(rate) {
				var value = $(rate).val();
				var name = $(rate).attr("name")+"Error";
				name = name.replace('partner.partnerServicesArrayList[','');
				name = name.replace('].rate','');
				$('.'+name).remove();
				var check = value.replace(/[^0-9]/g, '');
				if (value != check) {
					var parent = $(rate).parent();
					var lableError = $("<lable class='"+name+"' style='color:red;'>Không được nhập số</lable>");
					parent.append(lableError);
				} else {
					$('.'+name).remove();
				}
				
			}
			
			/* function loadLstSysUser(rowIndex,connectAccId,partnerId){
				$.ajax({
					url : $('#theForm').attr('action')
							+ '?method=getUserByPartnerId&partnerId='+partnerId,
					data : {cifno :""},
					method : 'GET',
					success : function(_result, status, xhr) {
						$.each(_result, function(i, item) {
							$('select[name="partner.partnerConnectAccountArrayList['+rowIndex+'].sysUserId.id"]').append(
									$('<option>', {
										value : item.id,
										text : item.username
									}));
							
						});
						if(connectAccId != null){
							$('select[name="partner.partnerConnectAccountArrayList['+rowIndex+'].sysUserId.id"]').val(connectAccId);
							$('select[name="partner.partnerConnectAccountArrayList['+rowIndex+'].sysUserId.id"]').select2();
						}
					}
				});
				
			} */
			
			function loadSysUserCbox(partnerId){
				$.ajax({
					url : $('#theForm').attr('action')
							+ '?method=getUserByPartnerId&partnerId='+partnerId,
					data : {cifno :""},
					method : 'GET',
					success : function(_result, status, xhr) {
						$.each(_result, function(i, item) {
							$('.sysUsers').append(
									$('<option>', {
										value : item.id,
										text : item.username
									}));
						});
					}
				});
			}
			
			function loadProduct(index,rowIndex,rowValue,catProductId) {
				var serviceName;
				var serviceValue;
				var productName ;
				if (index != null) {
					serviceName = $(index).attr("name");
					serviceValue = $(index).val();
					productName = serviceName.replace("serviceInfoId.id", "catProductId.id");
				} else {
					productName = "partner.partnerServicesArrayList["+rowIndex+"].catProductId.id";
					serviceValue = rowValue;
				}
				
				$.ajax({
					url : $('#theForm').attr('action')
							+ '?method=getProductByService&serviceId='+serviceValue,
					data : {cifno :""},
					method : 'GET',
					success : function(_result, status, xhr) {
						$('select[name="'+productName+'"]').empty();
						$('select[name="'+productName+'"]').append(
								$('<option>', {
									value : "",
									text : ""
								}));
						$.each(_result, function(i, item) {
							$('select[name="'+productName+'"]').append(
									$('<option>', {
										value : item.id,
										text : item.code + '-' + item.name
									}));
						});
						
						// Set 
						if (catProductId != null) {
							$('select[name="'+productName+'"]').val(catProductId);
						}
						if (rowValue != null) {
							$('select[name="partner.partnerServicesArrayList['+rowIndex+'].serviceInfoId.id"]').val(rowValue);
						}
						$('select[name="'+productName+'"]').select2();
						$('select[name="partner.partnerServicesArrayList['+rowIndex+'].serviceInfoId.id').select2();
						$(".period").select2();
					}
				});
			}
			
			function delRowCustomer(){
				tableSerProduct._deleteRow();
				//instancReindexDelete(tableSerProduct);
			}
			
			function editPartner(id) {
	            $.loader( {
	                className : "blue-with-image-2"
	            });
	            clearDiv('divDetail');
	            var tokenIdKey = $('#tokenIdKey').val();
	            var tokenId = $('#tokenId').val();
	            $.getJSON(editUrl, 
	            {
	                "id" : id, "tokenIdKey" : tokenIdKey, "tokenId" : tokenId
	            }).done(function (res) {
	            	initDataEdit(res);
	                $.loader('close');

	            });
	        }
			function afterEdit1(){
				//for()
					//partner.partnerServicesArrayList[0].catProductId.id
			}
			
			// fill data to table partner connect ip, username, password
			function afterEdit(id, res){ 
				tableIp.deleteAllRow();
				//tableAccount.deleteAllRow();
				tableSerProduct.deleteAllRow();
				var _partConnectIp = res.partnerConnectIpArrayList;
				if(_partConnectIp != null ){
					for (var i = 0; i < _partConnectIp.length; i++) {
						tableIp.addRow(tableIp.rowTemp);
							$('input[type="hidden"][name="partner.partnerConnectIpArrayList['+ i +'].id"]').val(_partConnectIp[i].id);
							$('input[type="hidden"][name="partner.partnerConnectIpArrayList['+ i +'].partnerId"]').val(res.id);
							$('input[type="text"][name="partner.partnerConnectIpArrayList['+ i +'].ipAddress"]').val(_partConnectIp[i].ipAddress);
					}
				}
				/* var _partConnectAccount = res.partnerConnectAccountArrayList;
				var _sysUser = res.sysUsersArrayList;
				if(_partConnectAccount != null ){
					for (var i = 0; i < _partConnectAccount.length; i++) {
						tableAccount.addRow(tableAccount.rowTemp);
						var partnerId = null;
						var connectAccId = null;
						if (_sysUser[i].companyId != null) {
							partnerId = _sysUser[i].companyId;
						}
						if (_partConnectAccount[i].id != null) {
							connectAccId = _partConnectAccount[i].sysUserId.id;
						}
						loadLstSysUser(i,connectAccId,partnerId);
						
						$('input[type="hidden"][name="partner.partnerConnectAccountArrayList['+ i +'].id"]').val(_partConnectAccount[i].id);
						$('input[type="hidden"][name="partner.partnerConnectAccountArrayList['+ i +'].partnerId"]').val(res.id);
						/* $('select[name="partner.sysUsersArrayList['+ i +'].id"]').val(_sysUser[i].id).attr('selected','selected'); */
						/* $('input[type="text"][name="partner.partnerConnectAccountArrayList['+ i +'].username"]').val(_partConnectAccount[i].username);
						$('input[type="password"][name="partner.partnerConnectAccountArrayList['+ i +'].password"]').val(_partConnectAccount[i].password); */
			/*		}
				} */
				
				var _partnerServicesArrayList = res.partnerServicesArrayList;
				if (_partnerServicesArrayList != null) {
					for (var i = 0; i < _partnerServicesArrayList.length; i++) {
						tableSerProduct.addRow(tableSerProduct.rowTemp);
						//$('select[name="partner.partnerServicesArrayList['+i+'].serviceInfoId.id"]').change();
						var catProductId = null;
						var serviceId = null;
						if (_partnerServicesArrayList[i].catProductId != null) {
							catProductId = _partnerServicesArrayList[i].catProductId.id;
						}
						if (_partnerServicesArrayList[i].serviceInfoId != null)
							serviceId = _partnerServicesArrayList[i].serviceInfoId.id;
						loadProduct(null,i,serviceId,catProductId);
						
						$('input[type="hidden"][name="partner.partnerServicesArrayList['+ i +'].id"]').val(_partnerServicesArrayList[i].id);
						$('input[type="hidden"][name="partner.partnerServicesArrayList['+ i +'].partnerId"]').val(res.id);
						/*$('select[name="partner.partnerServicesArrayList"]['+ i +'].serviceInfoId.id').val(_partnerServicesArrayList[i].serviceInfoId.id).trigger('change');;
						$('select[name="partner.partnerServicesArrayList"]['+ i +'].catProductId.id option[value='+_partnerServicesArrayList[i].catProductId.id+']').attr('selected','selected');*/
						$('input[type="text"][name="partner.partnerServicesArrayList['+ i +'].rate"]').val(_partnerServicesArrayList[i].rate);
						$('select[name="partner.partnerServicesArrayList['+ i +'].period"]').val(_partnerServicesArrayList[i].period).trigger('change');
						if (_partnerServicesArrayList[i].disableStatus) {
							$('input[type="checkbox"][name="partner.partnerServicesArrayList['+ i +'].disableStatus"]').prop("checked", true);
						} else {
							$('input[type="checkbox"][name="partner.partnerServicesArrayList['+ i +'].disableStatus"]').prop("checked", false);
						}
		
					}
				}
			}
			
			function exportExcel(){
				var code =  $('#keyword_code').val();
				var name = $('#keyword_name').val();
				window.open($('#theForm').attr('action') + '?method=exportFileExcel&code=' +code+ '&name=' +name);
			}
			
			/* function validateServiceProduct() {
				$('.serviceInfoId').each(function (i) {
					var service  = $(this).val();
					var count = 0;
					var lstCheck = [];
					$('.catProductId').each(function (j) {
						lstCheck.push(service + $(this).val());
					});
					var index = 0;
					for (var a = 0 ; a < lstCheck.length; a ++) {
						for (var c = a+1; c < lstCheck.length; c++) {
							if (lstCheck[a] === lstCheck[c]) {
								count++;
								index  = c;
							}
						}
						if (count > 0) {
							alert("dịch vụ sản phẩm bị trùng : " + index);
						}
					}
				});
			} */
			
			function editAddAll(id, res) {
				tableSerProduct.deleteAllRow();
				var _partnerServicesArrayList = res.partnerServicesArrayList;
				if (_partnerServicesArrayList != null) {
					for (var i = 0; i < _partnerServicesArrayList.length; i++) {
						tableSerProduct.addRow(tableSerProduct.rowTemp);
						//$('select[name="partner.partnerServicesArrayList['+i+'].serviceInfoId.id"]').change();
						var catProductId = null;
						var serviceId = null;
						if (_partnerServicesArrayList[i].catProductId != null) {
							catProductId = _partnerServicesArrayList[i].catProductId.id;
						}
						if (_partnerServicesArrayList[i].serviceInfoId != null)
							serviceId = _partnerServicesArrayList[i].serviceInfoId.id;
						loadProduct(null,i,serviceId,catProductId);
						
						$('input[type="hidden"][name="partner.partnerServicesArrayList['+ i +'].id"]').val(_partnerServicesArrayList[i].id);
						$('input[type="hidden"][name="partner.partnerServicesArrayList['+ i +'].partnerId"]').val(id);
						/*$('select[name="partner.partnerServicesArrayList"]['+ i +'].serviceInfoId.id').val(_partnerServicesArrayList[i].serviceInfoId.id).trigger('change');;
						$('select[name="partner.partnerServicesArrayList"]['+ i +'].catProductId.id option[value='+_partnerServicesArrayList[i].catProductId.id+']').attr('selected','selected');*/
						$('input[type="text"][name="partner.partnerServicesArrayList['+ i +'].rate"]').val(_partnerServicesArrayList[i].rate);
						$('select[name="partner.partnerServicesArrayList['+ i +'].period"]').val(_partnerServicesArrayList[i].period).trigger('change');
						if (_partnerServicesArrayList[i].disableStatus) {
							$('input[type="checkbox"][name="partner.partnerServicesArrayList['+ i +'].disableStatus"]').prop("checked", true);
						} else {
							$('input[type="checkbox"][name="partner.partnerServicesArrayList['+ i +'].disableStatus"]').prop("checked", false);
						}
		
					}
				}
			}
			
			function addAllRowCustomer() {
				tableSerProduct.deleteAllRow();
				$.ajax({
					url : $('#theForm').attr('action')
							+ '?method=buildAllServiceProduct',
					data : {partnerId : $('#id').val()},
					method : 'POST',
					success : function(_result, status, xhr) {
						console.log(_result);
						editAddAll($('#id').val(), _result);
					}
				});
				/* $("#lstSvrs > option").each(function() {
				    //console(this.text + ' ' + this.value);
				    
				    addRowCustomer();
				}); */
			}
			
			function createFTP(){
				$.ajax({
					url : $('#theForm').attr('action')
							+ '?method=createFtpFolder',
					data : {partnerId : $('#id').val()},
					method : 'POST',
					success : function(_result, status, xhr) {
						alert(_result);
						
					}
				});
			}
			
			function instanceValidate(){
				//alert("a");
				if($("input[type='checkbox'][name*='disableStatus']:checked").length > 0 && $("#reasonUnconnett").val() == ""){
					alert("Ch\u01B0a nh\u1EADp l\u00FD do h\u1EE7y k\u1EBFt n\u1ED1i");
					console.log("Ch\u01B0a nh\u1EADp l\u00FD do h\u1EE7y k\u1EBFt n\u1ED1i");
					return false;
				}
				return true;
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>