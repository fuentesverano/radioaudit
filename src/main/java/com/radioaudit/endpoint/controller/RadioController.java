package com.radioaudit.endpoint.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.radioaudit.domain.to.RadioDTO;
import com.radioaudit.domain.to.RadioLogDTO;
import com.radioaudit.domain.to.UpdateRadioDTO;
import com.radioaudit.service.JingleBean;
import com.radioaudit.service.RadioBean;

@Controller
public class RadioController {

	private static final String USER_NAME = "ffuentes";
	private static final String RADIO_MODEL = "radioModel";
	private static final String VIEW_RADIO_INFO = "radioInfo";

	private static final String RADIO_LOG_MODEL = "radioLogModel";
	private static final String VIEW_RADIO_LOG = "radioLog";

	@Autowired
	private RadioBean radioBean;

	@Autowired
	private JingleBean commercialBean;

	@RequestMapping(value = "/showRadioInfo", method = RequestMethod.GET)
	public ModelAndView show(@RequestParam(required = true, value = "radioCode") String radioCode,
			@RequestParam(required = false, value = "date") Date date,
			@RequestParam(required = false, value = "toDate") Date toDate) {

		RadioDTO radioDTO = this.radioBean.getRadioInfo(USER_NAME, radioCode, date);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put(RADIO_MODEL, radioDTO);
		return new ModelAndView(VIEW_RADIO_INFO, modelMap);
	}

	@RequestMapping(value = "/showRadioLog", method = RequestMethod.GET)
	public ModelAndView showLog(@RequestParam(required = true, value = "radioCode") String radioCode) {

		RadioLogDTO radioLogDTO = this.radioBean.getRadioLog(USER_NAME, radioCode);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put(RADIO_LOG_MODEL, radioLogDTO);
		return new ModelAndView(VIEW_RADIO_LOG, modelMap);
	}

	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public ModelAndView upload(@RequestParam(required = false, value = "radioCode") String radioCode,
			HttpServletRequest request) throws IOException, UnsupportedAudioFileException {

		// Check that we have a file upload request
		if (ServletFileUpload.isMultipartContent(request)) {

			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);

			try {
				FileItem fileIitem = upload.parseRequest(request).iterator().next();
				this.commercialBean.createCommercial(USER_NAME, radioCode, fileIitem);
			} catch (Exception e) {
				// LOGGER
			}
		}
		RadioDTO radioDTO = this.radioBean.getRadioInfo(USER_NAME, radioCode, null);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put(RADIO_MODEL, radioDTO);
		return new ModelAndView(VIEW_RADIO_INFO, modelMap);
	}

	@RequestMapping(value = "/activateRadio")
	public ModelAndView activate(@RequestParam(required = true, value = "radioCode") String radioCode) {

		boolean activate = this.radioBean.activateRadio(radioCode);

		RadioDTO radioDTO = this.radioBean.getRadioInfo(USER_NAME, radioCode, null);
		radioDTO.setActive(activate);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put(RADIO_MODEL, radioDTO);
		return new ModelAndView(VIEW_RADIO_INFO, modelMap);
	}

	@RequestMapping(value = "/playRadio")
	public ModelAndView play(@RequestParam(required = true, value = "radioCode") String radioCode) {
		RadioDTO radioDTO = this.radioBean.getRadioInfo(USER_NAME, radioCode, null);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put(RADIO_MODEL, radioDTO);
		return new ModelAndView(VIEW_RADIO_INFO, modelMap);
	}

	@RequestMapping(value = "/desactivateRadio")
	public ModelAndView desactivate(@RequestParam(required = true, value = "radioCode") String radioCode) {

		boolean desactivate = this.radioBean.desactivateRadio(radioCode);

		RadioDTO radioDTO = this.radioBean.getRadioInfo(USER_NAME, radioCode, null);
		radioDTO.setActive(!desactivate);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put(RADIO_MODEL, radioDTO);
		return new ModelAndView(VIEW_RADIO_INFO, modelMap);
	}

	@RequestMapping(value = "/stopRadio")
	public ModelAndView stop(@RequestParam(required = true, value = "radioCode") String radioCode) {
		boolean activate = this.radioBean.stopRadio(radioCode);

		RadioDTO radioDTO = this.radioBean.getRadioInfo(USER_NAME, radioCode, null);
		radioDTO.setActive(activate);
		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put(RADIO_MODEL, radioDTO);
		return new ModelAndView(VIEW_RADIO_INFO, modelMap);
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/updateRadioCommercials", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateRadioCommercials(@RequestBody(required = true) UpdateRadioDTO updateRadioDTO) {

		this.radioBean.updateRadioCommercials(updateRadioDTO, USER_NAME);
	}

}
