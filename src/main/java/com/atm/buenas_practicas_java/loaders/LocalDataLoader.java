package com.atm.buenas_practicas_java.loaders;

import com.atm.buenas_practicas_java.entities.UserRol;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;


/**
 * Clase de configuración que se utiliza exclusivamente para el perfil "default" en entornos locales.
 *
 * Esta clase está anotada con:
 * - {@code @Configuration}: Define esta clase como fuente de beans y configuración.
 * - {@code @Log4j2}: Habilita el uso de la biblioteca Log4j2 para registro de mensajes en los logs.
 * - {@code @Profile("default")}: Asegura que esta clase solo se cargue en el perfil "default".
 */

@Configuration
@Log4j2
@Profile("local")
public class LocalDataLoader {

    private UsuarioRepo usuarioRepo;

    /**
     * Constructor de la clase {@code LocalDataLoader}.
     *
     * Inicializa un objeto {@code LocalDataLoader} configurado con los repositorios de las entidades,
     * proporcionando la capacidad de interactuar con estas entidades en la base de datos.
     */
    public LocalDataLoader(UsuarioRepo usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }


    @PostConstruct
    public void loadDataLocal() {

        log.info("Iniciando la carga de datos para el perfil local");
        Usuario usuario1 = new Usuario();
        usuario1.setApellidos("Apellido 1");
        usuario1.setEmail("pepe@pepe.com");
        usuario1.setNickname("pepe");
        usuario1.setNombre("pepe");
        usuario1.setPassword("$2a$10$w5ewfdBtL87AMoMk7MGJm.qWma.swNh.oxQCiGkQalRGz7rebnLae");
        usuario1.setRol(UserRol.valueOf("USUARIO"));
        usuario1.setToken(57);
        usuario1.setAvatar("");
        if(!usuarioRepo.existsUsuarioByNickname(usuario1.getNickname())) {
            usuarioRepo.save(usuario1);
        }


        Usuario usuario2 = new Usuario();
        usuario2.setApellidos("Admin 1");
        usuario2.setEmail("admin@admin.com");
        usuario2.setNickname("admin");
        usuario2.setNombre("admin");
        usuario2.setPassword("$2a$10$w5ewfdBtL87AMoMk7MGJm.qWma.swNh.oxQCiGkQalRGz7rebnLae");
        usuario2.setRol(UserRol.valueOf("ADMIN"));
        usuario2.setToken(77);
        usuario2.setAvatar("");

        if(!usuarioRepo.existsUsuarioByNickname(usuario2.getNickname())) {
            usuarioRepo.save(usuario2);
        }
        log.info("Datos de entidades cargados correctamente.");
    }
}
