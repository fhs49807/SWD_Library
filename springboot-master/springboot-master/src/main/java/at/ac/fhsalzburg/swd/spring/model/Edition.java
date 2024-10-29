package at.ac.fhsalzburg.swd.spring.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;

//edition = exemplar eines mediums

@Entity
@Table(name = "EDITIONS")
@NoArgsConstructor
public class Edition {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String editionYear;

	@ManyToOne
	private Media media;// one media can have multiple editions

	public Edition(Media media) {
		this.media = media;
	}

	public long getEditionID() {
		return id;
	}

	public void setEditionID(long id) {
		this.id = id;
	}

	public String getEditionYear() {
		return editionYear;
	}

	public void setEditionYear(String editionYear) {
		this.editionYear = editionYear;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

}
