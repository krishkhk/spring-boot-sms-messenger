package com.sms.provider.scheduler.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.provider.scheduler.model.MessageLog;

public interface MessageLogRepository extends JpaRepository<MessageLog, Long> {
	
	MessageLog findByMessageSid(String messageSid);
	Page<MessageLog> findAllByOrderByTimestampDesc(Pageable pageable);

}
