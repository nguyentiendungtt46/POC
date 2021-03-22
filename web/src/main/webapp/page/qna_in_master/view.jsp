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

<select id = "cbCustomer" style="display:none;">
		<option value="">- Chọn -</option>
		<c:forEach items="#{lstCustomer}" var="item">
			<option value="${item.id}">
				<c:out value="${item.maKh}" /> - <c:out value="${item.tenKh}" /> 
			</option>
		</c:forEach>
	</select>
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Lịch sử hỏi tin sang CIC"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/qnaInMaster" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Lịch sử hỏi tin sang CIC</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày hỏi tin từ</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="ngayhoitintu" name="ngayhoitintu" cssClass="input_date"></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày hỏi tin đến</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <form:input path="ngayhoitinden" name="ngayhoitinden" cssClass="input_date"></form:input>
		                    </div>
						</div>
						<div class="Row">
						<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã số phiếu</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <form:input path="maSoPhieu" name="maSoPhieu"></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại sản phẩm</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <select class="form-control" name="loaisp" id="loaisp">
									<option value="">- Chọn -</option>
									<option value="S11A">S11A - Sản phẩm chi tiết khách hàng vay thể nhân</option>
									<option value="S10A">S10A - Sản phẩm chi tiết khách hàng vay pháp nhân</option>
									<option value="R11A">R11A - Sản phẩm thông tin quan hệ tín dụng thể nhân</option>
									<option value="R10A">R10A - Sản phẩm thông tin quan hệ tín dụng pháp nhân</option>
									<option value="R14">R14 - Sản phẩm thông tin chủ thẻ tín dụng thể nhân</option>
									<option value="R14.DN">R14.DN - Sản phẩm thông tin chủ thẻ tín dụng pháp nhân</option>
									<option value="R21">R21 - Sản phẩm thông tin bảo đảm tiền vay thể nhân</option>
									<option value="R20">R20 - Sản phẩm thông tin bảo đảm tiền vay pháp nhân</option>
									<option value="XH50">XH50 - Tra cứu xếp hạng tín dụng cho doanh nghiệp</option>
									<option value="XH51">XH51 - Tra cứu XHTD Tập đoàn, Tổng Công ty</option>
									<%-- <c:forEach items="#{dsDoiTac}" var="item">
										<option value="${item.id}">
											<c:out value="${item.code}" /> -
											<c:out value="${item.name}" />
										</option>
									</c:forEach> --%>
								</select>
		                    </div>
						</div>
						<div class="Row">
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Người dùng</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<select class="form-control" name=username id="username">
									<option value="">- Chọn -</option>
									<c:forEach items="#{sysUsers}" var="item">
										<option value="${item.username}">
											<c:out value="${item.username}" /> -
											<c:out value="${item.name}" />
										</option>
									</c:forEach>
								</select>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tồn tại phiếu hỏi tin lỗi</div>
		                     <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                     	<form:checkbox path="hasRealTimeError"/>
		                     </div>
		                     <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đã có kết quả</div>
		                     <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                     	<form:checkbox path="hasResult"/>
		                     </div>
						</div>
						<div class="divaction" align="center">
				            <input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm"/>
				            <input class="btn blue" type="button" onclick="exportExcel();" value="Export"/>
				        </div>
				        <!-- <div align="center" class="HeaderText">&#8203;</div> -->			    
			        	
					</div>
				</div>
				<%@ include file="/page/include/data_table.jsp"%>
		    </div>
		    <input type="hidden" id="expriredDay" name="expriredDay" value="${expriredDay}"/>
		</tiles:putAttribute>
		
		<tiles:putAttribute name="catDetail" cascade="true">
			<div class="box-custom">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Cấu hình hỏi tin</h1>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại sản phẩm<font color="red">*</font></div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qnaInMaster.loaisp" cssClass="required" title="Mã số phiếu không được để trống"></form:input>
					</div>
					<%-- <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Số lượng phiếu
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="qnaInMaster.maSoPhieu" cssClass="required" title="Mã số phiếu không được để trống"></form:input>
					</div> --%>
				</div>
			</div>
			
			<div class="box-custom data-table">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Danh sách thuộc tính</h1>
					</div>
				</div>
				<div class="table-responsive" style="margin-top: -40px;">
					<table class="table table-bordered" id="table-cfgDetail">
						<thead>
							<tr>
								<th width="200px">Khách hàng</th>
								<th width="200px">Mã phiếu</th>
								<th width="120px">Mã CIC</th>
								<th width="200px">Số CMT</th>
								<th width="90px">Số ĐKKD</th>
								<th width="150px">Địa chỉ</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					<div align="right" style="text-align: right">
						<input type="button" id="table-product-customer_add" value="THÊM"
							class="btn blue" onclick="addRowCfg();"
							style="font-size: 10px; height: auto; padding: 0px 8px; height: 22px !important; border-radius: 4px;"
							aria-invalid="false">
						<input type="button"
							id="table-product-customer_del" value="Xóa" class="btn red" onclick="delRowCfg();"
							style="font-size: 10px; height: auto; padding: 0px 8px; height: 22px !important; border-radius: 4px;">
					</div>
				</div>
			</div>
		</tiles:putAttribute>
	
	</form:form>
	
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
		function initParam(tblCfg){	
			tblCfg.bFilter = false;
			tblCfg.buttons = [];
            tblCfg.aoColumns = [			 
	            {"sClass": "left","bSortable" : false,"sTitle":'STT'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Loại sản phẩm'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Thời điểm gửi sang CIC'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Trạng thái'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Số lượng phiếu'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã lỗi'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mô tả lỗi'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Chi tiết khách hàng'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Hành động'},
            ];
		}
		
		
		var x;
		function viewClob(data){
			x = JSON.parse(data);
			$("#element").empty();
			$('#element').jsonView(JSON.parse(data));
        	$(".modal-title").empty();
        	$(".modal-title").append("Chi tiết lỗi");
        	$('#myModal').modal('show');
		}
		function redo(qnaInMsId){

		    $.loader( {
		        className : "blue-with-image-2"
		    });
		    var tokenIdKey = $('#tokenIdKey').val();
		    var tokenId = $('#tokenId').val();
		    $.ajax( {
		        async : false, type : "POST", url : $('#theForm').attr('action') + "?method=redo&qnaInMsId="+qnaInMsId,
		        success : function (data, status, xhr) {
		            $.loader("close");
		            if (data.trim() != '') {
		            	alert(data);
	                }
	                else {
	                    alert('Thực hiện thành công', function () {
	                        findData();
	                    });

	                }
		        },
		        error : function (xhr, status, error) {
		            $.loader("close");
		            alert('Thực hiện không thành công');
		        }
		    });
			
		}
		
		function viewDescription(id){
			//disabledDiv("modal-view-tree", false);
			$('#treeDescription').tree('reload');
			$.ajax({
				url : $('#theForm').attr('action')
						+ '?method=getTree',
				data : {
					id : id
				},
				method : 'GET',
				success : function(_result) {
					if (_result != null) {
						myJSON = JSON.parse(_result);
						console.log(myJSON);
	                    $('#treeDescription').tree({
	                        data : myJSON
	                    });
	                    $(".modal-title").empty();
	                	$(".modal-title").append("Chi tiết lỗi");
	                	$('#myModal').modal('show');
					}
				}
			});
		}
		function exportExcel() {
			var tuNgay = $('input[name="ngayhoitintu"]').val();
			var denNgay = $('input[name="ngayhoitinden"]').val();
			var partnerCode = $('select[name="matctd"]').val();
			var productType = $('select[name="loaisp"]').val();
			var maSoPhieu = $('input[name="maSoPhieu"]').val();
			var hasResult = null;
			if ($('input[name="hasResult"]:checked').length > 0) {
				hasResult = 1;
			} else {
				hasResult = 0;
			}
			var username = $('select[name="username"]').val();
			var hasRealTimeError = null;
			if ($('input[name="hasRealTimeError"]:checked').length) {
				hasRealTimeError = 1;
			} else {
				hasRealTimeError = 0;
			}
			
			window.open($('#theForm').attr('action') + '?method=ExportFileExcel&tuNgay='+tuNgay+'&denNgay='+denNgay+
					'&partnerCode='+partnerCode+'&productType='+productType+'&maSoPhieu='+maSoPhieu+'&hasResult='+hasResult+'&username='+username+
					'&hasRealTimeError='+hasRealTimeError);
		}
		
		function addRowCfg(){
			tableObject.addRow(tableObject.rowTemp);
			$('#table-cfgDetail select').select2();
		}
		function delRowCfg(){
			tableObject._deleteRow();
		}
		$(document).ready(function() {
		     //$('.btnDtAdd').hide();
		     $('.btnDtDelete').hide();
		     $(".input_date").datepicker({
					dateFormat : 'dd/mm/yy',
					changeMonth : false,
					changeYear : false,
					showButtonPanel : false,
					onSelect: function(selected,evnt) {
				         findByDate(selected);
				    }
				});
		     var expriredDay = $('#expriredDay').val();
		     if (expriredDay != "") {
		    	 alert("Số ngày hết hạn mật khẩu " + expriredDay + " ngày, bạn phải đổi mật khẩu");
		     }
		     initDataTableCfg();
		});
		
		function initDataTableCfg() {
				 var rowTempIp = initRowTableCfg();
				 tableObject = new TFOnline.DataTable({
					id : 'table-cfgDetail',
					jQueryUI : true,
					rowTemp : rowTempIp,
					hasCheck : true,
					addOveride:true,
                    delOveride:true,
                    addButton: 'tableAllAst_add',
                    delButton: 'tableAllAst_del',
					maxRow : 1000
				}); 
		}
		function initRowTableCfg() {
			var rowTemp = [];
			var strHidden = '<input type="hidden" class="form-control" name="qnaInMaster.id" />';
			strHidden	 += '<input type="hidden" class="form-control" name="qnaInMaster.qnaInDetailArrayList[].tenkh" />';
			strHidden	 += '<input type="hidden" class="form-control" name="qnaInMaster.qnaInDetailArrayList[].makh" />';
			rowTemp = [
				    '<td><div class="line-table"><select class="notnull customer" name="qnaInMaster.qnaInDetailArrayList[].selectkh">'  + $('#cbCustomer').html() + '</select></div></td>',
					'<div class="line-table"><input type="text" class="form-control notnull" name="qnaInMaster.qnaInDetailArrayList[].msphieu" /></div>',
					'<div class="line-table"><input type="text" class="form-control notnull" name="qnaInMaster.qnaInDetailArrayList[].macic" /></div>',
					'<div class="line-table"><input type="text" class="form-control notnull" name="qnaInMaster.qnaInDetailArrayList[].socmt" /></div>',
					'<div class="line-table"><input type="text" class="form-control" name="qnaInMaster.qnaInDetailArrayList[].dkkd" /></div>',
					'<div class="line-table"><input type="text" class="form-control" name="qnaInMaster.qnaInDetailArrayList[].diachi" /></div>'
					 + strHidden];
			return rowTemp;
		}
		
		$(document.body).on("change","select.customer",function(){	
			
			var nameSelect = this.name;
			$.ajax({
				url : $('#theForm').attr('action') + '?method=loadDataCustomer',
				data : {
					id : this.value
				},
				method : 'GET',
				success : function(_result) {
					if (_result != null) {
						myJSON = JSON.parse(_result);
						console.log(myJSON);
						$("input[name='" + nameSelect.replace('selectkh','macic') +"']").val(myJSON.maCic);
						$("input[name='" + nameSelect.replace('selectkh','socmt') +"']").val(myJSON.soCmt);
						$("input[name='" + nameSelect.replace('selectkh','dkkd') +"']").val(myJSON.dkkd);
						$("input[name='" + nameSelect.replace('selectkh','diachi') +"']").val(myJSON.diaChi);
						$("input[name='" + nameSelect.replace('selectkh','tenkh') +"']").val(myJSON.tenKh);
						$("input[name='" + nameSelect.replace('selectkh','makh') +"']").val(myJSON.maKh);
					}
				}
			});
		});
		
		function retryCallCic(qnaInMsId){
		    $.loader( {
		        className : "blue-with-image-2"
		    });
		    $.ajax( {
		        async : true, type : "POST", url : $('#theForm').attr('action') + "?method=retryCallCic&qnaInMsId="+qnaInMsId,
		        success : function (data, status, xhr) {
		            $.loader("close");
		            if (data.trim() != '') {
		            	alert(data);
	                }
	                else {
	                    alert('Thực hiện thành công', function () {
	                        findData();
	                    });

	                }
		        },
		        error : function (xhr, status, error) {
		            $.loader("close");
		            alert('Thực hiện không thành công');
		        }
		    });
			
		}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>