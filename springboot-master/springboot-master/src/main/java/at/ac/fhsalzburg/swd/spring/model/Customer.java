package at.ac.fhsalzburg.swd.spring.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CUSTOMER")
public class Customer {
	
	
	@Id
    @Column(name = "CUSTOMER_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private int customerID;
    private Date birthDate;
    private String customerType; //remove?
    private int loanLimit;
    private String name;
    
    
	public Customer(int customerID, Date birthDate, String customerType, int loanLimit, String name) {
		super();
		this.customerID = customerID;
		this.birthDate = birthDate;
		this.customerType = customerType;
		this.loanLimit = loanLimit;
		this.name = name;
	}


	public int getCustomerID() {
		return customerID;
	}


	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}


	public Date getBirthDate() {
		return birthDate;
	}


	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}


	public String getCustomerType() {
		return customerType;
	}


	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}


	public int getLoanLimit() {
		return loanLimit;
	}


	public void setLoanLimit(int loanLimit) {
		this.loanLimit = loanLimit;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
    	
    
    
}
