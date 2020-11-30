package pl.kamil.bak.project.auctionsite.domain.bidding.service;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.kamil.bak.project.auctionsite.domain.bidding.dao.BiddingRepository;
import pl.kamil.bak.project.auctionsite.domain.bidding.dto.BiddingDto;
import pl.kamil.bak.project.auctionsite.domain.shoppingcart.service.CartService;
import pl.kamil.bak.project.auctionsite.model.biddingEntity.Bidding;
import pl.kamil.bak.project.auctionsite.model.enums.Type;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BiddingService {
    private final BiddingRepository biddingRepository;
    private final ModelMapper modelMapper;
    private final CartService cartService;

    public BiddingService(BiddingRepository biddingRepository, ModelMapper modelMapper, CartService cartService) {
        this.biddingRepository = biddingRepository;
        this.modelMapper = modelMapper;
        this.cartService = cartService;
    }

    public List<Bidding> getAll() {
        return checkingIfItIsEmpty(biddingRepository.findAll());
    }

    public List<Bidding> getAllBiddingByUser(String name) {
        return checkingIfItIsEmpty(biddingRepository.findBiddingByUserUserName(name));
    }

    public Bidding getBidding(long id) {
        biddingRepository.findById(id).ifPresent(bidding -> {
            if (bidding.getEndBidding().isBefore(LocalDateTime.now())) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        });
        return biddingRepository.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });

    }

    @Transactional
    public Bidding crateBidding(BiddingDto biddingDto, User user) {
        Bidding bidding = new Bidding();
        if (biddingDto.isPromoted() && user.getType().equals(Type.PREMIUM)) {
            biddingDto.setPromoted(true);
        }else{
            bidding.setPromoted(false);
        }
        biddingDto.setUser(user);
        modelMapper.map(biddingDto, bidding);
        return biddingRepository.save(bidding);
    }

    @Transactional
    public Bidding updatePrice(User user, double price, long id) {
        Optional<Bidding> first = getAllBiddingByUser(user.getUserName()).stream()
                .filter(bidding -> bidding.getId().equals(getBidding(id).getId()))
                .findFirst();
        if (first.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        if (price <= getBidding(id).getCurrentPrice().doubleValue()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        getBidding(id).setCurrentPrice(BigDecimal.valueOf(price));
        getBidding(id).setWinnerUserId(user.getId());
        getBidding(id).getProduct().setPrice(BigDecimal.valueOf(price));
        return biddingRepository.save(getBidding(id));
    }

    private List<Bidding> checkingIfItIsEmpty(List<Bidding> bidding) {
        bidding.removeIf(bidding1 -> bidding1.getEndBidding().isBefore(LocalDateTime.now()));
        if (bidding.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }

        return bidding;
    }

    public void addBiddingProductToCart() {
        biddingRepository.findAll().forEach(bidding -> {
            if (bidding.getEndBidding().isBefore(LocalDateTime.now())) {
                cartService.save(bidding.getProduct(), bidding.getWinnerUserId());
            }
        });

    }
}
