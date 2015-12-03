package com.radioaudit.endpoint;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.radioaudit.domain.to.UserTO;
import com.radioaudit.service.UserBean;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserBean userBean;

	@RequestMapping(method = RequestMethod.POST)
	public UserTO create(@RequestBody UserTO userTO, HttpServletRequest request) {

		// CreateUserDTO createUserDTO = this.mapCreateUserDTO(createUserModel);
		// this.userBean.createUser(createUserDTO);
		// return this.goToHome(request);
		return userTO;
	}

}
