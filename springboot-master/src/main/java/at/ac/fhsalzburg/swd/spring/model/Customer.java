package at.ac.fhsalzburg.swd.spring.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "CUSTOMERS")
public class Customer {

    @Id
    private int customerID;
    
    private String category;
    private int loanAmount;
    private String name;

    protected Customer() { }

    public Customer(String category, int customerID, int loanAmount, String name) {
        this.category = category;
        this.customerID = customerID;
        this.loanAmount = loanAmount;
        this.name = name;

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(int loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
