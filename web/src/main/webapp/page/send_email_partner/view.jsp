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
	<tiles:putAttribute name="title" value="Gửi email cho TCTD" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/sendEmailPartner" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm"
		enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			<div id="divGrid" align="left">
				<div class="row search-style">
					<div class="Table" id = "divSearchInf">
						<div class="row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Gửi email cho TCTD</h1>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tổ chức tín dụng</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<select id="parentId" onchange="getEmail(this);">
								<option value=""> -Chọn-</option>
								<c:forEach items="#{listParent}" var="item">
									<option value="${item.id}">
										<c:out value="${item.code}" /> - <c:out value="${item.name}" />
									</option>
								</c:forEach>
								</select>
							</div>
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Email</div>
							<div class="col-md-3 col-lg-3 col-sm-12 col-xs-12">
								<input type="text" id="email" class="email" maxlength="1000">
							</div>
						</div>
						
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Tiêu đề</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<input type="text" id="subject"  maxlength="500">
							</div>
						</div>
						
						<div class="row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Nội dung</div>
							<div class="col-md-8 col-lg-8 col-sm-12 col-xs-12">
								<textarea id="contentEmail" ></textarea>
							</div>
						</div>
						
						<div class="divaction" align="center">
							<input class="btn blue" type="button" onclick="sendMail();"
								value="Gửi Email" />
						</div>
					</div>
				</div>
			</div>
		</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
			$(document).ready(function() {
				CKEDITOR.replace('contentEmail', {} );
			});
			
			function sendMail(){
				var parentId = $("#parentId").val();
				var email = $("#email").val();
				var subject = $("#subject").val();
				var contentEmail = CKEDITOR.instances['contentEmail'].getData();
				
				if(email.length == 0){
					alert("Chưa nhập email!");
				}else if(subject.length == 0){
					alert("Chưa nhập tiêu đề!");
				}else if(contentEmail.length == 0){
					alert("Chưa nhập nội dung!");
				}else{
					$.ajax({
		                url:$('#theForm').attr('action') + '?method=sendMail&parentId=' + parentId + '&email=' + encodeURIComponent(email) +'&subject='+ encodeURIComponent(subject) +'&contentEmail='+encodeURIComponent(contentEmail),
		                method: 'GET',
		                success: function(data){
		                	if (data.trim() != '') {
				            	alert(data);
			                } else {
			                	alert('Thực hiện thành công', function(){window.location='h2hEmail'});
			                }
		                }
		            });
				}
			}
			
			function getEmail(s){
			 	/* console.log(s[s.selectedIndex].value);
			 	console.log(s[s.selectedIndex].id); */
			 	
			 	var id = s[s.selectedIndex].value;
				$.ajax({
	                url:$('#theForm').attr('action') + '?method=getEmail&id=' + id,
	                method: 'GET',
	                success: function(data){
	                	$("#email").val(data);
	                }
	            });
			}
			
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>