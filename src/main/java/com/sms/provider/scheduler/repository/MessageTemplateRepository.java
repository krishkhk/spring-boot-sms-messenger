package com.sms.provider.scheduler.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.provider.scheduler.model.MessageTemplate;

public interface MessageTemplateRepository extends JpaRepository<MessageTemplate, Long> {
	
	Page<MessageTemplate> findByTemplateNameContainingIgnoreCase(String name,Pageable pageable);

}
