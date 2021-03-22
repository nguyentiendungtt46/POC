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
	<tiles:putAttribute name="title" value="Chi nhánh TCTD"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/partnerBranch" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Chi nhánh TCTD</h1>
							</div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã chi nhánh</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="codeSearch" ></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên chi nhánh</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                       <form:input path="nameSearch" ></form:input>
		                    </div>
						</div>
						
						<div class="divaction" align="center">
				            <input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm"/>
				            <c:if test="${add}">
				            	<input class="btnDtAdd btn blue" type="button" id="btnDtAdd" onclick="addNew();" value="Thêm mới" />
							</c:if>
				            <input class="btn blue" type="button" onclick="exportExcel();" value="Export"/>
				        </div>
				        <!-- <div align="center" class="HeaderText">&#8203;</div> -->			    
			        	
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
		    </div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="partnerBranch.id" id="id"/>
			<div class="box-custom">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Thông tin chi tiết </h1>
				</div>  
				<div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã chi nhánh<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "partnerBranch.code" cssClass="required uppercase ascii partnerBranchCode" />
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên chi nhánh<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "partnerBranch.name" cssClass="required" title="Tên không được để trống"/>
		            </div>
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Địa chỉ chi nhánh<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "partnerBranch.address" cssClass="required" title="mô tả không được để trống"/>
		            </div>     
		              
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Không hoạt động</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:checkbox path = "partnerBranch.active" cssClass="" />
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
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã chi nhánh'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Tên chi nhánh'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Địa chỉ'}
            ];
		}
		
		$(document).ready(function() {
			 $('.btnDtDelete').hide();
		});
		
		$(document).on('click', '.btnDtAdd', function () {
			document.getElementById("partnerBranch.code").disabled = false;
        });
		
		function beforeSave() {
			document.getElementById("partnerBranch.code").disabled = false;
		}
		
		function afterEdit(id,res) {
			document.getElementById("partnerBranch.code").disabled = true;
			
			console.log(res);
			$('select[name="partnerBranch.partnerId"]').val(res.partnerId.id).trigger('change');
		}
		
		function exportExcel(){
			var code =  $('#codeSearch').val();
			var value = $('#nameSearch').val();
			var paramType = $('#tctdId').val();;
			window.open($('#theForm').attr('action') + '?method=exportFileExcel&code=' +code+ '&value=' +value+ '&paramType='+paramType);
		}
		
		function validateBranchCode() {
			var a = $('select[name="partnerBranch.partnerId"] option:selected').val();
			var branchCode = $('input[name="partnerBranch.code"]').val();
			if (a == '' || a == null || a=="" || branchCode == null || branchCode == "")
				return;
			var text = $('select[name="partnerBranch.partnerId"] option:selected').text();
			var partner = text.split("-");
			var partnerCode = partner[0].trim();
			var code = branchCode.substring(2, branchCode.length-3)
			if (code != partnerCode) {
				alert("Mã chi nhánh sai định dạng");
			}
		}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>