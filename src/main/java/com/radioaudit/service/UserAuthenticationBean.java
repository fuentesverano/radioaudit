package com.radioaudit.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.radioaudit.domain.dao.UserDAO;
import com.radioaudit.domain.model.User;
import com.radioaudit.domain.to.UserDTO;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class UserAuthenticationBean {

	@Autowired
	private UserDAO userDAO;

	public UserDTO authenticate(String username, String password) {

		User user = this.userDAO.readByUsername(username);
		if (user != null) {
			String passwordMD5 = this.convertToMD5(password);
			if (StringUtils.equals(passwordMD5, user.getPassword())) {
				UserDTO userDTO = this.createUserDTO(user);
				return userDTO;
			}
		}
		return null;
	}

	private UserDTO createUserDTO(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	private String convertToMD5(String password) {

		try {
			String hashPassword = DigestUtils.md5DigestAsHex(password.getBytes("UTF-8"));
			return hashPassword;
		} catch (Exception e) {
			// _log.error("Failed to encrypt password.", e);
		}
		return "";
	}

	public String encodePassword(String password) {
		return this.convertToMD5(password);
	}

}
