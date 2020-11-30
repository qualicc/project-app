package pl.kamil.bak.project.auctionsite;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.kamil.bak.project.auctionsite.domain.shoppingcart.service.CartService;
import pl.kamil.bak.project.auctionsite.model.biddingEntity.Bidding;
import pl.kamil.bak.project.auctionsite.model.cartEntity.ShoppingCart;
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
    private final CartService cartService;

    public StarterUser(UserRepository userRepository, PasswordEncoder encoder, LocationService locationService, CartService cartService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.locationService = locationService;
        this.cartService = cartService;
        init();
    }

    @PrePersist
    private void init() {
        User admin = new User();
        Bidding biddingAdmin = new Bidding();
        Product computer = new Product("Komputer", "Asus", admin, BigDecimal.valueOf(200.00));
        Product tShirt = new Product("Koszulka", "M", admin, BigDecimal.valueOf(20.00));
        ShoppingCart shoppingCart1 = new ShoppingCart();
        shoppingCart1.setUser(admin);
        biddingAdmin.setMinAmount(computer.getPrice());
        biddingAdmin.setPromoted(true);
        biddingAdmin.setCurrentPrice(biddingAdmin.getMinAmount());
        biddingAdmin.setUser(admin);
        biddingAdmin.setProduct(computer);
//        biddingAdmin.setEndBidding(LocalDateTime.now().plusSeconds(20));
        admin.setEmail("kam@op.pl");
        admin.setUserName("Kamil");
        admin.setPassword(encoder.encode("pass"));
        admin.setRole(Role.ADMIN);
        admin.setStatus(Status.ACTIVE);
        admin.setType(Type.PREMIUM);
        admin.setLocation(locationService.addLocation(new LocationDto("slask", "kato", null), new AddressDto("123", "23", "23321")));
        admin.setProduct(Arrays.asList(computer, tShirt, new Product("Auto", "Opel Astra", admin, BigDecimal.valueOf(15000.00))));
        admin.setBidding(Collections.singletonList(biddingAdmin));


        User user = new User();
        Bidding biddingUser = new Bidding();
        Product phone = new Product("Telefon", "Samsung S8", user, BigDecimal.valueOf(2000.00));
        ShoppingCart shoppingCart2 = new ShoppingCart();
        shoppingCart2.setUser(user);
        biddingUser.setMinAmount(phone.getPrice());
        biddingUser.setPromoted(false);
        biddingUser.setCurrentPrice(biddingUser.getMinAmount());
        biddingUser.setUser(user);
//        biddingUser.setEndBidding(LocalDateTime.now().plusSeconds(20));
        biddingUser.setProduct(phone);
        user.setEmail("lau@op.pl");
        user.setUserName("Laura");
        user.setPassword(encoder.encode("pass"));
        user.setRole(Role.USER);
        user.setStatus(Status.ACTIVE);
        user.setLocation(locationService.addLocation(new LocationDto("slask", "katowice", null), new AddressDto("Koszutki", "23", "42303")));
        user.setProduct(Collections.singletonList(phone));
        user.setBidding(Collections.singletonList(biddingUser));

        User user2 = new User();
        Bidding biddingUser2 = new Bidding();
        Product comp = new Product("Komputer", "Lenovo", user2, BigDecimal.valueOf(2000.00));
        biddingUser2.setMinAmount(comp.getPrice());
        biddingUser2.setPromoted(false);
        biddingUser2.setCurrentPrice(biddingUser2.getMinAmount());
        biddingUser2.setUser(user2);
        biddingUser2.setProduct(comp);
        user2.setEmail("ola@op.pl");
        user2.setUserName("ola");
        user2.setPassword(encoder.encode("pass"));
        user2.setRole(Role.USER);
        user2.setStatus(Status.ACTIVE);
        user2.setLocation(locationService.addLocation(new LocationDto("slask", "Bytom", null), new AddressDto("Wozowa", "28", "48303")));
        user2.setProduct(Collections.singletonList(comp));
        user2.setBidding(Collections.singletonList(biddingUser2));

        shoppingCart1.setProducts(Arrays.asList(
                phone,
                comp
        ));
        shoppingCart2.setProducts(Arrays.asList(
                comp,
                computer,
                tShirt
        ));
        userRepository.save(admin);
        userRepository.save(user);
        userRepository.save(user2);
        cartService.save(shoppingCart1.getProducts().get(0), shoppingCart1.getUser());
        cartService.save(shoppingCart1.getProducts().get(1), shoppingCart1.getUser());
        cartService.save(shoppingCart2.getProducts().get(0), shoppingCart2.getUser());
        cartService.save(shoppingCart2.getProducts().get(1), shoppingCart2.getUser());
    }

}
