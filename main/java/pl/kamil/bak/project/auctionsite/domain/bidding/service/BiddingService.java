package pl.kamil.bak.project.auctionsite.domain.bidding.service;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.kamil.bak.project.auctionsite.domain.bidding.dao.BiddingRepository;
import pl.kamil.bak.project.auctionsite.domain.bidding.dto.BiddingDto;
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

    public BiddingService(BiddingRepository biddingRepository, ModelMapper modelMapper) {
        this.biddingRepository = biddingRepository;
        this.modelMapper = modelMapper;
    }

    public List<Bidding> getAll(){
        return checkingIfItIsEmpty(biddingRepository.findAll());
    }

    public List<Bidding> getAllBiddingByUser(String name){
        return checkingIfItIsEmpty(biddingRepository.findBiddingByUserUserName(name));
    }

    public Bidding getBidding(long id){
        return biddingRepository.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
    }

    @Transactional
    public Bidding crateBidding(BiddingDto biddingDto, User user){
        Bidding bidding = new Bidding();
        if (biddingDto.isPromoted() && user.getType().equals(Type.PREMIUM)){
            biddingDto.setPromoted(true);
        }
        biddingDto.setUser(user);
        modelMapper.map(biddingDto,bidding);
        return biddingRepository.save(bidding);
    }

    @Transactional
    public Bidding updatePrice(User user, double price, long id){
        Optional<Bidding> first = getAllBiddingByUser(user.getUserName()).stream()
                .filter(bidding -> bidding.getId().equals(getBidding(id).getId()))
                .findFirst();
        if (first.isPresent()){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        getBidding(id).setCurrentPrice(BigDecimal.valueOf(price));
        getBidding(id).getProduct().setPrice(BigDecimal.valueOf(price));
        return biddingRepository.save(getBidding(id));
    }

    //TODO it should be without calling url

    @Transactional
    public void deleteById(long id){
        if (getBidding(id).getEndBidding().isBefore(LocalDateTime.now())){
            biddingRepository.deleteById(id);
        }
    }

    private List<Bidding> checkingIfItIsEmpty(List<Bidding> bidding){
        if (bidding.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return bidding;
    }
}
