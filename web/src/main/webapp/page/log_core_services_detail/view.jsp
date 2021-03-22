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
	<tiles:putAttribute name="title" value="Chi tiết trao đổi" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/logCoreServicesDetail" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm"
		enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id="divSearchInf">
					<form:hidden path="logCoreId" id="logCoreId"/>
						<div class="row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Chi tiết trao đổi</h1>
							</div>
							<%-- <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã báo cáo</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <select class="form-control" name="reportCode" id="reportCode">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstRpType}" var="item">
										<option value="${item.id}">
											<c:out value="${item.id}" />
										</option>
									</c:forEach>
								</select>
		                    </div> --%>
		                    <div class="Empty"></div>
						</div>
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
			</div>
			
			<div class="modal fade" id="myModal" role="dialog">
				<div class="modal-dialog">
					<!-- Modal content-->
					<div class="modal-content form-group">
						<div class="modal-header">
				          <h4 class="modal-title"></h4>
				        </div>
						<div class="modal-body value">
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

		<%-- <tiles:putAttribute name="catDetail" cascade="true">
		</tiles:putAttribute> --%>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg){	
				tblCfg.bFilter = false;
	            tblCfg.aoColumns = [			 
		            {"sClass": "left","bSortable" : false,"sTitle":'STT'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Tên Service'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Loại'},
		            {"sClass": "left","bSortable" : false,"sTitle":'Giá trị'}
	            ];
			}
			$(document).ready(function() {
			     $(".btnDtAdd, .btnDtDelete").hide();
			});
			
			function valueDetail(id){
				$.ajax({
	                url:location.pathname + '?method=loadValue&id=' + id,
	                method: 'GET',
	                success: function(data){
	                	$(".value").empty();
	                	$(".value").append("<textarea class=\"form-control\" rows=\"20\">"+ data +"</textarea>");
	                	
	                	$(".modal-title").empty();
	                	$(".modal-title").append("Chi tiết giá trị");
	                	
	                }
	            });
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>
	
	
