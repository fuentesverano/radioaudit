package com.radioaudit.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("userDetailsService")
	private UserDetailsService userDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("user").password("password").roles("USER");
		auth.inMemoryAuthentication().withUser("ffuentes").password("cielo01").roles("ADMIN");
		auth.userDetailsService(this.userDetailsService);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/home").permitAll().anyRequest().authenticated().and().formLogin()
				.loginPage("/login").permitAll().and().logout().permitAll();
		// http.authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')").antMatchers("/dba/**")
		// .access("hasRole('ROLE_ADMIN') or hasRole('ROLE_DBA')").and().formLogin();

		// http.httpBasic().and().authorizeRequests().anyRequest().hasRole("USER");
		//
		// // http.authorizeRequests().antMatchers("/home").hasAnyRole("ADMIN",
		// // "USER").antMatchers("/admin/**").hasRole("ADMIN")
		// // .and().httpBasic();
		// // // .formLogin().loginPage("/login");
		//
		// // .failureUrl("/login?error")
		// //
		// .usernameParameter("email").passwordParameter("password").and().logout().logoutSuccessUrl("/login?logout")
		// // .and().exceptionHandling().accessDeniedPage("/403");
		//
		// http.authorizeRequests().anyRequest().hasAuthority("BASIC_PERMISSION").and().formLogin().loginPage("/login")
		// .defaultSuccessUrl("/home", true).failureUrl("/login/error-login")
		// .loginProcessingUrl("/login/process-login").usernameParameter("username").passwordParameter("password")
		// .permitAll().and().logout().logoutSuccessUrl("/login").logoutUrl("/login").permitAll().and().rememberMe()
		// .key("your_key").rememberMeServices(rememberMeServices()).and().csrf().disable();
	}

	@Bean
	public RememberMeServices rememberMeServices() {
		// Key must be equal to rememberMe().key()
		TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices("your_key",
				this.userDetailsService);
		rememberMeServices.setCookieName("radioaudit_cookie");
		rememberMeServices.setParameter("remember_me_checkbox");
		rememberMeServices.setTokenValiditySeconds(2678400); // 1month
		return rememberMeServices;
	}

	@Bean
	@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
	public UserDetails authenticatedUserDetails() {
		SecurityContextHolder.getContext().getAuthentication();
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			if (authentication instanceof UsernamePasswordAuthenticationToken) {
				return (UserDetails) ((UsernamePasswordAuthenticationToken) authentication).getPrincipal();
			}
			if (authentication instanceof RememberMeAuthenticationToken) {
				return (UserDetails) ((RememberMeAuthenticationToken) authentication).getPrincipal();
			}
		}
		return null;
	}
}
