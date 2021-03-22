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
	<tiles:putAttribute name="title" value="Theo dõi hỏi file định kỳ"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/qnaOutFile" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Theo dõi hỏi file định kỳ</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời điểm CIC trả từ</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="ngaytraloitu" name="ngaytraloitu" cssClass="currentDate"></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời điểm CIC trả đến</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <form:input path="ngaytraloiden" name="ngaytraloiden" cssClass="currentDate"></form:input>
		                    </div>
						</div>
						<div class="Row">
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên file trả lời</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="outFileName" id="outFileName"></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <%-- <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên file hỏi</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <form:input path="inFileName" id="inFileName"></form:input>
		                    </div> --%>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn vị</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<select class="form-control" name=matctd id="matctd">
									<option value="">- Chọn -</option>
									<c:forEach items="#{dsDoiTac}" var="item">
										<option value="${item.code}">
											<c:out value="${item.code}" /> -
											<c:out value="${item.name}" />
										</option>
									</c:forEach>
								</select>
		                    </div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trạng thái</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<form:select path="status" title="Trạng thái không được để trống">
									<option value="">- Chọn -</option>
									<option value="1">Chưa có kết quả</option>
									<option value="2">Đã có kết quả</option>
								</form:select>
							</div>
							
						</div>
						<div class="divaction" align="center">
				            <input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm"/>
				            <input class="btn blue" type="button" onclick="exportExcel();" value="Xuất excel"/>
				        </div>
				        <!-- <div align="center" class="HeaderText">&#8203;</div> -->			    
			        	
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
		    </div>
		</tiles:putAttribute>
		
	<%-- <tiles:putAttribute name="catDetail" cascade="true">
	</tiles:putAttribute>  --%>
	</form:form>
	
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
		function initParam(tblCfg){	
			tblCfg.bFilter = false;
            tblCfg.aoColumns = [			 
	            {"sClass": "left","bSortable" : false,"sTitle":'STT'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Tên file'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Thời điểm có kết quả'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Thời điểm trả cho TCTD'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Hành động'}
            ];
		}
		
		$(document).ready(function() {
			$(".btnDtAdd, .btnDtDelete").hide();
		     
		     $(".currentDate").datepicker({
					dateFormat : 'dd/mm/yy',
					changeMonth : false,
					changeYear : false,
					showButtonPanel : false
			});
		});
		
		function taiFile(id){
			window.open($('#theForm').attr('action') + '?method=downloadFileFtp&id=' + id);
		}
		
		function exportExcel() {
			var tuNgay = $('input[name="ngaytraloitu"]').val();
			var denNgay = $('input[name="ngaytraloiden"]').val();
			var partnerCode = $('select[name="matctd"]').val();
			var fileName = $('input[name="outFileName"]').val();
			var status = $('select[name="status"]').val();
			window.open($('#theForm').attr('action') + '?method=ExportFileExcel&tuNgay='+tuNgay+'&denNgay='+denNgay+
					'&partnerCode='+partnerCode+'&fileName='+fileName+'&status='+status);
		}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>