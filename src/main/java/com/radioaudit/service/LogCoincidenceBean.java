package com.radioaudit.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.radioaudit.domain.dao.CoincidenceDAO;
import com.radioaudit.domain.dao.RadioDAO;
import com.radioaudit.domain.dao.UserDAO;
import com.radioaudit.domain.model.Coincidence;
import com.radioaudit.domain.model.Radio;
import com.radioaudit.domain.to.CoincidenceTO;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class LogCoincidenceBean {

	@Autowired
	private CoincidenceDAO coincidenceDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private RadioDAO radioDAO;

	@Transactional(readOnly = false)
	public void saveCoincidence(BigDecimal matchPercent, String radioCode, CoincidenceTO coincidence) {

		Radio radio = this.radioDAO.loadByCode(radioCode);

		Coincidence logCoincidence = new Coincidence();
		logCoincidence.setJingle(coincidence.getJingle());
		logCoincidence.setRadio(radio);
		logCoincidence.setTimestamp(coincidence.getDate());
		MathContext mc = new MathContext(2);
		logCoincidence.setMatchPercent(matchPercent.round(mc));

		BigDecimal fingerprintSimilarity = new BigDecimal(coincidence.getFingerprintSimilarity().getSimilarity());
		logCoincidence.setFingerprintSimilarity(fingerprintSimilarity.round(mc));

		// ////////////////////////////////////////////////////////////////////////
		this.printCoincidence(radio.getName(), matchPercent, coincidence);
		// /////////////////////////////////////////////////////////////////////////
		this.coincidenceDAO.save(logCoincidence);
	}

	private void printCoincidence(String radioName, BigDecimal matchPercent, CoincidenceTO coincidence) {

		FingerprintSimilarity fingerprintSimilarity = coincidence.getFingerprintSimilarity();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm:ss a");
		System.out.println(" ");
		System.out.println("|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|");
		System.out.println("Found total coincidence for radio: " + radioName);
		System.out.println("Match percent: " + matchPercent);
		System.out.println("Commercial Name: " + coincidence.getJingleName());
		System.out.println("Similarity: " + fingerprintSimilarity.getSimilarity());
		System.out.println("Score: " + fingerprintSimilarity.getScore());
		System.out.println("Found at: " + simpleDateFormat.format(coincidence.getDate()));
		System.out.println("|+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++|");
	}

}
