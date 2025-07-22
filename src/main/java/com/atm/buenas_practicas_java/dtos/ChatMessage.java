package com.atm.buenas_practicas_java.dtos;
import java.time.LocalDateTime;


import lombok.Data;

@Data
public class ChatMessage {
    private String emisor;      // nickname del que envía
    private String receptor;    // nickname del que recibe
    private String contenido;   // texto del mensaje
    private LocalDateTime fecha; //fecha del mensaje
}

