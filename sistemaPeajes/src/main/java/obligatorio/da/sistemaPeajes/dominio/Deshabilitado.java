package obligatorio.da.sistemaPeajes.dominio;

import excepciones.PeajeException;

/**
 * Clase que representa el estado "Deshabilitado" de un propietario.
 * 
 * El usuario no puede ingresar al sistema ni puede realizar tránsitos.
 * Tampoco se le pueden asignar bonificaciones.
 */
public class Deshabilitado extends EstadoPropietario {
    
    /**
     * Constructor que inicializa el estado como Deshabilitado.
     */
    public Deshabilitado() {
        super("Deshabilitado");
    }
    
    /**
     * El propietario deshabilitado no puede ingresar al sistema.
     * 
     * @return false - acceso denegado al sistema
     */
    @Override
    public boolean puedeIngresar() {
        return false;
    }
    
    /**
     * El propietario deshabilitado no puede realizar tránsitos.
     * 
     * @return false - no puede transitar por los peajes
     */
    @Override
    public boolean puedeTransitar() {
        return false;
    }
    
    /**
     * El propietario deshabilitado no puede recibir asignaciones de bonificaciones.
     * 
     * @return false - no se le pueden asignar bonificaciones
     */
    @Override
    public boolean puedeAsignarBonificacion() {
        return false;
    }
    
    /**
     * El propietario deshabilitado puede recibir notificaciones del sistema.
     * 
     * @return true - aunque esté deshabilitado, recibe notificaciones
     */
    @Override
    public boolean puedeRecibirNotificaciones() {
        return true;
    }
    
    /**
     * Al propietario deshabilitado no se le aplican bonificaciones
     * ya que no puede transitar por los peajes.
     * 
     * @return false - las bonificaciones no se aplican
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
        // Ya está deshabilitado, no hace nada
    }
    
    @Override
    public void suspender(Propietario p) throws PeajeException {
        p.setEstado(new Suspendido());
    }
    
    @Override
    public void penalizar(Propietario p)  throws PeajeException {
        p.setEstado(new Penalizado());
    }
}
