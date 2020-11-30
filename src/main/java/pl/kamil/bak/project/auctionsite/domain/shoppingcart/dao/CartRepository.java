package pl.kamil.bak.project.auctionsite.domain.shoppingcart.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kamil.bak.project.auctionsite.model.cartEntity.ShoppingCart;

import java.util.Optional;

public interface CartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findShoppingCartByUserId(Long id);
}
