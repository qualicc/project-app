package pl.kamil.bak.project.auctionsite.domain.product.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kamil.bak.project.auctionsite.model.productEntity.Product;

import java.util.List;


public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findProductByUserId(long id);
    List<Product> findProductsByName(String name);
}
