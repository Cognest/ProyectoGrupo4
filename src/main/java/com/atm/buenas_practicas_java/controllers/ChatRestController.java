package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.entities.Chat;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.ChatRepo;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatRestController {

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @GetMapping("/historial/{nickname}")
    public List<Chat> obtenerHistorial(
            @PathVariable String nickname,
            @AuthenticationPrincipal Usuario actual) {

        Usuario receptor = usuarioRepo.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return chatRepo.findByEmisorAndReceptor(actual, receptor);
    }
}
