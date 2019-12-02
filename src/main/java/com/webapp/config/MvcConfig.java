package com.webapp.config;

import com.webapp.service.database.DatabaseService;
import com.webapp.service.database.dao.UserDao;
import com.webapp.service.database.dao.impl.UserDaoImpl;
import com.webapp.service.filesystem.FaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.webapp.service.filesystem.Fao;

/**
 * This class is a part of Software-Testing lab02 timeline.
 * 
 * <p>
 * This is the configuration class for SpringMVC Dispatcher, which serves as
 * web.xml in normal Java Web Apps.
 * 
 * @author Juntao Peng
 */
@EnableWebMvc
@Configuration
@ComponentScan({"com.webapp"})
public class MvcConfig {

	@Bean
	public InternalResourceViewResolver viewResolver() {
		InternalResourceViewResolver vr = new InternalResourceViewResolver();
		vr.setPrefix("/WEB-INF/jsp/");
		vr.setSuffix(".jsp");
		return vr;
	}

	@Bean
	public StandardServletMultipartResolver getStandardServletMultipartResolver() {
		return new StandardServletMultipartResolver();
	}

	@Bean(name = "fao")
	public Fao getFao() {
		return new FaoImpl();
	}

	@Bean(name = "userDao")
	public UserDao getUserDao() {
		return new UserDaoImpl();
	}


}
