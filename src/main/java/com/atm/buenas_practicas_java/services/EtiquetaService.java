package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.EtiquetaDto;
import com.atm.buenas_practicas_java.entities.Etiqueta;
import com.atm.buenas_practicas_java.repositories.EtiquetaRepo;
import com.atm.buenas_practicas_java.services.mapper.EtiquetaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EtiquetaService extends AbstractBusinessService<Etiqueta, Integer, EtiquetaDto,
        EtiquetaRepo, EtiquetaMapper> {

    @Autowired
    private EtiquetaRepo etiquetaRepo;

    public EtiquetaService(EtiquetaRepo repo, EtiquetaMapper mapper) {
        super(repo, mapper);
    }

    public List<Etiqueta> procesarEtiquetas(String etiquetasTexto) {
        String[] nombres = etiquetasTexto.split(",");
        Set<String> nombresUnicos = new HashSet<>();

        List<Etiqueta> resultado = new ArrayList<>();

        for (String nombre : nombres) {
            String limpio = nombre.trim().toLowerCase();

            // Solo letras, números, guiones y guiones bajos
            if (!limpio.matches("^[a-z0-9_-]{1,30}$")) {
                continue; // etiqueta inválida, se ignora
            }

            if (nombresUnicos.contains(limpio)) {
                continue; // duplicado
            }

            nombresUnicos.add(limpio);

            Etiqueta etiqueta = etiquetaRepo.findByNombreIgnoreCase(limpio)
                    .orElseGet(() -> etiquetaRepo.save(new Etiqueta(null, limpio)));

            resultado.add(etiqueta);
        }

        return resultado;
    }
}

