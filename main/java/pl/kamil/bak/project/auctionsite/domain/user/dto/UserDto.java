package pl.kamil.bak.project.auctionsite.domain.user.dto;

import org.hibernate.validator.constraints.Length;
import pl.kamil.bak.project.auctionsite.model.userEntity.Location;

import javax.validation.constraints.*;


public class UserDto {
    @NotBlank
    @Size(max = 26, min = 3)
    private String userName;
    @NotBlank(message = "Enter your email")
    @Email(message = "Enter a valid email address")
    @Size(min = 8, max = 20)
    private String email;
    @NotBlank(message = "enter your password")
    @Length(min = 4, message = "Password must be 4")
    private String password;
    @NotBlank(message = "Re you password")
    @Length(min = 4, message = "Password must be 4")
    private String confirmPassword;
    private String oldPassword;
    private Location location;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocationDto() {
        return location;
    }

    public void setLocationDto(Location location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                '}';
    }
}
