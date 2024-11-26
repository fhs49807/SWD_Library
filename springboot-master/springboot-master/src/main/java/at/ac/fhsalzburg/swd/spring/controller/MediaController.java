package at.ac.fhsalzburg.swd.spring.controller;

import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.services.MediaServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.ReserveMediaTransactionServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class MediaController extends BaseController {

    @Autowired
    private MediaServiceInterface mediaService;

    @Autowired
    private UserServiceInterface userService;

    @Autowired
    private MediaTransactionServiceInterface mediaTransactionService;

    @Autowired
    private ReserveMediaTransactionServiceInterface reserveMediaTransactionService;

    @RequestMapping(value = "/loanReserveMedia", method = RequestMethod.POST)
    @SuppressWarnings("unused")
    public String loanReserveMedia(
        @RequestParam(required = false) Long mediaId,
        @RequestParam(required = false) String reserveStartDate, // TODO better as Date object
        @RequestParam(required = false) String reserveEndDate,
        @RequestParam(required = false) String selectedGenre,
        @RequestParam(required = false) String selectedType,
        @CurrentSecurityContext(expression = "authentication") Authentication authentication,
        Model model) {
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();

            // Validate required fields
            if (mediaId == null || reserveStartDate == null || reserveEndDate == null) {
                addErrorMessage("Please select a media item and specify the reserve date period.", model);

                // Populate genres, media types, and reapply user selections
                model.addAttribute("genres", mediaService.getAllGenres());
                model.addAttribute("mediaTypes", mediaService.getAllMediaTypes());
                model.addAttribute("selectedGenre", selectedGenre);
                model.addAttribute("selectedType", selectedType);
                model.addAttribute("mediaList", mediaService.searchMediaByGenreAndType(selectedGenre, selectedType,
                    userService.getByUsername(username)));

                return "loan";
            }

            try {
                // Parse the reserve start date
                Date parsedReserveStartDate = new SimpleDateFormat("yyyy-MM-dd").parse(reserveStartDate);

                Date today = new Date();
                if (parsedReserveStartDate.after(today)) {
                    Date parsedReserveEndDate = new SimpleDateFormat("yyyy-MM-dd").parse(reserveEndDate);
                    reserveMediaTransactionService.reserveMediaForCustomer(username, mediaId, parsedReserveStartDate,
                        parsedReserveEndDate);
                }

                // Create the transaction
                MediaTransaction transaction =
                    mediaTransactionService.loanMedia(username, mediaId, parsedReserveStartDate);

                // Populate success details
                Media media = transaction.getEdition().getMedia();
                model.addAttribute("username", username);
                model.addAttribute("loanDate", reserveStartDate);
                model.addAttribute("mediaId", mediaId);
                model.addAttribute("mediaTitle", media.getName());
                model.addAttribute("mediaGenre", media.getGenre().getName());
                model.addAttribute("mediaType", media.getMediaType().getType());
                model.addAttribute("editionIds", List.of(transaction.getEdition().getId()));

                return "loanSuccess";
            } catch (IllegalStateException e) {
                addErrorMessage(e.getMessage(), model);
            } catch (Exception e) {
                addErrorMessage("Error creating loan: " + e.getMessage(), model);
            }
        } else {
            addErrorMessage("You must log in to loan media.", model);
        }

        // Reapply user selections in case of error
        // TODO check when this code is reached
        model.addAttribute("genres", mediaService.getAllGenres());
        model.addAttribute("mediaTypes", mediaService.getAllMediaTypes());
        model.addAttribute("selectedGenre", selectedGenre);
        model.addAttribute("selectedType", selectedType);
        model.addAttribute("mediaList", mediaService.searchMediaByGenreAndType(selectedGenre, selectedType,
            userService.getByUsername(authentication.getName())));

        return "loan";
    }
}
