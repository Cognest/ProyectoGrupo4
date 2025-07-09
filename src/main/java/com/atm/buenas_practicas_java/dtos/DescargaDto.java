package com.atm.buenas_practicas_java.dtos;

import com.atm.buenas_practicas_java.entities.UsuarioContenido;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DescargaDto {
    private Integer id;
    private UsuarioContenido usuarioContenido;
    private LocalDate fecha;
}
