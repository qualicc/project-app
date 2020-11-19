package pl.kamil.bak.project.auctionsite.domain.user.controllers;


import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.kamil.bak.project.auctionsite.domain.provider.session.UserSessionProvider;
import pl.kamil.bak.project.auctionsite.domain.user.service.UserService;
import pl.kamil.bak.project.auctionsite.domain.user.dto.AddressDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.LocationDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.UserDto;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;


import javax.validation.Valid;

@Controller
public class UserController {
    private final UserService userService;
    private final UserSessionProvider getPrincipal;


    public UserController(UserService userService, UserSessionProvider getPrincipal) {
        this.userService = userService;
        this.getPrincipal = getPrincipal;
    }

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder) {
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @GetMapping("/owner")
    @ResponseBody
    public String getNameOwner() {
        return getPrincipal.getPrincipal().getUserName();
    }


    @GetMapping("/sing-up")
    public String register(@ModelAttribute UserDto userDto, Model model) {
        model.addAttribute("user", userDto);
        return "sing-up";
    }

    @GetMapping("/login")
    public String login() {
        User user = getPrincipal.getPrincipal();
        if (user != null) {
            return "main-page";
        }
        return "login";
    }


    @PostMapping("/sing-up")
    public String save(@Valid UserDto userDto, BindingResult result, RedirectAttributes redirectAttributes, LocationDto location, AddressDto address) {
        if (userService.userEmailExists(userDto.getEmail())) {
            result.addError(new FieldError("userDto", "email", "Email address already exist"));
        }
        if (userService.userNameExists(userDto.getUserName())){
            result.addError(new FieldError("userDto", "userName", "A user with that name already exists"));
        }
        if (userDto.getPassword() != null && userDto.getConfirmPassword() != null) {
            if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
                result.addError(new FieldError("userDto", "confirmPassword", "Password must match"));
            }
        }
        if (result.hasErrors()) {
            return "sing-up";
        }
        redirectAttributes.addFlashAttribute("message", "Success! Your registration is now complete, pleas check your email to activate your account!");
        userService.addUser(userDto, location, address);

        return "redirect:/login";
    }


}