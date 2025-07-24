package com.atm.buenas_practicas_java.controllers;

import com.atm.buenas_practicas_java.config.CustomUserDetails;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Controller
@RequiredArgsConstructor
public class AvatarController {

    private final UsuarioRepo usuarioRepo;

    @PostMapping("/usuario/cambiar-avatar")
    public String cambiarAvatar(@RequestParam("avatar") MultipartFile avatar,
                                @AuthenticationPrincipal CustomUserDetails usuarioAutenticado,
                                RedirectAttributes redirectAttributes) {
        try {
            log.info("🟢 Iniciando cambio de avatar. Usuario: {}", usuarioAutenticado.getUsername());

            String filename = avatar.getOriginalFilename();
            if (filename == null || !filename.matches(".*\\.(jpg|jpeg|png|gif)$")) {
                log.warn("❌ Archivo no válido: {}", filename);
                redirectAttributes.addFlashAttribute("error", "Solo se permiten imágenes jpg, jpeg, png o gif.");
                return "redirect:/config-perfil";
            }

            String nickname = usuarioAutenticado.getUsername();
            String basePath = "/uploads/perfiles/" + nickname;

            File directory = new File(basePath);
            if (!directory.exists()) {
                log.info("📁 Creando directorio: {}", basePath);
                if (!directory.mkdirs()) {
                    log.error("❌ No se pudo crear la carpeta: {}", basePath);
                    redirectAttributes.addFlashAttribute("error", "No se pudo crear la carpeta de destino.");
                    return "redirect:/config-perfil";
                }
            }

            // Nombre fijo para evitar acumulación
            String nombreAvatar = "avatar.jpg";
            Path rutaAvatar = Paths.get(basePath, nombreAvatar);
            Files.write(rutaAvatar, avatar.getBytes());
            log.info("✅ Avatar guardado en: {}", rutaAvatar.toAbsolutePath());

            // Generar URL accesible
            String rutaWeb = rutaAvatar.toString().replace("\\", "/");
            String relativeUrl = rutaWeb.substring(rutaWeb.indexOf("/uploads/") + "/uploads".length());
            String avatarWebUrl = "/archivos" + relativeUrl;
            log.info("🌐 URL del avatar accesible: {}", avatarWebUrl);

            // Guardar en base de datos
            Usuario usuario = usuarioRepo.findByNickname(nickname).orElseThrow();
            usuario.setAvatar(avatarWebUrl);
            usuarioRepo.save(usuario);
            log.info("📝 Avatar actualizado en BD para '{}'", nickname);

            redirectAttributes.addFlashAttribute("success", "Foto de perfil actualizada correctamente.");

        } catch (Exception e) {
            log.error("❌ Error subiendo avatar", e);
            redirectAttributes.addFlashAttribute("error", "Ocurrió un error al subir la imagen.");
        }

        return "redirect:/config-perfil";
    }
}
