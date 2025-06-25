package com.atm.buenas_practicas_java.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "contenido")
public class Contenido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 80)
    private String titulo;

    @Column(nullable = false, length = 150)
    private String descripcion;

    @Column(nullable = false, length = 20)
    private String formato;

    @Column(nullable = false)
    private Integer precio;

    @Column(length = 150)
    private String ubicacion;

    @Column(nullable = false, length = 200)
    private String url;

    // Relaciones bidireccionales
    @OneToMany(mappedBy = "contenido")
    private List<UsuarioContenido> usuarioContenidos;

    @OneToMany(mappedBy = "contenido")
    private List<Comentario> comentarios;

    @OneToMany(mappedBy = "contenido")
    private List<Like> likes;

    @OneToMany(mappedBy = "contenido")
    private List<Reporte> reportes;

    @OneToMany(mappedBy = "contenido")
    private List<EtiquetaContenido> etiquetas;
}
