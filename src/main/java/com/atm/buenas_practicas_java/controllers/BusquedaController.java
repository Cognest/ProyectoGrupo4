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
import org.springframework.web.bind.annotation.PathVariable;
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
            @RequestParam(required = false) String orden,
            @RequestParam(required = false) String fecha,
            Model model, Principal principal) {

        List<ContenidoSubidoDTO> resultados = contenidoService.buscarPorTermino(termino, orden, fecha);
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
        model.addAttribute("orden", orden);
        model.addAttribute("fecha", fecha);
        return "busqueda/busqueda"; // Nombre de la plantilla HTML con los resultados
    }

    @GetMapping("/{tipo}")
    public String busquedaPorTipo(
            @PathVariable String tipo,
            @RequestParam(value = "buscador", required = false) String termino,
            @RequestParam(required = false) String orden,
            @RequestParam(required = false) String fecha,
            Principal principal,
            Model model) {

        // Validar el tipo (opcionalmente)
        List<String> tiposValidos = List.of("imagenes", "videos", "audios", "modelos3d");
        if (!tiposValidos.contains(tipo.toLowerCase())) {
            return "index";
        }

        List<ContenidoSubidoDTO> resultados = null;

        if (termino == null || termino.isEmpty()) {
            resultados = contenidoService.buscarPorTipo(tipo, orden, fecha);
        }
        else {
            resultados = contenidoService.buscarPorTipoTermino(termino, tipo, orden, fecha);
        }

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

        //FILTROS SUBTIPOS
        List<String> subtipos = switch (tipo.toLowerCase()) {
            case "imagenes" -> List.of("stock", "ilustraciones", "texturas", "vectores");
            case "videos" -> List.of("stock", "vfx", "transiciones", "loops");
            case "audios" -> List.of("musica", "sfx", "ambientes sonoros", "samples");
            case "modelos3d" -> List.of("modelos estaticos", "modelos con rigging", "lowpoly", "escenarios");
            default -> Collections.emptyList();
        };

        model.addAttribute("subtipos", subtipos);


        model.addAttribute("busqueda", termino);
        model.addAttribute("tipo", tipo);
        model.addAttribute("orden", orden);
        model.addAttribute("fecha", fecha);
        return "busqueda/busquedaTipo"; // Vista especializada (ej: solo imágenes)
    }

    @GetMapping("/{tipo}/{subtipo}")
    public String busquedaPorSubtipo(
            @PathVariable String tipo,
            @PathVariable String subtipo,
            @RequestParam(value = "buscador", required = false) String termino,
            @RequestParam(required = false) String orden,
            @RequestParam(required = false) String fecha,
            Principal principal,
            Model model) {

        List<ContenidoSubidoDTO> resultados = null;

        if (termino == null || termino.isEmpty()) {
            resultados = contenidoService.buscarPorSubtipo(tipo, subtipo, orden, fecha);
        }
        else {
            resultados = contenidoService.buscarPorSubtipoTermino(termino, tipo, subtipo, orden, fecha);
        }

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
        model.addAttribute("tipo", tipo);
        model.addAttribute("subtipo", subtipo);
        model.addAttribute("orden", orden);
        model.addAttribute("fecha", fecha);
        return "busqueda/busquedaSubtipo"; // Vista especializada (ej: solo imágenes)
    }
}