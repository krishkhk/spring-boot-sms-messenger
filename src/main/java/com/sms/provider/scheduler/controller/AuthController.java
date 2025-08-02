package com.sms.provider.scheduler.controller;

import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sms.provider.scheduler.dto.SignUpRequestDTO;
import com.sms.provider.scheduler.model.Role;
import com.sms.provider.scheduler.model.User;
import com.sms.provider.scheduler.repository.RoleRepository;
import com.sms.provider.scheduler.repository.UserRepository;
import com.sms.provider.scheduler.service.JwtService;
import com.sms.provider.scheduler.utill.DataMaskingUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;
	private final RoleRepository roleRepository;

	public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder,
			AuthenticationManager authenticationManager, JwtService jwtService,RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.roleRepository=roleRepository;
	}

	@PostMapping("/signup")
	public ResponseEntity<String> signup(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
		if (userRepository.findByUsername(signUpRequestDTO.getUsername()).isPresent()) {
			return ResponseEntity.badRequest().body("Username is already taken");
		}
		
		User user = new User();
		user.setEmail(DataMaskingUtil.maskEmail(signUpRequestDTO.getEmail()));
		user.setMobileNumber(DataMaskingUtil.maskMobileNumber(signUpRequestDTO.getMobileNumber()));
		user.setPassword(passwordEncoder.encode(signUpRequestDTO.getPassword()));
		user.setUsername(signUpRequestDTO.getUsername());
		
		Role userRole = roleRepository.findByName("ROLE_USER");
		user.setRoles(Set.of(userRole));
		userRepository.save(user);
		return ResponseEntity.ok("User registered successfully");
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials,
			HttpServletResponse response) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(credentials.get("username"), credentials.get("password")));

		User user = (User) authentication.getPrincipal();
		String token = jwtService.generateToken(user);

		// Create a temporary cookie to hold the JWT
		Cookie jwtCookie = new Cookie("jwtToken", token);
		jwtCookie.setPath("/");
		jwtCookie.setMaxAge(60 * 60);
		// jwtCookie.setHttpOnly(true);
		response.addCookie(jwtCookie);
		return ResponseEntity.ok(Map.of("token", token));
	}

}
