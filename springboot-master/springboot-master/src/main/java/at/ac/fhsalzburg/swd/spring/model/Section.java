package at.ac.fhsalzburg.swd.spring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "SECTIONS")
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID bezieht sich auf die Tabelle
    @Column(name = "SECTION_ID")
    private Long sectionId;

    @Column(name = "NAME")
    private String name;

    @ManyToOne
    private Library library; // Each Library can have a list of Sections.

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Shelf> shelves; // A single Section can contain multiple Shelves.

    public Section(String name, Library library) {
        this.name = name;
        this.library = library;
    }
}
