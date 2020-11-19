package pl.kamil.bak.project.auctionsite;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.kamil.bak.project.auctionsite.model.biddingEntity.Bidding;
import pl.kamil.bak.project.auctionsite.model.productEntity.Product;
import pl.kamil.bak.project.auctionsite.domain.user.dao.UserRepository;
import pl.kamil.bak.project.auctionsite.domain.user.service.LocationService;
import pl.kamil.bak.project.auctionsite.domain.user.dto.AddressDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.LocationDto;
import pl.kamil.bak.project.auctionsite.model.enums.Role;
import pl.kamil.bak.project.auctionsite.model.enums.Status;
import pl.kamil.bak.project.auctionsite.model.enums.Type;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import javax.persistence.PrePersist;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;


//TODO Garbage class to be removed

@Component
public class StarterUser {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final LocationService locationService;

    public StarterUser(UserRepository userRepository, PasswordEncoder encoder, LocationService locationService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.locationService = locationService;
        init();
    }

    @PrePersist
    private void init(){
        User admin = new User();
        Bidding biddingAdmin = new Bidding();
        Product shoes = new Product("Buty", "Nike nowe", admin, BigDecimal.valueOf(200.00));
        biddingAdmin.setMinAmount(shoes.getPrice());
        biddingAdmin.setPromoted(true);
        biddingAdmin.setUser(admin);
        biddingAdmin.setProduct(shoes);
        admin.setEmail("kam@op.pl");
        admin.setUserName("Kamil");
        admin.setPassword(encoder.encode("pass"));
        admin.setRole(Role.ADMIN);
        admin.setStatus(Status.ACTIVE);
        admin.setType(Type.PREMIUM);
        admin.setLocation(locationService.addLocation(new LocationDto("slask", "kato", null), new AddressDto("123", "23", "23321")));
        admin.setProduct(Arrays.asList(shoes, new Product("Auto", "Opel Astra", admin, BigDecimal.valueOf(15000.00))));
        admin.setBidding(Arrays.asList(biddingAdmin));

        User user = new User();
        Bidding biddingUser = new Bidding();
        Product phone = new Product("Telefon", "Samsung S8", user, BigDecimal.valueOf(2000.00));
        biddingUser.setMinAmount(phone.getPrice());
        biddingUser.setPromoted(false);
        biddingUser.setUser(user);
        biddingUser.setEndBidding(LocalDateTime.now().plusSeconds(20));
        biddingUser.setProduct(phone);
        user.setEmail("lau@op.pl");
        user.setUserName("Laura");
        user.setPassword(encoder.encode("pass"));
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setLocation(locationService.addLocation(new LocationDto("slask", "katowice", null), new AddressDto("Koszutki", "23", "42303")));
        user.setProduct(Collections.singletonList(phone));
        user.setBidding(Collections.singletonList(biddingUser));

        userRepository.save(admin);
        userRepository.save(user);
    }

}
