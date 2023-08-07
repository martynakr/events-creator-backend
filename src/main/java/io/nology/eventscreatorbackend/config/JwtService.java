package io.nology.eventscreatorbackend.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private static final String SECRET_KEY = "VV9rX/iLtY5B2u/AKzZkhMIOgfAWOxq67s"
			+ "1p7erRrPxqMwv563rU+xNbGPlyBhWnJhIcK/2AfAe6oS2czE9OFKweQ28FiYMzNPK4"
			+ "i7gitMEX/B1MR714C733gu/cr6iC7Lm4LRqs0/48ZStre780YrCIUZuxAg4P9ad+Dry"
			+ "E2NHIDGsXuvQk2NPjC1edQOzTBts9sMZ7fS9LaEZDRCI1N8Ua19m+08Pv64zqk0d85"
			+ "jm8KhJC8R91hIoB9Hh5jWy/ibCwLVcSmieY1M/JAK2KnAC8AYLod3KHPOpD3W7YWJfK"
			+ "D184ztrWuQfSsOIkrgV7zvg9JghjDgq8RWIoTYFSJ3XY10/pNGtXRU0r11xH29c=\n";
	
	public String extractUsername(String token) {
		return extractClaims(token, Claims::getSubject);
	}
	
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {
		return extractClaims(token, Claims::getExpiration);
	}
	

	public String generateToken(
		Map<String, Object> extraClaims,
		UserDetails userDetails
	) {
		return Jwts
				.builder()
				.setClaims(extraClaims)
				.setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
				.signWith(getSigninKey(), SignatureAlgorithm.HS256)
				.compact();
	}
	
	public String generateToken(UserDetails userDetails) {
		  return generateToken(new HashMap<>(), userDetails);
    }
	
	public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Claims extractAllClaims(String token) {
		return Jwts
				.parserBuilder()
				.setSigningKey(getSigninKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	private Key getSigninKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
