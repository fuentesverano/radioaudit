package com.radioaudit.domain.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "JINGLE")
public class Jingle extends AbstractEntity {

	private String name;
	private String format;
	private Date creationDate = new Date();
	private int duration;
	private byte[] fingerprint;
	private User user;
	private Set<Radio> radios;

	@Column(name = "NAME", nullable = false, unique = true)
	public String getName() {
		return name;
	}

	@Column(name = "FORMAT", nullable = false)
	public String getFormat() {
		return format;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CREATION_DATE", nullable = false)
	public Date getCreationDate() {
		return creationDate;
	}

	@Column(name = "DURATION", nullable = false)
	public int getDuration() {
		return duration;
	}

	@Column(name = "FINGERPRINT", nullable = false)
	public byte[] getFingerprint() {
		return fingerprint;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "USER_ID", nullable = false)
	public User getUser() {
		return this.user;
	}

	@ManyToMany(mappedBy = "suscribeJingles")
	public Set<Radio> getRadios() {
		return radios;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setFingerprint(byte[] fingerprint) {
		this.fingerprint = fingerprint;
	}

	public void setRadios(Set<Radio> radios) {
		this.radios = radios;
	}

}
