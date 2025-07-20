package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.dtos.LikeDto;
import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Like;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.ContenidoRepo;
import com.atm.buenas_practicas_java.repositories.LikeRepo;
import com.atm.buenas_practicas_java.services.mapper.LikeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LikeService extends AbstractBusinessService<Like, Integer, LikeDto,
        LikeRepo, LikeMapper> {

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private ContenidoRepo contenidoRepo;

    public LikeService(LikeRepo repo, LikeMapper mapper) {
        super(repo, mapper);
    }

    public boolean toggleLike(Integer contenidoId, Usuario usuario) {
        Optional<Like> existing = likeRepo.findByContenidoIdAndUsuario(contenidoId, usuario);
        if (existing.isPresent()) {
            likeRepo.delete(existing.get());
            return false;
        } else {
            Contenido contenido = contenidoRepo.findById(contenidoId).orElseThrow();

            Like like = new Like();
            like.setContenido(contenido);
            like.setUsuario(usuario);
            likeRepo.save(like);

            return true;
        }
    }

    public int countLikes(Integer contenidoId) {
        return likeRepo.countByContenidoId(contenidoId);
    }

    public boolean usuarioHaDadoLike(Contenido contenido, Usuario usuario) {
        return likeRepo.findByContenidoIdAndUsuario(contenido.getId(), usuario).isPresent();
    }

}

