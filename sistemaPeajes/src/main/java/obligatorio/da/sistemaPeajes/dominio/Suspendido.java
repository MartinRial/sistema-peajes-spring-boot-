package obligatorio.da.sistemaPeajes.dominio;

import excepciones.PeajeException;

/**
 * Clase que representa el estado "Suspendido" de un propietario.
 * 
 * El usuario puede ingresar al sistema, pero no puede realizar tránsitos.
 */
public class Suspendido extends EstadoPropietario {
    
    /**
     * Constructor que inicializa el estado como Suspendido.
     */
    public Suspendido() {
        super("Suspendido");
    }
    
    /**
     * El propietario suspendido puede ingresar al sistema.
     * 
     * @return true - tiene acceso al sistema
     */
    @Override
    public boolean puedeIngresar() {
        return true;
    }
    
    /**
     * El propietario suspendido no puede realizar tránsitos por los peajes.
     * 
     * @return false - tránsitos bloqueados durante la suspensión
     */
    @Override
    public boolean puedeTransitar() {
        return false;
    }
    
    /**
     * El propietario suspendido puede recibir asignaciones de bonificaciones.
     * 
     * @return true - puede recibir bonificaciones para cuando se rehabilite
     */
    @Override
    public boolean puedeAsignarBonificacion() {
        return true;
    }
    
    /**
     * El propietario suspendido puede recibir notificaciones del sistema.
     * 
     * @return true - recibe notificaciones normalmente
     */
    @Override
    public boolean puedeRecibirNotificaciones() {
        return true;
    }
    
    /**
     * Al propietario suspendido se le aplican las bonificaciones asignadas
     * cuando vuelva a estar en condiciones de transitar.
     * 
     * @return true - las bonificaciones están activas para cuando se rehabilite
     */
    @Override
    public boolean aplicanBonificaciones() {
        return true;
    }
    
    @Override
    public void habilitar(Propietario p)  throws PeajeException {
        p.setEstado(new Habilitado());
    }
    
    @Override
    public void deshabilitar(Propietario p)  throws PeajeException {
        p.setEstado(new Deshabilitado());
    }
    
    @Override
    public void suspender(Propietario p) {
        // Ya está suspendido, no hace nada
    }
    
    @Override
    public void penalizar(Propietario p)  throws PeajeException {
        p.setEstado(new Penalizado());
    }
}
