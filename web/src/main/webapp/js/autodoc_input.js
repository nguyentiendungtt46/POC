function assign() {
	window.open(mnRmUrl, '', window_property);
}
function doAssign(rmcode) {
	$.ajax({
		url : $('#theForm').attr('action') + '?method=assign',
		data : {
			rmcode : rmcode,
			id : $('#id').val()
		},
		success : function(data, status, xhr) {
			alert(data.trim().length == 0 ? thuc_hien_thanh_cong : data);

		},
		error : function(data) {
			alert(thuc_hien_khong_thanh_cong);
		}
	});
}
function instanceCopyOk(data) {
	window.location.href = $('#theForm').attr('action') + '?id=' + data['id'];

}
function updateSameControl(jControl, controlTypeName) {
	if (jControl.is("select")) {
		$('select[name="' + jControl.prop('name') + '"]').val(jControl.val());
	} else if (jControl.is("input")) {
		$('input[name="' + jControl.prop('name') + '"]').val(jControl.val());
		if ($(this).is('input:checkbox'))
			$('input[name="' + jControl.prop('name') + '"]').prop('checked',
					jControl.prop('checked'));
		else if (jControl.is(':radio')) {
			// check radio o tab khac (cung id, khac ten)
			$('input[type=\'radio\'][id=\'' + jControl.attr('id') + '\']')
					.each(function() {
						if ($(this).attr('name') != jControl.attr('name'))
							$(this).prop('checked', jControl.prop('checked'))
					});
			// cap nhat value cho toan bo hidden field (theo ten)
			$('input[type=\'hidden\'][name=\'' + jControl.attr('id') + '\']')
					.val(jControl.val());

		}
		// $('#'+(jControl.attr('id'))).prop('checked',
		// jControl.prop('checked'));

	} else if (jControl.is("textarea")) {
		$('textarea[name="' + jControl.prop('name') + '"]').val(jControl.val());
		// if ( $(this).is('input:text') )
	}
}

function instanceValidate() {
	var isValid = true;
	var inputControl;
	// validate khach hang, dai dien, lien quan
	$('input[type="hidden"][name$="].customers.id"]').each(
			function() {
				if (isValid) {
					if ($(this).val().trim().length == 0) {
						isValid = false;
						inputControl = $('input[type="hidden"][name="'
								+ $(this).attr('name').replace(
										'].customers.id', '].customers.cifNo')
								+ '"]');
					}
				}
			});
	if (!isValid) {
		alert(
				'Ch\u01B0a nh\u1EADp \u0111\u1EE7 th\u00F4ng tin ch\u1EE7 th\u1EC3/ng\u01B0\u1EDDi li\u00EAn quan',
				function() {
					inputControl.focus();
				});

	}
	return isValid;
}
function toVerifyTemplateInf() {

	$('#id').prop('disabled', false);

	// Truoc khi validate
	if (typeof beforeValidate == 'function')
		beforeValidate();

	if (!$('#theForm').valid()) {
		return;
	}
	// customize validate
	if (typeof instanceValidate == 'function') {
		if (!instanceValidate())
			return;
	}

	// Before save
	if (typeof beforeSave == 'function')
		beforeSave();

	$('input[type="checkbox"]').each(function() {
		$(this).val($(this).prop('checked'));
	});
	$('input[type="radio"]').each(function() {
		try {
			if ($(this).prop('checked')) {
				var arrName = $(this).attr('name').split('.');
				var realId = arrName[arrName.length - 1];
				$(this).val($(this).attr('id').substring(realId.length));
			}
		} catch (err) {
		}

	});
	$.ajax({
		method : 'POST',
		async : false,
		type : "POST",
		url : $('#theForm').attr('action') + '?method=toVerifyTemplate',
		data : $('#theForm').serialize(),
		success : function(data, status, xhr) {
			if (data != undefined && data.trim().length != 0) {
				alert(data);
			} else {
				alert(thuc_hien_thanh_cong, function() {
					if ($('select#templateId').length > 0
							&& $('select#templateId').val().trim().length > 0
							&& 'tab02' == currentActiveTabID)
						loadTemplate($('select#templateId').val());
				});
			}

		},
		error : function(data, xhr) {
			alert(thuc_hien_khong_thanh_cong);
		}
	});
	if ('tab01' == currentActiveTabID) {
		disabledAllDiv('sTab02', false);
	} else {
		disabledAllDiv('sTab01', false);
	}
}
function approveTemplateInf() {
	jConfirm('Bạn có thực sự muốn duyệt?', 'OK', 'Cancel', function(r) {
		if (r)
			fnDoApprove(1);
	});
}
function unApproveUploadFile() {

	$.ajax({
		type : "POST",
		url : $('#theForm').attr('action') + '?method=unApproveUploadFile',
		data : {
			templateId : $('select#templateId').val(),
			id : $('#id').val(),
			recordId : $('#recordId').val(),
			'profile.product.id' : $('#productId').val()
		},
		success : function(actionResult) {
			if (actionResult.trim().length == 0) {
				alert(thuc_hien_thanh_cong, function() {
					// reload de cache lai cac id
					if ($('select#templateId').length > 0
							&& $('select#templateId').val().trim().length > 0
							&& 'tab02' == currentActiveTabID)
						loadTemplate($('select#templateId').val(), $(
								'#recordId').val());
				});
			} else {
				alert(actionResult);
			}
		}
	});
}
function approveUploadFile() {
	$.ajax({
		type : "POST",
		url : $('#theForm').attr('action') + '?method=approveUploadFile',
		data : {
			templateId : $('select#templateId').val(),
			id : $('#id').val(),
			recordId : $('#recordId').val(),
			'profile.product.id' : $('#productId').val()
		},
		success : function(actionResult) {
			if (actionResult.trim().length == 0) {
				alert(thuc_hien_thanh_cong, function() {
					// reload de cache lai cac id
					if ($('select#templateId').length > 0
							&& $('select#templateId').val().trim().length > 0
							&& 'tab02' == currentActiveTabID)
						loadTemplate($('select#templateId').val(), $(
								'#recordId').val());
				});
			} else {
				alert(actionResult);
			}
		}
	});

}

function cancelApprove() {
	jConfirm('Bạn có thực sự muốn thực hiện lại?', 'OK', 'Cancel', function(r) {
		if (r)
			fnDoApprove(-1);
	});

}
function uploadFile() {
	$('#file-select').val('');
	$('#modal_upload_file').modal();
}

function unfinish() {
	fnDoApprove(0);

}

function verifyTemplateInf() {
	fnDoApprove(1);
}
function delTemplateInf() {

	$.ajax({
		type : "POST",
		url : $('#theForm').attr('action') + '?method=delTemplate',
		data : {
			templateId : $('select#templateId').val(),
			id : $('#id').val(),
			recordId : $('#recordId').val(),
			'profile.product.id' : $('#productId').val(),
			'aproveVal' : aproveValue
		},
		success : function(actionResult) {

			if (actionResult.trim().length == 0) {
				alert(thuc_hien_thanh_cong, function() {
					// reload de cache lai cac id
					if ($('select#templateId').length > 0
							&& $('select#templateId').val().trim().length > 0
							&& 'tab02' == currentActiveTabID)
						loadTemplate($('select#templateId').val());
				});
			} else {
				alert(actionResult);

			}
		}
	});
}
function fnDoApprove(aproveValue) {

	$.ajax({
		type : "POST",
		url : $('#theForm').attr('action') + '?method=approveTemplate',
		data : {
			templateId : $('select#templateId').val(),
			id : $('#id').val(),
			recordId : $('#recordId').val(),
			'profile.product.id' : $('#productId').val(),
			'aproveVal' : aproveValue
		},
		success : function(actionResult) {
			if (actionResult.trim().length == 0) {
				alert(thuc_hien_thanh_cong, function() {
					// reload de cache lai cac id
					if ($('select#templateId').length > 0
							&& $('select#templateId').val().trim().length > 0
							&& 'tab02' == currentActiveTabID)
						loadTemplate($('select#templateId').val(), $(
								'#recordId').val());
				});
			} else {
				alert(actionResult);

			}
		},
		error : function(data, xhr) {
			alert(thuc_hien_khong_thanh_cong);
		}
	});
}

function makeChkScriptOnMulTiTatab(radioId) {
	$('input[type=\'radio\'][id=\'' + radioId + '\']').each(
			function() {
				$(this).click(
						function() {
							$(
									'input[type=\'radio\'][id=\'' + radioId
											+ '\'][value=\'' + $(this).val()
											+ '\']').prop('checked', true);
						});
			});
}
function instanceSaveFalse() {

}
function instanceSuccess(res) {
	alert(thuc_hien_thanh_cong, function() {
		edit(res.id);
	})

}
function instanceSuccess1(result) {

	if ('tab01' == currentActiveTabID) {
		disabledAllDiv('sTab02', false);
	} else {
		disabledAllDiv('sTab01', false);
	}

	if (result['id'] !== undefined && result['id'] != '') {
		$('#id').val(result['id']);

	} else {
		alert(result);
		return;
	}

	alert(thuc_hien_thanh_cong, function() {
		// reload de cache lai cac id
		if ('tab01' == currentActiveTabID) {
			/*
			 * if(window.location.href.indexOf('printfBalance')>=0){ return; }
			 * window.location.reload(); return;
			 */
			window.location.href = $('#theForm').attr('action') + '?id='
					+ result['id'];
		}
		if ($('select#templateId').length > 0
				&& $('select#templateId').val().trim().length > 0
				&& 'tab02' == currentActiveTabID)
			loadTemplate($('select#templateId').val(), $('#recordId').val());

	});
}

function showGroup1(lstDiv, jTriggerControl) {
	lstDiv.each(function() {
		if (!$(this).hasClass('grid'))
			$(this).show();
		$(this).find('select, input, textarea').prop('disabled', false);
	});
}
/**
 * Chi an hien dong
 * 
 * @param jTriggerControl
 */
function showGroup(jTriggerControl) {
	if (!jTriggerControl.hasClass('radioPanel'))
		return;
	var activeClss = jTriggerControl.attr('class').split(' ')[2];

	// Hide others
	var parentClss = jTriggerControl.attr('class').split(' ')[1];
	// loop all radio
	$('.radioPanel.' + parentClss).each(
			function() {
				if (!$(this).hasClass(activeClss)) {
					var inactiveClss = $(this).attr('class').split(' ')[2];
					$('.groupHeader.' + inactiveClss).hide();
					$('.row.' + inactiveClss).each(
							function() {
								// Cac dong ko phai grid
								if (!$(this).hasClass('grid'))
									$(this).hide();
								else {// disabled grid de kg submit
									$(this).find('select, input, textarea')
											.prop('disabled', true);
									$(this).hide();
								}
							});
				}
			});

	$('.groupHeader.' + activeClss).show();
	$('.row.' + activeClss).each(function() {
		// Cac dong ko phai grid
		if (!$(this).hasClass('grid'))
			$(this).show();
		else {// enable grid de submit
			$(this).find('select, input, textarea').prop('disabled', false);
			$(this).show();
		}
	});

}

/**
 * Chi an hien dong
 * 
 * @param jTriggerControl
 */
function showGroupAutodoc1(jTriggerControl) {
	if (!jTriggerControl.hasClass('radioPanel'))
		return;
	var activeClss = jTriggerControl.attr('class').split(' ')[2];
	$('.groupHeader.' + activeClss).show();
	$('.row.' + activeClss).each(function() {
		// Cac dong ko phai grid
		if (!$(this).hasClass('grid'))
			$(this).show();
		else {// enable grid de submit
			$(this).find('select, input, textarea').prop('disabled', false);
		}
	});

	// Hide others
	var parentClss = jTriggerControl.attr('class').split(' ')[1];
	// loop all radio
	$('.radioPanel.' + parentClss).each(
			function() {
				if (!$(this).hasClass(activeClss)) {
					var inactiveClss = $(this).attr('class').split(' ')[2];
					$('.groupHeader.' + inactiveClss).hide();
					$('.row.' + inactiveClss).each(
							function() {
								// Cac dong ko phai grid
								if (!$(this).hasClass('grid'))
									$(this).hide();
								else {// disabled grid de kg submit
									$(this).find('select, input, textarea')
											.prop('disabled', true);
								}
							});
				}
			});
}

function hideGroup(lstDiv, jTriggerControl) {
	lstDiv.each(function() {
		if (!$(this).hasClass('grid'))
			$(this).hide();
		$('.radioPanel').show();
		$(this).find('select, input, textarea').prop('disabled', true);
	});
}
function dynFormReadyScript() {
	// Gan su kien
	$('.checkbox_group')
			.each(
					function() {
						$(this)
								.click(
										function() {
											if ($(this).prop('checked')) {
												$('.Row.' + $(this).prop('id'))
														.each(
																function() {
																	if (!$(this)
																			.hasClass(
																					'grid'))
																		$(this)
																				.show();
																	else
																		$(this)
																				.find(
																						'select, input, textarea')
																				.prop(
																						'disabled',
																						false);
																})
											} else {
												$('.Row.' + $(this).prop('id'))
														.each(
																function() {
																	if (!$(this)
																			.hasClass(
																					'grid'))
																		$(this)
																				.hide()
																	else
																		$(this)
																				.find(
																						'select, input, textarea')
																				.prop(
																						'disabled',
																						true);
																});
											}

										});
					});
	// Mac dinh gia tri
	// $(".checkbox_group").trigger("click");
	$('.checkbox_group').each(
			function() {
				if ($(this).prop('checked')) {
					$('.Row.' + $(this).prop('id')).each(
							function() {
								if (!$(this).hasClass('grid'))
									$(this).show();
								else
									$(this).find('select, input, textarea')
											.prop('disabled', false);
							})
				} else {
					$('.Row.' + $(this).prop('id')).each(
							function() {
								if (!$(this).hasClass('grid'))
									$(this).hide()
								else
									$(this).find('select, input, textarea')
											.prop('disabled', true);
							});
				}
			});
}
function prcssChkGrp() {
	$('.checkbox_group').each(
			function() {
				if ($(this).prop('checked'))
					return;
				$('.Row.' + $(this).prop('id')).find('select, input, textarea')
						.each(
								function() {
									if ($(this).attr('class').indexOf(
											'gridColumn') >= 0
											|| $(this).attr('class').indexOf(
													'gridcolumn') >= 0) {
										$(this).prop('disabled', true);
									} else {
										$(this).val('');
									}

								});
			});
}
function inActiveTab(jCtrol, parentClss) {
	var rs = false;
	var activeTabId = 'tab01' == currentActiveTabID ? "#sTab01" : "#sTab02";
	$(activeTabId)
			.find('.radioPanel.' + parentClss)
			.each(
					function() {
						if ($(this).prop('checked')) {
							var activeClss = $(this).attr('class').split(' ')[2];
							if ($(activeTabId).find('.row.' + activeClss).find(
									'[name="' + jCtrol.attr('name') + '"]').length > 0) {
								if (!rs)
									rs = true;
							}

						}
					});
	return rs;
}
function beforeSave() {

}
function beforeSave1() {
	// process checkbox group
	prcssChkGrp();
	if ('tab01' == currentActiveTabID) {
		disabledAllDiv('sTab01', false);
		disabledAllDiv('sTab02', true);
		$('#fastInputTemplateId').prop('disabled', false);
	} else {
		disabledAllDiv('sTab01', true);
		disabledAllDiv('sTab02', false);
		$('#fastInputTemplateId').prop('disabled', true);
	}
	var activeTabId = 'tab01' == currentActiveTabID ? "#sTab01" : "#sTab02";
	// Cac field khong hop le duoc submit voi gia tri ''
	$(activeTabId)
			.find('.radioPanel')
			.each(
					function() {
						if (!$(this).prop('checked')) { // radioPanel 9 HDHM
							if ($(this).attr('class') != ''
									&& $(this).attr('class') != undefined
									&& $(this).attr('class').split(' ').length >= 3) {
								var inactiveClss = $(this).attr('class').split(
										' ')[2];
								var parentClss = $(this).attr('class').split(
										' ')[1];
								if (inactiveClss != undefined) {
									$(activeTabId)
											.find('.row.' + inactiveClss)
											.each(
													function() {
														try {
															if (!$(this)
																	.hasClass(
																			'grid')) {
																$(this)
																		.find(
																				'select, input, textarea')
																		.each(
																				function() {
																					if (inActiveTab(
																							$(this),
																							parentClss)) {
																						if ($(
																								this)
																								.parent()
																								.parent()
																								.css(
																										'display') == "none") {
																							$(
																									this)
																									.prop(
																											'disabled',
																											true);
																							console
																									.log('disabled: '
																											+ $(
																													this)
																													.attr(
																															'name'))
																						}
																					} else {
																						if ($(
																								this)
																								.parent()
																								.parent()
																								.css(
																										'display') == "none") {
																							console
																									.log('empty: '
																											+ $(
																													this)
																													.attr(
																															'name')
																											+ ', value: '
																											+ $(
																													this)
																													.val());
																							$(
																									this)
																									.val(
																											'');
																						}

																					}

																				});
															} else {
																$(this)
																		.find(
																				'select, input, textarea')
																		.prop(
																				'disabled',
																				true);
															}
														} catch (err) {
														}
													});
								}

							}
						}
					});

}

function addNewRecord() {
	$.ajax({
		type : "POST",
		url : $('#theForm').attr('action') + '?method=loadTemplate',
		data : {
			idBieuMau : $('#templateId').val(),
			id : $('#id').val(),
			recordId : -1,
			'profile.product.id' : $('#productId').val()
		},
		success : function(htmlContent) {
			$('#divDetailTemplate').html(htmlContent);
			initControl();
			if ($('#divDetailTemplate input[type!="hidden"]').length > 0)
				$('#divDetailTemplate input[type!="hidden"]')[0].focus();

			tableTableHis = new TFOnline.DataTable({
				id : 'tableTableHisId',
				jQueryUI : true,
				hasCheck : true,
				maxRow : 100,
				readOnly : true
			});

			$('#recordId').val('');
			globalRecordId = '';
			// An tat ca cac action button
			$('.divaction.tabTemplate input[type="button"]').css('display',
					'none');
			// Hien thi button save, cancelAddNew, chuyen phe duyet, hoan thanh

			$('#btnSave, #btnCancelAddNew, #btnFinish, #btnToApprove').css(
					'display', 'inline');
		}
	});

}
function cancelAddNew() {
	loadTemplate($('#templateId').val(), globalRecordId);
}

function downloadECM(fileId, fileNameField, fileIdField) {
	var cacheAction = $('#theForm').attr('action');
	var idx = $(fileId).parent().parent().find("input").attr("value");
	if (idx == null || idx === '') {
		alert('Phải lưu bản ghi trước!');
		return;
	}
	$('#theForm').attr(
			'action',
			$('#theForm').attr('action') + '?method=download&fileId=' + idx
					+ '&fileNameField=' + fileNameField + '&fileIdField='+fileIdField);
	document.getElementById("theForm").submit();
	$('#theForm').attr('action', cacheAction);
}
var fileNamePath, idRecordUpload;
var xd;
function uploadECM(fileNamePathServer, id, filePath) {
	xd = id;
	var idx = $(xd).parent().parent().find("input").attr("value");
	var templateId = $('#id').val();
	if (templateId != null && templateId != '') {
		$('#filePath').val(filePath);
		// $('#idRecordUpload').val(id);
		fileNamePath = fileNamePathServer;
		idRecordUpload = idx;
		// $('#fileNamePath').val(fileNamePath);
		$('#file-select').val('');
		$('#modal_upload_file').modal();
	} else {
		alert("Phải lưu biểu mẫu trước khi tải phải lên");
	}
}
