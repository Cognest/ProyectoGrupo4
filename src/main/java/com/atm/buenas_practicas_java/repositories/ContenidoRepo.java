package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO;
import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Guardado;
import com.atm.buenas_practicas_java.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContenidoRepo extends JpaRepository<Contenido, Integer> {
    @Query("""
        SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
            c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
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
            c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
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

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY COUNT(l) DESC
""")
    List<ContenidoSubidoDTO> buscarPorTermino(@Param("termino") String termino);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY c.fecha DESC
""")
    List<ContenidoSubidoDTO> buscarPorTerminoFecha(@Param("termino") String termino);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha, c.visitas
    ORDER BY c.visitas DESC
""")
    List<ContenidoSubidoDTO> buscarPorTerminoVisitas(@Param("termino") String termino);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.fecha >= :fechaInicio
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY COUNT(l) DESC
""")
    List<ContenidoSubidoDTO> buscarPorTerminoOrdenadoPorLikes(@Param("termino") String termino,
                                                              @Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.fecha >= :fechaInicio
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY c.fecha DESC
""")
    List<ContenidoSubidoDTO> buscarPorTerminoOrdenadoPorFecha(@Param("termino") String termino,
                                                              @Param("fechaInicio") LocalDateTime fechaInicio);


    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.fecha >= :fechaInicio
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha, c.visitas
    ORDER BY c.visitas DESC
""")
    List<ContenidoSubidoDTO> buscarPorTerminoOrdenadoPorVistas(@Param("termino") String termino,
                                                               @Param("fechaInicio") LocalDateTime fechaInicio);

    /*-------------------------------------------------
    * BUSQUEDAS POR TIPOS DE CONTENIDO
    *------------------------------------------------- */

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE c.formato = :tipo
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY COUNT(l) DESC
""")
    List<ContenidoSubidoDTO> buscarPorTipo(@Param("tipo") String tipo);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE c.formato = :tipo
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY c.fecha DESC
""")
    List<ContenidoSubidoDTO> buscarPorTipoFecha(@Param("tipo") String tipo);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE c.formato = :tipo
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha, c.visitas
    ORDER BY c.visitas DESC
""")
    List<ContenidoSubidoDTO> buscarPorTipoVisitas(@Param("tipo") String tipo);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE c.formato = :tipo
    AND c.fecha >= :fechaInicio
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY COUNT(l) DESC
""")
    List<ContenidoSubidoDTO> buscarPorTipoOrdenadoPorLikes(@Param("tipo") String tipo,
                                                              @Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE c.formato = :tipo
    AND c.fecha >= :fechaInicio
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY c.fecha DESC
""")
    List<ContenidoSubidoDTO> buscarPorTipoOrdenadoPorFecha(@Param("tipo") String tipo,
                                                              @Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE c.formato = :tipo
    AND c.fecha >= :fechaInicio
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha, c.visitas
    ORDER BY c.visitas DESC
""")
    List<ContenidoSubidoDTO> buscarPorTipoOrdenadoPorVistas(@Param("tipo") String tipo,
                                                               @Param("fechaInicio") LocalDateTime fechaInicio);

    /*-------------------------------------------------
     * BUSQUEDAS POR TIPOS Y TERMINOS DE CONTENIDO
     *------------------------------------------------- */

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.formato = :tipo
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY COUNT(l) DESC
""")
    List<ContenidoSubidoDTO> buscarPorTipoTermino(@Param("termino") String termino,
                                           @Param("tipo") String tipo);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.formato = :tipo
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY c.fecha DESC
""")
    List<ContenidoSubidoDTO> buscarPorTipoTerminoFecha(@Param("termino") String termino,
                                                @Param("tipo") String tipo);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.formato = :tipo
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha, c.visitas
    ORDER BY c.visitas DESC
""")
    List<ContenidoSubidoDTO> buscarPorTipoTerminoVisitas(@Param("termino") String termino,
                                                  @Param("tipo") String tipo);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.formato = :tipo
    AND c.fecha >= :fechaInicio
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY COUNT(l) DESC
""")
    List<ContenidoSubidoDTO> buscarPorTipoTerminoOrdenadoPorLikes(@Param("termino") String termino,
                                                           @Param("tipo") String tipo,
                                                           @Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.formato = :tipo
    AND c.fecha >= :fechaInicio
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY c.fecha DESC
""")
    List<ContenidoSubidoDTO> buscarPorTipoTerminoOrdenadoPorFecha(@Param("termino") String termino,
                                                           @Param("tipo") String tipo,
                                                           @Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.formato = :tipo
    AND c.fecha >= :fechaInicio
    GROUP BY c.id, c.titulo, c.formato, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha, c.visitas
    ORDER BY c.visitas DESC
""")
    List<ContenidoSubidoDTO> buscarPorTipoTerminoOrdenadoPorVistas(@Param("termino") String termino,
                                                            @Param("tipo") String tipo,
                                                            @Param("fechaInicio") LocalDateTime fechaInicio);

    /*-------------------------------------------------
     * BUSQUEDAS POR SUBTIPOS DE CONTENIDO
     *------------------------------------------------- */

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE c.formato = :tipo
    AND c.subtipo = :subtipo
    GROUP BY c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY COUNT(l) DESC
""")
    List<ContenidoSubidoDTO> buscarPorSubtipo(@Param("tipo") String tipo,
                                           @Param("subtipo") String subtipo);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE c.formato = :tipo
    AND c.subtipo = :subtipo
    GROUP BY c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY c.fecha DESC
""")
    List<ContenidoSubidoDTO> buscarPorSubtipoFecha(@Param("tipo") String tipo,
                                                @Param("subtipo") String subtipo);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE c.formato = :tipo
    AND c.subtipo = :subtipo
    GROUP BY c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha, c.visitas
    ORDER BY c.visitas DESC
""")
    List<ContenidoSubidoDTO> buscarPorSubtipoVisitas(@Param("tipo") String tipo,
                                                  @Param("subtipo") String subtipo);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE c.formato = :tipo
    AND c.fecha >= :fechaInicio
    AND c.subtipo = :subtipo
    GROUP BY c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY COUNT(l) DESC
""")
    List<ContenidoSubidoDTO> buscarPorSubtipoOrdenadoPorLikes(@Param("tipo") String tipo,
                                                           @Param("subtipo") String subtipo,
                                                           @Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE c.formato = :tipo
    AND c.fecha >= :fechaInicio
    AND c.subtipo = :subtipo
    GROUP BY c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY c.fecha DESC
""")
    List<ContenidoSubidoDTO> buscarPorSubtipoOrdenadoPorFecha(@Param("tipo") String tipo,
                                                           @Param("subtipo") String subtipo,
                                                           @Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE c.formato = :tipo
    AND c.fecha >= :fechaInicio
    AND c.subtipo = :subtipo
    GROUP BY c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha, c.visitas
    ORDER BY c.visitas DESC
""")
    List<ContenidoSubidoDTO> buscarPorSubtipoOrdenadoPorVistas(@Param("tipo") String tipo,
                                                            @Param("subtipo") String subtipo,
                                                            @Param("fechaInicio") LocalDateTime fechaInicio);

    /*-------------------------------------------------
     * BUSQUEDAS POR SUBTIPOS Y TERMINOS DE CONTENIDO
     *------------------------------------------------- */

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.formato = :tipo
    AND c.subtipo = :subtipo
    GROUP BY c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY COUNT(l) DESC
""")
    List<ContenidoSubidoDTO> buscarPorSubtipoTermino(@Param("termino") String termino,
                                                  @Param("tipo") String tipo,
                                                  @Param("subtipo") String subtipo);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.formato = :tipo
    AND c.subtipo = :subtipo
    GROUP BY c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY c.fecha DESC
""")
    List<ContenidoSubidoDTO> buscarPorSubtipoTerminoFecha(@Param("termino") String termino,
                                                       @Param("tipo") String tipo,
                                                       @Param("subtipo") String subtipo);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.formato = :tipo
    AND c.subtipo = :subtipo
    GROUP BY c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha, c.visitas
    ORDER BY c.visitas DESC
""")
    List<ContenidoSubidoDTO> buscarPorSubtipoTerminoVisitas(@Param("termino") String termino,
                                                         @Param("tipo") String tipo,
                                                         @Param("subtipo") String subtipo);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.formato = :tipo
    AND c.fecha >= :fechaInicio
    AND c.subtipo = :subtipo
    GROUP BY c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY COUNT(l) DESC
""")
    List<ContenidoSubidoDTO> buscarPorSubtipoTerminoOrdenadoPorLikes(@Param("termino") String termino,
                                                                  @Param("tipo") String tipo,
                                                                  @Param("subtipo") String subtipo,
                                                                  @Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.formato = :tipo
    AND c.fecha >= :fechaInicio
    AND c.subtipo = :subtipo
    GROUP BY c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha
    ORDER BY c.fecha DESC
""")
    List<ContenidoSubidoDTO> buscarPorSubtipoTerminoOrdenadoPorFecha(@Param("termino") String termino,
                                                                  @Param("tipo") String tipo,
                                                                  @Param("subtipo") String subtipo,
                                                                  @Param("fechaInicio") LocalDateTime fechaInicio);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada,
        u.nickname, u.avatar, COUNT(l), c.fecha
    )
    FROM Contenido c
    JOIN UsuarioContenido uc ON uc.contenido = c AND uc.tipo = 'Creador'
    JOIN Usuario u ON u.id = uc.usuario.id
    LEFT JOIN Like l ON l.contenido = c
    LEFT JOIN EtiquetaContenido ec ON ec.contenido = c
    LEFT JOIN Etiqueta e ON e.id = ec.etiqueta.id
    WHERE (
       LOWER(c.titulo) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(c.descripcion) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nickname) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :termino, '%'))
       OR LOWER(u.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))
    )
    AND c.formato = :tipo
    AND c.fecha >= :fechaInicio
    AND c.subtipo = :subtipo
    GROUP BY c.id, c.titulo, c.formato, c.subtipo, c.url, c.urlPortada, u.nickname, u.avatar, c.fecha, c.visitas
    ORDER BY c.visitas DESC
""")
    List<ContenidoSubidoDTO> buscarPorSubtipoTerminoOrdenadoPorVistas(@Param("termino") String termino,
                                                                   @Param("tipo") String tipo,
                                                                   @Param("subtipo") String subtipo,
                                                                   @Param("fechaInicio") LocalDateTime fechaInicio);
}
