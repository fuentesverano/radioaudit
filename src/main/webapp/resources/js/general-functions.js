function trim(text) {
	return $.trim(text);
}

function performAction() {
	startProgressBar();
	document.forms[0].submit();
	return;
};

function performActionByFormIndex(index) {
	startProgressBar();
	document.forms[index].submit();
	return;
};

function enterKeyPressed(someFunction) {
	$(document).keydown(function(e) {
		if (e.which == 13) {
			someFunction();
		}
	});
}

function startProgressBar() {
	$("#light").css("display", "block");
	$("#fade").css("display", "block");
}

function stopProgressBar() {
	$("#light").css("display", "none");
	$("#fade").css("display", "none");
}

function swapEditBR() {
	var title = document.getElementById("editBR");
	var container = document.getElementById("businessRulesEdit");

	if (container.style.display == "block") {
		container.style.display = "none";
		title.innerHTML = "<a href=\"javascript: swapEditBR()\">Deseo editar los valores para evaluar Business Rules &gt;&gt;</a>";
	} else {
		container.style.display = "block";
		title.innerHTML = "<span>Valores adicionales</span> Utilizados para la evaluar si corresponde Emisión Automática.";
	}
}

function swapBox(id) {
	var title = document.getElementById("viewIdsLink");
	var container = document.getElementById("messages");

	if (container.style.display == "block") {
		container.style.display = "none";
		title.innerHTML = "clickee aqui para ver los ids";
	} else {
		container.style.display = "block";
		title.innerHTML = "clickee aqui para ocultar los ids";
	}
}

function clear(form) {
	$(document.forms[form]).find('textarea,input').not('.button').not(
			'[type=hidden]').each(function(id, campo) {
		campo.value = "";
	});
}

function FormatTextarea(form){
	var name  = $(document.forms[form])[0].id;
	name = "#" + name + "Response";
	var res = $(name).text();
	res = jQuery.parseJSON(res);
	var myFormattedString = FormatJSON(res);
	$(name).text(myFormattedString);
}

function RealTypeOf(v) {
	if (typeof (v) == "object") {
		if (v === null)
			return "null";
		if (v.constructor == (new Array).constructor)
			return "array";
		if (v.constructor == (new Date).constructor)
			return "date";
		if (v.constructor == (new RegExp).constructor)
			return "regex";
		return "object";
	}
	return typeof (v);
}

function FormatJSON(oData, sIndent) {
	if (arguments.length < 2) {
		var sIndent = "";
	}
	var sIndentStyle = "  ";
	var sDataType = RealTypeOf(oData);

	// open object
	if (sDataType == "array") {
		if (oData.length == 0) {
			return "[]";
		}
		var sHTML = "[";
	} else {
		var iCount = 0;
		$.each(oData, function() {
			iCount++;
			return;
		});
		if (iCount == 0) { // object is empty
			return "{}";
		}
		var sHTML = "{";
	}

	// loop through items
	var iCount = 0;
	$.each(oData,
			function(sKey, vValue) {
				if (iCount > 0) {
					sHTML += ",";
				}
				if (sDataType == "array") {
					sHTML += ("\n" + sIndent + sIndentStyle);
				} else {
					sHTML += ("\n" + sIndent + sIndentStyle + "\"" + sKey
							+ "\"" + ": ");
				}

				// display relevant data type
				switch (RealTypeOf(vValue)) {
				case "array":
				case "object":
					sHTML += FormatJSON(vValue, (sIndent + sIndentStyle));
					break;
				case "boolean":
				case "number":
					sHTML += vValue.toString();
					break;
				case "null":
					sHTML += "null";
					break;
				case "string":
					sHTML += ("\"" + vValue + "\"");
					break;
				default:
					sHTML += ("TYPEOF: " + typeof (vValue));
				}

				// loop
				iCount++;
			});

	// close object
	if (sDataType == "array") {
		sHTML += ("\n" + sIndent + "]");
	} else {
		sHTML += ("\n" + sIndent + "}");
	}

	// return
	return sHTML;
}
