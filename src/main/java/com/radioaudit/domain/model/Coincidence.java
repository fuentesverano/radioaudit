package com.radioaudit.domain.model;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "COINCIDENCE")
public class Coincidence extends AbstractEntity {

	private Jingle jingle;
	private Radio radio;
	private Date timestamp;
	private BigDecimal matchPercent;
	private BigDecimal fingerprintSimilarity;


	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "JINGLE_ID", nullable = false)
	public Jingle getJingle() {
		return jingle;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TIMESTAMP", nullable = false)
	public Date getTimestamp() {
		return timestamp;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "RADIO_ID", nullable = false)
	public Radio getRadio() {
		return radio;
	}

	@Column(name = "MATCH_PERCENT", nullable = false)
	public BigDecimal getMatchPercent() {
		return matchPercent;
	}

	@Column(name = "FINGERPRINT_SIMILARITY", nullable = false)
	public BigDecimal getFingerprintSimilarity() {
		return fingerprintSimilarity;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setRadio(Radio radio) {
		this.radio = radio;
	}

	public void setJingle(Jingle jingle) {
		this.jingle = jingle;
	}

	public void setMatchPercent(BigDecimal matchPercent) {
		this.matchPercent = matchPercent;
	}

	public void setFingerprintSimilarity(BigDecimal fingerprintSimilarity) {
		this.fingerprintSimilarity = fingerprintSimilarity;
	}

}
