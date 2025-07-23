package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.config.CustomUserDetails;
import com.atm.buenas_practicas_java.dtos.ContenidoDto;
import com.atm.buenas_practicas_java.dtos.UsuarioRegistroDto;
import com.atm.buenas_practicas_java.entities.Contenido;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import com.atm.buenas_practicas_java.services.ContenidoService;
import com.atm.buenas_practicas_java.services.EtiquetaContenidoService;
import com.atm.buenas_practicas_java.services.UsuarioContenidoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Log4j2
@Controller
public class SubidaController {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private UsuarioContenidoService usuarioContenidoService;

    @Autowired
    private EtiquetaContenidoService etiquetaContenidoService;

    private final ContenidoService contenidoService;
    public SubidaController(ContenidoService contenidoService) {
        this.contenidoService = contenidoService;
    }

    @GetMapping("/subir-contenido")
    public String mostrarSubidaContenido(Model model) {
        log.info("Mostrando formulario para subir contenido.");
        model.addAttribute("contenido", new ContenidoDto());
        return "subirContenido";
    }

    @PostMapping("/subir-contenido")
    public String subirContenido(@RequestParam("file") MultipartFile file,
                                 @RequestParam("tipo") String tipo,
                                 @RequestParam("etiquetas") String etiquetas,
                                 @RequestParam(value = "portada", required = false) MultipartFile portada,
                                 @ModelAttribute("contenido") ContenidoDto contenidoDto,
                                 @AuthenticationPrincipal CustomUserDetails usuarioAutenticado,
                                 RedirectAttributes redirectAttributes) {
        try {
            log.info("Iniciando proceso de subida de contenido. Usuario: {}", usuarioAutenticado.getUsername());

            String filename = file.getOriginalFilename();
            if (filename == null || !esExtensionPermitida(filename, tipo)) {
                log.warn("Archivo con extensión no permitida: {} para tipo {}", filename, tipo);
                redirectAttributes.addFlashAttribute("error", "Tipo de archivo no permitido para el tipo seleccionado.");
                return "redirect:/subir-contenido";
            }

            String nickname = usuarioAutenticado.getUsername();
            String basePath = "uploads/" + tipo + "/" + nickname;
            File directory = new File(basePath);
            if (!directory.exists()) {
                log.info("Creando directorio: {}", basePath);
                if (directory.mkdirs()) {
                    log.info("Directorio creado: {}", basePath);
                }
                else {
                    log.error("Error creando directorio: {}", basePath);
                }
            }

            Path filepath = Paths.get(basePath, filename.replaceAll("\\s+", "_"));
            log.info("Guardando archivo en ruta: {}", filepath);
            Files.write(filepath, file.getBytes());

            String portadaUrl = null;
            if (portada != null && !portada.isEmpty()) {
                String portadaFilename = portada.getOriginalFilename().replaceAll("\\s+", "_");
                String portadaExtension = portadaFilename.substring(portadaFilename.lastIndexOf('.') + 1).toLowerCase();

                if (!List.of("jpg", "jpeg", "png", "gif").contains(portadaExtension)) {
                    log.warn("Portada con extensión no válida: {}", portadaExtension);
                    redirectAttributes.addFlashAttribute("error", "La portada debe ser una imagen válida.");
                    return "redirect:/subir-contenido";
                }

                Path portadaPath = Paths.get(basePath, "portadas");
                if (!Files.exists(portadaPath)) {
                    log.info("Creando directorio para portada: {}", portadaPath);
                    Files.createDirectories(portadaPath);
                }

                Path portadaFullPath = portadaPath.resolve(portadaFilename);
                log.info("Guardando portada en: {}", portadaFullPath);
                Files.write(portadaFullPath, portada.getBytes());

                String portadaStr = portadaFullPath.toString().replace("\\", "/");
                String relativePortada = portadaStr.substring(portadaStr.indexOf("/uploads/") + "/uploads".length());
                portadaUrl = "/archivos" + relativePortada;
            }

            String url = filepath.toString().replace("\\", "/");
            String relativeUrl = url.substring(url.indexOf("/uploads/") + "/uploads".length());
            String webUrl = "/archivos" + relativeUrl;

            log.info("URL accesible del contenido: {}", webUrl);

            Contenido contenido = contenidoService.subirContenido(contenidoDto, tipo, webUrl, nickname, portadaUrl);
            log.info("Contenido guardado en base de datos con ID: {}", contenido.getId());

            Usuario usuario = usuarioRepo.findByNickname(nickname)
                    .orElseThrow(() -> {
                        log.error("Usuario no encontrado: {}", nickname);
                        return new RuntimeException("Usuario no encontrado");
                    });

            usuarioContenidoService.vincularUsuarioContenido(usuario, contenido, "Creador");
            log.info("Usuario {} vinculado como Creador del contenido ID {}", nickname, contenido.getId());

            etiquetaContenidoService.vincularEtiquetaContenido(contenido, etiquetas);
            log.info("Etiquetas '{}' vinculadas al contenido ID {}", etiquetas, contenido.getId());

            redirectAttributes.addFlashAttribute("success", "Archivo subido exitosamente.");
        } catch (Exception e) {
            log.error("Error al subir el contenido: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("error", "Error al subir el archivo: " + e.getMessage());
        }

        return "redirect:/subir-contenido";
    }

    private boolean esExtensionPermitida(String filename, String tipo) {
        if (filename == null || !filename.contains(".")) return false;

        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        tipo = tipo.toLowerCase().trim();

        log.debug("Validando extensión del archivo: {} | tipo: {} | extensión: {}", filename, tipo, extension);

        return switch (tipo) {
            case "imagenes" -> List.of("jpg", "jpeg", "png", "gif").contains(extension);
            case "videos" -> List.of("mp4", "mov").contains(extension);
            case "audios" -> List.of("mp3", "wav").contains(extension);
            case "modelos3d", "modelos", "3d" -> List.of("glb").contains(extension);
            default -> false;
        };
    }
}
