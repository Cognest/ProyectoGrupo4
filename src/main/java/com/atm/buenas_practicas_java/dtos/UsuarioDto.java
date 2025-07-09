package com.atm.buenas_practicas_java.dtos;

import com.atm.buenas_practicas_java.entities.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioDto {
    private Integer id;
    private String nombre;
    private String apellidos;
    private String nickname;
    private String email;
    private String password;
    private Integer token;
    private List<UsuarioContenido> usuarioContenidos;
    private List<Comentario> comentarios;
    private List<Like> likes;
    private List<Reporte> reportes;
    private List<UsuarioBloqueado> bloqueados;
    private List<UsuarioBloqueado> bloqueadores;
    private List<Chat> chatsEnviados;
    private List<Chat> chatsRecibidos;
    private List<Cartera> movimientos;
}
