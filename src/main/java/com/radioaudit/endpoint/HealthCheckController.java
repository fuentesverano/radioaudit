package com.radioaudit.endpoint;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-check")
public class HealthCheckController {

	@Autowired
	private ApplicationContext applicationContext;

	@RequestMapping(method = RequestMethod.GET)
	public Map<String, String> healthCheck(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", "radioaudit");
		// map.put("version",
		// this.getClass().getPackage().getSpecificationVersion());
		map.put("startup_date", DateFormatUtils.ISO_DATETIME_FORMAT.format(applicationContext.getStartupDate()));
		map.put("node", request.getLocalAddr());
		return map;
	}
}