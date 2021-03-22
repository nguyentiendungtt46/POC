<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<head>
	
    
    
    <!-- jquery-datatables -->
    <link href="<spring:url value="/plugin/Bootstrap4_jQuery3_DataTables/datatables.min.css"/>" rel="stylesheet" />
    <script src="<spring:url value="/plugin/Bootstrap4_jQuery3_DataTables/datatables.min.js" />"></script>
    
   <!-- <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/v/bs4/dt-1.10.20/datatables.min.css"/>
 
	<script type="text/javascript" src="https://cdn.datatables.net/v/bs4/dt-1.10.20/datatables.min.js"></script> --> 
    
    
    
    <!-- bootstrap-datetimepicker -->    
    <!-- <link href="<spring:url value="/plugin/bootstrap-datetimepicker4.17.47/bootstrap-datetimepicker.min.css"/>" rel="stylesheet" />
    <script src="<spring:url value="/plugin/bootstrap-datetimepicker4.17.47/moment.js" />"></script>
    <script src="<spring:url value="/plugin/bootstrap-datetimepicker4.17.47/bootstrap-datetimepicker.min.js" />"></script> -->
    
    
    
    <!--<script src="<spring:url value="/js/bootstrap-material-datetimepicker.js" />"></script>
    <link href="<spring:url value="/css/bootstrap-material-datetimepicker.css"/>" rel="stylesheet" />  -->
    
   
    
    
     <link href="<spring:url value="/plugin/jquery-ui-1.12.1.custom/jquery-ui.css"/>" rel="stylesheet" />
     <script src="<spring:url value="/plugin/jquery-ui-1.12.1.custom/jquery-ui.js" />"></script>
     
    
    
    <!-- jQuery_alert -->
    <link href="<spring:url value="/plugin/jQuery_alert/jquery.alerts.css"/>" rel="stylesheet" />
    <script src="<spring:url value="/plugin/jQuery_alert/jquery.alerts.js" />"></script>
    
     <!--jquery.loader-->
    <script src="<spring:url value="/plugin/jquery.loader/jquery.loader.min.js" />"></script>
    
    <!-- select2 -->
    <link href="<spring:url value="/plugin/select2/select2.min.css"/>" rel="stylesheet" />
    <script src="<spring:url value="/plugin/select2/select2.min.js" />"></script>
    
    
    <!-- framework -->
    <link href="<spring:url value="/css/styles1.css"/>" rel="stylesheet" />
    <link href="<spring:url value="/css/font-awesome.min.css"/>" rel="stylesheet" />
    <script src="<spring:url value="/js/main.js" />"></script>
    <script src="<spring:url value="/js/catalog_v2.js" />"></script>
	
     <style type="text/css">
        container {
            
            margin-right: 1%;
            margin-left: 1%;
        }
     </style>   
   </head>
   <body>
   	<input type="text" id="testdt"/><input type="text" id="testdt1"/>
   </body>
   <script type="text/javascript">
   	$(document).ready(function(){
   		
   	            
         // Select 2 and validate
         $( "#testdt1" ).datepicker({ 
             dateFormat: 'dd/mm/yy',
             showButtonPanel: true,
             changeMonth: true,
             changeYear: true,
             yearRange: "-50:+0",
             maxDate:0                
         });    
   	});
   </script>