package pl.kamil.bak.project.auctionsite.domain.user.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.kamil.bak.project.auctionsite.domain.provider.session.UserSessionProvider;
import pl.kamil.bak.project.auctionsite.domain.user.dto.AddressDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.LocationDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.UserDto;
import pl.kamil.bak.project.auctionsite.domain.user.service.UserService;
import pl.kamil.bak.project.auctionsite.model.enums.Type;
import pl.kamil.bak.project.auctionsite.model.userEntity.Address;
import pl.kamil.bak.project.auctionsite.model.userEntity.Location;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(UserAccountTypeController.class)
class UserAccountTypeControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserSessionProvider getPrincipal;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void shouldBeReturnViewWithAttributesBuyAccountType() throws Exception {
        //given
        MediaType mediaType = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);
        given(getPrincipal.getPrincipal()).willReturn(prepareUser());

        //when
        ResultActions perform = mockMvc.perform(get("/buy").accept(mediaType));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(redirectedUrl(null))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("premium"))
                .andExpect(model().attributeExists("normal"))
                .andExpect(model().attribute("premium", false))
                .andExpect(model().attribute("normal", true))
                .andExpect(view().name("buy-version-type"))
                .andDo(print());

    }

    @Test
    @WithMockUser
    void shouldBeGiveRedirectToMainWebSiteAndChangeType() throws Exception {
        //given
        given(getPrincipal.getPrincipal()).willReturn(prepareUser());
        doNothing().when(userService).buyPremium(prepareUser());

        //when
        ResultActions perform = mockMvc.perform(post("/buy/change").with(csrf()));

        //then
        perform
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/buy"))
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/buy"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void shouldBeRedirectToMainWebsiteBeforeSendPostPay() throws Exception {
        mockMvc.perform(post("/pay").with(csrf()))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/buy"))
                .andExpect(view().name("redirect:/buy"));
    }


    private User prepareUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("abc");
        user.setEmail("abc@abc.pl");
        user.setType(Type.NORMAL);
        user.setLocation(prepareLocation());
        return user;
    }

    private Location prepareLocation() {
        Location location = new Location();
        location.setCity("abc");
        location.setProvince("abc");
        location.setAddress(prepareAddress());
        return location;
    }

    private Address prepareAddress() {
        Address address = new Address();
        address.setZipCode("12-234");
        address.setStreet("abc");
        address.setHouseNumber(2);
        return address;
    }

}