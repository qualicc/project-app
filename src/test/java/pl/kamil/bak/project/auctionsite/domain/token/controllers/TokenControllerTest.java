package pl.kamil.bak.project.auctionsite.domain.token.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import pl.kamil.bak.project.auctionsite.domain.token.service.TokenService;
import pl.kamil.bak.project.auctionsite.domain.user.service.UserService;
import pl.kamil.bak.project.auctionsite.model.tokenEntity.Token;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TokenController.class)
class TokenControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private TokenService tokenService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser
    void shouldBeReturnActivationView() throws Exception {
        //given
        MediaType mediaType = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        given(tokenService.findByToken("tokenName")).willReturn(prepareToken());

        //when
        ResultActions perform = mockMvc.perform(get("/activation").param("token", "tokenName").accept(mediaType).with(csrf()));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(view().name("activation"));

    }

    private Token prepareToken(){
        Token token = new Token();
        token.setToken("name");
        token.setExpirationDate(LocalDateTime.now().plusDays(1));
        token.setUser(new User());
        return token;
    }
}