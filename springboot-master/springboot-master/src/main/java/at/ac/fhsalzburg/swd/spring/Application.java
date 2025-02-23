package at.ac.fhsalzburg.swd.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@SpringBootApplication
public class Application {
	

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
	    return new BCryptPasswordEncoder();
	}
	
	
}
