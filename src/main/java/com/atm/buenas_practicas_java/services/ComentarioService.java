package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.ComentarioDto;
import com.atm.buenas_practicas_java.entities.Comentario;
import com.atm.buenas_practicas_java.repositories.ComentarioRepo;
import com.atm.buenas_practicas_java.services.mapper.ComentarioMapper;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService extends AbstractBusinessService<Comentario, Integer, ComentarioDto,
        ComentarioRepo, ComentarioMapper> {

    public ComentarioService(ComentarioRepo repo, ComentarioMapper mapper) {
        super(repo, mapper);
    }
}

