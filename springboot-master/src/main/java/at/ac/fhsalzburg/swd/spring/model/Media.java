package at.ac.fhsalzburg.swd.spring.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private boolean availabilityStatus;
    private int barcode;
    private String dueDate;
    private int ISBN_number;
    private String mediaType;
    private String name;

    protected Media() { }

    public Media(boolean availabilityStatus, int barcode, String dueDate, int ISBN_number, String mediaType,
        String name) {
        this.availabilityStatus = availabilityStatus;
        this.barcode = barcode;
        this.dueDate = dueDate;
        this.ISBN_number = ISBN_number;
        this.mediaType = mediaType;
        this.name = name;
    }

    public boolean isAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(boolean availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public int getISBN_number() {
        return ISBN_number;
    }

    public void setISBN_number(int ISBN_number) {
        this.ISBN_number = ISBN_number;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
