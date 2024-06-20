package io.nology.eventscreatorbackend.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import io.github.cdimascio.dotenv.Dotenv;
import io.nology.eventscreatorbackend.user.UserRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
	
	private final UserRepository repository;
	
	@Bean
	public Dotenv dotenv() {
		return Dotenv.configure().load();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
			return email -> repository.findByEmail(email)
			        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
	
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public CustomAuthEntryPoint customAuthenticationEntryPoint() {
		return new CustomAuthEntryPoint();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
    	CorsConfiguration configuration = new CorsConfiguration();
    	configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
    	configuration.setAllowedMethods(Arrays.asList("GET","POST"));
    	configuration.setAllowedHeaders(Arrays.asList("*"));
		configuration.setAllowCredentials(true);
    	UrlBasedCorsConfigurationSource source = new 	UrlBasedCorsConfigurationSource();
    	source.registerCorsConfiguration("/**", configuration);
    	return source;
	}

}
