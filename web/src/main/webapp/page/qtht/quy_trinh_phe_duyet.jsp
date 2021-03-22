<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Quy trình phê duyệt"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/appflow" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">	
				<div class="row search-style">
					<div class="Row">
						<div class="row title-page" style="adding-bottom: 20px;">
							<h1>Quy trình phê duyệt</h1>
						</div>
					</div>
					<form:hidden path="appFlow.wrkFlwMng.id"/>
			        <!-- <div align="center" class="HeaderText">&#8203;</div>
			        <div class="divaction" align="center">
			            <input type="button" onclick="addNew()" value="&#xf067; Thêm mới" class="btn blue"/>
			        </div> -->
				</div>		   
				  <%@ include file="/page/include/data_table.jsp"%>
		    </div>
		</tiles:putAttribute>
		
		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="appFlow.id" id="id"/>
			
			<div class="box-custom">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Thông tin chi tiết chức năng</h1>
				</div>  
				<div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Status<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "appFlow.status" cssClass="required digits"/>
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Sub status</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "appFlow.subStatus" cssClass="digits"/>
		            </div>
		        </div>
		        <div class="Row">
		        	<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "appFlow.name" cssClass="required"/>
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Hành động</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "appFlow.action"/>
		            </div>
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mô tả</div>
		            <div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
		            	<form:textarea path = "appFlow.description" />
		            </div>
		        </div>
			</div>
		</tiles:putAttribute> 
	</form:form>
	
							
			

	<tiles:putAttribute name="extra-scripts">
		<c:if test="${add}">
          	<script type="text/javascript">
          		tblCfg.buttons = [ 
		    		{ text: 'Thêm mới',attr:  {id:'btnDtAdd'}, className: 'mainGrid btnDtAdd btn blue', action: function ( e, dt, node, config ) { addNew(); } }
		        	];
          	</script>
		</c:if>
       <script type="text/javascript">
	        var createNew = false;
	        function initParam(tblCfg){			
	        	tblCfg.aoColumns = [			 
						{"sClass": "left","bSortable" : false,"sTitle":'STT'},
						{"sClass": "left","bSortable" : false,"sTitle":'Mã'},
						{"sClass": "left","bSortable" : false,"sTitle":'Tên'},
						{"sClass": "left","bSortable" : false,"sTitle":'Mô tả'},
						{"sClass": "left","bSortable" : false,"sTitle":'Hành động'}
				];
			}
	    </script>
    </tiles:putAttribute>
</tiles:insertDefinition>