package at.ac.fhsalzburg.swd.spring.test;

import at.ac.fhsalzburg.swd.spring.enums.CustomerType;
import at.ac.fhsalzburg.swd.spring.enums.TransactionStatus;
import at.ac.fhsalzburg.swd.spring.model.*;
import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;
import at.ac.fhsalzburg.swd.spring.services.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { LoanMediaConcurrencyTest.TestConfig.class })
public class LoanMediaConcurrencyTest {

	@Configuration
	@EnableTransactionManagement
	static class TestConfig {

		@Bean
		public LocalContainerEntityManagerFactoryBean entityManagerFactory(JpaVendorAdapter jpaVendorAdapter,
				DataSource dataSource) {
			LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
			factoryBean.setPersistenceUnitName("testPU");
			factoryBean.setDataSource(dataSource);
			factoryBean.setPackagesToScan("at.ac.fhsalzburg.swd.spring.model");
			factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
			factoryBean.setJpaProperties(additionalJpaProperties());
			return factoryBean;
		}

		@Bean
		public JpaVendorAdapter jpaVendorAdapter() {
			HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
			adapter.setDatabasePlatform("org.hibernate.dialect.H2Dialect");
			adapter.setShowSql(true);
			adapter.setGenerateDdl(true);
			return adapter;
		}

		@Bean
		public DataSource h2DataSource() {
			org.springframework.jdbc.datasource.DriverManagerDataSource ds = new org.springframework.jdbc.datasource.DriverManagerDataSource();
			ds.setDriverClassName("org.h2.Driver");
			ds.setUrl("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false");
			ds.setUsername("sa");
			ds.setPassword("");
			return ds;
		}

		@Bean
		public PlatformTransactionManager transactionManager(
				LocalContainerEntityManagerFactoryBean entityManagerFactory) {
			JpaTransactionManager tm = new JpaTransactionManager();
			tm.setEntityManagerFactory(entityManagerFactory.getObject());
			return tm;
		}

		private Properties additionalJpaProperties() {
			Properties properties = new Properties();
			properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
			properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
			return properties;
		}

		@Bean
		public MediaServiceInterface mediaService() {
			return Mockito.mock(MediaServiceInterface.class);
		}

		@Bean
		public EditionServiceInterface editionService() {
			return Mockito.mock(EditionServiceInterface.class);
		}

		@Bean
		public UserServiceInterface userService() {
			return Mockito.mock(UserServiceInterface.class);
		}

		@Bean
		public MediaTransactionRepository mediaTransactionRepository() {
			return Mockito.mock(MediaTransactionRepository.class);
		}

		@Bean
		public InvoiceServiceInterface invoiceService() {
			return Mockito.mock(InvoiceServiceInterface.class);
		}

		@Bean
		public MediaTransactionServiceInterface mediaTransactionService(
				MediaTransactionRepository mediaTransactionRepository, MediaServiceInterface mediaService,
				EditionServiceInterface editionService, InvoiceServiceInterface invoiceService,
				UserServiceInterface userService) {
			return new MediaTransactionService(mediaTransactionRepository, mediaService, editionService, invoiceService,
					userService);
		}
	}

	@Autowired
	private MediaTransactionServiceInterface mediaTransactionService;

	@Autowired
	private MediaServiceInterface mediaService;

	@Autowired
	private EditionServiceInterface editionService;

	@Autowired
	private UserServiceInterface userService;

	@Autowired
	private MediaTransactionRepository mediaTransactionRepository;

	@Autowired
	private InvoiceServiceInterface invoiceService;

	@Test
	public void testLoanMediaConcurrency() throws InterruptedException {
		Library library = new Library(null, "Central Library", "123 Library St", new ArrayList<>());

		Section fantasySection = new Section("Fantasy");
		Section scienceFictionSection = new Section("Science Fiction");
		Section thrillerSection = new Section("Thriller");

		fantasySection.setLibrary(library);
		scienceFictionSection.setLibrary(library);
		thrillerSection.setLibrary(library);

		Shelf shelfF1 = new Shelf(fantasySection);
		Shelf shelfF2 = new Shelf(fantasySection);
		Shelf shelfF3 = new Shelf(fantasySection);

		Shelf shelfSF1 = new Shelf(scienceFictionSection);
		Shelf shelfSF2 = new Shelf(scienceFictionSection);
		Shelf shelfSF3 = new Shelf(scienceFictionSection);

		Shelf shelfT1 = new Shelf(thrillerSection);
		Shelf shelfT2 = new Shelf(thrillerSection);
		Shelf shelfT3 = new Shelf(thrillerSection);

		fantasySection.setShelves(List.of(shelfF1, shelfF2, shelfF3));
		scienceFictionSection.setShelves(List.of(shelfSF1, shelfSF2, shelfSF3));
		thrillerSection.setShelves(List.of(shelfT1, shelfT2, shelfT3));

		library.setSections(List.of(fantasySection, scienceFictionSection, thrillerSection));

		Long mediaId = 1L;
		LocalDate endDate = LocalDate.now().plusDays(7);
		Media media = new Media();
		media.setId(mediaId);
		media.setName("Test Media");
		media.setMediaType(new MediaType("BOOK"));

		Genre genre = new Genre();
		genre.setName("Fiction");
		media.setGenre(genre);

		media.setShelf(shelfF1);

		Edition edition = new Edition();
		edition.setId(1L);
		edition.setMedia(media);

		when(mediaService.findById(mediaId)).thenReturn(media);

		when(userService.getByUsername(anyString())).thenAnswer(invocation -> {
			String username = invocation.getArgument(0);
			User newUser = new User();
			newUser.setUsername(username);
			newUser.setCustomerType(CustomerType.STUDENT);
			newUser.setCredit(100L);
			return newUser;
		});

		List<Edition> availableEditions = new ArrayList<>();
		availableEditions.add(edition);

		when(editionService.findAvailableEditions(any(Media.class), any(LocalDate.class), eq(endDate)))
				.thenAnswer(invocation -> {
					synchronized (availableEditions) {
						if (!availableEditions.isEmpty()) {
							System.out.println("Edition allocated to one user.");
							return List.of(availableEditions.remove(0));
						} else {
							System.out.println("No editions available.");
							return new ArrayList<>();
						}
					}
				});

		when(mediaTransactionRepository.save(any(MediaTransaction.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		int numberOfThreads = 5;
		ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
		CountDownLatch latch = new CountDownLatch(1);
		List<Future<Boolean>> results = new ArrayList<>();

		for (int i = 0; i < numberOfThreads; i++) {
			String username = "user" + i;
			results.add(executor.submit(() -> {
				latch.await();
				try {
					MediaTransaction transaction = mediaTransactionService.loanMedia(username, mediaId, endDate);
					System.out.println("Loan succeeded for user: " + username);
					return true;
				} catch (Exception e) {
					System.out.println("Loan failed for user: " + username + " due to " + e.getClass().getSimpleName()
							+ ": " + e.getMessage());
					return false;
				}
			}));
		}

		latch.countDown();
		executor.shutdown();
		executor.awaitTermination(10, TimeUnit.SECONDS);

		int successCount = 0;
		int failureCount = 0;

		for (Future<Boolean> result : results) {
			try {
				if (result.get()) {
					successCount++;
				} else {
					failureCount++;
				}
			} catch (ExecutionException e) {
				System.out.println("Thread encountered an exception: " + e.getCause());
				e.getCause().printStackTrace();
				failureCount++;
			}
		}

		assertEquals(1, successCount, "Only one user should successfully loan the media.");
		assertEquals(numberOfThreads - 1, failureCount, "All other users should fail to loan the media.");

		verify(mediaService, times(numberOfThreads)).findById(mediaId);
		verify(editionService, times(numberOfThreads)).findAvailableEditions(any(Media.class), any(LocalDate.class),
				eq(endDate));
		verify(userService, times(numberOfThreads)).getByUsername(anyString());
		verify(mediaTransactionRepository, times(1)).save(any(MediaTransaction.class));
	}
}
