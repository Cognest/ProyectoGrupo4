package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.GuardadoRepo;
import com.atm.buenas_practicas_java.repositories.LikeRepo;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import com.atm.buenas_practicas_java.services.ContenidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/busqueda")
public class BusquedaController {

    private final ContenidoService contenidoService;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private GuardadoRepo guardadoRepo;

    public BusquedaController(ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }

    @GetMapping
    public String realizarBusqueda(
            @RequestParam("buscador") String termino,
            Model model, Principal principal) {

        List<ContenidoSubidoDTO> resultados = contenidoService.buscarPorTermino(termino);
        model.addAttribute("contenidos", resultados);

        if (principal != null) {
            Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();

            // IDs de contenido con like o guardado
            Set<Integer> likesIds = likeRepo.findByUsuario(usuario).stream()
                    .map(like -> like.getContenido().getId())
                    .collect(Collectors.toSet());

            Set<Integer> guardadosIds = guardadoRepo.findByUsuario(usuario).stream()
                    .map(guardado -> guardado.getContenido().getId())
                    .collect(Collectors.toSet());

            model.addAttribute("likesIds", likesIds);
            model.addAttribute("guardadosIds", guardadosIds);
        } else {
            model.addAttribute("likesIds", Set.of());
            model.addAttribute("guardadosIds", Set.of());
        }


        model.addAttribute("busqueda", termino);
        return "busqueda"; // Nombre de la plantilla HTML con los resultados
    }
}