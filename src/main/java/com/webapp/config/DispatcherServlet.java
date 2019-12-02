package com.webapp.config;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * This class is a part of Software-Testing lab02 timeline.
 * 
 * <p>
 * This is the front controller class for SpringMVC.
 * 
 * @author Juntao Peng
 */
public class DispatcherServlet extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { MvcConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

	@Override
	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		//上传，文件大小为2M，整个请求不超过4M，而且所有文件都要写入磁盘
		registration.setMultipartConfig(new MultipartConfigElement("/",2097152,4194304,0));
	}

}
