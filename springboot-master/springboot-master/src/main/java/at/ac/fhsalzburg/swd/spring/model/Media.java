package at.ac.fhsalzburg.swd.spring.model;

import javax.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // create tables for inhereting classes
@NoArgsConstructor
public class Media {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	private double price;// set price automatically based on genre ('setGenre')

	// Each Media item belongs to a single Genre
	// One Genre can have multiple Media items
	@ManyToOne
	private Genre genre;

	// Each Media item has a single MediaType (book, audio, movie)
	// One MediaType can have multiple Media items
	@ManyToOne
	private MediaType mediaType;

	public Media() {
	}

	public Media(Long id, String name, MediaType mediaType) {
		super();
		this.id = id;
		this.name = name;
		this.mediaType = mediaType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public Long getId() {
		return id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
		if (genre != null) {
			this.price = genre.getPrice(); // Preis aus dem Genre übernehmen
		}
	}

	public Genre getGenre() {
		return genre;
	}

}
