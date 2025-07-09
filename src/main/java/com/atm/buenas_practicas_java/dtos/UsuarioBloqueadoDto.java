package com.atm.buenas_practicas_java.dtos;

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
public class UsuarioBloqueadoDto {
    private Integer id;
    private Usuario bloqueador;
    private Usuario bloqueado;
    private LocalDateTime fecha;
}
