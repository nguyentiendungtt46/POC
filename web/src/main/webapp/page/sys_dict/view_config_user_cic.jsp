<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Cấu hình tài khoản CIC" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/configUserCic" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm"
		enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id="divSearchInf">
						<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Cấu hình tài khoản CIC</h1>
							</div>
							<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12">
								<div class="Row">
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tài
										khoản</div>
									<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
										<input class="form-control" name="username" id="username" />
									</div>
								</div>
								<div class="Row">
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mật
										khẩu</div>
									<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
										<input class="form-control" name="passwordOld" 
											id="passwordOld" />
									</div>
								</div>
								<div class="Row" id="divPasswordNew">
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Mật
										khẩu mới</div>
									<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
										<input class="form-control" name="passwordNew" 
											id="passwordNew" />
									</div>
								</div>
								<div class="Row" style="padding: 0;">
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12" style="padding: 0;"></div>
									<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12" style="padding: 0;padding-left: 15px;">
										<input type="checkbox" 	id="checkNewConfig" name="checkNewConfig" />
										<label for="checkNewConfig" style="margin: 6px 0 0 5px; position: absolute; font-size: 12px !important; font-weight: 500 !important;">Cấu hình mới</label>
									</div>
								</div>
								<div class="Row">
									<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12"></div>
									<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
										<input class="btn blue" type="button" onclick="callAuthen();" id="actionNewConfig"
											value="Cấu hình" />
										<input class="btn blue" type="button" onclick="callCicUserInfo();" id="actionChangeConfig"
											value="Đổi mật khẩu" />
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<div class="box-custom"></div>
		</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
		function initParam(tblCfg) {
		}
			$(document).ready(function() {
				$("#actionNewConfig").hide();
			});
			function callCicUserInfo(){
				var username = $("#username").val();
				var passwordOld = $("#passwordOld").val();
				var passwordNew = $("#passwordNew").val();
				$.ajax({
					url : $('#theForm').attr('action') + '?method=callCicUserInfo',
					data : {
						username : username,
						passwordOld : passwordOld,
						passwordNew : passwordNew
					},
					method : 'GET',
					success : function(_result) {
						alert(_result);
					}
				});
			}
			
			function callAuthen(){
				var username = $("#username").val();
				var password = $("#passwordOld").val();
				$.ajax({
					url : $('#theForm').attr('action') + '?method=callAuthenCic',
					data : {
						username : username,
						password : password
					},
					method : 'GET',
					success : function(_result) {
						if(_result == null || _result == '')
							alert("Th\u1EF1c hi\u1EC7n th\u00E0nh c\u00F4ng");
						else
							alert(_result);
					}
				});
			}
			
			$("#checkNewConfig").change(function() {
			    if(this.checked) {
			    	$("#divPasswordNew").css("display", "none");
			    	$("#passwordNew").val("");
			    	$("#actionNewConfig").show();
			    	$("#actionChangeConfig").hide();
			    }else{
			    	$("#divPasswordNew").css("display", "inline-flex");
			    	$("#actionNewConfig").hide();
			    	$("#actionChangeConfig").show();
			    }
			});
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>