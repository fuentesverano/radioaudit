package com.radioaudit.endpoint.model;

import java.io.Serializable;
import java.util.List;

import com.radioaudit.domain.to.FrontRadioDTO;

@SuppressWarnings("serial")
public class HomeModel implements Serializable {

	private List<FrontRadioDTO> userRadios;

	public List<FrontRadioDTO> getUserRadios() {
		return userRadios;
	}

	public void setUserRadios(List<FrontRadioDTO> userRadios) {
		this.userRadios = userRadios;
	}

}
