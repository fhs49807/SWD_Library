package at.ac.fhsalzburg.swd.spring.model;

import java.util.Date;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
public class Movie extends Media {

	private String IMDb;

	public Movie(String imdb, String name, Genre genre, MediaType mediaType, Shelf shelf, int FSK) {
		super(name, genre, mediaType, shelf, FSK); // Call Media's constructor
		this.IMDb = imdb;
	}

	public Movie() {

	}

	public String getIMDb() {
		return IMDb;
	}

	public void setIMDb(String iMDb) {
		IMDb = iMDb;
	}

}
