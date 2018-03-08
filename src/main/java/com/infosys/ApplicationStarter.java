package com.infosys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * Main spring boot class
 *
 * @author Rohit
 */
@SpringBootApplication
@ComponentScan(basePackages="com.infosys")
@PropertySource("classpath:application.properties")
@PropertySource("classpath:error_messages.properties")
public class ApplicationStarter extends SpringBootServletInitializer {

	/**
	 * This method is used when configuring Spring boot in external container like Tomcat.
	 *
	 * @param application - An instance of SpringApplicationBuilder.
	 */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApplicationStarter.class);
    }

    /**
     * Main method.
     *
     * @param args
     */
	public static void main(String[] args) {
		SpringApplication.run(ApplicationStarter.class, args);
	}
}