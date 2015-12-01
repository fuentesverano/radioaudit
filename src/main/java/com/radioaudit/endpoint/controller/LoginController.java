package com.radioaudit.endpoint.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.radioaudit.domain.thread.MpegDecorder;
import com.radioaudit.domain.to.UserDTO;
import com.radioaudit.endpoint.model.UserAuthenticationModel;
import com.radioaudit.service.UserAuthenticationBean;

@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

	private final Logger LOGGER = LoggerFactory.getLogger(MpegDecorder.class);

	@Autowired
	private UserAuthenticationBean userAuthenticationBean;

	// @RequestMapping(value = "authenticate", method = RequestMethod.POST)
	public RedirectView authenticate(HttpServletRequest request, UserAuthenticationModel authenticationModel) {

		LOGGER.info("Authenticating user with email: {}", authenticationModel.getEmail());

		UserDTO userDTO = this.userAuthenticationBean.authenticate(authenticationModel.getEmail(),
				authenticationModel.getPassword());

		HttpSession httpSession = request.getSession(true);
		httpSession.setAttribute("user", userDTO);

		if (userDTO == null) {

		}
		return this.goToHome(request);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView show(HttpServletRequest request) {
		return new ModelAndView(LOGIN_PAGE);
	}

	@RequestMapping(value = "/success-login", method = RequestMethod.GET)
	public String successLogin() {
		return "forward:/home";
	}

	@RequestMapping(value = "/error-login", method = RequestMethod.GET)
	public ModelAndView invalidLogin() {
		ModelAndView modelAndView = new ModelAndView(LOGIN_PAGE);
		modelAndView.addObject("error", true);
		return modelAndView;
	}

	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ModelAndView logout() {
		return new ModelAndView(LOGIN_PAGE);
	}

	@RequestMapping(value = "/process-login", method = RequestMethod.POST)
	public ModelAndView processLogin() {
		return new ModelAndView(HOME_PAGE);
	}
}
