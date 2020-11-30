package pl.kamil.bak.project.auctionsite.domain.product.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.kamil.bak.app.test.ControllerTestConfiguration;
import pl.kamil.bak.project.auctionsite.domain.product.dto.ProductDto;
import pl.kamil.bak.project.auctionsite.domain.product.service.ProductService;
import pl.kamil.bak.project.auctionsite.model.enums.Type;
import pl.kamil.bak.project.auctionsite.model.productEntity.Product;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@Import(ControllerTestConfiguration.class)
@AutoConfigureMockMvc
class ProductControllerTest {
    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void getAllOwnProducts() throws Exception {
        //given
        given(productService.getUserByIdProduct(new User())).willReturn(prepareProducts());

        //when
        ResultActions resultActions = mockMvc.perform(get("/products/owned").accept(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andDo(print());

    }

    @Test
    @WithMockUser
    void getAll() throws Exception {
        //given
        given(productService.getAll()).willReturn(prepareProducts());

        //when
        ResultActions resultActions = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").isEmpty())
                .andExpect(jsonPath("$[0].description").isEmpty())
                .andExpect(jsonPath("$[0].price").isEmpty())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void addProduct() throws Exception {
        //given
        given(productService.addNewProduct(any(ProductDto.class))).willReturn(prepareProduct());

        //when
        String requestString = objectMapper.writeValueAsString(prepareProductDto());
        ResultActions resultActions = mockMvc.perform(post("/products")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").isEmpty())
                .andExpect(jsonPath("$.description").isEmpty())
                .andExpect(jsonPath("$.price").isEmpty())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void updateProduct() throws Exception {
        //given
        given(productService.update(prepareProductDto(), prepareProduct())).willReturn(prepareProduct());

        //when
        String requestString = objectMapper.writeValueAsString(prepareProductDto());
        ResultActions resultActions = mockMvc.perform(put("/products/{id}", 1)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestString));

        //then
        resultActions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @WithMockUser
    void deleteProduct() throws Exception {
        //given
        doNothing().when(productService).deleteById(1L);

        //when
        ResultActions resultActions = mockMvc.perform(delete("/products/{id}", 1L).with(csrf()));

        //then
        resultActions
                .andExpect(status().isNoContent());
    }

    private Product prepareProduct() {
        Product product = new Product();
        product.setId(1L);
        return product;
    }


    private ProductDto prepareProductDto() {
        ProductDto productDto = new ProductDto();
        productDto.setName("name");
        productDto.setDescription("none");
        productDto.setPrice(BigDecimal.valueOf(2.00));
        productDto.setId(1L);
        return productDto;
    }


    private List<Product> prepareProducts() {

        return Arrays.asList(
                prepareProduct(),
                new Product(),
                new Product(),
                new Product(),
                new Product()
        );
    }
}