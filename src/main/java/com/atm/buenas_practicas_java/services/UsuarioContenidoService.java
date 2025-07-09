package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.UsuarioContenidoDto;
import com.atm.buenas_practicas_java.entities.UsuarioContenido;
import com.atm.buenas_practicas_java.repositories.UsuarioContenidoRepo;
import com.atm.buenas_practicas_java.services.mapper.UsuarioContenidoMapper;
import org.springframework.stereotype.Service;

@Service
public class UsuarioContenidoService extends AbstractBusinessService<UsuarioContenido, Integer, UsuarioContenidoDto,
        UsuarioContenidoRepo, UsuarioContenidoMapper> {

    public UsuarioContenidoService(UsuarioContenidoRepo repo, UsuarioContenidoMapper mapper) {
        super(repo, mapper);
    }
}

