package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.entities.Etiqueta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EtiquetaRepo extends JpaRepository<Etiqueta, Integer> {
    Optional<Etiqueta> findByNombreIgnoreCase(String nombre);
}
