package com.walmart.ticketing.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.walmart.ticketing.interceptor.RefreshInterceptor;

@Configuration
public class Config extends WebMvcConfigurerAdapter{
	
    @Bean
    public RefreshInterceptor refreshInterceptor() {
        return new RefreshInterceptor();
    }
	
    @Override
    public void addInterceptors(InterceptorRegistry registry){
    	
    		//need to refresh the SEAT database based on the created time of the hold.
        registry.addInterceptor(refreshInterceptor()).addPathPatterns("/api/venues/**");
        registry.addInterceptor(refreshInterceptor()).addPathPatterns("/api/seatHolds/**");
    }
    

}
