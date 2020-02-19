package com.edu.egg.meetdia.com.repositorios;

import com.edu.egg.meetdia.entidades.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepositorio extends JpaRepository<Post, String>{
}
