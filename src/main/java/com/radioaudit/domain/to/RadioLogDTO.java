package com.radioaudit.domain.to;

import java.util.List;

import com.radioaudit.domain.model.constant.RadioFormat;

public class RadioLogDTO extends AbstractDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String code;
	private String name;
	private String url;
	private String website;
	private RadioFormat format;
	private int bitrate;
	private boolean active;

	private List<CoincidenceDTO> coincidences;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public int getBitrate() {
		return bitrate;
	}

	public void setBitrate(int bitrate) {
		this.bitrate = bitrate;
	}

	public RadioFormat getFormat() {
		return format;
	}

	public void setFormat(RadioFormat format) {
		this.format = format;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<CoincidenceDTO> getCoincidences() {
		return coincidences;
	}

	public void setCoincidences(List<CoincidenceDTO> coincidences) {
		this.coincidences = coincidences;
	}

}
