<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@page import="frwk.dao.hibernate.sys.RightUtils"%>
<jsp:include page="/page/base/commonheader.jsp"/>
<script src="<spring:url value="/js/common.js" />"></script>
<script src="<spring:url value="/js/dataTables.buttons.min.js" />"></script>
<%-- <title><tiles:getAsString name="title"></tiles:getAsString></title> --%>

<div id="main" class="container" style="margin-top: 20px;margin-bottom: 10px;">
	<tiles:putAttribute name="formInf"/>
	<form:form cssClass="form-horizontal" id="theForm" enctype="multipart/form-data" modelAttribute="${commandName}" method="post" action='${formAction}'>
	
    <c:if test="${approveForm}">
   		<input type="hidden" name="formTypeApprove" value="true" id = "formTypeApproveId"> 
   	</c:if>
    <tiles:insertAttribute name="catGrid"/>    
    <div id="divDetail"  class="Table">
    	<!-- <div class="HeaderText">Thông tin chi tiết</div>   -->
    	<tiles:insertAttribute name="catDetail"/>
    	<!-- <div align="center" class="HeaderText">&#8203;</div> -->
        <div align="center" class="divaction">
        	<c:if test="${save}">
        		<input type="button" onclick="save()" value="Lưu" id="btnSave" class="btn blue"> 
        	</c:if>
        	<c:if test="${del}">
        		<input type="button" onclick="del()" value="Xóa" id="btnDel" class="btn red"> 
        	</c:if>
            <input type="button" onclick="cancel()" value="Bỏ qua" id="btlCancel" class="btn gray">				
            <input style='display: none' type="button" onclick="next()" id="btnNext" class="btn blue">			
            <input style='display: none' type="button" onclick="previous()" value="Không duyệt" id="btnPrevious" class="btn red"> 
        </div>
    </div>
    
	</form:form>
</div>
<jsp:include page="/page/base/footer.jsp"/>
<tiles:insertAttribute name="extra-scripts"/>

<script type="text/javascript">
	$(document).ready(function(){		
		if(typeof tblCfg !== 'undefined'){
			if(tblCfg.approveInMain){
				$('.divaction #btnToApprove, .divaction #btnUnToApprove, .divaction #btnApprove, .divaction #btnUnApprove, .divaction #btnCancelApprove').remove();
			}else{
				$('.mainGrid#btnToApprove, .mainGrid#btnUnToApprove, .mainGrid#btnApprove, .mainGrid#btnUnApprove, .mainGrid#btnCancelApprove').remove();
			}
		}
		
		// An hien theo trang thai
		if($('#status').length==1){
			if($('#status').val()==undefined || $('#status').val()=='' || $('#status').val()==0){
	    		$('#btnUnToApprove, #btnApprove, #btnUnApprove, #btnCancelApprove').remove();
	        }else if($('#status').val()==1){
	        	$('#btnDtAdd, #btnSave, #btnDel, #btnToApprove, #btnCancelApprove').remove();
	    	}else if($('#status').val()==2){
	    		$('#btnDtAdd, #btnSave, #btnDel, #btnToApprove,#btnUnToApprove, #btnApprove, #btnUnApprove').remove();
	    	}
		}
		
	}); 
</script>

<c:if test="${!add}">
<script type="text/javascript">
	$(':button.btnDtAdd').css('display','none');
</script>
</c:if>


<c:if test="${!toApprove}">
<script type="text/javascript">
	$(document).ready(function(){
		$('#btnToApprove,#btnUnToApprove').remove();
	}); 
</script>
</c:if>


<c:if test="${!approve}">
<script type="text/javascript">
	$(document).ready(function(){
		$('#btnApprove,#btnUnApprove, #btnCancelApprove').remove();
	}); 
</script>
</c:if>
<script src="<spring:url value="/js/catalog_v2.js" />"></script>
<script>
$(document).ready(function(){
    divDatatablezize = $('#divDatatable').width();	
    if (typeof initParam == 'function')
		  initParam(tblCfg);
	  else{
		  alert('Chưa định nghĩa hàm initParam(tblCfg)');
		  return
	  } 
    if(tblCfg.bFilter && $('#divSearchInf input[type!="button"][type!="hidden"],#divSearchInf select,#divSearchInf textarea').length <=0)
  	  $('#gridTitle').remove();
    else{
  	  if(tblCfg.gridTitle!=undefined){
      	  if(tblCfg.gridTitle.trim().length==0)
      		  $('#gridTitle').remove();
      	  else
      		  $('#gridTitle').html(tblCfg.gridTitle);
        }
    }
    
    if(tblCfg.notSearchWhenStart)
        return
    findData(); 
});
</script>
