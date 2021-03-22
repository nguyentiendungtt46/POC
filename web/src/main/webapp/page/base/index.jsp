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
						<div class="col-md-12 col-lg-12 col-sm-12 col-xs-12">
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Đơn vị</div>
		                    <div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
								<select id="select_tctd_dashbroad" class="select_dashbroad">
									<option value="">- Chọn -</option>
									<c:forEach items="#{dsDonVi}" var="item">
											<option value="${item.id}">
												<c:out value="${item.code}" /> -
												<c:out value="${item.name}" />
											</option>
										</c:forEach>
								</select>
		                    </div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Ngày</div>
							<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12" style="    display: flex;">
								<!-- <input 
							class="form-control qnaInFile_start_date" 
							title="Ngày bắt đầu không được để trống" /> -->
							<input id="fromDate" value="${formDataModelAttr.ngaytaotu}" class="form-control input_date" style="border-bottom-right-radius: 0px;border-top-right-radius: 0px;"/>
							<span class="input-group-text" id="basic-addon3" style="padding: 2px 10px;font-size: 13px;border-radius: 1px !important;font-weight: bold;">đến</span>
							<input id="toDate" value="${formDataModelAttr.ngaytaoden}" class="form-control input_date" style="border-bottom-left-radius: 0px;border-top-left-radius: 0px;"/>
							</div>
						</div>
						<div class="Row">
							<div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Sản phẩm</div>
		                    <div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
								<select id="select_product_dashbroad" class="select_dashbroad">
									<option value="">-Chọn-</option>
									<c:forEach items="#{lstProcuct}" var="item">
										<option value="${item.code}">
											<c:out value="${item.code}" /> -
											<c:out value="${item.name}" />
										</option>
									</c:forEach>
								</select>
		                    </div>
		                    <div class="label-static col-md-2 col-lg-2 col-sm-12 col-xs-12">Trạng thái</div>
							<div class="col-md-4 col-lg-4 col-sm-12 col-xs-12">
								<select id="select_status_dashbroad" class="select_dashbroad">
									<option value="">- Chọn -</option>
									<option value="1">Chưa có kết quả</option>
									<option value="2">Đã có kết quả</option>
									<option value="3">Không thành công</option>
								</select>
		                    </div>
						</div>
				        </div>
				        <div class="col-md-12 col-lg-12 col-sm-12 col-xs-12">
				        	<figure class="highcharts-figure">
							    <div id="container"></div>
							</figure>

				        </div>		    
			        	</div>
						<input type="hidden" id="strChart" value='${jsonString}' /> 
						<input class="btn blue" style="float: right;" type="button" onclick="exportExcel();"
									value="Xuất excel" /> 
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
			tblCfg.bFilter = false;
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
		     ajaxParam($("#select_product_dashbroad").val(), $("#fromDate").val(), $("#toDate").val(),  $("#select_tctd_dashbroad").val(), $("#select_status_dashbroad").val());
			
		});
		
		$(document.body).on("change","select.select_dashbroad",function(){
			ajaxParam($("#select_product_dashbroad").val(), $("#fromDate").val(), $("#toDate").val(),  $("#select_tctd_dashbroad").val(), $("#select_status_dashbroad").val());
		});
		
		function findByDate(val){
			ajaxParam($("#select_product_dashbroad").val(), $("#fromDate").val(), $("#toDate").val(), $("#select_tctd_dashbroad").val(), $("#select_status_dashbroad").val());
		}
		
		function ajaxParam(product, fromDate, toDate, tctd, status){
			$.ajax({
				url : $('#theForm').attr('action') + '?method=loadFilter',
				data : {
					product : product,
					tctd : tctd,
					status : status,
					fromDate : fromDate,
					toDate : toDate
				},
				method : 'GET',
				success : function(_result) {
					if (_result != null && _result != "" & _result.size != 0) {
						console.log(_result);
						 var jsonChart = JSON.parse(_result);
						var dataChart = [];
						 for(var i=0;i< jsonChart.length; i++){
							 dataChart.push({y: parseInt(jsonChart[i][2]), name: jsonChart[i][0]});
						 }
						 designChart(dataChart);
						
					}
				}
			});
		}
		
		function designChart(dataChart){
			Highcharts.chart('container', {
			    chart: {
			        plotBackgroundColor: null,
			        plotBorderWidth: null,
			        plotShadow: false,
			        type: 'pie'
			    },
			    title: {
			        text: 'Q&A Dashboard'
			    },
			    tooltip: {
			        pointFormat: '{series.name}: <b>{point.y}</b>'
			    },
			    credits: {
		            enabled: false
		        },
		        exporting: {
		            enabled: false
		        },
			    legend: {
			        enabled: false
			    },
			    accessibility: {
			        point: {
			            valueSuffix: ''
			        }
			    },
			    plotOptions: {
			        pie: {
			            allowPointSelect: true,
			            cursor: 'pointer',
				        startAngle: 45,
			            dataLabels: {
			                enabled: true,
			                format: '<b>{point.name}</b>: - {y}' 
			            }
			        }
			    },
			    series: [{
			        name: 'S\u1ED1 l\u01B0\u1EE3ng',
			        colorByPoint: true,
			        turboThreshold  : 20,
			        data: dataChart
			    }]
			});
	}
		
		function exportExcel() {
			var product = $("#select_product_dashbroad").val();
			var fromDate = $("#fromDate").val(); 
			var toDate = $("#toDate").val();
			var tctd = $("#select_tctd_dashbroad").val();
			var status = $("#select_status_dashbroad").val();
			window.open($('#theForm').attr('action') + '?method=ExportFileExcel&product='+product+'&fromDate='+fromDate+'&toDate='+toDate+'&tctd='+tctd+'&status='+status);
		}
		/* function myFunction() {
			  window.open("http://localhost:8080/cic/definedError", "_blank", "top=100,left=500,width=400,height=400");
			} */
		</script>
	</tiles:putAttribute>
</tiles:insertDefinition>