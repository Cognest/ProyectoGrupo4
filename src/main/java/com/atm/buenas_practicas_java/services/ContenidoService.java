package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.ContenidoDto;
import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.repositories.ContenidoRepo;
import com.atm.buenas_practicas_java.services.mapper.ContenidoMapper;
import org.springframework.stereotype.Service;

@Service
public class ContenidoService extends AbstractBusinessService<Contenido, Integer, ContenidoDto,
        ContenidoRepo, ContenidoMapper> {

    public ContenidoService(ContenidoRepo repo, ContenidoMapper mapper) {
        super(repo, mapper);
    }
}

