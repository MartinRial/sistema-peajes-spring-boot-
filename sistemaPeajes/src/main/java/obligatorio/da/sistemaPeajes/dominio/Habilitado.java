package obligatorio.da.sistemaPeajes.dominio;

import excepciones.PeajeException;

/**
 * Clase que representa el estado "Habilitado" de un propietario.
 * 
 * Es el estado por defecto de los propietarios cuando se dan de alta en el sistema.
 * El propietario tiene todas las funcionalidades habilitadas.
 */
public class Habilitado extends EstadoPropietario {
    
    /**
     * Constructor que inicializa el estado como Habilitado.
     */
    public Habilitado() {
        super("Habilitado");
    }
    
    /**
     * El propietario habilitado puede ingresar al sistema.
     * 
     * @return true - tiene acceso completo al sistema
     */
    @Override
    public boolean puedeIngresar() {
        return true;
    }
    
    /**
     * El propietario habilitado puede realizar tránsitos por los peajes.
     * 
     * @return true - puede transitar normalmente
     */
    @Override
    public boolean puedeTransitar() {
        return true;
    }
    
    /**
     * El propietario habilitado puede recibir asignaciones de bonificaciones.
     * 
     * @return true - puede recibir bonificaciones
     */
    @Override
    public boolean puedeAsignarBonificacion() {
        return true;
    }
    
    /**
     * El propietario habilitado puede recibir notificaciones del sistema.
     * 
     * @return true - recibe todas las notificaciones
     */
    @Override
    public boolean puedeRecibirNotificaciones() {
        return true;
    }
    
    /**
     * Al propietario habilitado se le aplican las bonificaciones asignadas.
     * 
     * @return true - las bonificaciones se aplican normalmente
     */
    @Override
    public boolean aplicanBonificaciones() {
        return true;
    }
    
    @Override
    public void habilitar(Propietario p)   throws PeajeException{
        // Ya está habilitado, no hace nada
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
    public void penalizar(Propietario p)  throws PeajeException {
        p.setEstado(new Penalizado());
    }
}
