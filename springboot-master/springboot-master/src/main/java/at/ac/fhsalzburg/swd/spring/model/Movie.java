package at.ac.fhsalzburg.swd.spring.model;

import java.util.Date;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Movie extends Media {

	private String IMDb;

	public Movie(Long id, int barcode, String availabilityStatus, Date dueDate, String name, MediaType mediaType,
			Library library, String iMDb) {
		super();
		IMDb = iMDb;
	}

	public String getIMDb() {
		return IMDb;
	}

	public void setIMDb(String iMDb) {
		IMDb = iMDb;
	}

}
