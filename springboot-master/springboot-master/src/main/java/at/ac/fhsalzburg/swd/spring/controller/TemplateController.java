package at.ac.fhsalzburg.swd.spring.controller;

import at.ac.fhsalzburg.swd.spring.TestBean;
import at.ac.fhsalzburg.swd.spring.dto.UserDTO;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.services.MediaService;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionService;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;
import at.ac.fhsalzburg.swd.spring.util.ObjectMapperUtils;
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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

	// Add an empty/default option to the list of genres and media types
	private List<String> addDefaultOption(List<String> options, String defaultLabel) {
		options.add(0, defaultLabel); // Add an empty option at the beginning
		return options;
	}

	@RequestMapping(value = "/loan", method = RequestMethod.GET)
	public String showLoanPage(
			@RequestParam(value = "selectedGenre", required = false, defaultValue = "") String selectedGenre,
			@RequestParam(value = "selectedType", required = false, defaultValue = "") String selectedType, Model model,
			@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String username = authentication.getName();
			User user = userService.getByUsername(username);

			LocalDate todayDate = LocalDate.now();
			LocalDate endDate = todayDate.plusDays(1);

			// Fetch genres and media types with "All" as default
			List<String> genres = addDefaultOption(
					StreamSupport.stream(mediaService.getAllGenres().spliterator(), false).collect(Collectors.toList()),
					"All Genres");
			List<String> mediaTypes = addDefaultOption(StreamSupport
					.stream(mediaService.getAllMediaTypes().spliterator(), false).collect(Collectors.toList()),
					"All Media Types");

			model.addAttribute("genres", genres);
			model.addAttribute("mediaTypes", mediaTypes);
			model.addAttribute("selectedGenre", selectedGenre);
			model.addAttribute("selectedType", selectedType);
			model.addAttribute("todayDate", todayDate);
			model.addAttribute("endDate", endDate);

			Iterable<Media> mediaList = mediaService.searchMediaByGenreAndType(selectedGenre, selectedType, user);
			if (!mediaList.iterator().hasNext()) {
				model.addAttribute("errorMessage", "No media found for the selected Genre and Media Type.");
			}
			model.addAttribute("mediaList", mediaList);

			return "loan";
		}

		model.addAttribute("errorMessage", "You must log in to view the loan page.");
		return "login";
	}

	@GetMapping("/searchMedia")
	public String searchMedia(@RequestParam(value = "genre", required = true) String genre,
			@RequestParam(value = "type", required = true) String type, Model model,
			@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String username = authentication.getName();
			User user = userService.getByUsername(username);

			LocalDate todayDate = LocalDate.now();
			LocalDate endDate = todayDate.plusDays(1);

			// Fetch genres and media types with "All" as default
			List<String> genres = addDefaultOption(
					StreamSupport.stream(mediaService.getAllGenres().spliterator(), false).collect(Collectors.toList()),
					"All Genres");
			List<String> mediaTypes = addDefaultOption(StreamSupport
					.stream(mediaService.getAllMediaTypes().spliterator(), false).collect(Collectors.toList()),
					"All Media Types");

			Iterable<Media> mediaList = mediaService.searchMediaByGenreAndType(genre, type, user);
			model.addAttribute("genres", genres);
			model.addAttribute("mediaTypes", mediaTypes);
			model.addAttribute("selectedGenre", genre);
			model.addAttribute("selectedType", type);
			model.addAttribute("todayDate", todayDate);
			model.addAttribute("endDate", endDate);

			if (!mediaList.iterator().hasNext()) {
				model.addAttribute("errorMessage", "No media found for the selected Genre and Media Type.");
			}
			model.addAttribute("mediaList", mediaList);

			return "loan";
		}

		model.addAttribute("errorMessage", "You must be logged in to search for media.");
		return "login";
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

}
