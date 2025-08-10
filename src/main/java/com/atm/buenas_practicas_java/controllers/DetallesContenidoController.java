package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.dtos.ContenidoSubidoDTO;
import com.atm.buenas_practicas_java.entities.*;
import com.atm.buenas_practicas_java.repositories.*;
import com.atm.buenas_practicas_java.services.GuardadoService;
import com.atm.buenas_practicas_java.services.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class DetallesContenidoController {

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
    private UsuarioContenidoRepo usuarioContenidoRepo;

    @Autowired
    private GuardadoRepo guardadoRepo;

    @GetMapping("/contenido/{nickname}/{id}")
    public String mostrarContenido(@PathVariable String nickname, @PathVariable("id") Integer id, Model interfazConPantalla, Principal principal){
        System.out.println("Entro en servicios con id");

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

        //Sumar visita
        contenido.setVisitas(contenido.getVisitas() + 1);
        contenidoRepo.save(contenido);

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

        return "detallesContenido/detallesContenido";
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

        Descarga descarga = new Descarga();
        descarga.setFecha(LocalDate.now());
        descarga.setUsuarioContenido(relacion);
        descargaRepo.save(descarga);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + path.getFileName() + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
