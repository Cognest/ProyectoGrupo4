package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.entities.EtiquetaContenido;
import com.atm.buenas_practicas_java.entities.EtiquetaContenidoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EtiquetaContenidoRepo extends JpaRepository<EtiquetaContenido, EtiquetaContenidoId> {
    boolean existsById(EtiquetaContenidoId id);

    List<EtiquetaContenido> findByContenidoId(Integer contenidoId);
}
