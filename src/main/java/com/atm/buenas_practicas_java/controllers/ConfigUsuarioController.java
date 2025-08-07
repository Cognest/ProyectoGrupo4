package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.entities.UsuarioBloqueado;
import com.atm.buenas_practicas_java.repositories.UsuarioBloqueadoRepo;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/config-perfil")
public class ConfigUsuarioController {

    @Autowired
    private UsuarioRepo usuarioRepo;
    @Autowired
    private UsuarioBloqueadoRepo usuarioBloqueadoRepo;

    @GetMapping
    public String tuActividad(Model model, Principal principal) {
        if (principal != null) {
            Usuario usuario = usuarioRepo.findByNickname(principal.getName()).orElse(null);
            model.addAttribute("usuario", usuario);
        }
        return "configPerfil";
    }

    @GetMapping("/editar-foto")
    public String editarFoto(Model model, Principal principal) {
        if (principal != null) {
            Usuario usuario = usuarioRepo.findByNickname(principal.getName()).orElse(null);
            model.addAttribute("usuario", usuario);
        }
        return "cambiarAvatar";
    }

    @GetMapping("/seguridad")
    public String seguridadUsuario(Model model, Principal principal) {
        if (principal != null) {
            Usuario usuario = usuarioRepo.findByNickname(principal.getName()).orElse(null);
            model.addAttribute("usuario", usuario);
        }
        return "contraseniasSeguridad";
    }

    @PostMapping("/seguridad")
    public String cambiarPass(Principal principal,
                                BCryptPasswordEncoder passwordEncoder,
                                @RequestParam(value = "contrasenaActual", required = false) String contrasenaActual,
                                @RequestParam(value = "nuevaPassword", required = false) String nuevaPassword,
                                RedirectAttributes redirectAttrs) {

        if (principal != null) {
            Usuario usuario = usuarioRepo.findByNickname(principal.getName()).orElse(null);

            // Si se quieren cambiar las contraseñas
            if (nuevaPassword != null && !nuevaPassword.isBlank()) {
                if (contrasenaActual == null || contrasenaActual.isBlank()) {
                    redirectAttrs.addFlashAttribute("error", "Debes introducir la contraseña actual para cambiarla.");
                    System.out.println("No escribiste la contrasenaActual = " + contrasenaActual);
                    return "redirect:/config-perfil/seguridad";
                }

                // Verificar contraseña actual
                if (!passwordEncoder.matches(contrasenaActual, usuario.getPassword())) {
                    redirectAttrs.addFlashAttribute("error", "La contraseña actual es incorrecta.");
                    System.out.println("Esta mal la contrasenaActual = " + contrasenaActual);
                    return "redirect:/config-perfil/seguridad";
                }

                // Establecer nueva contraseña
                usuario.setPassword(passwordEncoder.encode(nuevaPassword));
            }

            usuarioRepo.save(usuario);
            System.out.println("contraseña actual:" + contrasenaActual + "nueva contraseña" + nuevaPassword);
            redirectAttrs.addFlashAttribute("exito", "Usuario actualizado correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "No se pudo actualizar el usuario.");
        }
        return "redirect:/config-perfil/seguridad";
    }

    @GetMapping("/datos-personales")
    public String datosPersonales(Model model, Principal principal) {
        if (principal != null) {
            Usuario usuario = usuarioRepo.findByNickname(principal.getName()).orElse(null);
            model.addAttribute("usuario", usuario);
        }
        return "datosPersonales";
    }

    @PostMapping("/datos-personales")
    public String guardarCambiosUsuario(Principal principal,
                                        @ModelAttribute("usuario") Usuario usuarioActualizado,
                                        RedirectAttributes redirectAttrs) {

        if (principal != null) {
            Usuario usuario = usuarioRepo.findByNickname(principal.getName()).orElse(null);
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setApellidos(usuarioActualizado.getApellidos());
            usuario.setNickname(usuarioActualizado.getNickname());
            usuario.setEmail(usuarioActualizado.getEmail());

            usuarioRepo.save(usuario);
            redirectAttrs.addFlashAttribute("exito", "Usuario actualizado correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "No se pudo actualizar el usuario.");
        }
        return "redirect:/config-perfil/datos-personales";
    }

    @GetMapping("/perfiles-bloqueados")
    public String mostrarUsuariosBloqueados(Model model, Principal principal)
    {
        if (principal != null) {
            Usuario usuario = usuarioRepo.findByNickname(principal.getName()).orElse(null);
            List<UsuarioBloqueado> usuariosBloqueados = usuarioBloqueadoRepo.findAllByBloqueador(usuario);

            model.addAttribute("usuario", usuario);
            model.addAttribute("usuariosBloqueados", usuariosBloqueados);
        }

        return "usuariosBloqueados"; // View name
    }
}
