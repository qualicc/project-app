package pl.kamil.bak.project.auctionsite.domain.user.service;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kamil.bak.project.auctionsite.domain.token.service.TokenService;
import pl.kamil.bak.project.auctionsite.domain.user.dao.UserRepository;
import pl.kamil.bak.project.auctionsite.domain.user.dto.AddressDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.LocationDto;
import pl.kamil.bak.project.auctionsite.domain.user.dto.UserDto;
import pl.kamil.bak.project.auctionsite.model.userEntity.User;
import pl.kamil.bak.project.auctionsite.model.enums.Status;
import pl.kamil.bak.project.auctionsite.model.enums.Type;

import java.util.Optional;
import java.util.UUID;


@Service
public class UserService {
    private final UserRepository userRepository;
    private final LocationService locationService;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EmailService emailService;

    public UserService(UserRepository userRepository, LocationService locationService, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder, TokenService tokenService, EmailService emailService) {
        this.userRepository = userRepository;
        this.locationService = locationService;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    @Transactional
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public Optional<User> findByUsername(String name){
        return userRepository.findByUserName(name);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void changeStatus(User user){
        user.setStatus(Status.ACTIVE);
        save(user);
    }



    public User addUser(UserDto userDto, LocationDto locationDto, AddressDto addressDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        User user = new User();
        modelMapper.map(userDto, user);
        user.setLocation(locationService.addLocation(locationDto, addressDto));
        Optional<User> saved = Optional.of(save(user));
        saved.ifPresent(u -> {
            try {
                String token = UUID.randomUUID().toString();
                tokenService.save(saved.get(), token);

                emailService.sendHtmlMail(u);
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        return saved.get();
    }

    public void update (UserDto userDto,LocationDto locationDto, AddressDto addressDto, User user){
        if (userDto.getEmail()==null || userDto.getUserName()==null){
            if (userDto.getUserName()==null){
               userDto.setUserName(user.getUserName());
            }
            userDto.setEmail(user.getEmail());
        }
        user.setEmail(userDto.getEmail());
        user.setUserName(userDto.getUserName());
        user.setLocation(locationService.update(locationDto,addressDto, user));
        userRepository.save(user);
    }

    public void updatePassword(UserDto userDto, User user){
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    public void buyPremium(User user){
        if (user.getType().equals(Type.NORMAL)){
            user.setType(Type.PREMIUM);
        }else {
            user.setType(Type.NORMAL);
        }
        userRepository.save(user);
    }

    public boolean userEmailExists(String email){
        return findByEmail(email).isPresent();
    }

    public boolean userNameExists(String userName){
        return findByUsername(userName).isPresent();
    }


}
