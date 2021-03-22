<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Danh sách phiếu hỏi tin" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/qnaInDetail" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>

	<form:form cssClass="form-horizontal" id="theForm"
		enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id="divSearchInf">
						<form:hidden path="qnaInMsId" id="qnaInMsId" />
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Danh sách phiếu hỏi tin</h1>
							</div>
						</div>

						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn vị</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:select path="branch" title="Chọn đơn vị">
									<option value="">- Chọn -</option>
									<c:forEach items="#{dsDonVi}" var="item">
										<option value="${item.id}">
											<c:out value="${item.code}" /> -
											<c:out value="${item.name}" />
										</option>
									</c:forEach>
								</form:select>
							</div>
							<div class="Empty"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời gian hỏi</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12" style="    display: flex;">
							<form:input path="ngaytaotu" cssClass="form-control input_date" cssStyle="border-bottom-right-radius: 0px;border-top-right-radius: 0px;"></form:input>
							<span class="input-group-text" id="basic-addon3" 
							style="padding: 2px 10px;font-size: 11px;border-radius: 1px !important;font-weight: bold;border-top: 1px solid #a99999;border-bottom: 1px solid #a99999;margin-top: -6px;height: 30px;">đến</span>
							<form:input path="ngaytaoden" cssClass="form-control input_date" cssStyle="border-bottom-left-radius: 0px;border-top-left-radius: 0px;"></form:input>
							</div>
						</div>

						<%-- <div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Thời
								gian hỏi từ</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="ngaytaotu" cssClass="toCurrentDate"></form:input>
							</div>
							<div class="Empty"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đến</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="ngaytaoden" cssClass="toCurrentDate"></form:input>
							</div>
						</div> --%>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã số
								phiếu</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="maphieu" id="maphieu"></form:input>
							</div>
							<div class="Empty"></div>
							<%-- <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã lô</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <form:input path="malo" id="malo"></form:input>
		                    </div> --%>
							<%-- <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã TCTD</div>
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
		                    </div> --%>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								CIC</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="macic" id="macic"></form:input>
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								khách hàng</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="makh" id="makh"></form:input>
							</div>
							<div class="Empty"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên
								khách hàng(CIF)</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="tenkh" id="tenkh"></form:input>
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số
								CMT/ DKKD</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="socmnd" id="socmnd"></form:input>
							</div>
							<div class="Empty"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								số thuế</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="masothue" id="masothue"></form:input>
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đã
								có kết quả</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:checkbox path="hasResult" />
							</div>

							<div class="Empty"></div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Lỗi
								tại CIC</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:checkbox path="cicErr" id="cicErr"></form:checkbox>
							</div>

						</div>

						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="findData();"
								value="Tìm kiếm" />
							<c:if test="${add}">
								<input class="btnDtAdd btn blue" type="button" id="btnDtAdd"
									onclick="addNew();" value="Tạo phiếu hỏi tin" />
							</c:if>
							<input class="btn blue" type="button" onclick="exportExcel();"
								value="Xuất Excel" />
						</div>
						<!-- <div align="center" class="HeaderText">&#8203;</div> -->

					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
			</div>

			<div class="modal fade" id="myModal" role="dialog">
				<div class="modal-dialog">
					<!-- Modal content-->
					<div class="modal-content form-group"
						style="width: 900px; margin-left: -200px;">
						<div class="modal-header">
							<h4 class="modal-title"
								style="font-size: 16px; font-weight: 500;"></h4>
						</div>
						<div class="modal-body question">
							<!-- <p>Some text in the modal.</p> -->
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default"
								data-dismiss="modal">Đóng</button>
						</div>
					</div>
				</div>
			</div>
			<div class="modal fade" id="modal-view-error" role="dialog">
				<div class="modal-dialog">
					<!-- Modal content-->
					<div class="modal-content" >
						<div class="modal-header">
							
							<h6 class="modal-title" id="modal-title">Danh sách lỗi Input</h6>
								<button type="button" class="close" data-dismiss="modal" style="color: #fff;">&times;</button>
						</div>
						<div class="modal-body">
							<div class="form-group">
								<div class="col-md-12">
					        		<table id="tableError" class="table table-bordered table-hover sticky-header-table " >
										<thead style="    text-transform: uppercase; background-color: #141ed2; color: #f2f0f0; font-weight: 500;">
											<tr>
												<td>Mã lỗi</td>
												<td>Mô tả lỗi</td>
											</tr>
										</thead>
										<tbody>
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
		<tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="qnaInDetail.id" id="id" />
			<div class="box-custom">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Thông tin phiếu hỏi tin</h1>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Loại sản phẩm<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="qnaInDetail.serviceProduct.id"
							cssClass="required" title="Không được để trống">
							<form:option value="">- Chọn -</form:option>
							<c:forEach items="#{lstServicePro}" var="item">
								<form:option value="${item.catProductId.id}">
									<c:out value="${item.catProductId.code}" /> -
										<c:out value="${item.catProductId.name}" />
								</form:option>
							</c:forEach>
						</form:select>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã số phiếu<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qnaInDetail.msphieu" readonly="true"
							placeholder="Hệ thống tự sinh"></form:input>
					</div>

				</div>

				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã Khách hàng(CIF)<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qnaInDetail.makh" cssClass="required"
							title="Không được để trống"></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Tên Khách hàng<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qnaInDetail.tenkh" cssClass="required"
							title="Không được để trống"></form:input>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã CIC<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qnaInDetail.macic" cssClass="required"
							title="Không được để trống"></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Số CMT/ DKKD<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qnaInDetail.socmt" cssClass="required"
							title="Không được để trống"></form:input>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Địa chỉ <font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qnaInDetail.diachi" cssClass="required"
							title="Không được để trống"></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã số thuế
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qnaInDetail.msthue" 
							title="Không được để trống"></form:input>
					</div>
				</div>
			</div>
			
		</tiles:putAttribute>
		<%-- <tiles:putAttribute name="catDetail" cascade="true">
	</tiles:putAttribute> --%>
	</form:form>

	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function initParam(tblCfg) {
				tblCfg.bFilter = false;
				/*
				ntdung: da tạo xong job, tan suat 10s, test = job hoac call tu console trinh duyet
				tblCfg.buttons = [ {
					text : 'Giả lập Job Hỏi tin S11A',
					className : 'btnVanTin btn blue',
					action : function(e, dt, node, config) {
						callServiceHoiTin("S11A");
					}
				}, {
					text : 'Giả lập Job Hỏi tin R14',
					className : 'btnVanTin btn blue',
					action : function(e, dt, node, config) {
						callServiceHoiTin("R14");
					}
				}, {
					text : 'Giả lập Job Vấn tin',
					className : 'btnVanTin btn blue',
					action : function(e, dt, node, config) {
						callServiceDsVanTin();
					}
				} ];
				
				*/
				tblCfg.buttons=[];
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã Phiếu'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã - Tên KH'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Mã CIC'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Số CMT/ DKKD'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Địa chỉ'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Chi nhánh'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Người hỏi'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Thời điểm hỏi'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Thời điểm gửi CIC'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Trạng thái'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Thời điểm CIC trả'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Chi tiết câu trả lời'
				} ];
				
			}

			$(document).ready(function() {
				$('#btnSave').attr('value', 'Hỏi tin');
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

			function chiTietCauHoi(id) {
				$.ajax({
					url : $('#theForm').attr('action')
							+ '&method=loadQuesAndAns&id=' + id
							+ '&type=question',
					method : 'GET',
					success : function(data) {
						$(".question").empty();
						$(".question").append(
								"<textarea class=\"form-control\" rows=\"20\">"
										+ data + "</textarea>");

						$(".modal-title").empty();
						$(".modal-title").append("Chi tiết câu hỏi");

					}
				});
			}

			function chiTietCauTraLoi(id) {
				var qnaInMsId = $('#qnaInMsId').val();
				var url = "";
				if (qnaInMsId == "") {
					url += '?method=loadQuesAndAns&id=' + id + '&type=answer';
				} else {
					url += '&method=loadQuesAndAns&id=' + id + '&type=answer';
				}
				console.log(url);
				$.ajax({
					url : $('#theForm').attr('action') + url,
					method : 'GET',
					success : function(data) {
						$(".question").empty();
						$('.question').jsonView(JSON.parse(data));
						$(".modal-title").empty();
						$(".modal-title").append("Chi tiết câu trả lời");
					}
				});
			}

			function exportExcel() {
				var qnaInMsId = $('#qnaInMsId').val();
				var partnerId = $('select[name="partnerId"]').val();
				var thoigiantraloitu = $('input[name="thoigiantraloitu"]')
						.val();
				var thoigiantraloiden = $('input[name="thoigiantraloiden"]')
						.val();
				var maphieu = $('input[name="maphieu"]').val();
				var macic = $('input[name="macic"]').val();
				var makh = $('input[name="makh"]').val();
				var tenkh = $('input[name="tenkh"]').val();
				var socmnd = $('input[name="socmnd"]').val();
				var masothue = $('input[name="masothue"]').val();

				var hasResult = null;
				if ($('#hasResult1').is(":checked")) {
					hasResult = 1;
				} else {
					hasResult = 0;
				}
				var cicErr = null;
				if ($('#cicErr').is(":checked")) {
					cicErr = 1;
				} else {
					cicErr = 0;
				}

				window.open($('#theForm').attr('action')
						+ '?method=ExportFileExcel&qnaInMsId=' + qnaInMsId
						+ '&partnerId=' + partnerId + '&thoigiantraloitu='
						+ escape(thoigiantraloitu) + '&thoigiantraloiden='
						+ escape(thoigiantraloiden) + '&maphieu=' + maphieu
						+ '&macic=' + macic + '&makh=' + makh + '&tenkh='
						+ tenkh + '&socmnd=' + socmnd + '&masothue=' + masothue
						+ '&hasResult=' + hasResult + '&cicErr=' + cicErr);
			}

			function callServiceHoiTin(val) {
				jConfirm(
						'Th\u1EF1c hi\u1EC7n gi\u1EA3 l\u1EADp Job H\u1ECFi tin sang CIC ? ',
						'\u0110\u1ED3ng \u00FD',
						'H\u1EE7y b\u1ECF',
						function(r) {
							if (!r)
								return;
							$
									.ajax({
										async : true,
										type : "POST",
										url : $('#theForm').attr('action')
												+ "?method=callServiceHoiTin&qnaMaSP=" + val,
										success : function(data, status, xhr) {
											$.loader("close");
											alert(data);
											findData();
										},
										error : function(xhr, status, error) {
											$.loader("close");
											alert('Th\u1EF1c hi\u1EC7n kh\u00F4ng th\u00E0nh c\u00F4ng');
										}
									});
						});
			}

			function callServiceDsVanTin() {
				jConfirm(
						'Th\u1EF1c hi\u1EC7n gi\u1EA3 l\u1EADp Job V\u1EA5n tin sang CIC ? ',
						'\u0110\u1ED3ng \u00FD',
						'H\u1EE7y b\u1ECF',
						function(r) {
							if (!r)
								return;
							$
									.ajax({
										async : true,
										type : "POST",
										url : $('#theForm').attr('action')
												+ "?method=callServiceDsVanTin",
										success : function(data, status, xhr) {
											$.loader("close");
											alert(data);
											findData();
										},
										error : function(xhr, status, error) {
											$.loader("close");
											alert('Th\u1EF1c hi\u1EC7n kh\u00F4ng th\u00E0nh c\u00F4ng');
										}
									});
						});
			}
			
			function chiTietLoiInput(id){
				$.ajax({
					url :location.pathname + '?method=loadQnaDetailsError',
	               	data: {
	               		qnaInOutDetailsId : id
	               	},
	                method: 'GET',
	                success: function(data){
	                	console.log(data);
	                	//console.log(JSON.parse(data));
	                	$('#tableError tbody').empty();
	                	arr = JSON.parse(data);
	                	for ( var item in arr) {
	                		var row = '<tr>';
							row += '<td>' + arr[item].maloi +'</td>';
							row += '<td>' + arr[item].motaloi +'</td>';
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