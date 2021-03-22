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
<!-- Modal Bo sung dich vu -->
<div class="modal fade" id="modal-view-file" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">

				<h6 class="modal-title" id="modal-title">Gửi tệp phi cấu trúc</h6>
				<button type="button" class="close" data-dismiss="modal"
					style="color: #fff;">&times;</button>
			</div>
			<div class="modal-body">
				<div class="row">
					<%-- <div class="Row">
						<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">Đơn vị</div>
						<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							<select id="selectTCTD">
								<option value="">- Chọn -</option>
								<c:forEach items="#{lstPartner}" var="item">
									<option value="${item.code}">
										<c:out value="${item.code}" /> -
										<c:out value="${item.name}" />
									</option>
								</c:forEach>
							</select>
						</div>

					</div> --%>
					<div class="Row">
						<div class="label-static col-md-3 col-lg-3 col-sm-12 col-xs-12">Chọn tệp</div>
						<div class="col-md-9 col-lg-9 col-sm-12 col-xs-12">
							<input type="file" id="file-select" class="file-modal"
								name="inputFile" title="Chọn tệp" />
						</div>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn blue" onclick="uploadFile();">Chọn</button>
					<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
				</div>
				<spring:url var="sendReportAction" value="/rpFileUnstructured/uploadFile"></spring:url>
			</div>
		</div>
	</div>
	</div>
	<tiles:insertDefinition name="catalog">
		<tiles:putAttribute name="title"
			value="Theo dõi tệp phi cấu trúc" />
		<tiles:putAttribute name="formInf">
			<spring:url value="/rpFileUnstructured" var="formAction" />
			<c:set var="commandName" scope="request" value="formDataModelAttr" />
		</tiles:putAttribute>

		<form:form cssClass="form-horizontal" id="theForm"
			enctype="multipart/form-data" modelAttribute="${commandName}"
			method="post" action='${formAction}'>
			<tiles:putAttribute name="catGrid">
				<div id="divGrid" align="left">
					<div class="row search-style">
						<div class="Table" id="divSearchInf">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Theo dõi tệp phi cấu trúc</h1>
							</div>
							<div class="Row">
								<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên
									file</div>
								<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
									<form:input path="keyword_code" cssClass="form-control"></form:input>
								</div>
								<div class="Empty"></div>
								<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn vị</div>
								<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
									<%-- <form:input path="keyword_name" ></form:input> --%>
									<form:select class="form-control" path="keyword_name"
										id="tctdId" title="Chọn đơn vị">
										<option value="">- Chọn -</option>
										<c:forEach items="#{dsDonVi}" var="item">
											<option value="${item.id}">
												<c:out value="${item.code}" /> -
												<c:out value="${item.name}" />
											</option>
										</c:forEach>
									</form:select>
								</div>
							</div>
							<div class="Row">
								<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày</div>
								<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12" style="    display: flex;">
								<form:input path="fromDate" cssClass="form-control input_date" cssStyle="border-bottom-right-radius: 0px;border-top-right-radius: 0px;"></form:input>
								<!-- <input id="fromDate" class="form-control input_date" style="border-bottom-right-radius: 0px;border-top-right-radius: 0px;"/> -->
								<span class="input-group-text" id="basic-addon3" style="padding: 2px 10px;font-size: 13px;border-radius: 1px !important;font-weight: bold;margin-top: -6px;height: 30px;">đến</span>
								<form:input path="toDate" cssClass="form-control input_date" cssStyle="border-bottom-left-radius: 0px;border-top-left-radius: 0px;"></form:input>
								<!-- <input id="toDate" class="form-control input_date" style="border-bottom-left-radius: 0px;border-top-left-radius: 0px;"/> -->
								</div>
							</div>
							
							<div class="divaction" align="center">
								<input class="btn blue" type="button" onclick="findData();"
									value="Tìm kiếm" /> <input class="btn blue" type="button"
									onclick="uploadFilePH();" value="Gửi tệp Phi cấu trúc" />
								<input class="btn blue" type="button" onclick="exportExcel();"
									value="Xuất Excel" /> 
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
				function initParam(tblCfg) {
					tblCfg.bFilter = false;
					tblCfg.aoColumns = [ {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'STT'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Tên file'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Thời gian thực hiện'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Tổ chức tín dụng'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Người gửi'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Thời gian lấy về gần nhất'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Dung lượng file'
					}, {
						"sClass" : "left",
						"bSortable" : false,
						"sTitle" : 'Tải file'
					} ];
				}

				$(document).ready(function() {
					$(".btnDtAdd, .btnDtDelete").hide();
					$(".input_date").datepicker({
						dateFormat : 'dd/mm/yy',
						changeMonth : false,
						changeYear : false,
						showButtonPanel : false,
						onSelect: function(selected,evnt) {
					         findByDate(selected);
					    }
					});
				});

				function taiFile(id) {
					window.open($('#theForm').attr('action')
							+ '?method=downloadFileFtp&id=' + id);
				}

				function uploadFilePH() {
					$('#selectTCTD').val('');
					$('#selectTCTD').select2();
					$('#file-select').val('');
					$('#modal-view-file').modal('show');
				}

				function uploadFile() {
					var files = document.getElementById('file-select').files;
			      if($("#selectTCTD").val() == "" || $("#selectTCTD").val() == null){
			    	  alert('Chưa chọn TCTD gửi file tra soát');
			          return;
			      }else if(files.length ==0 ){
			          alert('Chưa chọn file báo cáo!');
			          return;
			      }
			          var formData = new FormData();
			          formData.append("fileName", files[0].name);
			          formData.append("inputFile", files[0]);
			          formData.append("tokenIdKey", $('#tokenIdKey').val());    
			          formData.append("tokenId", $('#tokenId').val()); 
			          formData.append("tctd", $("#selectTCTD").val());
			          console.log("formData", formData);
			          
			          var xhr = new XMLHttpRequest();
			          xhr.open('POST','${sendReportAction}', true);
			          
			          $.loader({
			              className:"blue-with-image-2"
			          });   
			          xhr.onload = function () {
			            $.loader("close");
			            if (xhr.readyState == 4 && xhr.status == 200) {
			              if(xhr.responseText=='')
			              {
			                  alert('Thực hiện thành công!', function(){
			                	  findData();
			                  });               
			              }else{
			                  alert(xhr.responseText)
			              }
			              
			            } else {
			              alert('Import không thành công');
			              
			            }
			          };
			          xhr.send(formData);
			          $('#modal-view-file').modal('hide');
			          
				}
				function exportExcel() {
					var partnerCode = $('select[name="keyword_name"]').val();
					var fileName = $('input[name="keyword_code"]').val();
					var cicOwner = $("#cicOwner").val();
					var fromDate = $("#fromDate").val();
					var toDate = $("#toDate").val();
					window.open($('#theForm').attr('action') + '?method=ExportFileExcel&partnerCode='+partnerCode+'&fileName='+fileName+'&cicOwner='+cicOwner+'&fromDate='+fromDate+'&toDate='+toDate);
				}
			</script>
		</tiles:putAttribute>
	</tiles:insertDefinition>