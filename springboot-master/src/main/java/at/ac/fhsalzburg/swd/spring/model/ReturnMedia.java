package at.ac.fhsalzburg.swd.spring.model;

import java.sql.Date;

import javax.persistence.Entity;

@Entity
public class ReturnMedia {

	private Date returnDate;

	public ReturnMedia(Date returnDate) {
		super();
		this.returnDate = returnDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	
	
}
