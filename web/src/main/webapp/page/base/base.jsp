<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<jsp:include page="/page/base/commonheader.jsp"/>


<div id="main" class="container" style="background: #FFF;margin-top: 20px; margin-bottom: 10px;">
	<tiles:insertAttribute name="body"/>    
</div>
<jsp:include page="/page/base/footer.jsp"/>
<tiles:insertAttribute name="extra-scripts"/>