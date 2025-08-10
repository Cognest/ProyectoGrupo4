package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.dtos.UsuarioRegistroDto;
import com.atm.buenas_practicas_java.services.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/registrarse")
    public String mostrarFormularioRegistro(Model model, HttpServletRequest request) {
        if (request.getUserPrincipal() != null) return "redirect:/";
        model.addAttribute("usuario", new UsuarioRegistroDto());
        return "sesion/signin";
    }

    @PostMapping("/registrarse")
    public String registrarUsuario(@ModelAttribute("usuario") UsuarioRegistroDto usuarioDto,
                                   @RequestParam("confirmPassword") String confirmPassword,
                                   Model model) throws Exception {

        try{
            System.out.println("🟢 ENTRANDO EN EL MÉTODO DE REGISTRO");
            if (!usuarioDto.getPassword().equals(confirmPassword)) {
                model.addAttribute("error", "Las contraseñas no coinciden.");
                return "sesion/signin";
            }

            if (usuarioService.emailONicknameExiste(usuarioDto.getEmail(), usuarioDto.getNickname())) {
                model.addAttribute("error", "El email o nickname ya están registrados.");
                return "sesion/signin";
            }

            usuarioService.registrarUsuario(usuarioDto);

            return "redirect:/iniciar-sesion?registroExitoso";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error inesperado: " + e.getMessage());
            return "sesion/signin";
        }

    }

    @GetMapping("/iniciar-sesion")
    public String mostrarLogin(HttpServletRequest request)
    {
        if (request.getUserPrincipal() != null) {
            return "redirect:/";
        }
        return "sesion/login"; // View name
    }

    @GetMapping("/cerrar-sesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/iniciar-sesion";
    }

}
