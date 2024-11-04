package at.ac.fhsalzburg.swd.spring.model;

import java.sql.Date;

import org.springframework.http.MediaType;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Audio extends Media {

	private String codec;

	public Audio(int barcode, String availabilityStatus, Date dueDate, String name, MediaType mediaType,
			Library library) {
		super(barcode, availabilityStatus, dueDate, name, mediaType, library);
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

}
