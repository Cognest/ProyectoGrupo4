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
    public Contenido subirContenido(ContenidoDto contenidoDto, String tipo, String subtipo, String url, String nickname, String urlPortada) {
        Contenido contenido = contenidoMapper.toEntity(contenidoDto);

        System.out.println("Url en el servicio: " + url);
        contenido.setUrl(url);
        contenido.setFormato(tipo);
        contenido.setSubtipo(subtipo.toLowerCase().replace(' ', '-'));
        contenido.setUrlPortada(urlPortada);
        contenido.setFecha(LocalDateTime.now());
        contenido.setVisitas(0);
        contenidoRepo.save(contenido);

        return contenido;
    }

    public List<ContenidoSubidoDTO> buscarPorTermino(String termino, String orden, String fecha) {
        LocalDateTime fechaInicio = null;

        if ("hoy".equals(fecha)) {
            fechaInicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        } else if ("semana".equals(fecha)) {
            fechaInicio = LocalDateTime.now().minusDays(7);
        } else if ("mes".equals(fecha)) {
            fechaInicio = LocalDateTime.now().minusDays(30);
        }

        if ("recientes".equals(orden)) {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorTerminoFecha(termino);
            }
            else {
                return contenidoRepo.buscarPorTerminoOrdenadoPorFecha(termino, fechaInicio);
            }
        } else if ("vistos".equals(orden)) {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorTerminoVisitas(termino);
            }
            else {
                return contenidoRepo.buscarPorTerminoOrdenadoPorVistas(termino, fechaInicio);
            }
        } else {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorTermino(termino);
            } else {
                return contenidoRepo.buscarPorTerminoOrdenadoPorLikes(termino, fechaInicio);
            }
        }
    }

    public List<ContenidoSubidoDTO> buscarPorTipo(String tipo, String orden, String fecha) {
        LocalDateTime fechaInicio = null;

        if ("hoy".equals(fecha)) {
            fechaInicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        } else if ("semana".equals(fecha)) {
            fechaInicio = LocalDateTime.now().minusDays(7);
        } else if ("mes".equals(fecha)) {
            fechaInicio = LocalDateTime.now().minusDays(30);
        }

        if ("recientes".equals(orden)) {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorTipoFecha(tipo);
            }
            else {
                return contenidoRepo.buscarPorTipoOrdenadoPorFecha(tipo, fechaInicio);
            }
        } else if ("vistos".equals(orden)) {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorTipoVisitas(tipo);
            }
            else {
                return contenidoRepo.buscarPorTipoOrdenadoPorVistas(tipo, fechaInicio);
            }
        } else {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorTipo(tipo);
            } else {
                return contenidoRepo.buscarPorTipoOrdenadoPorLikes(tipo, fechaInicio);
            }
        }
    }

    public List<ContenidoSubidoDTO> buscarPorTipoTermino(String termino, String tipo, String orden, String fecha) {
        LocalDateTime fechaInicio = null;

        if ("hoy".equals(fecha)) {
            fechaInicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        } else if ("semana".equals(fecha)) {
            fechaInicio = LocalDateTime.now().minusDays(7);
        } else if ("mes".equals(fecha)) {
            fechaInicio = LocalDateTime.now().minusDays(30);
        }

        if ("recientes".equals(orden)) {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorTipoTerminoFecha(termino, tipo);
            }
            else {
                return contenidoRepo.buscarPorTipoTerminoOrdenadoPorFecha(termino, tipo, fechaInicio);
            }
        } else if ("vistos".equals(orden)) {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorTipoTerminoVisitas(termino, tipo);
            }
            else {
                return contenidoRepo.buscarPorTipoTerminoOrdenadoPorVistas(termino, tipo, fechaInicio);
            }
        } else {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorTipoTermino(termino, tipo);
            } else {
                return contenidoRepo.buscarPorTipoTerminoOrdenadoPorLikes(termino, tipo, fechaInicio);
            }
        }
    }

    public List<ContenidoSubidoDTO> buscarPorSubtipo(String tipo, String subtipo, String orden, String fecha) {
        LocalDateTime fechaInicio = null;

        if ("hoy".equals(fecha)) {
            fechaInicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        } else if ("semana".equals(fecha)) {
            fechaInicio = LocalDateTime.now().minusDays(7);
        } else if ("mes".equals(fecha)) {
            fechaInicio = LocalDateTime.now().minusDays(30);
        }

        if ("recientes".equals(orden)) {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorSubtipoFecha(tipo, subtipo);
            }
            else {
                return contenidoRepo.buscarPorSubtipoOrdenadoPorFecha(tipo, subtipo, fechaInicio);
            }
        } else if ("vistos".equals(orden)) {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorSubtipoVisitas(tipo, subtipo);
            }
            else {
                return contenidoRepo.buscarPorSubtipoOrdenadoPorVistas(tipo, subtipo, fechaInicio);
            }
        } else {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorSubtipo(tipo, subtipo);
            } else {
                return contenidoRepo.buscarPorSubtipoOrdenadoPorLikes(tipo, subtipo, fechaInicio);
            }
        }
    }

    public List<ContenidoSubidoDTO> buscarPorSubtipoTermino(String termino, String tipo, String subtipo, String orden, String fecha) {
        LocalDateTime fechaInicio = null;

        if ("hoy".equals(fecha)) {
            fechaInicio = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        } else if ("semana".equals(fecha)) {
            fechaInicio = LocalDateTime.now().minusDays(7);
        } else if ("mes".equals(fecha)) {
            fechaInicio = LocalDateTime.now().minusDays(30);
        }

        if ("recientes".equals(orden)) {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorSubtipoTerminoFecha(termino, tipo, subtipo);
            }
            else {
                return contenidoRepo.buscarPorSubtipoTerminoOrdenadoPorFecha(termino, tipo, subtipo, fechaInicio);
            }
        } else if ("vistos".equals(orden)) {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorSubtipoTerminoVisitas(termino, tipo, subtipo);
            }
            else {
                return contenidoRepo.buscarPorSubtipoTerminoOrdenadoPorVistas(termino, tipo, subtipo, fechaInicio);
            }
        } else {
            if (fechaInicio == null) {
                return contenidoRepo.buscarPorSubtipoTermino(termino, tipo, subtipo);
            } else {
                return contenidoRepo.buscarPorSubtipoTerminoOrdenadoPorLikes(termino, tipo, subtipo, fechaInicio);
            }
        }
    }
}

