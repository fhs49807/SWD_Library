package at.ac.fhsalzburg.swd.spring.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ReturnMedia extends BaseEntity {// extends mediaTransaction??

	private String returnCondition;
	private Date returnDate;

	@ManyToOne
	private MediaTransaction mediaTransaction;// media transaction can have multiple return entries??

	public ReturnMedia(String returnCondition, Date returnDate, MediaTransaction mediaTransaction) {
		super();
		this.returnCondition = returnCondition;
		this.returnDate = returnDate;
		this.mediaTransaction = mediaTransaction;
	}

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

	public MediaTransaction getMediaTransaction() {
		return mediaTransaction;
	}

	public void setMediaTransaction(MediaTransaction mediaTransaction) {
		this.mediaTransaction = mediaTransaction;
	}

}
