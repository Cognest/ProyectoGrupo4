package com.atm.buenas_practicas_java.services;

import com.atm.buenas_practicas_java.PostgreSQLContainerTest;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * Clase de prueba para la lógica de negocio correspondiente a la entidad "EntidadPadre".
 *
 * <p>Esta clase contiene casos de prueba que verifican el correcto funcionamiento
 * de los métodos y servicios asociados a la entidad "EntidadPadre". Utiliza un
 * entorno controlado para garantizar la exactitud de los resultados esperados.</p>
 *
 * <p>Se hacen uso de utilidades y dependencias, tales como frameworks para
 * pruebas unitarias, anotaciones para inyección de dependencias, entre otros,
 * para facilitar la implementación y ejecución de los tests.</p>
 *
 * <p>Los métodos de prueba de esta clase validan el comportamiento en diferentes
 * escenarios, incluyendo casos de éxito, fallos y excepciones esperadas.</p>
 *
 */
@Log4j2
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EntidadPadreServiceTest extends PostgreSQLContainerTest {


}
