package com.edu.egg.meetdia.entidades;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

public class Mensaje {
	// ESTA CLASE ES PARA ENVIAR CONTENIDO EN FORMA DE MENSAJE Y QUE EL USUARIO
	// PUEDA RECIBIRLO O ENVIARLO

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;

	private String contenido;

	private Persona emisor;

	private Persona receptor;

	public Persona getEmisor() {
		return emisor;
	}

	public void setEmisor(Persona emisor) {
		this.emisor = emisor;
	}

	public Persona getReceptor() {
		return receptor;
	}

	public void setReceptor(Persona receptor) {
		this.receptor = receptor;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getContenido() {
		return contenido;
	}

	public void setContenido(String contenido) {
		this.contenido = contenido;
	}

}
