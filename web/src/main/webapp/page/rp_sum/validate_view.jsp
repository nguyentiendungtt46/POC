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
	<tiles:putAttribute name="title" value="Chi tiết báo cáo"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/rpValidate" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						<form:hidden path="reportId"/>
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Chi tiết báo cáo</h1>
							</div>
						</div>
						
				        <div class="row">
				        	<div class="col-md-6">
				        		<font style="color: blue">Tên file : </font> ${rpSum.fileName}
				        		<input type="hidden" id="reportCode" name="reportCode" value="${rpSum.reportCode}"/>
				        	</div>
				        	<div class="col-md-6">
				        		<font style="color: blue">Người báo cáo : </font>${rpSum.userReport}
				        	</div>
				        </div>
				        <div class="row">
				        	<div class="col-md-6">
				        		<font style="color: blue">Ngày báo cáo : </font>${rpSum.strReportDate}
				        	</div>
				        	<div class="col-md-6" id="tongDuNo">
				        		<font style="color: blue">Tổng dư nợ : </font>${rpSum.strBal}
				        	</div>
				        </div>
				        <div class="row">
				        	<div class="col-md-6" id="tongGiaTriDauTu">
				        		<font style="color: blue">Tổng giá trị đầu tư : </font>${rpSum.strInst}
				        	</div>
				        	<div class="col-md-6" id="tongTaiSan">
				        		<font style="color: blue">Tổng tài sản : </font>${rpSum.strAst}
				        	</div>
				        </div>
				        <div class="row">
				        	<div class="col-md-6" id="tongSoTienCanThanhToan">
				        		<font style="color: blue">Tổng số tiền cần thanh toán : </font>${rpSum.strPmtAmt}
				        	</div>
				        	<div class="col-md-6" id="tongSoTraiPhieu">
				        		<font style="color: blue">Tổng số trái phiếu : </font>${rpSum.strBondAmt}
				        	</div>
				        </div>
					
				        <!-- <div align="center" class="HeaderText">&#8203;</div> -->	
				        
				        <div class="row">
				        	<c:if test="${not empty totalBalByCurrency && nameTable ne 'RP_DETAIL_K3213'}">
							    <div class="col-md-12">
					        		<table id="totalBalByCurrency" class="table table-bordered table-hover sticky-header-table " >
										<thead style="background-color: #339;color: #f2f0f0">
											<tr>
												<td>Loại tiền</td>
												<td>Số tiền</td>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="item" items="${totalBalByCurrency}">
												<tr>
													<td> <c:out value="${item[0]}"></c:out> </td>
													<td> <c:out value="${item[1]}"></c:out></td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
					        	</div>
							</c:if>
				        	<c:if test="${not empty totalBalAll && (nameTable eq 'RP_DETAIL_K3213' or nameTable eq 'RP_DETAIL_T02G1' or nameTable eq 'RP_DETAIL_T02G2')}">
				        		<div class="col-md-12">
				        		
				        		<table id="totalBalAll" class="table table-bordered table-hover sticky-header-table " >
									<thead style="background-color: #339;color: #f2f0f0">
										<tr>
											<td>Loại tiền</td>
											<td>Số tiền nội bảng</td>
											<td>Số tiền ngoại bảng</td>
											<td>Số tiền cam kết</td>
										</tr>
									</thead>
									<tbody>
										<c:forEach var="item" items="${totalBalAll}">
											<tr>
												<td> <c:out value="${item[0]}"></c:out> </td>
												<td> <c:out value="${item[1]}"></c:out></td>
												<td> <c:out value="${item[2]}"></c:out></td>
												<td> <c:out value="${item[3]}"></c:out></td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
				        	</div>
				        	</c:if>
				        	<c:if test="${not empty rCursorK5 && nameTable eq 'RP_DETAIL_K5'}">
							    <div class="col-md-12">
					        		<table id="totalBalByCurrency" class="table table-bordered table-hover sticky-header-table " >
										<thead style="background-color: #339;color: #f2f0f0">
											<tr>
												<td>Loại tiền</td>
												<td>Tổng số lượng trái phiếu</td>
												<td>Tổng giá trị đầu tư</td>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="item" items="${rCursorK5}">
												<tr>
													<td> <c:out value="${item[0]}"></c:out> </td>
													<td> <c:out value="${item[1]}"></c:out></td>
													<td> <c:out value="${item[2]}"></c:out> </td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
					        	</div>
							</c:if>
				        </div>		    
					</div>
				</div >
				<c:if test="${not empty errors}">
					<div class="search-style" title="Lỗi báo cáo">
						<div class="row">
							<h4 style="color: blue">Lỗi báo cáo</h4>
						</div>
						<input class="btn blue" type="button" onclick="exportExcel('errorReport');" value="Export"/>
						<div id="divDatatable"  class="table-responsive">
						<table id="rpSumError" class="table table-bordered table-hover sticky-header-table " >
							<thead style="background-color: #339;color: #f2f0f0">
								<tr>
									<td>Mã lỗi</td>
									<td>Mô tả lỗi</td>
									<td>Dữ liệu lỗi</td>
									<td>Dòng bắt đầu</td>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="item" items="${errors}">
									<tr>
										<td> <c:out value="${item.errorCode}"></c:out> </td>
										<td> <c:out value="${item.errorDes}"></c:out></td>
										<c:if test="${item.errorCode eq 'RPF_117'}">
											<td><a href="javascript:viewKhachHangError('2');">Khách hàng lệch loại vay</a></td>
										</c:if>
										<c:if test="${item.errorCode eq 'RPF_127'}">
											<td><a href="javascript:viewKhachHangError('1');">Khách hàng lệch nhóm nợ</a></td>
										</c:if>
										<c:if test="${item.errorCode ne 'RPF_117' and item.errorCode ne 'RPF_127'}">
											<td> <c:out value="${item.errorCause}"></c:out></td>
										</c:if>
										<td> <c:out value="${item.beginRow}"></c:out></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
						</div>
					</div>
				</c:if>
				
				<c:if test="${not empty branchs}">
					<div class="search-style" title="Lỗi chi nhánh">
						<div class="row">
							<h4 style="color: blue">Lỗi chi nhánh</h4>
						</div>
						<input class="btn blue" type="button" onclick="exportExcel('errorBranch');" value="Export"/>
						<div id="divDatatable"  class="table-responsive">
							<table id="rpSumBranchError" class="table table-bordered table-hover sticky-header-table " >
							<thead style="background-color: #339;color: #f2f0f0">
								<tr>
									<td>Mã chi nhánh</td>
									<td>Tên chi nhánh</td>
									<td>Dữ liệu lỗi</td>
									<td>Mã lỗi</td>
									<td>Mô tả lỗi</td>
									<td>Dòng bắt đầu</td>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="item" items="${branchs}">
									<c:forEach items="${item.rpSumBranchErrs}" var="bItem">
								       <tr>
											<td> <c:out value="${item.branchCode}"></c:out> </td>
											<td> <c:out value="${item.branchName}"></c:out></td>
											<td> <c:out value="${bItem.errorCause}"></c:out></td>
											<td> <c:out value="${bItem.errorCode}"></c:out></td>
											<td> <c:out value="${bItem.errorDes}"></c:out></td>
											<td> <c:out value="${bItem.rpSumBranch.beginRow}"></c:out></td>
										</tr>
								   </c:forEach>
									
								</c:forEach>
							</tbody>
						</table>
						</div>
						
					</div>
				</c:if>
				<c:if test="${not empty errorDetail}">
					<div class="search-style">
						<div class="row">
							<h4 style="color: blue">Lỗi chi tiết</h4>
						</div>
						<div class="Row">
			                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã lỗi</div>
			                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
			                    	<form:input path="errorCode" ></form:input>
			                    </div>
			                    <div class="Empty"></div>
			                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã khách hàng</div>
			                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
			                       <form:input path="CustomerCode" ></form:input>
			                    </div>
							</div>
							<div class="Row">
			                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã chi tiêu</div>
			                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
			                    	<form:input path="maCT" ></form:input>
			                    </div>
			                   
							</div>
							<div class="divaction" align="center">
								<input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm"/>
								<input class="btn blue" type="button" onclick="exportExcel('detail');" value="Export"/>
							</div>
							
						<%@ include file="/page/include/data_table.jsp"%>
					</div>
				</c:if>
				
		    </div>
		    <!-- Modal Bo sung dich vu -->
			<div class="modal fade" id="modal-view-error" role="dialog">
				<div class="modal-dialog">
					<!-- Modal content-->
					<div class="modal-content" >
						<div class="modal-header">
							
							<h6 class="modal-title" id="modal-title">Danh sách khách hàng lệch loai vay/nhóm nợ</h6>
								<button type="button" class="close" data-dismiss="modal" style="color: #fff;">&times;</button>
						</div>
						<div class="modal-body">
							<div class="form-group">
								<div class="col-md-12">
					        		<table id="tableError" class="table table-bordered table-hover sticky-header-table " >
										<thead style="background-color: #339;color: #f2f0f0">
											<tr>
												<td>Mã chi nhánh</td>
												<td>Mã khách hàng</td>
												<td>Giá trị K31</td>
												<td>Giá trị K32</td>
											</tr>
										</thead>
										<tbody>
											<c:forEach var="item" items="${rCursor}">
												<tr>
													<td> <c:out value="${item[0]}"></c:out> </td>
													<td> <c:out value="${item[1]}"></c:out> </td>
													<td> <c:out value="${item[2]}"></c:out> </td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
					        	</div>
							</div>
						</div>
							<div class="modal-footer">
								<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
							</div>
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
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã CN'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Tên CN'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã KH'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Tên KH'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Dòng lỗi'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã chi tiêu'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Giá trị lỗi'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã lỗi'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mô tả lỗi'}
	            /* {"sClass": "left","bSortable" : false,"sTitle":'Hành động'} */
            ];
		}
		
		function exportExcel(typeReport){
			var url = window.location.href;
			var tmp = url.split('=');
			var reportId = tmp[1];
			var urlTmp = tmp[0].split('?');
			window.open(urlTmp[0] + '?method=exportFileExcel&reportId=' + reportId + '&typeReport='+typeReport);
		}
		
		$(document).ready(function() {
			 $('.btnDtDelete').hide();
			 $('.btnDtAdd').hide();
			 $('#totalBalAll').DataTable({
	                "oLanguage" : {
	                    "sLengthMenu" : "Hiển thị" + "_MENU_" + "bản ghi",
	                    "sZeroRecords" : " ",
	                    "sInfo" : "Hiển thị" + " _START_ " + "đến" + " _END_ " + "của" + " _TOTAL_ " + "bản ghi",
	                    "sInfoEmpty" : "Hiển thị" + " " + "từ" + " 0 " + "tới" + " 0 " + "trên tổng số" + " 0 " + "bản ghi",
	                    "sInfoFiltered" : "( " + "Đã lọc từ" + " _MAX_ " + "tổng số bản ghi" + " )",
	                    "oPaginate" : {
			                "sFirst" :"<i class='fa fa-fast-backward'></i>",
			                "sLast" :"<i class='fa fa-fast-forward'></i>",
			                "sPrevious":"<i class='fa fa-backward'></i>",
			                "sNext" : "<i class='fa fa-forward'></i>"
		            },
	                    "sSearch" : "Từ khóa"
	                },
	                "bDestroy" : false,
	                "bFilter": false
	            });
			 $('#totalBalByCurrency').DataTable({
	                "oLanguage" : {
	                    "sLengthMenu" : "Hiển thị" + "_MENU_" + "bản ghi",
	                    "sZeroRecords" : " ",
	                    "sInfo" : "Hiển thị" + " _START_ " + "đến" + " _END_ " + "của" + " _TOTAL_ " + "bản ghi",
	                    "sInfoEmpty" : "Hiển thị" + " " + "từ" + " 0 " + "tới" + " 0 " + "trên tổng số" + " 0 " + "bản ghi",
	                    "sInfoFiltered" : "( " + "Đã lọc từ" + " _MAX_ " + "tổng số bản ghi" + " )",
	                    "oPaginate" : {
			                "sFirst" :"<i class='fa fa-fast-backward'></i>",
			                "sLast" :"<i class='fa fa-fast-forward'></i>",
			                "sPrevious":"<i class='fa fa-backward'></i>",
			                "sNext" : "<i class='fa fa-forward'></i>"
		            },
	                    "sSearch" : "Từ khóa"
	                },
	                "bDestroy" : false,
	                "bFilter": false
	            });
			 $('#rpSumError').DataTable({
	                "oLanguage" : {
	                    "sLengthMenu" : "Hiển thị" + "_MENU_" + "bản ghi",
	                    "sZeroRecords" : " ",
	                    "sInfo" : "Hiển thị" + " _START_ " + "đến" + " _END_ " + "của" + " _TOTAL_ " + "bản ghi",
	                    "sInfoEmpty" : "Hiển thị" + " " + "từ" + " 0 " + "tới" + " 0 " + "trên tổng số" + " 0 " + "bản ghi",
	                    "sInfoFiltered" : "( " + "Đã lọc từ" + " _MAX_ " + "tổng số bản ghi" + " )",
	                    "oPaginate" : {
			                "sFirst" :"<i class='fa fa-fast-backward'></i>",
			                "sLast" :"<i class='fa fa-fast-forward'></i>",
			                "sPrevious":"<i class='fa fa-backward'></i>",
			                "sNext" : "<i class='fa fa-forward'></i>"
		            },
	                    "sSearch" : "Từ khóa"
	                },
	                "bDestroy" : false
	            });
			 $('#rpSumBranchError').DataTable({
	                "oLanguage" : {
	                    "sLengthMenu" : "Hiển thị" + "_MENU_" + "bản ghi",
	                    "sZeroRecords" : " ",
	                    "sInfo" : "Hiển thị" + " _START_ " + "đến" + " _END_ " + "của" + " _TOTAL_ " + "bản ghi",
	                    "sInfoEmpty" : "Hiển thị" + " " + "từ" + " 0 " + "tới" + " 0 " + "trên tổng số" + " 0 " + "bản ghi",
	                    "sInfoFiltered" : "( " + "Đã lọc từ" + " _MAX_ " + "tổng số bản ghi" + " )",
	                    "oPaginate" : {
			                "sFirst" :"<i class='fa fa-fast-backward'></i>",
			                "sLast" :"<i class='fa fa-fast-forward'></i>",
			                "sPrevious":"<i class='fa fa-backward'></i>",
			                "sNext" : "<i class='fa fa-forward'></i>"
		            },
	                    "sSearch" : "Từ khóa"
	                },
	                "bDestroy" : false
	            });
			 
			$("#tongDuNo").hide();
			$("#tongGiaTriDauTu").hide();
			$("#tongTaiSan").hide();
			$("#tongSoTienCanThanhToan").hide();
			$("#tongSoTraiPhieu").hide();
			
			var code = $('#reportCode').val();
			switch(code) {
			  case "K3122A":
			  case "K3121A":
			  case "K3111A":
			  case "K3112A":
			  case "CD.K31X1A":
			  case "CD.K31X2A":
				  $("#tongDuNo").show();
			    break;
			  case "K3113":
			  case "K3123":
			  case "CD.31X3":
				  $("#tongDuNo").show();
			    break;
			  case "K3223":
			  case "K3213":
			  case "CD.K32":
				  $("#tongDuNo").show();
				break;
			  case "K3333":
			  case "CD.K333":
				  $("#tongSoTienCanThanhToan").show();
				break;
			  case "K4011":
			  case "K4012":
			  case "K4013":
			  case "K4021":
			  case "K4022":
			  case "K4023":
				  $("#tongTaiSan").show();
				break;
			  case "K5011":
			  case "K5012":
			  case "K5013":
			  case "CD.K5":
				  $("#tongGiaTriDauTu").show();
				  $("#tongSoTraiPhieu").show();
				break;
			  case "T02G1":
			  case "T02G2":
			  case "CD.T02G1":
			  case "CD.T02G2":
				  $("#tongDuNo").show();
				break;
			  case "K1011":
			  case "K1012":
			  case "K1013":
			  case "K1021":
			  case "K1022":
			  case "K1023":
			  case "K3111":
			  case "K3112":
			  case "K3121":
			  case "K3122":
				break;
			}
		});
		var arr;
		function viewKhachHangError(val){
			if(val == "1")
				$("#modal-title").text("Danh s\u00E1ch kh\u00E1ch h\u00E0ng l\u1ED7i nh\u00F3m n\u1EE3");
			else if(val == "2")
				$("#modal-title").text("Danh s\u00E1ch kh\u00E1ch h\u00E0ng l\u1ED7i lo\u1EA1i vay");
			var url = window.location.href;
			var tmp = url.split('=');
			var reportId = tmp[1];
			var urlTmp = tmp[0].split('?');
			$.ajax({
                url:urlTmp[0] + '?method=listKhachHangError',
               	data: {
               		reportId : reportId,
               		p_type	 : val
               	},
                method: 'GET',
                async : false,
                success: function(data){
                	console.log(JSON.parse(data));
                	$('#tableError tbody').empty();
                	arr = JSON.parse(data);
                	for ( var item in arr) {
                		var row = '<tr>';
						row += '<td>' + arr[item][0] +'</td>';
						row += '<td>' + arr[item][1] +'</td>';
						row += '<td>' + arr[item][2] +'</td>';
						row += '<td>' + arr[item][3] +'</td>';
						row += '</tr>';
	                	$("#tableError tbody").append(row);
					}
                	$('#modal-view-error').modal('show');
                }
            });
		}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>