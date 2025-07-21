package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.UsuarioContenidoDto;
import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.entities.UsuarioContenido;
import com.atm.buenas_practicas_java.repositories.UsuarioContenidoRepo;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import com.atm.buenas_practicas_java.services.mapper.UsuarioContenidoMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioContenidoService extends AbstractBusinessService<UsuarioContenido, Integer, UsuarioContenidoDto,
        UsuarioContenidoRepo, UsuarioContenidoMapper> {

    public UsuarioContenidoService(UsuarioContenidoRepo repo, UsuarioContenidoMapper mapper) {
        super(repo, mapper);
    }

    @Autowired
    private UsuarioContenidoRepo usuarioContenidoRepo;

    @Transactional
    public void vincularUsuarioContenido(Usuario usuario, Contenido contenido, String tipo) {
        // Crear relación UsuarioContenido
        UsuarioContenido relacion = new UsuarioContenido();
        relacion.setUsuario(usuario);
        relacion.setContenido(contenido);
        relacion.setTipo(tipo);
        relacion.setPrecio(contenido.getPrecio());

        usuarioContenidoRepo.save(relacion);
    }
}

