package at.ac.fhsalzburg.swd.spring.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.NoArgsConstructor;

//MediaType = Audio, Book, 

@Entity
public class MediaType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String typeName;

	
	public MediaType() {
	}
	
	public MediaType(String typeName) {
		this.typeName = typeName;
	}

	public String getType() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Long getId() {
		return id;
	}
		
}
