package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.entities.Cartera;
import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.entities.UsuarioContenido;
import com.atm.buenas_practicas_java.repositories.CarteraRepo;
import com.atm.buenas_practicas_java.repositories.ContenidoRepo;
import com.atm.buenas_practicas_java.repositories.UsuarioContenidoRepo;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CarritoController {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private UsuarioContenidoRepo usuarioContenidoRepo;

    @Autowired
    private ContenidoRepo contenidoRepo;

    @Autowired
    private CarteraRepo carteraRepo;

    @GetMapping("/carrito")
    public String pantallaCarrito(Model model, Principal principal) {

        Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();
        if (usuario == null) return "redirect:/iniciar-sesion";

        List<UsuarioContenido> carrito = usuarioContenidoRepo.findAllByTipoAndUsuario("Pendiente", usuario);
        model.addAttribute("carrito", carrito);

        Integer total = carrito.stream()
                .mapToInt(item -> item.getContenido().getPrecio())
                .sum();

        model.addAttribute("total", total);

        return "carrito/carritoCompra";
    }

    @PostMapping("/carrito/anadir")
    public String anadirAlCarrito(@RequestParam Integer contenidoId, Principal principal,
                                  @RequestHeader(value = "referer", required = false) String referer) {

        Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();
        if (usuario == null) return "redirect:/iniciar-sesion";

        Contenido contenido = contenidoRepo.findById(contenidoId).get();
        UsuarioContenido usuarioContenido = new UsuarioContenido();
        usuarioContenido.setUsuario(usuario);
        usuarioContenido.setContenido(contenido);
        usuarioContenido.setTipo("Pendiente");
        usuarioContenido.setPrecio(contenido.getPrecio());
        usuarioContenidoRepo.save(usuarioContenido);

        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/carrito/eliminar")
    @Transactional
    public String eliminarContenidoCarrito(@RequestParam Integer contenidoId, Principal principal,
                                           @RequestHeader(value = "referer", required = false) String referer){

        Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();
        if (usuario == null) return "redirect:/iniciar-sesion";

        Contenido contenido = contenidoRepo.findById(contenidoId).get();
        usuarioContenidoRepo.deleteUsuarioContenidoByContenidoAndUsuario(contenido, usuario);

        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/carrito/finalizar")
    @Transactional
    public String finalizarCompra(@RequestParam List<Integer> contenidoIds, @RequestParam Integer totalCompra, Principal principal) {

        Usuario comprador = usuarioRepo.findByNickname(principal.getName()).orElse(null);
        if (comprador == null) return "redirect:/iniciar-sesion";

        // Validar si tiene suficientes tokens
        if (comprador.getToken() < totalCompra) {
            return "redirect:/carrito?errorTokens=true";
        }

        List<UsuarioContenido> relaciones = usuarioContenidoRepo
                .findAllByUsuarioAndContenidoIdInAndTipo(comprador, contenidoIds, "Pendiente");


        for (UsuarioContenido uc : relaciones) {
            Contenido contenido = uc.getContenido();
            Integer precio = contenido.getPrecio();
            LocalDateTime now = LocalDateTime.now();

            // Buscar relación del creador de este contenido
            UsuarioContenido relacionCreador = usuarioContenidoRepo
                    .findByContenidoAndTipo(contenido, "Creador");

            if (relacionCreador == null) continue; // Seguridad

            Usuario vendedor = relacionCreador.getUsuario();

            // Cambiar tipo a Adquirido
            uc.setTipo("Adquirido");

            // Transferencia de tokens
            comprador.setToken(comprador.getToken() - precio);
            vendedor.setToken(vendedor.getToken() + precio);

            // Movimiento cartera - compra
            Cartera compra = new Cartera();
            compra.setUsuario(comprador);
            compra.setCantidad(precio);
            compra.setSaldoActual(comprador.getToken());
            compra.setOperacion("compra");
            compra.setFecha(now);
            carteraRepo.save(compra);

            // Movimiento cartera - venta
            Cartera venta = new Cartera();
            venta.setUsuario(vendedor);
            venta.setCantidad(precio);
            venta.setSaldoActual(vendedor.getToken());
            venta.setOperacion("venta");
            venta.setFecha(now);
            carteraRepo.save(venta);
        }

        // Guardar cambios
        usuarioContenidoRepo.saveAll(relaciones);
        usuarioRepo.save(comprador);

        // Guardar vendedores únicos
        List<Usuario> vendedores = relaciones.stream()
                .map(uc -> usuarioContenidoRepo
                        .findByContenidoAndTipo(uc.getContenido(), "Creador")
                        .getUsuario())
                .distinct()
                .toList();
        usuarioRepo.saveAll(vendedores);

        return "redirect:/?compraExitosa=true";
    }
}
