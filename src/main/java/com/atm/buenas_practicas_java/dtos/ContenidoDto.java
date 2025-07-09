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
public class ContenidoDto {
    private Integer id;
    private String descripcion;
    private String formato;
    private Integer precio;
    private String ubicacion;
    private String url;
    private List<UsuarioContenido> usuarioContenidos;
    private List<Comentario> comentarios;
    private List<Like> likes;
    private List<Reporte> reportes;
    private List<EtiquetaContenido> etiquetas;
}
