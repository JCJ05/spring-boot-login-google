package com.login.provedores.springweblogin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.login.provedores.springweblogin.model.Provider;
import com.login.provedores.springweblogin.model.Usuario;
import com.login.provedores.springweblogin.repository.UsuarioRepository;

@Service
public class UserService {
   
   @Autowired
   private UsuarioRepository repo;

   public void processOAuthPostLogin(String username) {
        Usuario existUser = repo.findByUsername(username);
     
        if (existUser == null) {

            Usuario newUser = new Usuario();
            newUser.setUsername(username);
            newUser.setProvider(Provider.GOOGLE);
            newUser.setEnabled(true);          
            
            repo.save(newUser);        
        }
     
    }
}