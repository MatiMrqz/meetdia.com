package com.edu.egg.meetdia.com;

import com.edu.egg.meetdia.com.entidades.Persona;
import com.edu.egg.meetdia.com.servicios.PersonaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SeguridadConfiguracion extends WebSecurityConfigurerAdapter{
        @Autowired
        private PersonaServicio personaServicio;
    
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
            auth.userDetailsService(personaServicio).passwordEncoder(new BCryptPasswordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
            http.httpBasic().disable().formLogin().disable();
	}

}
