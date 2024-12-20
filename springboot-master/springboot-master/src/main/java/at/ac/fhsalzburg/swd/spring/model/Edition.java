package at.ac.fhsalzburg.swd.spring.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.NoArgsConstructor;

//edition = exemplar eines mediums

@Entity
@Table(name = "EDITIONS")
public class Edition {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dueDate;// due date (return date) for specific edition of medium

	// TODO: add to class diagram
	private boolean available;// availability status

	// one media can have multiple editions
	// Each Edition is linked to one Media item
	@ManyToOne
	@JoinColumn(name = "media_id", nullable = false)
	private Media media;
	
	@Column(name = "media_name")
    private String mediaName; // Add the media name as a separate column


	public Edition(Media media, boolean available, Date dueDate) {
		this.media = media;
		this.available = available;
		this.dueDate = dueDate;
	}

	public Edition() {
		
	}
	

	public Edition(Media media) {
	    this.media = media;
	    this.available = true;
	}
	 public String getMediaName() {
	        return mediaName;
	    }

	    public void setMediaName(String mediaName) {
	        this.mediaName = mediaName;
	    }
	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}

	public Long getId() {
		return id;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

}
