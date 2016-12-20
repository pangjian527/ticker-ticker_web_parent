package com.tl.ticker.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;

/**
 * Created by pangjian on 16-11-30.
 */
@SpringBootApplication
public class TickerAdminServer implements EmbeddedServletContainerCustomizer{

    public static void main(String[] args ){
        SpringApplication.run(TickerAdminServer.class,args);
    }

    public void customize(ConfigurableEmbeddedServletContainer container) {
        container.setPort(8090);
    }
}
