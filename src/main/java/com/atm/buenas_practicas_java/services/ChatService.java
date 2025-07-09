package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.ChatDto;
import com.atm.buenas_practicas_java.entities.Chat;
import com.atm.buenas_practicas_java.repositories.ChatRepo;
import com.atm.buenas_practicas_java.services.mapper.ChatMapper;
import org.springframework.stereotype.Service;

@Service
public class ChatService extends AbstractBusinessService<Chat, Integer, ChatDto,
        ChatRepo, ChatMapper> {

    public ChatService(ChatRepo repo, ChatMapper mapper) {
        super(repo, mapper);
    }
}

