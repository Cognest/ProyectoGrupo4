package com.atm.buenas_practicas_java.dtos;

import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LikeDto {
    private Integer id;
    private Usuario usuario;
    private Contenido contenido;
}
