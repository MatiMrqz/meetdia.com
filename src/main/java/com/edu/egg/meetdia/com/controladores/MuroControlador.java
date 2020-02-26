package com.edu.egg.meetdia.com.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.edu.egg.meetdia.com.enumeraciones.Categoria;
import com.edu.egg.meetdia.com.errores.ErrorServicio;
import com.edu.egg.meetdia.com.servicios.PostServicio;


@Controller
@RequestMapping(value="/muro")
public class MuroControlador {
	

@Autowired
private PostServicio postServicio;

	

		
	@PostMapping("/agregarPost")
		public String nuevoPost(ModelMap modelo, @RequestParam String titulo, @RequestParam String descripcion, @RequestParam String idPersona, @RequestParam MultipartFile archivo, @RequestParam Categoria categoria){
			try {				
				postServicio.nuevoPost(titulo, descripcion, idPersona, archivo,categoria);	
				modelo.put("titulo", "POST");
				modelo.put("descripcion", "Creado de Forma Correcta!");
				return "aviso.html";
			} catch (ErrorServicio ex) {	
				modelo.put("error", ex.getMessage());
				return "muro.html";
			}
	}			
}