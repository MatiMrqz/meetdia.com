package com.edu.egg.meetdia.com.servicios;

import com.edu.egg.meetdia.com.entidades.Post;
import com.edu.egg.meetdia.com.enumeraciones.Categoria;
import com.edu.egg.meetdia.com.errores.ErrorServicio;
import com.edu.egg.meetdia.com.repositorios.PersonaRepositorio;
import com.edu.egg.meetdia.com.repositorios.PostRepositorio;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
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
    public void nuevoPost(String titulo, String descripcion, String idPersona, MultipartFile archivo, String categoria, boolean busco) throws ErrorServicio {
        validar(titulo, descripcion, categoria);
        Post post = new Post();
        post.setTitulo(titulo);
        post.setDescripcion(descripcion);
        post.setPersona(personaRepositorio.buscarPersona(idPersona));
        post.setFecha_publicacion(new Date());
        post.setMultimedia(ms.guardar(archivo));
        post.setCategoria(Categoria.valueOf(categoria));
        post.setBusco(busco);
        postRepositorio.save(post);
    }

    @Transactional
    public void modificarPost(String titulo, String descripcion, MultipartFile archivo, String categoria, boolean busco, String idPost) throws ErrorServicio {
        validar(titulo, descripcion, categoria);
        Optional<Post> respuesta = postRepositorio.findById(idPost);
        if (respuesta.isPresent()) {
            Post post = respuesta.get();
            post.setTitulo(titulo);
            post.setDescripcion(descripcion);
            post.setMultimedia(ms.actualizar(archivo, post.getMultimedia().getId()));
            post.setCategoria(Categoria.valueOf(categoria));
            post.setBusco(busco);
            postRepositorio.save(post);
        }

    }

    private void validar(String titulo, String descripcion, String categoria) throws ErrorServicio {
        if (titulo == null || titulo.isEmpty()) {
            throw new ErrorServicio("Debe ingresar un titulo");
        }
        if (descripcion == null || descripcion.isEmpty()) {
            throw new ErrorServicio("Debe ingresar una descripcion");
        }
        if (categoria == null || categoria.isEmpty()) {
            throw new ErrorServicio("La categoria debe ser seleccionada");
        }
    }

    public String getElapsedTime(Date created) {
        long duration = System.currentTimeMillis() - created.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long days = TimeUnit.MILLISECONDS.toDays(duration);
        long hours = TimeUnit.MILLISECONDS.toHours(duration);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        if (days > 0) {
            return "Hace " + days + " dias";
        }
        if (hours > 0) {
            return "Hace " + hours + " horas";
        }
        if (minutes > 0) {
            return "Hace " + minutes + " minutos";
        }

        if (seconds > 0) {
            return "Hace " + seconds + " segundos";
        }
        return "error en elapsedtime";
    }
}
