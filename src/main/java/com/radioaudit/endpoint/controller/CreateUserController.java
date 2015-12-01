package com.radioaudit.endpoint.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.radioaudit.domain.to.CreateUserDTO;
import com.radioaudit.endpoint.model.CreateUserModel;
import com.radioaudit.endpoint.model.PageTaskModel;
import com.radioaudit.service.UserBean;

@Controller
@RequestMapping("/createUser")
public class CreateUserController extends BaseController {

	protected static final String CREATE_USER = "createUser";

	@Autowired
	private UserBean userBean;

	@RequestMapping(value = "create", method = RequestMethod.POST)
	public RedirectView create(HttpServletRequest request, CreateUserModel createUserModel) {

		CreateUserDTO createUserDTO = this.mapCreateUserDTO(createUserModel);
		this.userBean.createUser(createUserDTO);
		return this.goToHome(request);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView show(HttpServletRequest request) {

		PageTaskModel pageTaskModel = new PageTaskModel();
		pageTaskModel.setProductId(1L);

		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("pageTaskModel", pageTaskModel);
		modelMap.put("dateFormatJs", DATE_FORMAT_JS);
		this.addGeneralPropertiesModel(request, modelMap);

		CreateUserModel createUserModel = new CreateUserModel();
		modelMap.put("createUserModel", createUserModel);

		return new ModelAndView(CREATE_USER, modelMap);
	}

	private CreateUserDTO mapCreateUserDTO(CreateUserModel createUserModel) {

		CreateUserDTO createUserDTO = new CreateUserDTO();
		createUserDTO.setUsername(createUserModel.getUsername());
		createUserDTO.setPassword(createUserModel.getPassword());
		createUserDTO.setFirstname(createUserModel.getFirstname());
		createUserDTO.setLastname(createUserModel.getLastname());
		createUserDTO.setEmail(createUserModel.getEmail());

		return createUserDTO;
	}

}
