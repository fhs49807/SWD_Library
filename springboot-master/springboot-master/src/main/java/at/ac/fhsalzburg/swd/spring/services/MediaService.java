package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.*;
import at.ac.fhsalzburg.swd.spring.repository.MediaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MediaService implements MediaServiceInterface {

	private final MediaRepository mediaRepository;
	private final EditionServiceInterface editionService;
	private final UserServiceInterface userService;

	// Constructor injection
	public MediaService(MediaRepository mediaRepository, EditionServiceInterface editionService,
			UserServiceInterface userService) {
		this.mediaRepository = mediaRepository;
		this.editionService = editionService;
		this.userService = userService;
	}

	@Override
	public Boolean addMedia(Media media) {
		if ((media.getName() != null) && (media.getName().length() > 0) && (media.getFSK() >= 0)
				&& (media.getFSK() <= 18)) {
			mediaRepository.save(media);

			// Delegate edition handling to EditionService
			editionService.addEditionsForMedia(media, 2); // Add 2 editions for each medium
			return true;
		}
		return false;
	}

	@Override
	public Collection<Media> findMediaById(long id) {
		return mediaRepository.findById(id).map(List::of).orElseGet(List::of);
	}

	@Override
	public void save(Media media) {
		mediaRepository.save(media);
	}

	@Override
	public Media findById(Long id) {
		return mediaRepository.findById(id).orElseThrow();
	}

	@Override
	public Iterable<Media> searchMediaByGenreAndType(String genre, String type, User user) {
		int userAge = userService.getAge(user);

		List<Media> mediaList = mediaRepository.findByGenreAndTypeOptional(isAll(genre) ? null : genre,
				isAll(type) ? null : type);

		return mediaList.stream().filter(media -> media.getFSK() <= userAge).collect(Collectors.toList());
	}

	@Override
	public Media findByName(String name) {
		return mediaRepository.findByName(name);
	}

	@Override
	public List<Long> getEditionIdsByMediaId(Long mediaId) {
		return editionService.getEditionIdsForMedia(mediaId);
	}

	private boolean isAll(String value) {
		return value == null || value.isEmpty() || value.equalsIgnoreCase("All Genres")
				|| value.equalsIgnoreCase("All Media Types");
	}
}
