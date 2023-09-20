package io.nology.eventscreatorbackend.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.nology.eventscreatorbackend.config.JwtService;
import io.nology.eventscreatorbackend.exceptions.NotFoundException;
import io.nology.eventscreatorbackend.user.User;
import io.nology.eventscreatorbackend.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private final PasswordEncoder passwordEncoder;
	
	@Autowired
	private final JwtService jwtService;
	
	@Autowired
	private final AuthenticationManager authManager;

	public AuthResponse register(AuthRegisterDTO data) {
		User user = new User(data.getFirstName(), 
				data.getLastName(), 
				data.getEmail(),
				passwordEncoder.encode(data.getPassword()));
		
		userRepository.save(user);
		String token = jwtService.generateToken(user);
		System.out.println(token + " token");
		return AuthResponse.builder().token(token).build();
		
	}
	
	public AuthResponse login(AuthLoginDTO data) {
		
		
		UsernamePasswordAuthenticationToken tok =  new UsernamePasswordAuthenticationToken(
	            data.getEmail(),
	            data.getPassword()
	        );
		
		
		authManager.authenticate(tok);

		User user = userRepository.findByEmail(data.getEmail()).orElseThrow(
				() -> new NotFoundException("Incorrect login details"));
		String token = jwtService.generateToken(user);
		return AuthResponse.builder().token(token).build();
	}
	
	public User getCurrentUser() {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String email = auth.getName();
		
		Optional<User> loggedInUser = 
				this.userRepository.findByEmail(email);
		
		if(loggedInUser == null) {
			// throw error? shoudln't really happen
			return null;
		}
		
		return loggedInUser.get();
	}
}
