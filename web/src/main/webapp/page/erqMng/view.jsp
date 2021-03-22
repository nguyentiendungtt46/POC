<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="<spring:url value="/js/autodoc_input.js" />"></script>
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Quản lý ứng viên" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/erqMng" var="formAction" />
		<spring:url value="/erqMng/upload" var="uploadFile" />
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
								<h1>Quản lý ứng viên</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên
								ứng viên</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="name" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số
								CMT/ Hộ chiếu</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="cardId" />
							</div>
						</div>
						<div class="row">
							<div class="row title-page" style="adding-bottom: 20px;"></div>

							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trạng
								thái</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select class="form-control" path="status"
									title="Chọn đơn vị">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstAppflow}" var="item">
										<option value="${item.id}">
											<c:out value="${item.code}" /> -
											<c:out value="${item.name}" />
										</option>
									</c:forEach>
								</form:select>
							</div>
						</div>
						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="findData();"
								value="Tìm kiếm" />
							<c:if test="${add}">
								<input class="btnDtAdd btn blue" type="button" id="btnDtAdd"
									onclick="addNew();" value="Thêm mới" />
							</c:if>
						</div>
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
			</div>

		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="erqInf.id" id="id" />
			<form:hidden path="erqInf.templateId" id="templateId" />
			<div class="box-custom">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Thông tin chi tiết</h1>

				</div>
				<div class="row">
					<input class="btnDtUpload btn blue" type="button" id="btnDtAdd"
						onclick="upload();" value="upload thông tin ứng viên" />
					<input class="btnHistory btn blue" type="button" id="btnHistory"
						onclick="history();" value="Lịch sử" />
				</div>
				<c:out value="${testHTML}" escapeXml="false"></c:out>
			</div>

		</tiles:putAttribute>
		

	</form:form>

	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function upload() {
				$('#file-select-mt').val('');
				$('#file-select-ms').val('');
				$('#modal_upload_cmt').modal('show');
			}

			function upload_CMT() {
				$.loader({
					className : "blue-with-image-2"
				});
				var files = [];
				$('.fileCMT').each(function() {
					files.push($(this)[0].files);
				});
				var filesMT = document.getElementById('file-select-mt').files;
				var filesMS = document.getElementById('file-select-ms').files;
				if (filesMT.length == 0) {
					alert('Chưa chọn File Mặt trước chứng minh thư!');
					return;
				}
				if (filesMS.length == 0) {
					alert('Chưa chọn File Mặt sau chứng minh thư!');
					return;
				}
				var formData = new FormData();
				formData.append("fileMT", filesMT[0]);
				formData.append("fileMS", filesMS[0]);
				formData.append("tokenIdKey", $('#tokenIdKey').val());
				formData.append("tokenId", $('#tokenId').val());
				var xhr = new XMLHttpRequest();
				xhr.open('POST', 'erqMng/uploadCMT', true);

				xhr.onload = function() {
					if (xhr.readyState == 4 && xhr.status == 200) {
						if (xhr.responseText == '') {
							alert('Thực hiện thành công!', function() {
							});
							$('#modal_upload_cmt').modal('hide');
						} else {
							var result = JSON.parse(xhr.responseText);
							console.log(result);
							var _dataMT = result.objMT[0];
							$('input[name="erqInf.name"]').val(_dataMT.name);
							$('input[name="erqInf.dateOfBirthdayStr"]').val(
									_dataMT.dob);
							$('input[name="erqInf.cardId"]').val(_dataMT.id);
							$('input[name="erqInf.permanentAddress"]').val(
									_dataMT.address);
							$('input[name="erqInf.placeOfBirth"]').val(
									_dataMT.home);
							var _dataMS = result.objMS[0];
							$('input[name="erqInf.nation"]').val(
									_dataMS.ethnicity);
							$('input[name="erqInf.dateCardRangeStr"]').val(
									_dataMS.issue_date);
							$('input[name="erqInf.placeCard"]').val(
									_dataMS.issue_loc);
							$('input[name="erqInf.nation"]').val(
									_dataMS.ethnicity);
							$('#modal_upload_cmt').modal('hide');
						}
						$.loader("close");
					} else {
						$.loader("close");
						alert('Tải lên không thành công');
						$('#modal_upload_cmt').modal('hide');
					}
				};
				xhr.send(formData);

			}

			function afterEdit(id, res) {
				if (res.appFlow.first) {
					$('#btnSave, #btnDel').css('display', 'inline');
				} else {
					$('#btnSave, #btnDel').css('display', 'none');
				}

				$('#btnNext')
						.css(
								'display',
								((res.appFlow.owner && res.appFlow.first) || (res.appFlow.right && !res.appFlow.last)) ? 'inline'
										: 'none');
				$('#btnNext').val(res.appFlow.action);

				$('#btnPrevious').css(
						'display',
						(res.appFlow.right && !res.appFlow.first) ? 'inline'
								: 'none');

			}
			function defaultValue() {
				$('#btnSave').css('display', 'inline');
				tblerqInf_pocFiles_null.deleteAllRow();
				tblerqInf_pocInterViewRs_null.deleteAllRow();
				defaultObject();

			}
			function beforeEdit(res) {
				if (res.pocInterViewRs != undefined)
					tblerqInf_pocInterViewRs_null
							.resize(res.pocInterViewRs.length);
				if (res.pocFiles != undefined)
					tblerqInf_pocFiles_null.resize(res.pocFiles.length);
			}

			function initParam(tblCfg) {
				tblCfg.bFilter = false;
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Cán bộ khởi tạo'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên ứng viên'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Ngày sinh'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Nơi sinh'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Email'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Số CMT/Hộ chiếu'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Số CMT/Hộ chiếu khác'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Quốc tịch'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Thời điểm khởi tạo'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Trạng thái'
				} ];
			}

			$(document).ready(function() {
				$('.btnDtDelete').hide();
				//	 initTableSerProduct();
			});
			function upload_onclick() {
				var files = document.getElementById('file-select').files;
				if (files.length == 0) {
					alert('Bạn chưa chọn file tải lên');
					return;
				}
				if ($('#id').val().trim().length == 0) {
					alert('Cần lưu biểu mẫu trước');
					return;
				}
				var formData = new FormData();
				var file = files[0];
				formData.append("inputFile", file);
				formData.append("fileName", file.name);
				formData.append("fileNamePath", fileNamePath);
				formData.append("idRecordUpload", idRecordUpload);
				formData.append("filePath", $('#filePath').val());
				formData.append("tokenIdKey", $('#tokenIdKey').val());
				formData.append("tokenId", $('#tokenId').val());
				$.loader({
					className : "blue-with-image-2"
				});
				var xhr = new XMLHttpRequest();
				xhr.open('POST',' ${uploadFile}', true);
				xhr.onload = function() {
					$.loader("close");
					if (xhr.readyState == 4 && xhr.status == 200) {
						$('#modal_upload_file').modal('hide');
						if (xhr.responseText.trim().length == 0) {
							alert("Tải file thành công", function() {
								$('#fileName').val(files[0].name);
								$('#downloadFile').css('display', 'block');
							});
							edit($('#id').val());
						} else {
							alert(xhr.responseText);
						}

					} else {
						alert(xhr.responseText);

					}
				};
				xhr.send(formData);
			}
			
			function history() {
				var id = $('#id').val();
				if (id == null || id === '') {
					alert('Chưa có thông tin lịch sử');
				}
				$.ajax({
					url : $('#theForm').attr('action')
							+ '?method=getHistory&id='+id,
					data : {cifno :""},
					method : 'GET',
					success : function(_result, status, xhr) {
						$('#table-history-user').DataTable().destroy();
						$('#table-history-user tbody').empty();
						var xxx = JSON.parse(_result);
						for (var i=0 ; i < xxx.length ; i++ ) {
							$('#table-history-user > tbody:last-child').append(
						            '<tr>'
							            +'<td> '+xxx[i].name+'</td>'
							            +'<td>'+xxx[i].status+'</td>'
							            +'<td>'+xxx[i].date+'</td>'
							            +'<td>'+xxx[i].note+'</td>'
						            +'</tr>');
						}
						
						$('#table-history-user').dataTable({
							"oLanguage" : {
								"oPaginate" : {
									"sFirst" : "<<", // This is the link to the first page
									"sPrevious" : "<<", // This is the link to the previous page
									"sNext" : ">>", // This is the link to the next page
									"sLast" : ">>",
									
								},
								"sLengthMenu": "Hiển thị _MENU_ bản ghi",
					            "sZeroRecords": " ",
					            "sInfo": "Hiển thị _START_ tới _END_ của _TOTAL_ bản ghi",
					            "sInfoEmpty": "Hiển thị từ 0 tới 0 trên tổng số 0 bản ghi",
					            "sInfoFiltered": "(�?ã l�?c từ _MAX_ tổng số bản ghi)"
							}
						});
						$('#history_modal_dialog').modal('show');
					}
				});
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>