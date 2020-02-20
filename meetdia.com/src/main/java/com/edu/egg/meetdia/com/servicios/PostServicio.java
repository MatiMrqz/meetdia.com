package com.edu.egg.meetdia.com.servicios;

import com.edu.egg.meetdia.com.enumeraciones.Categoria;
import com.edu.egg.meetdia.com.repositorios.PersonaRepositorio;
import com.edu.egg.meetdia.com.repositorios.PostRepositorio;
import com.edu.egg.meetdia.entidades.Multimedia;
import com.edu.egg.meetdia.entidades.Persona;
import com.edu.egg.meetdia.entidades.Post;
import com.edu.egg.meetdia.errores.ErrorServicio;
import java.util.Date;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServicio {
    
    @Autowired
    private PostRepositorio postRepositorio;
    
    @Autowired
    private PersonaRepositorio personaRepositorio;
    
    @Transactional
    public void nuevoPost(String titulo, String descripcion, Multimedia multimedia, String idPersona, Categoria categoria) throws ErrorServicio{
        
        validar(titulo,descripcion,categoria);
        
        Post post = new Post();
        post.setTitulo(titulo);
        post.setFecha_publicacion(new Date());
        post.setMultimedia(multimedia);
        
        Persona persona = personaRepositorio.buscarPersona(idPersona);
        post.setPersona(persona);
        
        post.setCategoria(categoria);
        
        postRepositorio.save(post);
        
    }
    
    public void validar(String titulo, String descripcion,Categoria categoria) throws ErrorServicio{
        if (titulo == null || titulo.isEmpty()){
            throw new ErrorServicio("El titulo no puede ser nulo");
        }
        if (descripcion == null || descripcion.isEmpty()){
            throw new ErrorServicio("La descripcion no debe ser nula");
        }
        if (categoria == null){
            throw new ErrorServicio("La categoría no puede ser nula");
        }
    }
}
