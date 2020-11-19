package pl.kamil.bak.project.auctionsite.domain.token.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kamil.bak.project.auctionsite.model.tokenEntity.Token;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Long> {

    Optional<Token> findByToken(String token);

    Token findByUser(User user);
}
