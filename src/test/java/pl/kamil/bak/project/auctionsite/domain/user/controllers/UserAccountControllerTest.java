package pl.kamil.bak.project.auctionsite.domain.user.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.kamil.bak.project.auctionsite.domain.provider.session.UserSessionProvider;
import pl.kamil.bak.project.auctionsite.domain.user.dto.AddressDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.UserDto;
import pl.kamil.bak.project.auctionsite.domain.user.service.UserService;
import pl.kamil.bak.project.auctionsite.model.userEntity.Address;
import pl.kamil.bak.project.auctionsite.model.userEntity.Location;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserAccountController.class)
class UserAccountControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private UserSessionProvider getPrincipal;


    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser
    void shouldBeReturnViewWithAttributesUserAccount() throws Exception {
        //given
        given(getPrincipal.getPrincipal()).willReturn(prepareUser());

        //when
        ResultActions perform = mockMvc.perform(get("/user/account").with(csrf()));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("email", prepareUser().getEmail()))
                .andExpect(model().attribute("userName", prepareUser().getUserName()))
                .andExpect(model().attribute("province", prepareUser().getLocation().getProvince()))
                .andExpect(model().attribute("city", prepareUser().getLocation().getCity()))
                .andExpect(model().attribute("street", prepareUser().getLocation().getAddress().getStreet()))
                .andExpect(model().attribute("houseNumber", prepareUser().getLocation().getAddress().getHouseNumber()))
                .andExpect(model().attribute("zip", prepareUser().getLocation().getAddress().getZipCode()))
                .andExpect(view().name("user-account"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void shouldBeUpdateUser() throws Exception {
        //given
        given(getPrincipal.getPrincipal()).willReturn(prepareUser());
        given(userService.userEmailExists(prepareUserDto().getEmail())).willReturn(false);
        given(userService.userNameExists(prepareUserDto().getUserName())).willReturn(false);

        //when
        ResultActions perform = mockMvc.perform(post("/user/account/details").with(csrf()));

        //then
        perform
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user/account"))
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/user/account"))
                .andDo(print());
    }

    @Test
    @WithMockUser
    void shouldBeChangePassword() throws Exception {
        //given
        given(getPrincipal.getPrincipal()).willReturn(prepareUser());
        given(encoder.matches(prepareUserDto().getPassword(), prepareUser().getPassword())).willReturn(false);

        //when
        ResultActions perform = mockMvc.perform(post("/user/account/security").with(csrf()));

        //then
        perform
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user/account"))
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:/user/account"))
                .andDo(print());
    }

    private UserDto prepareUserDto() {
        UserDto userDto = new UserDto();
        userDto.setPassword("sd");
        userDto.setConfirmPassword("sd");
        userDto.setOldPassword("password");
        userDto.setUserName("abc");
        userDto.setEmail("lau@op.pl");
        userDto.setLocation(prepareLocation());
        return userDto;
    }

    private User prepareUser() {
        User user = new User();
        user.setId(1L);
        user.setUserName("abc");
        user.setEmail("abc@abc.pl");
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