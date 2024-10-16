package at.ac.fhsalzburg.swd.spring.services;


import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import at.ac.fhsalzburg.swd.spring.model.Customer;
import at.ac.fhsalzburg.swd.spring.repository.UserRepository;
import at.ac.fhsalzburg.swd.spring.security.DemoPrincipal;
import at.ac.fhsalzburg.swd.spring.security.TokenService;


@Service
public class UserService implements UserServiceInterface {

	@Autowired
	private PasswordEncoder passwordEncoder;

	private TokenService tokenService; // autowired using setter/field injection

    public final static String DEFAULT_ROLE = "USER";

	int i;


    @Autowired
    private UserRepository repo;

    public UserService() {
        i = 0;
    }

    @Override
    public String doSomething() {
        i++;
        return Integer.toString(i);
    }

    @Override
    public boolean addUser(String username, String fullName, String eMail, String Tel,
            Date BirthDate, String password, String role) {

        if (username != null && username.length() > 0 //
                && fullName != null && fullName.length() > 0) {
        	DemoPrincipal userDetails = new DemoPrincipal(username, password, role, null);
        	userDetails.setJwtToken(tokenService.generateToken(userDetails));
            Customer newCustomer = new Customer("student", 1, 3, username);

            repo.save(newCustomer);
            return true;
        }

        return false;

    }

    @Override
    public boolean addUser(Customer user) {

    	if (user.getRole()==null) user.setRole(DEFAULT_ROLE);

    	if ((user.getUsername()!=null) && (user.getUsername().length()>0))
    	{
    		DemoPrincipal userDetails = new DemoPrincipal(user.getUsername(), user.getPassword(), user.getRole(), null);
    		userDetails.setJwtToken(tokenService.generateToken(userDetails));
    		user.setJwttoken(userDetails.getJwtToken());
    	}
    	user.setPassword(passwordEncoder.encode(user.getPassword()));


        repo.save(user);

        return false;

    }

    @Override
    public Collection<Customer> getAll() {
    	List<Customer> result =
    			  StreamSupport.stream(repo.findAll().spliterator(), false)
    			    .collect(Collectors.toList());

        return result;
    }



    @Override
    public boolean hasCredit(Customer customer) {
        if (customer.getCredit() > 0)
            return true;
        else
            return false;
    }

	@Override
	public Customer getByUsername(String username) {
		return repo.findByUsername(username);
	}

	@Autowired
    public void setTokenService(@Lazy TokenService tokenService) {
        this.tokenService = tokenService;
    }

    public TokenService getTokenService() {
        return tokenService;
    }

	@Override
	public boolean deleteUser(String username) {

		repo.deleteById(username);

		return true;
	}



}
