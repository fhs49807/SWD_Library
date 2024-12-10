package at.ac.fhsalzburg.swd.spring.controller;

import at.ac.fhsalzburg.swd.spring.dto.MediaTransactionDTO;
import at.ac.fhsalzburg.swd.spring.model.*;
import at.ac.fhsalzburg.swd.spring.services.MediaServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.ReserveMediaTransactionService;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;
import at.ac.fhsalzburg.swd.spring.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
		@RequestParam(required = false) Date start_date, @RequestParam(required = false) Date end_date,
		@RequestParam(required = false) String selectedGenre, @RequestParam(required = false) String selectedType,
		@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String username = authentication.getName();

			// Validate required fields
			if (mediaId == null || start_date == null || end_date == null) {
				return handleError("Please select a media item and specify the reserve date period.",
					selectedGenre, selectedType, username, model);
			}

			// Ensure start_date is before or equal to end_date
			if (start_date.after(end_date)) {
				return handleError("Start date must be on or before the end date.", selectedGenre,
					selectedType, username, model);
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
					populateLoanSuccessModel(transaction, model);
					return "loanSuccess";
				}
			} catch (Exception e) {
				System.err.println("Error processing loan/reservation: " + e.getMessage());
				return handleError("Error processing loan/reservation: " + e.getMessage(), selectedGenre, selectedType,
					username, model);
			}
		}

		model.addAttribute("errorMessage", "You must log in to loan or reserve media.");
		return "login";
	}

	@GetMapping("/returnMedia")
	public String returnMediaPage(Model model, Principal principal) {
	    if (principal == null) {
	        model.addAttribute("errorMessage", "You must log in to access this page.");
	        return "login"; // Zeigt die Login-Seite an, wenn der Benutzer nicht eingeloggt ist
	    }

	    // Weiterverarbeitung für eingeloggte Benutzer
	    User user = userService.getByUsername(principal.getName());
	    Collection<MediaTransaction> loans = mediaTransactionService.findLoansByUser(user);
	    if (loans == null) {
	        loans = new ArrayList<>(); // Falls loans null ist, initialisiere es als leere Liste
	    }
	    model.addAttribute("loans", loans);
	    return "returnMedia"; // Zeigt die Rückgabeseite an, wenn der Benutzer eingeloggt ist
	}

	@PostMapping("/returnMedia") // definiert die http post-anforderung zur verarbeitung der medienrückgabe
	public String returnMedia(@RequestParam Long transactionId, Model model) { // methode zur rückgabe eines mediums
	    try {
	        // Aufruf der Service-Methode, um die Rückgabe zu verarbeiten
	        mediaTransactionService.returnMedia(transactionId);

	        // Erfolgreiche Rückgabe: Erfolgsmeldung hinzufügen
	        addSuccessMessage("Media returned successfully.", model);

	        // Zurück zur Rückgabeseite mit aktualisierten Transaktionen
	        return "redirect:/returnMedia"; // Leitet zur Rückgabeseite um, um die aktualisierte Ansicht anzuzeigen
	    } catch (Exception e) {
	        // Fehler beim Rückgabeverfahren: Fehlermeldung hinzufügen
	        addErrorMessage("Error returning media: " + e.getMessage(), model);
	        return "redirect:/returnMedia"; // Fehler und Weiterleitung zur gleichen Seite
	    }
	}

	@PostMapping("/cancelReservation")
	public String cancelReservation(@RequestParam Long reservationId, Model model) {
		reserveMediaTransactionService.cancelReservation(reservationId);
		addSuccessMessage("Media returned successfully.", model);

		return "redirect:/returnMedia";
	}

	// populate reservation success page
	private void populateReservationSuccessModel(String username, Long mediaId, Date start_date, Date end_date,
		Model model) {
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
	private void populateLoanSuccessModel(MediaTransaction transaction, Model model) {
		// Retrieve the Media entity associated with the transaction
		Media media = transaction.getEdition().getMedia();

		// Retrieve the Shelf and Section from the Media entity
		Shelf shelf = media.getShelf();
		Section section = shelf.getSection();
		Library library = section.getLibrary();

		// Populate model attributes
		model.addAttribute("username", transaction.getUser().getUsername());
		model.addAttribute("transaction_date", transaction.getStart_date());
		model.addAttribute("start_date", transaction.getStart_date());
		model.addAttribute("end_date", transaction.getEnd_date());
		model.addAttribute("mediaId", media.getId());
		model.addAttribute("mediaTitle", media.getName());
		model.addAttribute("mediaGenre", media.getGenre().getName());
		model.addAttribute("mediaType", media.getMediaType().getType());
		model.addAttribute("editionIds", List.of(transaction.getEdition().getId()));

		// Add physical location details
		model.addAttribute("shelfNumber", shelf.getNumber());
		model.addAttribute("sectionName", section.getName());
		model.addAttribute("libraryName", library.getName());
	}


	/**
	 * Return to loan page with default data and error message.
	 *
	 * @return path to loan html page
	 */
	private String handleError(String errorMessage, String selectedGenre, String selectedType, String username,
		Model model) {
		List<String> genres = addDefaultOption(
			StreamSupport.stream(mediaService.getAllGenres().spliterator(), false).collect(Collectors.toList()),
			"All Genres");
		List<String> mediaTypes = addDefaultOption(
			StreamSupport.stream(mediaService.getAllMediaTypes().spliterator(), false).collect(Collectors.toList()),
			"All Media Types");

		model.addAttribute("genres", genres);
		model.addAttribute("mediaTypes", mediaTypes);
		model.addAttribute("selectedGenre", selectedGenre);
		model.addAttribute("selectedType", selectedType);
		model.addAttribute("mediaList", mediaService.searchMediaByGenreAndType(selectedGenre, selectedType,
			userService.getByUsername(username)));
		model.addAttribute("todayDate", DateUtils.getLocalDateFromDate(new Date()));
		model.addAttribute("endDate", DateUtils.getLocalDateFromDate(new Date()).plusDays(1));

		addErrorMessage(errorMessage, model);
		return "loan";
	}

	// Add an empty/default option to the list of genres and media types
	private List<String> addDefaultOption(List<String> options, String defaultLabel) {
		options.add(0, defaultLabel); // Add an empty option at the beginning
		return options;
	}

	private void fetchUserReservations(Model model, User user) {
		Collection<MediaTransactionDTO> mediaTransactionDTOs = new ArrayList<>();
		for (ReserveMediaTransaction transaction : reserveMediaTransactionService.findReservationsForUser(user)) {
			MediaTransactionDTO dto =
				new MediaTransactionDTO(transaction.getId(), transaction.getEdition().getMediaName(),
					DateUtils.getLocalDateFromDate(transaction.getReserveStartDate()),
					DateUtils.getLocalDateFromDate(transaction.getReserveEndDate()));
			mediaTransactionDTOs.add(dto);
		}
		model.addAttribute("reservations", mediaTransactionDTOs);
	}
	
	@PostMapping("/returnMediaSuccess")
	public String returnMediaSuccess(@RequestParam Long transactionId, Model model) {
	    try {
	        // Holen Sie die Transaktion basierend auf der Transaktions-ID
	        MediaTransaction transaction = mediaTransactionService.findById(transactionId);

	        if (transaction == null) {
	            addErrorMessage("Media transaction not found.", model);
	            return "redirect:/returnMedia";
	        }

	        // Berechne die verspätete Gebühr
	        double penaltyAmount = calculatePenalty(transaction);

	        // Setze das Rückgabedatum
	        Date returnDate = new Date(); // Rückgabedatum ist das aktuelle Datum

	        // Fülle das Modell mit den benötigten Details
	        model.addAttribute("username", transaction.getUser().getUsername());
	        model.addAttribute("transaction_date", transaction.getStart_date());
	        model.addAttribute("mediaTitle", transaction.getEdition().getMedia().getName());
	        model.addAttribute("mediaGenre", transaction.getEdition().getMedia().getGenre().getName());
	        model.addAttribute("mediaType", transaction.getEdition().getMedia().getMediaType().getType());
	        model.addAttribute("returnDate", returnDate);
	        model.addAttribute("penaltyAmount", penaltyAmount);

	        // Erfolgreiche Rückgabe abgeschlossen
	        return "returnMediaSuccess";
	    } catch (Exception e) {
	        addErrorMessage("Error processing media return: " + e.getMessage(), model);
	        return "redirect:/returnMedia";
	    }
	}

	// Hilfsmethode zur Berechnung der Mahngebühr
	private double calculatePenalty(MediaTransaction transaction) {
	    long overdueDays = (transaction.getReturnDate().getTime() - transaction.getLast_possible_return_date().getTime()) / (1000 * 60 * 60 * 24);
	    return overdueDays > 0 ? overdueDays * 1.0 : 0.0; // Gebühr von 1€ pro verspätetem Tag
	}

}
