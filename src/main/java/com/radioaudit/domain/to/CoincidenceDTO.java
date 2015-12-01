package com.radioaudit.domain.to;

import java.math.BigDecimal;
import java.util.Date;

public class CoincidenceDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private Date date;
	private BigDecimal matchPercent;
	private BigDecimal fingerprintSimilarity;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getMatchPercent() {
		return matchPercent;
	}

	public void setMatchPercent(BigDecimal matchPercent) {
		this.matchPercent = matchPercent;
	}

	public BigDecimal getFingerprintSimilarity() {
		return fingerprintSimilarity;
	}

	public void setFingerprintSimilarity(BigDecimal fingerprintSimilarity) {
		this.fingerprintSimilarity = fingerprintSimilarity;
	}

}
