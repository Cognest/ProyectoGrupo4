package com.atm.buenas_practicas_java.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ComentarioRealizadoDto {
    private String nicknameUsuario;       // quien hizo el comentario
    private String nicknameCreador;       // creador del contenido
    private Integer idContenido;
    private String mensaje;
    private LocalDateTime fecha;
}
