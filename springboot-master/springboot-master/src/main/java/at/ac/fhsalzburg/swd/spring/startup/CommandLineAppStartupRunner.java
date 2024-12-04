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

		int loanLimitRegular = 28; // customer max. loan time (4 weeks)
		int loanLimitStudent = 42; // students max. loan time (6 weeks)

		// create regular admin user (Age 0)
		// username: admin
		// password: admin
		userService.addUser("admin", "Administrator", "admin@work.org", "123", new Date(), "admin", "ADMIN",
				User.CustomerType.REGULAR, loanLimitRegular);

		// Child/Youth Student customer (Age 15)
		// username: John
		// password: pw
		userService.addUser("john", "TestStudent", "john@email.com", "111", getBirthDate(-15), "pw", "Student",
				User.CustomerType.STUDENT, loanLimitStudent);

		// Adult customer (Age 30)
		// username: Jane
		// password: pw
		userService.addUser("jane", "TestRegular", "jane@email.com", "222", getBirthDate(-30), "pw", "Regular Customer",
				User.CustomerType.REGULAR, loanLimitRegular);

		// Student customer (Age 22)
		// username: Joe
		// password: pw
		userService.addUser("joe", "TestStudent", "joe@email.com", "333", getBirthDate(-22), "pw", "Student",
				User.CustomerType.STUDENT, loanLimitStudent);

		productService.addProduct("first product", 3.30f);
		User user = userService.getAll().iterator().next();
		user.setCredit(100l);
		user = userService.getByUsername("admin");
		orderService.addOrder(new Date(), user, productService.getAll());

		createLibraryWithMedia();

	}

	// Helper method to calculate birth date based on age
	private Date getBirthDate(int yearsAgo) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, yearsAgo);
		return calendar.getTime();
	}

	private void createLibraryWithMedia() {
	    // Create sections
	    Section fantasySection = new Section("Fantasy");
	    Section scienceFictionSection = new Section("Science Fiction");
	    Section thrillerSection = new Section("Thriller");

	    // Create shelves for each section
	    Shelf shelfF1 = new Shelf(1, fantasySection);
	    Shelf shelfF2 = new Shelf(2, fantasySection);
	    Shelf shelfF3 = new Shelf(3, fantasySection);

	    Shelf shelfSF1 = new Shelf(1, scienceFictionSection);
	    Shelf shelfSF2 = new Shelf(2, scienceFictionSection);
	    Shelf shelfSF3 = new Shelf(3, scienceFictionSection);

	    Shelf shelfT1 = new Shelf(1, thrillerSection);
	    Shelf shelfT2 = new Shelf(2, thrillerSection);
	    Shelf shelfT3 = new Shelf(3, thrillerSection);

	    // Assign shelves to sections
	    fantasySection.setShelves(List.of(shelfF1, shelfF2, shelfF3));
	    scienceFictionSection.setShelves(List.of(shelfSF1, shelfSF2, shelfSF3));
	    thrillerSection.setShelves(List.of(shelfT1, shelfT2, shelfT3));

	    // Create the library
	    Library library = new Library(null, "Central Library", "123 Library St",
	            List.of(fantasySection, scienceFictionSection, thrillerSection));

	    // Associate each section with the library
	    fantasySection.setLibrary(library);
	    scienceFictionSection.setLibrary(library);
	    thrillerSection.setLibrary(library);

	    // Save the library with its sections
	    libraryService.save(library);

	    // Create and save genres
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

	    // Create and save media types
	    MediaType mediaTypeBook = mediaTypeRepository.save(new MediaType("Book"));
	    MediaType mediaTypeAudio = mediaTypeRepository.save(new MediaType("Audio"));
	    MediaType mediaTypeMovie = mediaTypeRepository.save(new MediaType("Movie"));

	    // Create and add books
	    mediaService.addMedia(new Book("55231", "Dune", scienceFictionGenre, mediaTypeBook, shelfF1, 0));
	    mediaService.addMedia(new Book("23423", "Harry Potter", fantasyGenre, mediaTypeBook, shelfF2, 0));
	    mediaService.addMedia(new Book("97802", "Gone Girl", thrillerGenre, mediaTypeBook, shelfT1, 18));
	    mediaService.addMedia(new Book("83729", "The Hobbit", fantasyGenre, mediaTypeBook, shelfF3, 0));
	    mediaService.addMedia(new Book("45231", "1984", scienceFictionGenre, mediaTypeBook, shelfSF2, 0));
	    mediaService.addMedia(new Book("98321", "The Da Vinci Code", thrillerGenre, mediaTypeBook, shelfT2, 18));

	    // Create and add audios
	    mediaService.addMedia(new Audio("AAC", "Dune Audiobook", scienceFictionGenre, mediaTypeAudio, shelfF1, 0));
	    mediaService.addMedia(new Audio("MP3", "Harry Potter Audiobook", fantasyGenre, mediaTypeAudio, shelfF2, 0));
	    mediaService.addMedia(new Audio("WAV", "Gone Girl Audiobook", thrillerGenre, mediaTypeAudio, shelfT1, 18));
	    mediaService.addMedia(new Audio("FLAC", "The Hobbit Audiobook", fantasyGenre, mediaTypeAudio, shelfF3, 0));
	    mediaService.addMedia(new Audio("MP3", "World Audiobook", scienceFictionGenre, mediaTypeAudio, shelfSF3, 0));
	    mediaService.addMedia(new Audio("ALAC", "The Shining Audiobook", thrillerGenre, mediaTypeAudio, shelfT3, 18));

	    // Create and add movies
	    mediaService.addMedia(new Movie("tt0816692", "Dune Movie", scienceFictionGenre, mediaTypeMovie, shelfF1, 0));
	    mediaService.addMedia(new Movie("tt0241527", "Harry Potter Movie", fantasyGenre, mediaTypeMovie, shelfF2, 0));
	    mediaService.addMedia(new Movie("tt2267998", "Gone Girl Movie", thrillerGenre, mediaTypeMovie, shelfT1, 18));
	    mediaService.addMedia(new Movie("tt0120737", "The Hobbit Movie", fantasyGenre, mediaTypeMovie, shelfF3, 0));
	    mediaService.addMedia(new Movie("tt0078748", "Alien", scienceFictionGenre, mediaTypeMovie, shelfSF1, 0));
	    mediaService.addMedia(new Movie("tt0102926", "The Silence", thrillerGenre, mediaTypeMovie, shelfT3, 18));
	}

}
