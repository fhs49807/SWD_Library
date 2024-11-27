package at.ac.fhsalzburg.swd.spring.model;

import java.util.Date;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
public class Book extends Media {

	private String ISBN;

	public Book(String ISBN, String name, Genre genre, MediaType mediaType, Shelf shelf, int FSK) {
        super(name, genre, mediaType, shelf, FSK); // Call Media's constructor
        this.ISBN = ISBN; // Initialize Book-specific field
    }

	public Book() {
		// TODO Auto-generated constructor stub
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}

}
