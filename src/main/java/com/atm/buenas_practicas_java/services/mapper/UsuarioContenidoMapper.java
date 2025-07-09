package com.atm.buenas_practicas_java.services.mapper;

import com.atm.buenas_practicas_java.dtos.UsuarioContenidoDto;
import com.atm.buenas_practicas_java.entities.UsuarioContenido;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class UsuarioContenidoMapper extends AbstractServiceMapper<UsuarioContenido, UsuarioContenidoDto> {

    @Override
    public UsuarioContenidoDto toDto(UsuarioContenido entidad) {
        UsuarioContenidoDto dto = new UsuarioContenidoDto();
        new ModelMapper().map(entidad, dto);
        return dto;
    }

    @Override
    public UsuarioContenido toEntity(UsuarioContenidoDto dto) {
        UsuarioContenido entidad = new UsuarioContenido();
        new ModelMapper().map(dto, entidad);
        return entidad;
    }
}

