package com.edu.egg.meetdia.com.controladores;

import com.edu.egg.meetdia.com.servicios.PersonaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class PortalControlador {
    @Autowired
    private PersonaServicio personaServicio;
    
    @GetMapping("/")
    public String inicio(){
        return ""
    }
}
