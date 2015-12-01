package com.radioaudit.domain.thread;

import com.radioaudit.domain.dao.RadioDAO;
import com.radioaudit.domain.model.Radio;

public class AbstractThread extends Thread {

	protected String radioCode;
	protected RadioDAO radioDAO;

	protected boolean isActive() {
		return this.radioDAO.isActive(radioCode);
	}

	protected void disconnect() {
		Radio radio = this.radioDAO.loadByCode(radioCode);
		radio.setConnect(false);
		this.radioDAO.update(radio);
	}

}
