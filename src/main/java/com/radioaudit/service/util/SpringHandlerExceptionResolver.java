package com.radioaudit.service.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

/**
 * @author ffuentes
 * 
 */
@Component
public class SpringHandlerExceptionResolver
    extends SimpleMappingExceptionResolver {

    // Used for handling HTTP error codes
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringHandlerExceptionResolver.class);

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
        Exception ex) {
        LOGGER.error(ex.getMessage(), ex);

        return super.doResolveException(request, response, handler, ex);
    }
}
