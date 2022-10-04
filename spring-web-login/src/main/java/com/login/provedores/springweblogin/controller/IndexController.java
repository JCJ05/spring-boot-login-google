package com.login.provedores.springweblogin.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    
    @RequestMapping(path = {"" , "/" , "/index"})
    public String Index(Model model){
      
        model.addAttribute("titulo", "Inicio de sesion with Google and Others Providers");

        return "index";

    }
    

    @RequestMapping(path = "/prueba")
    public String prueba(Model model){

        return "prueba";
    }
    
    @Secured("ROLE_admin")
    @RequestMapping(path = "/usuario")
    public String usuario(Model model){
        
        return "usuario";
    }

}
