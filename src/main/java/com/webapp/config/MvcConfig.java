package com.webapp.config;

import com.webapp.service.database.dao.*;
import com.webapp.service.database.dao.impl.*;
import com.webapp.service.filesystem.FaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
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
@PropertySource("classpath:application.properties")
public class MvcConfig implements WebMvcConfigurer {

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

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Bean(name = "fao")
    public Fao getFao() {
        return new FaoImpl();
    }

    @Bean(name = "userDao")
    public UserDao getUserDao() {
        return new UserDaoImpl();
    }

    @Bean(name = "loginDao")
    public LoginDao getLoginDao() {
        return new LoginDaoImpl();
    }

    @Bean(name = "newsDao")
    public NewsDao getNewsDao() {
        return new NewsDaoImpl();
    }

    @Bean(name = "recordDao")
    public RecordDao getRecordDao() {
        return new RecordDaoImpl();
    }

    @Bean(name = "buildingDao")
    public BuildingDao getBuildingDao() {
        return new BuildingDaoImpl();
    }

    @Bean(name = "commentDao")
    public  CommentDao getCommentDao() {
        return new CommentDaoImpl();
    }


}
