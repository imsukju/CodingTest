package com.test1017.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.test1017.init","com.test1017.dao","com.test1017.service"})
public class Appconfig {
	@Bean EntityManagerFactory entityfactory()
	{
		return Persistence.createEntityManagerFactory("jpabook");
		
	}
	
	
	 @Bean
	    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
	        JpaTransactionManager transactionManager = new JpaTransactionManager();
	        transactionManager.setEntityManagerFactory(emf);
	        return transactionManager;
	    }
}
