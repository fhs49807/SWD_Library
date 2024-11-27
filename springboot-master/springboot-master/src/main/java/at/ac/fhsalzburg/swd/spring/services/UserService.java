package at.ac.fhsalzburg.swd.spring.services;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.InvoiceRepository;
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

	@Autowired
	private InvoiceRepository invoiceRepository;// TODO: remove and move methods to invoiceRepo??

	public UserService() {
		i = 0;
	}

	// find customer by id
	// 	@Override
	// 	public User findById(Long id) {
	// 		return repo.findById(id).orElse(null);
	// 	}

	// calculate age of customer based on their birth date
	@Override
	public int getAge(User user) {
		LocalDate birthDate = user.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		return Period.between(birthDate, LocalDate.now()).getYears();
	}

	// checks if a customer can loan more based on their loanLimit
	@Override
	public boolean canLoanMore(User user) {
		int currentLoans = repo.countActiveLoansByUser(user);
		return currentLoans < user.getLoanLimit();
	}

	// checks if customer has any outstanding fees
	@Override
	public boolean hasOutstandingFees(User user) {
		Double outstandingBalance = invoiceRepository.calculateOutstandingBalance(user);
		return outstandingBalance != null && outstandingBalance > 0;
	}

	@Override
	public String doSomething() {
		i++;
		return Integer.toString(i);
	}

	@Override
	public boolean addUser(String username, String fullName, String eMail, String Tel, Date BirthDate, String password,
			String role, User.CustomerType customerType, int loanLimit) {

		if (username != null && username.length() > 0 //
				&& fullName != null && fullName.length() > 0) {
			
			DemoPrincipal userDetails = new DemoPrincipal(username, password, role, null);
			userDetails.setJwtToken(tokenService.generateToken(userDetails));
			
			User newCustomer = new User(username, fullName, eMail, Tel, BirthDate, passwordEncoder.encode(password),
					role, userDetails.getJwtToken(), customerType, loanLimit);

			repo.save(newCustomer);
			return true;
		}

		return false;

	}

	@Override
	public boolean addUser(User user) {

		if (user.getRole() == null)
			user.setRole(DEFAULT_ROLE);

		if ((user.getUsername() != null) && (user.getUsername().length() > 0)) {
			DemoPrincipal userDetails = new DemoPrincipal(user.getUsername(), user.getPassword(), user.getRole(), null);
			userDetails.setJwtToken(tokenService.generateToken(userDetails));
			user.setJwttoken(userDetails.getJwtToken());
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		repo.save(user);

		return false;

	}

	@Override
	public Collection<User> getAll() {
		List<User> result = StreamSupport.stream(repo.findAll().spliterator(), false).collect(Collectors.toList());

		return result;
	}

	@Override
	public boolean hasCredit(User customer) {
		if (customer.getCredit() > 0)
			return true;
		else
			return false;
	}

	@Override
	public User getByUsername(String username) {
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
	
	//aktualisiert das guthaben des user nach der abwicklung der rückgabe -> mediaTransactionSerice
	public void updateUser(User user) {
	    repo.save(user);  // änderungen des users in datenbank speichern
	}


}
