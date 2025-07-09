package com.atm.buenas_practicas_java.dtos;

import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioContenidoDto {
    private Integer id;
    private Usuario usuario;
    private Contenido contenido;
    private String tipo;
    private boolean guardado;
    private Integer precio;
    private LocalDate fecha_venta;
}
