package at.ac.fhsalzburg.swd.spring.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.NoArgsConstructor;

//edition = exemplar eines mediums

@Entity
@Table(name = "EDITIONS")
@NoArgsConstructor
public class Edition {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dueDate;// due date (return date) for specific edition of medium

	
	//	TODO: enum Status (loaned, reserved, available)
	
	// one media can have multiple editions
	// Each Edition is linked to one Media item
	@ManyToOne
	private Media media;

	public Edition(Long id, Date dueDate, Media media) {
		super();
		this.id = id;
		this.dueDate = dueDate;
		this.media = media;
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

}
