package com.radioaudit.service.job;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.radioaudit.domain.dao.RadioDAO;
import com.radioaudit.domain.model.Radio;
import com.radioaudit.service.RadioBean;

@Service
public class CheckConnectionJob {

	private final Logger LOGGER = LoggerFactory.getLogger(CheckConnectionJob.class);

	@Autowired
	private RadioBean radioBean;

	@Autowired
	private RadioDAO radioDAO;

	@PostConstruct
	private void init() {
		List<Radio> radios = this.radioDAO.findAll();
		for (Radio radio : radios) {
			radio.setConnect(false);
			this.radioDAO.update(radio);
		}
	}

	@Scheduled(initialDelay = 20000, fixedDelay = 10000)
	public void scheduledStart() {

		List<Radio> radios = this.radioDAO.findActiveButNotConnect();
		if (CollectionUtils.isNotEmpty(radios)) {
			this.LOGGER.info("Has {} radios to set up connection", radios.size());
			for (Radio radio : radios) {
				try {
					this.LOGGER.info("Trying to start connection for {}", radio.getName());
					boolean success = this.radioBean.activateRadio(radio.getCode());
					if (success) {
						this.LOGGER.info("{} is now connecting..", radio.getName());
						radio.setConnect(true);
						this.radioDAO.update(radio);
					} else {
						this.LOGGER.error("Error starting connection with radio : {}", radio.getName());
					}
				} catch (Exception e) {
					this.LOGGER.error("Error trying to conect with radio: {}", radio.getName(), e);
				}
			}
		}
	}

}
