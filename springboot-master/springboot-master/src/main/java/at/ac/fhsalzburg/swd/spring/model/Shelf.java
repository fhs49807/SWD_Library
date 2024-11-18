package at.ac.fhsalzburg.swd.spring.model;

import java.util.Collection;

import javax.persistence.*;

import lombok.NoArgsConstructor;

@Entity
@Table(name = "SHELVES")
public class Shelf {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Integer number;

	@ManyToOne
	@JoinColumn(name = "SECTION_ID")
	private Section section; // Each Shelf belongs to one Section

	@OneToMany(mappedBy = "shelf", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Collection<Media> mediaItems;// one self can hold multiple media items

	public Shelf() {
	}

	public Shelf(Integer number, Section section) {
		this.number = number;
		this.section = section;
	}

	public Long getShelfId() {
		return id;
	}

	public void setShelfId(Long id) {
		this.id = id;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}
}
