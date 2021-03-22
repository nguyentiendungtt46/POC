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
	<tiles:putAttribute name="title" value="Hỏi tin theo file" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/qnaInFile" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm"
		enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id="divSearchInf">
						<div class="row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Hỏi tin theo file</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn vị</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<%-- <form:input path="keyword_code" id="keyword_code"></form:input> --%>
								<form:select path="keyword_code">
								<option value=""> -Chọn-</option>
								<c:forEach items="#{dsDonVi}" var="item">
											<option value="${item.id}">
												<c:out value="${item.code}" /> -
												<c:out value="${item.name}" />
											</option>
										</c:forEach>
								</form:select>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên
								file</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="keyword_name" id="keyword_name"></form:input>
							</div>
						</div>
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">(Ngày hỏi) Từ</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<!-- <input 
							class="form-control qnaInFile_start_date" 
							title="Ngày bắt đầu không được để trống" /> -->
							<form:input path="fromDate" id="fromDate" cssClass="qnaInFile_start_date"></form:input>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến ngày</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
							<!-- <input 
							class="form-control qnaInFile_start_date" 
							title="Ngày bắt đầu không được để trống" />-->
							<form:input path="toDate" id="toDate" cssClass="qnaInFile_start_date"></form:input>
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

						<!-- <div align="center" class="HeaderText">&#8203;</div> -->
						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="findData();"
								value="Tìm kiếm" />
							<input class="btn blue" type="button" onclick="exportExcel();"
								value="Xuất excel" />
						</div>
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>


			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="qnaInFile.id" id="id" />
			<form:hidden path="qnaInFile.tenfile" cssClass="qnaInFile_tenfile" />
			<div class="box-custom">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Thông tin chi tiết câu hỏi theo File</h1>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Tổ chức tín dụng<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="qnaInFile.tctd" cssClass="required selectTCTD"
							title="Tổ chức tín dụng"></form:select>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Ngày bắt đầu <font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<input 
							class="form-control qnaInFile_start_date" placeholder="Chọn ngày"
							title="Ngày bắt đầu không được để trống" />
							<!-- path="qnaInFile.ngaybatdau" -->
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						File câu hỏi</div>
					<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
						<div class="custom-file">
							<input type="file" class="custom-file-input"
								id="inputGroupFile01" aria-describedby="inputGroupFileAddon01">
							<label class="custom-file-label lbFilename" for="inputGroupFile01">.../</label>
						</div>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Chu kỳ</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="qnaInFile.chuky">
							<form:option value="1">Ngày</form:option>
							<form:option value="2">Tuần</form:option>
							<form:option value="3">Tháng</form:option>
							<form:option value="4">Quý</form:option>
						</form:select>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			var createNew = false;
			var tableIp, tableAccount = null;
			function initParam(tblCfg) {
				tblCfg.bFilter = false;
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã đơn vị'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã sản phẩm'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên file'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Người hỏi tin'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Thời điểm hỏi'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Trạng thái'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mô tả lỗi'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Ngày trả lời'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Dung lượng File'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Hành động'
				} ];
			}

			$('input[type="file"]').change(function(e){
	            var fileName = e.target.files[0].name;
	            $('.lbFilename').text(fileName);
	            $('.qnaInFile_tenfile').val(fileName);
	        });
			
			function taiFile(id){
				window.open($('#theForm').attr('action') + '?method=downloadFileQuestion&id=' + id);
			}
			
			function taiFileTl(id){
				window.open($('#theForm').attr('action') + '?method=downloadFileAnswer&id=' + id);
			}
			
			function taiFileTlEr(id){
				window.open($('#theForm').attr('action') + '?method=downloadFileAnswerEr&id=' + id);
			}
			
			function beforeEdit() {

			}

			$(document).ready(function() {
				$(".btnDtAdd, .btnDtDelete").hide();
				$(".qnaInFile_start_date").datepicker({
					dateFormat : 'dd/mm/yy',
					changeMonth : false,
					changeYear : false,
					showButtonPanel : false
				});
				loadTCTD();
				 $('.lbFilename').text(".../");
		            $('.qnaInFile_tenfile').val("");
			});
			
			function loadTCTD(){
				$.ajax({
					url : $('#theForm').attr('action')
							+ '?method=getTCTD',
					data : {},
					method : 'GET',
					success : function(_result, status, xhr) {
						$.each(_result, function(i, item) {
							$('.selectTCTD').append(
									$('<option>', {
										value : item.id,
										text : item.name
									}));
						});
					}
				});
			}

			function editPartner(id) {
				$.loader({
					className : "blue-with-image-2"
				});
				clearDiv('divDetail');
				var tokenIdKey = $('#tokenIdKey').val();
				var tokenId = $('#tokenId').val();
				$.getJSON(editUrl, {
					"id" : id,
					"tokenIdKey" : tokenIdKey,
					"tokenId" : tokenId
				}).done(function(res) {
					initDataEdit(res);
					$.loader('close');

				});
			}
			
			function exportExcel() {
				var tuNgay = $('input[name="fromDate"]').val();
				var denNgay = $('input[name="toDate"]').val();
				var partnerCode = $('select[name="keyword_code"]').val();
				var status = $('select[name="status"]').val();
				var fileName = $('input[name="keyword_name"]').val();
				
				window.open($('#theForm').attr('action') + '?method=ExportFileExcel&tuNgay='+tuNgay+'&denNgay='+denNgay+
						'&partnerCode='+partnerCode+'&fileName='+fileName+'&status='+status);
			}

			// fill data to table partner connect ip, username, password
			function afterEdit(id, res) {

			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>