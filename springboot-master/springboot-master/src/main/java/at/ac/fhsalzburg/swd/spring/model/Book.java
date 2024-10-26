package at.ac.fhsalzburg.swd.spring.model;

import java.sql.Date;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Book extends Media {

	private String ISBN;

	public Book(int barcode, String availabilityStatus, Date dueDate, String name, MediaType mediaType,
			Library library) {
		super(barcode, availabilityStatus, dueDate, name, mediaType, library);
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

}
