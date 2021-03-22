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
	<tiles:putAttribute name="title" value="Tổ chức tín dụng gửi báo cáo"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/reportRule" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Danh Mục RULE</h1>
							</div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã Rule</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="code" ></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên Rule</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                       <form:input path="name" ></form:input>
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
		<form:hidden path="rpRule.id" id="id"/>
			<div class="box-custom">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Thông tin chi tiết rule</h1>
				</div>  
				<div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã rule<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "rpRule.code" cssClass="required uppercase ascii" />
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên rule<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "rpRule.name" cssClass="required" title="Tên không được để trống"/>
		            </div>
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả rule<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "rpRule.descripstion" cssClass="required" title="mô tả không được để trống"/>
		            </div>     
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Dữ liệu mẫu<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "rpRule.sample" />
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
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã rule'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Tên rule'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mô tả rule'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Dữ liệu mẫu'}
            ];
		}
		
		$(document).ready(function() {
			 $('.btnDtDelete').hide();
		});
		
		$(document).on('click', '.btnDtAdd', function () {
			document.getElementById("rpRule.code").disabled = false;
        });
		
		function afterEdit(id, res) {
			document.getElementById("rpRule.code").disabled = true;
		}
		function beforeSave() {
			document.getElementById("rpRule.code").disabled = false;
		}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>