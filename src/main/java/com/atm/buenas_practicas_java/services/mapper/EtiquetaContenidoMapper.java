package com.atm.buenas_practicas_java.services.mapper;

import com.atm.buenas_practicas_java.dtos.EtiquetaContenidoDto;
import com.atm.buenas_practicas_java.entities.EtiquetaContenido;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class EtiquetaContenidoMapper extends AbstractServiceMapper<EtiquetaContenido, EtiquetaContenidoDto> {
    @Override
    public EtiquetaContenidoDto toDto(EtiquetaContenido entidad) {
        EtiquetaContenidoDto dto = new EtiquetaContenidoDto();
        new ModelMapper().map(entidad, dto);
        return dto;
    }

    @Override
    public EtiquetaContenido toEntity(EtiquetaContenidoDto dto) {
        EtiquetaContenido entidad = new EtiquetaContenido();
        new ModelMapper().map(dto, entidad);
        return entidad;
    }
}
