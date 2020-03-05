package com.edu.egg.meetdia.com.controladores;

import com.edu.egg.meetdia.com.entidades.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.edu.egg.meetdia.com.errores.ErrorServicio;
import com.edu.egg.meetdia.com.repositorios.PostRepositorio;
import com.edu.egg.meetdia.com.servicios.PostServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@PreAuthorize("hasAnyRole( 'ROLE_USUARIO_REGISTRADO' )")
@RequestMapping("/muro")
public class MuroControlador {

    @Autowired
    private PostServicio postServicio;

    @Autowired
    private PostRepositorio postRepositorio;
    
    @RequestMapping("")
    //PreAuthorize("hasAnyRole( 'ROLE_USUARIO_REGISTRADO' )")
    @GetMapping
    public String muro(ModelMap modelo) {
        List<Post> listaPost = postRepositorio.mostrarUltimosPost();
        modelo.addAttribute("listapost", listaPost);
        return "muro.html";
    }

    //@PreAuthorize("hasAnyRole( 'ROLE_USUARIO_REGISTRADO' )")
    @GetMapping("/nuevo-post")
    public String editapost(ModelMap modelo) {
        modelo.put("url_success","/muro/publicar");
        modelo.put("boton","Publicar");
        return "nuevo_post.html";
    }

    @PostMapping("/publicar")
    public String nuevoPost(ModelMap modelo, @RequestParam String titulo, @RequestParam String descripcion, @RequestParam String idPersona, MultipartFile archivo, String categoria, String busco_check) {
        boolean busco;
        if (busco_check == null) {
            busco = false;
        } else {
            busco = true;
        }
        try {
            postServicio.nuevoPost(titulo, descripcion, idPersona, archivo, categoria, busco);
            return "forward:/muro/";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("catOpt", categoria);
            modelo.put("titulo", titulo);
            modelo.put("descripcion", descripcion);
            modelo.put("check_value", busco);
            modelo.put("url_success","/muro/publicar");
            modelo.put("boton","Publicar");
            return "nuevo_post.html";
        }
    }

    @GetMapping("/editar_post/{id_post}")
    public String editarPost(ModelMap modelo, @PathVariable("id_post") String id_post) {
        Optional<Post> respuesta = postRepositorio.findById(id_post);
        if (respuesta.isPresent()) {
            Post post = respuesta.get();
            modelo.put("catOpt", post.getCategoria());
            modelo.put("titulo", post.getTitulo());
            modelo.put("descripcion", post.getDescripcion());
            modelo.put("check_value", post.isBusco());
            modelo.put("id_post", post.getId());
            modelo.put("url_success","/muro/modificar_post");
            modelo.put("boton","Guardar");
            return "nuevo_post.html";
        }else{
            modelo.put("titulo", "Error");
            modelo.put("descripcion", "Link inválido");
            modelo.put("url_retorno", "/muro");
            modelo.put("boton", "Muro");
            return "exito.html";
        }
    }
    
    @PostMapping("/modificar_post")
    public String modificarPost(ModelMap modelo, @RequestParam String titulo, @RequestParam String descripcion, MultipartFile archivo, String categoria, String busco_check, @RequestParam String id_post) {
        boolean busco;
        if (busco_check == null) {
            busco = false;
        } else {
            busco = true;
        }
        try {
            postServicio.modificarPost(titulo, descripcion, archivo, categoria, busco, id_post);
            return "forward:/muro/";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("catOpt", categoria);
            modelo.put("titulo", titulo);
            modelo.put("descripcion", descripcion);
            modelo.put("check_value", busco);
            modelo.put("url_success","/muro/publicar");
            modelo.put("boton","Publicar");
            return "nuevo_post.html";
        }
    }

    @GetMapping("/ver_mas/{id_post}")
    public String verMas(ModelMap modelo, @PathVariable("id_post") String id_post) {
        Optional<Post> respuesta = postRepositorio.findById(id_post);
        if (respuesta.isPresent()) {
            Post post = respuesta.get();
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String usermail;
            if(principal instanceof UserDetails){
                usermail = ((UserDetails)principal).getUsername();
            }else{
                usermail = principal.toString();
            }
            if (!usermail.equals(post.getPersona().getEmail())) {
                modelo.put("titulo", post.getTitulo());
                modelo.put("descripcion", post.getDescripcion());
                modelo.put("categoria", post.getCategoria().getDisplayValue());
                modelo.put("image64", post.getMultimedia().getEncoded64());
                modelo.put("fecha", post.getFecha_publicacion().toString());
                modelo.put("nickname", post.getPersona().getNickname());
                modelo.put("profesion",post.getPersona().getProfesion());
                modelo.put("avatar", post.getPersona().getMultimedia().getEncoded64());
                return "post.html";
            } else {
                return "forward:/muro/editar_post/" + id_post;
            }
        } else {
            modelo.put("titulo", "Error");
            modelo.put("descripcion", "Link inválido");
            modelo.put("url_retorno", "/muro");
            modelo.put("boton", "Muro");
            return "exito.html";
        }
    }
}
