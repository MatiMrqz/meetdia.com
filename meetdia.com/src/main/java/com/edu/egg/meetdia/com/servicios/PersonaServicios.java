package com.edu.egg.meetdia.com.servicios;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.edu.egg.meetdia.com.repositorios.PersonaRepositorio;
import com.edu.egg.meetdia.entidades.Persona;
import com.edu.egg.meetdia.errores.ErrorServicio;






@Service
public class PersonaServicios {

	@Autowired
	private PersonaRepositorio personarepositorio;
	
//	@Autowired
//    private MultimediaServicio MultimediaServicio;

	@Transactional
	public void registrarPersona(MultipartFile archivo, String nombre, String profesion, String domicilio, String codpostal, String ciudad, String email, String alias, String clave, String mime, byte[] contenido)throws ErrorServicio {
		
		validar(nombre, profesion,domicilio,codpostal, ciudad, email, alias, clave);	//ver el orden en que van las validaciones	
		
		validarDupli(nombre, alias);
		
		Persona personanueva = new Persona();
		
		nombre = nombre.toUpperCase();
		personanueva.setNombre(nombre);
		profesion = profesion.toUpperCase();
		personanueva.setProfesion(profesion);
		domicilio = domicilio.toUpperCase();
		personanueva.setDomicilio(domicilio);
		personanueva.setCodpostal(codpostal);
		ciudad = ciudad.toUpperCase();
		personanueva.setCiudad(ciudad);
		personanueva.setEmail(email);
		personanueva.setAlias(alias);
		personanueva.setClave(clave);
		
//		Multimedia multimedia = MultimediaServicio.guardar(archivo); //Hacer cuando este la multimedia
//		personanueva.setContenidoMultimedia(contenidoMultimedia);
		
		personarepositorio.save(personanueva);		
	}
	
	
	
	@Transactional
	public void modificarPersona(String idPersona, String nombre, String profesion, String domicilio, String codpostal, String ciudad, String email, String alias, String clave, String mime, byte[] contenido) throws ErrorServicio {
		
		validar(nombre, profesion,domicilio,codpostal, ciudad, email, alias, clave);
		nombre = nombre.toUpperCase();
		profesion = profesion.toUpperCase();
		domicilio = domicilio.toUpperCase();
		ciudad = ciudad.toUpperCase();
		
		Optional<Persona> respuesta = personarepositorio.findById(idPersona);
		
		if(respuesta.isPresent()) {
			Persona persona = respuesta.get();
			persona.setNombre(nombre);
			persona.setProfesion(profesion);
			persona.setDomicilio(domicilio);
			persona.setCodpostal(codpostal);
			persona.setCiudad(ciudad);
			persona.setEmail(email);
			persona.setAlias(alias);
			persona.setClave(clave);
			
//			String idMultimedia = null;                                          Hacer cuando este la multimedia
//             if(persona.getcontenidoMultimedia() != null){
//                 idMultimedia = persona.getcontenidoMultimedia().getId();
//             }
//             
//             Multimedia multimedia = MultimediaServicio.actualizar(idMultimedia, archivo);
//             persona.setcontenidoMultimedia(contenidoMultimedia);
             
			personarepositorio.save(persona);	
			
		} else {
		throw new ErrorServicio("No se encontro la persona solicitada");
		}
		}
	
	@Transactional
	public void eliminarPersona(String idPersona) throws ErrorServicio {
			
			Persona respuesta = personarepositorio.buscarPersona(idPersona);	
			
			if (respuesta!=null) {		
				personarepositorio.delete(respuesta);		
			}else {
				throw new ErrorServicio("No se encontro la persona solicitada");
			}
			
		}
	public void validar(String nombre, String profesion, String domicilio, String codpostal, String ciudad, String email, String alias, String clave ) throws ErrorServicio {
		if (nombre == null || nombre.isEmpty()) {
			throw new ErrorServicio("El nombre de la persona no puede ser nulo o vacio");
		}
		if (profesion == null || profesion.isEmpty()) {
			throw new ErrorServicio("La profesion de la persona no puede ser nula o vacia");
		}
		if (domicilio == null || domicilio.isEmpty()) {
			throw new ErrorServicio("El domicilio de la persona no puede ser nulo o vacio");
		}
		if (codpostal == null || codpostal.isEmpty()) {
			throw new ErrorServicio("El codigo postal de la persona no puede ser nulo o vacio");
		}
		if (ciudad == null || ciudad.isEmpty()) {
			throw new ErrorServicio("La ciudad correspondiente al domicilio de la persona no puede ser nula o vacia");
		}
		if (email == null || email.isEmpty()) {
			throw new ErrorServicio("El email de la persona no puede ser nulo o vacio");
		}
		if (alias == null || alias.isEmpty()) {
			throw new ErrorServicio("El alias de la persona no puede ser nulo o vacio");
		}
		if (clave == null || clave.isEmpty()) {
			throw new ErrorServicio("La clave de la persona no puede ser nula o vacia");
		}
	}
	
	public void validarDupli(String nombre, String alias) throws ErrorServicio{		
		List<Persona> lista1= personarepositorio.buscarTodos();		
		for(Persona unitario:lista1 ){		  
			if (unitario.getNombre().equals(nombre)){    
		     throw new ErrorServicio ("La persona ya existe en la base de datos"); 
		}	if (unitario.getAlias().equals(alias)){    
		     throw new ErrorServicio ("El alias de La persona ya existe en la base de datos"); 
		}
	}
}
}
		