package obligatorio.da.sistemaPeajes.controladores;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import obligatorio.da.sistemaPeajes.dominio.AsignacionBonificacion;
import obligatorio.da.sistemaPeajes.dominio.Notificacion;
import obligatorio.da.sistemaPeajes.dominio.Propietario;
import obligatorio.da.sistemaPeajes.dominio.Transito;
import obligatorio.da.sistemaPeajes.dominio.Vehiculo;
import obligatorio.da.sistemaPeajes.dtos.BonificacionDTO;
import obligatorio.da.sistemaPeajes.dtos.NotificacionDTO;
import obligatorio.da.sistemaPeajes.dtos.PropietarioDTO;
import obligatorio.da.sistemaPeajes.dtos.TransitoDTO;
import obligatorio.da.sistemaPeajes.dtos.VehiculoResumenDTO;
import obligatorio.da.sistemaPeajes.servicios.Fachada;
import obligatorio.da.sistemaPeajes.utils.ConexionNavegador;
import obligatorio.da.sistemaPeajes.utils.Respuesta;
import observador.Observable;
import observador.Observador;

/**
 * Controlador REST para el tablero de control del propietario.
 * Implementa el patrón Observer para recibir actualizaciones automáticas.
 * 
 * Caso de Uso: Tablero de control del propietario
 * Curso normal:
 * 1) El sistema muestra:
 * • Nombre completo del propietario
 * • Estado
 * • Saldo actual
 * • Tabla con las bonificaciones asignadas
 * • Tabla de vehículos registrados
 * • Tabla de tránsitos realizados ordenados por fecha/hora descendente
 * • Tabla de notificaciones del sistema ordenados por fecha/hora descendente
 * 2) Opcionalmente el propietario indica que desea borrar las notificaciones
 * recibidas.
 * El sistema borra todas las notificaciones del propietario.
 * 
 * Cursos alternativos:
 * 2) En caso de que el propietario no tenga notificaciones se muestra mensaje
 * "No hay notificaciones para borrar"
 */
@RestController
@RequestMapping("/usuarios/propietarios/tablero")
@Scope("session") // Define el alcance de la sesión para este controlador
public class ControladorTableroPropietario implements Observador {

    private static final String USUARIO_PROPIETARIO_STATE_KEY = "usuarioPropietario";

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

    /**
     * Inicializa la vista del tablero del propietario.
     * 
     * PASO 1 del CU: El sistema muestra toda la información del propietario.
     * 
     * @param propietario El propietario logueado (obtenido de la sesión)
     * @return Lista de respuestas con toda la información del tablero
     */
    @GetMapping("/vistaConectada")
    public List<Respuesta> inicializarVista(
            @SessionAttribute(name = USUARIO_PROPIETARIO_STATE_KEY, required = false) Propietario propietario) {

        // Validación de sesión
        if (propietario == null) {
            return Respuesta.lista(
                    new Respuesta("usuarioNoAutenticado", "login.html"));
        }

        // Validar estado del propietario
        if (!propietario.puedeIngresar()) {
            return Respuesta.lista(
                    new Respuesta("error", "Usuario deshabilitado, no puede ingresar al sistema"),
                    new Respuesta("redirigir", "login.html"));
        }

        // Suscribirse como observador del propietario para recibir actualizaciones
        // automáticas
        suscribirseAPropietario(propietario);

        // Retornar información inicial completa (PASO 1 del CU)
        return Respuesta.lista(
                datosPropietario(propietario),
                bonificacionesAsignadas(propietario),
                vehiculosRegistrados(propietario),
                transitosRealizados(propietario),
                notificaciones(propietario));
    }

    /**
     * Endpoint para cerrar la vista y limpiar los observadores.
     */
    @PostMapping("/vistaCerrada")
    public void salir() {
        desuscribirObservadorDePropietario();
    }

    /**
     * Endpoint para borrar notificaciones.
     * 
     * PASO 2 del CU:
     * Curso normal:
     * 2) Opcionalmente el propietario indica que desea borrar las notificaciones
     * recibidas.
     * El sistema borra todas las notificaciones del propietario.
     * 
     * Cursos alternativos:
     * 2) En caso de que el propietario no tenga notificaciones se muestra mensaje
     * "No hay notificaciones para borrar"
     * 
     * @param propietario El propietario logueado
     * @return Lista de respuestas con el resultado de la operación
     */
    @PostMapping("/borrarNotificaciones")
    public List<Respuesta> borrarNotificaciones(
            @SessionAttribute(name = USUARIO_PROPIETARIO_STATE_KEY, required = false) Propietario propietario) {

        try {
            // Validar sesión
            if (propietario == null) {
                return Respuesta.lista(
                        new Respuesta("error", "Usuario no autenticado"));
            }

            // Intentar borrar notificaciones
            boolean seBorraron = Fachada.getInstancia().borrarNotificaciones(propietario);

            if (!seBorraron) {
                // Curso alternativo: No había notificaciones
                return Respuesta.lista(
                        new Respuesta("info", "No hay notificaciones para borrar"));
            }

            // Curso normal: Se borraron exitosamente
            return Respuesta.lista(
                    new Respuesta("exito", "Notificaciones borradas correctamente"),
                    new Respuesta("notificaciones", new ArrayList<>()) // Lista vacía
            );

        } catch (Exception e) {
            return Respuesta.lista(
                    new Respuesta("error", "Error al borrar notificaciones: " + e.getMessage()));
        }
    }

    // ==========================================================
    // BLOQUES DE DATOS DEL TABLERO (PASO 1 del CU)
    // ==========================================================

    /**
     * PASO 1 del CU: Nombre completo del propietario, Estado, Saldo actual
     */
    private Respuesta datosPropietario(Propietario propietario) {
        PropietarioDTO dto = new PropietarioDTO(propietario);
        return new Respuesta("datosPropietario", dto);
    }

    /**
     * PASO 1 del CU: Tabla con las bonificaciones asignadas
     * Información: Nombre de la bonificación, puesto, fecha de asignada.
     */
    private Respuesta bonificacionesAsignadas(Propietario propietario) {
        List<BonificacionDTO> dtos = new ArrayList<>();
        for (AsignacionBonificacion ab : propietario.getBonificaciones()) {
            dtos.add(new BonificacionDTO(ab));
        }
        return new Respuesta("bonificacionesAsignadas", dtos);
    }

    /**
     * PASO 1 del CU: Tabla de vehículos registrados
     * Información: Número de matrícula, modelo, color, cantidad de tránsitos
     * realizados
     * y monto total gastado en sus tránsitos.
     */
    private Respuesta vehiculosRegistrados(Propietario propietario) {
        List<VehiculoResumenDTO> dtos = new ArrayList<>();
        for (Vehiculo v : propietario.getVehiculos()) {

            int cantidad = Fachada.getInstancia()
                    .contarTransitosDeVehiculo(propietario, v);

            double montoTotal = Fachada.getInstancia()
                    .totalGastadoPorVehiculo(propietario, v);

            dtos.add(new VehiculoResumenDTO(v, cantidad, montoTotal));
        }
        return new Respuesta("vehiculosRegistrados", dtos);
    }

    /**
     * PASO 1 del CU: Tabla de tránsitos realizados ordenados por fecha/hora
     * descendente
     * Información: Nombre del puesto, número de matrícula, categoría, monto de la
     * tarifa,
     * nombre de la bonificación, monto de la bonificación, monto pagado, fecha y
     * hora.
     */
    private Respuesta transitosRealizados(Propietario propietario) {
        List<TransitoDTO> dtos = new ArrayList<>();

        for (Transito t : Fachada.getInstancia().obtenerTransitosPropietario(propietario)) {
            dtos.add(new TransitoDTO(t));
        }
        return new Respuesta("transitosRealizados", dtos);
    }

    /**
     * PASO 1 del CU: Tabla de notificaciones del sistema ordenados por fecha/hora
     * descendente
     * Información: Fecha y hora, mensaje
     */
    private Respuesta notificaciones(Propietario propietario) {
        List<NotificacionDTO> dtos = new ArrayList<>();
        for (Notificacion n : Fachada.getInstancia().obtenerNotificacionesDePropietario(propietario)) {
            dtos.add(new NotificacionDTO(n));
        }
        return new Respuesta("notificaciones", dtos);
    }

    // ==================== Métodos del patrón Observer ====================

    /**
     * Suscribe al controlador como observador del propietario.
     * Desuscribe del propietario anterior si existe.
     */
    private void suscribirseAPropietario(Propietario propietario) {
        // Desuscribirse del propietario anterior si existe
        desuscribirObservadorDePropietario();

        // Suscribirse al nuevo propietario
        propietarioActual = propietario;
        propietario.agregarObservador(this);
        Fachada.getInstancia().agregarObservador(this);
    }

    /**
     * Desuscribe al controlador del propietario actual.
     */
    private void desuscribirObservadorDePropietario() {
        if (propietarioActual != null) {
            propietarioActual.quitarObservador(this);
            propietarioActual = null;
        }
        Fachada.getInstancia().quitarObservador(this);
    }

    /**
     * Método del patrón Observer que se ejecuta cuando el propietario notifica un
     * cambio.
     * Envía las actualizaciones correspondientes al navegador mediante SSE.
     * 
     * Cumple con el requerimiento:
     * "La información de todas las vistas debe actualizarse de manera automática,
     * sin necesidad de que el usuario indique que desea actualizar la información."
     * 
     * @param evento El tipo de evento que ocurrió
     * @param origen El objeto observable que generó el evento (no usado en esta
     *               implementación)
     */
    @Override
    public void actualizar(Object evento, Observable origen) {
        if (propietarioActual != null) {
            // Si hubo un cambio en el propietario, enviar los datos actualizados
            if (Propietario.Eventos.ESTADO_CAMBIADO.equals(evento)
                    || Propietario.Eventos.BONIFICACION_ASIGNADA.equals(evento)
                    || Propietario.Eventos.SALDO_MODIFICADO.equals(evento)
                    || Propietario.Eventos.NOTIFICACIONES_BORRADAS.equals(evento)
                    || Fachada.Eventos.TRANSITO_REGISTRADO.equals(evento)) {

                // Enviar TODA la información actualizada del tablero
                conexionNavegador.enviarJSON(Respuesta.lista(
                        datosPropietario(propietarioActual),
                        bonificacionesAsignadas(propietarioActual),
                        vehiculosRegistrados(propietarioActual),
                        transitosRealizados(propietarioActual),
                        notificaciones(propietarioActual)));
            }
        }
    }
}