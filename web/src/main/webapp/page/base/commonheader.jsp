<%@page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ page import="frwk.utils.ApplicationContext" %>
 
 <%@ page import="entity.frwk.SysUsers" %>
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<%@page import="constants.RightConstants"%>

   <title><tiles:getAsString name="title" /></title>
   <jsp:include page="/page/base/commontop.jsp"/> 
   <jsp:include page="/page/base/banner_nvhuy.jsp"/>
         <%-- <div style="clear:both;"></div>
         <jsp:include page="/page/base/commonnavbar.jsp"/> --%>
         
         
         
   