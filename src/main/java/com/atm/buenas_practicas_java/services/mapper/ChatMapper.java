package com.atm.buenas_practicas_java.services.mapper;

import com.atm.buenas_practicas_java.dtos.ChatDto;
import com.atm.buenas_practicas_java.entities.Chat;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class ChatMapper extends AbstractServiceMapper<Chat, ChatDto>{
    //Convertir de entidad a dto
    @Override
    public ChatDto toDto(Chat entidad){
        final ChatDto dto = new ChatDto();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(entidad,dto);
        return dto;
    }
    //Convertir de dto a entidad
    @Override
    public Chat toEntity(ChatDto dto){
        final Chat entidad = new Chat();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(dto,entidad);

        return entidad;
    }

    public ChatMapper() {
    }
}
