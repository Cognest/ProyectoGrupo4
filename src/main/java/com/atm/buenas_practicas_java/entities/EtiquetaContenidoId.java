package com.atm.buenas_practicas_java.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EtiquetaContenidoId implements java.io.Serializable {
    private Integer etiqueta;
    private Integer contenido;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EtiquetaContenidoId)) return false;
        EtiquetaContenidoId entity = (EtiquetaContenidoId) o;
        return Objects.equals(etiqueta, entity.etiqueta) &&
                Objects.equals(contenido, entity.contenido);
    }

    @Override
    public int hashCode() {
        return Objects.hash(etiqueta, contenido);
    }
}
