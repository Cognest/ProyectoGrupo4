package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.config.CustomUserDetails;
import com.atm.buenas_practicas_java.dtos.ChatDto;
import com.atm.buenas_practicas_java.entities.Chat;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.ChatRepo;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import com.atm.buenas_practicas_java.services.ChatService;
import com.atm.buenas_practicas_java.services.mapper.ChatMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;



import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private ChatMapper chatMapper;


    @GetMapping("/historial/{nickname}")
    public List<ChatDto> obtenerHistorial(
            @PathVariable String nickname,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Usuario emisor = usuarioRepo.findByNickname(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario autenticado no encontrado"));
        Usuario receptor = usuarioRepo.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("Usuario receptor no encontrado"));

        List<Chat> chats = chatRepo.findConversacionEntre(emisor, receptor);
        return chatMapper.toDtoList(chats);
    }

}
