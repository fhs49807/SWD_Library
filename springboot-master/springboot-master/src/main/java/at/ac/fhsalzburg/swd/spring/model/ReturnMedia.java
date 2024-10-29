package at.ac.fhsalzburg.swd.spring.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class ReturnMedia {// extends mediaTransaction??

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String returnCondition;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date returnDate;

	@ManyToOne
	private MediaTransaction mediaTransaction;// media transaction can have multiple return entries??

	public ReturnMedia(Long id, String returnCondition, Date returnDate, MediaTransaction mediaTransaction) {
		super();
		this.id = id;
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

	public Long getId() {
		return id;
	}

}
