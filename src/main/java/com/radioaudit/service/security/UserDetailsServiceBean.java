package com.radioaudit.service.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.radioaudit.domain.dao.UserDAO;
import com.radioaudit.domain.model.User;

@Service("userDetailsService")
public class UserDetailsServiceBean implements UserDetailsService {

	@Autowired
	private UserDAO userDAO;

	@Transactional(readOnly = true)
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		User user = this.userDAO.findByEmail(username);

		GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(user.getAccountType().name());

		UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
				user.getPassword(), true, true, true, true, Arrays.asList(grantedAuthority));

		return userDetails;
	}
}
