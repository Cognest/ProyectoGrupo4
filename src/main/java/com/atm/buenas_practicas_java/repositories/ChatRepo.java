package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.entities.Chat;
import com.atm.buenas_practicas_java.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepo extends JpaRepository<Chat, Integer> {

    @Query("SELECT c FROM Chat c WHERE " +
            "(c.emisor = :usuario1 AND c.receptor = :usuario2) OR " +
            "(c.emisor = :usuario2 AND c.receptor = :usuario1) " +
            "ORDER BY c.fecha ASC")
    List<Chat> findConversacionEntre(@Param("usuario1") Usuario usuario1,
                                     @Param("usuario2") Usuario usuario2);

    @Query("SELECT DISTINCT CASE " +
            "WHEN c.emisor.id = :id THEN c.receptor.nickname " +
            "WHEN c.receptor.id = :id THEN c.emisor.nickname " +
            "END " +
            "FROM Chat c " +
            "WHERE c.emisor.id = :id OR c.receptor.id = :id")
    List<String> findUsuariosConConversacion(@Param("id") Integer idUsuario);

}
