package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.DescargaDto;
import com.atm.buenas_practicas_java.entities.Descarga;
import com.atm.buenas_practicas_java.repositories.DescargaRepo;
import com.atm.buenas_practicas_java.services.mapper.DescargaMapper;
import org.springframework.stereotype.Service;

@Service
public class DescargaService extends AbstractBusinessService<Descarga, Integer, DescargaDto,
        DescargaRepo, DescargaMapper> {

    public DescargaService(DescargaRepo repo, DescargaMapper mapper) {
        super(repo, mapper);
    }
}

