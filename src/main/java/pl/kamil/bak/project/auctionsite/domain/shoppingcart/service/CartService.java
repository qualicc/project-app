package pl.kamil.bak.project.auctionsite.domain.shoppingcart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kamil.bak.project.auctionsite.domain.shoppingcart.dao.CartRepository;
import pl.kamil.bak.project.auctionsite.domain.user.dao.UserRepository;
import pl.kamil.bak.project.auctionsite.model.cartEntity.ShoppingCart;
import pl.kamil.bak.project.auctionsite.model.productEntity.Product;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import java.util.*;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ShoppingCart save(Product product, User user) {
        if (getCart(user).isEmpty()) {
            ShoppingCart shoppingCart = new ShoppingCart();
            user.setShoppingCart(shoppingCart);
            shoppingCart.setUser(user);
            cartRepository.save(shoppingCart);
        }
        getCart(user).get().getProducts().add(product);
        return cartRepository.save(getCart(user).get());
    }

    @Transactional
    public ShoppingCart save(Product product, long id) {
        Optional<User> user = userRepository.findById(id);
        Optional<ShoppingCart> cart = cartRepository.findShoppingCartByUserId(id);
        if (cart.isEmpty()) {
            cart = Optional.of(new ShoppingCart());
            ShoppingCart shoppingCart = cart.get();
            user.ifPresent(user1 -> {
                user1.setShoppingCart(shoppingCart);
                shoppingCart.setUser(user1);
            });
            cartRepository.save(shoppingCart);
        }
        cart.get().getProducts().add(product);
        return cartRepository.save(cart.get());
    }

    @Transactional
    public void deleteById(User user) {
        cartRepository.deleteById(user.getId());
    }

    @Transactional
    public void deleteProduct(Product product, User user) {
        Optional<ShoppingCart> cart = getCart(user);
        cart.ifPresent(shoppingCart -> shoppingCart.getProducts().removeIf(product1 -> product1.getId().equals(product.getId())));
        cartRepository.save(cart.get());
    }

    public Optional<ShoppingCart> getCart(User user) {
        return cartRepository.findShoppingCartByUserId(user.getId());
    }
}
