package com.edu.egg.meetdia.com.repositorios;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.edu.egg.meetdia.entidades.Persona;





@Repository
public interface PersonaRepositorio extends JpaRepository<Persona,String>{
	
	 @Query("SELECT a FROM Persona a WHERE a.id= :id")
	 public Persona buscarPersona(@Param("id")String id);
 	 
	 @Query("SELECT a FROM Persona a WHERE a.nombre LIKE :nombre")
	 public Persona buscarPersonaporNombre(@Param("nombre")String nombre);
	 
	 @Query("SELECT a FROM Persona a ORDER BY a.nombre ASC")
	 public  List<Persona> buscarTodos();
	 
}