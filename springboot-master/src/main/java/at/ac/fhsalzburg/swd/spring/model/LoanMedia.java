package at.ac.fhsalzburg.swd.spring.model;

import java.sql.Date;

import javax.persistence.Entity;

@Entity
public class LoanMedia {

	private Date expectedReturnDate;
	private Date loanDate;
	private Date loanStatus;
	
	
	public LoanMedia(Date expectedReturnDate, Date loanDate, Date loanStatus) {
		super();
		this.expectedReturnDate = expectedReturnDate;
		this.loanDate = loanDate;
		this.loanStatus = loanStatus;
	}


	public Date getExpectedReturnDate() {
		return expectedReturnDate;
	}


	public void setExpectedReturnDate(Date expectedReturnDate) {
		this.expectedReturnDate = expectedReturnDate;
	}


	public Date getLoanDate() {
		return loanDate;
	}


	public void setLoanDate(Date loanDate) {
		this.loanDate = loanDate;
	}


	public Date getLoanStatus() {
		return loanStatus;
	}


	public void setLoanStatus(Date loanStatus) {
		this.loanStatus = loanStatus;
	}
	
	
	
}
