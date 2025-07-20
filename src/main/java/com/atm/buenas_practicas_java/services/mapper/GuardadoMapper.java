package com.atm.buenas_practicas_java.services.mapper;

import com.atm.buenas_practicas_java.dtos.GuardadoDto;
import com.atm.buenas_practicas_java.entities.Guardado;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class GuardadoMapper extends AbstractServiceMapper<Guardado, GuardadoDto> {
    @Override
    public GuardadoDto toDto(Guardado entidad) {
        GuardadoDto dto = new GuardadoDto();
        new ModelMapper().map(entidad, dto);
        return dto;
    }

    @Override
    public Guardado toEntity(GuardadoDto dto) {
        Guardado entidad = new Guardado();
        new ModelMapper().map(dto, entidad);
        return entidad;
    }
}
