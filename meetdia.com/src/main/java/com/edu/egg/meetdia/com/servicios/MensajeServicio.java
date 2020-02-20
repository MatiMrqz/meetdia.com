package com.edu.egg.meetdia.com.servicios;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.edu.egg.meetdia.com.repositorios.MensajeRepositorio;
import com.edu.egg.meetdia.entidades.Mensaje;
import com.edu.egg.meetdia.entidades.Persona;
import com.edu.egg.meetdia.errores.ErrorServicio;

@Service
public class MensajeServicio {
	@Autowired
	private MensajeRepositorio mensajeRepositorio;

	@Transactional
	public void crearMensaje(String id, String contenido, Persona emisor, Persona receptor) throws ErrorServicio {

		if (contenido != null) {
			throw new ErrorServicio("No se puede crear un mensaje vacio");

		}

		Mensaje mensaje = new Mensaje();

		mensaje.setContenido(contenido);
		mensaje.setFecha(new Date());
		mensaje.setEmisor(emisor);
		mensaje.setReceptor(receptor);

		mensajeRepositorio.save(mensaje);

	}
	

	public List<Mensaje> buscarMensajes(String contenido, Persona emisor, Persona receptor, Date fecha)
			throws ErrorServicio {

		List<Mensaje> Chat = mensajeRepositorio.buscarMensajeEmisorReceptor(emisor, receptor);
		if (Chat != null) {
			throw new ErrorServicio("No se puede crear un mensaje vacio");

		} else {

			return Chat;
		}

	}
}