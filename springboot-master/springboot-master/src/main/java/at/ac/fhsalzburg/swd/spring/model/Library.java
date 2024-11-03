package at.ac.fhsalzburg.swd.spring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

//library name, library id, address

@Entity
@Getter
@Setter
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

    public Library(String name, String location) {
        this.name = name;
        this.location = location;
    }

}
