package at.ac.fhsalzburg.swd.spring.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import java.sql.Date;

import org.springframework.http.MediaType;

import lombok.NoArgsConstructor;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn   TODO: ??
@NoArgsConstructor
public class Media {

	@Id  //markiert das Feld als Primärschlüssel
	@GeneratedValue(strategy = GenerationType.IDENTITY) //sorgt dafür, dass die ID automatisch generiert wird (z. B. bei Datenbank-Tabellen mit Auto-Increment-Funktion)
    private Long id;
	
	// TODO: add id?
	private int barcode;// id?
	private String availabilityStatus;
	private Date dueDate; // remove??
	private String name;

	@ManyToOne
	private MediaType mediaType;// one mediaType can have multiple medias??

	@ManyToOne
	private Library library;// one library can have multiple media

	public Media(int barcode, String availabilityStatus, Date dueDate, String name, MediaType mediaType,
			Library library) {
		super();
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

}
