package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.entities.Like;
import com.atm.buenas_practicas_java.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepo extends JpaRepository<Like, Integer> {
    Integer countByContenidoId(Integer contenidoId);
    Optional<Like> findByContenidoIdAndUsuario(Integer contenidoId, Usuario usuario);
    List<Like> findByUsuario(Usuario usuario);

    void deleteAllByUsuario(Usuario usuario);
}
