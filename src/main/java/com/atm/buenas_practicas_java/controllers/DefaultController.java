package com.atm.buenas_practicas_java.controllers;


import com.atm.buenas_practicas_java.entities.Prueba;
import com.atm.buenas_practicas_java.services.EntidadHijaService;
import com.atm.buenas_practicas_java.services.EntidadPadreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

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

    private final EntidadHijaService entidadHijaService;
    private final EntidadPadreService entidadPadreService;

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
    public String mostrarHome(Model model)
    {
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
        return "index"; // View name
    }

    @GetMapping("/iniciar-sesion")
    public String mostrarLogin(Model model)
    {
        return "login"; // View name
    }

    @GetMapping("/registrarse")
    public String mostrarSignin(Model model)
    {
        return "signin"; // View name
    }

    @GetMapping("/imagenes/{id}")
    public String mostrarImagen(@PathVariable("id") String id, ModelMap interfazConPantalla){
        System.out.println("Entro en servicios con id");
        Prueba prueba = new Prueba();
        prueba.setId(1);
        prueba.setNum_visitas(1232);
        prueba.setNum_descargas(145);
        prueba.setNota(6);

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

        interfazConPantalla.addAttribute("prueba", prueba);
        interfazConPantalla.addAttribute("imagen",id);
        interfazConPantalla.addAttribute("imagenes", imagenes);
        return "detallesContenido";
    }

    @GetMapping("/planes")
    public String mostrarPlanPrecios(Model model)
    {
        return "planPrecios"; // View name
    }

    @GetMapping("/subir-contenido")
    public String mostrarSubidaContenido(Model model)
    {
        return "subirContenido"; // View name
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
}
