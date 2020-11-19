package pl.kamil.bak.project.auctionsite.domain.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kamil.bak.project.auctionsite.model.userEntity.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
