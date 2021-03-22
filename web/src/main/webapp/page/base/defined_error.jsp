<%@page language="java" pageEncoding="UTF-8"%>

<title>Lỗi</title>
<jsp:include page="/page/base/commontop.jsp"/> 
  
<script language="javascript" type="text/javascript">   
	$(document).ready(function(){
    	alert('<s:property value="exception.message" />', function(){
        if(history.length ==1) window.close(); 
        else history.back();
    	})
  	})
</script>