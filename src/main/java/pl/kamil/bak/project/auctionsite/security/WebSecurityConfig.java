package pl.kamil.bak.project.auctionsite.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import pl.kamil.bak.project.auctionsite.domain.user.service.UserService;
import pl.kamil.bak.project.auctionsite.model.enums.Type;

import java.util.concurrent.TimeUnit;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;

    public WebSecurityConfig(UserService userService) {
        this.userService = userService;

    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userService.findByEmail(email).orElseThrow(() -> {
            throw new UsernameNotFoundException("No user Found with email: " + email);
        });
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/bidding", "/products/owned").authenticated()
                .antMatchers(HttpMethod.GET, "/products", "/products/{name}").permitAll()
                .antMatchers(HttpMethod.POST, "/bidding").authenticated()
                .antMatchers(HttpMethod.POST, "/products").authenticated()
                .antMatchers(HttpMethod.PUT, "/products/{id}").authenticated()
                .antMatchers(HttpMethod.DELETE, "/products/{id}").authenticated()
                .antMatchers("/", "/sign-up", "/activation", "/css/**", "/main-page", "/assets/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //TODO restore the commented parts
//                .permitAll()
//                .and()
//                .httpBasic()
//                .and()
//                .csrf().disable();
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/main-page", true)
                .passwordParameter("password")
                .usernameParameter("username")
                .and()
                .rememberMe()
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                .key("userSecret!")
                .rememberMeParameter("remember-me")
                .and()
                .logout()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me").permitAll()
                .and()
                .cors();
    }
}
