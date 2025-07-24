package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepo extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmailOrNickname(String email, String nickname);
    Optional<Usuario> findByNickname(String nickname);
    Page<Usuario> findAllByIdNot(Integer idActual, Pageable pageable);
    List<Usuario> findByNicknameNot(String nickname);
    List<Usuario> findByNicknameInAndNicknameNot(List<String> nicknames, String exclude);

    boolean existsUsuarioByNickname(String nickname);
}
