package com.login.provedores.springweblogin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.login.provedores.springweblogin.service.CustomOAuth2UserService;
import com.login.provedores.springweblogin.service.JpaUsersDetailsService;
import com.login.provedores.springweblogin.service.UserService;

@EnableMethodSecurity(securedEnabled = true)
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    
    @Autowired
    private JpaUsersDetailsService usersDetailsService;
    
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private CustomOAuth2UserService oauthUserService;
    
    @Autowired
    private UserService userService;
    
   /*  @Bean
    public UserDetailsService userDetailsService() throws Exception{
   
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        
        manager.createUser(User.withUsername("admin").password(passwordEncoder().encode("12345")).roles("admin" , "user").build());
        manager.createUser(User.withUsername("user").password(passwordEncoder().encode("12345")).roles("user").build());

        return manager;
    }*/

    @Autowired
	public void configureGlobal(AuthenticationManagerBuilder builder) throws Exception {
		
		builder.userDetailsService(usersDetailsService)
        .passwordEncoder(encoder);
		
	}

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
            .antMatchers("/prueba","/login", "/oauth/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login")
            .permitAll()
            .and()
            .logout().permitAll()
            .and()
            .exceptionHandling()
            .accessDeniedPage("/error_403")
            .and()
            .oauth2Login()
            .loginPage("/login")
            .userInfoEndpoint()
            .userService(oauthUserService)
            .and()
            .successHandler(new AuthenticationSuccessHandler() {
                @Override
                public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {
            
                    DefaultOidcUser oauthUser = (DefaultOidcUser) authentication.getPrincipal();
                    String email = oauthUser.getAttribute("email");
            
                    userService.processOAuthPostLogin(email);
            
                    response.sendRedirect("/");
                }
            });

        return http.build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.googleClientRegistration());
    }

    private ClientRegistration googleClientRegistration() {
        return CommonOAuth2Provider.GOOGLE.getBuilder("google")
            .clientId("124102762077-lf8554d3f7s2p9bo5o4j4k716a908epk.apps.googleusercontent.com")
            .clientSecret("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")
            .scope("openid", "profile", "email")
            .build();
    }
   
    
}



