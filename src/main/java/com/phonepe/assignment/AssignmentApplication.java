package com.phonepe.assignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@SpringBootApplication
public class AssignmentApplication extends SpringBootServletInitializer{
	
	public static void main(String[] args) {
		SpringApplication.run(AssignmentApplication.class, args);
	}
	
	@Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        FixedBackOffPolicy bopolicy = new FixedBackOffPolicy();
        	bopolicy.setBackOffPeriod(100l);
        retryTemplate.setBackOffPolicy(bopolicy);
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(2);
        retryTemplate.setRetryPolicy(retryPolicy);
        return retryTemplate;
    }
}
