package pl.kamil.bak.project.auctionsite.model.userEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "location")
public class Location {
    @Id
    @GeneratedValue
    private Long id;

    private String province;
    private String city;
    @OneToOne
    private Address address;


    public Location() {
    }

    public Long getId() {
        return id;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
