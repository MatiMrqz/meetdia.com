package com.edu.egg.meetdia.entidades;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.GenericGenerator;


@Entity
public class Persona {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id; 
    private String nombre;
    private String profesion;
    private String domicilio;
    private String codpostal;
    private String ciudad;
    private String email; 
    private String alias;
    private String clave;
    
    private String mime;
    @Lob @Basic(fetch = FetchType.LAZY)
    private byte[] contenido;

	public Persona(String id, String nombre, String profesion, String domicilio, String codpostal, String ciudad,
			String email, String alias, String clave, String mime, byte[] contenido) {
	
		this.id = id;
		this.nombre = nombre;
		this.profesion = profesion;
		this.domicilio = domicilio;
		this.codpostal = codpostal;
		this.ciudad = ciudad;
		this.email = email;
		this.alias = alias;
		this.clave = clave;
		this.mime = mime;
		this.contenido = contenido;
	}

	public Persona() {
		
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getProfesion() {
		return profesion;
	}

	public void setProfesion(String profesion) {
		this.profesion = profesion;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getCodpostal() {
		return codpostal;
	}

	public void setCodpostal(String codpostal) {
		this.codpostal = codpostal;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getMime() {
		return mime;
	}

	public void setMime(String mime) {
		this.mime = mime;
	}

	public byte[] getContenido() {
		return contenido;
	}

	public void setContenido(byte[] contenido) {
		this.contenido = contenido;
	}
    
    
}
