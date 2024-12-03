package at.ac.fhsalzburg.swd.spring.controller;

import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.ReserveMediaTransaction;
import at.ac.fhsalzburg.swd.spring.services.MediaServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.ReserveMediaTransactionService;
import at.ac.fhsalzburg.swd.spring.services.ReserveMediaTransactionServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;
import at.ac.fhsalzburg.swd.spring.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Controller
public class MediaController extends BaseController {

	@Autowired
	private MediaServiceInterface mediaService;

	@Autowired
	private UserServiceInterface userService;

	@Autowired
	private MediaTransactionServiceInterface mediaTransactionService;
	
	@Autowired
	private ReserveMediaTransactionService reserveMediaTransactionService;

	@RequestMapping(value = "/loanReserveMedia", method = RequestMethod.POST)
	public String loanReserveMedia(@RequestParam(required = false) Long mediaId,
	                               @RequestParam(required = false) Date start_date, 
	                               @RequestParam(required = false) Date end_date,
	                               @RequestParam(required = false) String selectedGenre, 
	                               @RequestParam(required = false) String selectedType,
	                               @CurrentSecurityContext(expression = "authentication") Authentication authentication, 
	                               Model model) {

	    if (!(authentication instanceof AnonymousAuthenticationToken)) {
	        String username = authentication.getName();

	        // Validate required fields
	        if (mediaId == null || start_date == null || end_date == null) {
	            return handleError("Please select a media item and specify the reserve date period.", 
	                               start_date, end_date, selectedGenre, selectedType, username, model);
	        }

	        // Ensure start_date is before or equal to end_date
	        if (start_date.after(end_date)) {
	            return handleError("Start date must be on or before the end date.", 
	                               start_date, end_date, selectedGenre, selectedType, username, model);
	        }

	        try {
	            // If start_date is after today, process reservation
	        	if (start_date.after(new Date())) {
	        	    reserveMediaTransactionService.reserveMediaForCustomer(username, mediaId, start_date, end_date);
	        	    populateReservationSuccessModel(username, mediaId, start_date, end_date, model);
	        	    return "reservationSuccess"; // Prevent loan creation during reservation
	        	} else {
	        	    // Process loan if start_date is today or earlier
	        	    MediaTransaction transaction = mediaTransactionService.loanMedia(username, mediaId, end_date);
	        	    populateLoanSuccessModel(transaction, model, selectedGenre, selectedType);
	        	    return "loanSuccess";
	        	}

	        } catch (Exception e) {
	            System.err.println("Error processing loan/reservation: " + e.getMessage());
	            return handleError("Error processing loan/reservation: " + e.getMessage(), 
	                               start_date, end_date, selectedGenre, selectedType, username, model);
	        }
	    }

	    model.addAttribute("errorMessage", "You must log in to loan or reserve media.");
	    return "login";
	}


	// populate reservation success page
	private void populateReservationSuccessModel(String username, Long mediaId, Date start_date, Date end_date, Model model) {
	    Media media = mediaService.findById(mediaId);
	    model.addAttribute("username", username);
	    model.addAttribute("reservation_date", new Date()); // Current date as reservation processing date
	    model.addAttribute("start_date", start_date);
	    model.addAttribute("end_date", end_date);
	    model.addAttribute("mediaId", media.getId());
	    model.addAttribute("mediaTitle", media.getName());
	    model.addAttribute("mediaGenre", media.getGenre().getName());
	    model.addAttribute("mediaType", media.getMediaType().getType());

	    // Retrieve the edition ID for reservation
	    ReserveMediaTransaction reservation = reserveMediaTransactionService.getLatestReservation(mediaId, username);
	    model.addAttribute("editionId", reservation.getEdition().getId());
	}

	// populate loan success page
	private void populateLoanSuccessModel(MediaTransaction transaction, Model model, String selectedGenre,
	                                      String selectedType) {
	    Media media = transaction.getEdition().getMedia();
	    model.addAttribute("username", transaction.getUser().getUsername());
	    model.addAttribute("transaction_date", transaction.getStart_date());
	    model.addAttribute("start_date", transaction.getStart_date());
	    model.addAttribute("end_date", transaction.getEnd_date());
	    model.addAttribute("mediaId", media.getId());
	    model.addAttribute("mediaTitle", media.getName());
	    model.addAttribute("mediaGenre", media.getGenre().getName());
	    model.addAttribute("mediaType", media.getMediaType().getType());
	    model.addAttribute("editionIds", List.of(transaction.getEdition().getId()));
	}

	/**
	 * Return to loan page with default data and error message.
	 *
	 * @return path to loan html page
	 */
	private String handleError(String errorMessage, Date reserveStartDate, Date reserveEndDate, String selectedGenre,
			String selectedType, String username, Model model) {
		model.addAttribute("genres", mediaService.getAllGenres());
		model.addAttribute("mediaTypes", mediaService.getAllMediaTypes());
		model.addAttribute("selectedGenre", selectedGenre);
		model.addAttribute("selectedType", selectedType);
		model.addAttribute("mediaList", mediaService.searchMediaByGenreAndType(selectedGenre, selectedType,
				userService.getByUsername(username)));
		model.addAttribute("todayDate", DateUtils.getLocalDateFromDate(new Date()));
		model.addAttribute("endDate", DateUtils.getLocalDateFromDate(new Date()).plusDays(1));

		addErrorMessage(errorMessage, model);
		return "loan";
	}
}
