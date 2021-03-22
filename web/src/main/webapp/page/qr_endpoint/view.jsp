<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@ page import="entity.frwk.SysUsers" %>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@page import="constants.RightConstants"%>
<%@page import="org.springframework.web.servlet.support.RequestContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="frwk.dao.hibernate.sys.SysParamDao"%>
<%@ page import="frwk.utils.ApplicationContext" %>

<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Endpoint service vấn tin"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/endpoint" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Endpoint service vấn tin</h1>
							</div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã sản phẩm</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	 <form:input path="code" ></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Endpoint</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                       <form:input path="endpointSearch" ></form:input>
		                    </div>
						</div>
						<div class="divaction" align="center">
				            <input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm"/>
				        </div>
				        <!-- <div align="center" class="HeaderText">&#8203;</div> -->			    
			        	
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
		    </div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="endpoint.id" id="id"/>
		<div style="display: none">
				<form:select path="endpoint.dictParams" id="lstFunEndpoints">
				<option value=""></option>
					<c:forEach items="#{dictParams}" var="item">
					<option value="${item.id}">
						<c:out value="${item.code}" /> - <c:out value="${item.value}" />
					</option>
				</c:forEach>
				</form:select>
			</div>
			<div class="box-custom">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Thông tin chi tiết Endpoint</h1>
				</div>  
				<div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã sản phẩm<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "endpoint.productCode" cssClass="required uppercase ascii" />
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Endpoint<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "endpoint.endpoint" cssClass="required" title="Tên không được để trống"/>
		            </div>
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên function</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "endpoint.funName" />
		            </div>
		        </div>
		        
			</div>
			<div class="box-custom row">
				<div class="col-md-12">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Danh sách param theo function</h1>
					</div>
					<div class="table-responsive" >
						<table class="table table-bordered" id="table-param-product">
							<thead>
								<tr>
									<th width="200px">param</th>
									<th width="130px">postion</th>
									<th width="150px">Format kiểu dữ liệu</th>
									<th width="150px">Max length</th>
									<th width="150px">required01</th>
									<th width="100px">required</th>
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
		function initParam(tblCfg){	
			tblCfg.bFilter = false;
            tblCfg.aoColumns = [			 
	            {"sClass": "left","bSortable" : false,"sTitle":'STT'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã Sản phẩm'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Endpoint'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Tên function'}
            ];
		}
		
		$(document).ready(function() {
			 $('.btnDtDelete').hide();
			 initTableSerProduct();
		});
		
		function initTableSerProduct(){
			var rowTempServiceProduct = initRowTableSerProduct();
			var tableObject = new TFOnline.DataTable({
				id : 'table-param-product',
				jQueryUI : true,
				rowTemp : rowTempServiceProduct,
				hasCheck : true,
				delCaption : 'Xóa',
				addOveride:true,
                delOveride:true,
				maxRow : 100 
			});
			tableSerProduct = tableObject;
		}
		function initRowTableSerProduct() {
			var rowTemp = [];
			var listObject = "endpoint";
			var strHidden  = '<input type="hidden" class="form-control" name="' + listObject + '.funEndpoints[].id" />';
				rowTemp = [
						'<div class="line-table"><select class="form-control " name="' + listObject + '.funEndpoints[].dictParam.id">'+$('#lstFunEndpoints').html()+'</select></div>',
						'<div class="line-table number"><input type="text" class="form-control number" name="' + listObject + '.funEndpoints[].position" /></div>',
						'<div class="line-table "><input type="text" class="form-control" name="' + listObject + '.funEndpoints[].formatDataType" /></div>',
						'<div class="line-table"><input type="text" name="' + listObject + '.funEndpoints[].maxLength" /></div>',
						'<div class="line-table"><input type="text" name="' + listObject + '.funEndpoints[].required01" /></div>',
						'<div class="line-table " style="text-align: center;"><input type="checkBox" name="' + listObject + '.funEndpoints[].required" /></div>'
						+ strHidden
						];
			return rowTemp;
		}
		
		$(document).on('click', '.btnDtAdd', function () {
			document.getElementById("endpoint.productCode").disabled = false;
        });
		
		function beforeSave() {
			document.getElementById("endpoint.productCode").disabled = false;
		}
		
		function afterEdit(id,res) {
			document.getElementById("endpoint.productCode").disabled = true;
			
			tableSerProduct.deleteAllRow();
			var _serverProducts = res.funEndpoints;
			if (_serverProducts != null) {
				console.log(_serverProducts);
				for (var i = 0 ; i < _serverProducts.length; i++) {
					tableSerProduct.addRow(tableSerProduct.rowTemp);
					$('input[type="hidden"][name="endpoint.funEndpoints['+ i +'].id"]').val(_serverProducts[i].id);
					$('input[type="text"][name="endpoint.funEndpoints['+ i +'].position"]').val(_serverProducts[i].position);
					if (_serverProducts[i].dictParam != null)
						$('select[name="endpoint.funEndpoints['+i+'].dictParam.id"]').val(_serverProducts[i].dictParam.id);
					$('input[type="text"][name="endpoint.funEndpoints['+ i +'].formatDataType"]').val(_serverProducts[i].formatDataType);
					$('input[type="text"][name="endpoint.funEndpoints['+ i +'].maxLength"]').val(_serverProducts[i].maxLength);
					$('input[type="text"][name="endpoint.funEndpoints['+ i +'].required01"]').val(_serverProducts[i].required01);
					if (_serverProducts[i].required) {
						$('input[type="checkBox"][name="endpoint.funEndpoints['+ i +'].required"]').prop("checked", true);
					} else {
						$('input[type="checkBox"][name="endpoint.funEndpoints['+ i +'].required"]').prop("checked", false);
					}
					
					$('select[name="endpoint.funEndpoints['+i+'].dictParam.id"]').select2();
				}
			}
		}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>