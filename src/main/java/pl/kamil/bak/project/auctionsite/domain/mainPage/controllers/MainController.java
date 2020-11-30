package pl.kamil.bak.project.auctionsite.domain.mainPage.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.kamil.bak.project.auctionsite.domain.provider.session.UserSessionProvider;


@Controller
public class MainController {
    private final UserSessionProvider getPrincipal;

    public MainController(UserSessionProvider getPrincipal) {
        this.getPrincipal = getPrincipal;
    }

    @GetMapping("/main-page")
    public String forUser( Model model) {
        if (getPrincipal.getUserExist()) {
            model.addAttribute("name", getPrincipal.getPrincipal().getUserName());
            return "main-page";
        }
        return "index";
    }

    @GetMapping("/biddings")
    public String bidding(){
        return "bidding";
    }


}
