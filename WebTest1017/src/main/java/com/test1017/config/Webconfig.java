package com.test1017.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@ComponentScan(basePackages = {"com.test1017.controller"})
public class Webconfig {
	@Bean
	public InternalResourceViewResolver viewResolver() {
	    InternalResourceViewResolver resolver = new InternalResourceViewResolver();
	    resolver.setPrefix("/WEB-INF/views/"); 
	    resolver.setSuffix(".jsp");  
	    return resolver;
	}
}

