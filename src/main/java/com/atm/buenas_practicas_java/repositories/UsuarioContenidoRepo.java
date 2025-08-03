package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO;
import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.entities.UsuarioContenido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioContenidoRepo extends JpaRepository<UsuarioContenido, Integer> {
    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id,
        c.titulo,
        c.formato,
        c.subtipo,
        c.url,
        c.urlPortada,
        u.nickname,
        u.avatar,
        SIZE(c.likes),
        c.fecha
    )
    FROM UsuarioContenido uc
    JOIN uc.usuario u
    JOIN uc.contenido c
    WHERE u = :usuario AND uc.tipo = 'Creador'
    ORDER BY c.fecha DESC
    """)
    List<ContenidoSubidoDTO> findContenidosSubidosPorUsuario(@Param("usuario") Usuario usuario);

    @Query("""

            SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
                 c.id,
                 c.titulo,
                 c.formato,
                 c.subtipo,
                 c.url,
                 c.urlPortada,
                 creador.nickname,
                 creador.avatar,
                 SIZE(c.likes),
                 c.fecha
             )
             FROM UsuarioContenido uc
             JOIN uc.contenido c
             JOIN UsuarioContenido ucCreador ON ucCreador.contenido = c AND ucCreador.tipo = 'Creador'
             JOIN ucCreador.usuario creador
             WHERE uc.usuario = :usuario AND uc.tipo = 'Adquirido'
             ORDER BY c.fecha DESC
    """)
    List<ContenidoSubidoDTO> findContenidosAdquiridosPorUsuario(@Param("usuario") Usuario usuario);

    List<UsuarioContenido> findAllByTipoAndUsuario(String tipo, Usuario usuario);

    UsuarioContenido findByContenidoAndUsuario(Contenido contenido, Usuario usuario);

    void deleteUsuarioContenidoByContenidoAndUsuario(Contenido contenido, Usuario usuario);

    List<UsuarioContenido> findAllByUsuarioAndContenidoIdInAndTipo(Usuario usuario, List<Integer> contenidoIds, String tipo);

    // Buscar la relación del creador de un contenido
    UsuarioContenido findByContenidoAndTipo(Contenido contenido, String tipo);

    void deleteAllByUsuario(Usuario usuario);
}