package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.ReporteDto;
import com.atm.buenas_practicas_java.entities.Reporte;
import com.atm.buenas_practicas_java.repositories.ReporteRepo;
import com.atm.buenas_practicas_java.services.mapper.ReporteMapper;
import org.springframework.stereotype.Service;

@Service
public class ReporteService extends AbstractBusinessService<Reporte, Integer, ReporteDto,
        ReporteRepo, ReporteMapper> {

    public ReporteService(ReporteRepo repo, ReporteMapper mapper) {
        super(repo, mapper);
    }
}

