package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.entities.EtiquetaContenido;
import com.atm.buenas_practicas_java.entities.EtiquetaContenidoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EtiquetaContenidoRepo extends JpaRepository<EtiquetaContenido, EtiquetaContenidoId> {
}
