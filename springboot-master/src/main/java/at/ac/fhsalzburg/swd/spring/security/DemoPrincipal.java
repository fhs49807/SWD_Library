package at.ac.fhsalzburg.swd.spring.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DemoPrincipal implements UserDetails {
	
	private static final long serialVersionUID = 1L;

	private static final String ROLE_PREFIX = "ROLE_";
	
	private String user;
	private String password;
	private String role;
	private String jwtToken;
	

    public DemoPrincipal(String user, String password, String role, String jwtToken) {
        this.user = user;
        this.password = password;
        this.role = role;
        this.setJwtToken(jwtToken);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(ROLE_PREFIX+role);
        return Collections.singleton(authority);
    }
    

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return user;
	}
	

	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}

	public String getJwtToken() {
		return jwtToken;
	}

	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	    
}

