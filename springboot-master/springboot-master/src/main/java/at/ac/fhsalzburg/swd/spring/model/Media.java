package at.ac.fhsalzburg.swd.spring.model;

import java.sql.Date;

import javax.persistence.Entity;

@Entity
public class Media {

	private int barcode;
	private String availabilityStatus;
	private Date dueDate; //remove??
	private String name;
	
	
	//TODO: add many to ones (eg. mediaType)
	
	//TODO: generate constructor including mediaType
}

	//TODO: generate getters and setters
