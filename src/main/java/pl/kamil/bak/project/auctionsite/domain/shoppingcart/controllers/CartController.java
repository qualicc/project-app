package pl.kamil.bak.project.auctionsite.domain.shoppingcart.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.kamil.bak.project.auctionsite.domain.product.service.ProductService;
import pl.kamil.bak.project.auctionsite.domain.provider.session.UserSessionProvider;
import pl.kamil.bak.project.auctionsite.domain.shoppingcart.service.CartService;
import pl.kamil.bak.project.auctionsite.model.cartEntity.ShoppingCart;
import pl.kamil.bak.project.auctionsite.model.productEntity.Product;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;
    private final ProductService productService;
    private final UserSessionProvider sessionProvider;

    public CartController(CartService cartService, ProductService productService, UserSessionProvider sessionProvider) {
        this.cartService = cartService;
        this.productService = productService;
        this.sessionProvider = sessionProvider;
    }

    @GetMapping
    public ShoppingCart getCart() {
        return cartService.getCart(sessionProvider.getPrincipal()).orElseGet(ShoppingCart::new);
    }

    @PostMapping
    public ShoppingCart savNewProduct(@RequestParam long id) {
        return cartService.save(productService.getProductById(id), sessionProvider.getPrincipal());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cleanCart() {
        cartService.deleteById(sessionProvider.getPrincipal());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("id") long id){
        Product productById = productService.getProductById(id);
        cartService.deleteProduct(productById, sessionProvider.getPrincipal());
    }
}
