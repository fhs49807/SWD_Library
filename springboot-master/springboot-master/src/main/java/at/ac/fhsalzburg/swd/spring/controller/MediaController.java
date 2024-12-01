package at.ac.fhsalzburg.swd.spring.controller;

import at.ac.fhsalzburg.swd.spring.model.Media;
import at.ac.fhsalzburg.swd.spring.model.MediaTransaction;
import at.ac.fhsalzburg.swd.spring.services.MediaServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.MediaTransactionServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.ReserveMediaTransactionServiceInterface;
import at.ac.fhsalzburg.swd.spring.services.UserServiceInterface;
import at.ac.fhsalzburg.swd.spring.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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
        @RequestParam(required = false) Date reserveStartDate,
        @RequestParam(required = false) Date reserveEndDate,
        @RequestParam(required = false) String selectedGenre,
        @RequestParam(required = false) String selectedType,
        @CurrentSecurityContext(expression = "authentication") Authentication authentication,
        Model model) {
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String username = authentication.getName();

            // Validate required fields
            if (mediaId == null || reserveStartDate == null || reserveEndDate == null) {
                return handleError("Please select a media item and specify the reserve date period.", reserveStartDate,
                    reserveEndDate, selectedGenre, selectedType, username, model);
            } else if (reserveStartDate.after(reserveEndDate)) {
                return handleError("Start date has to be on the same day or after end date", reserveStartDate,
                    reserveEndDate, selectedGenre, selectedType, username, model);
            }

            if (reserveStartDate.after(new Date())) {
                reserveMediaTransactionService.reserveMediaForCustomer(username, mediaId, reserveStartDate,
                    reserveEndDate);
            }

            // Create the transaction
            MediaTransaction transaction =
                mediaTransactionService.loanMedia(username, mediaId, reserveStartDate);

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

    /**
     * Return to loan page with default data and error message.
     *
     * @return path to loan html page
     */
    private String handleError(String errorMessage, Date reserveStartDate, Date reserveEndDate, String selectedGenre,
        String selectedType, String username, Model model) {
        // Populate genres, media types, and reapply user selections
        model.addAttribute("genres", mediaService.getAllGenres());
        model.addAttribute("mediaTypes", mediaService.getAllMediaTypes());
        model.addAttribute("selectedGenre", selectedGenre);
        model.addAttribute("selectedType", selectedType);
        model.addAttribute("mediaList", mediaService.searchMediaByGenreAndType(selectedGenre, selectedType,
            userService.getByUsername(username)));
        model.addAttribute("todayDate", DateUtils.getLocalDateFromDate(new Date()));
        model.addAttribute("endDate", DateUtils.getLocalDateFromDate(new Date()).plusDays(1));

        addErrorMessage(errorMessage, model);

        return "loan";
    }
}
