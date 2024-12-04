package at.ac.fhsalzburg.swd.spring.model;

import java.util.List;
import javax.persistence.*;

import lombok.NoArgsConstructor;

@Entity
@Table(name = "SECTIONS")
public class Section {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;

	@ManyToOne
	@JoinColumn(name = "LIBRARY_ID")
	private Library library; // Each Library can have a list of Sections.

	@OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Shelf> shelves; // A single Section can contain multiple Shelves.

	public Section(String name) {
		this.name = name;
	}

	public Section() {
	}

	public Section(String name, List<Shelf> shelves) {
		this.name = name;
		this.shelves = shelves;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Library getLibrary() {
		return library;
	}

	public void setLibrary(Library library) {
		this.library = library;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Shelf> getShelves() { // Aligned getter type with field type
		return shelves;
	}

	public void setShelves(List<Shelf> shelves) {
		this.shelves = shelves;
	}
}
