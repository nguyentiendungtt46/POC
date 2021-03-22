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
	ApplicationContext appContext = (ApplicationContext)request.getSession().getAttribute(ApplicationContext.APPLICATIONCONTEXT);
	SysUsers user = null;
	if (appContext != null)
	user = (SysUsers) appContext.getAttribute(ApplicationContext.USER);
%>

<nav class="navbar navbar-expand-lg navbar-light bg-light" style="width: 100%">
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <img alt="logo"
				src="<spring:url value="/images/noun_menu.svg"/>"
				 />
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto" id="navbar-nav" style="    margin: 0 auto;">
      <li class='nav-item <c:if test="${fn:contains(actionURL, '/index')}">active</c:if>'>
        <a class="nav-link" href="<spring:url value='/index'/>">Trang chủ</a>
      </li>
      
      <% if(RightUtils.haveRight("ERQMNG", appContext) || RightUtils.haveRight("ERQMNG_VIEW", appContext)
      			  || RightUtils.haveRight("ERQMNG_EDIT", appContext) || RightUtils.haveRight("ERQMNG_ADD", appContext)){ %>
  			  	<li class='nav-item <c:if test="${fn:contains(actionURL, '/erqMng')}">active</c:if>'>
		        	<a class="nav-link" href="<spring:url value='/erqMng'/>">Quản lý ứng viên</a> 
		      </li>
          	
      	<% } %>
       
    
      <li class='nav-item dropdown <c:if test="${fn:contains(actionURL, '/wrkFlwMng') ||fn:contains(actionURL, '/rpType') || fn:contains(actionURL, '/catService')|| fn:contains(actionURL, '/partner')|| fn:contains(actionURL, '/sysType')|| fn:contains(actionURL, '/sysParam')|| fn:contains(actionURL, '/entParIdx')|| fn:contains(actionURL, '/catProduct')|| fn:contains(actionURL, '/rpM1Filter') || fn:contains(actionURL, '/endpoint') || fn:contains(actionURL, '/partnerBranch') || fn:contains(actionURL, '/catError') || fn:contains(actionURL, '/qnaFilePro') || fn:contains(actionURL, '/infoSearchCic') || fn:contains(actionURL, '/catCustomer') }">active</c:if>'>
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          Quản trị nghiệp vụ
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown" style="border: 1px solid;">
        	<a class='dropdown-item <c:if test="${fn:contains(actionURL, '/managerTemplate')}">active</c:if>' href="<spring:url value='managerTemplate'/>">Quản lý biểu mẫu</a> 
        	<a class='dropdown-item <c:if test="${fn:contains(actionURL, '/dictMeasure')}">active</c:if>' href="<spring:url value='dictMeasure'/>">Quản lý chỉ tiêu</a> 
        <% if(RightUtils.haveRight("APPFLOW", appContext) || RightUtils.haveRight("APPFLOW_VIEW", appContext)
      			  || RightUtils.haveRight("APPFLOW_EDIT", appContext) || RightUtils.haveRight("APPFLOW_ADD", appContext)){ %>
          	<a class='dropdown-item <c:if test="${fn:contains(actionURL, '/wrkFlwMng')}">active</c:if>' href="<spring:url value='wrkFlwMng'/>">Quản lý quy trình xử lý</a> 
      	  	<% } %>
<%--          <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/rpType')}">active</c:if>' href="<spring:url value='/rpType'/>">Danh mục báo cáo</a>
		  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/serviceInfo')}">active</c:if>' href="<spring:url value='/serviceInfo'/>">Danh mục service</a>
		  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/catProduct')}">active</c:if>' href="<spring:url value='/catProduct'/>">Danh mục sản phẩm</a>
           
          <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/partner')}">active</c:if>' href="<spring:url value='/partner'/>">Danh mục đối tác</a>
          <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/entParIdx')}">active</c:if>' href="<spring:url value='/entParIdx'/>">Danh mục chỉ tiêu</a>--%>
      	   <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/catAgencyStructure')}">active</c:if>' href="<spring:url value='/catAgencyStructure'/>">Danh mục Cơ cấu đại lý</a>
      	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/sysType')}">active</c:if>' href="<spring:url value='/sysType'/>">Danh mục loại từ điển</a>
      	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/sysParam')}">active</c:if>' href="<spring:url value='/sysParam'/>">Từ điển hệ thống</a>
      	  <%-- <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/rpM1Filter')}">active</c:if>' href="<spring:url value='/rpM1Filter'/>">Tham số xử lý báo cáo</a>
      	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/endpoint')}">active</c:if>' href="<spring:url value='/endpoint'/>">Endpoint service vấn tin</a>
      	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/partnerBranch')}">active</c:if>' href="<spring:url value='/partnerBranch'/>">Danh mục đơn vị</a>
      	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/catError')}">active</c:if>' href="<spring:url value='/catError'/>">Danh mục lỗi</a>
       	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/qnaFilePro')}">active</c:if>' href="<spring:url value='qnaFilePro'/>">Mã sản phẩm hỏi tin theo tệp</a>
      	  
      	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/infoSearchCic')}">active</c:if>' href="<spring:url value='/infoSearchCic'/>">Tìm kiếm khách hàng trên kho CIC</a>
      	 
      	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/cicSchedule')}">active</c:if>' href="<spring:url value='/cicSchedule'/>">Quản trị job</a>
      	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/cfgClass')}">active</c:if>' href="<spring:url value='/cfgClass'/>">Danh mục lớp học</a>
      	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/cfgQuestion')}">active</c:if>' href="<spring:url value='/cfgQuestion'/>">Danh mục câu hỏi</a>--%>
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
          
         <%--  <% if(RightUtils.haveRight("LOGSERVICES", appContext)){ %>
          		<a class='dropdown-item <c:if test="${fn:contains(actionURL, '/logServices')}">active</c:if>' href="<spring:url value='logServices'/>">Giám sát service</a>
      	  	<% } %> --%>
      	  	
      	
          <%-- 
          
      	  
      	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/security')}">active</c:if>' href="<spring:url value='/security'/>">Chính sách bảo mật</a>
      	  <% if(RightUtils.haveRight("LOGCORESERVICES", appContext)){ %>
          	<a class='dropdown-item <c:if test="${fn:contains(actionURL, '/logCoreServices')}">active</c:if>' href="<spring:url value='logCoreServices'/>">Giám sát service Core</a>
      	  	<% } %>
      	  	
      	  
      	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/vOauthSession')}">active</c:if>' href="<spring:url value='/vOauthSession'/>">Danh mục user đang kết nối</a>
      	  <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/h2hEmail')}">active</c:if>' href="<spring:url value='/h2hEmail'/>">Giám sát gửi email</a>
           <a class='dropdown-item' <c:if test="${fn:contains(actionURL, '/dataStatus')}">active</c:if>'  href="<spring:url value='/dataStatus'/>">Trạng thái truyền nhận dữ liệu</a> --%>
           <a class='dropdown-item <c:if test="${fn:contains(actionURL, '/audit')}">active</c:if>' href="<spring:url value='/audit'/>">Audit hệ thống</a>
        </div>
      </li>
      <%-- <li class='nav-item dropdown <c:if test="${fn:endsWith(actionURL, '/rpSum')||fn:endsWith(actionURL, '/rp')||fn:endsWith(actionURL, '/rpFileUnstructured')}">active</c:if>'>
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          TCTD gửi báo cáo
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown" style="border: 1px solid;">        
        	<% if(RightUtils.haveRight("UPLOAD_BCTD", appContext)){ %>
          		<a class="dropdown-item" href="#" onclick="sendReport()">Gửi báo cáo</a>    
      	  	<% } %> 
          	<a class='dropdown-item <c:if test="${fn:endsWith(actionURL, '/rpSum')}">active</c:if>' href="<spring:url value='/rpSum'/>">DANH SÁCH GỬI BÁO CÁO</a> 
      	  	<a class='dropdown-item <c:if test="${fn:endsWith(actionURL, '/rp')}">active</c:if>' href="<spring:url value='/rp'/>">Kiểm tra kết quả báo cáo</a>
      	  	<a class='dropdown-item <c:if test="${fn:endsWith(actionURL, '/rpFileUnstructured')}">active</c:if>' href="<spring:url value='/rpFileUnstructured'/>">Theo dõi tệp phi cấu trúc</a> 
        </div>
      </li> --%>
     <%--  <li class='nav-item dropdown <c:if test="${fn:contains(actionURL, '/reportRule') || fn:contains(actionURL, '/configCfgMaster')  || fn:contains(actionURL, '/configCfgDetail')}">active</c:if>'>
        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
         Cấu hình báo cáo
        </a>
        <div class="dropdown-menu" aria-labelledby="navbarDropdown" style="border: 1px solid;">
        	<% if(RightUtils.haveRight("CONFIGCFGMASTER", appContext) || RightUtils.haveRight("CONFIGCFGMASTER_ADD", appContext) || RightUtils.haveRight("CONFIGCFGMASTER_DEL", appContext) || RightUtils.haveRight("CONFIGCFGMASTER_EDIT", appContext) || RightUtils.haveRight("CONFIGCFGMASTER_VIEW", appContext)){ %>
          	<a class='dropdown-item <c:if test="${fn:contains(actionURL, '/configCfgMaster')}">active</c:if>' href="<spring:url value='/configCfgMaster'/>">Cấu hình báo cáo</a>
      	  	<% } %>
      	  	<% if(RightUtils.haveRight("CONFIGCFGDETAIL", appContext) || RightUtils.haveRight("CONFIGCFGDETAIL_ADD", appContext) || RightUtils.haveRight("CONFIGCFGDETAIL_DEL", appContext) || RightUtils.haveRight("CONFIGCFGDETAIL_EDIT", appContext) || RightUtils.haveRight("CONFIGCFGDETAIL_VIEW", appContext)){ %>
      	  	<a class='dropdown-item <c:if test="${fn:contains(actionURL, '/configCfgDetail')}">active</c:if>' href="<spring:url value='/configCfgDetail'/>">Cấu hình validate báo cáo</a>
      	  	<% } %>
      	  	<% if(RightUtils.haveRight("REPORTRULE", appContext) || RightUtils.haveRight("REPORTRULE_ADD", appContext) || RightUtils.haveRight("REPORTRULE_DEL", appContext) || RightUtils.haveRight("REPORTRULE_EDIT", appContext) || RightUtils.haveRight("REPORTRULE_VIEW", appContext)){ %>
      	  	<a class='dropdown-item <c:if test="${fn:contains(actionURL, '/reportRule')}">active</c:if>' href="<spring:url value='/reportRule'/>">Danh mục loại validate</a>
      		<% } %>
        </div>
      </li> --%>
    </ul>

	<%-- <ul class="nav navbar-nav account">
		<li><a data-toggle="modal" data-target="#modalChangePass" href = '#'><i class="fa fa-expeditedssl">
			</i>Đổi mật khẩu</a>
		</li> &nbsp;|&nbsp;
		<li><a class="no-caret" href="<spring:url value='/login'/>">Thoát<i class="fa fa-sign-out"></i></a></li>
	</ul> --%>
	<%-- 
	<!-- Topbar Navbar -->
    <ul class="navbar-nav ml-auto">
      <!-- Nav Item - User Information -->
      <li class="nav-item dropdown no-arrow">
        <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
          <span class="mr-2 d-none d-lg-inline text-gray-600 small"><%= user.getUsername() %></span>
          <!-- <img class="img-profile rounded-circle" src="https://source.unsplash.com/QAB-WJcbgJk/60x60"> -->
        </a>
        <!-- Dropdown - User Information -->
        <div class="dropdown-menu dropdown-menu-right shadow animated--grow-in" aria-labelledby="userDropdown">
          <a class="dropdown-item" data-toggle="modal" data-target="#modalChangePass" href = '#'>
            <i class="fa fa-expeditedssl"></i>
            Đổi mật khẩu
          </a>
          <div class="dropdown-divider"></div>
          <a class="dropdown-item" href="<spring:url value='/login'/>">
            <i class="fa fa-sign-out"></i>
            Thoát
          </a>
        </div>
      </li>
    </ul> --%>

		<!-- <form class="form-inline my-2 my-lg-0">
      <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
      <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
    </form> -->
  </div>
</nav>
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




