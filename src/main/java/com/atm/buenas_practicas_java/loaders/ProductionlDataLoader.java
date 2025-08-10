package com.atm.buenas_practicas_java.loaders;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
@Profile("produccion")
public class ProductionlDataLoader {

    public ProductionlDataLoader() {}

    @Profile("produccionRESETDATA")
    public void loadDataProduccion() {
        log.info("Iniciando la carga de datos para el perfil de producción.");
        log.info("Datos de producción aún no definidos. Este método requiere implementación adicional.");
        log.info("Datos de entidades borrados correctamente.");
    }



}
