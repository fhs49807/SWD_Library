package at.ac.fhsalzburg.swd.spring.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

//library name, library id, address

@Entity
@NoArgsConstructor
@Table(name = "LIBRARIES")
public class Library {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private String location;

	@OneToMany(mappedBy = "library", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Section> sections;// one library has many sections

	public Library(String name, String location, List<Section> sections) {
		this.name = name;
		this.location = location;
		this.sections = sections;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public Long getId() {
		return id;
	}

    public void setId(Long id) {
        this.id = id;
    }
}
