package at.ac.fhsalzburg.swd.spring.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import at.ac.fhsalzburg.swd.spring.repository.MediaTransactionRepository;

@Service
public class MediaTransactionService {
	@Autowired
    private MediaTransactionRepository mediaTransactionRepository;

	public MediaTransactionRepository getMediaTransactionRepository() {
		return mediaTransactionRepository;
	}

	public void setMediaTransactionRepository(MediaTransactionRepository mediaTransactionRepository) {
		this.mediaTransactionRepository = mediaTransactionRepository;
	}
}
