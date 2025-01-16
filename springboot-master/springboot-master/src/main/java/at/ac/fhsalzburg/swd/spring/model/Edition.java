package at.ac.fhsalzburg.swd.spring.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

//edition = exemplar eines mediums

@Entity
@Getter
@Setter
@Table(name = "EDITIONS")
public class Edition {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// one media can have multiple editions
	// Each Edition is linked to one Media item
	@ManyToOne
	@JoinColumn(name = "media_id", nullable = false)
	private Media media;

	public Edition() {

	}

	public Edition(Media media) {
		this.media = media;
	}

	public Media getMedia() {
		return media;
	}

	public Long getId() {
		return id;
	}

}
