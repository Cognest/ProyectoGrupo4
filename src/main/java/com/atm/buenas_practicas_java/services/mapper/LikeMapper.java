package com.atm.buenas_practicas_java.services.mapper;

import com.atm.buenas_practicas_java.dtos.LikeDto;
import com.atm.buenas_practicas_java.entities.Like;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class LikeMapper extends AbstractServiceMapper<Like, LikeDto> {

    @Override
    public LikeDto toDto(Like entidad) {
        LikeDto dto = new LikeDto();
        new ModelMapper().map(entidad, dto);
        return dto;
    }

    @Override
    public Like toEntity(LikeDto dto) {
        Like entidad = new Like();
        new ModelMapper().map(dto, entidad);
        return entidad;
    }
}

