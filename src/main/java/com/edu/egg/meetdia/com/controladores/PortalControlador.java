package com.edu.egg.meetdia.com.controladores;

import com.edu.egg.meetdia.com.entidades.ConfirmationToken;
import com.edu.egg.meetdia.com.entidades.Persona;
import com.edu.egg.meetdia.com.entidades.Post;
import com.edu.egg.meetdia.com.errores.ErrorServicio;
import com.edu.egg.meetdia.com.repositorios.ConfirmationTokenRepositorio;
import com.edu.egg.meetdia.com.repositorios.PersonaRepositorio;
import com.edu.egg.meetdia.com.repositorios.PostRepositorio;
import com.edu.egg.meetdia.com.servicios.PersonaServicio;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class PortalControlador {
    
    @Autowired
    private PersonaServicio personaServicio;
    
    @Autowired
    private PersonaRepositorio personaRepositorio;
    
    @Autowired
    private PostRepositorio postRepositorio;
    
    @Autowired
    private ConfirmationTokenRepositorio confirmationTokenRepositorio;
    
    @GetMapping("/")
    public String inicio(){
        return "index.html";
    }
    
    @GetMapping("/login")
    public String login(){
        return "login.html";
    }
    
    @PreAuthorize("hasAnyRole( 'ROLE_USUARIO_REGISTRADO' )")
    @GetMapping("/muro")
    public String muro(ModelMap modelo){
        List<Post> listaPost = postRepositorio.mostrarUltimosPost();
        modelo.addAttribute("listapost", listaPost);
        
        return "muro.html";
    }
    
    @GetMapping("/confirm-account")
    public String confirmUserAccount(ModelMap modelo, @RequestParam String token){
        ConfirmationToken Actoken = confirmationTokenRepositorio.buscarToken(token);
        
        if(Actoken != null){
            Persona persona = personaRepositorio.buscarPersona(Actoken.getPersona().getId());
            persona.setIs_enabled(true);
            personaRepositorio.save(persona);
            Actoken.setConfirmationToken(UUID.randomUUID().toString());
            confirmationTokenRepositorio.save(Actoken);
            modelo.put("titulo","Cuenta Verificada Correctamente!");
            modelo.put("descripcion","Presione continuar");
        } else {
            modelo.put("titulo","Error");
            modelo.put("descripcion","Link inválido o incorrecto, intente nuevamente");
        }
        return "confirm-account.html";
    }
    
    @PostMapping("/registrar")
    public String registrar(ModelMap modelo,MultipartFile archivo, @RequestParam String nombre, @RequestParam String mail, @RequestParam String clave1, @RequestParam String clave2, @RequestParam String alias,@RequestParam String profesion,@RequestParam String ciudad, String cp) throws ErrorServicio{
        try{
        personaServicio.registrar(archivo, nombre, alias, profesion, ciudad, cp, mail, clave1, clave2);
        }catch (ErrorServicio ex){
            modelo.put("error", ex.getMessage());
            modelo.put("nombre",nombre);
            modelo.put("mail", mail);
            modelo.put("alias",alias);
            modelo.put("profesion", profesion);
            modelo.put("ciudad",ciudad);
            modelo.put("cp", cp);
            return "login.html";
        }
        
        modelo.put("titulo", "Bienvenido a meetdia.com");
        modelo.put("descripcion", "Revisa tu email para verificar tu cuenta");
        return "exito.html";
    }
}
