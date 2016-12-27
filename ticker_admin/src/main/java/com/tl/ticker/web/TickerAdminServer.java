package com.tl.ticker.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * Created by pangjian on 16-11-30.
 */
@SpringBootApplication
@EnableAutoConfiguration
@Configuration
public class TickerAdminServer implements EmbeddedServletContainerCustomizer{

    public static void main(String[] args ){
        SpringApplication.run(TickerAdminServer.class,args);
    }

    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8090);
    }
}
