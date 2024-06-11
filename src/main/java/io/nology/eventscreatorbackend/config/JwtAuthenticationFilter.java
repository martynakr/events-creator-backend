package io.nology.eventscreatorbackend.config;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.nology.eventscreatorbackend.user.User;
import io.nology.eventscreatorbackend.user.UserService;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	private final JwtService jwtService;
	private final UserService userService;


	@Override
	protected void doFilterInternal(
			@NonNull HttpServletRequest request, 
			@NonNull HttpServletResponse response, 
			@NonNull FilterChain filterChain
			) throws ServletException, IOException {
		String requestURI = request.getRequestURI();
			// final String authHeader = request.getHeader("Authorization");

			// change to array of strings to allow swagger as well
			System.out.println("INSIDE JWT FILTER");
			  if (requestURI.equals("/auth/login") || requestURI.equals("/auth/register")) {
        		filterChain.doFilter(request, response);
        		return;
    }
			final String jwt = getCookieValue(request, "jwt");
			System.out.println(jwt + " JWT");
			final Long userId;

			
			// if(authHeader == null || !authHeader.startsWith("Bearer ")) {
			// 	filterChain.doFilter(request, response);
			// 	return;
			// }
			// jwt = authHeader.substring(7);
			if (jwt == null) {
            	filterChain.doFilter(request, response);
            	return;
        	}
			;
			userId = jwtService.extractUserId(jwt);
			
			// IF USER is in db and is not authenticated
			if(userId != null && SecurityContextHolder
					.getContext().getAuthentication() == null) {
				User user = this.userService.getById(userId);
				
				if(jwtService.isTokenValid(jwt, user)) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				            user,
				            null,
				            user.getAuthorities()
				        );
				        authToken.setDetails(
				            new WebAuthenticationDetailsSource().buildDetails(request)
				        );
				        
				        SecurityContextHolder.getContext().setAuthentication(authToken);
				        
				}
			}
			filterChain.doFilter(request, response);
	}

	    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }


}
