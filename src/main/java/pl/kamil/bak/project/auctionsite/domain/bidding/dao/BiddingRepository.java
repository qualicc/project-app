package pl.kamil.bak.project.auctionsite.domain.bidding.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kamil.bak.project.auctionsite.model.biddingEntity.Bidding;

import java.util.List;


public interface BiddingRepository extends JpaRepository<Bidding, Long> {
    List<Bidding> findBiddingByUserUserName(String name);
}
