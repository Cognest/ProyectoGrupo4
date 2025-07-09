package com.atm.buenas_practicas_java.services.mapper;

import com.atm.buenas_practicas_java.dtos.UsuarioBloqueadoDto;
import com.atm.buenas_practicas_java.entities.UsuarioBloqueado;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UsuarioBloqueadoMapper extends AbstractServiceMapper<UsuarioBloqueado, UsuarioBloqueadoDto> {

    @Override
    public UsuarioBloqueadoDto toDto(UsuarioBloqueado entidad) {
        UsuarioBloqueadoDto dto = new UsuarioBloqueadoDto();
        new ModelMapper().map(entidad, dto);
        return dto;
    }

    @Override
    public UsuarioBloqueado toEntity(UsuarioBloqueadoDto dto) {
        UsuarioBloqueado entidad = new UsuarioBloqueado();
        new ModelMapper().map(dto, entidad);
        return entidad;
    }
}

