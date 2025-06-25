package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.entities.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComentarioRepo extends JpaRepository<Comentario, Integer> {
}
