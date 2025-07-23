package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.ContenidoDto;
import com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO;
import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.entities.UsuarioContenido;
import com.atm.buenas_practicas_java.repositories.ContenidoRepo;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import com.atm.buenas_practicas_java.services.mapper.ContenidoMapper;
import com.atm.buenas_practicas_java.services.mapper.UsuarioMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContenidoService extends AbstractBusinessService<Contenido, Integer, ContenidoDto,
        ContenidoRepo, ContenidoMapper> {

    @Autowired
    private ContenidoRepo contenidoRepo;

    @Autowired
    private ContenidoMapper contenidoMapper;



    public ContenidoService(ContenidoRepo repo, ContenidoMapper mapper) {
        super(repo, mapper);
    }

    @Transactional
    public Contenido subirContenido(ContenidoDto contenidoDto, String tipo, String url, String nickname, String urlPortada) {
        Contenido contenido = contenidoMapper.toEntity(contenidoDto);

        System.out.println("Url en el servicio: " + url);
        contenido.setUrl(url);
        contenido.setFormato(tipo);
        contenido.setUrlPortada(urlPortada);
        contenido.setFecha(LocalDateTime.now());
        contenidoRepo.save(contenido);

        return contenido;
    }

    public List<ContenidoSubidoDTO> buscarPorTermino(String termino) {
        return contenidoRepo.buscarContenidoPorTermino(termino);
    }
}

