package com.edu.egg.meetdia.com.servicios;

import com.edu.egg.meetdia.com.entidades.Persona;
import com.edu.egg.meetdia.com.errores.ErrorServicio;
import com.edu.egg.meetdia.com.repositorios.PersonaRepositorio;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PersonaServicio implements UserDetailsService {

    @Autowired
    private PersonaRepositorio personaRepositorio;

    @Autowired
    private MultimediaServicio ms;

    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String nickname, String profesion, String ciudad, String email, String clave) throws ErrorServicio {
        validar(nombre, nickname, profesion, ciudad, email, clave, clave);

        Persona persona = new Persona();
        persona.setNombre(nombre);
        persona.setNickname(nickname);
        persona.setEmail(email);
        persona.setProfesion(profesion);
        persona.setCiudad(ciudad);
        persona.setMultimedia(ms.guardar(archivo));

        String encriptada = new BCryptPasswordEncoder().encode(clave);
        persona.setClave(encriptada);

        personaRepositorio.save(persona);
    }

    @Transactional
    public void eliminarPersona(String idPersona) throws ErrorServicio {

        Persona respuesta = personaRepositorio.buscarPersona(idPersona);

        if (respuesta != null) {
            personaRepositorio.delete(respuesta);
        } else {
            throw new ErrorServicio("No se encontro la persona solicitada");
        }
    }

    @Transactional
    public void modificarPersona(String idPersona, String nombre, String profesion, String domicilio, String codpostal, String ciudad, String email, String alias, String clave, String mime, byte[] contenido) throws ErrorServicio {

        validar(nombre, alias, profesion, ciudad, email, clave, clave);
        nombre = nombre.toUpperCase();
        profesion = profesion.toUpperCase();
        domicilio = domicilio.toUpperCase();
        ciudad = ciudad.toUpperCase();

        Optional<Persona> respuesta = personaRepositorio.findById(idPersona);

        if (respuesta.isPresent()) {
            Persona persona = respuesta.get();
            persona.setNombre(nombre);
            persona.setProfesion(profesion);
            persona.setDomicilio(domicilio);
            persona.setCodpostal(codpostal);
            persona.setCiudad(ciudad);
            persona.setEmail(email);
            persona.setNickname(alias);
            persona.setClave(clave);

//			String idMultimedia = null;                                          Hacer cuando este la multimedia
//             if(persona.getcontenidoMultimedia() != null){
//                 idMultimedia = persona.getcontenidoMultimedia().getId();
//             }
//             
//             Multimedia multimedia = MultimediaServicio.actualizar(idMultimedia, archivo);
//             persona.setcontenidoMultimedia(contenidoMultimedia);
            personaRepositorio.save(persona);

        } else {
            throw new ErrorServicio("No se encontro la persona solicitada");
        }
    }

    private void validar(String nombre, String nickname, String profesion, String ciudad, String email, String clave, String clave2) throws ErrorServicio {
        if (nombre == null || nombre.isEmpty()) {
            throw new ErrorServicio("El nombre no puede ser nulo");
        }
        if (profesion == null || profesion.isEmpty()) {
            throw new ErrorServicio("La profesión no puede ser nula");
        }
        if (ciudad == null || ciudad.isEmpty()) {
            throw new ErrorServicio("La ciudad no puede ser nula");
        }
        if (email == null || email.isEmpty()) {
            throw new ErrorServicio("El E-Mail no puede ser nulo");
        }
        if (clave == null || clave.isEmpty() || clave.length() < 6) {
            throw new ErrorServicio("La clave de usuario no puede ser nula, y debe ser mayor a 6 caracteres");
        }
        if (!clave.equals(clave2)) {
            throw new ErrorServicio("Las claves deben coincidir");
        }
        Persona personaporalias = personaRepositorio.buscarNickname(nickname);
        if (personaporalias==null) {
            throw new ErrorServicio("El nickname ya esta siendo utilizado");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String string) throws UsernameNotFoundException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
