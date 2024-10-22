package at.ac.fhsalzburg.swd.spring.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Invoices")
public class Invoice {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int invoiceID;
	private Date dueDate;
	private boolean paymentStatus;
	
	
	//TODO: generate constructor including price, mediaType
	
}

	//TODO: generate getters and setters
