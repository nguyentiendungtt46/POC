<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="frwk.utils.ApplicationContext"%>
<%@ page import="entity.frwk.SysUsers"%>
<%
	ApplicationContext appContext = (ApplicationContext) request.getSession()
			.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
	SysUsers user = null;
	if (appContext != null)
		user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
%>
<div id="header" class="clearfix"
	style="background-color: #fff; box-shadow: 0 0 2px #6d6d6d; position: fixed; width: 100%; top: 0;">
	<div class="row" style="padding: 5px 0px 0px 10px;">
		<div class="col-md-2 col-sm-12"
			style="text-align: center; padding: 5px 0 0 0;">
			<a href="<spring:url value='/index'/>"> <img alt="logo"
				src="<spring:url value="/images/mbal_logo.png"/>"
				style="width: 135px; padding-bottom: 5px;" />
			</a>
		</div>
		<div class="col-md-10" style="padding: 0px 0px 0px 10px;">
			<div>
				<h3
					style="font-size: 18px; color: #141ed2; font-weight: 700; margin-bottom: 0px; text-transform: uppercase; padding-top: 10px;">
					Bảo Hiểm Nhân Thọ MB Ageas Life</h3>
				<span style="font-size: 12px; font-weight: 500; color: #dd0071;">Đia
					chỉ: Tòa nhà Vinaconex, Tầng 4, Số 34 Láng Hạ, P. Láng Hạ, Q. Đống
					Đa, Hà Nội. <br />Điện thoại: 024 2229 8888 | Email:
					dvkh@mbageas.life
				</span>
			</div>
			<div class="row" style="">
				<%-- <img alt="alt" src="<spring:url value="/images/69e086a1da1c6fd55b0bbab4fb4dc1e8.svg"/>" style="width: 300px;" /> --%>
				<ul class="nav navbar-nav account"
					style="position: fixed; right: 0px; display: flex; flex-direction: row; top: 0; padding: 5px; background: #f8f9fa; border-bottom-left-radius: 5px; z-index: 1000; font-size: 13px; box-shadow: 0px 0px 3px 1px #bfbebe;">
					<li><a data-toggle="modal" data-target="#modalChangePass"
						href='#' style="font-size: 13px"><i class="fa fa-expeditedssl"
							style="margin-top: 2px; float: left;"> </i><%=user.getUsername()%>
					</a> &nbsp;|&nbsp;
					<li><a class="no-caret" href="<spring:url value='/logout'/>"
						style="font-size: 13px">Thoát&nbsp<i class="fa fa-sign-out"></i></a></li>
				</ul>
			</div>

		</div>
	</div>
	<div class="row" style="padding-bottom: 0px;">
		<jsp:include page="/page/base/navbar.jsp" />
	</div>
</div>

<div id="modalChangePass" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content form-group">
			<div class="modal-header">
				<h4 class="modal-title">Đổi mật khẩu</h4>
			</div>
			<div class="modal-body">
				<form action='login.action' id='frmChangePass'>
					<div class='Table'>
						<div class='Row' style="height: 40px;">
							<div class='col-md-4 col-lg-4 col-sm-12 col-xs-12'
								style="height: 40px;">Mật khẩu cũ</div>
							<div class='col-md-6 col-lg-6 col-sm-12 col-xs-12'
								style="height: 40px;">
								<input id='oldPassWord' type='password' style='width: 100%;'
									class="inputModal" />
							</div>
						</div>
						<div class='Row' style="height: 40px;">
							<div class='col-md-4 col-lg-4 col-sm-12 col-xs-12'
								style="height: 40px;">Mật khẩu mới</div>
							<div class='col-md-6 col-lg-6 col-sm-12 col-xs-12'
								style="height: 40px;">
								<input id='newPassWord' style='width: 100%;' type='password'
									class="inputModal" />
							</div>
						</div>
						<div class='Row' style="height: 40px;">
							<div class='col-md-4 col-lg-4 col-sm-12 col-xs-12'
								style="height: 40px;">Xác nhận mật khẩu</div>
							<div class='col-md-6 col-lg-6 col-sm-12 col-xs-12'
								style="height: 40px;">
								<input id='confirmPassWord' style='width: 100%;' type='password'
									class="inputModal" />
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn blue"
					onclick="ChangePasswordSave();">Lưu</button>
				<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
			</div>
		</div>
	</div>
</div>

<div id="modal_upload_file" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">&times;</button>
				<h4 class="modal-title">Tải file</h4>
			</div>
			<div class="modal-body">
				<form id="file-form" action="handler.php" method="POST"
					enctype="multipart/form-data">
					<div class='Table'>
						<div class='Row'>
							<div class='Span12Cell'>
								<input type="file" id="file-select" name="inputFile" />
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<a class="btn blue" onclick="upload_onclick();"><i
					class="fa fa-upload"></i>Tải lên</a> <a class="btn default"
					data-dismiss="modal"><i class="fa fa-sign-out"></i>Thoát</a>
			</div>
		</div>
	</div>
</div>
<div id="modal_upload_cmt" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Tải file</h4>
			</div>
			<div class="modal-body">
				<form id="file-form" action="handler.php" method="POST"
					enctype="multipart/form-data">
					<div class='Table'>
						<div class='Row'>
							<div class='Span12Cell'>
								<label for="file-select" style="width: 100px;">Mặt trước
									CMT</label> <input type="file" id="file-select-mt" name="inputFileMT"
									class="fileCMT" style="font-size: 13px;" />
							</div>
							<div class='Span12Cell'>
								<label for="file-select" style="width: 100px;">Mặt sau
									CMT</label> <input type="file" id="file-select-ms" name="inputFileMS"
									class="fileCMT" style="font-size: 13px;" />
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<a class="btn blue" onclick="upload_CMT();"><i
					class="fa fa-upload"></i>Tải lên</a> <a class="btn gray"
					data-dismiss="modal"><i class="fa fa-sign-out"></i>Thoát</a>
			</div>
		</div>
	</div>
</div>

<div id="upload_modal_dialog" class="modal fade" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">TCTD gửi file</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form id="file-form" action="handler.php" method="POST"
					enctype="multipart/form-data">
					<div class='Table'>
						<div class='Row'>
							<div class='Span12Cell'>
								<label for="file-select" style="width: 100px;">File báo
									cáo</label> <input type="file" id="file-select" class="file-modal"
									name="inputFile" />
							</div>
							<div class='Span12Cell'>
								<label for="file-select" style="width: 100px;">File đính
									kèm</label> <input type="file" id="attFile-select" class="file-modal"
									name="attFile" />
							</div>
						</div>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<a class="btn blue" onclick="import_onclick();"><i
					class="fa fa-upload"></i>Upload</a> <a class="btn gray"
					data-dismiss="modal"><i class="fa fa-sign-out"></i>Thoát</a>
			</div>
		</div>
	</div>
</div>
<div id="history_modal_dialog" class="modal fade" role="dialog">
	<div class="modal-dialog modal-lg">
		<!-- Modal content-->
		<div class="modal-content">
			<div class="modal-header">
				<h4 class="modal-title">Lịch sử ứng viên</h4>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<form id="file-form" action="handler.php" method="POST"
					enctype="multipart/form-data">
					<div class="table-responsive" >
						<table class="table table-bordered" id="table-history-user">
							<thead>
								<tr>
									<th width="300px">Người thực hiện</th>
									<th width="150px">Trạng thái</th>
									<th width="100px">Ngày</th>
									<th width="350px">Ghi chú</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</form>
			</div>
			<div class="modal-footer">
				<a class="btn gray"
					data-dismiss="modal"><i class="fa fa-sign-out"></i>Thoát</a>
			</div>
		</div>
	</div>
</div>
<%--<spring:url var="sendReportAction" value="/test"><spring:param name="method" value="sendReport"></spring:param></spring:url> --%>
<spring:url var="sendReportAction" value="/test/upload"></spring:url>
<spring:url var="sendReportAttAction" value="/test/uploadAtt"></spring:url>

<script type="text/javascript">
	function sendReport() {
		$('#file-select').val('');
		$('#attFile-select').val('');
		$('#upload_modal_dialog').modal();
	}
	function import_onclick() {
		var files = [];
		$('.file-modal').each(function() {
			files.push($(this)[0].files);
		});
		var files = document.getElementById('file-select').files;
		var attFiles = document.getElementById('attFile-select').files;
		if (files.length == 0) {
			alert('Chưa chọn file báo cáo!');
			return;
		}
		var formData = new FormData();
		formData.append("fileName", files[0].name);
		formData.append("inputFile", files[0]);
		formData.append("tokenIdKey", $('#tokenIdKey').val());
		formData.append("tokenId", $('#tokenId').val());
		var xhr = new XMLHttpRequest();
		if (attFiles.length > 0) {
			formData.append("attFileName", attFiles[0].name);
			formData.append("attInputFile", attFiles[0]);
			xhr.open('POST', '${sendReportAttAction}', true);
		} else
			xhr.open('POST', '${sendReportAction}', true);

		console.log("formData", formData);
		$.loader({
			className : "blue-with-image-2"
		});

		xhr.onload = function() {
			$.loader("close");
			if (xhr.readyState == 4 && xhr.status == 200) {
				if (xhr.responseText == '') {
					alert('Thực hiện thành công!', function() {
					});
				} else {
					alert(xhr.responseText)
				}

			} else {
				alert('Import không thành công');

			}
		};
		xhr.send(formData);

	}
</script>