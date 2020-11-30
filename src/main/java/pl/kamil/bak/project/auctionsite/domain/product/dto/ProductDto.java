package pl.kamil.bak.project.auctionsite.domain.product.dto;

import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

public class ProductDto {
    private Long id;
    @NotBlank
    @Size(min = 2, max = 20,message = "value from 2 to 20")
    private String name;
    @NotBlank
    @Size(min = 2, max = 20,message = "value from 2 to 20")
    private String description;
    private User user;
    @Min(1)
    private BigDecimal price;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
