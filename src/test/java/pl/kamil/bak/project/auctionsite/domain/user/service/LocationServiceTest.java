package pl.kamil.bak.project.auctionsite.domain.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import pl.kamil.bak.project.auctionsite.domain.user.dao.AddressRepository;
import pl.kamil.bak.project.auctionsite.domain.user.dao.LocationRepository;
import pl.kamil.bak.project.auctionsite.domain.user.dto.AddressDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.LocationDto;
import pl.kamil.bak.project.auctionsite.model.userEntity.Address;
import pl.kamil.bak.project.auctionsite.model.userEntity.Location;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private AddressRepository addressRepository;

    private LocationService locationService;


    @BeforeEach
    public void init(){
        ModelMapper modelMapper = new ModelMapper();
        locationService = new LocationService(locationRepository,addressRepository,modelMapper);
    }

    @Test
    void shouldBeAddLocationAndDontHaveToReturnNull() {
        //given
        given(locationRepository.save(any())).willReturn(prepareLocation());

        //when
        Location location = locationService.addLocation(prepareLocationDto(), prepareAddressDto());

        //then
        assertThat(location).isNotNull().isNotSameAs(prepareLocationDto());
        assertThat(location.getAddress()).isNotNull();
        assertThat(location.getCity()).isNotBlank().isNotEmpty().isEqualTo("abc");
        assertThat(location.getProvince()).isNotBlank().isNotEmpty().isEqualTo("abc");
    }

    @Test
    void shouldBeReturnUpdateObjectOtherThanPresent() {
        //given
        given(locationRepository.save(any())).willReturn(prepareLocation());

        //when
        User user = new User();
        user.setLocation(prepareLocation());
        Location update = locationService.update(prepareLocationDto(), prepareAddressDto(), user);

        //then
        assertThat(update).isNotNull().isNotSameAs(prepareLocationDto());
        assertThat(update.getAddress()).isNotNull();
        assertThat(update.getCity()).isNotBlank().isNotEmpty().isEqualTo("abc");
        assertThat(update.getProvince()).isNotBlank().isNotEmpty().isEqualTo("abc");
    }

    private AddressDto prepareAddressDto(){
        return new AddressDto("abc", "1", "32-567");
    }

    private LocationDto prepareLocationDto(){
        return new LocationDto("abc", "abc", prepareAddressDto());
    }

    private Location prepareLocation(){
        Location location = new Location();
        Address address = new Address();
        address.setHouseNumber(1);
        address.setStreet("abc");
        address.setZipCode("12-234");
        location.setCity("abc");
        location.setProvince("abc");
        location.setAddress(address);
        return location;
    }

}