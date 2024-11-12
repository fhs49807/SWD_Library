package at.ac.fhsalzburg.swd.spring.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "CUSTOMERS")
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date birthDate;

	// customer role --> student, regular
	@Enumerated(EnumType.STRING)
	private CustomerType customerType;

	private int loanLimit;// based on customerType enum

    @Column(unique = true)
	private String name;// username

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
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

	public enum CustomerType {
		STUDENT, REGULAR
	}
}
