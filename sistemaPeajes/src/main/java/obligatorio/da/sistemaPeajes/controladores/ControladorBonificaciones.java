package obligatorio.da.sistemaPeajes.controladores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import excepciones.PeajeException;
import obligatorio.da.sistemaPeajes.dominio.EstrategiaBonificacion;
import obligatorio.da.sistemaPeajes.dominio.Propietario;
import obligatorio.da.sistemaPeajes.dominio.Puesto;
import obligatorio.da.sistemaPeajes.dominio.Usuario;
import obligatorio.da.sistemaPeajes.dtos.BonificacionSimpleDTO;
import obligatorio.da.sistemaPeajes.dtos.PropietarioDTO;
import obligatorio.da.sistemaPeajes.dtos.PuestoDTO;
import obligatorio.da.sistemaPeajes.servicios.Fachada;
import obligatorio.da.sistemaPeajes.utils.ConexionNavegador;
import obligatorio.da.sistemaPeajes.utils.Respuesta;
import observador.Observable;
import observador.Observador;

@RestController
@RequestMapping("/usuarios/admins/bonificaciones")
@Scope("session") // Define el alcance de la sesión para este controlador
public class ControladorBonificaciones implements Observador {

    private static final String USUARIO_ADMINISTRADOR_STATE_KEY = "usuarioAdministrador";
    private List<Puesto> puestos;
    private List<EstrategiaBonificacion> bonificaciones;
    private Propietario propietarioActual; // El propietario que está siendo observado
    
    @Autowired
    private ConexionNavegador conexionNavegador;

    /**
     * Endpoint para establecer la conexión SSE (Server-Sent Events).
     * Permite recibir actualizaciones en tiempo real del propietario.
     * 
     * @return El objeto SseEmitter para la conexión
     */
    @GetMapping(value = "/registrarSSE", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter conectarSSE() {
        conexionNavegador.conectarSSE();
        return conexionNavegador.getConexionSSE();
    }

    @GetMapping("/vistaConectada")
    public List<Respuesta> inicializarVista(@SessionAttribute(name = USUARIO_ADMINISTRADOR_STATE_KEY, required = false) Usuario usuario) {
        if (usuario == null) {
            // Manejar el caso en que el usuario no está en la sesión pide redireccionar a
            // la página de login
            return Respuesta.lista(new Respuesta("usuarioNoAutenticado", "login.html"));
        }
        return Respuesta.lista(bonificaciones(), puestos());
    }
    
    /**
     * Endpoint para cerrar la vista y limpiar los observadores.
     */
    @PostMapping("/vistaCerrada")
    public void salir() {
        desuscribirObservadorDePropietario();
    }

    private Respuesta puestos() {
        puestos = new ArrayList<>(Fachada.getInstancia().getPuestos());
        List<PuestoDTO> puestosDto = new ArrayList<>();

        for (Puesto puesto : puestos) {
            puestosDto.add(new PuestoDTO(puesto));
        }
        return new Respuesta("puestos", puestosDto);
    }
    
    /**
     * Endpoint para obtener el listado de bonificaciones disponibles.
     * 
     * @return Lista de respuestas con las bonificaciones en formato DTO
     */
    private Respuesta bonificaciones() {
        // Obtener bonificaciones desde la fachada
        bonificaciones = Fachada.getInstancia().getBonificaciones();
        
        // Convertir a DTOs
        List<BonificacionSimpleDTO> bonificacionesDto = new ArrayList<>();
        for (EstrategiaBonificacion bonificacion : bonificaciones) {
            bonificacionesDto.add(new BonificacionSimpleDTO(bonificacion));
        }
        
        // Retornar respuesta
        return new Respuesta("bonificaciones", bonificacionesDto);
    }

    /**
     * Endpoint para buscar un propietario por cédula.
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
            // Desuscribirse del propietario anterior si existe
            desuscribirObservadorDePropietario();
            
            // Buscar propietario en la fachada
            Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCedula(cedula);
            
            // Suscribirse al nuevo propietario para recibir actualizaciones
            propietarioActual = propietario;
            propietario.agregarObservador(this);
            
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
     * Endpoint para asignar una bonificación a un propietario.
     * 
     * Curso normal:
     * 5) El administrador selecciona una bonificación de la lista de bonificaciones
     *    definidas, selecciona un puesto, e indica que desea asignar la bonificación al propietario.
     * 6) El sistema asigna la bonificación al propietario para el puesto seleccionado y registra la fecha
     *    y hora de asignada.
     * 
     * Cursos alternativos:
     * - No hay una bonificación seleccionada. Mensaje "Debe especificar una bonificación"
     * - No hay un puesto seleccionado. Mensaje "Debe especificar un puesto"
     * - El propietario ya tiene una bonificación para el puesto seleccionado. 
     *   Mensaje "Ya tiene una bonificación asignada para ese puesto".
     * - El propietario está deshabilitado. 
     *   Mensaje "El propietario esta deshabilitado. No se pueden asignar bonificaciones".
     * 
     * @param cedula La cédula del propietario
     * @param posBonificacion La posición de la bonificación en la lista
     * @param posPuesto La posición del puesto en la lista
     * @return Respuesta con éxito o error
     */
    @PostMapping("/asignarBonificacion")
    public List<Respuesta> asignarBonificacion(
            @RequestParam String cedula,
            @RequestParam int posBonificacion,
            @RequestParam int posPuesto) {
        try {
            // Validar que se especificó una bonificación (posición válida)
            if (posBonificacion < 0) {
                return Respuesta.lista(new Respuesta("error", "Debe especificar una bonificación"));
            }
            
            // Validar que se especificó un puesto (posición válida)
            if (posPuesto < 0) {
                return Respuesta.lista(new Respuesta("error", "Debe especificar un puesto"));
            }
            
            // Validar que las listas de bonificaciones y puestos existen en la sesión
            if (bonificaciones == null || bonificaciones.isEmpty()) {
                return Respuesta.lista(new Respuesta("error", "No hay bonificaciones disponibles"));
            }
            
            if (puestos == null || puestos.isEmpty()) {
                return Respuesta.lista(new Respuesta("error", "No hay puestos disponibles"));
            }
            
            // Validar que las posiciones están dentro del rango
            if (posBonificacion >= bonificaciones.size()) {
                return Respuesta.lista(new Respuesta("error", "Debe especificar una bonificación"));
            }
            
            if (posPuesto >= puestos.size()) {
                return Respuesta.lista(new Respuesta("error", "Debe especificar un puesto"));
            }
            
            // Obtener la bonificación y el puesto de las listas en sesión
            EstrategiaBonificacion bonificacion = bonificaciones.get(posBonificacion);
            Puesto puesto = puestos.get(posPuesto);
            
            // Buscar el propietario
            Propietario propietario = Fachada.getInstancia().buscarPropietarioPorCedula(cedula);
            
            // Asignar bonificación con todas las validaciones
            Fachada.getInstancia().asignarBonificacionConValidaciones(propietario, bonificacion, puesto);
            
            // Buscar el propietario actualizado para retornar sus datos (refrescar los datos)
            PropietarioDTO propietarioDto = new PropietarioDTO(propietario);
            
            // Retornar respuesta exitosa con el propietario actualizado
            return Respuesta.lista(
                new Respuesta("exito", "Bonificación asignada correctamente"),
                new Respuesta("propietario", propietarioDto)
            );
        } catch (PeajeException | IllegalArgumentException e) {
            // Retornar error
            return Respuesta.lista(new Respuesta("error", e.getMessage()));
        }
    }
    
    // ==================== Métodos del patrón Observer ====================
    
    /**
     * Desuscribe al controlador del propietario actual.
     */
    private void desuscribirObservadorDePropietario() {
        if (propietarioActual != null) {
            propietarioActual.quitarObservador(this);
            propietarioActual = null;
        }
    }
    
    /**
     * Método del patrón Observer que se ejecuta cuando el propietario notifica un cambio.
     * Envía los datos actualizados del propietario al navegador mediante SSE.
     * 
     * @param evento El tipo de evento que ocurrió
     * @param origen El objeto observable que generó el evento (no usado en esta implementación)
     */
    @Override
    public void actualizar(Object evento, Observable origen) {
        if (propietarioActual != null) {
            // Si hubo un cambio en el propietario, enviar los datos actualizados
            if (Propietario.Eventos.ESTADO_CAMBIADO.equals(evento)
                    || Propietario.Eventos.BONIFICACION_ASIGNADA.equals(evento)
                    || Propietario.Eventos.SALDO_MODIFICADO.equals(evento)) {
                
                // Convertir el propietario a DTO
                PropietarioDTO propietarioDto = new PropietarioDTO(propietarioActual);
                
                // Enviar la actualización al navegador mediante SSE
                conexionNavegador.enviarJSON(Respuesta.lista(
                    new Respuesta("propietario", propietarioDto)
                ));
            }
        }
    }
}
