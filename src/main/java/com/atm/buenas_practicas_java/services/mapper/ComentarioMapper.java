package com.atm.buenas_practicas_java.services.mapper;

import com.atm.buenas_practicas_java.dtos.CarteraDto;
import com.atm.buenas_practicas_java.dtos.ComentarioDto;
import com.atm.buenas_practicas_java.entities.Cartera;
import com.atm.buenas_practicas_java.entities.Comentario;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ComentarioMapper extends AbstractServiceMapper<Comentario, ComentarioDto>{
    //Convertir de entidad a dto
    @Override
    public ComentarioDto toDto(Comentario entidad){
        final ComentarioDto dto = new ComentarioDto();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(entidad,dto);
        return dto;
    }
    //Convertir de dto a entidad
    @Override
    public Comentario toEntity(ComentarioDto dto){
        final Comentario entidad = new Comentario();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(dto,entidad);

        return entidad;
    }

    public ComentarioMapper() {
    }
}
