package at.ac.fhsalzburg.swd.spring.model;

import javax.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED) // create tables for inhereting classes
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
	
	@ManyToOne
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;

	public Media() {
    }
	
	
	public Media(String name, Genre genre, MediaType mediaType, Shelf shelf) {
        this.name = name;
        this.genre = genre;
        this.mediaType = mediaType;
        this.shelf = shelf;
        if (genre != null) {
            this.price = genre.getPrice();
        }
    }

	public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
        if (genre != null) {
            this.price = genre.getPrice();
        }
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }
}