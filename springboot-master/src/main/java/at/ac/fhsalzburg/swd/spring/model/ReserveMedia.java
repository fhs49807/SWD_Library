package at.ac.fhsalzburg.swd.spring.model;

import java.sql.Date;

import javax.persistence.Entity;

@Entity
public class ReserveMedia {

	private Date expirationDate;
	private Date reservationDate;
	private Date reservationStatus;
	public ReserveMedia(Date expirationDate, Date reservationDate, Date reservationStatus) {
		super();
		this.expirationDate = expirationDate;
		this.reservationDate = reservationDate;
		this.reservationStatus = reservationStatus;
	}
	
	
	public Date getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
	public Date getReservationDate() {
		return reservationDate;
	}
	public void setReservationDate(Date reservationDate) {
		this.reservationDate = reservationDate;
	}
	public Date getReservationStatus() {
		return reservationStatus;
	}
	public void setReservationStatus(Date reservationStatus) {
		this.reservationStatus = reservationStatus;
	}
	
	
}
