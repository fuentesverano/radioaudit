package com.radioaudit.endpoint.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

public abstract class BaseController {

	protected static final String HOME_PAGE = "home";
	protected static final String LOGIN_PAGE = "login";

	protected static final String HOME_FORWARD = "/home";
	protected static final String LOGIN_FORWARD = "/login";

	protected static final String DATE_FORMAT_JS = "dd/mm/yy";
	protected static final String DATE_FORMAT_DEFAULT = "dd/MM/yyyy";

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_DEFAULT);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/************************************************************************************************************
	 * PUBLIC METHODS - HANDLERS OF HTTPREQUESTS
	 ************************************************************************************************************/

	/************************************************************************************************************
	 * PROTECTED AUXILIAR METHODS
	 ************************************************************************************************************/

	protected RedirectView goToHome(HttpServletRequest request) {
		return new RedirectView(HOME_FORWARD, false);
	}

	protected RedirectView goToLOgin(HttpServletRequest request) {
		return new RedirectView(LOGIN_FORWARD, false);
	}

	protected void addGeneralPropertiesModel(HttpServletRequest request, Map<String, Object> modelMap) {
	}

	/************************************************************************************************************
	 * ABSTRACT METHODS
	 ************************************************************************************************************/
	protected final String getControllerName() {
		String controllerName = this.getClass().getAnnotation(RequestMapping.class).value()[0];
		if (controllerName.startsWith("/")) {
			controllerName = controllerName.substring(1);
		}
		return controllerName;
	}

}
