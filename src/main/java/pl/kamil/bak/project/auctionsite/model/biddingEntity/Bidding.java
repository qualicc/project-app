package pl.kamil.bak.project.auctionsite.model.biddingEntity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.kamil.bak.project.auctionsite.model.productEntity.Product;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bidding")
public class Bidding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private BigDecimal minAmount;
    private BigDecimal currentPrice;
    private LocalDateTime startBidding;
    private LocalDateTime endBidding;
    private boolean promoted;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @JsonIgnore
    private long winnerUserId;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    public Bidding() {
        this.startBidding = LocalDateTime.now();
        this.endBidding = LocalDateTime.now().plusHours(24);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public LocalDateTime getStartBidding() {
        return startBidding;
    }

    public void setStartBidding(LocalDateTime startBidding) {
        this.startBidding = startBidding;
    }

    public LocalDateTime getEndBidding() {
        return endBidding;
    }

    public void setEndBidding(LocalDateTime endBidding) {
        this.endBidding = endBidding;
    }

    public boolean isPromoted() {
        return promoted;
    }

    public void setPromoted(boolean promoted) {
        this.promoted = promoted;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public long getWinnerUserId() {
        return winnerUserId;
    }

    public void setWinnerUserId(long winnerUserId) {
        this.winnerUserId = winnerUserId;
    }
}
