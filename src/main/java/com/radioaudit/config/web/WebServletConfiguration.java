package com.radioaudit.config.web;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@ComponentScan({ "com.radioaudit.endpoint.controller" })
public class WebServletConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/views/", ".jsp");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/home").setViewName("home");
		registry.addViewController("/").setViewName("login");
		registry.addViewController("/hello").setViewName("hello");
		registry.addViewController("/login").setViewName("login");
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> httpMessageConverters) {
		httpMessageConverters.add(this.getMappingJackson2HttpMessageConverter());
	}

	@Bean(name = "jsonConverter")
	public MappingJackson2HttpMessageConverter getMappingJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		// objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
		// objectMapper.setSerializationInclusion(Include.NON_NULL);
		mappingJackson2HttpMessageConverter.setObjectMapper(objectMapper);
		return mappingJackson2HttpMessageConverter;
	}

}
