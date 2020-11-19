package pl.kamil.bak.project.auctionsite.domain.user.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LocationDto {
    @NotBlank
    @Size(max = 48, min = 3)
    private String province;
    @NotBlank
    @Size(max = 50, min = 3)
    private String city;
    private AddressDto addressDto;

    public LocationDto(@NotBlank @Size(max = 48, min = 3) String province, @NotBlank @Size(max = 50, min = 3) String city, AddressDto addressDto) {
        this.province = province;
        this.city = city;
        this.addressDto = addressDto;
    }

    public AddressDto getAddressDto() {
        return addressDto;
    }

    public void setAddressDto(AddressDto addressDto) {
        this.addressDto = addressDto;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
