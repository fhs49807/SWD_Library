package at.ac.fhsalzburg.swd.spring.security;


import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

// test with: curl http://localhost:8080/api/users -H 'Accept: application/json' -H "Authorization:Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPbmxpbmUgSldUIEJ1aWxkZXIiLCJpYXQiOjE2NjQzMDc0NDksImV4cCI6MTY5NTg0MzQ0OSwiYXVkIjoid3d3LmV4YW1wbGUuY29tIiwic3ViIjoiYWRtaW4iLCJ1c2VybmFtZSI6ImFkbWluIn0.curjpEf0q9S43s5EPLB9Pk7VXZEex0onsK2xr74QOak"

@Service
public class TokenService implements TokenServiceInterface {

	@Value("${myapp.jwt.secret}") // inject secret from application.properties
	private String JWT_SECRET;

	UserDetailService userDetailService; // autowired using setter/field injection
	
    public TokenService() {

    }

    @Override
    public String generateToken(UserDetails puser) {
    	DemoPrincipal user = (DemoPrincipal) puser;
        Instant expirationTime = Instant.now().plus(1, ChronoUnit.HOURS);
        Date expirationDate = Date.from(expirationTime);

        Key key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
        
        String compactTokenString = "Bearer " 
        		+ Jwts.builder()
        		.claim("id", user.getUsername())
                .claim("sub", user.getUsername())
                .claim("admin", user.getRole().equals("ADMIN"))
                .claim("role", user.getRole())
                .claim("password", user.getPassword())
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return compactTokenString;
    }

	@Override
	public DemoPrincipal parseToken(String token) {
		byte[] secretBytes = JWT_SECRET.getBytes();
		
		if (!token.startsWith("Bearer ")) return null;
		token = token.substring(6);
        
		Jws<Claims> jwsClaims = Jwts.parserBuilder()
                .setSigningKey(secretBytes)
                .build()
                .parseClaimsJws(token);

        String username = jwsClaims.getBody()
                .getSubject();               
        
        return (DemoPrincipal) userDetailService.loadUserByUsername(username);        
	}

	@Autowired
    public void setUserDetailService(@Lazy UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    public UserDetailService getUserDetailService() {
        return userDetailService;
    }
}
