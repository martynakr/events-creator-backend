package io.nology.eventscreatorbackend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService service;
	
	@PostMapping("/register")
	public ResponseEntity<String> register(
			@Valid @RequestBody AuthRegisterDTO data
			) {
		
		AuthResponse jwt = this.service.register(data);
    	ResponseCookie cookie = ResponseCookie.from("jwt", jwt.getToken())
        .httpOnly(true)
        .secure(true)
        .maxAge(3600)
        .path("/")
        .build();

		return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body("Registration successful");
	}

	@PostMapping("/login")
	public ResponseEntity<String> login(@Valid @RequestBody AuthLoginDTO data) {
		AuthResponse jwt = this.service.login(data);

		ResponseCookie cookie = ResponseCookie.from("jwt", jwt.getToken())
        .httpOnly(true)
        .secure(true)
        .maxAge(3600)
        .path("/")
        .build();

		return ResponseEntity.ok()
        .header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body("Login successful");
	}
}
