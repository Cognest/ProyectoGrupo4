package com.atm.buenas_practicas_java.services.mapper;

import com.atm.buenas_practicas_java.dtos.CarteraDto;
import com.atm.buenas_practicas_java.entities.Cartera;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CarteraMapper extends AbstractServiceMapper<Cartera, CarteraDto>{
    //Convertir de entidad a dto
    @Override
    public CarteraDto toDto(Cartera entidad){
        final CarteraDto dto = new CarteraDto();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(entidad,dto);
        return dto;
    }
    //Convertir de dto a entidad
    @Override
    public Cartera toEntity(CarteraDto dto){
        final Cartera entidad = new Cartera();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(dto,entidad);

        return entidad;
    }

    public CarteraMapper() {
    }
}
