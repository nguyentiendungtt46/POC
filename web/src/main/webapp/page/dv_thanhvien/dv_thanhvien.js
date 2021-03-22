function initTableIP(){
	$('#table-partner').dataTable({
		"bJQueryUI" : true,
		"iDisplayLength" : 200,
		//"data":cacheJson
		"bProcessing" : true,
        "bFilter":true,
		"oLanguage" : {
			"sLengthMenu" : "Hiển thị" + "_MENU_" + "bản ghi",
			"sZeroRecords" : " ",
			"sInfo" : "Hiển thị" + " _START_ " + "đến" + " _END_ " + "của" + " _TOTAL_ " + "bản ghi",
			"sInfoEmpty" : "Hiển thị" + " " + "từ" + " 0 " + "tới" + " 0 " + "trên tổng số" + " 0 " + "bản ghi",
			"sInfoFiltered" : "( " + "Đã lọc từ" + " _MAX_ " + "tổng số bản ghi" + " )",
			"oPaginate" : {
				"sFirst" :"<i class='fa fa-fast-backward'></i>",
				"sLast" :"<i class='fa fa-fast-forward'></i>",
				"sPrevious":"<i class='fa fa-backward'></i>",
				"sNext" : "<i class='fa fa-forward'></i>"
			},
			"sSearch":"Từ khóa"
		},
		"bDestroy" : true
	});
}