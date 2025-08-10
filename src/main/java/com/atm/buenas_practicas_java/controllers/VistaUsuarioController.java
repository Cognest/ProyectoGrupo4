package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.entities.UsuarioBloqueado;
import com.atm.buenas_practicas_java.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class VistaUsuarioController {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private UsuarioContenidoRepo usuarioContenidoRepo;

    @Autowired
    private UsuarioBloqueadoRepo usuarioBloqueadoRepo;

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private GuardadoRepo guardadoRepo;

    @GetMapping("/usuario/{nickname}")
    public String vistaUsuario(@PathVariable String nickname, Model model, Principal principal) {
        Usuario autor = usuarioRepo.findByNickname(nickname).get();
        boolean usuarioBloqueado = false;

        List<ContenidoSubidoDTO> galeria = usuarioContenidoRepo.findContenidosSubidosPorUsuario(autor);
        model.addAttribute("galeria", galeria);

        if (principal != null) {
            Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();
            model.addAttribute("usuarioLogueado", usuario);

            usuarioBloqueado = usuarioBloqueadoRepo.existsByBloqueadoAndBloqueador(autor, usuario);

            // IDs de contenido con like o guardado
            Set<Integer> likesIds = likeRepo.findByUsuario(usuario).stream()
                    .map(like -> like.getContenido().getId())
                    .collect(Collectors.toSet());

            Set<Integer> guardadosIds = guardadoRepo.findByUsuario(usuario).stream()
                    .map(guardado -> guardado.getContenido().getId())
                    .collect(Collectors.toSet());

            model.addAttribute("likesIds", likesIds);
            model.addAttribute("guardadosIds", guardadosIds);
            model.addAttribute("usuarioBloqueado", usuarioBloqueado);
        } else {
            model.addAttribute("likesIds", Set.of());
            model.addAttribute("guardadosIds", Set.of());
            model.addAttribute("usuarioBloqueado", usuarioBloqueado);
        }

        model.addAttribute("usuario", autor);
        return "vistaUsuario/vistaUsuario"; // nombre del HTML
    }

    @PostMapping("/bloquear-usuario")
    public String bloquearUsuario(@RequestParam Integer usuarioBloqueadoId, Principal principal,
                                  @RequestHeader(value = "referer", required = false) String referer) {

        Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();
        if (usuario == null) return "redirect:/iniciar-sesion";

        Usuario userBloqueado = usuarioRepo.findById(usuarioBloqueadoId).get();

        UsuarioBloqueado usuarioBloqueado = new UsuarioBloqueado();
        usuarioBloqueado.setFecha(LocalDateTime.now());
        usuarioBloqueado.setBloqueado(userBloqueado);
        usuarioBloqueado.setBloqueador(usuario);
        usuarioBloqueadoRepo.save(usuarioBloqueado);

        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/desbloquear-usuario")
    public String desbloquearUsuario(@RequestParam Integer usuarioBloqueadoId, Principal principal,
                                     @RequestHeader(value = "referer", required = false) String referer) {

        Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();
        if (usuario == null) return "redirect:/iniciar-sesion";

        Usuario userBloqueado = usuarioRepo.findById(usuarioBloqueadoId).get();

        UsuarioBloqueado usuarioBloqueado = usuarioBloqueadoRepo.findByBloqueadoAndBloqueador(userBloqueado, usuario);
        usuarioBloqueadoRepo.delete(usuarioBloqueado);

        return "redirect:" + (referer != null ? referer : "/");
    }
}
