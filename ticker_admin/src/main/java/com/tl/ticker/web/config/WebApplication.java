package com.tl.ticker.web.config;

import com.tl.ticker.web.config.filter.SecurityFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.FilterConfig;

/**
 * Created by pangjian on 16-12-1.
 */
@Configuration
public class WebApplication extends WebMvcConfigurerAdapter{

    public void addResourceHadlers(ResourceHandlerRegistry resgistry){
        resgistry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Bean
    public SecurityFilter securityFilter(){
        return new SecurityFilter();
    }

}
