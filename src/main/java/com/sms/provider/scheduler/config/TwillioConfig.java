package com.sms.provider.scheduler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.twilio.Twilio;

import jakarta.annotation.PostConstruct;

@Configuration
public class TwillioConfig {
	
	 @Value("${twilio.account-sid}")
	    private String accountSid;
	 
	 @Value("${twilio.auth-token}")
	    private String authToken;
	 
	 @PostConstruct // This ensures this method runs after the bean is created
	    public void initTwilio() {
	        Twilio.init(accountSid, authToken);
	    }

}
