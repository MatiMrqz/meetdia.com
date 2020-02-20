package com.edu.egg.meetdia.com.repositorios;

import com.edu.egg.meetdia.entidades.Post;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepositorio extends JpaRepository<Post, String>{
    @Query("SELECT p FROM Post p WHERE p.fecha = MAX(p.fecha)")
    public Post buscarUltimoPost();
    @Query("SELECT * FROM Post p ORDER BY p.fecha")
    public List<Post> mostrarUltimosPost();
}
