package com.spring.boot.service.web.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication (scanBasePackages = {"com.spring.boot"})
@EnableDiscoveryClient
@ConfigurationPropertiesScan
public class SpringBootServiceApplication
{

    /**
     * The main method.
     *
     * @param args the arguments used to instantiate the main application
     */
    public static void main( String[] args )
    {
        SpringApplication.run(SpringBootServiceApplication.class, args);
    }

}
