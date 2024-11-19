package at.ac.fhsalzburg.swd.spring.services;

import at.ac.fhsalzburg.swd.spring.model.Edition;
import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.ReserveMediaTransaction;
import at.ac.fhsalzburg.swd.spring.model.User;
import at.ac.fhsalzburg.swd.spring.repository.ReserveMediaTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReserveMediaTransactionService implements ReserveMediaTransactionServiceInterface {

    @Autowired
    private MediaServiceInterface mediaService;

    @Autowired
    private EditionServiceInterface editionService;

    @Autowired
    private ReserveMediaTransactionRepository reserveMediaTransactionRepository;

    @Override
    public void reserveMediaForCustomer(User user, String mediaName, Date reserveStartDate) {
        Media media = mediaService.findByName(mediaName);

        // check if media is available
        List<Edition> editions = editionService.findByMediaAndAvailable(media);
        if (editions.isEmpty()) {
            // media is not available => reserve media for customer
            //ReserveMediaTransaction reserveMediaTransaction = new ReserveMediaTransaction(user, media);
            //reserveMediaTransactionRepository.save(reserveMediaTransaction);

            System.out.println("media " + media.getName() + " was reserved for customer " + user.getUsername());
        } else {
            // media is available => reserve media for customer
            ReserveMediaTransaction reserveMediaTransaction =
                new ReserveMediaTransaction(user, editions.get(0), reserveStartDate);
            reserveMediaTransactionRepository.save(reserveMediaTransaction);
        }
    }
}
