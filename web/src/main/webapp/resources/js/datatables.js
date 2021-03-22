function checkboxtable(selector1, selector2) {
	$(selector1).click(function() {
		if ($(selector1).prop('checked')) {
			$(selector2).prop('checked', true);
		} else {
			$(selector2).prop('checked', false);
		}
	});
}

function buildDataTable(selector, url, tableMapping, orderCol, serialCol,
		paramData, defaultPageLength, sortType) {
	if ($.fn.dataTable.isDataTable($(selector))) {
		$(selector).DataTable().clear().destroy();
	}
	sortType = (sortType == null || sortType == undefined) ? "desc" : sortType + "";
	var lengthChange = false;
	if (defaultPageLength == null || defaultPageLength == undefined) {
		defaultPageLength = 20;
		lengthChange = true;
	}
	var ajax;
	if (paramData == null || paramData == undefined) {
		ajax = {
			"method" : 'get',
			"url" : url,
			contentType : "application/json",
			"data" : function(data) {
			},
			error : function(data) {
				if (data.status == 401) {
					window.location.href = "logout";
				}
			},
			dataSrc : function(json) {
				var dataStr = $("<div/>").text(JSON.stringify(json.data))
						.html();
				json.data = JSON.parse(dataStr);
				return json.data;
			}
		}
	} else {
		ajax = {
			"method" : 'post',
			"url" : url,
			contentType : "application/json",
			"data" : function(data) {
				var requestObject = {
					dataTableInRQ : data,
					paramData : paramData
				}
				var ajaxObj = {
					data : JSON.stringify(requestObject),
					url : url
				}
				return JSON.stringify(ajaxObj);
			},
			error : function(data) {
				if (data.status == 401) {
					window.location.href = "logout";
				}
			},
			dataSrc : function(json) {
				//				console.log(json);
				var dataStr = $("<div/>").text(JSON.stringify(json.data))
						.html();
				json.data = JSON.parse(dataStr);
				return json.data;
			}
		}
	}
	
	var dataTable = $(selector).DataTable ({
    	"processing" : true,
		"serverSide" : true,
		"pageLength" : defaultPageLength,
		'searching' : false,
		"order" : [ [ orderCol - 1, sortType ] ],
		"lengthMenu" : [ 20, 30, 50 ],
		"ajax" : ajax,
        "columns" : tableMapping,
        drawCallback : function(settings) {
			var pagination = $(this).closest(
					'.dataTables_wrapper').find(
					'.dataTables_paginate');
			pagination.toggle(this.api().page.info().pages > 1);
			checkboxtable(selector + ' .checkbox1', selector + ' .checkbox2');
			$(".checkbox2").parents("td").addClass("text-center");
		}
    });
	
	/*var dataTable = $(selector).DataTable({
		"processing" : true,
		"serverSide" : true,
		"pageLength" : defaultPageLength,
		"autoWidth" : false,
		"dom" : "<'row'<'col-sm-12 col-md-6'l><'col-sm-12 col-md-6'f>><'row'<'col-sm-12'tr>><'row'<'col-sm-12 col-md-5'i><'col-sm-12 col-md-7'p>>",
		'searching' : false,
		'responsive' : true,
		"ajax" : ajax,
		// "bSort": false,
		"order" : [ [ orderCol - 1, sortType ] ],
		"columns" : tableMapping,
		"orderCellsTop" : true,
		"lengthMenu" : [ 20, 30, 50 ],
		"lengthChange" : lengthChange,
		"scrollX": true,
		drawCallback : function(settings) {
			var pagination = $(this).closest(
					'.dataTables_wrapper').find(
					'.dataTables_paginate');
			pagination.toggle(this.api().page.info().pages > 1);
			checkboxtable(selector + ' .checkbox1', selector + ' .checkbox2');
			$(".checkbox2").parents("td").addClass("text-center");
		}
	});*/
	dataTable.on(
			'error.dt',
			function(e, settings, techNote, message) {
				console.log('An error has been reported by DataTables: ',
						message);
			});
	dataTable.on('draw.dt', function() {
		var pageInfo = dataTable.page.info()
		if (serialCol != null && serialCol != undefined && serialCol > 0) {
			dataTable.column(serialCol - 1, {
				search : 'applied',
				order : 'applied'
			}).nodes().each(function(cell, i) {
				cell.innerHTML = pageInfo.start + i + 1;
				cell.classList.add("text-center");
			});
		}
	});
}