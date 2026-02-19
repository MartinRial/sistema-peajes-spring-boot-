package obligatorio.da.sistemaPeajes.controladores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import excepciones.PeajeException;
import obligatorio.da.sistemaPeajes.dominio.EstadoPropietario;
import obligatorio.da.sistemaPeajes.dominio.Propietario;
import obligatorio.da.sistemaPeajes.dominio.Usuario;
import obligatorio.da.sistemaPeajes.dtos.EstadoPropietarioDTO;
import obligatorio.da.sistemaPeajes.dtos.PropietarioDTO;
import obligatorio.da.sistemaPeajes.servicios.Fachada;
import obligatorio.da.sistemaPeajes.utils.Respuesta;

/**
 * Controlador para gestionar el cambio de estado de propietarios.
 * Implementa el caso de uso de cambiar estado de propietario.
 */
@RestController
@RequestMapping("/usuarios/admins/estado-propietario")
@Scope("session") // Define el alcance de la sesión para este controlador
public class ControladorEstadoPropietario {

    private static final String USUARIO_ADMINISTRADOR_STATE_KEY = "usuarioAdministrador";
    private List<EstadoPropietario> estados;

    /**
     * Inicializa la vista con los estados disponibles.
     * 
     * Curso normal:
     * 1) El sistema muestra la lista de estados de propietario definidos en el sistema
     * 
     * @param usuario El administrador logueado
     * @return Lista de respuestas con los estados disponibles
     */
    @GetMapping("/vistaConectada")
    public List<Respuesta> inicializarVista(@SessionAttribute(name = USUARIO_ADMINISTRADOR_STATE_KEY, required = false) Usuario usuario) {
        if (usuario == null) {
            // Manejar el caso en que el usuario no está en la sesión - redireccionar a login
            return Respuesta.lista(new Respuesta("usuarioNoAutenticado", "login.html"));
        }
        return Respuesta.lista(estados());
    }

    /**
     * Obtiene y almacena en sesión la lista de estados disponibles.
     * 
     * @return Respuesta con la lista de estados en formato DTO
     */
    private Respuesta estados() {
        // Obtener estados desde la fachada
        estados = new ArrayList<>(Fachada.getInstancia().getEstadosDisponibles());
        
        // Convertir a DTOs
        List<EstadoPropietarioDTO> estadosDto = new ArrayList<>();
        for (EstadoPropietario estado : estados) {
            estadosDto.add(new EstadoPropietarioDTO(estado));
        }
        
        // Retornar respuesta
        return new Respuesta("estados", estadosDto);
    }

    /**
     * Endpoint para buscar un propietario por cédula.
     * 
     * Curso normal:
     * 2) El sistema muestra el nombre completo del propietario y su estado
     * 
     * Curso alternativo:
     * - No se encuentra un propietario con la cedula especificada. Mensaje "no existe el propietario"
     * 
     * @param cedula La cédula del propietario a buscar
     * @return Respuesta con los datos del propietario o error
     */
    @PostMapping("/buscarPropietario")
    public List<Respuesta> buscarPropietario(@RequestParam String cedula) {
        try {
            // Buscar propietario en la fachada
            Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCedula(cedula);
            
            // Convertir a DTO
            PropietarioDTO propietarioDto = new PropietarioDTO(propietario);
            
            // Retornar respuesta exitosa
            return Respuesta.lista(new Respuesta("propietario", propietarioDto));
        } catch (PeajeException e) {
            // Retornar error
            return Respuesta.lista(new Respuesta("error", e.getMessage()));
        }
    }

    /**
     * Endpoint para cambiar el estado de un propietario.
     * 
     * Curso normal:
     * 4) El usuario selecciona un nuevo estado de la lista e indica que desea cambiarlo
     * 5) El sistema cambia el estado del propietario y registra una notificación
     * 
     * Cursos alternativos:
     * - El estado seleccionado es igual al actual. Mensaje "El propietario ya esta en estado " + nombre del estado actual
     * 
     * @param cedula La cédula del propietario
     * @param posEstado La posición del estado en la lista
     * @return Respuesta con éxito o error
     */
    @PostMapping("/cambiarEstado")
    public List<Respuesta> cambiarEstado(
            @RequestParam String cedula,
            @RequestParam int posEstado) {
        try {
            // Validar que se especificó un estado (posición válida)
            if (posEstado < 0) {
                return Respuesta.lista(new Respuesta("error", "Debe especificar un estado"));
            }
            
            // Validar que la lista de estados existe en la sesión
            if (estados == null || estados.isEmpty()) {
                return Respuesta.lista(new Respuesta("error", "No hay estados disponibles"));
            }
            
            // Validar que la posición está dentro del rango
            if (posEstado >= estados.size()) {
                return Respuesta.lista(new Respuesta("error", "Debe especificar un estado"));
            }
            
            // Obtener el estado de la lista en sesión
            EstadoPropietario nuevoEstado = estados.get(posEstado);
            
            // Buscar el propietario
            Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCedula(cedula);
            
            // Cambiar estado con todas las validaciones y registrar notificación
            Fachada.getInstancia().cambiarEstadoPropietario(propietario, nuevoEstado);
            
            // Obtener el propietario actualizado para retornar sus datos
            PropietarioDTO propietarioDto = new PropietarioDTO(propietario);
            
            // Retornar respuesta exitosa con el propietario actualizado
            return Respuesta.lista(
                new Respuesta("exito", "Estado cambiado correctamente"),
                new Respuesta("propietario", propietarioDto)
            );
        } catch (PeajeException | IllegalArgumentException e) {
            // Retornar error
            return Respuesta.lista(new Respuesta("error", e.getMessage()));
        }
    }
}
