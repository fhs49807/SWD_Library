package at.ac.fhsalzburg.swd.spring.model;

import java.util.Collection;
import java.util.List;

import javax.persistence.*;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "SECTIONS")
public class Section {

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SECTION_ID")
    private Integer sectionId;
    
    @Column(name = "NAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "LIBRARY_ID")
    private Library library;
    
    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Shelf> shelves;


    public Section(String name) {
        this.name = name;
    }

    public Section(String name, List<Shelf> shelves) {
        this.name = name;
        this.shelves = shelves;
    }

    // Getters and setters
    public Integer getSectionId() {
        return sectionId;
    }

    public void setSectionId(Integer sectionId) {
        this.sectionId = sectionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Shelf> getShelves() {
        return shelves;
    }

    public void setShelves(List<Shelf> shelves) {
        this.shelves = shelves;
    }
}
