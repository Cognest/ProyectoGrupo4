package com.atm.buenas_practicas_java.dtos;

import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Etiqueta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EtiquetaContenidoDto {
    private Etiqueta etiqueta;
    private Contenido contenido;
}
