package com.edu.egg.meetdia.com.repositorios;

import java.sql.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.edu.egg.meetdia.entidades.Mensaje;


@Repository
public interface MensajeRepositorio extends JpaRepository<Mensaje, String>{
	
	@Query("SELECT a FROM Mensaje a WHERE a.id= :id")
	 public Mensaje buscarMensaje(@Param("id")String id);
	
	@Query("SELECT a FROM Mensaje a WHERE a.fecha= :fecha")
	 public Mensaje buscarFecha(@Param("fecha")Date fecha);
	
	@Query("SELECT a FROM Mensaje a WHERE a.contenido= :contenido")
	 public Mensaje buscarContenido(@Param("contenido")Byte [] contenido);
	
}
