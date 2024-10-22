package at.ac.fhsalzburg.swd.spring.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MediaTransaction {

	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int transactionID;
	
	private String transcationStatus;
	
	private Date expectedReturnDate;
	private Date expirationDate;
	private Date transactionDate;
	
	
	@ManyToOne
    private Edition edition;
	
	//TODO: @ManyToOne Customer??
	//TODO: @ManyToOne price??
	
	
	//TODO: generate constructor
}

	//TODO: generate getters and setters