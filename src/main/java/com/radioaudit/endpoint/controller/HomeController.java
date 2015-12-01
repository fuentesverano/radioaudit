package com.radioaudit.endpoint.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.radioaudit.domain.to.FrontRadioDTO;
import com.radioaudit.endpoint.model.HomeModel;
import com.radioaudit.endpoint.model.PageTaskModel;
import com.radioaudit.service.JingleBean;
import com.radioaudit.service.RadioBean;

@Controller
public class HomeController extends BaseController {

	@Autowired
	private RadioBean radioBean;

	@Autowired
	private JingleBean commercialBean;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public RedirectView redirectToHome(HttpServletRequest request) {
		// if has user valid redirect to home
		// else go to login
		return this.goToHome(request);
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView show(HttpServletRequest request) {

		PageTaskModel pageTaskModel = new PageTaskModel();
		pageTaskModel.setProductId(1L);

		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("pageTaskModel", pageTaskModel);
		modelMap.put("dateFormatJs", DATE_FORMAT_JS);
		this.addGeneralPropertiesModel(request, modelMap);

		List<FrontRadioDTO> userRadios = this.radioBean.findActiveRadiosByUser("ffuentes");
		modelMap.put("userRadios", userRadios);

		List<FrontRadioDTO> radios = this.radioBean.findDeactivatedRadiosByUser("ffuentes");
		HomeModel homeModel = new HomeModel();
		homeModel.setUserRadios(radios);
		modelMap.put("homeModel", homeModel);

		return new ModelAndView(HOME_PAGE, modelMap);
	}
}
