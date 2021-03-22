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
	<tiles:putAttribute name="title" value="Quản trị job" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/cicSchedule" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm"
		enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<%@ include file="/page/include/data_table.jsp"%>
			</div>
		</tiles:putAttribute>

		<%-- <tiles:putAttribute name="catDetail" cascade="true">
			<form:hidden path="catProduct.id" id="id" />
			<form:hidden path="catProduct.strFixProduct" id="fixProduct" />	
			<form:hidden path="catProduct.price" id="priceVal" class="money" />
			<div class="box-custom">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Thông tin sản phẩm</h1>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã sản phẩm<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catProduct.code" cssClass="required uppercase ascii" 
							title="Mã loại sản phẩm không được để trống"></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Tên sản phẩm<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catProduct.name" cssClass="required"
							title="Tên loại sản phẩm không được để trống"></form:input>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Disable sản phẩm
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:checkbox path="catProduct.disable" />
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Mã SP HTHTL</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catProduct.codeQA" cssClass=""></form:input>
					</div>
				</div>
				
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Thời gian hiệu lực dữ liệu (giờ)<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catProduct.timeToLive" cssClass="required number" maxlength="2"></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Giới hạn dữ liệu (MB)
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catProduct.maxSizeApi" cssClass="number" maxlength="3"></form:input>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Tần suất (phút)<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catProduct.frequency" cssClass="required number" maxlength="3"></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">
						Loại sản phẩm<font color="red">*</font>
					</div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:select path="catProduct.typeProduct" cssClass="required">
							<form:option value="0">Cá nhân</form:option>
							<form:option value="1">Pháp nhân</form:option>
						</form:select>
					</div>
				</div>
			</div>
			
			
		</tiles:putAttribute> --%>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			var createNew = false;
			var table = null;
			var optionTable = '';

			function initParam(tblCfg) {
				tblCfg.buttons = [];
				tblCfg.bFilter = false;
				tblCfg.gridTitle = 'Danh sách job';
				tblCfg.aoColumns = [ {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'STT'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên dịch vụ'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên sản phẩm'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Giới hạn dữ liệu (MB)'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tần suất (phút)'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Thời gian hiệu lực dữ liệu (giờ)'
				},{
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Disable'
				} ];
			}

			$(".nav-link").on("click", function() {
				$(".nav-item").find(".active").removeClass("active");
				$(this).addClass("active");
			});

			$(document).on('click', '.btnDtAdd', function() {
				document.getElementById("catProduct.code").disabled = false;
			});

			function beforeEdit(res) {
				document.getElementById("catProduct.code").disabled = true;

				/* if(res.fixProduct){
					$("#cboxFixProduct").prop("checked", "checked");
					$(".gridMoney").css("display", "none");
				} */
				if (res.disable) {
					$("#cboxDisable").prop("checked", "checked");
				}
			}

			function displayGird() {
				// Get the checkbox
				var checkBox = document.getElementById("cboxFixProduct");
				if (checkBox.checked == true) {
					$(".gridMoney").css("display", "none");
					$("#fixProduct").val("1");
				} else {
					$(".gridMoney").css("display", "block");
					$("#fixProduct").val("0");
				}

			}

			function disableClick() {
				var cboxDisable = document.getElementById("cboxDisable");
				if (cboxDisable.checked == true) {
					$("#disable").val("1");
				} else {
					$("#disable").val("0");
				}
			}

			function displayDivAuto(_val) {
				if (_val.value == "0") {
					$("#divByAuto, .gridPeriod").css("display", "none");

				}

				else {
					$("#divByAuto").css("display", "flex");
					$(".gridPeriod").css("display", "inline");
				}

			}

			$(document).ready(function() {
				$('.btnDtDelete').hide();
				$('.btnDtAdd').hide();
				/* $("#divByAuto, .gridPeriod").css("display", "none");
				var myJSON;
				$('#_tree_catParIdx').tree('reload');
				$.ajax({
					url : $('#theForm').attr('action') + '?method=getTree',
					data : {
						parentId : ""
					},
					method : 'GET',
					success : function(_result) {
						if (_result != null) {
							myJSON = JSON.parse(_result);
							$('#_tree_catParIdx').tree({
								data : myJSON
							});
						}
					}
				}); */
				$("#start_date_auto").datepicker({
					dateFormat : 'dd/mm/yy',
					changeMonth : false,
					changeYear : false,
					showButtonPanel : false
				});
				initTableCustomer();
				//loadTCTDCbox();
			});

			function loadTCTDCbox() {
				$
						.ajax({
							url : $('#theForm').attr('action')
									+ '?method=getListTCTD',
							data : {},
							method : 'GET',
							success : function(_result, status, xhr) {
								$('.listTCTD').append($('<option>', {
									value : '',
									text : 'Chọn'
								}));
								$
										.each(
												_result,
												function(i, item) {
													$('.listTCTD')
															.append(
																	$(
																			'<option>',
																			{
																				value : item.id,
																				text : item.code
																						+ " - "
																						+ (item.sortName == '' ? item.name
																								: (item.name
																										+ '('
																										+ item.sortName + ')'))
																			}));
												});
							}
						});
			}
			// lay du lieu chi nhanh theo ngan hang
			/* $('.listTCTD')
					.on(
							'change',
							function(e) {
								var optionSelected = $("option:selected", this);
								var valueSelected = this.value;
								//alert(valueSelected);
								$
										.ajax({
											url : $('#theForm').attr('action')
													+ '?method=getListTCTDById',
											data : {
												id : valueSelected
											},
											method : 'GET',
											success : function(_result, status,
													xhr) {
												optionTable = '';
												$
														.each(
																_result,
																function(i,
																		item) {
																	optionTable += '<option value=" '+ item.id + '">'
																	+ item.code + " - " +item.name
																			+ '</option>';
																});
												table.rowTemp[0] = '<div class="line-table selectCss"><select>'
														+ optionTable
														+ '</select></div>';
											}
										});
							}); */
			function initTableCustomer() {
				var rowTempIp = initRowTable();
				var tableObject = new TFOnline.DataTable({
					id : 'table-product-customer',
					jQueryUI : true,
					rowTemp : rowTempIp,
					hasCheck : true,
					addOveride : true,
					delOveride : true,
					addButton : 'tableAllAst_add',
					delButton : 'tableAllAst_del',
					maxRow : 100
				});
				table = tableObject;
			}
			function initRowTable() {
				var rowTemp = [];
				var listObject = "partner";
				var strHidden = '<input type="hidden" class="form-control" name="' + listObject + '.partnerConnectIpArrayList[].id" />';
				rowTemp = [
						'',
						'<div class="line-table selectCss"><select><option value="0">Pháp nhân</option><option value="1">Thế nhân</option></select></div>',
						'<div class="line-table"><input type="text" class="form-control" name="" /></div>',
						'<div class="line-table"><input type="text" class="form-control" name="" /></div>',
						'<div class="line-table"><input type="text" class="form-control" name="" /></div>',
						'<div class="line-table"><input type="text" class="form-control" name="" /></div>',
						'<div class="line-table"><input type="text" class="form-control" name="" /></div>'
								+ strHidden ];
				return rowTemp;
			}

			$('#_tree_catParIdx').tree(
					{
						onBeforeExpand : function(node) {
							$.ajax({
								url : $('#theForm').attr('action')
										+ '?method=getTree',
								async : false,
								data : {
									parentId : node.id
								},
								method : 'GET',
								success : function(_result) {
									if (_result != null) {
										myJSON = JSON.parse(_result);
										if (node.children != null) {
											if (node.children.length > 0)
												return;
										}

										$('#_tree_catParIdx').tree('append', {
											id : 'abc',
											parent : node.target,
											data : myJSON
										});
										$('#_tree_catParIdx').tree('expandTo',
												node.target);
									}
								}
							});
						},
						onCheck : function(node) {
							var arrChecked = $('#_tree_catParIdx').tree(
									'getChecked');
							_totalMoney = caculatFee(arrChecked);
							var nf = Intl.NumberFormat();
							$("#priceTxt").text(nf.format(_totalMoney));
							$("#priceVal").val(_totalMoney);
						}
					});
			function caculatFee(arrChecked) {
				var _totalMoney = 0;
				for (var i = 0; i < arrChecked.length; i++) {
					if (parentNotAvail(arrChecked[i], arrChecked)) {
						if (arrChecked[i].price != ''
								&& arrChecked[i].price != null
								&& arrChecked[i].price != 'undefined')
							_totalMoney += arrChecked[i].price;
					}

				}
				return _totalMoney;

			}
			function parentNotAvail(treeNode, arrChecked) {
				// Khong co parent
				if (treeNode.parentId == '' || treeNode.parentId == undefined)
					return true;

				var parentNode = $('#_tree_catParIdx').tree('getParent',
						treeNode.target);

				// Cha không co phi
				if (parentNode.price == '' && parentNode.price == null
						&& parentNode.price == 'undefined')
					return true;

				// Co parent trong danh sach
				for (var i = 0; i < arrChecked.length; i++) {
					if (arrChecked[i].id == parentNode.id)
						return false;
				}

				return true;
			}

			function addCatParent() {
				var parentId = $('#_tree_catParIdx').tree('getSelected');
				if (parentId == null || parentId == ''
						|| parentId == 'undefined') {
					alert("Chọn một chỉ tiêu cha");
					return;
				}
				$("#ent_part_idx_parent").val(parentId.fullpath);
				$('#modal-view-tree').modal('hide');
			}

			function addRowCustomer() {
				if ($('#listTCTD').val() == undefined
						|| $('#listTCTD').val().length == 0) {
					alert('Chưa chọn TCTD!')
					return;
				}
				table.addRowWithoutReIndex(table.rowTemp);
				//$("#table-product-customer selectC").select2();
			}

			function delRowCustomer(tableId, serviceGroupId) {
				table._deleteRowReIndex();
				instancReindexDelete(tableObj);
			}

			function cancel() {
				$('#divGrid').css('display', 'inline');
				$('#divDetail').css('display', 'none');
				$(".gridMoney").css("display", "block");
			}

			function loadDataTree(catProductCfgs) {
				for (var i = 0; i < catProductCfgs.length; i++) {
					// expand parent
					var _strArrId = getRootTree(catProductCfgs[i].indexId);
					getCheckedNode(_strArrId);
				}
			}

			function getRootTree(val) {
				if (val.parent != null) {
					return getRootTree(val.parent) + ";" + val.id;
				} else {
					return val.id;
				}
			}

			function getCheckedNode(_strArrId) {
				var _arrId = _strArrId.split(";");
				for (var i = 0; i < _arrId.length; i++) {
					if (_arrId[i] != "" && i != _arrId.length - 1) {
						var _findNode = $('#_tree_catParIdx').tree('find',
								_arrId[i]);
						$('#_tree_catParIdx').tree('expand', _findNode.target);
					} else if (_arrId[i] != "") {
						var _findNode = $('#_tree_catParIdx').tree('find',
								_arrId[i]);
						$('#_tree_catParIdx').tree('check', _findNode.target);
					}
				}
			}

			function exportExcel() {
				var code = $('#keyword_code').val();
				var name = $('#keyword_name').val();
				var serverId = $('#server_name').val();
				window.open($('#theForm').attr('action')
						+ '?method=exportExcel&code=' + code + '&name=' + name
						+ '&serverId=' + serverId);
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>