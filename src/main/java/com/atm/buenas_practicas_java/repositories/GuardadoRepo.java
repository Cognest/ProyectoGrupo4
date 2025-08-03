package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO;
import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Guardado;
import com.atm.buenas_practicas_java.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuardadoRepo extends JpaRepository<Guardado, Integer> {
    Optional<Guardado> findByUsuarioAndContenido(Usuario usuario, Contenido contenido);
    int countByContenidoId(Integer contenidoId);
    List<Guardado> findByUsuario(Usuario usuario);

    void deleteAllByUsuario(Usuario usuario);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO(
        c.id,
        c.titulo,
        c.formato,
        c.subtipo,
        c.url,
        c.urlPortada,
        uc.usuario.nickname,
        uc.usuario.avatar,
        SIZE(c.likes),
        c.fecha
    )
    FROM Guardado g
    JOIN g.contenido c
    JOIN c.usuarioContenidos uc
    WHERE g.usuario = :usuario AND uc.tipo = 'Creador'
""")
    List<ContenidoSubidoDTO> findContenidoGuardadoDTOByUsuario(@Param("usuario") Usuario usuario);
}
