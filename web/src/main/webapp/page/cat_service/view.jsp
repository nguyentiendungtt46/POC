<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Danh mục service" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/serviceInfo" var="formAction" />
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
								<h1>Danh mục service</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại service</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="type" title="Trạng thái không được để trống">
									<option value="">- Chọn -</option>
									<option value="1">Xác thực</option>
									<option value="2">Hỏi và trả lời</option>
									<option value="3">Báo cáo</option>
									<option value="4">Vấn tin</option>
								</form:select>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên service</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="name"/>
							</div>
						</div>
						<%-- <div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên
								service</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="name" id="name"></form:input>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên api server</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="apiServerName" id="apiServerName"></form:input>
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên api publish</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="apiPublishName" id="apiPublishName"></form:input>
							</div>
						</div> --%>
						<!-- <div align="center" class="HeaderText">&#8203;</div> -->
						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm" />
							<c:if test="${add}">
				            	<input class="btnDtAdd btn blue" type="button" id="btnDtAdd" onclick="addNew();" value="Thêm mới" />
							</c:if>
							<input class="btn blue" type="button" onclick="exportExcel();" value="Export" />
						</div>
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>


			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="serviceInfo.id" id="id" />
			<form:hidden path="serviceInfo.appType" id="appType" />
			<form:hidden path="serviceInfo.order" id="order" />
			<div style="display: none">
				<form:select path="serviceInfo.catProducts" id="lstProduct">
				<option value=""></option>
					<c:forEach items="#{products}" var="item">
					<option value="${item.id}">
						<c:out value="${item.code}" /> - <c:out value="${item.name}" />
					</option>
				</c:forEach>
				</form:select>
			</div>
			<div class="box-custom">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Thông tin dịch vụ</h1>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Loại dịch vụ<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="serviceInfo.type" cssClass="required" >
							<option value="">- Chọn -</option>
							<option value="1">Xác thực</option>
							<option value="2">Hỏi và trả lời</option>
							<option value="3">Báo cáo</option>
							<option value="4">Vấn tin</option>
						</form:select>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã dịch vụ<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="serviceInfo.code" cssClass="required" ></form:input>
					</div>
					
					
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Tên dịch vụ<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="serviceInfo.serviceName" cssClass="required"
							title="Tên dịch vụ không được để trống"></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mô tả dịch vụ<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="serviceInfo.description" cssClass="required"
							title="Tên loại sản phẩm không được để trống"></form:input>
					</div>
					
				</div>
				
				<div class="row">
					
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Disabled
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:checkbox path="serviceInfo.status"/>
					</div>
					
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Giới hạn dữ liệu (MB)
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="serviceInfo.maxSizeApi" cssClass="number" maxlength="3"></form:input>
					</div>
					
					
					
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Thời gian hiệu lực dữ liệu (giờ)<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="serviceInfo.timeToLive" cssClass="required number" maxlength="2" ></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Tần suất gọi sang CIC theo quy định (phút)<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="serviceInfo.frequency" cssClass="required number" maxlength="3"></form:input>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Job thực hiện
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:checkbox path="serviceInfo.callByJob"></form:checkbox>
					</div>
					
				</div>
			</div>
			<div class="box-custom row">
				<div class="col-md-8">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Danh sách sản phẩm theo dịch vụ</h1>
					</div>
					<div class="table-responsive" >
						<table class="table table-bordered" id="table-service-product" >
							<thead>
								<tr>
									<th>Sản phẩm</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			var createNew = false;
			var tableSerProduct = null;
			function initParam(tblCfg) {
				tblCfg.bFilter = false;
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Loại service'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên service'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mô tả Service'
				},{
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tần suất gọi sang CIC theo quy định (phút)'
				},{
					"sClass" : "center",
					"bSortable" : false,
					"sTitle" : 'Disabled'
				},{
					"sClass" : "center",
					"bSortable" : false,
					"sTitle" : 'Job thực hiện'
				} ];
			}
			
			$(document).ready(function() {
				$('.btnDtDelete').hide();
				initTableSerProduct();
			});
			
			function initTableSerProduct(){
				var rowTempServiceProduct = initRowTableSerProduct();
				var tableObject = new TFOnline.DataTable({
					id : 'table-service-product',
					jQueryUI : true,
					rowTemp : rowTempServiceProduct,
					hasCheck : true,
					delCaption : 'Xóa',
					addOveride:true,
                    delOveride:true,
                    hasOrder:true,
					maxRow : 100 
				});
				tableSerProduct = tableObject;
				
			}
			
			function initRowTableSerProduct() {
				var rowTemp = [];
				var listObject = "serviceInfo";
				var strHidden  = '<input type="hidden" class="form-control" name="' + listObject + '.serviceProductArrayList[].id" />';
				strHidden += '<input type="hidden" class="form-control" name="' + listObject + '.serviceProductArrayList[].serviceInfoId.id" />';
					rowTemp = [
							'<div class="line-table"><select onChange="checkDuplicate(this);" class="form-control catProductService " name="' + listObject + '.serviceProductArrayList[].catProductId.id">'+$('#lstProduct').html()+'</select></div>'
							+ strHidden
							];
				return rowTemp;
			}
			function afterEdit(id,res) {
				tableSerProduct.deleteAllRow();
				var _serverProducts = res.serviceProductArrayListClient;
				if (_serverProducts != null) {
					console.log(_serverProducts);
					for (var i = 0 ; i < _serverProducts.length; i++) {
						tableSerProduct.addRow(tableSerProduct.rowTemp);
						$('input[type="hidden"][name="serviceInfo.serviceProductArrayList['+ i +'].id"]').val(_serverProducts[i].id);
						$('input[type="hidden"][name="serviceInfo.serviceProductArrayList['+ i +'].serviceInfoId.id"]').val(_serverProducts[i].serviceId);
						$('select[name="serviceInfo.serviceProductArrayList['+i+'].catProductId.id"]').val(_serverProducts[i].productId);
						$('select[name="serviceInfo.serviceProductArrayList['+i+'].catProductId.id"]').select2();
					}
				}
				$('input[name="code"]').attr('readonly','true');
			}
			function checkDuplicate(newProduct) {
				var array = $('.catProductService');
				productVal = $(newProduct).val();
				var check = false;
				if (array.length > 0) {
					for (var i = 0; i < array.length - 1; i ++) {
						if (productVal == $('select[name="serviceInfo.serviceProductArrayList['+i+'].catProductId.id"] option:selected').val()) {
							check = true;
						}
					}
					if (check) {
						alert("Sản phẩm đã tồn tại vui lòng chọn sản phẩm khác");
					}
				}
				
			}
			
			function exportExcel() {
				var status = $('#status').val();
				var name = $('#name').val();
				var apiServerName = $('#apiServerName').val();
				var apiPublishName = $('#apiPublishName').val();
				var servierType = $('#type').val();
				window.open($('#theForm').attr('action')
						+ '?method=exportExcel&servierType=' + servierType
						+ '&apiPublishName=' + apiPublishName + '&apiServerName=' + apiServerName + '&name='+name+'&status='+status);
			}
			
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
	
	
