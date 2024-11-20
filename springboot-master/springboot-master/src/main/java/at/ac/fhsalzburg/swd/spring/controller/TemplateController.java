package at.ac.fhsalzburg.swd.spring.controller;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import at.ac.fhsalzburg.swd.spring.TestBean;
import at.ac.fhsalzburg.swd.spring.dto.UserDTO;
import at.ac.fhsalzburg.swd.spring.model.Genre;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.MediaType;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.services.MediaService;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionService;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;
import at.ac.fhsalzburg.swd.spring.util.ObjectMapperUtils;

@Controller // marks the class as a web controller, capable of handling the HTTP requests.
			// Spring
			// will look at the methods of the class marked with the @Controller annotation
			// and
			// establish the routing table to know which methods serve which endpoints.
public class TemplateController {

	Logger logger = LoggerFactory.getLogger(TemplateController.class);

	// Dependency Injection
	// ----------------------------------------------------------------------

	@Autowired // To wire the application parts together, use @Autowired on the fields,
				// constructors, or methods in a component. Spring's dependency injection
				// mechanism
				// wires appropriate beans into the class members marked with @Autowired.
	private ApplicationContext context;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	UserServiceInterface userService;

	@Autowired
	MediaService mediaService;

	@Autowired
	MediaTransactionService mediaTransactionService;

	@Resource(name = "sessionBean") // The @Resource annotation is part of the JSR-250 annotation
									// collection and is packaged with Jakarta EE. This annotation
									// has the following execution paths, listed by Match by Name,
									// Match by Type, Match by Qualifier. These execution paths are
									// applicable to both setter and field injection.
									// https://www.baeldung.com/spring-annotations-resource-inject-autowire
	TestBean sessionBean;

	@Autowired
	TestBean singletonBean;

	// HTTP Request Mappings GET/POST/... and URL Paths
	// ----------------------------------------------------------------------

	@RequestMapping("/") // The @RequestMapping(method = RequestMethod.GET, value = "/path")
							// annotation specifies a method in the controller that should be
							// responsible for serving the HTTP request to the given path. Spring will
							// work the implementation details of how it's done. You simply specify the
							// path value on the annotation and Spring will route the requests into the
							// correct action methods:
							// https://springframework.guru/spring-requestmapping-annotation/#:~:text=%40RequestMapping%20is%20one%20of%20the,map%20Spring%20MVC%20controller%20methods.
	public String index(Model model, HttpSession session,
			@CurrentSecurityContext(expression = "authentication") Authentication authentication) {

		logger.info("index called");

		if (session == null) {
			model.addAttribute("message", "no session");
		} else {
			Integer count = (Integer) session.getAttribute("count");
			if (count == null) {
				count = Integer.valueOf(0);
			}
			count++;
			session.setAttribute("count", count);
		}

		// check if user is logged in
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			model.addAttribute("user", currentUserName);
		}

		model.addAttribute("message", userService.doSomething());

		model.addAttribute("halloNachricht", "welchem to SWD lab");

		// map list of entities to list of DTOs
		List<UserDTO> listOfUserTO = ObjectMapperUtils.mapAll(userService.getAll(), UserDTO.class);

		model.addAttribute("users", listOfUserTO);

		model.addAttribute("beanSingleton", singletonBean.getHashCode());

		TestBean prototypeBean = context.getBean("prototypeBean", TestBean.class);
		model.addAttribute("beanPrototype", prototypeBean.getHashCode());

		model.addAttribute("beanSession", sessionBean.getHashCode());

		Authentication lauthentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("authenticated", lauthentication);

		return "index";
	}

	@RequestMapping(value = { "/login" })
	public String login(Model model) {
		logger.info("login called");
		return "login";
	}

	// TODO: add "/media" to view all mediums in library
	// TODO: add "/editions" to show editions for specific medium
	// TODO: add "/media/add,update,delete"??

	// TODO: add "/loan" to loan exemplar to customer
	// TODO: add "/return" to return medium back
	// TODO: add "/reserve" to reserve medium

	// TODO: add "/invoices" to get all unpaid invoices for customer
	// TODO: add "/pay" to pay outstanding balances

	@RequestMapping(value = "/loan", method = RequestMethod.GET)
	public String showLoanPage(Model model) {
		model.addAttribute("genres", mediaService.getAllGenres());
		model.addAttribute("mediaTypes", mediaService.getAllMediaTypes());
		model.addAttribute("mediaList", mediaService.searchMediaByGenreAndType("defaultGenre", "defaultType"));
		return "loan";
	}

	@GetMapping("/searchMedia")
	public String searchMedia(@RequestParam String genre, @RequestParam String type, Model model) {

		model.addAttribute("genres", mediaService.getAllGenres());
		model.addAttribute("mediaTypes", mediaService.getAllMediaTypes());

		Iterable<Media> mediaList = mediaService.searchMediaByGenreAndType(genre, type);//TODO: add fsk
		model.addAttribute("mediaList", mediaList);
		return "loan";
	}

	@RequestMapping(value = "/loanMedia", method = RequestMethod.POST)
	public String loanMedia(@RequestParam Long mediaId, @RequestParam String loanDate,
	        @CurrentSecurityContext(expression = "authentication") Authentication authentication, Model model) {
	    if (!(authentication instanceof AnonymousAuthenticationToken)) {
	        String username = authentication.getName();
	        try {
	            Date parsedLoanDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(loanDate);

	            // Attempt to loan media
	            mediaTransactionService.loanMedia(username, Collections.singletonList(mediaId), parsedLoanDate);

	            model.addAttribute("successMessage", "Loan created successfully!");
	        } catch (IllegalStateException e) {
	            model.addAttribute("errorMessage", e.getMessage()); // User already has the media on loan
	        } catch (Exception e) {
	            model.addAttribute("errorMessage", "Error creating loan: " + e.getMessage());
	        }
	    } else {
	        model.addAttribute("errorMessage", "You must log in to loan media.");
	    }
	 // Populate genres and media types to re-render the loan page properly
	    model.addAttribute("genres", mediaService.getAllGenres());
	    model.addAttribute("mediaTypes", mediaService.getAllMediaTypes());
	    model.addAttribute("mediaList", Collections.emptyList()); // Or keep mediaList populated if needed

	    return "loan"; // Return to loan page with feedback
	}


	@RequestMapping(value = { "/login-error" })
	public String loginError(Model model) {
		logger.info("loginError called");
		model.addAttribute("error", "Login error");
		return "login";
	}

	@RequestMapping(value = { "/admin/addUser" }, method = RequestMethod.GET)
	public String showAddPersonPage(Model model, @RequestParam(value = "username", required = false) String username) {
		logger.info("showAddPersonPage called");
		User modUser = null;
		UserDTO userDto = new UserDTO();

		if (username != null) {
			modUser = userService.getByUsername(username);
		}

		if (modUser != null) {
			// map user to userDTO
			userDto = ObjectMapperUtils.map(modUser, UserDTO.class);
		} else {
			userDto = new UserDTO();
		}

		model.addAttribute("user", userDto);

		return "addUser";
	}

	@RequestMapping(value = { "/admin/addUser" }, method = RequestMethod.POST)
	public String addUser(Model model, //
			@ModelAttribute("UserForm") UserDTO userDTO) { // The @ModelAttribute is
															// an annotation that binds
															// a method parameter or
															// method return value to a
															// named model attribute
															// and then exposes it to a
															// web view:

		logger.info("addUser called");

		// merge instances
		User user = ObjectMapperUtils.map(userDTO, User.class);

		// if user already existed in DB, new information is already merged and saved
		// a new user must be persisted (because not managed by entityManager yet)
		if (!entityManager.contains(user))
			userService.addUser(user);

		return "redirect:/";
	}
	
	@GetMapping("/returnMedia") // definiert die http get-anforderung, um die seite zur medienrückgabe anzuzeigen; url = "/returnMedia"
	public String showReturnMediaPage(Model model, @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
	    if (!(authentication instanceof AnonymousAuthenticationToken)) { // prüft, ob der benutzer authentifiziert ist und keine anonyme authentifizierung vorliegt
	        String username = authentication.getName(); // holt den benutzernamen des aktuell angemeldeten benutzers
	        User user = userService.getByUsername(username); // holt den benutzer anhand des benutzernamens aus der datenbank
	        Collection<MediaTransaction> loans = mediaTransactionService.findLoansByUser(user); // holt alle derzeitigen ausleihen des benutzers
	        model.addAttribute("loans", loans); // fügt die ausleihen zur modellattributliste hinzu, um sie in der view darzustellen
	    }
	    return "returnMedia"; // gibt den namen der html-seite zurück, die angezeigt werden soll ("returnMedia.html")
	}

	@PostMapping("/returnMedia") // post-mapping zur verarbeitung der rückgabe von medien
	public String returnMedia(@RequestParam Long transactionId, Model model) { // methode zum zurückgeben von medien, nimmt die transaktions-id als parameter
	    try {
	        mediaTransactionService.returnMedia(transactionId); // aufruf der service-methode, um das medium zurückzugeben
	        model.addAttribute("successMessage", "Media returned successfully."); // nachricht hinzufügen, dass die rückgabe erfolgreich war
	    } catch (Exception e) { // fängt eine ausnahme, falls etwas schiefgeht
	        model.addAttribute("errorMessage", "Error returning media: " + e.getMessage()); // nachricht hinzufügen, wenn ein fehler bei der rückgabe aufgetreten ist
	    }
	    return "redirect:/returnMedia"; // nach der bearbeitung wird zur rückgabeseite umgeleitet, um die aktualisierte ansicht anzuzeigen
	}

}
