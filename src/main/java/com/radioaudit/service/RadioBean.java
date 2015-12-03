package com.radioaudit.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.musicg.wave.Wave;
import com.radioaudit.domain.dao.CoincidenceDAO;
import com.radioaudit.domain.dao.JingleDAO;
import com.radioaudit.domain.dao.RadioDAO;
import com.radioaudit.domain.dao.UserDAO;
import com.radioaudit.domain.model.Coincidence;
import com.radioaudit.domain.model.Jingle;
import com.radioaudit.domain.model.Radio;
import com.radioaudit.domain.model.constant.RadioFormat;
import com.radioaudit.domain.thread.AacDecorder;
import com.radioaudit.domain.thread.FingerprintComputer;
import com.radioaudit.domain.thread.MpegDecorder;
import com.radioaudit.domain.thread.WriteCoincidence;
import com.radioaudit.domain.to.CoincidenceDTO;
import com.radioaudit.domain.to.FrontRadioDTO;
import com.radioaudit.domain.to.JingleTO;
import com.radioaudit.domain.to.JingleResultDTO;
import com.radioaudit.domain.to.PartialCoincidenceTO;
import com.radioaudit.domain.to.RadioTO;
import com.radioaudit.domain.to.RadioLogDTO;
import com.radioaudit.domain.to.UpdateRadioDTO;

@Service
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
public class RadioBean {

	private final Logger LOGGER = LoggerFactory.getLogger(RadioBean.class);

	private static Map<Integer, Float> bitrateMap;

	static {
		bitrateMap = new HashMap<Integer, Float>();
		bitrateMap.put(Integer.valueOf(64), Float.class.cast(0.30f));
		bitrateMap.put(Integer.valueOf(32), Float.class.cast(0.30f));
		bitrateMap.put(Integer.valueOf(24), Float.class.cast(0.10f));
	}

	@Autowired
	private LogCoincidenceBean coincidenceBean;

	@Autowired
	private RadioDAO radioDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private JingleDAO jingleDAO;

	@Autowired
	private CoincidenceDAO coincidenceDAO;

	/**
	 * Return all info for radio and user
	 * 
	 * @param username
	 * @param radioCode
	 * @param date
	 * @return RadioDTO
	 */
	public RadioTO getRadioInfo(String username, String radioCode, Date date) {

		List<Jingle> userCommercials = this.jingleDAO.findByUsername(username);
		Radio radio = this.radioDAO.loadByCodeWithSuscribeCommercials(radioCode, username);
		userCommercials.removeAll(radio.getSuscribeJingles());

		if (date == null) {
			date = new Date();
		}
		Date toDate = DateUtils.ceiling(date, Calendar.DAY_OF_MONTH);
		Date fromDate = DateUtils.addDays(toDate, -1);

		List<Coincidence> coincidences = this.coincidenceDAO.findByRadioAndUsername(radioCode, username, fromDate, toDate);

		return this.mapRadioDTO(radio, userCommercials, radio.getSuscribeJingles(), coincidences);
	}

	/**
	 * Update radio commercials
	 * 
	 * @param updateRadioDTO
	 * @param username
	 */
	@Transactional(readOnly = false)
	public void updateRadioCommercials(UpdateRadioDTO updateRadioDTO, String username) {

		List<String> radioCommercials = new LinkedList<String>();
		CollectionUtils.addAll(radioCommercials, updateRadioDTO.getRadioCommercials());

		List<String> userCommercialsNames = new LinkedList<String>();
		CollectionUtils.addAll(userCommercialsNames, updateRadioDTO.getUserCommercials());

		Radio radio = this.radioDAO.loadByCodeWithSuscribeCommercials(updateRadioDTO.getRadioCode(), username);

		Set<Jingle> suscribeCommercialsTemp = new HashSet<Jingle>(radio.getSuscribeJingles());

		for (Jingle commercial : suscribeCommercialsTemp) {
			if (userCommercialsNames.contains(commercial.getName())) {
				radio.getSuscribeJingles().remove(commercial);
				this.LOGGER.info("Remove commercial with name: {} from radio with code: {}", commercial.getName(),
						radio.getCode());
			}
			if (radioCommercials.contains(commercial.getName())) {
				radioCommercials.remove(commercial.getName());
			}
		}

		for (String radioCommercialName : radioCommercials) {
			Jingle commercial = this.jingleDAO.loadByName(radioCommercialName);
			this.LOGGER.info("Add commercial with name: {} to radio with code: {}", commercial.getName(), radio.getCode());
			radio.getSuscribeJingles().add(commercial);
		}

		this.radioDAO.update(radio);
	}

	/**
	 * Return all radios
	 * 
	 * @return List<RadioDTO>
	 */
	public List<RadioTO> getRadios() {

		List<RadioTO> radioDTOs = new LinkedList<RadioTO>();

		List<Radio> radios = this.radioDAO.findAll();
		for (Radio radio : radios) {
			RadioTO radioDTO = this.mapRadioDTO(radio);
			radioDTOs.add(radioDTO);
		}
		return radioDTOs;
	}

	/**
	 * Stop to reproduce radio streaming
	 * 
	 * @param radioCode
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean stopRadio(String radioCode) {
		Radio radio = this.radioDAO.loadByCode(radioCode);
		radio.setPlay(false);
		this.radioDAO.update(radio);
		return true;
	}

	/**
	 * Reproduce radio streaming
	 * 
	 * @param radioCode
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean playRadio(String radioCode) {
		Radio radio = this.radioDAO.loadByCode(radioCode);
		radio.setPlay(true);
		this.radioDAO.update(radio);
		return true;
	}

	/**
	 * Deactivate radio, this method finish radio streaming
	 * 
	 * @param radioCode
	 * @return boolean
	 */
	@Transactional(readOnly = false)
	public boolean desactivateRadio(String radioCode) {
		Radio radio = this.radioDAO.loadByCode(radioCode);
		radio.setActive(false);
		radio.setConnect(false);
		this.radioDAO.update(radio);
		return true;
	}

	/**
	 * Activate radio if not are activate and connect
	 * 
	 * @param radioCode
	 * @return
	 */
	@Transactional(readOnly = false)
	public boolean activateRadio(String radioCode) {

		Radio radio = this.radioDAO.loadByCodeWithSuscribeCommercials(radioCode);
		// validate radio state
		if (CollectionUtils.isEmpty(radio.getSuscribeJingles())) {
			return true;
		}

		// start connection
		return this.startConnection(radio);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private boolean startConnection(Radio radio) {
		try {

			// create thread safe buffers
			BlockingQueue<Wave> waveBuffer = new ArrayBlockingQueue(2, true);
			BlockingQueue<List<PartialCoincidenceTO>> coincidenceBuffer = new ArrayBlockingQueue(5, true);

			Float minFingerprintSimilarity = bitrateMap.get(Integer.valueOf(radio.getBitrate()));

			// create thread FingerprintComputer
			FingerprintComputer fingerprintComputer = new FingerprintComputer(radio.getCode(), minFingerprintSimilarity,
					this.radioDAO, waveBuffer, coincidenceBuffer);
			fingerprintComputer.setName("FingerprintComputer-" + radio.getCode());

			// create thread WriteCoincidence
			WriteCoincidence writeCoincidence = new WriteCoincidence(radio.getCode(), this.radioDAO, this.coincidenceBean,
					coincidenceBuffer);
			writeCoincidence.setName("WriteCoincidence-" + radio.getCode());

			if (RadioFormat.MPEG.equals(radio.getFormat())) {
				// use MP3 decoder
				MpegDecorder mpegDecorder = new MpegDecorder(radio.getCode(), this.radioDAO, radio.getUrl(), waveBuffer);
				mpegDecorder.setName("MpegRrecorder-" + radio.getCode());
				mpegDecorder.start();
			} else {
				// use AAC decoder
				AacDecorder aacDecorder = new AacDecorder(radio.getCode(), this.radioDAO, radio.getUrl(), waveBuffer);
				aacDecorder.setName("AccRecorder-" + radio.getCode());
				aacDecorder.start();
			}
			fingerprintComputer.start();
			writeCoincidence.start();
		} catch (Exception exception) {
			this.LOGGER.error("Error trying to activate {}", radio.getName(), exception);
			radio.setConnect(false);
			this.radioDAO.save(radio);
			return false;
		}
		return true;
	}

	public List<FrontRadioDTO> findRadios() {
		List<Radio> radios = this.radioDAO.findAll();
		return this.mapFrontRadioDTO(radios);
	}

	private List<FrontRadioDTO> mapFrontRadioDTO(List<Radio> radios) {
		List<FrontRadioDTO> frontRadioDTOs = new LinkedList<FrontRadioDTO>();
		for (Radio radio : radios) {
			FrontRadioDTO frontRadioDTO = new FrontRadioDTO();
			frontRadioDTO.setCode(radio.getCode());
			frontRadioDTO.setName(radio.getName());
			frontRadioDTOs.add(frontRadioDTO);
		}
		return frontRadioDTOs;
	}

	private RadioTO mapRadioDTO(Radio radio) {

		RadioTO radioDTO = new RadioTO();
		radioDTO.setName(radio.getName());
		radioDTO.setUrl(radio.getUrl());
		radioDTO.setWebsite(radio.getWebsite());
		// radioDTO.setFormat(radio.getFormat());
		radioDTO.setBitrate(radio.getBitrate());

		return radioDTO;
	}

	private RadioTO mapRadioDTO(Radio radio, Collection<Jingle> deactivedJingles, Collection<Jingle> activeJingles,
			Collection<Coincidence> coincidences) {

		RadioTO radioDTO = new RadioTO();
		radioDTO.setCode(radio.getCode());
		radioDTO.setName(radio.getName());
		radioDTO.setUrl(radio.getUrl());
		radioDTO.setWebsite(radio.getWebsite());
		// radioDTO.setFormat(radio.getFormat());
		radioDTO.setBitrate(radio.getBitrate());
		radioDTO.setActive(radio.isActive() && radio.isConnect());

		List<JingleTO> deactivedJinglesDTOs = new LinkedList<JingleTO>();
		for (Jingle jingle : deactivedJingles) {
			deactivedJinglesDTOs.add(this.mapJingleDTO(jingle));
		}
		radioDTO.setDeactivedJingles(deactivedJinglesDTOs);

		List<JingleTO> activeJinglesDTOs = new LinkedList<JingleTO>();
		for (Jingle jingle : activeJingles) {
			activeJinglesDTOs.add(this.mapJingleDTO(jingle));
		}
		radioDTO.setActiveJingles(activeJinglesDTOs);

		Map<String, JingleResultDTO> map = new HashMap<String, JingleResultDTO>();

		for (Coincidence coincidence : coincidences) {
			if (map.containsKey(coincidence.getJingle().getName())) {
				JingleResultDTO jingleResultDTO = map.get(coincidence.getJingle().getName());
				int frecuency = jingleResultDTO.getFrecuency() + 1;
				jingleResultDTO.setFrecuency(frecuency);

				Date firstCoincidenceDate = jingleResultDTO.getFirstCoincidenceDate();
				if (firstCoincidenceDate.after(coincidence.getTimestamp())) {
					jingleResultDTO.setFirstCoincidenceDate(coincidence.getTimestamp());
				}
				Date lastCoincidenceDate = jingleResultDTO.getLastCoincidenceDate();
				if (lastCoincidenceDate.before(coincidence.getTimestamp())) {
					jingleResultDTO.setLastCoincidenceDate(coincidence.getTimestamp());
				}
				map.put(coincidence.getJingle().getName(), jingleResultDTO);
			} else {
				JingleResultDTO jingleResultDTO = new JingleResultDTO();
				jingleResultDTO.setName(coincidence.getJingle().getName());
				jingleResultDTO.setFrecuency(1);
				jingleResultDTO.setFirstCoincidenceDate(coincidence.getTimestamp());
				jingleResultDTO.setLastCoincidenceDate(coincidence.getTimestamp());
				map.put(coincidence.getJingle().getName(), jingleResultDTO);
			}
		}
		radioDTO.setJingleResults(map.values());

		return radioDTO;
	}

	private JingleTO mapJingleDTO(Jingle jingle) {
		JingleTO jingleDTO = new JingleTO();
		jingleDTO.setTitle(jingle.getName());
		jingleDTO.setFormat(jingle.getFormat());
		jingleDTO.setDuration(jingle.getDuration());
		jingleDTO.setCreationDate(jingle.getCreationDate());
		return jingleDTO;
	}

	public RadioLogDTO getRadioLog(String username, String radioCode) {

		Radio radio = this.radioDAO.loadByCodeWithSuscribeCommercials(radioCode, username);

		List<Coincidence> logCoincidences = this.coincidenceDAO.findByRadioAndUsername(radioCode, username);

		return this.mapRadioLogDTO(radio, logCoincidences);
	}

	private RadioLogDTO mapRadioLogDTO(Radio radio, List<Coincidence> logCoincidences) {
		RadioLogDTO radioLogDTO = new RadioLogDTO();
		radioLogDTO.setCode(radio.getCode());
		radioLogDTO.setName(radio.getName());
		radioLogDTO.setUrl(radio.getUrl());
		radioLogDTO.setWebsite(radio.getWebsite());
		// radioLogDTO.setFormat(radio.getFormat());
		radioLogDTO.setBitrate(radio.getBitrate());
		radioLogDTO.setActive(radio.isActive() && radio.isConnect());

		List<CoincidenceDTO> coincidences = new LinkedList<CoincidenceDTO>();
		for (Coincidence logCoincidence : logCoincidences) {
			CoincidenceDTO coincidenceDTO = new CoincidenceDTO();
			coincidenceDTO.setName(logCoincidence.getJingle().getName());
			coincidenceDTO.setDate(logCoincidence.getTimestamp());
			coincidenceDTO.setMatchPercent(logCoincidence.getMatchPercent());
			coincidenceDTO.setFingerprintSimilarity(logCoincidence.getFingerprintSimilarity());

			coincidences.add(coincidenceDTO);
		}
		radioLogDTO.setCoincidences(coincidences);
		return radioLogDTO;
	}

	public List<FrontRadioDTO> findActiveRadiosByUser(String username) {
		List<Radio> userRadio = this.radioDAO.findByUser(username);
		return this.mapFrontRadioDTO(userRadio);
	}

	public List<FrontRadioDTO> findDeactivatedRadiosByUser(String username) {
		List<Radio> userRadio = this.radioDAO.findByNotUser(username);
		return this.mapFrontRadioDTO(userRadio);
	}

}
