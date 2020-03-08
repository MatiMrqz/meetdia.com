package com.edu.egg.meetdia.com.servicios;

import com.edu.egg.meetdia.com.entidades.Mensaje;
import com.edu.egg.meetdia.com.entidades.Persona;
import com.edu.egg.meetdia.com.errores.ErrorServicio;
import com.edu.egg.meetdia.com.repositorios.MensajeRepositorio;
import com.edu.egg.meetdia.com.repositorios.PersonaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class MensajeServicio {

    @Autowired
    private MensajeRepositorio mensajeRepositorio;

    @Autowired
    private PersonaRepositorio personaRepositorio;

    @Transactional
    public Mensaje crearMensaje(String contenido, String id_receptor) throws ErrorServicio {
        if (contenido == null || contenido.isEmpty()) {
            throw new ErrorServicio("No se puede crear un mensaje vacio");
        }
        Mensaje mensaje = new Mensaje();
        mensaje.setContenido(contenido);
        mensaje.setFecha(new Date());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String usermail;
        if (principal instanceof UserDetails) {
            usermail = ((UserDetails) principal).getUsername();
        } else {
            usermail = principal.toString();
        }
        Persona emite = personaRepositorio.buscarPersonaporEmail(usermail);
        Persona recibe = personaRepositorio.buscarPersona(id_receptor);
        if (emite == null) {
            throw new ErrorServicio("No se encontró el id de usuario emisor");
        }
        mensaje.setEmisor(emite);
        if (recibe == null) {
            throw new ErrorServicio("No se encontró el id de usuario receptor");
        }
        mensaje.setReceptor(recibe);
        mensajeRepositorio.save(mensaje);
        return mensaje;
    }

    public List<Persona> buscaChats() throws ErrorServicio {
        Persona usuario = usuarioLogueado();
        List<Mensaje> Personas = mensajeRepositorio.chatsde(usuario);
        List<Persona> chats_con = new ArrayList<>();
        for (Mensaje Persona : Personas) {
            if (!Persona.getEmisor().getId().equals(usuario.getId())) {
                chats_con.add(Persona.getEmisor());
            } else {
                chats_con.add(Persona.getReceptor());
            }
        }
        return chats_con;
    }

    public List<Mensaje> buscaMensajes(Persona receptor) {
        Persona emisor = usuarioLogueado();
        List<Mensaje> chats = mensajeRepositorio.buscarMensaje2personas(emisor, receptor);
        return chats;
    }

    private Persona usuarioLogueado() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String usermail;
        if (principal instanceof UserDetails) {
            usermail = ((UserDetails) principal).getUsername();
        } else {
            usermail = principal.toString();
        }
        Persona user = personaRepositorio.buscarPersonaporEmail(usermail);
        return user;
    }
}