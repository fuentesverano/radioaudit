package com.radioaudit.endpoint;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.radioaudit.domain.to.RadioTO;
import com.radioaudit.domain.to.UpdateRadioDTO;
import com.radioaudit.service.JingleBean;
import com.radioaudit.service.RadioBean;

@RestController
@RequestMapping(value = "/radios")
public class RadioController {

	@Autowired
	private RadioBean radioBean;

	@Autowired
	private JingleBean commercialBean;

	@RequestMapping(method = RequestMethod.GET)
	public List<RadioTO> getRadios(@RequestParam(required = false, value = "active") Boolean active,
			@RequestParam(required = false, value = "user") String user,
			@RequestParam(required = false, value = "date") Date date,
			@RequestParam(required = false, value = "toDate") Date toDate) {
		return this.radioBean.getRadios();
	}

	@RequestMapping(value = "/{radioCode}", method = RequestMethod.GET)
	public RadioTO getRadio(@PathVariable String radioCode) {
		return radioBean.getRadioInfo("", radioCode, new Date());
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = "/updateRadioCommercials", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public void updateRadioCommercials(@RequestBody(required = true) UpdateRadioDTO updateRadioDTO) {

		this.radioBean.updateRadioCommercials(updateRadioDTO, "USER_NAME");
	}

}
