package at.ac.fhsalzburg.swd.spring.model;


public class LibraryManagementSystem {
	
    private boolean acceptance;


    public LibraryManagementSystem() { }

    public LibraryManagementSystem(boolean acceptance) {
        this.acceptance = acceptance;
    }

    public boolean isAcceptance() {
        return acceptance;
    }

    public void setAcceptance(boolean acceptance) {
        this.acceptance = acceptance;
    }
}


