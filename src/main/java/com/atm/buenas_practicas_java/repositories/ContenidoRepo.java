package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO;
import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContenidoRepo extends JpaRepository<Contenido, Integer> {
    @Query("""
        SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
            c.id, c.titulo, c.formato, c.url, c.urlPortada,
            u.nickname, u.avatar, COUNT(l), c.fecha
        )
        FROM Contenido c
        JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
        JOIN Usuario u ON u.id = uc.usuario.id
        LEFT JOIN Like l ON l.contenido = c
        GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
        ORDER BY COUNT(l) DESC
    """)
    List<ContenidoSubidoDTO> findAllOrderByLikes();

    @Query("""
        SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
            c.id, c.titulo, c.formato, c.url, c.urlPortada,
            u.nickname, u.avatar, COUNT(l), c.fecha
        )
        FROM Contenido c
        JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
        JOIN Usuario u ON u.id = uc.usuario.id
        LEFT JOIN Like l ON l.contenido = c
        WHERE c.id <> :contenidoId
        GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
        ORDER BY COUNT(l) DESC
    """)
    List<ContenidoSubidoDTO> findAllOrderByLikesExcludingId(@Param("contenidoId") Integer contenidoId);
}
