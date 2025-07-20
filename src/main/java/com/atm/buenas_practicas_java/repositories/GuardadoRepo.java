package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Guardado;
import com.atm.buenas_practicas_java.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuardadoRepo extends JpaRepository<Guardado, Integer> {
    Optional<Guardado> findByUsuarioAndContenido(Usuario usuario, Contenido contenido);
    int countByContenidoId(Integer contenidoId);
    List<Guardado> findByUsuario(Usuario usuario);
}
