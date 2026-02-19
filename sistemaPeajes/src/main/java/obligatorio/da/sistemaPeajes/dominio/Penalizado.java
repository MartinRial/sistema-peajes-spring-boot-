package obligatorio.da.sistemaPeajes.dominio;

import excepciones.PeajeException;

/**
 * Clase que representa el estado "Penalizado" de un propietario.
 * 
 * El usuario puede ingresar al sistema, pero no se le registran notificaciones.
 * Puede realizar tránsitos, pero no aplican las bonificaciones que tenga asignadas.
 */
public class Penalizado extends EstadoPropietario {
    
    /**
     * Constructor que inicializa el estado como Penalizado.
     */
    public Penalizado() {
        super("Penalizado");
    }
    
    /**
     * El propietario penalizado puede ingresar al sistema.
     * 
     * @return true - tiene acceso al sistema
     */
    @Override
    public boolean puedeIngresar() {
        return true;
    }
    
    /**
     * El propietario penalizado puede realizar tránsitos por los peajes.
     * Sin embargo, no se le aplican las bonificaciones que tenga asignadas.
     * 
     * @return true - puede transitar pero sin bonificaciones
     */
    @Override
    public boolean puedeTransitar() {
        return true;
    }
    
    /**
     * El propietario penalizado puede recibir asignaciones de bonificaciones.
     * Aunque las bonificaciones no se aplicarán mientras esté penalizado.
     * 
     * @return true - puede recibir bonificaciones (que se aplicarán al rehabilitarse)
     */
    @Override
    public boolean puedeAsignarBonificacion() {
        return true;
    }
    
    /**
     * El propietario penalizado no recibe notificaciones del sistema.
     * Como parte de la penalización, se bloquean las notificaciones.
     * 
     * @return false - no se le registran notificaciones
     */
    @Override
    public boolean puedeRecibirNotificaciones() {
        return false;
    }
    
    /**
     * Al propietario penalizado no se le aplican las bonificaciones asignadas.
     * Esta es una de las principales consecuencias del estado penalizado.
     * 
     * @return false - las bonificaciones no se aplican durante la penalización
     */
    @Override
    public boolean aplicanBonificaciones() {
        return false;
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
    public void suspender(Propietario p)  throws PeajeException {
        p.setEstado(new Suspendido());
    }
    
    @Override
    public void penalizar(Propietario p) {
        // Ya está penalizado, no hace nada
    }
}
