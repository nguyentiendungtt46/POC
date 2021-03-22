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

<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Báo cáo danh sách TCTD vi phạm" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/reportSecurity" var="formAction" />
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
							<h1>Báo cáo danh sách TCTD vi phạm</h1>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								TCTD</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select class="form-control" path="branchCode"
									title="Chọn đơn vị">
									<option value="">- Chọn -</option>
									<c:forEach items="#{dsDonVi}" var="item">
										<option value="${item.code}">
											<c:out value="${item.code}" /> -
											<c:out value="${item.name}" />
										</option>
									</c:forEach>
								</form:select>
							</div>
							<div class="Empty"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã chính sách vi phạm</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="violateCode">
									<option value="">- Chọn -</option>
									<option value="CMM_002">CMM_002 - IP không hợp lệ</option>
									<option value="CMM_003">CMM_003 - User không có quyền sử dụng dịch vụ hoặc sản phẩm</option>
									<option value="CMM_004">CMM_004 - Vi phạm thời gian giữa 2 lần sử dụng dịch vụ</option>
									<option value="CMM_006">CMM_006 - Dịch vụ đang đóng</option>
									<option value="CMM_007">CMM_007 - Đăng nhập lần đầu tiên cần thay đổi mật khẩu để tiếp tục sử dụng dịch vụ</option>
									<option value="CMM_008">CMM_008 - Dung lượng dữ liệu vượt quá mức cho phép</option>
								</form:select>
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Từ ngày</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="fromDate" name="fromDate" cssClass="currentDate required" value="${startDate}"></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến ngày</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <form:input path="toDate" name="toDate" cssClass="currentDate required" value="${endDate}"></form:input>
		                    </div>
						</div>
						
						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="exportExcel();"
								value="Export" />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			$(document).ready(function() {
				$(".btnDtDelete").hide();
				$(".btnDtAdd").hide();
				$(".currentDate").datepicker({
						dateFormat : 'dd/mm/yy',
						changeMonth : false,
						changeYear : false,
						showButtonPanel : false
				});
			});

			function exportExcel() {
				var branchCode = $('#branchCode').val();
				var violateCode = $('#violateCode').val();
				var fromDate = $('#fromDate').val();
				var toDate = $('#toDate').val();
				if (fromDate == null || fromDate == '' || toDate == null || toDate == '') {
					alert('Bắt buộc nhập từ ngày, đến ngày');
					return;
				}
				window.open($('#theForm').attr('action')
						+ '?method=exportExcel&branchCode=' + branchCode + '&violateCode=' + violateCode
						+ '&fromDate=' + fromDate + '&toDate=' + toDate);
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>