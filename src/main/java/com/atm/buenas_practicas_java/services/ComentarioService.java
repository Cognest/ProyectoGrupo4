package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.ComentarioDto;
import com.atm.buenas_practicas_java.dtos.ComentarioRealizadoDto;
import com.atm.buenas_practicas_java.entities.Comentario;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.ComentarioRepo;
import com.atm.buenas_practicas_java.services.mapper.ComentarioMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService extends AbstractBusinessService<Comentario, Integer, ComentarioDto,
        ComentarioRepo, ComentarioMapper> {

    @Autowired
    private ComentarioRepo comentarioRepo;

    public ComentarioService(ComentarioRepo repo, ComentarioMapper mapper) {
        super(repo, mapper);
    }

    public Page<ComentarioRealizadoDto> obtenerComentariosRealizados(Usuario usuario, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("fecha").descending());
        return comentarioRepo.findComentariosRealizadosPorUsuario(usuario, pageable);
    }
}

