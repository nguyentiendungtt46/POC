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
	<tiles:putAttribute name="title" value="Danh sách đặt lịch/Job"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/jobConfig" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Danh sách đặt lịch/Job</h1>
							</div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã lịch</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="code" ></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên lịch</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                       <form:input path="name" ></form:input>
		                    </div>
						</div>
						<div class="divaction" align="center">
				            <input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm"/>
				            <input class="btn blue" type="button" onclick="exportExcel();" value="export"/>
				        </div>
				        <!-- <div align="center" class="HeaderText">&#8203;</div> -->			    
			        	
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
		    </div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="sysJobConfig.id" id="id"/>
			<div class="box-custom">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Danh sách đặt lịch/Job</h1>
				</div>  
				<div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã lịch<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "sysJobConfig.jobCode" cssClass="required " />
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên lịch<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "sysJobConfig.jobName" cssClass="required" />
		            </div>
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tần suất<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "sysJobConfig.rate" id="rate" cssClass="required textnumber"  maxlength="6" />
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chu kỳ<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
<%-- 		            	<form:input path = "sysJobConfig.period" cssClass="required" title="Chu kỳ không được để trống"/> --%>
						<form:select path="sysJobConfig.period" cssClass="required">
							<form:option value="1">Giờ</form:option>
							<form:option value="2">Phút</form:option>
							<form:option value="3">Giây</form:option>
						</form:select>
		            </div>
		        </div>
		        <div class="Row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Trạng thái
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:checkbox path="sysJobConfig.active"/>
					</div>
				</div>
			</div>
	</tiles:putAttribute>
	</form:form>
	
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
		function initParam(tblCfg){	
			tblCfg.bFilter = false;
			tblCfg.buttons = [];
            tblCfg.aoColumns = [			 
	            {"sClass": "left","bSortable" : false,"sTitle":'STT'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã lịch'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Tên lịch'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Tần suất'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Chu kỳ'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Trạng thái'}
            ];
		}
		
		$(document).ready(function() {
			 $('.btnDtDelete').hide();
		});
		
		$(document).on('click', '.btnDtAdd', function () {
			document.getElementById("sysJobConfig.jobCode").disabled = false;
        });
		
		function afterEdit(id, res) {
			document.getElementById("sysJobConfig.jobCode").disabled = true;
		}
		function beforeSave() {
			document.getElementById("sysJobConfig.jobCode").disabled = false;
		}
		
		
		function exportExcel(){
			var code =  $('#code').val();
			var value = $('#name').val();
			window.open($('#theForm').attr('action') + '?method=exportFileExcel&code=' +code+ '&value=' +value);
		}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>