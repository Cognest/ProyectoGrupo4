package com.atm.buenas_practicas_java.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BusquedaCtrl {


    @GetMapping("/busqueda")
    public String buscar(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "destacados") String ordenamiento,
            @RequestParam(defaultValue = "todo") String tipo,
            Model model
    ) {
        // Añadir parámetros de búsqueda al modelo
        model.addAttribute("query", query);
        model.addAttribute("tipoActual", tipo);
        model.addAttribute("ordenamientoActual", ordenamiento);

        // Aquí iría la lógica de búsqueda una vez implementes el servicio
        // Page<?> resultados = mediaService.buscar(query, tipo,
        //     PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaCreacion")));

        // model.addAttribute("resultados", resultados);

        return "busqueda";
    }
}