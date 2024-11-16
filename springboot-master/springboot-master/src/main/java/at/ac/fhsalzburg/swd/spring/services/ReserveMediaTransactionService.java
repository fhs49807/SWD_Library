package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.ReserveMediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.ReserveMediaTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReserveMediaTransactionService implements ReserveMediaTransactionServiceInterface {

    @Autowired
    private EditionServiceInterface editionService;

    @Autowired
    private ReserveMediaTransactionRepository reserveMediaTransactionRepository;

    @Override
    public void reserveMediaForCustomer(User user, Media media) {
        // check if media is available
        if (editionService.findByMediaAndAvailable(media).isEmpty()) {
            // media is not available => reserve media for customer
            ReserveMediaTransaction reserveMediaTransaction = new ReserveMediaTransaction(user, media);
            reserveMediaTransactionRepository.save(reserveMediaTransaction);

            System.out.println("media " + media.getName() + " was reserved for customer " + user.getUsername());
        } else {
            // media is available => loan media to customer
            // TODO
            System.out.println("user " + user.getUsername() + " can loan the media " + media.getName());
        }
    }
}
