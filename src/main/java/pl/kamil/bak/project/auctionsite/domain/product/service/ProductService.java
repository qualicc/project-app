package pl.kamil.bak.project.auctionsite.domain.product.service;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;
import pl.kamil.bak.project.auctionsite.domain.product.dao.ProductRepository;
import pl.kamil.bak.project.auctionsite.domain.product.dto.ProductDto;
import pl.kamil.bak.project.auctionsite.domain.provider.session.UserSessionProvider;
import pl.kamil.bak.project.auctionsite.model.productEntity.Product;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final UserSessionProvider userSessionProvider;

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper, UserSessionProvider userSessionProvider) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.userSessionProvider = userSessionProvider;
    }

    public List<Product> getUserByIdProduct(User user) {
        List<Product> productByUserId = productRepository.findProductByUserId(user.getId());
        if (productByUserId.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
        return productByUserId;
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
    }

    public List<Product> findProductByName(String name){
        return productRepository.findProductsByName(name);
    }


    @Transactional
    public Product addNewProduct(ProductDto productDto) {
        Product product = new Product();
        modelMapper.map(productDto, product);
        return productRepository.save(product);
    }

    @Transactional
    public Product update(ProductDto productDto, Product product) {
        productRepository.findById(product.getId()).ifPresent(product1 -> {
            if (checkingUserIsExist(product1)) {
                productDto.setUser(userSessionProvider.getPrincipal());
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        });
        modelMapper.map(productDto, product);
        return productRepository.save(product);
    }

    @Transactional
    public void deleteById(long id) {
        Optional<Product> byId = productRepository.findById(id);
        if (byId.isPresent()){
            if (checkingUserIsExist(byId.get())) {
                productRepository.deleteById(id);
            } else {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN);
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    private boolean checkingUserIsExist(Product product) {
        return product.getUser().getId().equals(userSessionProvider.getPrincipal().getId());
    }

}
