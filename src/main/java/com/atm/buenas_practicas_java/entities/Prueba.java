package com.atm.buenas_practicas_java.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Prueba {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    private int num_visitas;
    private int num_descargas;
    private int nota;
}
