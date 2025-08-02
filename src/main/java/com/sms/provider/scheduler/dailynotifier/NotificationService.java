package com.sms.provider.scheduler.dailynotifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sms.provider.scheduler.model.MessageLog;
import com.sms.provider.scheduler.repository.MessageLogRepository;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class NotificationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

	@Value("${twilio.phone-number}")
    private String twilioSmsNumber;
	
	@Value("${twilio.whatsapp-number}")
    private String twilioWhatsAppNumber;
	

	private final MessageLogRepository messageLogRepo;
	
	public NotificationService(MessageLogRepository messageLogRepo) {
		this.messageLogRepo=messageLogRepo;
	}
	
	
	@Async
	public void sendSMS(String to, String messageBody) {
		Message message;
		try {
			message = Message.creator(new PhoneNumber(to), new PhoneNumber(twilioSmsNumber), messageBody).create();
			LOGGER.info("SMS sent successfully! SID: {}", message.getSid());
			messageLogRepo.save(new MessageLog(message.getSid(), to, messageBody, "SENT", "SMS"));
		} catch (Exception e) {
			LOGGER.error("Failed to send SMS to {}: {}", to, e.getMessage());
			messageLogRepo.save(new MessageLog(null, to, messageBody, "FAILED", "SMS"));
		}
	}
	
	@Async
	public void sendWhatsAppMessage(String to, String messageBody) {
        try {
            Message message = Message.creator(
                    new PhoneNumber("whatsapp:" + to),                    
                    new PhoneNumber("whatsapp:" + twilioWhatsAppNumber),
                    messageBody
            ).create();
            LOGGER.info("WhatsApp message sent successfully! SID: {}", message.getSid());
            messageLogRepo.save(new MessageLog(message.getSid(), to, messageBody, "SENT", "WhatsApp"));
        } catch (Exception e) {
            LOGGER.error("Failed to send WhatsApp message: {}", e.getMessage());
            messageLogRepo.save(new MessageLog(null, to, messageBody, "FAILED", "WhatsApp"));
        }
    }
	 
	
	
}
