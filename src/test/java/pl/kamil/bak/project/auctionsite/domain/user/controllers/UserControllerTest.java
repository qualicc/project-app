package pl.kamil.bak.project.auctionsite.domain.user.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.kamil.bak.project.auctionsite.domain.provider.session.UserSessionProvider;
import pl.kamil.bak.project.auctionsite.domain.user.dto.AddressDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.LocationDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.UserDto;
import pl.kamil.bak.project.auctionsite.domain.user.service.UserService;
import pl.kamil.bak.project.auctionsite.model.userEntity.Address;
import pl.kamil.bak.project.auctionsite.model.userEntity.Location;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import static org.mockito.BDDMockito.given;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserSessionProvider sessionProvider;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldBeReturnViewForToRegister() throws Exception {
        //given
        MediaType mediaType = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);

        //when
        ResultActions perform = mockMvc.perform(get("/sign-up").accept(mediaType));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(redirectedUrl(null))
                .andExpect(view().name("sign-up"))
                .andExpect(model().attributeExists("user"))
                .andDo(print());


    }

    @Test
    void shouldBeGetLoginWebsite() throws Exception {
        //given
        MediaType mediaType = new MediaType(MediaType.TEXT_HTML, StandardCharsets.UTF_8);


        //when
        ResultActions perform = mockMvc.perform(get("/login").accept(mediaType));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(content().contentType(mediaType))
                .andExpect(redirectedUrl(null))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void shouldBeNotSaveNewUserBecauseHasError() throws Exception {
        //given
        given(userService.userEmailExists(prepareUserDto().getEmail())).willReturn(false);
        given(userService.userNameExists(prepareUserDto().getUserName())).willReturn(false);
        given(userService.addUser(prepareUserDto(),prepareLocationDto(),prepareAddressDto())).willReturn(prepareUser().get());

        //when
        ResultActions perform = mockMvc.perform(post("/sign-up").with(csrf()));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(redirectedUrl(null));
    }

    private UserDto prepareUserDto() {
        UserDto userDto = new UserDto();
        userDto.setPassword("sd");
        userDto.setConfirmPassword("sd");
        userDto.setUserName("abc");
        userDto.setEmail("lau@op.pl");
        userDto.setLocation(prepareLocation());
        return userDto;
    }

    private Optional<User> prepareUser() {
        Optional<User> user = Optional.of(new User());
        user.get().setId(1L);
        user.get().setUserName("abc");
        user.get().setEmail("abc@abc.pl");
        user.get().setLocation(prepareLocation());
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

    private AddressDto prepareAddressDto() {
        return new AddressDto("abc", "2", "12-234");
    }

    private LocationDto prepareLocationDto() {
        return new LocationDto("abc", "abc", prepareAddressDto());
    }
}