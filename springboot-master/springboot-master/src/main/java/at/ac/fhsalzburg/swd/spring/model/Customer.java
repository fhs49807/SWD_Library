package at.ac.fhsalzburg.swd.spring.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name = "CUSTOMER")
@NoArgsConstructor
public class Customer extends BaseEntity {

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
