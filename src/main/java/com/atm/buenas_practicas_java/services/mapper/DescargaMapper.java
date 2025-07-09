package com.atm.buenas_practicas_java.services.mapper;

import com.atm.buenas_practicas_java.dtos.DescargaDto;
import com.atm.buenas_practicas_java.entities.Descarga;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class DescargaMapper extends AbstractServiceMapper<Descarga, DescargaDto> {

    @Override
    public DescargaDto toDto(Descarga entidad) {
        DescargaDto dto = new DescargaDto();
        new ModelMapper().map(entidad, dto);
        return dto;
    }

    @Override
    public Descarga toEntity(DescargaDto dto) {
        Descarga entidad = new Descarga();
        new ModelMapper().map(dto, entidad);
        return entidad;
    }
}

