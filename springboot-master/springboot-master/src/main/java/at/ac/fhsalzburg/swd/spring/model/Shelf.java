package at.ac.fhsalzburg.swd.spring.model;

import javax.persistence.*;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "SHELVES")
public class Shelf {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Integer number;

	@ManyToOne
	@JoinColumn(name = "SECTION_ID") // Consistent naming convention
	private Section section; // Each Shelf belongs to one Section

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
