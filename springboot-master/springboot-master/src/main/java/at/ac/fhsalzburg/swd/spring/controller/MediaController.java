package at.ac.fhsalzburg.swd.spring.controller;

import at.ac.fhsalzburg.swd.spring.dto.MediaTransactionDTO;
import at.ac.fhsalzburg.swd.spring.model.*;
import at.ac.fhsalzburg.swd.spring.services.LibraryService;
import at.ac.fhsalzburg.swd.spring.services.MediaServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

@Controller
public class MediaController extends BaseController {

	@Autowired
	private MediaServiceInterface mediaService;

	@Autowired
	private UserServiceInterface userService;

	@Autowired
	private MediaTransactionServiceInterface mediaTransactionService;

	@Autowired
	private LibraryService libraryService;

	@RequestMapping(value = "/loanReserveMedia", method = RequestMethod.POST)
	public String loanReserveMedia(@RequestParam(required = false) Long mediaId,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start_date,
		@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end_date,
		@RequestParam(required = false) String selectedGenre, @RequestParam(required = false) String selectedType,
		@CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {

		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String username = authentication.getName();

			// Validate required fields
			if (mediaId == null || start_date == null || end_date == null) {
				return handleError("Please select a media item and specify the reserve date period.", selectedGenre,
					selectedType, username, model);
			}

			// Ensure start_date is before or equal to end_date
			if (start_date.isAfter(end_date)) {
				return handleError("Start date must be on or before the end date.", selectedGenre, selectedType,
					username, model);
			}

			try {
				// If start_date is after today, process reservation
				if (start_date.isAfter(LocalDate.now())) {
					mediaTransactionService.reserveMediaForCustomer(username, mediaId, start_date, end_date);
					populateReservationSuccessModel(username, mediaId, start_date, end_date, model);
					return "reservationSuccess"; // Prevent loan creation during reservation
				} else {
					// Process loan if start_date is today or earlier
					MediaTransaction transaction = mediaTransactionService.loanMedia(username, mediaId, end_date);
					populateLoanSuccessModel(transaction, model);
					return "loanSuccess";
				}
			} catch (Exception e) {
				e.printStackTrace();
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

		// Verleihungen und Reservierungen einfügen, falls vorhanden
		fetchUserLoans(model, user);
		fetchUserReservations(model, user);

		return "returnMedia"; // Zeigt die Rückgabeseite an, wenn der Benutzer eingeloggt ist
	}

	@PostMapping("/returnMedia")
	public String returnMedia(@RequestParam Long transactionId, Model model) {
		try {
			mediaTransactionService.returnMedia(transactionId);

			// Redirect with transactionId
			return "redirect:/returnMediaSuccess?transactionId=" + transactionId;
		} catch (Exception e) {
			addErrorMessage("Error returning media: " + e.getMessage(), model);
			return "redirect:/returnMedia";
		}
	}

	@PostMapping("/cancelReservation")
	public String cancelReservation(@RequestParam Long reservationId, Model model) {
		mediaTransactionService.cancelReservation(reservationId);
		addSuccessMessage("Media returned successfully.", model);

		return "redirect:/returnMedia";
	}

	// populate reservation success page
	private void populateReservationSuccessModel(String username, Long mediaId, LocalDate start_date,
		LocalDate end_date, Model model) {
		model.addAttribute("username", username);
		model.addAttribute("reservation_date", new Date()); // Current date as reservation processing date
		model.addAttribute("start_date", start_date);
		model.addAttribute("end_date", end_date);

		Media media = mediaService.findById(mediaId);
		model.addAttribute("mediaId", mediaId);
		model.addAttribute("mediaTitle", media.getName());
		model.addAttribute("mediaGenre", media.getGenre().getName());
		model.addAttribute("mediaType", media.getMediaType().getType());

		// Retrieve the edition ID for reservation
		MediaTransaction reservation = mediaTransactionService.getLatestReservation(mediaId, username);
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
		model.addAttribute("shelfNumber", shelf.getId());
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
		List<String> genres = addDefaultOption(libraryService.getAllGenres(), "All Genres");
		List<String> mediaTypes = addDefaultOption(libraryService.getAllMediaTypes(), "All Media Types");

		model.addAttribute("genres", genres);
		model.addAttribute("mediaTypes", mediaTypes);
		model.addAttribute("selectedGenre", selectedGenre);
		model.addAttribute("selectedType", selectedType);
		model.addAttribute("mediaList", mediaService.searchMediaByGenreAndType(selectedGenre, selectedType,
			userService.getByUsername(username)));
		model.addAttribute("todayDate", LocalDate.now());
		model.addAttribute("endDate", LocalDate.now().plusDays(1));

		addErrorMessage(errorMessage, model);
		return "loan";
	}

	// Add an empty/default option to the list of genres and media types
	private List<String> addDefaultOption(List<String> options, String defaultLabel) {
		options.add(0, defaultLabel); // Add an empty option at the beginning
		return options;
	}

	private void fetchUserLoans(Model model, User user) {
		// Nur aktive Transaktionen anzeigen (status != COMPLETED)
		List<MediaTransactionDTO> loanDTOs = new ArrayList<>();
		for (MediaTransaction transaction : mediaTransactionService.findLoansByUser(user)) {
			MediaTransactionDTO dto = new MediaTransactionDTO(transaction.getId(),
				transaction.getEdition().getMedia().getName(), null,
				transaction.getReturnDate());
			loanDTOs.add(dto);
		}
		model.addAttribute("loans", loanDTOs);
	}

	private void fetchUserReservations(Model model, User user) {
		List<MediaTransactionDTO> mediaTransactionDTOs = new ArrayList<>();
		for (MediaTransaction transaction : mediaTransactionService.findReservationsForUser(user)) {
			MediaTransactionDTO dto = new MediaTransactionDTO(transaction.getId(),
				transaction.getEdition().getMedia().getName(), transaction.getReserveStartDate(),
				transaction.getReserveEndDate());
			mediaTransactionDTOs.add(dto);
		}
		model.addAttribute("reservations", mediaTransactionDTOs);
	}

	@GetMapping("/returnMediaSuccess")
	public String returnMediaSuccess(@RequestParam Long transactionId, Model model) {
		// Handle success view logic here
		try {
			MediaTransaction transaction = mediaTransactionService.findById(transactionId);

			if (transaction == null) {
				addErrorMessage("Media transaction not found.", model);
				return "redirect:/returnMedia";
			}

			// Fill the model with success details
			double penaltyAmount = calculatePenalty(transaction);
			model.addAttribute("username", transaction.getUser().getUsername());
			model.addAttribute("transaction_date", transaction.getStart_date());
			model.addAttribute("mediaTitle", transaction.getEdition().getMedia().getName());
			model.addAttribute("mediaGenre", transaction.getEdition().getMedia().getGenre().getName());
			model.addAttribute("mediaType", transaction.getEdition().getMedia().getMediaType().getType());
			model.addAttribute("returnDate", new Date());
			model.addAttribute("penaltyAmount", penaltyAmount);

			return "returnMediaSuccess";
		} catch (Exception e) {
			addErrorMessage("Error processing media return: " + e.getMessage(), model);
			return "redirect:/returnMedia";
		}
	}

	// Hilfsmethode zur Berechnung der Mahngebühr
	private double calculatePenalty(MediaTransaction transaction) {
		long overdueDays = DAYS.between(transaction.getReturnDate(), transaction.getLast_possible_return_date());
		return overdueDays > 0 ? overdueDays * 1.0 : 0.0; // Gebühr von 1€ pro verspätetem Tag
	}

}
