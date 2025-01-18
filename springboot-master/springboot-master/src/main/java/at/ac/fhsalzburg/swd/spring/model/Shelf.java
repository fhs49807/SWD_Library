package at.ac.fhsalzburg.swd.spring.model;

import java.util.Collection;

import javax.persistence.*;

@Entity
@Table(name = "SHELVES")
public class Shelf {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "SECTION_ID")
	private Section section; // Each Shelf belongs to one Section

	@OneToMany(mappedBy = "shelf", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<Media> mediaItems; // One shelf can hold multiple media items

	public Shelf() {
	}

	public Shelf(Section section) {
		this.section = section;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public Collection<Media> getMediaItems() {
		return mediaItems;
	}

	public void setMediaItems(Collection<Media> mediaItems) {
		this.mediaItems = mediaItems;
	}
}
