package com.radioaudit.domain.to;

import java.util.Date;

public class JingleResultDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private int frecuency;
	private Date firstCoincidenceDate;
	private Date lastCoincidenceDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getFrecuency() {
		return frecuency;
	}

	public void setFrecuency(int frecuency) {
		this.frecuency = frecuency;
	}

	public Date getFirstCoincidenceDate() {
		return firstCoincidenceDate;
	}

	public void setFirstCoincidenceDate(Date firstCoincidenceDate) {
		this.firstCoincidenceDate = firstCoincidenceDate;
	}

	public Date getLastCoincidenceDate() {
		return lastCoincidenceDate;
	}

	public void setLastCoincidenceDate(Date lastCoincidenceDate) {
		this.lastCoincidenceDate = lastCoincidenceDate;
	}

}
