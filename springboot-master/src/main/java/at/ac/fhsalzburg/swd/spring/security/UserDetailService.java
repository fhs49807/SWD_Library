package at.ac.fhsalzburg.swd.spring.security;

import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserService UserService;

    @Override
    public UserDetails loadUserByUsername(String username) {

        Customer user = UserService.getByUsername(username);
        if (user == null)
            throw new UsernameNotFoundException(username);
        //return new DemoPrincipal(user.getName(), user.get(), user.getRole(), user.getJwttoken());
        return null;
    }
}
