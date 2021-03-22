<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<body class="login-page">
<jsp:include page="/page/base/commontop.jsp" />
<spring:url value="/login" var="formAction">
	<spring:param name="method" value="loginProcess"></spring:param>
</spring:url>
<c:set var="commandName" scope="request" value="formDataModelAttr" />
<div id="main" class="container clearfix" style="min-height: 347px;">

	<div class="col-md-4 form-login">
		<p class="login-title">
			<img alt="logo" src="<spring:url value="/images/mbal_logo.png"/>" style="    width: 180px;"/>
			
		</p>
		<div class="panel-body">
			<form:form cssClass="form-horizontal login" id="frmLogin"
				action='${formAction}' method="post" modelAttribute="${commandName}">
				<div id="dangnhap">
				<div class="form-body" style="color: #0678B3; font-weight: bold;">
					<div class="form-group">
						<label for="exampleInputEmail1">Tên đăng nhập</label>	
						<div class="input-group-icon">
						<i class="fa fa-user" style="color: #141ed2;"></i>
						<form:input path="user" id="username" cssClass="required form-control" placeholder="Nhập tên tài khoản" />
						</div>
						
					</div>
					<div class="form-group">
						<label>Mật khẩu</label>
						<div class="input-icon right input-group-icon">
							<i class="fa fa-lock" style="color: #141ed2;"></i>
							<form:input path="pass" id="password" type="password" cssClass="required form-control" placeholder="Nhập mật khẩu" />
						</div>
					</div>
				</div>
				<div class="form-actions">
					<%-- <a type="button" style="display: flow-root;" class="btn blue" href="<spring:url value='/login?method=loginProcess'/>">
                                                   <i class="fa fa-edit"></i> Đăng nhập
                        </a> --%>
					<button type="button" id="btnLogin" onclick="login();" class="btn-login">Đăng nhập</button>
				</div>
				</div>
				<div id="dangki">
				<div class="form-body" style="color: #0678B3; font-weight: bold;">
					<div class="form-group">
						<label for="exampleInputEmail1">Mật khẩu cũ</label>	
						<div class="input-group-icon">
						<i class="fa fa-user" style="color: #141ed2;"></i>
						<input id="olsPW"  type="password"  Class="required form-control" placeholder="Mật khẩu cũ" />
						</div>
						
					</div>
					<div class="form-group">
						<label>Mật khẩu mới</label>
						<div class="input-icon right input-group-icon">
							<i class="fa fa-lock" style="color: #141ed2;"></i>
							<input id="newPW" type="password" data-toggle="tooltip" title="Mật khẩu tối thiểu 8 ký tự. Bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt" Class="required form-control" placeholder="Nhập mật khẩu mới" />
						</div>
					</div>
					<div class="form-group">
						<label>Xác nhận</label>
						<div class="input-icon right input-group-icon">
							<i class="fa fa-lock" style="color: #141ed2;"></i>
							<input id="confirmNewPW" type="password" data-toggle="tooltip" title="Mật khẩu tối thiểu 8 ký tự. Bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt" Class="required form-control" placeholder="Xác nhận mật khẩu mới" />
						</div>
					</div>
				</div>
				<div class="form-actions">
					<button type="button" onclick="changePassword();" class="btn-login">Submit</button>
					<button type="button" onclick="backLogin();" class="btn-login">Back</button>
				</div>
				</div>
				<input type="hidden" id="status" name="status" value="${status}"/>
			</form:form>
		</div>
	</div>
</div>
<script type="text/javascript">
	/* function doLogin() {
		$.ajax({
			url : $('#theForm').attr('action') + '?method=login',
			success : function(severtrave) {
				alert('Login ok');
			},
			error : function(severtrave) {
				alert(severtrave);
			},
			data : $('#theForm').serialize()

		});

	} */
	 $(document).ready(function(){
		 var status = $('#status').val();
		$('#dangki').css('display','none');
		 $('#dangnhap').css('display','block');
		 console.log(status);
		 if(status=="false"){
			 //jAlert('<s:text name ="Đăng nhập không thành công"/>','<s:text name ="ok"/>');
			 alert("Thông tin đăng nhập không đúng");
		 } else if (status =="exprired") {
			 alert("Tài khoản hết hạn sử dụng, liên hệ admin để kích hoạt lại");
		 } else if (status == "firstLogin") {
			 $('#dangki').css('display','block');
			 $('#dangnhap').css('display','none');
		 }
		 
		 
		// tokenId           
         if($('#tokenId').length <=0){
             var tokenIDValue = '${tokenId}';
             $('<input>').attr({
                 type: 'hidden',
                 id: 'tokenId',
                 name: 'tokenId',
                 value: tokenIDValue
             }).appendTo($('#frmLogin'));
         }
         
         // tokenIdKey    
         if($('#tokenIdKey').length <=0){
             $('<input>').attr({
                 type: 'hidden',
                 id: 'tokenIdKey',
                 name: 'tokenIdKey',
                 value:'${tokenIdKey}'
             }).appendTo($('#frmLogin'));
         }   
         
	 });
	
	 $(document).keypress(function(e) {
	        if(e.which == 13) {
	           login();
	        }
	    });
	
	function backLogin() {
		$('#dangki').css('display','none');
		$('#dangnhap').css('display','block');
	}
	
	function changePassword(){
		if ($('#newPW').val() == null || $('#confirmNewPW').val() == null) {
			alert("Mật khẩu không được null");
			return;
		}
		if ($('#newPW').val() != $('#confirmNewPW').val()) {
			alert("Mật khẩu không giống nhau");
			return;
		}
		var uri = $('#frmLogin').attr('action');
		var path = uri.split('?');
		var newPass = $('#newPW').val();
		var url = path[0]
		+ '?method=changepassword';
		url = encodeURI(url);
		$.ajax({
			url : url,
			data : {"oldPassWord" :$('#olsPW').val(),
				"newPassWord": newPass,
				"firstLogin" : "firstLogin"},
			method : 'POST',
			success : function(_result, status, xhr) {
				if (_result.length > 0) {
					if ("success" === _result) {
						$('#dangki').css('display','none');
						$('#dangnhap').css('display','block');
						alert("Thay đổi mật khẩu thành công");
					} else {
						alert(_result);
					}
				}
			}
		});
	}
	
	
	
	function login(){
		if ($('#username').val() == null || $('#password').val() == null) {
			alert("Tên đăng nhập hoặc mật khẩu không đc để trống");
		}
		var uri = $('#frmLogin').attr('action');
		var path = uri.split('?');
		var url = path[0]
		$.ajax({
			url : uri,
			data : {"username" :$('#username').val(),
				"password": $('#password').val()},
			method : 'POST',
			success : function(_result, status, xhr) {
				if (_result.length > 0) {
					if ("success" === _result) {
						$('#dangki').css('display','none');
						$('#dangnhap').css('display','block');
						alert("Thay đổi mật khẩu thành công");
						return;
					}  
					if (_result ==="exprired") {
						 alert("Tài khoản hết hạn sử dụng, liên hệ admin để kích hoạt lại");
						 return;
					 }  
					if (_result === "firstLogin") {
						 $('#dangki').css('display','block');
						 $('#dangnhap').css('display','none');
						 return;
					}  
					if (_result === "fail") {
						 alert("Thông tin đăng nhập không đúng");
						 return;
					}
					if (_result === "INACTIVE") {
						 alert("Người dùng bị khóa, liên hệ với quản trị hệ thống để được hỗ trợ");
						 return;
					}
					if (_result === "login_exprired") {
						 alert("Bạn đăng nhập sai thông tin nhiều lần, hãy đợi ít phút rồi thử lại");
						 return;
					}
					if (_result === "LOGIN_M6") {
						 alert("Có lỗi xảy ra khi đăng nhập M6");
						 return;
					}
					if (_result === "LOGIN_M6_1") {
						 alert("Đăng nhập M6: trống tên đăng nhập hoặc mật khẩu");
						 return;
					}
					if (_result === "LOGIN_M6_2") {
						 alert("Đăng nhập M6: sai thông tin tên đăng nhập hoặc mật khẩu");
						 return;
					}
					
				}
				else window.location.href = "erqMng";
			}
		});
	}
</script>

</body>
