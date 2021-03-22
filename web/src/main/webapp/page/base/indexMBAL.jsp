<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<tiles:insertDefinition name="catalog">
	<tiles:putAttribute name="title" value="Bảo Hiểm Nhân Thọ MB Ageas Life" />
	<tiles:putAttribute name="formInf">
		<spring:url value="/index" var="formAction" />
		<c:set var="commandName" scope="request" value="formDataModelAttr" />
	</tiles:putAttribute>
	<form:form cssClass="form-horizontal" id="theForm"
		enctype="multipart/form-data" modelAttribute="${commandName}"
		method="post" action='${formAction}'>
		<tiles:putAttribute name="catGrid">
			
			<!-- <button onclick="myFunction()">Try it</button> -->
			
			
			
			<div id="divGrid" align="left">
			
			<div class="row search-style">
					<div class="Table" id = "divSearchInf">
					<div class="Row">
							<div class="row title-page" style="adding-bottom: 20px;">
								<h1>Cơ cấu tổ chức đại lý</h1>
							</div>
						</div>
<ul id="_tree_entParIdx" class="easyui-tree"
						data-options="animate:true,cascadeCheck:true,lines:true"></ul>
						<ul id="danhSachQuyen" class="easyui-tree" data-options="animate:true,checkbox:false,cascadeCheck:true,lines:true"></ul>
					</div>
				</div>
			</div>
			<input type="hidden" id="expriredDay" name="expriredDay" value="${expriredDay}"/>
		</tiles:putAttribute>

		<tiles:putAttribute name="catDetail" cascade="true">
			<div class="box-custom"></div>
		</tiles:putAttribute>
	</form:form>
	<tiles:putAttribute name="extra-scripts">
		<script type="text/javascript">
		function initParam(tblCfg) {
			//tblCfg.bFilter = false;
		}
		$(document).ready(function() {
		     var expriredDay = $('#expriredDay').val();
		     if (expriredDay != "") {
		    	 alert("Thời gian hiệu lực còn lại của mật khẩu là " + expriredDay + " ngày!");
		     }
		     $(".input_date").datepicker({
					dateFormat : 'dd/mm/yy',
					changeMonth : false,
					changeYear : false,
					showButtonPanel : false,
					onSelect: function(selected,evnt) {
				         findByDate(selected);
				    }
				});
		     $("select").select2();
		     //initTree();
		     initRightTree();
		});
		function initTree(){
			$('#_tree_entParIdx').tree('reload');
			$.ajax({
				url : $('#theForm').attr('action')
						+ '?method=getTree',
				data : {
					parentId : ""
				},
				method : 'GET',
				success : function(_result) {
					if (_result != null) {
						myJSON = JSON.parse(_result);
						console.log(myJSON);
	                    $('#_tree_entParIdx').tree({
	                        data : myJSON
	                    });
						$('#modal-view-tree').modal('show');
					}
				}
			});
		}
		
		$('#_tree_entParIdx').tree({
			onBeforeExpand: function(node){
				$.ajax({
					url : $('#theForm').attr('action')
							+ '?method=getTree',
					async: false,
					data : {
						parentId : node.id
					},
					method : 'GET',
					success : function(_result) {
						if (_result != null) {
							myJSON = JSON.parse(_result);
							if(node.children != null){
								if(node.children.length > 0) return;
							}
		                    $('#_tree_entParIdx').tree('append', {
		                    	parent : node.target,
		                        data : myJSON
		                    });
		                    $('#_tree_entParIdx').tree('expandTo', node.target);
						}
					}
				});
			  }
		});
		
		function initRightTree(){
            $('#danhSachQuyen').tree('reload');
            $.ajax( {
                async : false, type : "GET"
                , url : $('#theForm').attr('action') + '?method=treeRight'
                ,data:{
                    tokenId:$('#tokenId').val(),
                    tokenIdKey:$('#tokenIdKey').val()
                }                
                ,success : function (data1) {
                    myJSON = JSON.parse(data1);
                    treeDanhSachQuyen = $('#danhSachQuyen').tree({
                        data : myJSON
                    });
                },
                error : function (data1) {
                    
                }
            });
        }
		
		
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>