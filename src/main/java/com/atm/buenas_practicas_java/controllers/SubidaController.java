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

@Controller
public class SubidaController {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private UsuarioContenidoService usuarioContenidoService;

    @Autowired
    private EtiquetaContenidoService etiquetaContenidoService;

    private final ContenidoService contenidoService;
    public SubidaController(ContenidoService contenidoService) {this.contenidoService = contenidoService;}

    @GetMapping("/subir-contenido")
    public String mostrarSubidaContenido(Model model)
    {
        model.addAttribute("contenido", new ContenidoDto());
        return "subirContenido"; // View name
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
            String filename = file.getOriginalFilename();
            if (filename == null || !esExtensionPermitida(filename, tipo)) {
                redirectAttributes.addFlashAttribute("error", "Tipo de archivo no permitido para el tipo seleccionado.");
                return "redirect:/subir-contenido";
            }

            // Asegura que el directorio exista
            String nickname = usuarioAutenticado.getUsername();
            String basePath = "/uploads/" + tipo + "/" + nickname;
            File directory = new File(basePath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Guarda el archivo
            Path filepath = Paths.get(basePath, filename.replaceAll("\\s+", "_"));
            Files.write(filepath, file.getBytes());

            //Guardar portada
            String portadaUrl = null;

            if (portada != null && !portada.isEmpty()) {
                String portadaFilename = portada.getOriginalFilename().replaceAll("\\s+", "_");

                // Validación de extensión de portada
                String portadaExtension = portadaFilename.substring(portadaFilename.lastIndexOf('.') + 1).toLowerCase();
                if (!List.of("jpg", "jpeg", "png", "gif").contains(portadaExtension)) {
                    redirectAttributes.addFlashAttribute("error", "La portada debe ser una imagen válida.");
                    return "redirect:/subir-contenido";
                }

                // Guardar portada en carpeta separada pero dentro del mismo tipo/nickname
                Path portadaPath = Paths.get(basePath, "portadas");
                if (!Files.exists(portadaPath)) {
                    Files.createDirectories(portadaPath);
                }

                Path portadaFullPath = portadaPath.resolve(portadaFilename);
                Files.write(portadaFullPath, portada.getBytes());

                String portadaStr = portadaFullPath.toString().replace("\\", "/");
                String relativePortada = portadaStr.substring(portadaStr.indexOf("/uploads/") + "/uploads".length());
                portadaUrl = "/archivos" + relativePortada;
            }

            //Guardar en contenido
            // 1. Convertir a URLs accesibles desde navegador
            String url = filepath.toString().replace("\\", "/");
            System.out.println("Remplazando las barras: " + url);
            String relativeUrl = url.substring(url.indexOf("/uploads/") + "/uploads".length());
            System.out.println("Quitando uploads: " + relativeUrl);
            String webUrl = "/archivos" + relativeUrl;
            System.out.println("Añadiendo archivos: " + webUrl);

            // 2. Llama al servicio usando rutas web
            Contenido contenido = contenidoService.subirContenido(contenidoDto, tipo, webUrl, nickname, portadaUrl);

            // Buscar el usuario
            Usuario usuario = usuarioRepo.findByNickname(nickname)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            String tipoRelacion = "Creador";
            Boolean guardado = false;

            usuarioContenidoService.vincularUsuarioContenido(usuario, contenido, tipoRelacion, guardado);

            // Crear etiquetas y vincular al contenido
            etiquetaContenidoService.vincularEtiquetaContenido(contenido, etiquetas);

            redirectAttributes.addFlashAttribute("success", "Archivo subido exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al subir el archivo: " + e.getMessage());
        }

        return "redirect:/subir-contenido"; // Volvemos a la página del formulario
    }

    private boolean esExtensionPermitida(String filename, String tipo) {
        if (filename == null || !filename.contains(".")) return false;

        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        tipo = tipo.toLowerCase().trim();

        System.out.println("🧪 Validando archivo: " + filename + " | tipo: " + tipo + " | extensión: " + extension);

        return switch (tipo) {
            case "imagenes" -> List.of("jpg", "jpeg", "png", "gif").contains(extension);
            case "videos" -> List.of("mp4", "mov").contains(extension);
            case "audios" -> List.of("mp3", "wav").contains(extension);
            case "modelos3d", "modelos", "3d" -> List.of("glb").contains(extension);
            default -> false;
        };
    }
}
