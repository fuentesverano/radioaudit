package com.radioaudit.domain.to;

import java.math.BigDecimal;
import java.util.Date;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.radioaudit.domain.model.Jingle;

public class PartialCoincidenceTO {

	private Jingle jingle;
	private BigDecimal matchSeconds;
	private FingerprintSimilarity fingerprintSimilarity;
	private Date date = new Date();

	public PartialCoincidenceTO(Jingle jingle, BigDecimal matchSeconds, FingerprintSimilarity fingerprintSimilarity) {
		this.setJingle(jingle);
		this.matchSeconds = matchSeconds;
		this.fingerprintSimilarity = fingerprintSimilarity;
	}

	public FingerprintSimilarity getFingerprintSimilarity() {
		return fingerprintSimilarity;
	}

	public void setFingerprintSimilarity(FingerprintSimilarity fingerprintSimilarity) {
		this.fingerprintSimilarity = fingerprintSimilarity;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getMatchSeconds() {
		return matchSeconds;
	}

	public void setMatchSeconds(BigDecimal matchSeconds) {
		this.matchSeconds = matchSeconds;
	}

	public String getJingleName() {
		return this.jingle.getName();
	}

	public BigDecimal getJingleDuration() {
		return new BigDecimal(this.getJingle().getDuration());
	}

	public Jingle getJingle() {
		return jingle;
	}

	public void setJingle(Jingle jingle) {
		this.jingle = jingle;
	}

}
