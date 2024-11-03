package at.ac.fhsalzburg.swd.spring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "SHELVES")
public class Shelf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer number;

    @ManyToOne
    @JoinColumn(name = "SECTION_ID") // Consistent naming convention
    private Section section; // Each Shelf belongs to one Section

    public Shelf(Section section, Integer number) {
        this.section = section;
        this.number = number;
    }
}
