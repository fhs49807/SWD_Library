package at.ac.fhsalzburg.swd.spring.model;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "CUSTOMER")
@NoArgsConstructor
public class Customer {

	@Id
    @Column(name = "CUSTOMER_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer customerId;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private String customerType; //remove?
    private int loanLimit;
    private String name;

    public Customer(Date birthDate, String customerType, int loanLimit, String name) {
        super();
        this.birthDate = birthDate;
        this.customerType = customerType;
        this.loanLimit = loanLimit;
        this.name = name;
    }

    public Integer getCustomerId() {
        return customerId;
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
