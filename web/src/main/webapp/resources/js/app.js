function toStringDDMMYYYYHHmmss(data, type, row) {
	if (data == null || data == undefined) {
		return "";
	}
	var date = new Date(data)
	return moment(date).format('DD/MM/YYYY HH:mm:ss');
};

function renderLogServiceStatus(data,type,row)
{
	if(data == 1){
		return '<span class="badge badge-success">Success</span>';
	}else if(data == 0){
		return '<span class="badge badge-warning">Block</span>';
	}else{
		return '<span class="badge badge-danger">Error</span>';
	}
}

