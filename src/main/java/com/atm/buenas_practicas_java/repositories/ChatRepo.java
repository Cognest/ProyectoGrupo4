package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.atm.buenas_practicas_java.entities.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


@Repository
public interface ChatRepo extends JpaRepository<Chat, Integer> {
    @Query("SELECT c FROM Chat c WHERE " +
            "(c.emisor = :u1 AND c.receptor = :u2) OR " +
            "(c.emisor = :u2 AND c.receptor = :u1) " +
            "ORDER BY c.fecha ASC")
    List<Chat> findByEmisorAndReceptor(@Param("u1") Usuario u1, @Param("u2") Usuario u2);

}
