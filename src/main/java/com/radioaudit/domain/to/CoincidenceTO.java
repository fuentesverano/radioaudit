package com.radioaudit.domain.to;

import java.math.BigDecimal;
import java.util.Date;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.radioaudit.domain.model.Jingle;

public class CoincidenceTO {

	private Jingle jingle;
	private BigDecimal totalSeconds;
	private FingerprintSimilarity fingerprintSimilarity;
	private Date date = new Date();
	private int jingleSegments;

	/**
	 * Create a new coincidence
	 * 
	 * @param partialCoincidenceTO
	 */
	public CoincidenceTO(PartialCoincidenceTO partialCoincidenceTO) {
		this.jingle = partialCoincidenceTO.getJingle();
		this.setTotalSeconds(partialCoincidenceTO.getMatchSeconds());
		this.fingerprintSimilarity = partialCoincidenceTO.getFingerprintSimilarity();
		this.jingleSegments = this.jingle.getDuration() / 5;
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

	public String getJingleName() {
		return this.jingle.getName();
	}

	public BigDecimal getCommercialDuration() {
		return new BigDecimal(this.jingle.getDuration());
	}

	public BigDecimal getTotalSeconds() {
		return totalSeconds;
	}

	public void setTotalSeconds(BigDecimal totalSeconds) {
		this.totalSeconds = totalSeconds;
	}

	public int getJingleSegments() {
		return jingleSegments;
	}

	public void setJingleSegments(int jingleSegments) {
		this.jingleSegments = jingleSegments;
	}

	public void consumeJingleSegment() {
		this.jingleSegments--;
	}

	public Jingle getJingle() {
		return jingle;
	}

	public void setJingle(Jingle jingle) {
		this.jingle = jingle;
	}

}
