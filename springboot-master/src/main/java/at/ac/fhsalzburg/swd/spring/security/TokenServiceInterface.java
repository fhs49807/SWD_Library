package at.ac.fhsalzburg.swd.spring.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenServiceInterface {
	
	String generateToken(UserDetails user);

	UserDetails parseToken(String token);

}
