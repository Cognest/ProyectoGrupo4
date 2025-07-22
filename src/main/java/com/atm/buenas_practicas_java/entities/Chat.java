package com.atm.buenas_practicas_java.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_usuario_emisor", nullable = false)
    @JsonIgnoreProperties({"usuarioContenidos", "comentarios", "likes", "reportes", "bloqueados", "bloqueadores", "chatsEnviados", "chatsRecibidos", "movimientos"})
    private Usuario emisor;

    @ManyToOne
    @JoinColumn(name = "id_usuario_receptor", nullable = false)
    @JsonIgnoreProperties({"usuarioContenidos", "comentarios", "likes", "reportes", "bloqueados", "bloqueadores", "chatsEnviados", "chatsRecibidos", "movimientos"})
    private Usuario receptor;

    @Column(nullable = false, length = 200)
    private String mensaje;

    @Column(nullable = false)
    private LocalDateTime fecha;
}
