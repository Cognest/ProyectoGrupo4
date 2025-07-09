package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.UsuarioBloqueadoDto;
import com.atm.buenas_practicas_java.entities.UsuarioBloqueado;
import com.atm.buenas_practicas_java.repositories.UsuarioBloqueadoRepo;
import com.atm.buenas_practicas_java.services.mapper.UsuarioBloqueadoMapper;
import org.springframework.stereotype.Service;

@Service
public class UsuarioBloqueadoService extends AbstractBusinessService<UsuarioBloqueado, Integer, UsuarioBloqueadoDto,
        UsuarioBloqueadoRepo, UsuarioBloqueadoMapper> {

    public UsuarioBloqueadoService(UsuarioBloqueadoRepo repo, UsuarioBloqueadoMapper mapper) {
        super(repo, mapper);
    }
}

