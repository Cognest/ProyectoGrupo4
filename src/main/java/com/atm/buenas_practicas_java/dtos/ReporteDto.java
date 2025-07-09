package com.atm.buenas_practicas_java.dtos;

import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReporteDto {
    private Integer id;
    private Usuario usuario;
    private Contenido contenido;
    private String mensaje;
    private LocalDateTime fecha;
}
