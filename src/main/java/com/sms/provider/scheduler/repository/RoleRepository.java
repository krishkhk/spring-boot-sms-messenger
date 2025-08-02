package com.sms.provider.scheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sms.provider.scheduler.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Role findByName(String name);

}
