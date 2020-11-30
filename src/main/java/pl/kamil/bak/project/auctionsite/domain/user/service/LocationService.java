package pl.kamil.bak.project.auctionsite.domain.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pl.kamil.bak.project.auctionsite.domain.user.dao.AddressRepository;
import pl.kamil.bak.project.auctionsite.domain.user.dao.LocationRepository;
import pl.kamil.bak.project.auctionsite.domain.user.dto.AddressDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.LocationDto;
import pl.kamil.bak.project.auctionsite.model.userEntity.Address;
import pl.kamil.bak.project.auctionsite.model.userEntity.Location;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final AddressRepository addressRepository;
    private final ModelMapper modelMapper;

    public LocationService(LocationRepository locationRepository, AddressRepository addressRepository, ModelMapper modelMapper) {
        this.locationRepository = locationRepository;
        this.addressRepository = addressRepository;
        this.modelMapper = modelMapper;
    }

    public Location addLocation(LocationDto locationDto, AddressDto addressDto) {
        Location location = new Location();
        Address address = new Address();
        modelMapper.map(locationDto, location);
        modelMapper.map(addressDto, address);
        location.setAddress(addressRepository.save(address));
        return locationRepository.save(location);
    }

    public Location update(LocationDto locationDto, AddressDto addressDto, User user) {
        if (locationDto.getProvince()==null || locationDto.getCity()==null){
            if (locationDto.getCity()==null){
                locationDto.setCity(user.getLocation().getCity());
            }
            locationDto.setProvince(user.getLocation().getProvince());
        }
        if (addressDto.getHouseNumber()==null || addressDto.getStreet()==null || addressDto.getZipCode()==null){
            if (addressDto.getStreet()==null || addressDto.getZipCode()==null){
                if (addressDto.getZipCode()==null){
                    addressDto.setZipCode(user.getLocation().getAddress().getZipCode());
                }
                addressDto.setStreet(user.getLocation().getAddress().getStreet());
            }
            addressDto.setHouseNumber(String.valueOf(user.getLocation().getAddress().getHouseNumber()));
        }
        locationDto.setAddressDto(addressDto);
        modelMapper.map(locationDto, user.getLocation());
        user.getLocation().setAddress(user.getLocation().getAddress());
        user.getLocation().setAddress(addressRepository.save(user.getLocation().getAddress()));
        return locationRepository.save(user.getLocation());

    }
}
