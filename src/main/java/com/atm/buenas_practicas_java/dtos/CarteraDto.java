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
public class CarteraDto {
    private Integer id;
    private Usuario usuario;
    private Integer cantidad;
    private String operacion;
    private LocalDateTime fecha;
}
