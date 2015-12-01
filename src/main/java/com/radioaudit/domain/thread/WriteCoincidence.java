package com.radioaudit.domain.thread;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.radioaudit.domain.dao.RadioDAO;
import com.radioaudit.domain.to.CoincidenceTO;
import com.radioaudit.domain.to.PartialCoincidenceTO;
import com.radioaudit.service.LogCoincidenceBean;
import com.radioaudit.service.util.MathUtils;

public class WriteCoincidence extends AbstractThread {

	private final Logger LOGGER = LoggerFactory.getLogger(WriteCoincidence.class);

	private final float MIN_MATCH_PERCENT = 0.70f;

	private LogCoincidenceBean coincidenceBean;
	private BlockingQueue<List<PartialCoincidenceTO>> coincidenceBuffer;
	private Map<String, CoincidenceTO> coincidenceMap;

	/**
	 * Public Constructor
	 * 
	 * @param radioCode
	 * @param radioDAO
	 * @param logCoincidenceBean
	 * @param coincidenceBuffer
	 */
	public WriteCoincidence(String radioCode, RadioDAO radioDAO, LogCoincidenceBean coincidenceBean,
			BlockingQueue<List<PartialCoincidenceTO>> coincidenceBuffer) {
		this.radioCode = radioCode;
		this.radioDAO = radioDAO;
		this.coincidenceBean = coincidenceBean;
		this.coincidenceBuffer = coincidenceBuffer;
		this.coincidenceMap = new HashMap<String, CoincidenceTO>();
	}

	@Override
	public void run() {

		boolean active = true;

		do {
			try {
				Thread.sleep(100);

				while (active) {

					// take coincidences from buffer
					List<PartialCoincidenceTO> partialCoincidences = this.coincidenceBuffer.poll(30, TimeUnit.SECONDS);

					// not has coincidences
					if (CollectionUtils.isEmpty(partialCoincidences) && MapUtils.isEmpty(this.coincidenceMap)) {
						active = super.isActive();
						Thread.sleep(100);
						continue;
					}

					if (MapUtils.isNotEmpty(this.coincidenceMap)) {
						// consume jingle segment
						for (CoincidenceTO coincidenceTO : this.coincidenceMap.values()) {
							LOGGER.info("Jingle: {} has {} segments to match", coincidenceTO.getJingleName(),
									coincidenceTO.getJingleSegments());
							coincidenceTO.consumeJingleSegment();
						}
					}

					LOGGER.info("Has {} coincidences to process...", partialCoincidences.size());

					for (PartialCoincidenceTO partialCoincidenceTO : partialCoincidences) {

						// check if coincidence is into current coincidences map
						boolean hasPreviousCoincidence = this.coincidenceMap.containsKey(partialCoincidenceTO
								.getJingleName());

						if (hasPreviousCoincidence) {

							// take previous coincidence
							CoincidenceTO previousCoincidenceTO = this.coincidenceMap.get(partialCoincidenceTO
									.getJingleName());

							// add match seconds to previous coincidence
							BigDecimal totalSeconds = MathUtils.sum(previousCoincidenceTO.getTotalSeconds(),
									partialCoincidenceTO.getMatchSeconds());
							previousCoincidenceTO.setTotalSeconds(totalSeconds);

							// put updated coincidence
							this.coincidenceMap.put(previousCoincidenceTO.getJingleName(), previousCoincidenceTO);

							this.LOGGER.info("Update coincidence with name {} total match seconds {}",
									partialCoincidenceTO.getJingleName(), totalSeconds);

						} else {

							CoincidenceTO coincidenceTO = new CoincidenceTO(partialCoincidenceTO);

							// add first coincidence into current coincidences
							// map
							this.coincidenceMap.put(partialCoincidenceTO.getJingleName(), coincidenceTO);

							this.LOGGER.info("Save new coincidence with name {}", partialCoincidenceTO.getJingleName());
						}
					}

					if (MapUtils.isNotEmpty(this.coincidenceMap)) {
						// process finalized coincidences
						Iterator<CoincidenceTO> iterator = this.coincidenceMap.values().iterator();
						while (iterator.hasNext()) {
							CoincidenceTO coincidenceTO = iterator.next();
							if (coincidenceTO.getJingleSegments() == 0) {
								this.processCoincidence(coincidenceTO);
								// remove processed coincidence
								iterator.remove();
							}
						}

					}
				}

				active = super.isActive();

			} catch (InterruptedException e1) {
				LOGGER.warn("InterruptedException for radio {}", radioCode);
			} catch (Exception exception) {
				LOGGER.error("Unexpected exception", exception);
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

	private void processCoincidence(CoincidenceTO coincidenceTO) {
		BigDecimal matchPercent = MathUtils.divide(coincidenceTO.getTotalSeconds(), coincidenceTO.getCommercialDuration());
		if (matchPercent.floatValue() >= MIN_MATCH_PERCENT) {
			LOGGER.info("Found jingle: {} with total match percent {}", coincidenceTO.getJingleName(), matchPercent);
			this.coincidenceBean.saveCoincidence(matchPercent, this.radioCode, coincidenceTO);
		} else {
			LOGGER.info("Coincidence for jingle: {} not has enough match percent seconds {}", coincidenceTO.getJingleName(),
					matchPercent);
		}
	}

}
