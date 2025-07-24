package com.atm.buenas_practicas_java.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContenidoSubidoDTO {
    private Integer id;
    private String titulo;
    private String formato;
    private String subtipo;
    private String url;
    private String urlPortada;
    private String nicknameCreador;
    private String avatarCreador; // Si tienes avatar
    private long totalLikes;
    private LocalDateTime fecha;

    public String getClaseFiltro() {
        if (formato == null) return "";
        switch (formato) {
            case "imagenes": return "select-img";
            case "videos": return "select-vid";
            case "audios": return "select-aud";
            case "modelos3d": return "select-tres-d";
            default: return "";
        }
    }
}
