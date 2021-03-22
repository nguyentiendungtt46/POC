<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Danh sách chức năng"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/right" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">	
				<div class="row search-style">
					<div class="Row">
						<div class="row title-page" style="adding-bottom: 20px;">
							<h1>Danh sách chức năng hệ thống</h1>
						</div>
					</div>
					
			        <!-- <div align="center" class="HeaderText">&#8203;</div>
			        <div class="divaction" align="center">
			            <input type="button" onclick="addNew()" value="&#xf067; Thêm mới" class="btn blue"/>
			        </div> -->
				</div>		   
				  <%@ include file="/page/include/data_table.jsp"%>
		    </div>
		</tiles:putAttribute>
		
		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="sysObj.id" id="id"/>
			<div class="box-custom">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Thông tin chi tiết chức năng</h1>
				</div>  
				<div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "sysObj.objectId" cssClass="required uppercase ascii"/>
		            </div>
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "sysObj.name" cssClass="required" title="Tên không được để trống"/>
		            </div>
		        </div>
		        <div class="Row">
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Action<font color="red">*</font></div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            	<form:input path = "sysObj.action" cssClass="required" title="Action không được để trống"/>
		            </div>     
		            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chức năng cha</div>
		            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		            
		            <select class="form-control" name="sysObj.sysObjects.id" id="RightList">
						<option value="">- Chọn -</option>
						<c:forEach items="#{danhSachQuyen}" var="item">
							<option value="${item.id}">
								<c:out value="${item.objectId}" /> -
								<c:out value="${item.name}" />
							</option>
						</c:forEach>
					</select>
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
						{"sWidth" : "5%","sClass": "left","bSortable" : false,"sTitle":'STT'},
						{"sWidth" : "10%","sClass": "left","bSortable" : false,"sTitle":'Mã'},
						{"sWidth" : "20%","sClass": "left","bSortable" : false,"sTitle":'Tên'},
						{"sWidth" : "28%","sClass": "left","bSortable" : false,"sTitle":'Hành động'},
						{"sWidth" : "10%","sClass": "left","bSortable" : false,"sTitle":'Chức năng cha'}
				];
			}
	        
	        $(document).ready(function() {
			     $('.btnDtDelete').hide();
			});
	        
	        $(document).on('click', '.btnDtAdd', function () {
				document.getElementById("sysObj.objectId").disabled = false;
	        });
			
			function beforeEdit(res){
				document.getElementById("sysObj.objectId").disabled = true;
			}
			
			function beforeSave(){
				document.getElementById("sysObj.objectId").disabled = false;
			}
	        
	        function defaultValue(){
	            $('#status').val(true);
	            $('#statustrue').prop('checked', true);
	            
	        }
	        function instanceValidate(){ 
	        	if ($('#id').val() === "") {
	        		createNew = true;
	        	}
	            if($('#RightList').val() == $('#id').val() && !createNew){
	                alert('Chức năng cha không được trùng với chức năng con');
	                return false;
	            }
	            return true;
	        }
	    </script>
    </tiles:putAttribute>
    
	
</tiles:insertDefinition>