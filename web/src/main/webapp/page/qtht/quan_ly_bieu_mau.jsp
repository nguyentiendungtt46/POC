<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
    @media only screen and (min-width: 1200px){
        .container {
            max-width: 100% !important; 
        }
    }
 </style>

<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Quản lý biểu mẫu" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/managerTemplate" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>

	<form:form cssClass="form-horizontal" id="theForm"
		enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left" style="    max-width: 1140px !important;margin: 0 auto;">
				<div class="row search-style">
					<div class="Table" id="divSearchInf">
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Quản lý biểu mẫu</h1>
							</div>
						</div>
						<%-- <div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								biểu mẫu</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input id="templateCode" path="templateCode" />
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								báo cáo</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input id="khongquantam" path="id" />
							</div>

						</div> --%>
						<div class="Row">

							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã/Tên
								biểu mẫu</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="stempNm"></form:input>
							</div>
							<div class="col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>

							<!-- <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Loại
								mẫu biểu</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<s:select headerKey="" headerValue="Tất cả"
									list="lstTemplateType" listKey="value"
									listValueKey="description" name="loai_mau_bieu"></s:select>
							</div> -->
						</div>
						<%-- <div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Sản
								phẩm</div>
							<div class="col-md-10 col-lg-10 col-sm-12 col-xs-12">
								<!-- <s:select list="lstProduct" headerKey="" headerValue="Tất cả"
									listKey="id"
									listValue="%{fullGroupPath + '\\\' +  id + '-'+ prdNm}"
									name="productId" id="productId"></s:select> -->
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Có
								nhiều biểu mẫu trong hợp đồng</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:checkbox path="hasMultiInstance"></form:checkbox>
							</div>
						</div> --%>
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
			<div class="box-custom">
				<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Thông tin chi tiết</h1>
				</div>
				<form:hidden path="template.id" id="id"></form:hidden>
				<form:hidden path="template.pathFile" id="pathFile"></form:hidden>
				<!-- <s:select style="display:none" list="listDict" id="dsChiTieu"
					headerKey="" headerValue="" listKey="id"
					listValue="%{id + '-' + colDesc}"></s:select> -->
				<div style="display: none;">
					<select id="dsChiTieu">
						<option value="">--Chọn--</option>
						<c:forEach items="#{listDict}" var="item">
							<option value="${item.id}">
								<%-- <c:out value="${item.code}" /> - --%>
								<c:out value="${item.id}" /> -
								<c:out value="${item.colDesc}" />
							</option>
						</c:forEach>
					</select> <select id="dsControlType">
						<option value="">- Chọn -</option>
						<c:forEach items="#{dsControlType}" var="item">
							<option value="${item.id}">
								<c:out value="${item.code}" /> -
								<c:out value="${item.value}" />
							</option>
						</c:forEach>
					</select> <select id="dsDictType">
						<option value="">- Chọn -</option>
						<c:forEach items="#{dsDictType}" var="item">
							<option value="${item.id}">
								<c:out value="${item.code}" /> -
								<c:out value="${item.name}" />
							</option>
						</c:forEach>
					</select> <select id="listHeaderLevel">
						<option value="">- Chọn -</option>
						<c:forEach items="#{listHeaderLevel}" var="item">
							<option value="${item.key}">
								<c:out value="${item.value}" />
							</option>
						</c:forEach>
					</select>


				</div>
				<div class="Row">
					<div class="LabelCell">
						Mã biểu mẫu <font color="red">*</font>
					</div>
					<div class="Span2Cell">
						<form:input path="template.code" cssClass="required"></form:input>
					</div>

					<div class="Empty"></div>
					<div class="LabelCell">
						Tên biểu mẫu <font color="red">*</font>
					</div>
					<div class="Span2Cell">
						<form:input path="template.tempNm" cssClass="required"></form:input>
					</div>

				</div>


				<div id="sTab01" style="display: block; overflow-x: scroll;">
					<table id="tableDicTempDetailId" width="100%"
						style="table-layout: fixed">
						<thead>
							<tr>
								<th style="width: 16%">Chỉ tiêu</th>
								<th style="width: 15%">Nhãn hiển thị</th>
								<th style="width: 12%">Loại control</th>
								<th style="width: 11%">Dữ liệu</th>
								<th style="width: 16%">Nhóm</th>
								<th style="width: 6%">Level nhóm<font color="red">*</font></th>
								<th style="width: 4%">Vị trí</th>
								<th style="width: 26%">Path</th>
								<th style="width: 3%">Bắt buộc</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>

			</div>
		</tiles:putAttribute>
	</form:form>


	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			function defaultValue(){
				tableDicTempDetail.deleteAllRow();
			}
			function beforeEdit(res){
				if(res.lstDictTemps!=undefined)
					tableDicTempDetail.resize(res.lstDictTemps.length);
			}
			function afterEdit(id, res) {
				// an hien download file template
				if (res.pathFile != null && res.pathFile != '') {
					$('#downloadFile').css('display', 'block');
				} else {
					$('#downloadFile').css('display', 'none');
				}
			}

			function reloadTableDictTemp(res) {
				var data = res.lstDictTemps;
				if (data) {
					for (var i = 0; i < data.length; i++) {
						var dictId="";
						if (data[i].dictType !=null) {
							dictId = data[i].dictType.id;
						}
						var ctrTypeId = "";
						if (data[i].ctrType != null) {
							ctrTypeId = data[i].ctrType.id;
						}
						var rowTemp = [
							'<font></font>' 
							,
								'<input type="hidden" name="template.lstDictTemps[].templates.id" value="'+ res.id +'"/><input type="hidden" name="template.lstDictTemps[].id" value="'+ data[i].id +'"/><select onchange="this.title=this.options[this.selectedIndex].text" class="updateValue required needUpdate" name="template.lstDictTemps[].dictMeasure.id" value="'+ data[i].dictMeasure.id +'">'
										+ '<option value="'+data[i].dictMeasure.id+'">'
										+ data[i].dictMeasure.id
										+ "-"
										+ data[i].dictMeasure.colDesc
										+ '</option>' + '</select>',
								'<input type = "text"  name="lstDictTemps[].columnDesc" value="'
										+ (data[i].columnDesc || "") + '"/>',
								'<select onchange="this.title=this.options[this.selectedIndex].text" class="updateValue" name="template.lstDictTemps[].ctrType.id" value="'+ctrTypeId +'">'
										+ $("#dsControlType").html()
										+ '</select>',
								'<select onchange="this.title=this.options[this.selectedIndex].text" class="updateValue" name="template.lstDictTemps[].dictType.id" value="'+ dictId + '">'
										+ $("#dsDictType").html() + '</select>',
								'<input type = "text" onchange="this.title=this.value" name="template.lstDictTemps[].groupName" title="'
										+ (data[i].groupName || "")
										+ '" value="'
										+ (data[i].groupName || "") + '"/>',
								'<select  onchange="this.title=this.options[this.selectedIndex].text" class="required updateValue" name="template.lstDictTemps[].grpLevel" value="'
										+ (data[i].grpLevel || "")
										+ '">'
										+ $('#listHeaderLevel').html()
										+ '</select>',
								'<input type = "text" onchange="this.title=this.value" class = "digits" title="'
										+ (data[i].columnPosition || "")
										+ '" name="template.lstDictTemps[].columnPosition" value="'
										+ (data[i].columnPosition || "")
										+ '"/>',
								'<input type = "text"  onchange="this.title=this.value" name="template.lstDictTemps[].dictMeasure.path" title="'
										+ (data[i].dictMeasure.path || "")
										+ '" value="'
										+ (data[i].dictMeasure.path || "")
										+ '"/>',
								'<input type="checkbox" name="template.lstDictTemps[].mandatory" value="true"'
										+ (data[i].mandatory === true ? "checked"
												: "") + '/>'
								,'<input type="checkbox" class="rowSelect"/>'];
						tableDicTempDetail.addRowWithoutReIndex(rowTemp);
					}

					tableDicTempDetail.reIndex();
					$('select.updateValue').each(function() {
						//$(this).html($('#dsChiTieu').html());
						$(this).val($(this).attr('value'));
					});

				}

				initControl();

				$('select.needUpdate').select2('destroy');

				$("select.needUpdate").mouseover(function() {
					if ($(this).hasClass('needUpdate')) {
						var oldValue = $(this).val();
						$(this).html($('#dsChiTieu').html());
						$(this).removeClass("needUpdate");
						$(this).val(oldValue);
						$(this).unbind("mouseover");
						$(this).select2();
					}

				});
			}

			function initParam(tblCfg) {
				tblCfg.bFilter = true;
				tblCfg.bScrollX = true;
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : '<s:text name = "STT"/>'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên biểu mẫu'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Ngày bắt đầu'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Ngày kết thúc'
				} ];

			}
			var tableDicTempDetail = null;
			$(document).ready(function() {
				$('.btnDtDelete').hide();
				tableDicTempDetail = initTable("tableDicTempDetailId","template");
			});
			
			function initTable(tableId,listObject){
		        var rowTemp = initRowTemp(tableId, listObject);         
		        var tableObject = new TFOnline.DataTable({
		                id : tableId
		                ,jQueryUI : true	
		                ,rowTemp : rowTemp
		                ,hasCheck : true
		                ,maxRow : 100
		                ,hasOrder:true
		                ,afterAddRow:function(){
		                    initControl();
		                }
		        });
		        return tableObject;
		    }

			function initRowTemp(tableId, listObject) {
				var rowTemp = [];
				var listObject = "template";
				if (tableId == "tableDicTempDetailId") {
					var strHidden  = '<input type="hidden" class="form-control" name="' + listObject + '.lstDictTemps[].id" />';
					rowTemp = [
						'<div class="line-table"><select class="form-control updateValue required" onchange="this.title=this.options[this.selectedIndex].text" name="' + listObject + '.lstDictTemps[].dictMeasure.id">'+$('#dsChiTieu').html()+'</select></div>',
						'<div class="line-table"><input type="text" class="form-control" name="' + listObject + '.lstDictTemps[].columnDesc" /></div>',
						'<div class="line-table"><select class="form-control updateValue " onchange="this.title=this.options[this.selectedIndex].text" name="' + listObject + '.lstDictTemps[].ctrType.id">'+$('#dsControlType').html()+'</select></div>',
						'<div class="line-table"><select class="form-control updateValue " onchange="this.title=this.options[this.selectedIndex].text" name="' + listObject + '.lstDictTemps[].dictType.id">'+$('#dsDictType').html()+'</select></div>',
						'<div class="line-table "><input type="text" class="form-control" name="' + listObject + '.lstDictTemps[].groupName" /></div>',
						'<div class="line-table"><select class="form-control updateValue required" onchange="this.title=this.options[this.selectedIndex].text" name="' + listObject + '.lstDictTemps[].grpLevel">'+$('#listHeaderLevel').html()+'</select></div>',
						'<div class="line-table"><input class="digits" onchange="this.title=this.value" type="text" name="' + listObject + '.lstDictTemps[].columnPosition" /></div>',
						'<div class="line-table"><input onchange="this.title=this.value" type="text" name="' + listObject + '.lstDictTemps[].dictMeasure.path" /></div>',
						'<div class="line-table " style="text-align: center;"><input type="checkBox" name="' + listObject + '.lstDictTemps[].mandatory" /></div>'
						+ strHidden
						];
				}
				return rowTemp;
			}
			
			
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>