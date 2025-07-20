package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.EtiquetaContenidoDto;
import com.atm.buenas_practicas_java.entities.*;
import com.atm.buenas_practicas_java.repositories.EtiquetaContenidoRepo;
import com.atm.buenas_practicas_java.services.mapper.EtiquetaContenidoMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtiquetaContenidoService extends AbstractBusinessService<EtiquetaContenido, EtiquetaContenidoId, EtiquetaContenidoDto, EtiquetaContenidoRepo, EtiquetaContenidoMapper>{
    @Autowired
    private EtiquetaContenidoRepo etiquetaContenidoRepo;

    @Autowired
    private EtiquetaService etiquetaService;

    public EtiquetaContenidoService(EtiquetaContenidoRepo repo, EtiquetaContenidoMapper mapper) { super(repo, mapper); }

    @Transactional
    public void vincularEtiquetaContenido(Contenido contenido, String etiquetasTexto) {
        // Procesar etiquetas
        List<Etiqueta> etiquetas = etiquetaService.procesarEtiquetas(etiquetasTexto);
        for (Etiqueta etiqueta : etiquetas) {
            EtiquetaContenidoId id = new EtiquetaContenidoId(etiqueta.getId(), contenido.getId());

            // Verifica si ya existe
            if (!etiquetaContenidoRepo.existsById(id)) {
                EtiquetaContenido ec = new EtiquetaContenido();
                ec.setEtiqueta(etiqueta);
                ec.setContenido(contenido);
                etiquetaContenidoRepo.save(ec);
            }
        }
    }
}
