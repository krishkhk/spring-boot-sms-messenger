package com.sms.provider.scheduler.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sms.provider.scheduler.model.MessageLog;
import com.sms.provider.scheduler.repository.MessageLogRepository;
import org.springframework.http.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(WebhookController.class);
	    private final MessageLogRepository logRepository;
	    
	    public WebhookController(MessageLogRepository logRepository) {
	        this.logRepository = logRepository;
	    }
	    
	    @PostMapping(value = "/twilio/status", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	    public ResponseEntity<Void> handleTwilioStatusUpdate(
	            @RequestParam("MessageSid") String messageSid,
	            @RequestParam("MessageStatus") String messageStatus) {

	        LOGGER.info("Received Twilio status update. SID: {}, Status: {}", messageSid, messageStatus.toUpperCase());

	        MessageLog log = logRepository.findByMessageSid(messageSid);
	        if (log != null) {
	            log.setStatus(messageStatus.toUpperCase()); // e.g., DELIVERED, FAILED
	            logRepository.save(log);
	        } else {
	            LOGGER.warn("Received status for unknown MessageSid: {}", messageSid);
	        }

	        return ResponseEntity.ok().build();
	    }
	    
}
