package com.sms.provider.scheduler.dailynotifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class DailyMessageScheduler {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DailyMessageScheduler.class);

    private final NotificationService notificationService;

    @Value("${recipient.phone-number}")
    private String recipientPhoneNumber;

    public DailyMessageScheduler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    //@Scheduled(cron = "*/30 * * * * *")
  //  @Scheduled(cron = "0 26 17 * * ?")
    public void sendDailyMessages() {
    	LOGGER.info("Scheduler is running. Sending daily messages...");
    
    String smsMessage = "Hi ! This is your daily SMS reminder. Have a test TIMe !";
	String whatsappMessage = "Hello from your Spring Boot app! This is your daily WhatsApp notification. 🚀";
    	
    	
    	// Send the SMS
    	notificationService.sendSMS(recipientPhoneNumber, smsMessage);
    	
    	// Send the WhatsApp message
    	notificationService.sendWhatsAppMessage(recipientPhoneNumber, whatsappMessage);
    }

}
