package pl.kamil.bak.project.auctionsite.domain.product.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.kamil.bak.project.auctionsite.domain.product.service.ProductService;
import pl.kamil.bak.project.auctionsite.domain.product.dto.ProductDto;
import pl.kamil.bak.project.auctionsite.model.productEntity.Product;
import pl.kamil.bak.project.auctionsite.domain.provider.session.UserSessionProvider;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final UserSessionProvider getPrincipal;

    public ProductController(ProductService productService, UserSessionProvider getPrincipal) {
        this.productService = productService;
        this.getPrincipal = getPrincipal;
    }

    @GetMapping("/owned")
    public List<Product> getAllOwnProducts() {
        return productService.getUserByIdProduct(getPrincipal.getPrincipal());
    }

    @GetMapping
    public List<Product> getAll() {
        if (productService.getAll().isEmpty()){
            return new ArrayList<>();
        }
        return productService.getAll();
    }


    @PostMapping
    public Product addProduct(@RequestBody @Validated ProductDto productDto) {
        productDto.setUser(getPrincipal.getPrincipal());
        return productService.addNewProduct(productDto);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@RequestBody @Validated ProductDto productDto, @PathVariable("id") long id) {
        Product product = productService.getProductById(id);
        return productService.update(productDto, product);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("id") long id) {
        productService.deleteById(id);
    }


}
