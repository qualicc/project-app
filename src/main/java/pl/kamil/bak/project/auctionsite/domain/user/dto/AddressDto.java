package pl.kamil.bak.project.auctionsite.domain.user.dto;

import javax.validation.constraints.*;

public class AddressDto {
    @NotBlank
    @Size(max = 30, min = 3)
    private String street;
    @Max(300)
    @Min(1)
    @PositiveOrZero
    private String houseNumber;
    @NotBlank(message = "Zip code have to from 5 to 6 sign")
    @Size(min = 5, max = 6)
    private String zipCode;

    public AddressDto(
            @NotBlank @Size(max = 30, min = 3) String street, @Max(300) @Min(1) @PositiveOrZero String houseNumber,
            @NotBlank(message = "Zip code have to from 5 to 6 sign") @Size(min = 5, max = 6) String zipCode) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
