package pl.kamil.bak.project.auctionsite.domain.user.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.thymeleaf.TemplateEngine;
import pl.kamil.bak.project.auctionsite.domain.token.dao.TokenRepository;
import pl.kamil.bak.project.auctionsite.domain.token.service.TokenService;
import pl.kamil.bak.project.auctionsite.domain.user.dao.AddressRepository;
import pl.kamil.bak.project.auctionsite.domain.user.dao.LocationRepository;
import pl.kamil.bak.project.auctionsite.domain.user.dao.UserRepository;
import pl.kamil.bak.project.auctionsite.domain.user.dto.AddressDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.LocationDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.UserDto;
import pl.kamil.bak.project.auctionsite.model.userEntity.Address;
import pl.kamil.bak.project.auctionsite.model.userEntity.Location;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

import java.time.LocalDateTime;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private ModelMapper modelMapper;

    private UserService userService;

    private LocationService locationService;

    @BeforeEach
    public void init() {
        locationService = new LocationService(locationRepository, addressRepository, modelMapper);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        TokenService tokenService = new TokenService(tokenRepository);
        TemplateEngine templateEngine = new TemplateEngine();
        EmailService emailService = new EmailService(tokenService, templateEngine, javaMailSender);
        userService = new UserService(userRepository, locationService, modelMapper, passwordEncoder, tokenService, emailService);
    }

    @Test
    void findByEmail() {
        //given
        given(userRepository.findByEmail("abc@abc.pl")).willReturn(prepareUser());

        //when
        Optional<User> byEmail = userService.findByEmail("abc@abc.pl");

        //then
        assertNotNull(byEmail);
        assertThat(byEmail).hasSameClassAs(prepareUser());
        assertEquals(prepareUser().get().getId(), byEmail.get().getId());
        assertEquals(prepareUser().get().getUserName(), byEmail.get().getUserName());
        assertEquals(prepareUser().get().getEmail(), byEmail.get().getEmail());
        assertThat(byEmail.get().getBidding()).isNullOrEmpty();
        assertThat(byEmail.get().getProduct()).isNullOrEmpty();
        assertThat(byEmail.get().getPassword()).isNull();
        assertThat(byEmail.get().getAuthorities()).isNotEmpty();
        assertThat(byEmail.get().getDate()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(byEmail.get().isEnabled()).isFalse();
    }

    @Test
    void findByUsername() {
        //given
        given(userRepository.findByUserName("abc")).willReturn(prepareUser());

        //when
        Optional<User> abc = userService.findByUsername("abc");

        //then
        assertThat(abc).isNotEmpty().isNotNull().hasSameClassAs(prepareUser());
        assertEquals("abc", abc.get().getUserName());
        assertEquals(prepareUser().get().getUserName(), abc.get().getUserName());
        assertEquals(prepareUser().get().getEmail(), abc.get().getEmail());
        assertThat(abc.get().getBidding()).isNullOrEmpty();
        assertThat(abc.get().getProduct()).isNullOrEmpty();
        assertThat(abc.get().getPassword()).isNull();
        assertThat(abc.get().getAuthorities()).isNotEmpty();
        assertThat(abc.get().getDate()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(abc.get().isEnabled()).isFalse();
    }

    @Test
    void save() {
        //given
        given(userRepository.save(any())).willReturn(prepareUser().get());

        //when
        User user = prepareUser().get();
        User save = userService.save(user);

        //then
        assertThat(save).hasSameClassAs(prepareUser().get()).isNotNull();
        assertEquals(prepareUser().get().getId(), save.getId());
        assertEquals(prepareUser().get().getUserName(), save.getUserName());
        assertEquals(prepareUser().get().getEmail(), save.getEmail());
        assertThat(save.getBidding()).isNullOrEmpty();
        assertThat(save.getProduct()).isNullOrEmpty();
        assertThat(save.getPassword()).isNull();
        assertThat(save.getAuthorities()).isNotEmpty();
        assertThat(save.getDate()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(save.isEnabled()).isFalse();
    }

    @Test
    void changeStatus() {
        //given
        given(userRepository.save(any())).willReturn(prepareUser().get());

        //when
        userService.changeStatus(prepareUser().get());

        //then
        verify(userRepository).save(any());
    }

    @Test
    void addUser() {
        //given
        given(userRepository.save(any())).willReturn(prepareUser().get());

        //when
        UserDto userDto = new UserDto();
        userDto.setPassword("sd");
        userDto.setConfirmPassword("sd");
        User save = userService.addUser(userDto, prepareLocationDto(), prepareAddressDto());

        //then
        assertThat(save).hasSameClassAs(prepareUser().get()).isNotNull();
        assertEquals(prepareUser().get().getId(), save.getId());
        assertEquals(prepareUser().get().getUserName(), save.getUserName());
        assertEquals(prepareUser().get().getEmail(), save.getEmail());
        assertThat(save.getBidding()).isNullOrEmpty();
        assertThat(save.getProduct()).isNullOrEmpty();
        assertThat(save.getPassword()).isNull();
        assertThat(save.getAuthorities()).isNotEmpty();
        assertThat(save.getDate()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(save.isEnabled()).isFalse();
    }

    @Test
    void update() {
        //given
        given(userRepository.save(any())).willReturn(prepareUser().get());
        given(locationRepository.save(any())).willReturn(new Location());
        given(addressRepository.save(any())).willReturn(new Address());

        //when
        locationService.update(prepareLocationDto(), prepareAddressDto(), prepareUser().get());
        userService.update(prepareUserDto(), prepareLocationDto(), prepareAddressDto(), prepareUser().get());

        //then
        verify(userRepository).save(any(User.class));
    }


    @Test
    void updatePassword() {
        //given
        given(userRepository.save(any())).willReturn(prepareUser().get());

        //when
        userService.updatePassword(prepareUserDto(), prepareUser().get());

        //then
        verify(userRepository).save(any());
    }

    @Test
    void buyPremium() {
        //given
        given(userRepository.save(any())).willReturn(prepareUser().get());

        //when
        userService.buyPremium(prepareUser().get());

        //then
        verify(userRepository).save(any());
    }

    @Test
    void userEmailExists() {
        //given
        given(userRepository.findByEmail("abc@abc.pl")).willReturn(prepareUser());

        //when
        boolean b = userService.userEmailExists("abc@abc.pl");

        //then
        assertThat(b).isTrue();
    }

    @Test
    void userNameExists() {
        //given
        given(userRepository.findByUserName("abc")).willReturn(prepareUser());

        //when
        boolean b = userService.userNameExists("abc");

        //then
        assertThat(b).isTrue();
    }

    private UserDto prepareUserDto() {
        UserDto userDto = new UserDto();
        userDto.setPassword("sd");
        userDto.setConfirmPassword("sd");
        userDto.setUserName("abc");
        userDto.setEmail("abc");
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