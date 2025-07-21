package com.atm.buenas_practicas_java.controllers;


import com.atm.buenas_practicas_java.config.CustomUserDetails;
import com.atm.buenas_practicas_java.dtos.ContenidoDto;
import com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO;
import com.atm.buenas_practicas_java.entities.*;
import com.atm.buenas_practicas_java.repositories.*;
import com.atm.buenas_practicas_java.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.File;
import java.io.IOException;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
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
 * Dependencias:
 * - {@code EntidadPadreRepository}: Interfaz del repositorio que permite interactuar con
 *   la base de datos para operaciones de persistencia y consulta relacionadas con
 *   la entidad padre.
 *
 * Métodos principales:
 * - {@code listEntities}: Maneja solicitudes GET a la URL "/entities", recupera los
 *   datos de las entidades desde la base de datos y los pasa al modelo para mostrarlos
 *   en una vista.
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
    private DescargaRepo descargaRepo;

    @Autowired
    private ComentarioRepo comentarioRepo;

    @Autowired
    private EtiquetaContenidoRepo etiquetaContenidoRepo;

    @Autowired
    private LikeService likeService;

    @Autowired
    private GuardadoService guardadoService;

    @Autowired
    private GuardadoRepo guardadoRepo;

    @Autowired
    private UsuarioContenidoRepo usuarioContenidoRepo;

    private final EntidadHijaService entidadHijaService;
    private final EntidadPadreService entidadPadreService;
    private final List<Map<String, Object>> planes = List.of(
            Map.of("id", 1, "nombre", "Principiante", "precio", 9.99, "tokens", 1000),
            Map.of("id", 2, "nombre", "Más vendido", "precio", 24.99, "tokens", 2800),
            Map.of("id", 3, "nombre", "Avanzado", "precio", 39.99, "tokens", 5000),
            Map.of("id", 4, "nombre", "Experto", "precio", 99.99, "tokens", 13500)
    );
    @Autowired
    private CarteraRepo carteraRepo;

    /**
     * Constructor de la clase DefaultController.
     * <p>
     * Inicializa el controlador principal asignando los servicios
     * utilizados para gestionar las entidades EntidadPadre y EntidadHija.
     *
     * @param entidadHijaService  instancia de {@link EntidadHijaService} que proporciona
     *                            funcionalidades adicionales relacionadas con la entidad EntidadHija.
     * @param entidadPadreService instancia de {@link EntidadPadreService} que proporciona
     *                            funcionalidades adicionales relacionadas con la entidad EntidadPadre.
     */
    public DefaultController(EntidadHijaService entidadHijaService, EntidadPadreService entidadPadreService) {
        this.entidadHijaService = entidadHijaService;
        this.entidadPadreService = entidadPadreService;
    }

    /**
     * Método que lista las entidades disponibles y las añade al modelo para ser utilizadas en la vista.
     * Recupera todas las entidades de un repositorio y las presenta en una vista específica.
     *
     * @param model El objeto del modelo que se utiliza para compartir datos entre el backend y la vista.
     *              Aquí se añade un atributo llamado "entities" con la lista obtenida del repositorio.
     * @return Una cadena que representa el nombre de la vista ("entitiesList") donde se renderizarán las entidades.
     */
    @GetMapping("/entities")
    public String listEntities(Model model)
    {
        model.addAttribute("entidades", entidadHijaService.findAll());
        return "entidadesHijas"; // View name
    }

    /**
     * Gestiona las solicitudes GET para obtener y mostrar la lista de entidades protegidas.
     * Añade las entidades obtenidas del repositorio al modelo para renderizarlas en la vista correspondiente.
     *
     * @param model Objeto {@link Model} que se utiliza para pasar datos desde el controlador a la vista.
     *              Contendrá la lista de entidades recuperadas desde el repositorio.
     * @return El nombre de la vista "entitiesList" donde se mostrará la lista de entidades.
     */
    @GetMapping("/protected")
    public String protectedList(Model model)
    {
        model.addAttribute("entidades", entidadPadreService.findAll());
        return "entidadesPadre"; // View name
    }

    /**
     * Deletes an EntidadHija entity by its ID using the EntidadHijaService.
     *
     * @param id The ID of the EntidadHija to delete.
     * @return A redirect to the "/protected" endpoint after deletion.
     */
    @PostMapping("/entidades/deleteHija/{id}")
    public String deleteEntidadHija(@PathVariable Long id) {
        entidadHijaService.deleteById(id);
        return "redirect:/entities";
    }



    /**
     * Deletes an EntidadHija entity by its ID using the EntidadHijaService.
     *
     * @param id The ID of the EntidadHija to delete.
     * @return A redirect to the "/protected" endpoint after deletion.
     */
    @PostMapping("/entidades/deletePadre/{id}")
    public String deleteEntidadPadre(@PathVariable Long id) {
        entidadPadreService.deleteById(id);
        return "redirect:/entities";
    }

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


    @GetMapping("/config-usuario")
    public String configUsuario(Model model)
    {
        return "configUsuario"; // View name
    }


    @GetMapping("/usuario/{nickname}")
    public String vistaUsuario(@PathVariable String nickname, Model model, Principal principal) {
        Usuario autor = usuarioRepo.findByNickname(nickname).get();

        List<ContenidoSubidoDTO> galeria = usuarioContenidoRepo.findContenidosSubidosPorUsuario(autor);
        model.addAttribute("galeria", galeria);

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

        model.addAttribute("usuario", autor);
        return "vistaUsuario"; // nombre del HTML
    }
    @GetMapping("/reportes")
    public String reportes(Model model)
    {
        return "reportes"; // View name
    }

    @GetMapping("/chat")
    public String pantallaChat(Model model) {
        return "chat";
    }

    @GetMapping("/guardados")
    public String pantallaGuardados(Model model) {
        List<String> imagenes = List.of(
                "https://mdbcdn.b-cdn.net/img/Photos/Horizontal/Nature/4-col/img%20(73).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Vertical/mountain1.webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Vertical/mountain2.webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Horizontal/Nature/4-col/img%20(73).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Horizontal/Nature/4-col/img%20(18).webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Vertical/mountain3.webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Vertical/mountain2.webp",
                "https://mdbcdn.b-cdn.net/img/Photos/Horizontal/Nature/4-col/img%20(73).webp"
        );

        model.addAttribute("imagenes", imagenes);
        return "guardados";
    }

    @GetMapping("/guardadosPrueba")
    public String pantallaGuardadosPrueba(Model model) {
        return "guardadosPrueba";
    }

    @GetMapping("/modelosPrueba")
    public String pantallaModelosPrueba(Model model) {
        return "modelosPrueba";
    }

    @GetMapping("/contenido/{nickname}/{id}")
    public String mostrarContenido(@PathVariable String nickname, @PathVariable("id") Integer id, Model interfazConPantalla, Principal principal){
        System.out.println("Entro en servicios con id");
        Prueba prueba = new Prueba();
        prueba.setId(1);
        prueba.setNum_visitas(1232);
        prueba.setNum_descargas(145);
        prueba.setNota(6);

        Contenido contenido = contenidoRepo.findById(id).get();
        Usuario autor = usuarioRepo.findByNickname(nickname).get(); // Este es el dueño del contenido

        // Likes
        Integer numLikes = likeRepo.countByContenidoId(id);
        // Descargas
        Integer numDescargas = descargaRepo.countByUsuarioContenidoContenidoId(id);
        // Comentarios
        List<Comentario> comentarios = comentarioRepo.findByContenidoIdOrderByFechaAsc(id);
        // Etiquetas
        List<EtiquetaContenido> etiquetaContenidos = etiquetaContenidoRepo.findByContenidoId(id);
        List<Etiqueta> etiquetas = etiquetaContenidos.stream()
                .map(EtiquetaContenido::getEtiqueta)
                .collect(Collectors.toList());

        interfazConPantalla.addAttribute("prueba", prueba);
        interfazConPantalla.addAttribute("contenido", contenido);
        interfazConPantalla.addAttribute("likes", numLikes);
        interfazConPantalla.addAttribute("descargas", numDescargas);
        interfazConPantalla.addAttribute("comentarios", comentarios);
        interfazConPantalla.addAttribute("etiquetas", etiquetas);
        interfazConPantalla.addAttribute("usuario", autor); // Se sigue usando como "autor del contenido"

        List<ContenidoSubidoDTO> galeria = contenidoRepo.findAllOrderByLikesExcludingId(id);
        interfazConPantalla.addAttribute("galeria", galeria);

        if (principal != null) {
            Usuario usuarioLogueado = usuarioRepo.findByNickname(principal.getName()).get();
            boolean haDadoLike = likeService.usuarioHaDadoLike(contenido, usuarioLogueado);
            boolean estaGuardado = guardadoService.estaGuardadoPorUsuario(contenido, usuarioLogueado);
            UsuarioContenido usuarioContenido = usuarioContenidoRepo.findByContenidoAndUsuario(contenido, usuarioLogueado);

            String relacion = null;

            if (usuarioContenido != null) {
                relacion = usuarioContenido.getTipo();
            }

            // IDs de contenido con like o guardado
            Set<Integer> likesIds = likeRepo.findByUsuario(usuarioLogueado).stream()
                    .map(like -> like.getContenido().getId())
                    .collect(Collectors.toSet());

            Set<Integer> guardadosIds = guardadoRepo.findByUsuario(usuarioLogueado).stream()
                    .map(guardado -> guardado.getContenido().getId())
                    .collect(Collectors.toSet());

            interfazConPantalla.addAttribute("likesIds", likesIds);
            interfazConPantalla.addAttribute("guardadosIds", guardadosIds);
            interfazConPantalla.addAttribute("estaGuardado", estaGuardado);
            interfazConPantalla.addAttribute("haDadoLike", haDadoLike);
            interfazConPantalla.addAttribute("usuarioLogueado", usuarioLogueado);
            interfazConPantalla.addAttribute("relacion", relacion);
        } else {
            interfazConPantalla.addAttribute("haDadoLike", false);
            interfazConPantalla.addAttribute("estaGuardado", false);
            interfazConPantalla.addAttribute("likesIds", Set.of());
            interfazConPantalla.addAttribute("guardadosIds", Set.of());
        }

        return "detallesContenido";
    }

    @PostMapping("/comentarios")
    @ResponseBody
    public ResponseEntity<?> guardarComentario(@RequestParam Integer contenidoId,
                                               @RequestParam String mensaje,
                                               Principal principal) {

        if (mensaje == null || mensaje.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El comentario no puede estar vacío.");
        }

        Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get(); // o por username
        Contenido contenido = contenidoRepo.findById(contenidoId).orElse(null);

        Comentario comentario = new Comentario();
        comentario.setUsuario(usuario);
        comentario.setContenido(contenido);
        comentario.setMensaje(mensaje);
        comentario.setFecha(LocalDateTime.now());

        comentarioRepo.save(comentario);

        // Respuesta parcial con datos útiles
        Map<String, Object> result = new HashMap<>();
        result.put("nickname", usuario.getNickname());
        result.put("avatar", usuario.getAvatar());
        result.put("mensaje", mensaje);
        result.put("fecha", LocalDateTime.now().toString());

        return ResponseEntity.ok(result);
    }

    @PostMapping("/like")
    @ResponseBody
    public ResponseEntity<?> likeContenido(@RequestParam Integer contenidoId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();
        boolean nuevoLike = likeService.toggleLike(contenidoId, usuario);

        int totalLikes = likeService.countLikes(contenidoId);

        return ResponseEntity.ok(Map.of(
                "liked", nuevoLike,
                "totalLikes", totalLikes
        ));
    }

    @PostMapping("/guardar")
    @ResponseBody
    public ResponseEntity<?> guardarContenido(@RequestParam Integer contenidoId, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();
        Contenido contenido = contenidoRepo.findById(contenidoId).orElse(null);
        boolean guardado = guardadoService.toggleGuardar(contenido, usuario);

        return ResponseEntity.ok(Map.of("guardado", guardado));
    }


    @GetMapping("/planes")
    public String mostrarPlanPrecios(Model model)
    {
        model.addAttribute("planes", planes);
        return "planPrecios"; // View name
    }

    @GetMapping("/pagar")
    public String formularioPago(@RequestParam int planId, Model model) {
        Map<String, Object> plan = planes.stream()
                .filter(p -> (int) p.get("id") == planId)
                .findFirst()
                .orElse(null);

        if (plan == null) return "redirect:/planes";

        model.addAttribute("plan", plan);
        return "formularioPago";
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
            return "formularioPago";
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
        return "pagoExitoso";
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

    @GetMapping("/usuarios-bloqueados")
    public String mostrarUsuariosBloqueados(Model model)
    {
        List<String> imagenes = List.of(
                "https://randomuser.me/api/portraits/men/99.jpg",
                "https://randomuser.me/api/portraits/men/1.jpg",
                "https://randomuser.me/api/portraits/women/2.jpg",
                "https://randomuser.me/api/portraits/women/99.jpg",
                "https://randomuser.me/api/portraits/women/1.jpg",
                "https://randomuser.me/api/portraits/men/2.jpg"
        );

        model.addAttribute("imagenes", imagenes);
        return "usuariosBloqueados"; // View name
    }
    @GetMapping("/actividad")
    public String tuActividad(Model model) {
        return "tuActividad"; // View name
    }
    @GetMapping("/config-perfil")
    public String configPerfil(Model model)
    {
        return "configPerfil"; // View name
    }

    @GetMapping("/conocenos")
    public String conocenos(Model model)
    {
        return "conocenos"; // View name
    }

    @GetMapping("/cartera")
    public String pantallaCartera(Model model, Principal principal) {

        Usuario usuario = usuarioRepo.findByNickname(principal.getName()).get();
        List<Cartera> carteras = carteraRepo.findAllByUsuarioOrderByFechaDesc(usuario);

        model.addAttribute("carteras", carteras);
        model.addAttribute("usuario", usuario);

        return "cartera";
    }

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

        return "carritoCompra";
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

    @GetMapping("/contenido/{id}/descargar")
    public ResponseEntity<Resource> descargarContenido(@PathVariable Integer id, Principal principal) throws IOException {
        // Obtener usuario logueado
        Usuario usuario = usuarioRepo.findByNickname(principal.getName()).orElse(null);
        Contenido contenido = contenidoRepo.findById(id).orElse(null);

        if (usuario == null || contenido == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Comprobar si el usuario es creador o adquirente
        UsuarioContenido relacion = usuarioContenidoRepo.findByContenidoAndUsuario(contenido, usuario);
        if (relacion == null || (!relacion.getTipo().equals("Adquirido") && !relacion.getTipo().equals("Creador"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        // Obtener ruta del archivo desde contenido.getUrl()
        Path path = Paths.get("C:" + contenido.getUrl().replaceFirst("/archivos/", "/uploads/"));
        System.out.println("C:" + contenido.getUrl().replaceFirst("/archivos/", "/uploads/"));

        if (!Files.exists(path)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
