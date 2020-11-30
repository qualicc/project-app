package pl.kamil.bak.project.auctionsite.domain.token.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.kamil.bak.project.auctionsite.domain.token.dao.TokenRepository;
import pl.kamil.bak.project.auctionsite.model.tokenEntity.Token;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private TokenRepository tokenRepository;

    TokenService tokenService;

    @BeforeEach
    public void init(){
        tokenService = new TokenService(tokenRepository);
    }


    @Test
    void ShouldByFindByTokenName() {
        //given
        given(tokenRepository.findByToken("name")).willReturn(Optional.of(prepareToken()));

        //when
        Token byToken = tokenService.findByToken("name");

        //then
        assertThat(byToken).isNotNull();
        assertEquals(byToken.getToken(), prepareToken().getToken());

    }

    @Test
    void ShouldByFindTokenByUser() {
        //given
        given(tokenRepository.findByUser(any())).willReturn(prepareToken());

        //when
        User user = new User();
        Token byUser = tokenService.findByUser(user);

        //then
        assertThat(byUser).isNotNull().hasSameClassAs(prepareToken());

    }

    @Test
    void ShouldBySaveToken() {
        //given
        given(tokenRepository.save(any())).willReturn(prepareToken());

        //when
        User user = new User();
        tokenService.save(user, "name");

        //then
        verify(tokenRepository).save(any());
    }

    private Token prepareToken(){
        Token token = new Token();
        token.setToken("name");
        token.setExpirationDate(LocalDateTime.now().plusDays(1));
        token.setUser(new User());
        return token;
    }
}