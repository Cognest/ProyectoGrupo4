package com.atm.buenas_practicas_java.controllers;


import com.atm.buenas_practicas_java.config.CustomUserDetails;
import com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO;
import com.atm.buenas_practicas_java.entities.*;
import com.atm.buenas_practicas_java.repositories.*;
import com.atm.buenas_practicas_java.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Controlador encargado de manejar las solicitudes relacionadas con la entidad principal.
 *
 * Este controlador utiliza la anotación {@code @Controller} para ser detectado como un componente
 * Spring MVC y maneja solicitudes HTTP. Su objetivo principal es gestionar las operaciones
 * necesarias para mostrar una lista de entidades en la vista correspondiente.
 *
 * Anotaciones importantes:
 * - {@code @Controller}: Indica que esta clase se comporta como un controlador Spring MVC.
 * - {@code @PreAuthorize}: Define que el acceso a ciertos métodos esté restringido
 *   según las reglas de autorización establecidas.
 *
 */
@Controller
public class DefaultController {

    @Autowired
    private ContenidoRepo contenidoRepo;

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private GuardadoRepo guardadoRepo;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private CarteraRepo carteraRepo;

    /**
     * Constructor de la clase DefaultController.
     */
    public DefaultController() {}

    @GetMapping("/")
    public String mostrarHome(Model model, Principal principal)
    {
        List<ContenidoSubidoDTO> contenidos = contenidoRepo.findAllOrderByLikes();
        model.addAttribute("contenidos", contenidos);

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

        return "index"; // View name
    }

    @GetMapping("/reportes")
    public String reportes(Model model)
    {
        return "reportes/reportes"; // View name
    }

    @GetMapping("/chat")
    public String pantallaChat(@RequestParam(name = "usuario", required = false) String receptorNickname,
                               Model model,
                               @AuthenticationPrincipal CustomUserDetails userDetails,
                               Principal principal) {

        String miNick = userDetails.getUsername();
        Usuario yo = usuarioRepo.findByNickname(principal.getName()).orElse(null);

        List<String> nicknamesConversados = chatRepo.findUsuariosConConversacion(yo.getId());
        List<Usuario> usuarios = usuarioRepo.findByNicknameInAndNicknameNot(nicknamesConversados, miNick);

        if (receptorNickname != null && !receptorNickname.equals(miNick)) {
            boolean yaIncluido = usuarios.stream().anyMatch(u -> u.getNickname().equals(receptorNickname));
            if (!yaIncluido) {
                usuarioRepo.findByNickname(receptorNickname).ifPresent(usuarios::add);
            }

            model.addAttribute("receptorPreseleccionado", receptorNickname);
        }

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("miNick", miNick);
        model.addAttribute("persona", yo);

        // Nueva línea clave:
        model.addAttribute("accesoDesdeBoton", receptorNickname == null);

        return "chat/chat";
    }

    @GetMapping("/api/tokens")
    @ResponseBody
    public ResponseEntity<Integer> getTokensUsuario(Principal principal) {
        if (principal != null) {
            int tokens = usuarioRepo.findByNickname(principal.getName())
                    .map(Usuario::getToken)
                    .orElse(0);
            return ResponseEntity.ok(tokens);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/conocenos")
    public String conocenos(Model model, Principal principal) {
        if (principal != null) {
            Usuario usuario = usuarioRepo.findByNickname(principal.getName()).orElse(null);
            model.addAttribute("usuario", usuario);
        }
        return "conocenos/conocenos";
    }

    @GetMapping("/cartera")
    public String pantallaCartera(Model model, Principal principal) {

        Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();
        List<Cartera> carteras = carteraRepo.findAllByUsuarioOrderByFechaDesc(usuario);

        model.addAttribute("carteras", carteras);
        model.addAttribute("usuario", usuario);

        return "cartera/cartera";
    }
}
