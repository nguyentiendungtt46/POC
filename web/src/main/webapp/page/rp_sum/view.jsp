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

<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Danh sách gửi báo cáo"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/rpSum" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left" style="    width: 1200px;
    margin-left: -30px;">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Danh sách gửi báo cáo</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày nhận từ</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="formDate" name="formDate" cssClass="currentDate"></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày nhận đến</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <form:input path="toDate" name="toDate" cssClass="currentDate"></form:input>
		                    </div>
						</div>
						<div class="Row">
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">User báo cáo</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="userReport" id="userReport"></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã báo cáo</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <select class="form-control" name="reportCode" id="reportCode">
									<option value="">- Chọn -</option>
									<c:forEach items="#{lstRpType}" var="item">
										<option value="${item.id}">
											<c:out value="${item.id}" />
										</option>
									</c:forEach>
								</select>
		                    </div>
						</div>
						<div class="Row">
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên file báo cáo</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="fileName" id="fileName"></form:input>
		                    </div>
		                    <div class="Empty"></div>
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
						</div>
						<div class="Row">
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trạng thái</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <select class="form-control" name="status" id="status">
									<option value="">- Chọn -</option>
									<option value="0">0 - Chưa gửi</option>
									<option value="1">1 - Đã gửi</option>
									<option value="2">2 - Ðã có kết quả</option>
									<option value="3">3 - Đã hủy</option>
								</select>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trả lại File</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<input type="checkbox"  name="re_do" id="re_do" value="1">
		                    </div>
		                    
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Báo cáo bị hủy</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<input type="checkbox"  name="partnerCancel" id="partnerCancel" value="1">
		                    </div>
						</div>
						<div class="divaction" align="center">
				            <input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm"/>
				            <input class="btn blue" type="button" onclick="exportExcel();" value="Xuất Excel"/>
				        </div>
			        	
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
		function initParam(tblCfg){	
			tblCfg.bFilter = false;
            tblCfg.aoColumns = [			 
	            {"sClass": "left","bSortable" : false,"sTitle":'STT'},
	            {"sClass": "left","bSortable" : false,"sTitle":'User'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã báo cáo'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Tên file/file đối ứng'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã Đơn vị'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Ngày báo cáo'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Tên người báo cáo'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Trạng thái'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Mã lỗi'},
	            {"sClass": "left","bSortable" : false,"sTitle":'% dữ liệu lỗi (Tổng số bản ghi)'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Thời điểm gửi'},
// 	            {"sClass": "left","bSortable" : false,"sTitle":'Thời gian xử lý (giây)'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Dung lượng file'},
	            {"sClass": "left","bSortable" : false,"sTitle":'Hành động'}
            ];
		}
		
		$(document).ready(function() {
		     $('.btnDtAdd').hide();
		     $('.btnDtDelete').hide();
		     
		     $(".currentDate").datepicker({
					dateFormat : 'dd/mm/yy',
					changeMonth : false,
					changeYear : false,
					showButtonPanel : false
			});
		});
		function detailValidate (reportId) {
			var tmp= $('#theForm').attr('action').split('/');
			window.location.href = '/' + tmp[1] + '/rpValidate?reportId='+reportId;
		} 
		
		function traLaiTctd(id){
			$.ajax({
                url:$('#theForm').attr('action') + '?method=updateRedo&id=' + id,
                method: 'POST',
                success: function(data){
                	if (data.trim() != '') {
		            	alert(data);
	                }else{
	                	alert('Thực hiện thành công', function () {
	                        findData();
	                    });
	                } 
                }
            });
		}
		
		function chuyenSangM1(id){
			$.ajax({
                url:$('#theForm').attr('action') + '?method=transfeFileToM1&id=' + id,
                method: 'GET',
                success: function(data){
                	if (data.trim() != '') {
		            	alert(data);
	                }else{
	                	alert('Thực hiện thành công', function () {
	                        findData();
	                    });
	                } 
                }
            });
		}
		
		function taiFile(id){
			window.open($('#theForm').attr('action') + '?method=downloadFileFtp&id=' + id + '&type=' + 'f');
		}
		
		function taiFileAttach(id){
			window.open($('#theForm').attr('action') + '?method=downloadFileFtp&id=' + id + '&type=' + 'fa');
		}
		
		function redo(reportId){
		    $.loader( {
		        className : "blue-with-image-2"
		    });
		    var tokenIdKey = $('#tokenIdKey').val();
		    var tokenId = $('#tokenId').val();
		    $.ajax( {
		        async : false, type : "POST", url : $('#theForm').attr('action') + "?method=redo&reportId="+reportId,
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
		
		function exportExcel() {
			var tuNgay = $('input[name="formDate"]').val();
			var denNgay = $('input[name="toDate"]').val();
			var userReport = $('input[name="userReport"]').val();
			var reportCode = $('select[name="reportCode"]').val();
			var fileName = $('input[name="fileName"]').val();
			var partnerCode = $('select[name="matctd"]').val();
			var status = $('select[name="status"]').val();
			var reDo = null;
			if ($('#re_do').is(":checked")) {
				reDo = "1";
			} else {
				reDo = "0";
			}
			
			window.open($('#theForm').attr('action') + '?method=ExportFileExcel&tuNgay='+tuNgay+'&denNgay='+denNgay+
					'&partnerCode='+partnerCode+'&fileName='+fileName+'&status='+status+'&userReport='+userReport+'&reportCode='+reportCode+
					'&reDo='+reDo);
		}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>