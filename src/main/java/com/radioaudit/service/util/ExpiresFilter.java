package com.radioaudit.service.util;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.time.DateUtils;

public class ExpiresFilter implements Filter {

	// private static final Logger LOGGER =
	// LoggerFactory.getLogger(ExpiresFilter.class);

	// private boolean applyFilter = false;

	private static final String HEADER_DATE_NAME = "Date";
	private static final String HEADER_EXPIRES_NAME = "Expires";
	private static final String HEADER_CACHE_CONTROL_NAME = "Cache-Control";
	private static final String HEADER_CACHE_CONTROL_VALUE = "public, max-age=12960000";// 5
																						// months

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		HttpServletResponse hTTPResponse = HttpServletResponse.class
				.cast(response);

		Date now = new Date();
		hTTPResponse.addDateHeader(HEADER_DATE_NAME, now.getTime());
		hTTPResponse.addDateHeader(HEADER_EXPIRES_NAME,
				DateUtils.addMonths(now, 5).getTime());
		hTTPResponse.addHeader(HEADER_CACHE_CONTROL_NAME,
				HEADER_CACHE_CONTROL_VALUE);

		chain.doFilter(request, response);

	}

	@Override
	public void destroy() {
	}

}
