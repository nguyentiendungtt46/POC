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
<%
       ApplicationContext appContext = (ApplicationContext)request.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT);
       SysUsers user = null;
       if(appContext!=null)
        user = (SysUsers)appContext.getAttribute(ApplicationContext.USER);
       WebApplicationContext ac = RequestContextUtils.findWebApplicationContext(request,null);
       SysParamDao sysParamDao = (SysParamDao) ac.getBean("sysParamDao");
       entity.frwk.SysParam LDAP_AUTHEN =  sysParamDao.getSysParamByCode("LDAP_AUTHEN");
%>
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Từ điển hệ thống"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/sysParam" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Từ điển hệ thống</h1>
							</div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="codeSearch" ></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên tham số</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                       <form:input path="valueSearch" ></form:input>
		                    </div>
						</div>
						
						<div class="Row">
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại từ điển</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<select name="paramsType" class="from-control">
		                    	<option value=""></option>
				            		<c:forEach items="#{sysTypes}" var="item">
										<option value="${item.id}">
											<c:out value="${item.code}" /> -
											<c:out value="${item.name}" />
										</option>
									</c:forEach>
				            	</select>
		                    </div>
		                    
						</div>
						<div class="divaction" align="center">
				            <input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm"/>
				            <c:if test="${add}">
								<input class="btnDtAdd btn blue" type="button" id="btnDtAdd"
									onclick="addNew();" value="Thêm mới" />
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
		
			<div class="box-custom">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Thông tin chi tiết</h1>
				</div>  
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "sysParam.code" cssClass="required uppercase ascii" />
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "sysParam.value" cssClass="required" title="giá trị không được để trống"/>
		            	<form:hidden path="sysParam.id" id="id"/>
		            </div>
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "sysParam.description" cssClass="required" title="mô tả không được để trống"/>
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:select path="sysParam.sysDictType.id" class="from-control">
		            	<option value=""></option>
		            		<c:forEach items="#{sysTypes}" var="item">
		            			
								<option value="${item.id}">
									<c:out value="${item.code}" /> -
									<c:out value="${item.name}" />
								</option>
							</c:forEach>
		            	</form:select>
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
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Tên'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mô tả'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Loại'}
            ];
		}
		
		function exportExcel(){
			var code =  $('#codeSearch').val();
			var value = $('#valueSearch').val();
			var paramType = $('select[name="paramsType"]').val();;
			window.open($('#theForm').attr('action') + '?method=exportFileExcel&code=' +code+ '&value=' +value+ '&paramType='+paramType);
		}
		
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