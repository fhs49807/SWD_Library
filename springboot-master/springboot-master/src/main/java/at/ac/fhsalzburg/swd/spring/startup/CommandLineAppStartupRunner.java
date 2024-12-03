package at.ac.fhsalzburg.swd.spring.startup;

import at.ac.fhsalzburg.swd.spring.model.*;
import at.ac.fhsalzburg.swd.spring.repository.EditionRepository;
import at.ac.fhsalzburg.swd.spring.repository.GenreRepository;
import at.ac.fhsalzburg.swd.spring.repository.MediaTypeRepository;
import at.ac.fhsalzburg.swd.spring.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Profile("!test")
@Component
public class CommandLineAppStartupRunner implements CommandLineRunner {
	@Autowired
	UserServiceInterface userService;

	@Autowired
	ProductServiceInterface productService;

	@Autowired
	OrderServiceInterface orderService;

	// -------------------------------------------------

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private MediaTypeRepository mediaTypeRepository;

	@Autowired
	private EditionRepository editionRepository;

	@Autowired
	private LibraryService libraryService;

	@Autowired
	private MediaService mediaService;

	@Autowired
	private MediaTransactionServiceInterface mediaTransactionService;

	@Autowired
	private ReserveMediaTransactionServiceInterface reserveMediaTransactionService;

    private final Scanner scanner = new Scanner(System.in);

    // Initialize System with preset accounts and stocks
	@Override
	@Transactional // this method runs within one database transaction; performing a commit at the
	// end
	public void run(String... args) throws Exception {

		if (userService.getByUsername("admin") != null)
			return; // data already exists -> return

		int loanLimitRegular = 28; //customer max. loan time (4 weeks)
		int loanLimitStudent = 42; //students max. loan time (6 weeks)
		
		
		userService.addUser("admin", "Administrator", "admin@work.org", "123", new Date(), "admin", "ADMIN",
				User.CustomerType.REGULAR, loanLimitRegular);

		productService.addProduct("first product", 3.30f);
		User user = userService.getAll().iterator().next();
		user.setCredit(100l);
		user = userService.getByUsername("admin");
		orderService.addOrder(new Date(), user, productService.getAll());

		// Create sample customer
		boolean newCustomer = userService.addUser("john", "John Doe", "john.doe@example.com", "123456789", new Date(),//TODO: change birthday for FSK check
				"pw", "Customer", User.CustomerType.STUDENT, loanLimitStudent);
		if (newCustomer) {
			System.out.println("User created successfully.");
		} else {
			System.out.println("Failed to create user.");
		}

		createLibraryWithMedia();

//		returnMediaSimulation();
//		loanMediaSimulation();

//		reserveMediaSimulation();
	}

// TODO: remove and move to unit tests
//    ---------------------------------------------------

//	private void loanMediaSimulation() {
//
//		User existingCustomer = userService.getByUsername("john.doe");
//
//		// find available editions for loan
//		Collection<Media> mediaItems = libraryService.validateMedia("10"); // "Harry Potter"
//		if (!mediaItems.isEmpty()) {
//			Media media = mediaItems.iterator().next();
//			Collection<Edition> availableEditions = libraryService.checkForAvailableEditions(media);
//
//			// if there are available editions
//			if (!availableEditions.isEmpty()) {
//				// get the edition ids
//				Collection<Long> editionIds = availableEditions.stream().map(Edition::getId).limit(1)
//						.collect(Collectors.toList());
//
//				// set due date (14 days from today)
//				Date dueDate = new Date(System.currentTimeMillis() + (14L * 24 * 60 * 60 * 1000));
//
//				// run loanMedia method
//				MediaTransaction transaction = mediaTransactionService.loanMedia(existingCustomer.getUsername(),
//						editionIds, dueDate);
//				System.out.println("Media loaned with ID: " + transaction.getId());
//			} else {
//				System.out.println("No available editions!");
//			}
//		} else {
//			System.out.println("No media found to loan!");
//		}
//	}

//	private void reserveMediaSimulation() {
//        User user = userService.getByUsername("john");
//
//        System.out.println("What do you want to reserve?");
//        String mediaName = scanner.nextLine();
//
//        System.out.println("When do you want to reserve " + mediaName + "? (yyyy-MM-dd)");
//        Date reserveStartDate = DateUtils.getDateFromString(scanner.nextLine());
//
//        reserveMediaTransactionService.reserveMediaForCustomer(user, mediaName, reserveStartDate);
//	}
//
//	private void returnMediaSimulation() {
//		// simuliert zurückgebn von ausleihe
//		User user = userService.getByUsername("john.doe");
//		Collection<MediaTransaction> loans = mediaTransactionService.findLoansByUser(user);
//		if (!loans.isEmpty()) {
//			MediaTransaction transaction = loans.iterator().next();
//			mediaTransactionService.returnMedia(transaction.getId());
//			System.out.println("Media returned for transaction ID: " + transaction.getId());
//		}
//	}

//  ---------------------------------------------------

	private void createLibraryWithMedia() {
		// Create sections and shelves for the library
		Section sectionA = new Section("Section A");
		Section sectionB = new Section("Section B");
		Shelf shelfA1 = new Shelf(1, sectionA);
		Shelf shelfB1 = new Shelf(1, sectionB);

		// Assign shelves to sections
		sectionA.setShelves(List.of(shelfA1));
		sectionB.setShelves(List.of(shelfB1));

		// Create the library and save it with sections
		Library library = new Library(null, "Central Library", "123 Library St", Arrays.asList(sectionA, sectionB));
		libraryService.save(library);
		System.out.println("Library created: " + library);

		// Create a genre and media type for media items
		// Create and save the genre and media type
		Genre scienceFictionGenre = new Genre();
		scienceFictionGenre.setName("Science Fiction");
		scienceFictionGenre.setPrice(10.0);
		scienceFictionGenre = genreRepository.save(scienceFictionGenre);

		Genre fantasyGenre = new Genre();
		fantasyGenre.setName("Fantasy Genre");
		fantasyGenre.setPrice(12.0);
		fantasyGenre = genreRepository.save(fantasyGenre);

		Genre thrillerGenre = new Genre();
		thrillerGenre.setName("Thriller");
		thrillerGenre.setPrice(15.0);
		thrillerGenre = genreRepository.save(thrillerGenre);

		// Create media types
		MediaType mediaTypeBook = new MediaType();
		mediaTypeBook.setType("Book");
		mediaTypeBook = mediaTypeRepository.save(mediaTypeBook);

		MediaType mediaTypeAudio = new MediaType();
		mediaTypeAudio.setType("Audio");
		mediaTypeAudio = mediaTypeRepository.save(mediaTypeAudio);

		MediaType mediaTypeMovie = new MediaType();
		mediaTypeMovie.setType("Movie");
		mediaTypeMovie = mediaTypeRepository.save(mediaTypeMovie);

		// Create and add books
		Book dune = new Book("55231", "Dune", scienceFictionGenre, mediaTypeBook, shelfA1, 0);
		mediaService.addMedia(dune);

		Book harryPotter = new Book("234234", "Harry Potter", fantasyGenre, mediaTypeBook, shelfB1, 0);
		mediaService.addMedia(harryPotter);

		Book goneGirl = new Book("9780307588371", "Gone Girl", thrillerGenre, mediaTypeBook, shelfA1, 18);
		mediaService.addMedia(goneGirl);

		
		// Create and add audios
		Audio duneAudio = new Audio("AAC", "Dune Audiobook", scienceFictionGenre, mediaTypeAudio, shelfA1, 0);
		mediaService.addMedia(duneAudio);

		Audio harryPotterAudio = new Audio("MP3", "Harry Potter Audiobook", fantasyGenre, mediaTypeAudio, shelfB1, 0);
		mediaService.addMedia(harryPotterAudio);

		Audio goneGirlAudio = new Audio("WAV", "Gone Girl Audiobook", thrillerGenre, mediaTypeAudio, shelfA1, 18);
		mediaService.addMedia(goneGirlAudio);

		
		// Create and add movies
		Movie duneMovie = new Movie("tt0816692", "Dune Movie", scienceFictionGenre, mediaTypeMovie, shelfA1, 0);
		mediaService.addMedia(duneMovie);

		Movie harryPotterMovie = new Movie("tt0241527", "Harry Potter Movie", fantasyGenre, mediaTypeMovie, shelfB1, 0);
		mediaService.addMedia(harryPotterMovie);

		Movie goneGirlMovie = new Movie("tt2267998", "Gone Girl Movie", thrillerGenre, mediaTypeMovie, shelfA1, 18);
		mediaService.addMedia(goneGirlMovie);

		System.out.println("Library with media created successfully.");

	}

}
