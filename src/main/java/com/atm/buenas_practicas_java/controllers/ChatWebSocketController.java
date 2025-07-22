package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.dtos.ChatMessage;
import com.atm.buenas_practicas_java.entities.Chat;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.ChatRepo;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.security.Principal;

import java.time.LocalDateTime;



@Controller
public class ChatWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private ChatRepo chatRepo;

    @MessageMapping("/chatsendMessage")
    public void enviarMensajePrivado(ChatMessage mensaje, Principal principal) {
        // Obtener el emisor a partir del Principal (que devuelve el nickname)
        String nicknameEmisor = principal.getName();
        Usuario emisor = usuarioRepo.findByNickname(nicknameEmisor).orElseThrow();
        Usuario receptor = usuarioRepo.findByNickname(mensaje.getReceptor()).orElseThrow();

        // Guardar mensaje en la base de datos
        Chat chat = new Chat();
        chat.setEmisor(emisor);
        chat.setReceptor(receptor);
        chat.setMensaje(mensaje.getContenido());
        chat.setFecha(LocalDateTime.now());
        chatRepo.save(chat);

        // Crear mensaje de respuesta para enviar por WebSocket
        ChatMessage respuesta = new ChatMessage();
        respuesta.setEmisor(emisor.getNickname());
        respuesta.setReceptor(receptor.getNickname());
        respuesta.setContenido(chat.getMensaje());
        respuesta.setFecha(chat.getFecha());

        System.out.println("➡️ Enviando mensaje a: " + receptor.getNickname());

        // Enviar al receptor
        messagingTemplate.convertAndSendToUser(
                receptor.getNickname(),
                "/messages",
                respuesta
        );
        System.out.println("📤 Mensaje enviado por WebSocket a /user/" + receptor.getNickname() + "/queue/messages");

    }
}


