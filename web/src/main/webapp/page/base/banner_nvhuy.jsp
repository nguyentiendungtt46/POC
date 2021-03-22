<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="frwk.utils.ApplicationContext" %>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@ page import="entity.frwk.SysUsers"%>
<%
	String url = request.getAttribute("javax.servlet.forward.request_uri").toString();
	pageContext.setAttribute("actionURL", url);
	ApplicationContext appContext = (ApplicationContext) request.getSession()
			.getAttribute(ApplicationContext.APPLICATIONCONTEXT);
	SysUsers user = null;
	if (appContext != null)
		user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
%>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
  <a href="<spring:url value='/index'/>" style="padding: 5px 20px;"> <img alt="logo"
				src="<spring:url value="/images/mbal_logo.png"/>"
				style="width: 135px; padding-bottom: 5px;" />
			</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" sty
  	data-target="#navbarTogglerDemo02" aria-controls="navbarTogglerDemo02" 
  	aria-expanded="false" aria-label="Toggle navigation">
    <img alt="logo"
				src="<spring:url value="/images/noun_menu.svg"/>" />
  </button>

  <div class="collapse navbar-collapse" id="navbarTogglerDemo02">
    <ul class="navbar-nav mr-auto mt-2 mt-lg-0 " id="navbar-nav">
      <li class='nav-item <c:if test="${fn:contains(actionURL, '/index')}">active</c:if>'>
        <a class="nav-link" href="<spring:url value='/index'/>">Trang chủ</a>
      </li>
      <% if(RightUtils.haveRight("ERQMNG", appContext) || RightUtils.haveRight("ERQMNG_VIEW", appContext)
      			  || RightUtils.haveRight("ERQMNG_EDIT", appContext) || RightUtils.haveRight("ERQMNG_ADD", appContext)){ %>
  			  	<li class='nav-item <c:if test="${fn:contains(actionURL, '/erqMng')}">active</c:if>'>
		        	<a class="nav-link" href="<spring:url value='/erqMng'/>">Quản lý ứng viên</a> 
		      </li>
          	
      	<% } %>
       
    
      <li class='nav-item dropdown <c:if test="${fn:contains(actionURL, '/appflow') ||fn:contains(actionURL, '/rpType') || fn:contains(actionURL, '/catService')|| fn:contains(actionURL, '/partner')|| fn:contains(actionURL, '/sysType')|| fn:contains(actionURL, '/sysParam')|| fn:contains(actionURL, '/entParIdx')|| fn:contains(actionURL, '/catProduct')|| fn:contains(actionURL, '/rpM1Filter') || fn:contains(actionURL, '/endpoint') || fn:contains(actionURL, '/partnerBranch') || fn:contains(actionURL, '/catError') || fn:contains(actionURL, '/qnaFilePro') || fn:contains(actionURL, '/infoSearchCic') || fn:contains(actionURL, '/catCustomer') }">active</c:if>'>
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Quản trị nghiệp vụ
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown" style="border: 1px solid;">
        	<a class='dropdown-item <c:if test="${fn:contains(actionURL, '/managerTemplate')}">active</c:if>' href="<spring:url value='managerTemplate'/>">Quản lý biểu mẫu</a> 
        	<a class='dropdown-item <c:if test="${fn:contains(actionURL, '/dictMeasure')}">active</c:if>' href="<spring:url value='dictMeasure'/>">Quản lý chỉ tiêu</a> 
        <% if(RightUtils.haveRight("APPFLOW", appContext) || RightUtils.haveRight("APPFLOW_VIEW", appContext)
      			  || RightUtils.haveRight("APPFLOW_EDIT", appContext) || RightUtils.haveRight("APPFLOW_ADD", appContext)){ %>
          	<a class='dropdown-item <c:if test="${fn:contains(actionURL, '/wrkFlwMng')}">active</c:if>' href="<spring:url value='wrkFlwMng'/>">Quản lý quy trình</a> 
      	  	<% } %>
      	   <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/catAgencyStructure')}">active</c:if>' href="<spring:url value='/catAgencyStructure'/>">Cấu trúc đại lý</a>
      	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/sysType')}">active</c:if>' href="<spring:url value='/sysType'/>">Loại từ điển</a>
      	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/sysParam')}">active</c:if>' href="<spring:url value='/sysParam'/>">Từ điển hệ thống</a>
        </div>
      </li>
      
      <li class='nav-item dropdown <c:if test="${fn:contains(actionURL, '/manageUser') || fn:contains(actionURL, '/role')  || fn:contains(actionURL, '/configUserCic')  ||
      fn:contains(actionURL, '/right')|| fn:contains(actionURL, '/param?type=1')|| fn:contains(actionURL, '/param?type=2')|| fn:contains(actionURL, '/audit') || fn:contains(actionURL, '/security') || fn:contains(actionURL, '/logCoreServices') || fn:contains(actionURL, '/jobConfig') || fn:contains(actionURL, '/logCheckConnect') || fn:contains(actionURL, '/vOauthSession') || fn:contains(actionURL, '/logServices')|| fn:contains(actionURL, '/h2hEmail')}">active</c:if>'>
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Quản trị hệ thống
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown" style="border: 1px solid;">
          <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/manageUser')}">active</c:if>' href="<spring:url value='/manageUser'/>">Danh mục người dùng</a>
          <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/right')}">active</c:if>' href="<spring:url value='/right'/>">Danh mục quyền</a>
          <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/role')}">active</c:if>' href="<spring:url value='/role'/>">Danh mục nhóm quyền</a>
          <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/param?type=1')}">active</c:if>' href="<spring:url value='/param?type=1'/>">Tham số hệ thống</a>
          <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/param?type=2')}">active</c:if>' href="<spring:url value='/param?type=2'/>">Tham số nghiệp vụ</a>
           <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/audit')}">active</c:if>' href="<spring:url value='/audit'/>">Audit hệ thống</a>
        </div>
      </li>
    </ul>
    <form class="form-inline my-2 my-lg-0">
      <a data-toggle="modal" data-target="#modalChangePass" 
						href='#' style="font-size: 13px; font-weight: 600;"><%=user.getName()%>
					</a> &nbsp;
	<a class="no-caret" href="<spring:url value='/logout'/>" style="font-size: 13px; font-weight: 600;">&nbsp<i class="fa fa-sign-out"></i>&nbsp</a>
    </form>
  </div>
</nav>

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

<script lang="javascript" type="text/javascript">
    $(document).ready(function(){
        function dequydaucadaumenungang(control){
                control.children('li').each(function(){
                    var aChildren = $(this).children('a');
                    if(aChildren.length > 0 && aChildren.attr('href') == '#'){	
                        if($(this).children('div').length > 0){
                            var ulChildren = $(this).children('div');
                            if(ulChildren.children('a').length == 0){
                                $(this).remove();
                                dequydaucadaumenungang(control.parent().parent());
                            }
                            else{
                                dequydaucadaumenungang(ulChildren);
                            }
                        }
                        else{
                            $(this).remove();
                        }
                    }
                });
            }            
            dequydaucadaumenungang($('#navbar-nav'));
            $('#navbar-nav').css('display', '');
            
            $('#navbar-nav > li > div').each(function(){
                var parentWidth = $(this).parent('a').width();
                var minWidth = $(this).width();
                parentWidth = parentWidth < minWidth ? minWidth : parentWidth;
                $(this).css('width', parentWidth);
                $(this).css('min-width', parentWidth);
            });
    });
    </script>