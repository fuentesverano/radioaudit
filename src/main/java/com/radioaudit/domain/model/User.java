package com.radioaudit.domain.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import com.radioaudit.domain.model.constant.UserAccountType;

@Entity
@Table(name = "USER", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
public class User extends AbstractEntity {

	private String username;
	private String firstname;
	private String lastname;
	private String password;
	private String email;
	private Date creationDate = new Date();
	private UserAccountType accountType;
	private Set<Jingle> jingles = new HashSet<Jingle>();

	@Column(name = "USERNAME", nullable = false, unique = true)
	public String getUsername() {
		return username;
	}

	@Column(name = "FIRSTNAME", nullable = false)
	public String getFirstname() {
		return firstname;
	}

	@Column(name = "LASTNAME", nullable = false)
	public String getLastname() {
		return lastname;
	}

	@Column(name = "PASSWORD", nullable = false)
	public String getPassword() {
		return password;
	}

	@Column(name = "EMAIL", nullable = false)
	public String getEmail() {
		return email;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATION_DATE", nullable = false)
	public Date getCreationDate() {
		return creationDate;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "ACCOUNT_TYPE", nullable = false)
	public UserAccountType getAccountType() {
		return accountType;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
	public Set<Jingle> getJingles() {
		return jingles;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setAccountType(UserAccountType accountType) {
		this.accountType = accountType;
	}

	public void setJingles(Set<Jingle> jingles) {
		this.jingles = jingles;
	}

}
