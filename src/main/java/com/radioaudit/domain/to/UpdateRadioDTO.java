package com.radioaudit.domain.to;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect
public class UpdateRadioDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotNull
	private String radioCode;
	private String[] userCommercials;
	private String[] radioCommercials;

	public String getRadioCode() {
		return radioCode;
	}

	public void setRadioCode(String radioCode) {
		this.radioCode = radioCode;
	}

	public String[] getUserCommercials() {
		return userCommercials;
	}

	public void setUserCommercials(String[] userCommercials) {
		this.userCommercials = userCommercials;
	}

	public String[] getRadioCommercials() {
		return radioCommercials;
	}

	public void setRadioCommercials(String[] radioCommercials) {
		this.radioCommercials = radioCommercials;
	}

}
