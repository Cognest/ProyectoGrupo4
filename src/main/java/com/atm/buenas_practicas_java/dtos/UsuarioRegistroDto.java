package com.atm.buenas_practicas_java.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UsuarioRegistroDto {
    private String nombre;
    private String apellidos;
    private String nickname;
    private String email;
    private String password;
}
