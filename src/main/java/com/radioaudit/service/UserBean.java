package com.radioaudit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.radioaudit.domain.dao.UserDAO;
import com.radioaudit.domain.model.User;
import com.radioaudit.domain.model.constant.UserAccountType;
import com.radioaudit.domain.to.CreateUserDTO;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class UserBean {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private UserAuthenticationBean userAuthenticationBean;

	/**
	 * Return true if can create user
	 * 
	 * @param createUserDTO
	 * @return boolean
	 */
	@Transactional(readOnly = false)
	public boolean createUser(CreateUserDTO createUserDTO) {

		User user = new User();
		user.setUsername(createUserDTO.getUsername());
		user.setFirstname(createUserDTO.getFirstname());
		user.setLastname(createUserDTO.getLastname());
		user.setEmail(createUserDTO.getEmail());
		user.setAccountType(UserAccountType.ADMIN);

		String password = this.userAuthenticationBean.encodePassword(createUserDTO.getPassword());
		user.setPassword(password);

		// save user
		this.userDAO.save(user);

		return false;
	}

}
