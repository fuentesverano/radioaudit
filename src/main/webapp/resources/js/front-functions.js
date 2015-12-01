/*******************************************************************************
 * PARAMETERS
 ******************************************************************************/

var gen_taskId = null;

var gen_readOnly = false;

gen_i18n.message_ajaxError = 'Un error a ocurrido al tratar de satisfacer su pedido';

/*******************************************************************************
 * READONLY GLOBAL VARIABLES
 ******************************************************************************/

var gen_loadingPagePending = 0;// Amount of pending ajax for the loading page

/*******************************************************************************
 * PRIVATE VARIABLES
 ******************************************************************************/

var _gen_taskIdParameterName = "taskIdControl";
var _gen_ajaxError_redirecting = false;
var _idIntervalKeepAlive = 0;
var _idIntervalReconnect = 0;

/*******************************************************************************
 * FORM FUNCTIONS
 ******************************************************************************/

$(document)
		.ready(
				function() {
					var $parentform = $("input[type=file]").parents(
							'form:first')
							.attr("enctype", "multipart/form-data");
					$parentform.attr("enctype", "multipart/form-data");
					$parentform
							.append('<input type="submit" value="" style="display: none;"/>');
				});

/*******************************************************************************
 * GENERAL FUNCTIONS
 ******************************************************************************/

function gen_disableElements(selector) {
	_gen_disableElements(selector, "body");
}

function gen_enableElements(selector) {
	_gen_enableElements(selector, "body");
}

function applyReadOnlyToControls(selectorContext,
		listSelectorElementsToDisable, listSelectorElementsToMaintainEnabled,
		listSelectorElementsToHidden) {
	var context = $(selectorContext);

	if (gen_readOnly) {
		if ((listSelectorElementsToDisable == null)
				|| (listSelectorElementsToDisable.length == 0)) {
			_gen_disableElements('button', context);
			_gen_disableElements('input:not([type="hidden"])', context); // all
																			// inputs
																			// except
																			// the
																			// hiddens
			_gen_disableElements('select', context);
			_gen_disableElements('a', context);
		} else {
			$.each(listSelectorElementsToDisable, function(index, value) {
				_gen_disableElements(value, context);
			});
		}

		if ((listSelectorElementsToHidden != null)
				&& (listSelectorElementsToHidden.length > 0)) {
			$.each(listSelectorElementsToHidden, function(index, value) {
				_hiddenElements(value, context);
			});
		}

		if ((listSelectorElementsToMaintainEnabled != null)
				&& (listSelectorElementsToMaintainEnabled.length > 0)) {
			$.each(listSelectorElementsToMaintainEnabled,
					function(index, value) {
						_gen_enableElements(value, context);
					});
		}
	}
}

function gen_atLeastOneCheckboxItemSelected(checkBoxClass) {
	var checked = $("input." + checkBoxClass + "[type=checkbox]:checked").length;
	if (checked >= 1) {
		return true;
	} else {
		return false;
	}
}

/*******************************************************************************
 * AJAX FUNCTIONS
 ******************************************************************************/

function gen_post(url, params, successCb, errorCb) {
	_gen_post(url, params, successCb, errorCb, true);
}

function gen_postLoadingPage(url, params, successCb, errorCb) {
	// increment the amount of pending ajax
	gen_loadingPagePending++;
	_gen_post(url, params, function(data) {
		gen_loadingPagePending--;
		successCb(data);
	},// errorCb
	function() {
		gen_loadingPagePending--;
		if (errorCb) {
			errorCb();
		}
	},
	// Do not control task assignee
	false);
}

function gen_postController(controller, action, params, successCb, errorCb) {
	gen_post(controller + "/" + action, params, successCb, errorCb);
}

function gen_getAjaxDialog(url, params, popUpSelector, closeButton, width,
		height, errorMessage, acceptButton, acceptFunction, customAcceptLabel,
		afterPopupLoadFunction) {
	gen_post(url, params, function(data) {
		$(popUpSelector).html(data);
		gen_popup({
			selector : popUpSelector,
			ancho : width,
			alto : height,
			cerrar : closeButton,
			aceptar : acceptButton,
			aceptarFuncion : acceptFunction,
			aceptarCustomLabel : customAcceptLabel,
			afterLoadFunction : afterPopupLoadFunction
		});
	},// errorCb
	function() {
		$(popUpSelector).html(
				"<b>"
						+ (errorMessage ? errorMessage
								: gen_i18n.message_ajaxError) + "</b>");
		gen_popup({
			selector : popUpSelector,
			ancho : 500,
			cerrar : true
		});
	});
}

function gen_getAjaxDialogController(controller, action, params, popUpSelector,
		closeButton, width, height, errorMessage, acceptButton, acceptFunction,
		customAcceptLabel, afterPopupLoadFunction) {
	gen_getAjaxDialog(controller + "/" + action, params, popUpSelector,
			closeButton, width, height, errorMessage, acceptButton,
			acceptFunction, customAcceptLabel, afterPopupLoadFunction);
}

/*******************************************************************************
 * SUBMIT FUNCTIONS
 ******************************************************************************/

//// Build a form from the parameters and makes an HTTP POST.
//function gen_submitData(url, extraParams) {
//
//	gen_safetyFunctionCall("waitingPopUp_open");
//	if (_waitingPopUp_open) {
//		_waitingPopUp_open = false;
//	}
//
//	// generate hidden form
//	var datas = "";
//	var $form = $("#pageForm");
//	for ( var att in extraParams) {
//		datas += "<input type='hidden' name='" + att + "' value='"
//				+ extraParams[att] + "'/>";
//	}
//	$form.append(datas);
//
//	setTimeout(function() {
//		_gen_submitForm($form, url);
//	}, 500);
//
//}

//Build a form from the parameters and makes an HTTP POST.
function gen_submitData(controller, action, extraParams, validateForm) {

	gen_safetyFunctionCall("waitingPopUp_open");
	if (_waitingPopUp_open) {
		_waitingPopUp_open = false;
	}

	//control takenTask
	if(!extraParams[_gen_taskIdParameterName] && gen_taskId!=null){
		extraParams[_gen_taskIdParameterName] = gen_taskId;
	}
	
	// generate hidden form
	var datas = "";
	for ( var att in extraParams) {
		datas += "<input type='hidden' name='" + att + "' value='"
				+ extraParams[att] + "'/>";
	}
	$("#pageForm").append(datas);

	setTimeout(function() {
		gen_submitForm("pageForm", controller, action, validateForm);
	}, 500);

}

function gen_submitForm(formId, controller, action, validateForm) {

	if (!validateForm || gen_valid(formId)) {
		var $form = $('#' + formId);
		_gen_submitForm($form, gen_buildURL(controller, action));
		gen_disableElements("a,button,input,select");
	}
	else{
		gen_safetyFunctionCall("waitingPopUp_close");
	}
}

function gen_valid(formId){
	var $form = $('#' + formId);
	
	$form.LeValidate({
		notifyBox : "le-notification-orange",
		errorMessages : versionedStaticResource_errorMessages
	});
	
	return $form.valid();
}

// ------------

function gen_createToolTip_ajax(selectorTrigger, url, data, selectorClose,
		textTitle, corner_tooltip, corner_target, width, postCreateFunction) {
	// Uses the qtip jquery plugin

	$(selectorTrigger).qtip({
		content : {
			title : {
				text : textTitle,
				button : $(selectorClose).html()
			},
			url : url,
			data : data,
			method : 'post'
		},
		position : {
			corner : {
				tooltip : corner_tooltip, // Use the corner...
				target : corner_target
			// ...and opposite corner
			}
		},
		show : {
			when : 'click', // Show it on click
			solo : true
		// And hide all other tooltips
		},
		hide : {
			when : {
				event : 'unfocus'
			}
		},
		style : {
			width : width,
			padding : '14px',
			border : {
				width : 9,
				radius : 9,
				color : '#666666'
			},
			name : 'light',
			tip : true
		},
		api : {
			onShow : function() {
				if (postCreateFunction != null) {
					postCreateFunction();
				}
			}
		}
	});

}

function gen_createToolTip(selectorTrigger, selectorContent, selectorClose,
		textTitle, corner_tooltip, corner_target, width, postCreateFunction) {
	// Uses the qtip jquery plugin

	$(selectorTrigger).qtip({
		content : {
			title : {
				text : textTitle,
				button : $(selectorClose).html()
			},
			text : $(selectorContent).html()
		},
		position : {
			corner : {
				tooltip : corner_tooltip, // Use the corner...
				target : corner_target
			// ...and opposite corner
			}
		},
		show : {
			when : 'click', // Show it on click
			solo : true
		// And hide all other tooltips
		},
		hide : {
			when : {
				event : 'unfocus'
			}
		},
		style : {
			width : width,
			padding : '14px',
			border : {
				width : 9,
				radius : 9,
				color : '#666666'
			},
			name : 'light',
			tip : true
		},
		api : {
			onShow : function() {
				if (postCreateFunction != null) {
					postCreateFunction();
				}
			}
		}
	});

}

// ----------------------------------------------------------------------------------------------

function gen_iniPoolingKeepAlive(controller, functionIniReconnect,
		functionEndReconnect, keepAliveFrequency, timeOutKeepAliveFrequency) {

	if (keepAliveFrequency > 0) {
		_idIntervalKeepAlive = window.setInterval(function() {
			_keepAlive(controller, "reconnect_dialog", "event_dialog",
					functionIniReconnect, functionEndReconnect,
					keepAliveFrequency, timeOutKeepAliveFrequency);
		}, keepAliveFrequency);
	}

}

function gen_stopPoolingKeepAlive() {
	_stopPoolingKeepAlive();
}

/*******************************************************************************
 * PRIVATE FUNCTIONS
 ******************************************************************************/

// ----------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------
function _stopPoolingKeepAlive() {
	clearInterval(_idIntervalKeepAlive);
}

// --------

function _iniPoolingReconnect(controller, functionIniReconnect,
		functionEndReconnect, keepAliveFrequency, timeOutKeepAliveFrequency) {

	functionIniReconnect(controller);

	_idIntervalReconnect = window.setInterval(function() {
		_reconnect(controller, functionIniReconnect, functionEndReconnect,
				keepAliveFrequency, timeOutKeepAliveFrequency);
	}, keepAliveFrequency / 2);
}

function _stopPoolingReconnect() {
	clearInterval(_idIntervalReconnect);
}

// --------

function _keepAlive(controller, idPopupReconnect, idPopupEvent,
		functionIniReconnect, functionEndReconnect, keepAliveFrequency,
		timeOutKeepAliveFrequency) {
	gen_loadingPagePending++;

	var data = {};
	if (gen_taskId != null) {
		data[_gen_taskIdParameterName] = gen_taskId;
	}

	$
			.ajax({
				cache : false,
				timeout : timeOutKeepAliveFrequency, // Miliseconds
				type : "POST",
				url : gen_buildURL(controller + "/keepAlive"),
				data : data,
				success : function(data) {
					gen_loadingPagePending--;
					if (data.containingEvent) {
						gen_showMsg(data.type, data.msg);
					}
				},
				error : function(xhr, status, error) {
					gen_loadingPagePending--;
					if (xhr.status == 401 || xhr.status == 403) {
						var data = jQuery.parseJSON(xhr.responseText);
						if (data.redirect_uri) {
							_gen_ajaxError_redirecting = true;
							window.location
									.replace(/^http.*/.test(data.redirect_uri) ? data.redirect_uri
											: (gen_contextPath + '/' + data.redirect_uri));
						}
					} else if ((status == 'timeout') || (status == 'error')) {

						_stopPoolingKeepAlive();

						gen_popup({
							selector : "#" + idPopupReconnect,
							ancho : 360,
							alto : 200,
							cerrar : false,
							permitirCerrar : false
						});

						_iniPoolingReconnect(controller, functionIniReconnect,
								functionEndReconnect, keepAliveFrequency,
								timeOutKeepAliveFrequency);
					}
				}
			});
}

// --------

function _reconnect(controller, functionIniReconnect, functionEndReconnect,
		keepAliveFrequency, timeOutKeepAliveFrequency) {
	gen_loadingPagePending++;
	var data = {};
	if (gen_taskId != null) {
		data[_gen_taskIdParameterName] = gen_taskId;
	}
	$
			.ajax({
				cache : false,
				timeout : timeOutKeepAliveFrequency, // Miliseconds
				type : "POST",
				url : gen_buildURL(controller + "/keepAlive"),
				data : data,
				success : function(data) {
					gen_loadingPagePending--;
					functionEndReconnect();
					_stopPoolingReconnect();
					gen_iniPoolingKeepAlive(controller, functionIniReconnect,
							functionEndReconnect, keepAliveFrequency,
							timeOutKeepAliveFrequency);
				},
				error : function(xhr, status, error) {
					gen_loadingPagePending--;
					if (xhr.status == 401 || xhr.status == 403) {
						var data = jQuery.parseJSON(xhr.responseText);
						if (data.redirect_uri) {
							_gen_ajaxError_redirecting = true;
							window.location
									.replace(/^http.*/.test(data.redirect_uri) ? data.redirect_uri
											: (gen_contextPath + '/' + data.redirect_uri));
						}
					}
				}
			});
}

// ----------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------

function _gen_post(url, params, successCb, errorCb, controlTask) {
	
//	alert("url: " + url);
//	alert("params: " + params);
//	alert("successCb: " + successCb);
//	alert("errorCb: " + errorCb);
//	alert("controlTask: " + controlTask);
	
	if (controlTask && gen_taskId != null) {
		if (params == null) {
			params = {};
		}
		params[_gen_taskIdParameterName] = gen_taskId;
	}
	return $.post(gen_buildURL(url), params, successCb).error(function(data) {
		if (!_gen_ajaxError_redirecting && errorCb) {
			errorCb();
		}
	});
}

//
// submits a form and add the taskId parameter
//
function _gen_submitForm($form, url) {

	gen_stopPoolingKeepAlive();

	if (gen_taskId != null && $form.find('[name="gen_taskId"]').length == 0) {
		$form.append("<input type='hidden' name='" + _gen_taskIdParameterName
				+ "' value='" + gen_taskId + "'/>");
	}
	$form.attr('action', url);

	$form.submit();
}

//
// If an error occurs redirect to redirect_uri page
//
$
		.ajaxSetup({
			error : function(xhr, status, error) {
				if (xhr.status == 401 || xhr.status == 403) {
					var data = jQuery.parseJSON(xhr.responseText);
					if (data.redirect_uri) {
						_gen_ajaxError_redirecting = true;
						window.location.replace(/^http.*/
								.test(data.redirect_uri) ? data.redirect_uri
								: (gen_contextPath + '/' + data.redirect_uri));
					}
				}
			}
		});

function _gen_disableElements(selector, context) {

	$(selector, context).attr("disabled", "disabled");

	$(selector, context).each(function(index) {

		var classToRemove = "";
		var classToAdd = "";

		if ($(this).hasClass("button-green")) {
			classToRemove = "ui-state-default-green";
			classToAdd = "ui-state-disabled-color";
		} else if ($(this).hasClass("button-yellow")) {
			classToRemove = "ui-state-default-yellow";
			classToAdd = "ui-state-disabled-color";
		} else if ($(this).hasClass("button-red")) {
			classToRemove = "ui-state-default-red";
			classToAdd = "ui-state-disabled-color";
		} else if ($(this).hasClass("ui-state-default")) {
			classToRemove = "ui-state-default";
			classToAdd = "ui-state-disabled";
		} else {
			if ($(this).prop("tagName") == "A") {
				$(this).css("display", "none");
			}
		}

		$(this).removeClass(classToRemove);
		$(this).addClass(classToAdd);
	});
}

function _gen_enableElements(selector, context) {
	$(selector, context).removeAttr("disabled");

	$(selector, context).each(function(index) {

		var classToAdd = "";
		var classToRemove = "";

		if ($(this).hasClass("button-green")) {
			classToAdd = "ui-state-default-green";
			classToRemove = "ui-state-disabled-color";
		} else if ($(this).hasClass("button-yellow")) {
			classToAdd = "ui-state-default-yellow";
			classToRemove = "ui-state-disabled-color";
		} else if ($(this).hasClass("button-red")) {
			classToAdd = "ui-state-default-red";
			classToRemove = "ui-state-disabled-color";
		} else if ($(this).hasClass("ui-state-disabled")) {
			classToAdd = "ui-state-default";
			classToRemove = "ui-state-disabled";
		} else {
			if ($(this).prop("tagName") == "A") {
				$(this).css("display", "inline");
			}
		}

		$(this).removeClass(classToRemove);
		$(this).addClass(classToAdd);
	});
}

function _hiddenElements(selector, context) {
	$(selector, context).css("display", "none");
}

function gen_downloadArchive(id, num, attachmentToken) {
	gen_downloadArchive(template_myController, id, num, attachmentToken);
}

function gen_downloadArchive(controller, id, num, attachmentToken) {
	gen_postController(controller, "urlAttachmentForToken", {
		token : attachmentToken
	}, function(url) {
		if (url != '') {
			$("body").append($('<iframe>', {
				frameBorder : '0',
				width : '0',
				height : '0',
				css : 'display:none; visibility:hidden; height:0px;',
				src : url
			}));
			console.log("OK");
		} else {
			console.log("Failed to download file");
			alert("Failed to download file");
		}
	}, function() {
		console.log("Failed to download file");
		alert("Failed to download file");
	});
}

function gen_downloadArchiveFromToken(attachmentToken) {
	gen_postController("attachFile", "urlAttachmentForToken", {
		token : attachmentToken
	}, function(url) {
		if (url != '') {
			$("body").append($('<iframe>', {
				frameBorder : '0',
				width : '0',
				height : '0',
				css : 'display:none; visibility:hidden; height:0px;',
				src : url
			}));
			console.log("OK");
		} else {
			console.log("Failed to download file");
			alert("Failed to download file");
		}
	}, function() {
		console.log("Failed to download file");
		alert("Failed to download file");
	});
}



//function gen_validateContent(contentId) {
//	var formId = contentId + "form";
//
//	var formHtml = $("#" + formId).html();
//	if (formHtml == null) {
//		$('#' + contentId).wrap('<form action="#"  id="' + formId + '"/>');
//	}
//	var isContentValid = $("#" + formId).valid();
//	return isContentValid;
//}
