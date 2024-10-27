package at.ac.fhsalzburg.swd.spring.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class Library extends BaseEntity {

    private int cell;
    private int shelf;

    public Library(int cell, int shelf) {
        super();
        this.cell = cell;
        this.shelf = shelf;
    }

    public int getShelf() {
        return shelf;
    }

    public void setShelf(int shelf) {
        this.shelf = shelf;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }
}
