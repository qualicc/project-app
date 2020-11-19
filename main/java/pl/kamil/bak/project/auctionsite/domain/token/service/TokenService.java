package pl.kamil.bak.project.auctionsite.domain.token.service;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pl.kamil.bak.project.auctionsite.domain.token.dao.TokenRepository;
import pl.kamil.bak.project.auctionsite.model.tokenEntity.Token;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import java.time.LocalDateTime;

@Service
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Transactional
    public Token findByToken(String token) {
       return tokenRepository.findByToken(token).orElseThrow(() -> {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        });
    }

    @Transactional
    public Token findByUser(User user) {
        return tokenRepository.findByUser(user);
    }

    @Transactional
    public void save(User user, String token) {
        Token newToken = new Token(token, user);
        newToken.setExpirationDate(LocalDateTime.now().plusDays(30));
        tokenRepository.save(newToken);
    }

}
