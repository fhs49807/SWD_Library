package at.ac.fhsalzburg.swd.spring.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "USERS")
public class User {

	@Id
	private String username;// prinary key
	private String fullname;
	private String eMail;
	private String tel;
	private Long credit;
	private Date birthDate;
	private String password;
	private String role;
	private String jwttoken;

	// customer role --> student, regular
	@Enumerated(EnumType.STRING)
	private CustomerType customerType;

	private int loanLimit;// based on customerType enum

	// default constructor
	public User() {
	}

	public User(String username, String fullname, String eMail, String tel, Date birth, String password,
		String role, String jwtToken, CustomerType customerType, int loanLimit) {
		this.username = username;
		this.fullname = fullname;
		this.eMail = eMail;
		this.tel = tel;
		this.birthDate = birth;
		this.setCredit((long) 0);
		this.password = password;
		this.role = role;
		this.jwttoken = jwtToken;
		this.customerType = customerType;
		this.loanLimit = loanLimit;
	}

	public CustomerType getCustomerType() {
		return customerType;
	}

	public void setCustomerType(CustomerType customerType) {
		this.customerType = customerType;
	}

	public enum CustomerType {
		STUDENT, REGULAR
	}

	public int getLoanLimit() {
		return loanLimit;
	}

	public void setLoanLimit(int loanLimit) {
		this.loanLimit = loanLimit;
	}

	public String getFullname() {
		return fullname;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Long getCredit() {
		return credit;
	}

	public void setCredit(Long credit) {
		this.credit = credit;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getJwttoken() {
		return jwttoken;
	}

	public void setJwttoken(String jwttoken) {
		this.jwttoken = jwttoken;
	}

}
