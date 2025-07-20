package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.GuardadoDto;
import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Guardado;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.ContenidoRepo;
import com.atm.buenas_practicas_java.repositories.GuardadoRepo;
import com.atm.buenas_practicas_java.services.mapper.GuardadoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GuardadoService extends AbstractBusinessService<Guardado, Integer, GuardadoDto,
        GuardadoRepo, GuardadoMapper> {

    @Autowired
    private GuardadoRepo guardadoRepo;

    @Autowired
    private ContenidoRepo contenidoRepo;

    public GuardadoService(GuardadoRepo repo, GuardadoMapper mapper) {super(repo, mapper);}

    public boolean toggleGuardar(Contenido miContenido, Usuario usuario) {
        Optional<Guardado> guardado = guardadoRepo.findByUsuarioAndContenido(usuario, miContenido);
        if (guardado.isPresent()) {
            guardadoRepo.delete(guardado.get());
            return false;
        } else {
            Guardado nuevo = new Guardado();
            nuevo.setUsuario(usuario);
            nuevo.setContenido(miContenido);
            guardadoRepo.save(nuevo);
            return true;
        }
    }

    public boolean estaGuardadoPorUsuario(Contenido contenido, Usuario usuario) {
        return guardadoRepo.findByUsuarioAndContenido(usuario, contenido).isPresent();
    }
}
