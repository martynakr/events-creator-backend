package io.nology.eventscreatorbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final CustomAuthEntryPoint customAuthEntryPoint;
	private final CorsConfigurationSource corsConfigurationSource;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		.csrf(CsrfConfigurer::disable)
		.cors(c -> c.configurationSource(corsConfigurationSource))
		.authorizeHttpRequests(authorize -> authorize
				.requestMatchers("/auth/register")
				.permitAll()
				.requestMatchers("/auth/login")
				.permitAll()
				.anyRequest().authenticated())
		 .sessionManagement(session -> session
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	     )
		 .exceptionHandling(exceptionHandling -> exceptionHandling
					.authenticationEntryPoint(customAuthEntryPoint) 
				)
		 .authenticationProvider(authenticationProvider)
		 .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
		return http.build();
	}
}
