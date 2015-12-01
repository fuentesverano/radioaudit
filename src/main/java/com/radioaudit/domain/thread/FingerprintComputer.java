package com.radioaudit.domain.thread;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.fingerprint.FingerprintSimilarityComputer;
import com.musicg.wave.Wave;
import com.radioaudit.domain.dao.RadioDAO;
import com.radioaudit.domain.model.Jingle;
import com.radioaudit.domain.model.Radio;
import com.radioaudit.domain.to.PartialCoincidenceTO;

public class FingerprintComputer extends AbstractThread {

	private static final BigDecimal MATCH_SECONDS = new BigDecimal(5);

	private final Logger LOGGER = LoggerFactory.getLogger(FingerprintComputer.class);

	private float minFingerprintSimilarity;
	private BlockingQueue<Wave> waveBuffer;
	private BlockingQueue<List<PartialCoincidenceTO>> coincidenceBuffer;

	/**
	 * Public Constructor
	 * 
	 * @param radioCode
	 * @param radioDAO
	 * @param waveBuffer
	 * @param coincidenceBuffer
	 * @param processTimeBuffer
	 */
	public FingerprintComputer(String radioCode, float minFingerprintSimilarity, RadioDAO radioDAO,
			BlockingQueue<Wave> waveBuffer, BlockingQueue<List<PartialCoincidenceTO>> coincidenceBuffer) {
		this.radioCode = radioCode;
		this.minFingerprintSimilarity = minFingerprintSimilarity;
		this.radioDAO = radioDAO;
		this.waveBuffer = waveBuffer;
		this.coincidenceBuffer = coincidenceBuffer;
	}

	@Override
	public void run() {

		boolean active = true;

		do {
			try {
				Thread.sleep(100);

				while (active) {

					// take a current record wave
					Wave recordWave = waveBuffer.poll(10, TimeUnit.SECONDS);

					if (recordWave == null) {
						active = super.isActive();
						Thread.sleep(100);
						continue;
					}

					long startTimeMillis = System.currentTimeMillis();

					// load radio with subscribe jingles
					Radio radio = this.radioDAO.loadByCodeWithSuscribeCommercials(this.radioCode);

					// create empty list of coincidences
					List<PartialCoincidenceTO> coincidences = new LinkedList<PartialCoincidenceTO>();

					// compare record wave with all radio commercials
					for (Jingle commercial : radio.getSuscribeJingles()) {
						FingerprintSimilarity fingerprintSimilarity = new FingerprintSimilarityComputer(
								recordWave.getFingerprint(), commercial.getFingerprint()).getFingerprintsSimilarity();

						if (fingerprintSimilarity.getSimilarity() >= this.minFingerprintSimilarity
								|| fingerprintSimilarity.getScore() >= this.minFingerprintSimilarity) {

							PartialCoincidenceTO partialCoincidenceTO = new PartialCoincidenceTO(commercial, MATCH_SECONDS,
									fingerprintSimilarity);

							// put coincidence
							coincidences.add(partialCoincidenceTO);

							LOGGER.info(MessageFormat.format("Found jingle: {0} with similarity: {1} and score: {2}",
									commercial.getName(), fingerprintSimilarity.getSimilarity(),
									fingerprintSimilarity.getScore()));
						}
					}

					// put coincidence into buffer
					this.coincidenceBuffer.put(coincidences);

					long timeMillis = System.currentTimeMillis() - startTimeMillis;
					this.LOGGER.info("Process {} jingles in {} ms", radio.getSuscribeJingles().size(), timeMillis);

					active = super.isActive();
				}

			} catch (InterruptedException e1) {
				LOGGER.warn("InterruptedException for radio {}", radioCode);
			} catch (Exception exception) {
				LOGGER.error("Unexpected exception", exception);
				active = false;
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}

			active = super.isActive();

		} while (active);

		// mark as disconnected
		super.disconnect();
	}
}
