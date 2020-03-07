package com.edu.egg.meetdia.com.controladores;

import com.edu.egg.meetdia.com.entidades.Mensaje;
import com.edu.egg.meetdia.com.entidades.Persona;
import com.edu.egg.meetdia.com.errores.ErrorServicio;
import com.edu.egg.meetdia.com.repositorios.MensajeRepositorio;
import com.edu.egg.meetdia.com.repositorios.PersonaRepositorio;
import com.edu.egg.meetdia.com.servicios.MensajeServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@PreAuthorize("hasAnyRole( 'ROLE_USUARIO_REGISTRADO' )")
@RequestMapping("/buzon")
public class MensajesControlador {
    
    @Autowired
    private MensajeServicio mensajeServicio;
    
    @Autowired
    private MensajeRepositorio mensajeRepositorio;
    
    @Autowired
    private PersonaRepositorio personaRepositorio;
    
    @RequestMapping("")
    @GetMapping
    public String buzon(ModelMap modelo){
        try{
        List<Persona> usuarios = mensajeServicio.buscaChats();
        modelo.addAttribute("lista_chats",usuarios);
        }catch (ErrorServicio ex){
            modelo.put("titulo", "Error");
            modelo.put("descripcion", ex.getMessage());
            modelo.put("url_retorno", "/muro");
            modelo.put("boton", "Muro");
            return "exito.html";
        }
        return "buzonv2.html";
    }
    
//    @PreAuthorize("hasAnyRole( 'ROLE_USUARIO_REGISTRADO' )")
    @PostMapping("/nuevomensaje")
    public String nuevoMensaje(ModelMap modelo,@RequestParam String id_receptor,@RequestParam String contenido){
        try{
            Mensaje enviado = mensajeServicio.crearMensaje(contenido,id_receptor);
            return "redirect:/buzon/ver_mas/"+enviado.getReceptor().getId();
        }catch (ErrorServicio ex){
            modelo.put("titulo", "Error");
            modelo.put("descripcion", ex.getMessage());
            modelo.put("url_retorno", "/muro");
            modelo.put("boton", "Muro");
            return "exito.html";
        }
    }
    
    @PreAuthorize("hasAnyRole( 'ROLE_USUARIO_REGISTRADO' )")
    @GetMapping("/ver_mas/{id_user}")
    public String verMas(ModelMap modelo, @PathVariable("id_user") String id_user){
        Persona receptor = personaRepositorio.buscarPersona(id_user);
        List<Mensaje> mensajes =  mensajeServicio.buscaMensajes(receptor);
//        for (Mensaje mensaje : mensajes) {
//            System.out.println("Usuario emisor:"+mensaje.getEmisor().getNickname()+" Contenido: "+mensaje.getContenido());
//        }
        modelo.addAttribute("listamsj",mensajes);
        return "mensajes.html";
    }
}
