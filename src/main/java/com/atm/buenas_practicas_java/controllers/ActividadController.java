package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.dtos.ComentarioRealizadoDto;
import com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.GuardadoRepo;
import com.atm.buenas_practicas_java.repositories.LikeRepo;
import com.atm.buenas_practicas_java.repositories.UsuarioContenidoRepo;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import com.atm.buenas_practicas_java.services.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/actividad")
public class ActividadController {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private GuardadoRepo guardadoRepo;

    @Autowired
    private UsuarioContenidoRepo usuarioContenidoRepo;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private LikeRepo likeRepo;

    @GetMapping
    public String tuActividad(Model model, Principal principal) {
        if (principal != null) {
            Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();

            model.addAttribute("usuario", usuario);
        } else {
            model.addAttribute("likesIds", Set.of());
            model.addAttribute("guardadosIds", Set.of());
        }
        return "actividad/tuActividad";

    }

    @GetMapping("/guardados")
    public String actividadGuardados(Model model, Principal principal) {
        if (principal != null) {
            Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();

            List<ContenidoSubidoDTO> contenidos = guardadoRepo.findContenidoGuardadoDTOByUsuario(usuario);
            model.addAttribute("contenidos", contenidos);

            // IDs de contenido con like o guardado
            Set<Integer> likesIds = likeRepo.findByUsuario(usuario).stream()
                    .map(like -> like.getContenido().getId())
                    .collect(Collectors.toSet());

            Set<Integer> guardadosIds = guardadoRepo.findByUsuario(usuario).stream()
                    .map(guardado -> guardado.getContenido().getId())
                    .collect(Collectors.toSet());

            model.addAttribute("likesIds", likesIds);
            model.addAttribute("guardadosIds", guardadosIds);
            model.addAttribute("usuario", usuario);
        } else {
            model.addAttribute("likesIds", Set.of());
            model.addAttribute("guardadosIds", Set.of());
        }
        return "actividad/guardados";
    }

    @GetMapping("/contenidos-adquiridos")
    public String actividadAdquiridos(Model model, Principal principal) {
        if (principal != null) {
            Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();

            List<ContenidoSubidoDTO> adquiridos = usuarioContenidoRepo.findContenidosAdquiridosPorUsuario(usuario);
            model.addAttribute("adquiridos", adquiridos);

            // IDs de contenido con like o guardado
            Set<Integer> likesIds = likeRepo.findByUsuario(usuario).stream()
                    .map(like -> like.getContenido().getId())
                    .collect(Collectors.toSet());

            Set<Integer> guardadosIds = guardadoRepo.findByUsuario(usuario).stream()
                    .map(guardado -> guardado.getContenido().getId())
                    .collect(Collectors.toSet());

            model.addAttribute("likesIds", likesIds);
            model.addAttribute("guardadosIds", guardadosIds);
            model.addAttribute("usuario", usuario);
        } else {
            model.addAttribute("likesIds", Set.of());
            model.addAttribute("guardadosIds", Set.of());
        }
        return "actividad/contenidoAdquirido";
    }

    @GetMapping("/comentarios")
    public String actividadComentarios(Model model, Principal principal, @RequestParam(defaultValue = "0") int page) {
        if (principal != null) {
            Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();

            Page<ComentarioRealizadoDto> comentarios = comentarioService.obtenerComentariosRealizados(usuario, page, 5);
            model.addAttribute("comentarios", comentarios);
            model.addAttribute("usuario", usuario);
        } else {
            model.addAttribute("likesIds", Set.of());
            model.addAttribute("guardadosIds", Set.of());
        }
        return "actividad/listaComentarios";
    }
}
