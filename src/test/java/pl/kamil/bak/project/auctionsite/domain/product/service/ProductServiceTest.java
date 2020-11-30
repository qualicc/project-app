package pl.kamil.bak.project.auctionsite.domain.product.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import org.springframework.web.server.ResponseStatusException;
import pl.kamil.bak.project.auctionsite.domain.product.dao.ProductRepository;
import pl.kamil.bak.project.auctionsite.domain.product.dto.ProductDto;
import pl.kamil.bak.project.auctionsite.domain.provider.session.UserSessionProvider;
import pl.kamil.bak.project.auctionsite.model.productEntity.Product;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserSessionProvider sessionProvider;

    private ProductService productService;


    @BeforeEach
    public void init() {
        ModelMapper modelMapper = new ModelMapper();
        productService = new ProductService(productRepository, modelMapper, sessionProvider);
    }


    @Test
    void shouldReturnUserByIdProduct() {
        //given
        given(productRepository.findProductByUserId(2L)).willReturn(prepareProductsData());

        //when
        User user = new User();
        user.setId(2L);
        List<Product> userByIdProduct = productService.getUserByIdProduct(user);

        //then
        assertThat(userByIdProduct).hasSize(5);

    }

    @Test
    void shouldReturnArrayProducts() {
        //given
        given(productRepository.findAll()).willReturn(prepareProductsData());

        //when
        List<Product> products = productService.getAll();

        //then
        assertThat(products).hasSize(5);
    }

    @Test
    void shouldReturnProductById() {
        //given
        given(productRepository.findById(2L)).willReturn(Optional.ofNullable(prepareProductsData().get(2)));

        //when
        Product productById = productService.getProductById(2L);

        //then
        assertEquals(productById.getName(), "Skuter");
        assertEquals(productById.getDescription(), "Brak");
        assertEquals(productById.getPrice(), BigDecimal.valueOf(4000.00));
    }

    @Test
    void shouldReturnProductWithThrow() {

        //when
        User user = new User();
        user.setId(1L);

        //then
        assertThrows(ResponseStatusException.class, () -> productService.getProductById(1L));
        assertThrows(ResponseStatusException.class, () -> productService.getUserByIdProduct(user));
    }

    @Test
    void shouldSaveProductAndDontHaveToBeANull() {
        //given
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        given(productRepository.save(productArgumentCaptor.capture())).willReturn(convertDto());

        //when
        ProductDto productDto = prepareProductDto();
        Product product = productService.addNewProduct(productDto);

        //then
        Product saveProduct = productArgumentCaptor.getValue();
        assertNotNull(saveProduct);
        assertEquals(productDto.getName(), product.getName());
        assertEquals(productDto.getDescription(), product.getDescription());
        assertEquals(productDto.getPrice(), product.getPrice());
    }

    @Test
    void shouldUpdateProductAndDontHaveToBeANull() {
        //given
        given(productRepository.save(any())).willReturn(convertDto());

        //when
        Product update = productService.update(prepareProductDto(), convertDto());

        //then
        Assertions.assertNotNull(update);
        assertEquals(update.getName(), prepareProductDto().getName());
        assertEquals(update.getDescription(), prepareProductDto().getDescription());
        assertEquals(update.getPrice(), prepareProductDto().getPrice());
    }

    @Test
    void shouldByDeleteProductById() {
        //then
        assertThrows(ResponseStatusException.class, () -> productService.deleteById(1L));


    }

    private Product convertDto() {
        Product product = new Product();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(prepareProductDto(), product);
        product.setId(1L);
        return product;
    }

    private User prepareUser() {
        User user = new User();
        user.setEmail("abc@gamil.com");
        user.setUserName("abc");
        user.setPassword("123");
        return user;
    }

    private ProductDto prepareProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setName("wąż");
        productDto.setDescription("zwierze");
        productDto.setUser(prepareUser());
        productDto.setPrice(BigDecimal.valueOf(200.00));
        productDto.setId(1L);
        return productDto;
    }

    private List<Product> prepareProductsData() {
        return Arrays.asList(
                new Product("Buty", "Nowe", new User(), BigDecimal.valueOf(200.00)),
                new Product("Auto", "Stare", new User(), BigDecimal.valueOf(10000.00)),
                new Product("Skuter", "Brak", new User(), BigDecimal.valueOf(4000.00)),
                new Product("Laptop", "Używany", new User(), BigDecimal.valueOf(5000.00)),
                new Product("Telefon", "Nowy", new User(), BigDecimal.valueOf(2300.00))
        );
    }
}