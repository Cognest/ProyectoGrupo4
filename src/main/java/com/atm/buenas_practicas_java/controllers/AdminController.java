package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.entities.UserRol;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private GuardadoRepo guardadoRepo;

    @Autowired
    private LikeRepo likeRepo;

    @Autowired
    private ComentarioRepo comentarioRepo;

    @Autowired
    private CarteraRepo carteraRepo;

    @Autowired
    private UsuarioContenidoRepo usuarioContenidoRepo;

    @GetMapping("/admin/usuarios")
    public String listarUsuarios(@RequestParam(defaultValue = "0") int page,
                                 Model model,
                                 Principal principal) {
        Usuario usuarioActual = usuarioRepo.findByNickname(principal.getName()).orElse(null);

        Pageable pageable = PageRequest.of(page, 10, Sort.by("nickname").ascending()); // 10 por página
        Page<Usuario> usuariosPage = usuarioRepo.findAllByIdNot(usuarioActual.getId(), pageable);

        model.addAttribute("usuariosPage", usuariosPage);
        model.addAttribute("currentPage", page);
        return "admin/adminUsuarios";
    }

    @PostMapping("/admin/usuarios/eliminar/{id}")
    @Transactional
    public String eliminarUsuario(@PathVariable Integer id, RedirectAttributes redirectAttrs) {
        Usuario usuario = usuarioRepo.findById(id).orElse(null);
        if (usuario != null) {
            //eliminar relaciones dependientes
            guardadoRepo.deleteAllByUsuario(usuario);
            likeRepo.deleteAllByUsuario(usuario);
            comentarioRepo.deleteAllByUsuario(usuario);
            carteraRepo.deleteAllByUsuario(usuario);
            usuarioContenidoRepo.deleteAllByUsuario(usuario);

            //eliminar usuario
            usuarioRepo.delete(usuario);
            redirectAttrs.addFlashAttribute("exito", "Usuario eliminado correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "Usuario no encontrado.");
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/admin/usuarios/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable Integer id, Model model, RedirectAttributes redirectAttrs) {
        Optional<Usuario> usuarioOpt = usuarioRepo.findById(id);
        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
            model.addAttribute("roles", UserRol.values()); // Si usas Enum
            return "admin/editarUsuario";
        } else {
            redirectAttrs.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/admin/usuarios";
        }
    }

    @PostMapping("/admin/usuarios/editar/{id}")
    public String guardarCambiosUsuario(@PathVariable Integer id,
                                        BCryptPasswordEncoder passwordEncoder,
                                        @ModelAttribute("usuario") Usuario usuarioActualizado,
                                        @RequestParam(value = "contrasenaActual", required = false) String contrasenaActual,
                                        @RequestParam(value = "nuevaPassword", required = false) String nuevaPassword,
                                        RedirectAttributes redirectAttrs) {
        Optional<Usuario> usuarioOpt = usuarioRepo.findById(id);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setNombre(usuarioActualizado.getNombre());
            usuario.setApellidos(usuarioActualizado.getApellidos());
            usuario.setNickname(usuarioActualizado.getNickname());
            usuario.setEmail(usuarioActualizado.getEmail());
            usuario.setRol(usuarioActualizado.getRol());

            // Si se quieren cambiar las contraseñas
            if (nuevaPassword != null && !nuevaPassword.isBlank()) {
                if (contrasenaActual == null || contrasenaActual.isBlank()) {
                    redirectAttrs.addFlashAttribute("error", "Debes introducir la contraseña actual para cambiarla.");
                    return "redirect:/admin/usuarios/editar/" + id;
                }

                // Verificar contraseña actual
                if (!passwordEncoder.matches(contrasenaActual, usuario.getPassword())) {
                    redirectAttrs.addFlashAttribute("error", "La contraseña actual es incorrecta.");
                    return "redirect:/admin/usuarios/editar/" + id;
                }

                // Establecer nueva contraseña
                usuario.setPassword(passwordEncoder.encode(nuevaPassword));
            }

            usuarioRepo.save(usuario);
            redirectAttrs.addFlashAttribute("exito", "Usuario actualizado correctamente.");
        } else {
            redirectAttrs.addFlashAttribute("error", "No se pudo actualizar el usuario.");
        }
        return "redirect:/admin/usuarios";
    }
}
