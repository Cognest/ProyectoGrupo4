package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@Component
@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private UsuarioRepo usuarioRepo;

    @ModelAttribute("tokensUsuario")
    public Integer getTokensUsuario(Principal principal) {
        if (principal != null) {
            return usuarioRepo.findByNickname(principal.getName())
                    .map(Usuario::getToken)
                    .orElse(0);
        }
        return 0;
    }
}
