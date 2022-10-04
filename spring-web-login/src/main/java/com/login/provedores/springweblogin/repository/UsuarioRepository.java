package com.login.provedores.springweblogin.repository;

import org.springframework.data.repository.CrudRepository;

import com.login.provedores.springweblogin.model.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario , Long> {

    public Usuario findByUsername(String username);
    
}
