package io.nology.eventscreatorbackend.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService service;
	
	@PostMapping("/register")
	public ResponseEntity<AuthResponse> register(
			@Valid @RequestBody AuthRegisterDTO data
			) {
		
		return new ResponseEntity<>(service.register(data), HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginDTO data) {
		System.out.println(data.getEmail() + " email from controller");
		System.out.println(data.getPassword() + " passowrd");
		return new ResponseEntity<>(service.login(data), HttpStatus.OK);
	}
}
