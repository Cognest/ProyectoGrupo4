package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.dtos.ComentarioRealizadoDto;
import com.atm.buenas_practicas_java.entities.Comentario;
import com.atm.buenas_practicas_java.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComentarioRepo extends JpaRepository<Comentario, Integer> {
    List<Comentario> findByContenidoIdOrderByFechaAsc(Integer contenidoId);

    void deleteAllByUsuario(Usuario usuario);

    @Query("""
    SELECT new com.atm.buenas_practicas_java.dtos.ComentarioRealizadoDto(
        c.usuario.nickname,
        ucCreador.usuario.nickname,
        c.contenido.id,
        c.mensaje,
        c.fecha
    )
    FROM Comentario c
    JOIN UsuarioContenido ucCreador ON ucCreador.contenido = c.contenido AND ucCreador.tipo = 'Creador'
    WHERE c.usuario = :usuario
    ORDER BY c.fecha DESC
""")
    Page<ComentarioRealizadoDto> findComentariosRealizadosPorUsuario(
            @Param("usuario") Usuario usuario,
            Pageable pageable
    );
}
