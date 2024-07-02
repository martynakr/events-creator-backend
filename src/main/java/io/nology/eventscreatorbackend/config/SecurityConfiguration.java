package io.nology.eventscreatorbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
//CsrfConfigurer::disable
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
	
	private final JwtAuthenticationFilter jwtAuthFilter;
	private final AuthenticationProvider authenticationProvider;
	private final CustomAuthEntryPoint customAuthEntryPoint;
	private final CorsConfigurationSource corsConfigurationSource;
	private final CustomAccessDeniedHandler accessDeniedHandler;
	private final CsrfCookieFilter csrfCookieFilter;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		CookieCsrfTokenRepository tokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();

		CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();

		requestHandler.setCsrfRequestAttributeName(null);

		http
		.csrf((csrf) -> csrf
			.csrfTokenRepository(tokenRepository)
			.csrfTokenRequestHandler(requestHandler)
		)
		.cors(c -> c.configurationSource(corsConfigurationSource))
		.authorizeHttpRequests(authorize -> authorize
				.dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
				.requestMatchers("/auth/register")
    			.permitAll()
    			.requestMatchers("/auth/login")
    			.permitAll()
				.requestMatchers("/auth/token")
    			.permitAll()
				.requestMatchers(HttpMethod.OPTIONS)
    			.permitAll()
				.anyRequest().authenticated())
		.sessionManagement(session -> session
	                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	     )
		.exceptionHandling(exceptionHandling -> exceptionHandling
					//.accessDeniedHandler(accessDeniedHandler)
					.authenticationEntryPoint(customAuthEntryPoint) 
				)
		.authenticationProvider(authenticationProvider)
		.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
		.addFilterAfter(
			csrfCookieFilter, BasicAuthenticationFilter.class);
        
		return http.build();
	}
}
