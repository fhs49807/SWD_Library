package at.ac.fhsalzburg.swd.spring.model;

import javax.persistence.Entity;

@Entity
public class Library {
	
	private int row;
	private int shelf;
	
	
	public Library(int row, int shelf) {
		super();
		this.row = row;
		this.shelf = shelf;
	}


	public int getRow() {
		return row;
	}


	public void setRow(int row) {
		this.row = row;
	}


	public int getShelf() {
		return shelf;
	}


	public void setShelf(int shelf) {
		this.shelf = shelf;
	}

	
}

