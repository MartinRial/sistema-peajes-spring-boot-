package obligatorio.da.sistemaPeajes.servicios;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import obligatorio.da.sistemaPeajes.dominio.Notificacion;
import obligatorio.da.sistemaPeajes.dominio.Propietario;
import obligatorio.da.sistemaPeajes.dominio.Puesto;
import obligatorio.da.sistemaPeajes.dominio.Vehiculo;

public class ServicioNotificaciones {
    
    /**
     * Notifica al propietario sobre un tránsito realizado.
     * 
     * Principio GRASP: Experto de la Información + Controlador
     * ServicioNotificaciones actúa como controlador/coordinador, pero delega
     * al Propietario (experto) la responsabilidad de registrar su propia notificación.
     * 
     * @param propietario El propietario a notificar
     * @param puesto El puesto por el que transitó
     * @param vehiculo El vehículo con el que transitó
     * @param fechaHora La fecha y hora del tránsito
     */
    public void notificarTransito(Propietario propietario, Puesto puesto, Vehiculo vehiculo, Date fechaHora) {
        // GRASP: Propietario gestiona sus propias notificaciones (Experto)
        propietario.notificarTransito(puesto, vehiculo, fechaHora);
    }
    
    /**
     * Notifica al propietario que su saldo está bajo.
     * 
     * Principio GRASP: Experto de la Información + Controlador
     * ServicioNotificaciones actúa como controlador/coordinador, pero delega
     * al Propietario (experto) la responsabilidad de registrar su propia notificación.
     * El Propietario conoce su saldo y puede formatearlo correctamente.
     * 
     * @param propietario El propietario a notificar
     * @param fechaHora La fecha y hora de la notificación
     */
    public void notificarSaldoBajo(Propietario propietario, Date fechaHora) {
        // GRASP: Propietario gestiona sus propias notificaciones (Experto)
        propietario.notificarSaldoBajo(fechaHora);
    }
    
    /**
     * Notifica al propietario sobre un cambio de estado.
     * Esta notificación SIEMPRE se registra, sin importar el estado actual o anterior.
     * 
     * Principio GRASP: Experto de la Información + Controlador
     * ServicioNotificaciones actúa como controlador/coordinador, pero delega
     * al Propietario (experto) la responsabilidad de registrar su propia notificación.
     * 
     * @param propietario El propietario al que se le cambió el estado
     * @param fechaHora La fecha y hora del cambio de estado
     */
    public void notificarCambioEstado(Propietario propietario, Date fechaHora) {
        // GRASP: Propietario gestiona sus propias notificaciones (Experto)
        // Esta notificación siempre se registra, el Propietario maneja esta excepción
        propietario.notificarCambioEstado(fechaHora);
    }
    
    /**
     * Obtiene todas las notificaciones de un propietario.
     */
 public List<Notificacion> obtenerNotificaciones(Propietario propietario) {
    return propietario.getNotificaciones().stream()
            .sorted(Comparator.comparing(Notificacion::getFechaHora).reversed())
            .collect(Collectors.toList());
}
    
    /**
     * Borra todas las notificaciones de un propietario.
     * 
     * Principio GRASP: Experto de la Información + Controlador
     * ServicioNotificaciones actúa como controlador/coordinador, pero delega
     * al Propietario (experto) la responsabilidad de borrar sus propias notificaciones.
     * 
     * @param propietario El propietario cuyas notificaciones serán borradas
     * @return true si se borraron notificaciones, false si no había notificaciones
     */
    public boolean borrarNotificaciones(Propietario propietario) {
        // GRASP: Propietario gestiona sus propias notificaciones (Experto)
        return propietario.borrarNotificaciones();
    }
}
