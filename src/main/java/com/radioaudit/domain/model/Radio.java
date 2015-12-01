package com.radioaudit.domain.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.radioaudit.domain.model.constant.RadioFormat;

@Entity
@Table(name = "RADIO", uniqueConstraints = { @UniqueConstraint(columnNames = "code") })
public class Radio extends AbstractEntity {

	private String code;
	private String name;
	private String url;
	private String website;
	private RadioFormat format;
	private int bitrate;
	private boolean connect;
	private boolean active = true;
	private boolean play;
	private Set<Jingle> suscribeJingles;

	@Column(name = "CODE", nullable = false, unique = true)
	public String getCode() {
		return code;
	}

	@Column(name = "NAME", nullable = false)
	public String getName() {
		return name;
	}

	@Column(name = "URL", nullable = false)
	public String getUrl() {
		return url;
	}

	@Column(name = "WEBSITE", nullable = false)
	public String getWebsite() {
		return website;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "CONTENT_TYPE", nullable = false)
	public RadioFormat getFormat() {
		return format;
	}

	@Column(name = "BITRATE", nullable = false)
	public int getBitrate() {
		return bitrate;
	}

	@Column(name = "CONNECT", nullable = false)
	public boolean isConnect() {
		synchronized (this) {
			return connect;
		}
	}

	@Column(name = "ACTIVE", nullable = false)
	public boolean isActive() {
		synchronized (this) {
			return active;
		}
	}

	@Column(name = "PLAY", nullable = false)
	public boolean isPlay() {
		return play;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "RL_RADIO_SUSCRIBE_JINGLE", joinColumns = { @JoinColumn(name = "RADIO_ID") }, inverseJoinColumns = { @JoinColumn(name = "JINGLE_ID") })
	public Set<Jingle> getSuscribeJingles() {
		return suscribeJingles;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setFormat(RadioFormat format) {
		this.format = format;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	public void setConnect(boolean connect) {
		synchronized (this) {
			this.connect = connect;
		}
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public void setSuscribeJingles(Set<Jingle> suscribeJingles) {
		this.suscribeJingles = suscribeJingles;
	}

	public void setPlay(boolean play) {
		this.play = play;
	}

}
