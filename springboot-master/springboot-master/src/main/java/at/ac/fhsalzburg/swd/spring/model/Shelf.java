package at.ac.fhsalzburg.swd.spring.model;

import javax.persistence.*;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Table(name = "SHELVES")
public class Shelf {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long shelfId;
    private Integer number;

    @ManyToOne
    @JoinColumn(name = "SECTION_ID")
    private Section section;

    public Shelf(Integer number, Section section) {
        this.number = number;
        this.section = section;
    }

    public Long getShelfId() {
        return shelfId;
    }

    public void setShelfId(Long shelfId) {
        this.shelfId = shelfId;
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
