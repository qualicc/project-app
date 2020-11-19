package pl.kamil.bak.project.auctionsite.domain.bidding.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.kamil.bak.project.auctionsite.domain.bidding.dto.BiddingDto;
import pl.kamil.bak.project.auctionsite.domain.bidding.service.BiddingService;
import pl.kamil.bak.project.auctionsite.domain.provider.session.UserSessionProvider;
import pl.kamil.bak.project.auctionsite.model.biddingEntity.Bidding;

import java.util.List;

@RestController
@RequestMapping("/bidding")
public class BiddingController {
    private final BiddingService biddingService;
    private final UserSessionProvider sessionProvider;

    public BiddingController(BiddingService biddingService, UserSessionProvider sessionProvider) {
        this.biddingService = biddingService;
        this.sessionProvider = sessionProvider;
    }

    @GetMapping
    public List<Bidding> getAll(){
        return biddingService.getAll();
    }
    @GetMapping("/owned")
    public List<Bidding> getAllOwned(){
        return biddingService.getAllBiddingByUser(sessionProvider.getPrincipal().getUserName());
    }

    @GetMapping("/{id}")
    public Bidding getById(@PathVariable("id") long id){
        return biddingService.getBidding(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Bidding addBidding(@RequestBody BiddingDto biddingDto){
        return biddingService.crateBidding(biddingDto, sessionProvider.getPrincipal());
    }

    @PutMapping("/{id}")
    public Bidding update(@PathVariable("id") long id, @RequestParam double price){
        return biddingService.updatePrice(sessionProvider.getPrincipal(), price, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.CONFLICT)
    public void delete(@PathVariable("id") long id){
        biddingService.deleteById(id);
    }
}
