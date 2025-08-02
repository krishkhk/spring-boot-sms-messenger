package com.sms.provider.scheduler.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MessageLog {
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	 	
	 	private String messageSid;
	    private String recipientNumber;
	    private String messageBody;
	    private String status; // e.g., "SENT", "FAILED"
	    private String messageType; // "SMS" or "WhatsApp"
	    private LocalDateTime timestamp;
	    
	    
	    public MessageLog() {
	    }
	    
	    public MessageLog(String messageSid, String recipientNumber, String messageBody, String status, String messageType) {
	        this.messageSid = messageSid;
	        this.recipientNumber = recipientNumber;
	        this.messageBody = messageBody;
	        this.status = status;
	        this.messageType = messageType;
	        this.timestamp = LocalDateTime.now();
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getMessageSid() {
			return messageSid;
		}

		public void setMessageSid(String messageSid) {
			this.messageSid = messageSid;
		}

		public String getRecipientNumber() {
			return recipientNumber;
		}

		public void setRecipientNumber(String recipientNumber) {
			this.recipientNumber = recipientNumber;
		}

		public String getMessageBody() {
			return messageBody;
		}

		public void setMessageBody(String messageBody) {
			this.messageBody = messageBody;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getMessageType() {
			return messageType;
		}

		public void setMessageType(String messageType) {
			this.messageType = messageType;
		}

		public LocalDateTime getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(LocalDateTime timestamp) {
			this.timestamp = timestamp;
		}
	    
	    

}
