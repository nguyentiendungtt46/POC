<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 

<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Audit hệ thống"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/audit" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Audit hệ thống</h1>
							</div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên đăng nhập</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="username" id="username"></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Danh mục</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<%-- <form:input path="actionLists" id="actionLists"></form:input> --%>
		                    	<!-- <s:select onchange="changeCatalog($(this).val())" name="actionLists" list="actionList" id="actionLists" listKey="code" 
		                    		listValue="name" headerValue="%{getText('all')}" headerKey=""></s:select> -->
		                        <select class="form-control" name="actionLists" id="actionLists" path="actionLists">
									<option value="">- Chọn -</option>
									<c:forEach items="#{actionLists}" var="item">
										<option value="${item.code}">
											<%-- <c:out value="${item.code}" /> - --%>
											<c:out value="${item.name}" />
										</option>
									</c:forEach>
								</select>
		                    </div>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đối tượng</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="keyWord" id="keyWord"></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <%-- <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn vị</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <select class="form-control" name="spartnerId" id="SpartnerId">
									<option value="">- Chọn -</option>
									<c:forEach items="#{dsDoiTac}" var="item">
										<option value="${item.id}">
											<c:out value="${item.code}" /> -
											<c:out value="${item.name}" />
										</option>
									</c:forEach>
								</select>
		                    </div> --%>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Từ ngày</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="fromdate" name="todate" cssClass="toCurrentDate"></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến ngày</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <form:input path="todate" name="fromdate" cssClass="toCurrentDate"></form:input>
		                    </div>
						</div>
						<div class="divaction" align="center">
				            <input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm"/>
				            <input class="btn blue" type="button" onclick="exportExcel();" value="Export"/>
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
						<div class="modal-body question">
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
		
	</tiles:putAttribute>  --%>
	</form:form>
	
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function changeCatalog(catType){
			    if('hibernatedto.SysUsers' == catType){
			        $('.rKeyWord').css('display', 'block');
			        $('#keyWord').prop('disabled',false);
			    }
			        
			    else{
			        $('.rKeyWord').css('display', 'none');
			        $('#keyWord').prop('disabled',true);
			    }
			        
			  }
			  function initParam(tblCfg) {
				  tblCfg.bFilter = false;
				  tblCfg.notSearchWhenStart=true;
				  tblCfg.aoColumns = [
			      {
			    	  "sClass": "left","bSortable" : false, "sTitle" : 'STT'
			      },      
			      {
			    	  "sClass": "left","bSortable" : false, "sTitle" : 'Tên đăng nhập/TCTD'
			      },
			      {
			    	  "sClass": "left","bSortable" : false, "sTitle" : 'Hành động'
			      },
			      {
			    	  "sClass": "left","bSortable" : false, "sTitle" : 'Đối tượng'
			      },
			      {
			    	  "sClass": "left","bSortable" : false, "sTitle" : 'Chi tiết'
			      },
			      {
			    	  "sClass": "left","bSortable" : false, "sTitle" : 'Thời gian'
			      }
			];
			  }
			$(document).ready(function(){
			    $('#fromdate').val(getCurrentDate());
			    $('#todate').val(getCurrentDate());
			    findData();
			    if($('#spartnerId').children('option').length == 2)
			    {
			        $("#spartnerId option[value='']").remove();
			    }
			    $('.btnDtDelete').hide();
			    $('.btnDtAdd').hide();
			});
	
			  function defaultValue() {
			      $('#status').val(true);
			      $('#statustrue').prop('checked', true);
	
			  }
			  
			  function validateSearch(){
			        var sdtFromDate = $('#fromdate').val().split('/');
			        var sdtToDate = $('#todate').val().split('/');       
			        var dtFromDate = new Date(sdtFromDate[2], sdtFromDate[1]-1, sdtFromDate[0]);
			        var dtToDate = new Date(sdtToDate[2], sdtToDate[1]-1, sdtToDate[0]);
			        if(dtFromDate>dtToDate){
			            alert(tu_ngay_khong_duoc_lon_hon_den_ngay);
			            return false;
			        }
			        return true;
		    }
			
			  function chiTietLog(id){
					$.ajax({
		                url:$('#theForm').attr('action') + '?method=loadDetail&id=' + id,
		                method: 'GET',
		                success: function(data){
		                	$(".question").empty();
		                	$(".question").append("<textarea class=\"form-control\" rows=\"20\">"+ data +"</textarea>");
		                	
		                	$(".modal-title").empty();
		                	$(".modal-title").append("Chi tiết log hệ thống");
		                }
		            });
				}
			  
			  function exportExcel() {
					var tuNgay = $('input[name="fromdate"]').val();
					var denNgay = $('input[name="todate"]').val();
					var catelogy = $('select[name="actionLists"]').val();
					var username = $('input[name="username"]').val();
					var obj = $('input[name="keyWord"]').val();
					
					downloadFileUrl(location.pathname + '?method=ExportFileExcel&tuNgay='+tuNgay+'&denNgay='+denNgay+
							'&catelogy='+catelogy+'&username='+username+'&obj='+obj);
				}

		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>