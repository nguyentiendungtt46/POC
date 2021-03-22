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
<%
       ApplicationContext appContext = (ApplicationContext)request.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT);
       SysUsers user = null;
       if(appContext!=null)
        user = (SysUsers)appContext.getAttribute(ApplicationContext.USER);
       WebApplicationContext ac = RequestContextUtils.findWebApplicationContext(request,null);
       SysParamDao sysParamDao = (SysParamDao) ac.getBean("sysParamDao");
       entity.frwk.SysParam LDAP_AUTHEN =  sysParamDao.getSysParamByCode("LDAP_AUTHEN");
%>
                       
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Quản lý người dùng"/> 
	<tiles:putAttribute name="formInf">
		<spring:url value="/manageUser" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">	
				<div class="row search-style">	
					<div class="Table" id = "divSearchInf">
		                <div class="Row">
		                    <div class="row title-page" style="adding-bottom: 20px;">
								<h1>Danh sách người dùng</h1>
							</div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên đăng nhập</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                        <form:input path="strusername" id="strusername"></form:input>
		                    </div>
		                    <div class="Empty"></div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên người dùng</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
		                    	<form:input path="strname" id="strname"></form:input>
		                    </div>
		                </div>
		                <div class="Row">
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn vị</div>
		                    <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
			                	<form:select class="form-control" path="job_name" title="Chọn đơn vị">
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
		                </div>
		                <!-- <div align="center" class="HeaderText">&#8203;</div> -->
		                <div class="divaction" align="center">
		                    <input class="btn blue" type="button" onclick="findData();" value="Tìm kiếm"/>
		                    <c:if test="${add}">
				            	<input class="btnDtAdd btn blue" type="button" id="btnDtAdd" onclick="addNew();" value="Thêm mới" />
							</c:if>
		                </div>
		            </div>  
				</div>    
		        <%@ include file="/page/include/data_table.jsp"%>
		    </div>
		</tiles:putAttribute>
		
	<tiles:putAttribute name="catDetail" cascade="true">
		<form:hidden path="su.id" id="id"/> 
        <form:hidden path="su.roles" id="roles"/>
        <form:hidden path="su.pwdDatestr" id="pwdDatestr"/>
        <div style="display: none;">
        	<form:select  path="su.agencyStructureId" id="agencyStructureId"  >
				<c:forEach items="#{dsCatAgencyStructure}" var="item">
					<option value="${item.id}"><c:out value="${item.code}" /> - <c:out value="${item.name}" /></option>
				</c:forEach>
			</form:select>
		</div>
        <div class="box-custom">
        	<div class="Row">
        		<div class="row title-page" style="adding-bottom: 20px;">
					<h1>Thông tin chi tiết người dùng</h1>
				</div>   
                <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên</div>
                <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
                	<form:input path="su.name" title="Tên không được để trống"></form:input>
                </div>
                <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tên đăng nhập<font color="red">*</font></div>
            	<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
            		<form:input path="su.username" cssClass="required ascii lowercase" ></form:input>
            	</div> 
            </div>
            <div class="Row">
            	<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn vị<font color="red">*</font></div>
                <div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
                	<form:select onchange="checkMaxUser();" class="form-control required" path="su.departmentId" title="Chọn đơn vị">
						<option value="">- Chọn -</option>
						<c:forEach items="#{dsDonVi}" var="item">
							<option value="${item.id}">
								<c:out value="${item.code}" /> -
								<c:out value="${item.name}" />
							</option>
						</c:forEach>
					</form:select>
                </div>
                <%-- <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Phòng ban</div>
                <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
                    <select class="form-control" name="su.departmentId" id="DepartmentList">
						<option value="">- Chọn -</option>
						<c:forEach items="#{dsGender}" var="item">
							<option value="${item.id}">
								<c:out value="${item.code}" /> -
								<c:out value="${item.value}" />
							</option>
						</c:forEach>
					</select>
                </div> --%>
            </div>
            <div class="row">
					<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đại lý</div>
					<div class="col-md-8 col-lg-8 col-sm-9 col-xs-9">
						<input type="text" id="ent_part_idx_parent" name="catAgencyStructure.fullPath" readonly="readonly"/>
					</div>
					<div class="col-md-2 col-lg-2 col-sm-3 col-xs-3" style="    padding: 0;">
						<input class="btn blue" type="button" onclick="viewModal();" value="Chọn" style="    margin-top: -7px;" />
						<input class="btn red" type="button" onclick="resetParent();" value="Xóa" style="    margin-top: -7px;" />
					</div>
				</div>
            <div class="Row">
                <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Điện thoại</div>
                <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
                	<form:input path="su.phone" onkeypress="return onlyNumber(event);"></form:input>
                </div>
                <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Di động</div>
                <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
                	<form:input path="su.cellPhone" onkeypress="return onlyNumber(event);" title="Di động không được để trống"></form:input>
                </div>
            </div>	
            <div class="Row">
                <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Email</div>
                <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
                	<form:input path="su.email" cssClass="email"></form:input>
                </div>
                <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mật khẩu</div>
                 <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
                <form:input path="su.passWordPlainText" disabled="true"></form:input>
                <a id="btnResetPass" href="#" onclick="resetPass()">Đặt lại mật khẩu</a>
                </div>
            </div>	
            <div class="Row">
                <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Kích hoạt</div>
                <div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
                <form:checkbox path="su.active" value="true"></form:checkbox>
                </div>
            </div>
            <div class="Row">
            	<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12">
            		<div class="row title-page" style="adding-bottom: 20px;">
						<h6>Danh sách quyền</h6>
					</div>
            		<div class="easyui-panel" style="padding:5px">
                    	<ul id="danhSachQuyen" class="easyui-tree" data-options="animate:true,checkbox:true,cascadeCheck:true,lines:true"></ul>
                	</div>
            	</div>  
                
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
	
	<script src="<spring:url value="/page/qtht/quan_ly_nguoi_dung.js" />"></script>
    
	<tiles:putAttribute name="extra-scripts">
       <script type="text/javascript">
	        var createNew = false;
	        function initParam(tblCfg){		
	        	tblCfg.bFilter= false;
	        	tblCfg.aoColumns = [
	                {
	                    "sClass" : "left", "bSortable" : false, "sTitle" : 'STT'
	                },
	                {
	                    "sClass" : "left", "bSortable" : false, "sTitle" : 'Tên đăng nhập'
	                },
	                {
	                    "sClass" : "left", "bSortable" : false, "sTitle" : 'Tên người dùng'
	                },
	                {
	                    "sClass" : "left", "bSortable" : false, "sTitle" : 'Vị Trí'
	                },
	                {
	                    "sClass" : "left", "bSortable" : false, "sTitle" : 'TCTD'
	                },
	                {
	                    "sClass" : "left", "bSortable" : false, "sTitle" : 'Điện thoại'
	                }
	                /* ,
	                {
	                    "sClass" : "left", "bSortable" : false, "sTitle" : 'CMT'
	                } */
	        ];
			}
	        var listBaseStateRMCU = [];
	        var initRmcodeUrl = '', tblDataRmcode;
	        $(document).ready(function () {
	            initRmcodeUrl = $('#theForm').attr('action') + '?method=initTableRmcode';
	            $('input[type="checkbox"]').click(function () {
	                $(this).val($(this).prop('checked'));
	            });
	            $('#sTab01').css('display', 'inline');
	            $('#sTab02').css('display', 'none');
	            $('#tab01').addClass("current");
	            $('#tab02').removeClass("current");
	            $('#tab01').css('color', '#006ecb');
	            $('#tab02').css('color', '#8E8E8E');
	            
	        });

	        $(document).ready(function() {
			     $('.btnDtDelete').hide();
			     $('.btnDtAdd').click(function() {
			    	 $('input[name="su.username"]').attr("readonly",false);
			     });
			});
	        
	        function exportThongTinNguoiDung(){
	        	var id = $('#id').val();
				window.open($('#theForm').attr('action') + '?method=ExportFileExcel&id=' + id);
			}
	        function exportExcel(){
	        	var username = $('#strusername').val();
	        	var fullname = $('#strname').val();
	        	var partner = $('#job_name').val();
				window.open($('#theForm').attr('action') + '?method=exportExcel&username=' + username + '&fullname='+fullname+'&partner='+partner);
			}
	        function loadTableRmcode(id, companyId) {
	            var tokenIdKey = $('#tokenIdKey').val();
	            var tokenId = $('#tokenId').val();
	           
	            tblDataRmcode = $('#tblDsNoiDung123').dataTable( {
	                "bJQueryUI" : true, 
	                "sPaginationType" : "full_numbers", 
	                "iDisplayLength" : 10,
	                "bProcessing" : true, 
	                "bFilter" : bFilter, 
	                "bServerSide" : true, 
	                "sAjaxSource" : initRmcodeUrl, 
	                "ordering": false,
	                "fnServerData" : function (initRmcodeUrl, aoData, fnCallback) {
	                    addFormData(aoData, $('#theForm').serializeObject()); 
	                    aoData.push({name: "userId" , value : id});
	                    aoData.push({name: "tokenIdKey", value : tokenIdKey});
	                    aoData.push({name: "tokenId", value : tokenId});
	                    aoData.push({name: "companyId", value : companyId});
	                    $.ajax( {
	                        "dataType" : 'json', 
	                        "type" : "POST", 
	                        "url" : initRmcodeUrl, 
	                        "data" : aoData, 
	                        "success" : function (result) {
	                            fnCallback(result);
	                            try {
	                                instanceUseResult(result);
	                                
	                            }
	                            catch (err) {
	                                // console.print('Instance does not use result more');
	                            }
	                            $.loader("close");
	                            $(".date").datepicker({ 
	                                dateFormat: 'dd/mm/yy',
	                        showButtonPanel: true,
	                                changeMonth: true,
	                                changeYear: true,
	                                yearRange: "-50:+50"    
	                            });     
	                            initCheck();
	                            
	                        }
	                    });
	                },
	                "initComplete" : function (settings, json) {
	                        try {
	                            instanceFindComplete(tblDataRmcode.fnGetData().length);
	                            $("#input-datepicker").datepicker({ 
	                                dateFormat: 'dd/mm/yy',
	                                changeMonth: true,
	                                changeYear: true,
	                                showButtonPanel: true,
	                                yearRange: "-50:+50" 
	                            });     
	                        }
	                        catch (err) {
	            
	                        }
	                    },
	                "oLanguage" :  {
	                        "sLengthMenu" : hien_thi + "_MENU_" + ban_ghi, 
	                        "sZeroRecords" : " ", 
	                        "sInfo" : hien_thi + " _START_ " + den + " _END_ " + cua + " _TOTAL_ " + ban_ghi, 
	                        "sInfoEmpty" : hien_thi + " " + tu + " 0 " + toi + " 0 " + tren_tong_so + " 0 " + ban_ghi, 
	                        "sInfoFiltered" : "( " + da_loc_tu + " _MAX_ " + tong_so_ban_ghi + " )", 
	                        "oPaginate" :  {
	                            "sFirst" : "&#xf049;", "sLast" : "&#xf050;", "sEnter" : "&#xf061;", "sPrevious" : "&#xf04a;", "sNext" : "&#xf04e;"
	                        },
	                        "sSearch" : tu_khoa
	                    },
	                "bDestroy" : true,
	                scrollX : bScrollX, 
	                "fnDrawCallback" : function (oSettings) {
	                        if (typeof fnDrawCallback != typeof undefined) {
	                            fnDrawCallback(oSettings);
	                        }
	                    }
	                });
	        }


	        function updateDataTableRm(){
	            
	        }

	        function editRmcodeUser(id){
	            $.loader( {
	                className : "blue-with-image-2"
	            });
	            clearDiv('divDetail');
	            var tokenIdKey = $('#tokenIdKey').val();
	            var tokenId = $('#tokenId').val();
	            $.getJSON($('#theForm').attr('action') + '?method=editRmcodeUser', 
	            {
	                "id" : id, "tokenIdKey" : tokenIdKey, "tokenId" : tokenId
	            }).done(function (res,status, xhr) {
	                var result = chkJson(res, xhr);
	                if(!result){
	                    alert(res);
	                    return;
	                }
	                listBaseStateRMCU = JSON.parse(res.rmCodeUsesBinding);
	                loadTableRmcode(id);
	                $('#btnDel').css('display', '');
	                $('#divGrid').css('display', 'none');
	                $('#divDetail').css('display', 'inline');
	                $("#id").val(res.id);
	                $.loader('close');
	            }).error(function (res) {
	                alert(res.responseText, function(){
	                     $.loader('close');
	                });
	            });
	            
	        }

	        function editUser(id) {
	            $.loader( {
	                className : "blue-with-image-2"
	            });
	            clearDiv('divDetail');
	            var tokenIdKey = $('#tokenIdKey').val();
	            var tokenId = $('#tokenId').val();
	            $.getJSON(editUrl, 
	            {
	                "id" : id, "tokenIdKey" : tokenIdKey, "tokenId" : tokenId
	            }).done(function (res) {
	                if (typeof beforeEdit == 'function') {
	                    beforeEdit(res);
	                }
	                for (var prop in res) {
	                    // select
	                    $("select[name$='." + prop + "']").each(function () {
	                        var temp = res[prop];
	                        if (temp === true)
	                            temp = 'true';
	                        if (temp === false)
	                            temp = 'false';
	                        $(this).val(temp);
	                    });

	                    // input, textarea
	                    $("input[name$='." + prop + "'][type!='radio']" + ", textarea[name$='." + prop + "']").each(function () {
	                        $(this).val(res[prop]);
	                    });

	                    // radio
	                    $('input[type="radio"][name$=".' + prop + '"]').each(function () {
	                        // Gan gia tri
	                        //$(this).val(res[prop]);
	                        // Check theo id
	                        //$('#'+prop+res[prop]).prop('checked', true);
	                        $('input[name$=".' + prop + '"][value="' + res[prop] + '"]').prop('checked', true);
	                    });

	                    // checkbox
	                    $('input[type="checkbox"][name$=".' + prop + '"]').each(function () {
	                        $(this).prop('checked', res[prop]);
	                    });
	                }
	                //select2
	                $('#divDetail select').each(function (iIndex) {
	                    $(this).select2();
	                });
	                $('#btnDel').css('display', 'none');
	                $('#divGrid').css('display', 'none');
	                $('#divDetail').css('display', 'inline');

	                if (typeof afterEdit == 'function')
	                    afterEdit(id, res);
	                $.loader('close');
	            });
	        }

	        function initCheck() {
	            for (var i = 0;i < listBaseStateRMCU.length;i++) {
	                    var itemdata = listBaseStateRMCU[i];
	                    $("#"+ itemdata.rmCd).checked = itemdata.check;
	                    $("#st-"+ itemdata.rmCd).checked = itemdata.startDt;
	                    $("#en-"+ itemdata.rmCd).checked = itemdata.expireDt;
	                    $("#mt-"+ itemdata.rmCd).checked = itemdata.masterRM;
	                    if (!itemdata.check) {
	                        $("#st-"+ itemdata.rmCd).css('display', 'none');
	                        $("#en-"+ itemdata.rmCd).css('display', 'none');
	                        $("#mt-"+ itemdata.rmCd).css('display', 'none');
	                    }
	                    else{
	                        $("#st-"+ itemdata.rmCd).css('display', 'block');
	                        $("#en-"+ itemdata.rmCd).css('display', 'block');
	                        $("#mt-"+ itemdata.rmCd).css('display', 'block');
	                    }
	                }
	            var checkall = true;
	            for(var i = 0;i < listBaseStateRMCU.length;i++){
	                if(listBaseStateRMCU[i].check == null || listBaseStateRMCU[i].check == false){
	                    checkall = false;
	                }
	            }
	            $('#sla').prop('checked', checkall);
	        }


	        function selectAll(isSlAll) {
	            $('.sli').prop('checked', isSlAll == true);
	            for (var i = 0;i < listBaseStateRMCU.length;i++) {
	                listBaseStateRMCU[i].check = isSlAll;
	            }
	            initCheck();

	        }

	        function selectItemMasterRM(ctrItem, index){
	             var item = listBaseStateRMCU[index];
	            item.ischange = true;
	            item.masterRM = ctrItem.checked;
	            if(ctrItem.checked){
	               for (var i = 0;i < listBaseStateRMCU.length;i++) {
	                var itemdata = listBaseStateRMCU[i];
	                if(item.rmCd != itemdata.rmCd){
	                    itemdata.masterRM = false;
	                    $('#mt-'+itemdata.rmCd).prop('checked', false);
	                }
	                } 
	            }
	            
	            
	        }

	        function selectItem(ctrItem, index) {
	            var item = listBaseStateRMCU[index];
	            if (ctrItem.checked) {
	                $("#st-"+ item.rmCd).css('display', 'block');
	                $("#en-"+ item.rmCd).css('display', 'block');
	                $("#mt-"+ item.rmCd).css('display', 'block');
	            }
	            else {
	                $("#st-"+ item.rmCd).css('display', 'none');
	                $("#en-"+ item.rmCd).css('display', 'none');
	                $("#mt-"+ item.rmCd).css('display', 'none');
	            }
	            $("#"+ item.rmCd).checked = ctrItem.checked;

	            item.ischange = true;
	            item.check = ctrItem.checked;
	            
	            var checkAll = true;
	            for(var i = 0; i < listBaseStateRMCU.length; i ++){
	                if(listBaseStateRMCU[i].check == false){
	                     checkAll = false;
	                }
	            }
	            $('#sla').prop('checked', checkAll);
	        }

	        function changeStDate(ctrItem, index) {
	            var item = listBaseStateRMCU[index];
	            item.startDt = ctrItem.value;
	        }

	        function changeEnDate(ctrItem, index) {
	            var item = listBaseStateRMCU[index];
	            item.expireDt = ctrItem.value;
	        }

	        function changeBranch(newBrId) {
	            var currentDepartmentId = $('select[name="su.departmentId"]').val();
	            $.ajax( {
	                url : $('#theForm').attr('action') + '?method=changeBranch', success : function (severtrave) {
	                    $('select[name="su.departmentId"]').html(severtrave);
	                    if (currentDepartmentId) {
	                        $('select[name="su.departmentId"]').val(currentDepartmentId);
	                        $('select[name="su.departmentId"]').select2();
	                    }
	                },
	                data :  {
	                    tokenId : $('#tokenId').val(), tokenIdKey : $('#tokenIdKey').val(), newBrId : newBrId

	                }

	            });
	        }
	        
	        function checkMaxUser() {
	        	 var currentPartner = $('select[name="su.companyId"]').val();
		            $.ajax( {
		                url : $('#theForm').attr('action') + '?method=checkMaxUser',
		                data :  {
		                    partnerId : currentPartner
		                },
		                success : function (res) {
		                	if ("" != res.trim())
		                		alert(res);
		                }
		            });
	        }



	        function loadcbHanMuc() {
	            reloadCbHanMuc('', '');
	            reloadCbHanMucQt('', '');
	        }

	        function defaultValue() {
	            $('#btnResetPass').css('display', 'none');
	            //$('#active').val(true);
	            $('input[name="su.active"]').prop('checked', true);
	            //$("#theForm_su_partnerId option[value='']").remove();
	            initRoleTree();
	            formatLayout();

	        }

	        /*function resetPass() {
	            if ($('input[name="su.email"]').val().trim() == "") {
	                alert('Vui ḷng nh?p ??a ch? Email');
	                return false;
	            }
	            else {
	                $.ajax( {
	                    url : $('#theForm').attr('action') + '?method=resetPass', method : 'POST', data :  {
	                        id : $('#id').val(), tokenId : $('#tokenId').val(), tokenIdKey : $('#tokenIdKey').val()
	                    },
	                    success : function (severtrave) {
	                        alert('<s:text name = "re_succ"/>');
	                    },
	                    err : function (severtrave) {
	                        alert('<s:text name = "no_reset"/>');
	                    }
	                });
	            }
	        }
	        */

	        function initRoleTree() {
	            var sUrl = $('#theForm').attr('action') + '?method=treeRole' + '&suID=' + $('#id').val();
	            if ($('select[name="su.jobId"]')!= undefined && $('select[name="su.jobId"]').val()!=undefined && $('select[name="su.jobId"]').val().trim() != '')
	                sUrl += '&parentRoleId=' + $('select[name="su.jobId"]').val().trim();
	            $.ajax( {
	                async : false, type : "POST", url : sUrl, data :  {
	                    tokenId : $('#tokenId').val(), tokenIdKey : $('#tokenIdKey').val()
	                },
	                success : function (data1) {
	                    var myJSON = JSON.parse(data1);
	                    $('#danhSachQuyen').tree( {
	                        data : myJSON.treeRoles
	                    });
	                },
	                error : function (data1) {

	                }
	            });
	        }

	        function formatLayout() {
	            //$('.dataTables_scrollHead').width($('#tblRight').width());
	            $('.dataTables_scrollHeadInner').width("100%");
	            //$('.dataTables_scrollHead').width($('#tblRight').width());
	            $('.dataTable.no-footer').css('width', '100%');
	        }

	        function changeRootRole() {
	            initRoleTree($('#input[name="su.jobId"]').val());
	            var arrRight = $('input[name="su.roles"]').val().split(',');
	            for (var i = 0;i < arrRight.length;i++) {
	                $('td input[type="checkbox"][id^="chk"][id$="' + arrRight[i].trim() + '"]').prop('checked', true);
	            }
	            
	        }

	        function afterEdit(sid, res) {
	            $('#sTab01').css('display', 'inline');
	            $('#sTab02').css('display', 'none');
	            $('#tab01').addClass("current");
	            $('#tab02').removeClass("current");
	            $('#tab01').css('color', '#006ecb');
	            $('#tab02').css('color', '#8E8E8E');
	            initRoleTree($('#input[name="su.jobId"]').val());
	            $('#btnResetPass').css('display', 'inline');
	            /*var arrRight = $('input[name="su.roles"]').val().split(',');
	                for(var i=0; i< arrRight.length; i++){
	                    $('td input[type="checkbox"][id^="chk"][id$="'+arrRight[i].trim()+'"]').prop('checked',true);
	                }*/
	            formatLayout();
	            $('input[name="su.username"]').attr("readonly",true);
	            //$('#spartnerId').val($('#hidPartnerId').val());
	            if (res.agencyStructureId != null && res.agencyStructureId != null && res.agencyStructureId != "")
					$('#ent_part_idx_parent').val($('#agencyStructureId option:selected').text());
	        }

	        function reloadCbHanMucQt(hanmucGiaoDich, hanMucNgay) {
	            // Truong hop Vietin nhap nsd cho doi tac can rebuil combo suhanMucGiaoDichId suhanMucNgayid
	            $.ajax( {
	                async : false, type : "POST", url : 'LtdCategory.action?method=getHm&typex=qt&partnerId=' + $('select[name="su.partnerId"]').val(), success : function (res) {
	                    $('#hanMucGiaoDichChuyenTienQTId').html(res['hanMucGd']);
	                    $('#hanMucNgayChuyenTienQTId').html(res['hanMucNgay']);
	                    $('#hanMucGiaoDichChuyenTienQTId').val(hanmucGiaoDich);
	                    $('#hanMucNgayChuyenTienQTId').val(hanMucNgay);

	                },
	                error : function (data1) {

	                }
	            });
	        }

	        function reloadCbHanMuc(hanmucGiaoDich, hanMucNgay) {
	            // Truong hop Vietin nhap nsd cho doi tac can rebuil combo suhanMucGiaoDichId suhanMucNgayid
	            $.ajax( {
	                async : false, type : "POST", url : 'LtdCategory.action?method=getHm&partnerId=' + $('select[name="su.partnerId"]').val(), success : function (res) {
	                    $('#suhanMucGiaoDichId').html(res['hanMucGd']);
	                    $('#suhanMucNgayid').html(res['hanMucNgay']);
	                    $('#suhanMucGiaoDichId').val(hanmucGiaoDich);
	                    $('#suhanMucNgayid').val(hanMucNgay);

	                },
	                error : function (data1) {

	                }
	            });
	        }

	        function expand(idquyen, pathQuyen) {
	            if ($('#tr' + pathQuyen).hasClass('open')) {
	                $('#tr' + pathQuyen).removeClass('open');
	                $('#tr' + pathQuyen).addClass('close');
	                $('#fnt' + pathQuyen).html('(-) ');
	            }
	            else {
	                $('#tr' + pathQuyen).removeClass('close');
	                $('#tr' + pathQuyen).addClass('open');
	                $('#fnt' + pathQuyen).html('(+) ');
	            }
	            if ($('#tr' + pathQuyen).hasClass('close'))// Truong hop dong                
	                $('tr[id^="tr' + pathQuyen + '"][id!="tr' + pathQuyen + '"]').css('display', 'none');
	            else {
	                // Truong hop mo, mo de quy
	                openRecur(idquyen, pathQuyen);
	            }
	            formatLayout();
	        }

	        function openRecur(idquyen, pathQuyen) {
	            if ($('#tr' + pathQuyen).hasClass('close'))
	                return;
	            $('.' + idquyen).each(function () {
	                $(this).css('display', 'table-row');
	                if ($(this).hasClass('trparent')) {
	                    var trId = $(this).attr('id');
	                    var childIdquyen = trId.split(pathQuyen + '_')[1];
	                    var childPathQuyen = pathQuyen + '_' + childIdquyen;
	                    openRecur(childIdquyen, childPathQuyen);
	                }
	            });
	        }

	        function selectRight(idQuyen, pathQuyen, isSelect) {
	            $('input[type="checkbox"][id^="chk' + pathQuyen + '"]').prop('checked', isSelect);
	        }

	        function beforeSave() {
	            /*var rights="";
	                    $( "td input:checked" ).each(function(){
	                        rights+= $(this).attr('class') + ",";
	                    });*/
	            //var nodes = $('#danhSachQuyen').tree('getChecked', ['checked', 'indeterminate']);
	            var nodes = $('#danhSachQuyen').tree('getChecked', ['checked']);
	            var rights = '';
	            for (var i = 0;i < nodes.length;i++) {
	                if (rights != '')
	                    rights += ',';
	                rights += nodes[i].id;
	            }
	            $('input[name="su.roles"]').val(rights);
	            
	            
	        }

	        function extraClear() {
	            $('td input[type="checkbox"][id^="chk"]').prop('checked', false);
	        }


	        function LoadDeviceSecurity(partnerCode) {
	            $.loader( {
	                className : "blue-with-image-2"
	            });
	            $.ajax( {
	                url : $('#theForm').attr('action') + '?method=loadDevice&partnerCode=' + partnerCode, method : 'POST', success : function (severtrave) {
	                    $('input[name="secDiviceCode"]').val(severtrave);
	                    $('#txtUsername').val($('input[name="su.username"]').val());
	                    $('#txtName').val($('input[name="su.name"]').val());
	                    $('#oldPIN').val($('input[name="su.pin"]').val());
	                    $.loader("close");
	                },
	                error : function () {
	                    alert('Không k?t n?i ???c v?i Server vui ḷng liên h? v?i qu?n tr? h? th?ng!');
	                    $.loader("close");
	                }
	            });
	        }

	        function ValidateData() {
	            var mess = '<s:text name="required"/>';
	            if ($('input[name="su.secDiviceCode"]').val() == '') {
	                $('input[name="su.secDiviceCode"]').attr("placeholder", mess);
	                return false;
	            }
	            if ($('input[name="su.pin"]').val() == '') {
	                $('input[name="su.pin"]').attr("placeholder", mess);
	                return false;
	            }
	            else {
	                if ($('input[name="su.pin"]').val().length != 6) {
	                    alert('Mă PIN ph?i g?m 6 ch? s?.');
	                    return false;
	                }
	                else if (!$.isNumeric($('input[name="su.pin"]').val())) {
	                    alert('Mă PIN ph?i g?m 6 ch? s?.');
	                    return false;
	                }
	            }
	            return true;
	        }

	        function changePINRSA() {
	            if ($('input[name="su.pin"]').val() == $('#oldPIN').val())
	                alert('Vui ḷng thay ??i thông tin.');
	            else 
	                $("#modal_OTP").modal();
	        }

	        function changePIN(OTP) {
	            var username = $('#txtUsername').val();
	            var Pin = $('input[name="su.pin"]').val();;

	            $.loader( {
	                className : "blue-with-image-2"
	            });

	            $.ajax( {
	                url : $('#theForm').attr('action') + '?method=changePIN&UserID=' + $('#id').val() + '&oldPin=' + $('#oldPIN').val() + '&PIN=' + Pin + '&username=' + username + '&OTP=' + OTP, method : 'POST', success : function (severtrave) {
	                    if (severtrave == "0") {
	                        alert(thuc_hien_thanh_cong, function () {
	                            $("#modal_OTP").modal('hide');
	                        });
	                    }
	                    else 
	                        alert(severtrave);

	                    $.loader("close");
	                },
	                error : function () {
	                    alert('Không xác th?c ???c mă OTP');
	                    $.loader("close");
	                }
	            });
	        }

	        function subscriberRSA() {
	            $.loader( {
	                className : "blue-with-image-2"
	            });
	            var username = $('#txtUsername').val();
	            var name = $('#txtName').val();
	            var secDiviceCode = $('input[name="su.secDiviceCode"]').val();
	            var Pin = $('input[name="su.pin"]').val();
	            var errorMess1 = '<s:text name="PRINCIPAL_USERID_EXISTS_IN_REALM"/>';
	            var errorMess2 = '<s:text name="TOKENMGT_TOKEN_ALREADY_ASSIGNED"/>';
	            $.ajax( {
	                url : $('#theForm').attr('action') + '?method=subscriber&action=0&UserID=' + $('#id').val() + '&username=' + username + '&lastname=' + name + '&secDiviceCode=' + secDiviceCode + '&Pin=' + Pin, method : 'POST', success : function (severtrave) {
	                    //alert('??ng kư thành công.');
	                    if (severtrave == "OTP") {
	                        // UAT: change PIN
	                        //                        $('#modal_OTP').modal();
	                        // PROD: user ko su dung PIN khi xac thuc RSA
	                        alert('??ng kư thành công.');
	                    }
	                    else {
	                        if (severtrave == 'PRINCIPAL_USERID_EXISTS_IN_REALM')
	                            alert(errorMess1);
	                        else if (severtrave == 'TOKENMGT_TOKEN_ALREADY_ASSIGNED')
	                            alert(errorMess2);
	                        else 
	                            alert(severtrave);
	                    }

	                    $.loader("close");
	                },
	                error : function () {
	                    alert('??ng kư không thành công.');
	                    $.loader("close");
	                }
	            });
	        }

	        function unsubscriber() {
	            $.loader( {
	                className : "blue-with-image-2"
	            });
	            var username = $('#txtUsername').val();
	            var name = $('#txtName').val();
	            var secDiviceCode = $('input[name="su.secDiviceCode"]').val();
	            var Pin = $('input[name="su.pin"]').val();
	            var errorMess1 = '<s:text name="PRINCIPAL_USERID_EXISTS_IN_REALM"/>';
	            var errorMess2 = '<s:text name="TOKENMGT_TOKEN_ALREADY_ASSIGNED"/>';
	            $.ajax( {
	                url : $('#theForm').attr('action') + '?method=unsubscriber&action=0&UserID=' + $('#id').val() + '&username=' + username + '&lastname=' + name + '&secDiviceCode=' + secDiviceCode + '&Pin=' + Pin, method : 'POST', success : function (severtrave) {
	                    if (severtrave == "SUCCESS") {
	                        alert('H?y ??ng kư thành công.');
	                        findData();
	                        $('#divGrid').css('display', 'inline');
	                        $('#divDetail').css('display', 'none');
	                    }
	                    else {
	                        alert(severtrave);
	                    }
	                    $.loader("close");
	                },
	                error : function () {
	                    alert('L?i không xác ??nh.');
	                    $.loader("close");
	                }
	            });
	        }

	        function subscriber() {
	            if (ValidateData()) {
	                subscriberRSA();

	            }
	            else 
	                return false;
	        }

	        function btnSave_Rmcode_click(){
	            var listChange = [];
	            for (var i = 0;i < listBaseStateRMCU.length;i++) {
	                if (!listBaseStateRMCU[i].check) {
	                    continue;
	                }
	                var item = {
	                    id : listBaseStateRMCU[i].id
	                    , startDt : listBaseStateRMCU[i].startDt
	                    , check : listBaseStateRMCU[i].check
	                    , expireDt : listBaseStateRMCU[i].expireDt
	                    , userad : listBaseStateRMCU[i].userad
	                    , rmCd : listBaseStateRMCU[i].rmCd
	                    , masterRM : listBaseStateRMCU[i].masterRM
	                };
	                listChange.push(item);
	            }
	            var data = [];
	            data.push({name: 'id', value: $("#id").val()});
	            data.push({name: 'listRmcodeChange', value: JSON.stringify(listChange)});
	            $.ajax( {
	                async : false, 
	                type : "POST", 
	                url : $('#theForm').attr('action') + '?method=saveRmcodeUser',
	                data : data, 
	                success : function (data1, status, xhr) {
	                   $.loader("close");
	                    var jsnResult = chkJson(data1, xhr);
	                    // Khong tra ve json, truong hop tra ve loi hoac khong giu nguyen man hinh
	                    if (!jsnResult) {
	                        if (data1.trim() != '') {
	                            alert(data1);
	                        }
	                        // Thuc hien thanh cong thi data=''           
	                        else {
	                            alert(thuc_hien_thanh_cong, function () {
	                                findData();
	                                $('#divGrid').css('display', 'inline');
	                                $('#divDetail').css('display', 'none');
	                            });
	                        }
	                    }
	                    else{
	                        findData();
	                        if (typeof instanceSuccess == 'function') {
	                            instanceSuccess(data1, xhr);
	                            return;
	                        }
	                        alert(thuc_hien_thanh_cong, function () {    
	                            if(closeOnSaveSuccess){
	                                $('#divGrid').css('display', 'inline');
	                                $('#divDetail').css('display', 'none');
	                            }else{
	                            	binding(res);
	                            }
	                            	
	                        });
	                    }
	                },
	                error : function (data1) {
	                    
	                    $.loader("close");
	                    var result = chkJson(data1);
	                    if (typeof instanceSaveFalse == 'function') {
	                        instanceSaveFalse(!result ? data1 : result);
	                        return;
	                    }

	                    alert(data1);
	                }
	            });
	            
	        }
	        function resetPass(){
	        	$.loader( {
	                className : "blue-with-image-2"
	            });
	        	$.ajax({
	        		url: $('#theForm').attr('action') + '?method=resetPass',
	        		data:{
	        			"su.id": $('#id').val()
	        		},
	        		success: function(data, status, xhr){
	        			$.loader("close");
	        			var jsnResult = chkJson(data, xhr);
	        			if(jsnResult){
	        				alert('Thực hiện thành công!', function(){
	        					$('input[name="su.passWordPlainText"]').val(data['passWordPlainText']);
	        				});
	        			}
	        		},
	        		error:function(data){
	        			$.loader("close");
	        			alert('Thực hiện không thành công');
	        		}
	        	});
	        	
	        }
	        function btnSave_click() {
	            $('#id').prop('disabled', false);
	            try {
	                // Cac thao tac thuc hien truoc khi validate
	                beforeValidate();
	            }
	            catch (err) {
	            }
	            if (!$('#theForm').valid())
	                return;
	            try {

	                if (!instanceValidate())
	                    return;

	            }
	            catch (err) {
	            }
	            try {
	                // Cac thao tac thuc hien truoc khi validate
	                beforeSave();
	            }
	            catch (err) {
	            }
	            $('input[type="checkbox"]').each(function () {
	                $(this).val($(this).prop('checked'));
	            });
	            $('input[type="radio"]').each(function () {
	                try {
	                    if ($(this).prop('checked')) {
	                        var arrName = $(this).attr('name').split('.');
	                        var realId = arrName[arrName.length - 1];
	                        $(this).val($(this).attr('id').substring(realId.length));
	                    }
	                }
	                catch (err) {
	                }

	            });
	            $.loader( {
	                className : "blue-with-image-2"
	            });
	            $.ajax( {
	                async : false, type : "POST", url : saveUrl, data : $('#theForm').serialize(), success : function (data) {
	                    $.loader("close");
	                    try {
	                        instanceSuccess(data);
	                        return;
	                    }
	                    catch (err1) {
	                    }
	                    if (data.trim() != '') {
	                        // Thuc hien thanh cong thi data=''                 
	                        alert(data);
	                    }
	                    else {
	                        if ($('#id').val() == '') {
	                            $('#sTab02').css('display', 'inline');
	                            $('#sTab01').css('display', 'inline');
	                            $('#tab02').addClass("current");
	                            LoadDeviceSecurity($('select[name="su.partnerId"]').val());
	                        }
	                        alert(thuc_hien_thanh_cong, function () {
	                            findData();
	                            $('#divGrid').css('display', 'inline');
	                            $('#divDetail').css('display', 'none');
	                        });
	                    }
	                },
	                error : function (data) {
	                    $.loader("close");
	                    alert(data);
	                }
	            });

	        }
	        
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
					alert("Chọn một đại lý");
					return;
				}
				$("#ent_part_idx_parent").val(parentId.text);
				$("#agencyStructureId").val(parentId.id);
				$('#modal-view-tree').modal('hide');
			}
			
			function resetParent() {
				$('#ent_part_idx_parent').val('');
				$('#agencyStructureId').val('');
			}
			function instanceSuccess(data, xhr){
				alert(thuc_hien_thanh_cong, function () {    
                    if(closeOnSaveSuccess){
                        $('#divGrid').css('display', 'inline');
                        $('#divDetail').css('display', 'none');
                    }else{
                    	binding(data);
                        findData();
                    }
                    $('#ent_part_idx_parent').val($('#agencyStructureId option:selected').text());	
                });
				
			}
	    </script>
    </tiles:putAttribute>
    
	
</tiles:insertDefinition>