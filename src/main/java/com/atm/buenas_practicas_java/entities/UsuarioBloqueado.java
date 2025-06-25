package com.atm.buenas_practicas_java.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuariosBloqueados")
public class UsuarioBloqueado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_bloqueador", nullable = false)
    private Usuario bloqueador;

    @ManyToOne
    @JoinColumn(name = "id_bloqueado", nullable = false)
    private Usuario bloqueado;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
