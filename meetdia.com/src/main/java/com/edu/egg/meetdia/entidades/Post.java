package com.edu.egg.meetdia.entidades;

import com.edu.egg.meetdia.com.enumeraciones.Categoria;
import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class Post {
    @Id
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid", strategy="uuid2")
    private String id;
    private String titulo;
    private String descripcion;
    
    @Temporal(TemporalType.DATE)
    private Date fecha_publicacion;
    
    @OneToOne
    private Multimedia multimedia;
    
    @OneToMany
    private TreeSet<Mensaje> mensajes;
    
    @ManyToOne
    private Persona persona;
    
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    public Multimedia getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(Multimedia multimedia) {
        this.multimedia = multimedia;
    }

    public TreeSet<Mensaje> getMensajes() {
        return mensajes;
    }

    public void setMensajes(TreeSet<Mensaje> mensajes) {
        this.mensajes = mensajes;
    }

    public Post() {
        mensajes = new TreeSet<>((Mensaje msj1, Mensaje msj2) -> {
            if (msj1.getFecha().after(msj2.getFecha())) {
                return 1;
            }
            else if (msj1.getFecha().before(msj2.getFecha())) {
                return -1;
            }
            else{
                return 0;
            }
        });
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha_publicacion() {
        return fecha_publicacion;
    }

    public void setFecha_publicacion(Date fecha_publicacion) {
        this.fecha_publicacion = fecha_publicacion;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getId() {
        return id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    } 
}
