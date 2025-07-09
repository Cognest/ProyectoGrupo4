package com.atm.buenas_practicas_java.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EtiquetaDto {
    private Integer id;
    private String nombre;
}
