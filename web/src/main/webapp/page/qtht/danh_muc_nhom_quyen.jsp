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
	<tiles:putAttribute name="title" value="Danh sách nhóm quyền"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/role" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Danh sách nhóm quyền hệ thống</h1>
							</div>
						</div>
						<!-- <div class="divaction" align="center">
				            <input type="button" onclick="createNew=true;addNew();reloadTree();" value="Thêm mới" class="btn blue" aria-invalid="false"/> 
				        </div> -->
				        <!-- <div align="center" class="HeaderText">&#8203;</div> -->			    
			        
					</div>
				</div>
					<%@ include file="/page/include/data_table.jsp"%>
		    </div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="sysRoles.id" id="id"/>
		<div style="display: none">
				<select name="lstProduct" id="lstProduct">
					<option value=""></option>
					<c:forEach items="#{lstUser}" var="item">
						<option value="${item.id}">
							<c:out value="${item.username}" /> - <c:out value="${item.name}" />
						</option>
					</c:forEach>
				</select>
			</div>
		<div class="box-custom">
			<div class="row title-page" style="adding-bottom: 20px;">
				<h1>Thông tin chi tiết nhóm quyền</h1>
			</div>	
			
			<div class="Row">
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mã nhóm<font color="red">*</font></div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:input path = "sysRoles.code" cssClass="required uppercase ascii" />
	            </div>
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên nhóm<font color="red">*</font></div>
	            <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
	            	<form:input path = "sysRoles.descriptionVi" cssClass="required" title="Tên nhóm không được để trống"/>
	            </div>
	        </div>
	        <div class="Row">
	            <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Nhóm quyền cha</div>
	            <div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
	            	<select class="form-control" name="sysRoles.sysRoles.id" id="RoleList">
						<option value="">- Chọn -</option>
						<c:forEach items="#{danhSachNhomQuyen}" var="item">
							<option value="${item.id}">
								<c:out value="${item.code}" /> -
								<c:out value="${item.descriptionVi}" />
							</option>
						</c:forEach>
					</select>
	            </div>
	        </div>
	        
	        <div class="Row">
	        	<div class="easyui-panel" style="padding:5px">
	            	<ul id="danhSachQuyen" class="easyui-tree" data-options="animate:true,checkbox:true,cascadeCheck:true,lines:true"></ul>
	            </div>
	        </div>
	        <div class="box-custom row">
				<div class="col-md-6">
					<div class="row title-page" style="adding-bottom: 20px;">
						<h1>Danh sách người dùng theo nhóm quyền</h1>
					</div>
					<div class="table-responsive" >
						<table class="table table-bordered" id="table-service-product">
							<thead>
								<tr>
									<th>Người dùng</th>
								</tr>
							</thead>
							<tbody>
							</tbody>
						</table>
					</div>
				</div>
			</div>
	        <form:hidden path="sysRoles.rights"/>
		</div>
	</tiles:putAttribute> 
	</form:form>
	

	<tiles:putAttribute name="extra-scripts">
		<c:if test="${add}">
          	<script type="text/javascript">
          		tblCfg.buttons = [ 
		    		{ text: 'Thêm mới',attr:  {id:'btnDtAdd'}, className: 'mainGrid btnDtAdd btn blue', action: function ( e, dt, node, config ) { addNew(); } }
		        	];
          	</script>
		</c:if>
       <script type="text/javascript">
	        var createNew = false;
	        function initParam(tblCfg){			
	        	tblCfg.aoColumns = [			 
						{"sWidth" : "5%","sClass": "left","bSortable" : false,"sTitle":'STT'},
						{"sWidth" : "10%","sClass": "left","bSortable" : false,"sTitle":'Mã'},
						{"sWidth" : "20%","sClass": "left","bSortable" : false,"sTitle":'Tên'},
						{"sWidth" : "10%","sClass": "left","bSortable" : false,"sTitle":'Nhóm quyền cha'}
				];
			}
	        
	        function defaultValue(){
	        	document.getElementById("sysRoles.code").disabled = false;
	            $('#status').val(true);
	            $('#statustrue').prop('checked', true);
	           // formatLayout();
	        }
	        $(document).ready(function(){
	            // Khoi tao cay quyen
	            initRightTree();
	            var windowWidth = window.innerWidth;
	            window.onresize = function(event) {
	                if(windowWidth > 740 && $('#divGrid').css('display') == 'none')
	                    $('#divDetail').css('display', 'inline');
	                else if(windowWidth < 740 && $('#divGrid').css('display') == 'none')
	                    $('#divDetail').css('display', 'inline-grid');
	            };
	            initTableSerProduct();
	        });
	        // build lai combo
	        function instanceValidate(){    
	            if($('#id').val()!=''){// Sua
	                if($('#parentId').val()==$('#id').val()){// id cha trung id con
	                    alert('<s:text name="quyen_cha_khong_duoc_trung_voi_quyen_con"/>');
	                    return false;
	                }
	            }            
	            return true;
	        }
	        function instanceSuccess(data){
	            console.log('data response : '+data);
	            if("ConstraintViolationException" == data){
	                alert('Dữ liệu đã tồn tại!');
	                return;
	            }else if(data != ''){
	                alert(data);
	                return;
	            }else{
	                alert(thuc_hien_thanh_cong,function(){
	                    findData();
	    
	                    $('#divGrid').css('display', 'inline');                    
	                    $('#divDetail').css('display', 'none');
	                    $.ajax({
	                            url:$('#theForm').attr('action') + '?method=reload&id=' + $('#id').val(),
	                            method: 'GET',
	                            data:{
	                                tokenId:$('#tokenId').val(),
	                                tokenIdKey:$('#tokenIdKey').val()
	                            }
	                            ,success: function(severtrave){
	                               $('#parentId').html(severtrave);
	                            }
	                        });    
	                    });
	                }
	               
	            
	        }
	        function afterDelete()
	        {
	            $.ajax({
	                url:$('#theForm').attr('action') + '?method=reload&id=' + $('#id'),
	                method: 'GET',
	                success: function(severtrave){
	                   $('#parentId').html(severtrave);
	                }
	            });
	        }
	        //end build
	        
	        var myJSON;
	        function initRightTree(){
	            $('#danhSachQuyen').tree('reload');
	            $.ajax( {
	                async : false, type : "GET"
	                , url : $('#theForm').attr('action') + '?method=treeRight'
	                ,data:{
	                    tokenId:$('#tokenId').val(),
	                    tokenIdKey:$('#tokenIdKey').val()
	                }                
	                ,success : function (data1) {
	                    myJSON = JSON.parse(data1);
	                    treeDanhSachQuyen = $('#danhSachQuyen').tree({
	                        data : myJSON
	                    });
	                },
	                error : function (data1) {
	                    
	                }
	            });
	        }
	        
	        function reloadTree()
	        {
	            $('#danhSachQuyen').tree('reload');
	            initRightTree();
	            $('#danhSachQuyen').tree({
	                data : myJSON.treeRight
	            });
	        }
	        
	        function formatLayout(){
	            //$('.dataTables_scrollHead').width($('#tblRight').width());
	            $('.dataTables_scrollHeadInner').width("100%");
	            //$('.dataTables_scrollHead').width($('#tblRight').width());
	            $('.dataTable.no-footer').css('width','100%');
	            var windowWidth = window.innerWidth;
	            if(windowWidth > 740 && $('#divGrid').css('display') == 'none')
	                    $('#divDetail').css('display', 'inline');
	            else if(windowWidth < 740 && $('#divGrid').css('display') == 'none')
	                $('#divDetail').css('display', 'inline-grid');
	        }
	        
	        var tableSerProduct = null;
	        function initTableSerProduct(){
				var rowTempServiceProduct = initRowTableSerProduct();
				var tableObject = new TFOnline.DataTable({
					id : 'table-service-product',
					jQueryUI : true,
					rowTemp : rowTempServiceProduct,
					hasCheck : true,
					delCaption : 'Xóa',
					addOveride:true,
                    delOveride:true,
					maxRow : 100 
				});
				tableSerProduct = tableObject;
				
			}
	        function initRowTableSerProduct() {
				var rowTemp = [];
				var listObject = "sysRoles";
				var strHidden  = '<input type="hidden" class="form-control" name="' + listObject + '.sysRolesUserses[].id.roleId" />';
				strHidden += '<input type="hidden" class="form-control" name="' + listObject + '.sysRolesUserses[].id.userId" />';
					rowTemp = [
							'<div class="line-table"><select onChange="checkDuplicate(this);" class="form-control catProductService required" name="' + listObject + '.sysRolesUserses[].sysUsers.id">'+$('#lstProduct').html()+'</select></div>'
							+ strHidden
							];
				return rowTemp;
			}
	        
	        function afterEdit(sid, res){ 
	        	document.getElementById("sysRoles.code").disabled = true;
	        	
	            //reload tree data
	            $('#danhSachQuyen').tree('reload');
	            $('#danhSachQuyen').tree({
	                data : myJSON.treeRight
	            });
	            var arrRight = $('input[name="sysRoles.rights"]').val().split(',');
	            for(var i=0; i< arrRight.length; i++){
	                if(arrRight[i] != null && arrRight[i] != '' && arrRight[i] != 'null')
	                {
	                    //var node = $('#danhSachQuyen').tree('find', arrRight[i].trim());
	                    //$('#danhSachQuyen').tree('check', node.target);
	                    var allChildrenNode = $('#danhSachQuyen').tree('getChildren');
	                    for(var x = 0; x < allChildrenNode.length; x++){
	                        if(allChildrenNode[x].children == null){                            
	                            if(allChildrenNode[x].id == arrRight[i]){
	                                $('#danhSachQuyen').tree('check', allChildrenNode[x].target);                                
	                            }
	                        }
	                    }
	                }
	            }
	           // formatLayout();   
	            //$("#spartnerId option[value='']").remove();
	            tableSerProduct.deleteAllRow();
				var _partConnectIp = res.sysRolesUserses;
				if(_partConnectIp != null ){
					for (var i = 0; i < _partConnectIp.length; i++) {
						tableSerProduct.addRow(tableSerProduct.rowTemp);
							$('input[type="hidden"][name="sysRoles.sysRolesUserses['+ i +'].id.userId"]').val(_partConnectIp[i].id.userId);
							$('input[type="hidden"][name="sysRoles.sysRolesUserses['+ i +'].id.roleId"]').val(_partConnectIp[i].id.roleId);
							$('select[name="sysRoles.sysRolesUserses['+i+'].sysUsers.id"]').val(_partConnectIp[i].id.userId);
							$('select[name="sysRoles.sysRolesUserses['+i+'].sysUsers.id"]').select2();
					}
				}
	        }
	        function expand(idquyen, pathQuyen){
	            if($('#tr'+pathQuyen).hasClass('open')){
	                $('#tr'+pathQuyen).removeClass('open');
	                $('#tr'+pathQuyen).addClass('close');
	                $('#fnt'+pathQuyen).html('(+) ');
	            }else{
	                $('#tr'+pathQuyen).removeClass('close');
	                $('#tr'+pathQuyen).addClass('open');
	                $('#fnt'+pathQuyen).html('(-) ');
	            }
	            if($('#tr'+pathQuyen).hasClass('close'))// Truong hop dong                
	                $('tr[id^="tr' + pathQuyen + '"][id!="tr'+pathQuyen+'"]').css('display','none');
	            else{// Truong hop mo, mo de quy
	                openRecur(idquyen, pathQuyen);
	            }
	            //formatLayout();
	            
	        }
	        function openRecur(idquyen, pathQuyen){
	            if($('#tr'+pathQuyen).hasClass('close'))
	                return;
	            $('.'+idquyen).each(function(){
	                $(this).css('display','table-row');
	                if($(this).hasClass('trparent')){
	                    var trId = $(this).attr('id');
	                    var childIdquyen = trId.split(pathQuyen+'_')[1];
	                    var childPathQuyen = pathQuyen + '_'+childIdquyen;
	                    openRecur(childIdquyen,childPathQuyen);
	                }
	            });
	        }
	        function selectRight(idQuyen,pathQuyen, isSelect){
	            $('input[type="checkbox"][id^="chk' + pathQuyen + '"]').prop('checked',isSelect);
	        }
	        function beforeSave(){
	        	document.getElementById("sysRoles.code").disabled = false;
	        	
	            //var nodes = $('#danhSachQuyen').tree('getChecked', ['checked','indeterminate']);
	            var nodes = $('#danhSachQuyen').tree('getChecked', ['checked']);
	            var rights = '';
	            for(var i=0; i<nodes.length; i++){
	                    if (rights != '') rights += ',';
	                    rights += nodes[i].id;
	            }
	            $('input[name="sysRoles.rights"]').val(rights);
	        }
	        function extraClear(){
	            $('td input[type="checkbox"][id^="chk"]').prop('checked',false);
	        }
	    </script>
    </tiles:putAttribute>
	
</tiles:insertDefinition>