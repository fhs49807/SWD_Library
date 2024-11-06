package at.ac.fhsalzburg.swd.spring.model;

import java.util.Date;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Audio extends Media {

	private String codec;

	public Audio(Long id, int barcode, String availabilityStatus, Date dueDate, String name, MediaType mediaType,
			Library library, String codec) {
		super(id, name, mediaType);
		this.codec = codec;
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

}
