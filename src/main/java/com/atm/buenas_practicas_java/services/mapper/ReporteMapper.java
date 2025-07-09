package com.atm.buenas_practicas_java.services.mapper;

import com.atm.buenas_practicas_java.dtos.ReporteDto;
import com.atm.buenas_practicas_java.entities.Reporte;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ReporteMapper extends AbstractServiceMapper<Reporte, ReporteDto> {

    @Override
    public ReporteDto toDto(Reporte entidad) {
        ReporteDto dto = new ReporteDto();
        new ModelMapper().map(entidad, dto);
        return dto;
    }

    @Override
    public Reporte toEntity(ReporteDto dto) {
        Reporte entidad = new Reporte();
        new ModelMapper().map(dto, entidad);
        return entidad;
    }
}

