package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.entities.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepo extends JpaRepository<Comentario, Integer> {
    List<Comentario> findByContenidoIdOrderByFechaAsc(Integer contenidoId);
}
