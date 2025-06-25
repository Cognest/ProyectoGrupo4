package com.atm.buenas_practicas_java.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "etiquetaContenido")
@IdClass(EtiquetaContenidoId.class)
public class EtiquetaContenido {
    @Id
    @ManyToOne
    @JoinColumn(name = "id_etiqueta", nullable = false)
    private Etiqueta etiqueta;

    @Id
    @ManyToOne
    @JoinColumn(name = "id_contenido", nullable = false)
    private Contenido contenido;
}
