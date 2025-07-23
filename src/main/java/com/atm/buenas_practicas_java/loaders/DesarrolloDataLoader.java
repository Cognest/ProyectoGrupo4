package com.atm.buenas_practicas_java.loaders;

import com.atm.buenas_practicas_java.entities.EntidadHija;
import com.atm.buenas_practicas_java.entities.EntidadPadre;
import com.atm.buenas_practicas_java.entities.UserRol;
import com.atm.buenas_practicas_java.entities.Usuario;
import com.atm.buenas_practicas_java.repositories.EntidadHijaRepository;
import com.atm.buenas_practicas_java.repositories.EntidadPadreRepository;
import com.atm.buenas_practicas_java.repositories.UsuarioRepo;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;

/**
* Clase de configuración que permite cargar datos iniciales en los repositorios
* de entidades para diferentes perfiles de configuración.
*
* Esta clase es útil para inicializar datos predefinidos utilizados durante el
* desarrollo o en entornos locales.
*
* Se utiliza la anotación @Configuration para indicar que es una clase de configuración
* de Spring, y métodos específicos anotados con @Profile para definir qué datos
* iniciales se cargarán según el perfil activo.
*/

@Configuration
@Log4j2
@Profile("desarrollo")
public class DesarrolloDataLoader {

private final EntidadPadreRepository repository;
private final EntidadHijaRepository entidadHijaRepository;
private UsuarioRepo usuarioRepo;
/**
 * Clase de configuración que permite cargar datos iniciales en los repositorios
 * de entidades para diferentes perfiles de configuración.
 *
 * Esta clase es útil para inicializar datos predefinidos utilizados durante el
 * desarrollo o en entornos específicos según el perfil.
 *
 * **Anotaciones utilizadas**:
 * - `@Configuration`: Define esta clase como una clase de configuración de Spring.
 *   Permite registrar beans en el contexto de la aplicación y gestionar configuraciones específicas.
 *
 * - `@Log4j2`: Habilita el uso de Log4j2 para registrar mensajes de log importantes,
 *   utilizados para monitoreo y depuración de la aplicación.
 *
 * Cada método anotado con `@Profile` y `@PostConstruct` permite la carga de datos
 * iniciales dependiendo del perfil activo.
 */
public DesarrolloDataLoader(EntidadPadreRepository repository, EntidadHijaRepository entidadHijaRepository) {
    this.repository = repository;
    this.entidadHijaRepository = entidadHijaRepository;
}

@PostConstruct
public void loadDataDesarrollo() {
    log.info("Iniciando la carga de datos para el perfil desarrollo");
    Usuario usuario1 = new Usuario();
    usuario1.setApellidos("Apellido 1");
    usuario1.setEmail("pepe@pepe.com");
    usuario1.setNickname("pepe");
    usuario1.setNombre("pepe");
    usuario1.setPassword("$2a$10$w5ewfdBtL87AMoMk7MGJm.qWma.swNh.oxQCiGkQalRGz7rebnLae");
    usuario1.setRol(UserRol.valueOf("USUARIO"));
    usuario1.setToken(57);
    usuario1.setAvatar("");

    usuarioRepo.save(usuario1);

    Usuario usuario2 = new Usuario();
    usuario2.setApellidos("Admin 1");
    usuario2.setEmail("admin@admin.com");
    usuario2.setNickname("admin");
    usuario2.setNombre("admin");
    usuario2.setPassword("$2a$10$w5ewfdBtL87AMoMk7MGJm.qWma.swNh.oxQCiGkQalRGz7rebnLae");
    usuario2.setRol(UserRol.valueOf("ADMIN"));
    usuario2.setToken(77);
    usuario2.setAvatar("");

    usuarioRepo.save(usuario2);
    log.info("Datos de entidades cargados correctamente.");

}



}
