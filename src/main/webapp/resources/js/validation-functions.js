function validate(form){
	var action = true;
	$(document.forms[form]).find('input').not('.response').not('.unrequired').each(function (id, campo){		
		if(campo.value =="" || campo.value==null){
			alert ("El campo "+ campo.id + " no debe ser vacio.");
			action = false;
			return false;
		}
    });			
		if (action){	
			/*selectCurrentTab(form);*/
			performAction(form);	
		}		
}

function performAction(form){
	window.scrollTo(0,0);
    startProgressBar();
    $(document.forms[form]).submit();     
    return;
};

/*
function selectCurrentTab(form){
	$("#"+document.forms[form].id + "_selectedTab").val(getSelectedTab());	
	return;
}*/

function getSelectedTab(){
	return $("#tabs").tabs("option", "selected")+1;
}







