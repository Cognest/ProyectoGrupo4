package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.entities.Cartera;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.CarteraRepo;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
public class PlanesController {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private CarteraRepo carteraRepo;

    private final List<Map<String, Object>> planes = List.of(
            Map.of("id", 1, "nombre", "Principiante", "precio", 9.99, "tokens", 1000),
            Map.of("id", 2, "nombre", "Más vendido", "precio", 24.99, "tokens", 2800),
            Map.of("id", 3, "nombre", "Avanzado", "precio", 39.99, "tokens", 5000),
            Map.of("id", 4, "nombre", "Experto", "precio", 99.99, "tokens", 13500)
    );

    @GetMapping("/planes")
    public String mostrarPlanPrecios(Model model)
    {
        model.addAttribute("planes", planes);
        return "planes/planPrecios"; // View name
    }

    @GetMapping("/pagar")
    public String formularioPago(@RequestParam int planId, Model model) {
        Map<String, Object> plan = planes.stream()
                .filter(p -> (int) p.get("id") == planId)
                .findFirst()
                .orElse(null);

        if (plan == null) return "redirect:/planes";

        model.addAttribute("plan", plan);
        return "planes/formularioPago";
    }

    @PostMapping("/procesar-pago")
    public String procesarPago(@RequestParam int planId,
                               @RequestParam String numeroTarjeta,
                               @RequestParam String nombreTitular,
                               @RequestParam String cvv,
                               @RequestParam String exp,
                               Model model,/*,
                               Principal principal*/Principal principal) {

        Map<String, Object> plan = planes.stream()
                .filter(p -> (int) p.get("id") == planId)
                .findFirst()
                .orElse(null);

        if (plan == null) return "redirect:/planes";

        // Validación básica
        if (!numeroTarjeta.matches("\\d{16}") || !cvv.matches("\\d{3}") || !exp.matches("\\d{2}/\\d{2}")) {
            model.addAttribute("plan", plan);
            model.addAttribute("error", "Datos de tarjeta inválidos");
            return "planes/formularioPago";
        }

        int tokens = (int) plan.get("tokens");

        // Actualizar los tokens del usuario
        Usuario user = usuarioRepo.findByNickname(principal.getName()).get();
        System.out.println("Mis tokens: " + user.getToken());
        user.setToken(user.getToken() + tokens);
        System.out.println("Mis tokens actualizado: " + user.getToken());
        usuarioRepo.save(user);

        //Crear registro Cartera
        Cartera cartera = new Cartera();
        cartera.setCantidad(tokens);
        cartera.setFecha(LocalDateTime.now());
        cartera.setOperacion("ingreso");
        cartera.setSaldoActual(user.getToken());
        cartera.setUsuario(user);
        carteraRepo.save(cartera);

        model.addAttribute("mensaje", "Pago exitoso. Recibiste " + tokens + " tokens.");
        return "planes/pagoExitoso";
    }
}
