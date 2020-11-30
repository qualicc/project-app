package pl.kamil.bak.project.auctionsite.domain.user.controllers;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.kamil.bak.project.auctionsite.domain.provider.session.UserSessionProvider;
import pl.kamil.bak.project.auctionsite.domain.user.service.UserService;
import pl.kamil.bak.project.auctionsite.domain.user.dto.AddressDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.LocationDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.UserDto;

@Controller
public class UserAccountController {
    private final UserService userService;
    private final PasswordEncoder encoder;
    private final UserSessionProvider getPrincipal;

    public UserAccountController(UserService userService, PasswordEncoder encoder, UserSessionProvider getPrincipal) {
        this.userService = userService;
        this.encoder = encoder;
        this.getPrincipal = getPrincipal;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/user/account")
    public String userAccount(@ModelAttribute UserDto userDto, Model model) {
        model.addAttribute("email", getPrincipal.getPrincipal().getEmail());
        model.addAttribute("userName", getPrincipal.getPrincipal().getUserName());
        model.addAttribute("province", getPrincipal.getPrincipal().getLocation().getProvince());
        model.addAttribute("city", getPrincipal.getPrincipal().getLocation().getCity());
        model.addAttribute("street", getPrincipal.getPrincipal().getLocation().getAddress().getStreet());
        model.addAttribute("houseNumber", getPrincipal.getPrincipal().getLocation().getAddress().getHouseNumber());
        model.addAttribute("zip", getPrincipal.getPrincipal().getLocation().getAddress().getZipCode());
        return "user-account";
    }

    @PostMapping("/user/account/details")
    public String userUpdate(UserDto userDto, BindingResult result, RedirectAttributes redirectAttributes, LocationDto location, AddressDto address) {
        if (userService.userEmailExists(userDto.getEmail())) {
            result.addError(new FieldError("userDto", "email", "Email address already exist"));
        }
        if (userService.userNameExists(userDto.getUserName())) {
            result.addError(new FieldError("userDto", "userName", "A user with that name already exists"));
        }
        redirectAttributes.addFlashAttribute("message1", "Success! Your data account has been changed successfully");
        userService.update(userDto, location, address, getPrincipal.getPrincipal());
        return "redirect:/user/account";
    }

    @PostMapping("/user/account/security")
    public String passwordUpdate(UserDto userDto, BindingResult result, RedirectAttributes redirectAttributes) {
        if (encoder.matches(userDto.getPassword(), getPrincipal.getPrincipal().getPassword())) {
            result.addError(new FieldError("userDto", "oldPassword", "Password not match to old password"));
        }
        if (userDto.getPassword() != null && userDto.getConfirmPassword() != null) {
            if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
                result.addError(new FieldError("userDto", "confirmPassword", "Password must match"));
            }
        }
        if (result.hasErrors()) {
            return "user-account";
        }
        redirectAttributes.addFlashAttribute("message2", "Success! Your password has been changed successfully");
        userService.updatePassword(userDto, getPrincipal.getPrincipal());
        return "redirect:/user/account";
    }

}
