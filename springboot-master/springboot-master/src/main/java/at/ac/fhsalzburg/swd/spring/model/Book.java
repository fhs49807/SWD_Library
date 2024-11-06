package at.ac.fhsalzburg.swd.spring.model;

import java.util.Date;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Book extends Media {

	private String ISBN;

	public Book(Long id, int barcode, String availabilityStatus, Date dueDate, String name, MediaType mediaType,
			Library library, String ISBN) {
		super(id, name, mediaType);
		this.ISBN = ISBN;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}

}
