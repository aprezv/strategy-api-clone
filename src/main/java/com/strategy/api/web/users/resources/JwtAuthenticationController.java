package com.strategy.api.web.users.resources;

import com.strategy.api.auth.services.JwtAuthenticationService;
import com.strategy.api.web.users.dto.JwtRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@CrossOrigin
public class JwtAuthenticationController {
	private final JwtAuthenticationService authenticationService;

	@Autowired
	public JwtAuthenticationController(JwtAuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest,
													   HttpServletResponse response) throws Exception {

		Cookie cookie = authenticationService.createTokenCookie(authenticationRequest);
		response.addCookie(cookie);
		return ResponseEntity.ok()
				.build();
	}
}
