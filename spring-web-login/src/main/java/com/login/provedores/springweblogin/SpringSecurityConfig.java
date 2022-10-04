package com.login.provedores.springweblogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.login.provedores.springweblogin.service.JpaUsersDetailsService;

@EnableMethodSecurity(securedEnabled = true)
@Configuration
public class SpringSecurityConfig {
    
    @Autowired
    private JpaUsersDetailsService usersDetailsService;
    
    @Autowired
    private BCryptPasswordEncoder encoder;
    
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
            .antMatchers("/prueba").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().loginPage("/login")
            .permitAll()
            .and()
            .logout().permitAll()
            .and()
            .exceptionHandling()
            .accessDeniedPage("/error_403");

        return http.build();
    }
    
}



