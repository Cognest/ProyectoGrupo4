package com.atm.buenas_practicas_java.repositories;

import com.atm.buenas_practicas_java.entities.Cartera;
import com.atm.buenas_practicas_java.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarteraRepo extends JpaRepository<Cartera, Integer> {

    List<Cartera> findAllByUsuarioOrderByFechaDesc(Usuario usuario);

    void deleteAllByUsuario(Usuario usuario);
}
