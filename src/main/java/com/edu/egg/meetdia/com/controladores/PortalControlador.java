package com.edu.egg.meetdia.com.controladores;

import com.edu.egg.meetdia.com.entidades.ConfirmationToken;
import com.edu.egg.meetdia.com.entidades.Persona;
import com.edu.egg.meetdia.com.errores.ErrorServicio;
import com.edu.egg.meetdia.com.repositorios.ConfirmationTokenRepositorio;
import com.edu.egg.meetdia.com.repositorios.PersonaRepositorio;
import com.edu.egg.meetdia.com.servicios.EmailSenderService;
import com.edu.egg.meetdia.com.servicios.PersonaServicio;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private PersonaServicio personaServicio;

    @Autowired
    private PersonaRepositorio personaRepositorio;

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private ConfirmationTokenRepositorio confirmationTokenRepositorio;

    @GetMapping(value = {""})
    public String inicio() {
        return "index.html";
    }

    @GetMapping(value = {"/login"})
    public String login() {
        return "login.html";
    }

    @GetMapping(value = {"/confirm-account"})
    public String confirmUserAccount(ModelMap modelo, @RequestParam String token) {
        ConfirmationToken Actoken = confirmationTokenRepositorio.buscarToken(token);

        if (Actoken != null) {
            Persona persona = personaRepositorio.buscarPersona(Actoken.getPersona().getId());
            persona.setIs_enabled(true);
            personaRepositorio.save(persona);
            Actoken.setConfirmationToken(UUID.randomUUID().toString());
            confirmationTokenRepositorio.save(Actoken);
            modelo.put("titulo", "Cuenta Verificada Correctamente!");
            modelo.put("descripcion", "Presione continuar");
            modelo.put("url_retorno","/login");
            modelo.put("boton","Iniciar Sesion");
        } else {
            modelo.put("titulo", "Error");
            modelo.put("descripcion", "Link inválido o incorrecto, intente nuevamente");
            modelo.put("url_retorno","/");
            modelo.put("boton","Inicio");
        }
        return "exito.html";
    }

    @PostMapping(value = {"/registrar"})
    public String registrar(ModelMap modelo, MultipartFile archivo, @RequestParam String nombre, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String alias, @RequestParam String profesion, @RequestParam String ciudad, String cp) throws ErrorServicio {
        try {
            personaServicio.registrar(archivo, nombre, alias, profesion, ciudad, cp, mail, clave1, clave2);
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("mail", mail);
            modelo.put("alias", alias);
            modelo.put("profesion", profesion);
            modelo.put("ciudad", ciudad);
            modelo.put("cp", cp);
            return "login.html";
        }

        modelo.put("titulo", "Bienvenido a meetdia.com");
        modelo.put("descripcion", "Revisa tu email para verificar tu cuenta");
        modelo.put("url_retorno","/");
        modelo.put("boton","Inicio");
        return "exito.html";
    }

    @GetMapping("/forgot")
    public String forgot_pass(ModelMap modelo, String email) {
        if (email != null) {
            Persona persona = personaRepositorio.buscarPersonaporEmail(email);
            if (persona != null) {
                ConfirmationToken confirmationToken = new ConfirmationToken(persona);
                confirmationTokenRepositorio.save(confirmationToken);
                emailSenderService.sendEmail(persona.getEmail(), "Recuperacion de Contraseña", "Meetdia", "Recupera tu cuenta haciendo click en el siguiente enlace: "
                        + ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() +"/change-password/"+ confirmationToken.getConfirmationToken());
                modelo.put("titulo", "Hemos enviado un link a tu mail");
                modelo.put("descripcion", "Revisa tu casilla para continuar");
                modelo.put("url_retorno", "/");
                modelo.put("boton", "Inicio");
                return "exito.html";
            }else{
                modelo.put("titulo", "Error");
                modelo.put("descripcion", "No se encontró el usuario");
                modelo.put("url_retorno","/forgot");
                modelo.put("boton","Volver");
                return "exito.html";
            }
        }
        return "forgot.html";
    }

    @RequestMapping(value = {"/change-password/{token}"})
    public String changePassword(ModelMap modelo, @PathVariable("token") String token) {
        ConfirmationToken Actoken = confirmationTokenRepositorio.buscarToken(token);
        if (Actoken != null) {
            modelo.put("token", token);
            return "change-password.html";
        } else {
            modelo.put("titulo", "Error");
            modelo.put("descripcion", "Link inválido o incorrecto, intente nuevamente");
            modelo.put("url_retorno","/forgot");
            modelo.put("boton", "Enviar mail de nuevo");
            return "exito.html";
        }
    }

    @PostMapping(value = {"/change-password"})
    public String changePassword(ModelMap modelo, @RequestParam String token, @RequestParam String clave1, @RequestParam String clave2) {
        ConfirmationToken Actoken = confirmationTokenRepositorio.buscarToken(token);
        try {
            personaServicio.modificarContraseña(Actoken.getPersona(), clave1, clave2);
            Actoken.setConfirmationToken(UUID.randomUUID().toString());
            confirmationTokenRepositorio.save(Actoken);
            modelo.put("titulo", "Cambio correcto!");
            modelo.put("descripcion", "La contraseña ha sido cambiada correctamente. Puede iniciar sesión.");
            modelo.put("url_retorno", "/login");
            modelo.put("boton","Iniciar Sesión");
            return "exito.html";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "forward:/change-password/"+token;
        }
    }
}
