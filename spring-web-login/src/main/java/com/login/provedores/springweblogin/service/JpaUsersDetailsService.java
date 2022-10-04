package com.login.provedores.springweblogin.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.login.provedores.springweblogin.model.Role;
import com.login.provedores.springweblogin.model.Usuario;
import com.login.provedores.springweblogin.repository.UsuarioRepository;

@Service
public class JpaUsersDetailsService implements UserDetailsService {
  
    @Autowired
    private UsuarioRepository repository;

    private Logger logger = LoggerFactory.getLogger(JpaUsersDetailsService.class);

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Usuario usuario = repository.findByUsername(username);

        if(usuario == null){
            logger.error("Error usuario no existe: " + username);
            throw new UsernameNotFoundException("Username: " + username + " no existe en el sistema");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : usuario.getRoles()) {
            
            authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
        }

        
        if(authorities.isEmpty()){
            logger.error("Error usuario : " + username + " no tiene roles asignados");
            throw new UsernameNotFoundException("Username: " + username + " no tiene roles asignados");
        }

        return new User(usuario.getUsername(), usuario.getPassword(), usuario.isEnabled() , true , true , true , authorities);
    }
    
}
