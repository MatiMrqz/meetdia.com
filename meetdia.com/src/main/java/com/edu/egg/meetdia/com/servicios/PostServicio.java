package com.edu.egg.meetdia.com.servicios;

import com.edu.egg.meetdia.com.enumeraciones.Categoria;
import com.edu.egg.meetdia.com.repositorios.PersonaRepositorio;
import com.edu.egg.meetdia.com.repositorios.PostRepositorio;
import com.edu.egg.meetdia.entidades.Post;
import com.edu.egg.meetdia.errores.ErrorServicio;
import java.util.Date;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PostServicio {
    
    @Autowired
    PostRepositorio postRepositorio;
    
    @Autowired
    PersonaRepositorio personaRepositorio;
    
    @Autowired
    MultimediaServicio ms;
    
    @Transactional
    public void nuevoPost(String titulo, String descripcion, String idPersona, MultipartFile archivo, Categoria categoria) throws ErrorServicio{
        validar(titulo, descripcion, categoria);
        Post post = new Post();
        post.setTitulo(titulo);
        post.setDescripcion(descripcion);
        post.setPersona(personaRepositorio.buscarPersona(idPersona));
        post.setFecha_publicacion(new Date());
        post.setMultimedia(ms.guardar(archivo));
        post.setCategoria(categoria);
        postRepositorio.save(post);
    }
    private void validar(String titulo, String descripcion,Categoria categoria) throws ErrorServicio{
        if (titulo==null || titulo.isEmpty()){
            throw new ErrorServicio("El titulo no puede ser nulo");
        }
        if (descripcion == null || descripcion.isEmpty()){
            throw new ErrorServicio("La descripcion no puede ser nula");
        }
        if (categoria == null){
            throw new ErrorServicio("La categoria debe ser seleccionada");
        }
    }
}
