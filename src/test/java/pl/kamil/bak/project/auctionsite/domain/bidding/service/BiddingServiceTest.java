package pl.kamil.bak.project.auctionsite.domain.bidding.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.web.server.ResponseStatusException;
import pl.kamil.bak.project.auctionsite.domain.bidding.dao.BiddingRepository;
import pl.kamil.bak.project.auctionsite.domain.bidding.dto.BiddingDto;
import pl.kamil.bak.project.auctionsite.domain.shoppingcart.service.CartService;
import pl.kamil.bak.project.auctionsite.model.biddingEntity.Bidding;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BiddingServiceTest {

    @Mock
    private BiddingRepository biddingRepository;

    @Mock
    private CartService cartService;

    private BiddingService biddingService;

    @BeforeEach
    public void init() {
        ModelMapper modelMapper = new ModelMapper();
        biddingService = new BiddingService(biddingRepository, modelMapper, cartService);
    }


    @Test
    void getAll() {
        //given
        given(biddingRepository.findAll()).willReturn(prepareBidding());

        //when
        List<Bidding> all = biddingService.getAll();

        //then
        assertThat(all).hasSize(3);

    }

    @Test
    void getAllBiddingByUser() {
        //given
        given(biddingRepository.findBiddingByUserUserName(anyString())).willReturn(prepareBidding());

        //when
        List<Bidding> allBiddingByUser = biddingService.getAllBiddingByUser(anyString());

        //then
        assertThat(allBiddingByUser).hasSize(3);

    }

    @Test
    void getBidding() {
        //given
        given(biddingRepository.findById(anyLong())).willReturn(prepareBidding().stream().findAny());

        //when
        Bidding bidding = biddingService.getBidding(anyLong());

        //then
        assertNotNull(bidding);
    }

    @Test
    void crateBidding() {
        //given
        given(biddingRepository.save(any())).willReturn(new Bidding());

        //when
        Bidding bidding = biddingService.crateBidding(new BiddingDto(), new User());

        //then
        assertNotNull(bidding);
    }

    @Test
    void updatePrice() {
        //then
        assertThrows(ResponseStatusException.class, () -> biddingService.updatePrice(prepareUser(), 25.00, prepareOneBidding().getId()));
    }

    private List<Bidding> prepareBidding() {
        return Arrays.asList(
                new Bidding(),
                new Bidding(),
                new Bidding()
        );
    }

    private User prepareUser(){
        User user = new User();
        user.setId(1L);
        user.setUserName("abc");
        user.setEmail("abc@gamil.com");
        return user;
    }

    private Bidding prepareOneBidding(){
        Bidding bidding = new Bidding();
        bidding.setUser(prepareUser());
        bidding.setId(1L);
        bidding.setCurrentPrice(BigDecimal.valueOf(20.00));
        return bidding;
    }
}