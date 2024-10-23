package at.ac.fhsalzburg.swd.spring.model;

import java.sql.Date;

import javax.persistence.ManyToOne;

public class ReturnMedia {//extends mediaTransaction??
	
	private String returnCondition;
    private Date returnDate;

    //TODO: change?
    
    @ManyToOne 
    private Customer customer; 

    public ReturnMedia(String returnCondition, Date returnDate, Customer customer) { 
        this.returnCondition = returnCondition;
        this.returnDate = returnDate;
        this.customer = customer;
        }

    // Default constructor
    public ReturnMedia() {
    }

    // Getters and Setters
    public String getReturnCondition() {
        return returnCondition;
    }

    public void setReturnCondition(String returnCondition) {
        this.returnCondition = returnCondition;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public Customer getCustomer() { 
        return customer;
    }

    public void setCustomer(Customer customer) { 
        this.customer = customer;
    }
}