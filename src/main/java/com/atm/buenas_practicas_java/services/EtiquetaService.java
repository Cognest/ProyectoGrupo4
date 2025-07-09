package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.EtiquetaDto;
import com.atm.buenas_practicas_java.entities.Etiqueta;
import com.atm.buenas_practicas_java.repositories.EtiquetaRepo;
import com.atm.buenas_practicas_java.services.mapper.EtiquetaMapper;
import org.springframework.stereotype.Service;

@Service
public class EtiquetaService extends AbstractBusinessService<Etiqueta, Integer, EtiquetaDto,
        EtiquetaRepo, EtiquetaMapper> {

    public EtiquetaService(EtiquetaRepo repo, EtiquetaMapper mapper) {
        super(repo, mapper);
    }
}

