package com.atm.buenas_practicas_java.services.mapper;

import com.atm.buenas_practicas_java.dtos.ChatDto;
import com.atm.buenas_practicas_java.entities.Chat;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatMapper extends AbstractServiceMapper<Chat, ChatDto> {

    @Override
    public ChatDto toDto(Chat entidad){
        ChatDto dto = new ChatDto();
        dto.setId(entidad.getId());
        dto.setMensaje(entidad.getMensaje());
        dto.setFecha(entidad.getFecha());
        dto.setEmisor(entidad.getEmisor().getNickname());
        dto.setReceptor(entidad.getReceptor().getNickname());
        return dto;
    }

    @Override
    public Chat toEntity(ChatDto dto){
        final Chat entidad = new Chat();
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(dto, entidad);
        return entidad;
    }

    public List<ChatDto> toDtoList(List<Chat> chats) {
        return chats.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ChatMapper() {}
}
