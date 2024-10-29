package at.ac.fhsalzburg.swd.spring.model;

import java.util.Date;
import javax.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn(name = "media_type") ????
@NoArgsConstructor
public class Media {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int barcode;
	private String availabilityStatus;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dueDate;
	private String name;

	@ManyToOne
	private MediaType mediaType; //NullPointerException??

	@ManyToOne
	private Library library;//NullPointerException??

	public Media(Long id, int barcode, String availabilityStatus, Date dueDate, String name, MediaType mediaType,
			Library library) {
		super();
		this.id = id;
		this.barcode = barcode;
		this.availabilityStatus = availabilityStatus;
		this.dueDate = dueDate;
		this.name = name;
		this.mediaType = mediaType;
		this.library = library;
	}

	public int getBarcode() {
		return barcode;
	}

	public void setBarcode(int barcode) {
		this.barcode = barcode;
	}

	public String getAvailabilityStatus() {
		return availabilityStatus;
	}

	public void setAvailabilityStatus(String availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
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

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}

	public Long getId() {
		return id;
	}

}
