package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.entities.UsuarioBloqueado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioBloqueadoRepo extends JpaRepository<UsuarioBloqueado, Integer> {
    boolean existsByBloqueadoAndBloqueador(Usuario bloqueado, Usuario bloqueador);

    UsuarioBloqueado findByBloqueadoAndBloqueador(Usuario bloqueado, Usuario bloqueador);

    List<UsuarioBloqueado> findAllByBloqueador(Usuario usuario);
}
