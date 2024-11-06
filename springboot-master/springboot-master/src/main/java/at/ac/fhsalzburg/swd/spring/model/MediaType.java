package at.ac.fhsalzburg.swd.spring.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.NoArgsConstructor;

//MediaType = Audio, Book, 

@Entity
@NoArgsConstructor
public class MediaType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String typeName;

	
	public MediaType(Long id, String typeName) {
		super();
		this.id = id;
		this.typeName = typeName;
	}

	public String getType() {
		return typeName;
	}

	public void setType(String type) {
		this.typeName = type;
	}

	public Long getId() {
		return id;
	}

}
