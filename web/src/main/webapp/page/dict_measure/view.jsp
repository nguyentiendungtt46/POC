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
	<tiles:putAttribute name="title" value="Quản lý chỉ tiêu"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/dictMeasure" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Quản lý chỉ tiêu</h1>
							</div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã chỉ tiêu</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	 <form:input path="codeSearch" ></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">path</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                       <form:input path="pathSearch" ></form:input>
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
		<form:hidden path="dictMeasure.id" id="id"/>
		<div style="display: none">
			</div>
			<div class="box-custom">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Thông tin chi tiết</h1>
				</div>  
				<div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã chỉ tiêu<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "dictMeasure.colCode" cssClass="required uppercase ascii" />
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Kiểu dữ liệu<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "dictMeasure.dataType" cssClass="required" title="Không được để trống"/>
		            </div>
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Độ dài</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "dictMeasure.colLength" cssClass="number" maxlength="3"/>
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Path<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "dictMeasure.path" cssClass="required" title="Không được để trống"/>
		            </div>
		        </div>
		         <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "dictMeasure.colDesc" cssClass="required"/>
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
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã chỉ tiêu'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mô tả'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Kiểu dữ liệu'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Path'}
	            
            ];
		}
		
		$(document).ready(function() {
			 $('.btnDtDelete').hide();
		});
		
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>