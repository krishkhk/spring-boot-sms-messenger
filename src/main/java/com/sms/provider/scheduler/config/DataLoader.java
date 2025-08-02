package com.sms.provider.scheduler.config;

import org.springframework.stereotype.Component;

import com.sms.provider.scheduler.model.Role;
import com.sms.provider.scheduler.model.User;
import com.sms.provider.scheduler.repository.RoleRepository;
import com.sms.provider.scheduler.repository.UserRepository;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
public class DataLoader implements CommandLineRunner {
	
	private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    
    public DataLoader(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    

	@Override
	public void run(String... args) throws Exception {
		Role userRole =createRoleIfNotFound("ROLE_USER");
		Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
		
		 // Create a default admin user if one doesn't exist
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Set a strong password
            admin.setName("Admin User");
            admin.setRoles(Set.of(adminRole, userRole)); // Admin has both roles
            userRepository.save(admin);
        }
	}
	
	
	private Role createRoleIfNotFound(String name) {
		Role role = roleRepository.findByName(name);
		if(role==null) {
			role =new Role();
			role.setName(name);
			role=roleRepository.save(role);
		}
		return role;
	}

}
