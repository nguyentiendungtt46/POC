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
	<tiles:putAttribute name="title" value="Cấu trúc đại lý" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/entParIdx" var="formAction" />
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
								<h1>Cấu trúc đại lý</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
								đại lý</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="keyword_code" id="keyword_code"></form:input>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên
								đại lý</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<form:input path="keyword_name" id="keyword_name"></form:input>
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
			<form:hidden path="catAgencyStructure.id" id="id"/>
			<form:hidden path="catAgencyStructure.parentId" id="parentId"/>
			<div class="box-custom">
				<div class="row">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Thông tin đơn vị</h1>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã
						đại lý<font color="red">*</font></div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catAgencyStructure.code" cssClass="required uppercase ascii"></form:input>
					</div>
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên
						đại lý<font color="red">*</font></div>
					<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
						<form:input path="catAgencyStructure.name" cssClass="required" title="Tên đại lý không được để trống"></form:input>
					</div>
				</div>
				<div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đại lý cha</div>
					<div class="col-md-6 col-lg-6 col-sm-9 col-xs-9">
						<input type="text" id="ent_part_idx_parent" name="catAgencyStructure.fullPath" readonly="readonly"/>
					</div>
					<div class="col-md-4 col-lg-4 col-sm-3 col-xs-3" style="    padding: 0;">
						<input class="btn blue" type="button" onclick="viewModal();" value="Chọn" style="    margin-top: -7px;" />
						<input class="btn red" type="button" onclick="resetParent();" value="Xóa" style="    margin-top: -7px;" />
					</div>
				</div>
			</div>
			
			<div class="Row">
				<div class="easyui-panel" style="padding: 5px">
					<ul id="danhSachQuyen" class="easyui-tree"
						data-options="animate:true,checkbox:true,cascadeCheck:true,lines:true"></ul>
				</div>
			</div>
			
<!-- Modal Bo sung dich vu -->
<div class="modal fade" id="modal-view-tree" role="dialog">
	<div class="modal-dialog">
		<!-- Modal content-->
		<div class="modal-content" style="    width: 870px;">
			<div class="modal-header">
				
				<h6 class="modal-title" id="modal-title">Agency structure</h6>
					<button type="button" class="close" data-dismiss="modal" style="color: #fff;">&times;</button>
			</div>
			<div class="modal-body">
				<div class="form-group">
				<div class="easyui-panel" style="padding: 5px">
					<ul id="_tree_entParIdx" class="easyui-tree"
						data-options="animate:true,cascadeCheck:true,lines:true"></ul>
				</div>
			</div>
				<div class="modal-footer">
					<button type="button" class="btn blue"
						onclick="addCatParent();">Chọn</button>
					<button type="button" class="btn gray" data-dismiss="modal">Đóng</button>
				</div>
			</div>
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
					"sTitle" : 'Mã đại lý'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Tên đại lý'
				}, {
					"sClass" : "left",
					"bSortable" : false,
					"sTitle" : 'Đại lý cha'
				} ];
			}
			
			$(document).on('click', '.btnDtAdd', function () {
				document.getElementById("catAgencyStructure.code").disabled = false;
	        });
			
			function beforeEdit(res){
				document.getElementById("catAgencyStructure.code").disabled = true;
			}
			
			function beforeSave(){
				document.getElementById("catAgencyStructure.code").disabled = false;
			}
			
			
			function afterEdit(id, res){
				if (res.parent != null && res.parent.id != null && res.parent.id != "")
					$('#parentId').val(res.parent.id);
			}
			
			$(document).ready(function () {
				$('.btnDtDelete').hide();
			});
			var myJSON;
			function viewModal(){
				disabledDiv("modal-view-tree", false);
				$('#_tree_entParIdx').tree('reload');
				$.ajax({
					url : $('#theForm').attr('action')
							+ '?method=getTree',
					data : {
						parentId : ""
					},
					method : 'GET',
					success : function(_result) {
						if (_result != null) {
							myJSON = JSON.parse(_result);
							console.log(myJSON);
		                    $('#_tree_entParIdx').tree({
		                        data : myJSON
		                    });
							$('#modal-view-tree').modal('show');
						}
					}
				});
			}
			
			$('#_tree_entParIdx').tree({
				onBeforeExpand: function(node){
					$.ajax({
						url : $('#theForm').attr('action')
								+ '?method=getTree',
						async: false,
						data : {
							parentId : node.id
						},
						method : 'GET',
						success : function(_result) {
							if (_result != null) {
								myJSON = JSON.parse(_result);
								if(node.children != null){
									if(node.children.length > 0) return;
								}
			                    $('#_tree_entParIdx').tree('append', {
			                    	parent : node.target,
			                        data : myJSON
			                    });
			                    $('#_tree_entParIdx').tree('expandTo', node.target);
							}
						}
					});
				  }
			})
			
			function addCatParent(){
				var parentId = $('#_tree_entParIdx').tree('getSelected');
				if(parentId == null || parentId == '' || parentId == 'undefined') {
					alert("Chọn một đại lý cha");
					return;
				}
				$("#ent_part_idx_parent").val(parentId.text);
				$("#parentId").val(parentId.id);
				$('#modal-view-tree').modal('hide');
			}
			
			function editPartner(id) {
	            $.loader( {
	                className : "blue-with-image-2"
	            });
	            clearDiv('divDetail');
	           
	            $.getJSON(editUrl, 
	            {
	                "id" : id
	            }).done(function (res) {
	            	initDataEdit(res);
	                $.loader('close');

	            });
	        }
			
			function resetParent() {
				$('#ent_part_idx_parent').val('');
				$('#parentId').val('');
			}
			$(document).ready(function () {
				$('.btnDtDelete').hide();;
			});
			
			function exportExcel() {
				var code = $('#keyword_code').val();
				var name = $('#keyword_name').val();
				downloadFileUrl(location.pathname + '?method=exportExcel&code=' + code + '&name=' + name);
			}
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>