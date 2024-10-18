package com.test1017.init;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.test1017.config.Appconfig;
import com.test1017.config.Webconfig;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;

public class MyWebApplicationInitializer implements WebApplicationInitializer{

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// TODO Auto-generated method stub
		AnnotationConfigWebApplicationContext rootContext= new AnnotationConfigWebApplicationContext();
		rootContext.register(Appconfig.class);
		rootContext.refresh();
		servletContext.addListener(new ContextLoaderListener(rootContext));
		
		AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
		webContext.register(Webconfig.class);
		
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("dispatcher", new DispatcherServlet(webContext));

		if (dispatcher != null) 
		{
				dispatcher.setLoadOnStartup(1);
	            dispatcher.addMapping("/");
	    } 
		else 
		{
	            throw new ServletException("Failed to register DispatcherServlet.");
	    }

		
		
	}

}
