package com.edu.egg.meetdia.com.controladores;

import com.edu.egg.meetdia.com.entidades.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.edu.egg.meetdia.com.enumeraciones.Categoria;
import com.edu.egg.meetdia.com.errores.ErrorServicio;
import com.edu.egg.meetdia.com.repositorios.PostRepositorio;
import com.edu.egg.meetdia.com.servicios.PostServicio;
import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/muro")
public class MuroControlador {

    @Autowired
    private PostServicio postServicio;
    
    @Autowired
    private PostRepositorio postRepositorio;
    
    @RequestMapping("")
    @PreAuthorize("hasAnyRole( 'ROLE_USUARIO_REGISTRADO' )")
    @GetMapping
    public String muro(ModelMap modelo) {
        List<Post> listaPost = postRepositorio.mostrarUltimosPost();
        modelo.addAttribute("listapost", listaPost);
        return "muro.html";
    }
    
    @PreAuthorize("hasAnyRole( 'ROLE_USUARIO_REGISTRADO' )")
    @GetMapping("/index")
    public String index2(ModelMap modelo){
        modelo.put("titulo", "UD esta adentro");
        modelo.put("descripcion", "ud esta en el muro");
        return "exito.html";
    }
    
    @PostMapping("/muro/agregarPost")
    public String nuevoPost(ModelMap modelo, @RequestParam String titulo, @RequestParam String descripcion, @RequestParam String idPersona, @RequestParam MultipartFile archivo, @RequestParam Categoria categoria) {
        try {
            postServicio.nuevoPost(titulo, descripcion, idPersona, archivo, categoria);
            modelo.put("titulo", "POST");
            modelo.put("descripcion", "Creado de Forma Correcta!");
            return "exito.html";
        } catch (ErrorServicio ex) {
            modelo.put("error", ex.getMessage());
            return "muro.html";
        }
    }
}
