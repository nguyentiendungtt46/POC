<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 

<tiles:insertDefinition name="catalog">
	<%-- <tiles:putAttribute name="title" value="Tham số hệ thống"/>  --%>
	<c:choose>
		<c:when test="${type == 2}">
			<tiles:putAttribute name="title" value="Tham số nghiệp vụ"/>
		</c:when>
		<c:otherwise>
			<tiles:putAttribute name="title" value="Tham số hệ thống"/>
		</c:otherwise>
	</c:choose>
	<tiles:putAttribute name="formInf">
		<spring:url value="/param" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						<form:hidden path="type" id="type"/>
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<c:choose>
									<c:when test="${type == 2}">
										<h1>Danh sách tham số nghiệp vụ</h1>
									</c:when>
									<c:otherwise>
										<h1>Danh sách tham số hệ thống</h1>
									</c:otherwise>
								</c:choose>
							</div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã Tham số</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="scode" id="scode"></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên Tham số</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <form:input path="sname" id="sname"></form:input>
		                    </div>
						</div>
						<div class="divaction" align="center">
				            <input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm"/>
				            <c:if test="${add}">
								<input class="btnDtAdd btn blue" type="button" id="btnDtAdd"
									onclick="addNew();" value="Thêm mới" />
							</c:if>	   
				        </div>
				         
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
		    </div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="sysParam.id" id="id"/>
		<div class="box-custom">
			<div class="row title-page" style="adding-bottom: 20px;">
				<h1>Thông tin chi tiết tham số</h1>
			</div>	
			<div class="Row">
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã<font color="red">*</font></div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:input path = "sysParam.code" cssClass="required uppercase ascii"/>
	            </div>
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên<font color="red">*</font></div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:input path = "sysParam.name" cssClass="required" title="Tên không được để trống"/>
	            </div>
	        </div>
	        <div class="Row">
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả</div>
	            <div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
	            	<form:textarea cols="5" path = "sysParam.description"/>
	            </div>
	        </div>
	        <div class="Row">
	        	<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Giá trị</div>
	            <div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
	            	<form:textarea cols="3" path="sysParam.value"/>
	            </div>
	        </div>
		</div>
	</tiles:putAttribute> 
	</form:form>
	
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
		function initParam(tblCfg){		
            tblCfg.bScrollX = true;
            tblCfg.aoColumns = [			 
                    {"sClass": "left","bSortable" : false,"sTitle":'STT'},
                    {"sClass": "left","bSortable" : false,"sTitle":'Mã'},
                    {"sClass": "left","bSortable" : false,"sTitle":'Tên'},
                    {"sClass": "left","bSortable" : false,"sTitle":'Mô tả'},
                    {"sClass": "left","bSortable" : false,"sTitle":'Giá trị'}
                            
            ];
		}
		
		$(document).ready(function() {
		     $('.btnDtDelete').hide();
		});
		
		$(document).on('click', '.btnDtAdd', function () {
			document.getElementById("sysParam.code").disabled = false;
        });
		
		function beforeEdit(res){
			document.getElementById("sysParam.code").disabled = true;
		}
		
		function beforeSave(){
			document.getElementById("sysParam.code").disabled = false;
		}
		
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>