package at.ac.fhsalzburg.swd.spring.model;

import java.util.Date;

import org.springframework.http.MediaType;

import javax.persistence.Entity;

import lombok.NoArgsConstructor;

@Entity
public class Audio extends Media {

	private String codec;

	public Audio(String codec, String name, Genre genre, MediaType mediaType, Shelf shelf, int FSK) {
        super(name, genre, mediaType, shelf, FSK); // Call Media's constructor
        this.codec = codec;
    }
	
	public Audio() {
        
    }

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

}
