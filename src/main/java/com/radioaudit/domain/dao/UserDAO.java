package com.radioaudit.domain.dao;

import javax.annotation.PostConstruct;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.radioaudit.domain.model.User;

@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
@Repository
public class UserDAO extends AbstractDAO<User> {

	@PostConstruct
	private void setPersistentClazz() {
		this.persistentClazz = User.class;
	}

	public User readByUsername(String username) {
		return this.readBy("username", username);
	}

	public User readByUsernameWithCurrentRadio(String username) {
		return this.readByPropertyWith("username", username, "radio");
	}

	public User readByUsernameWithCommercials(String username) {
		return this.readByPropertyWith("username", username, "commercials");
	}

	public User findByUsername(String username) {
		User user = this.readBy("username", username);
		if (user == null) {
			throw new UsernameNotFoundException("User with email can not be found");
		}
		return user;
	}

	public User findByEmail(String email) {
		User user = this.readBy("email", email);
		if (user == null) {
			throw new UsernameNotFoundException("User with email can not be found");
		}
		return user;
	}
}
