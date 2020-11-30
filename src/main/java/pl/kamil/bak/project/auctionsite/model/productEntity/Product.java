package pl.kamil.bak.project.auctionsite.model.productEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;
import pl.kamil.bak.project.auctionsite.domain.shoppingcart.controllers.CartController;
import pl.kamil.bak.project.auctionsite.model.biddingEntity.Bidding;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String name;

    private String description;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private BigDecimal price;
    @JsonView(CartController.class)
    private int amount;

    //TODO delete this below constructor after ended project

    public Product( String name,  String description, User user, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.price = price;

    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
