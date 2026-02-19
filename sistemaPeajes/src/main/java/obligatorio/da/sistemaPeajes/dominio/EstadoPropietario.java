package obligatorio.da.sistemaPeajes.dominio;

import excepciones.PeajeException;
import lombok.Getter;
import lombok.Setter;

/**
 * Clase abstracta que representa el estado de un propietario.
 * Implementa el patrón Strategy/State para gestionar los diferentes estados
 * que puede tener un propietario en el sistema de peajes.
 */
@Getter
@Setter
public abstract class EstadoPropietario {
    protected String nombre;

    /**
     * Constructor que inicializa el estado con un propietario y un nombre específico.
     * 
     * @param nombre El nombre del estado
     */
    public EstadoPropietario(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Determina si el propietario puede ingresar al sistema según su estado actual.
     * 
     * @return true si puede ingresar, false en caso contrario
     */
    public abstract boolean puedeIngresar();
    
    /**
     * Determina si el propietario puede transitar por los peajes.
     * 
     * @return true si puede transitar, false en caso contrario
     */
    public abstract boolean puedeTransitar();
    
    /**
     * Determina si el propietario puede asignar bonificaciones.
     * 
     * @return true si puede asignar bonificaciones, false en caso contrario
     */
    public abstract boolean puedeAsignarBonificacion();
    
    /**
     * Determina si el propietario puede recibir notificaciones.
     * 
     * @return true si puede recibir notificaciones, false en caso contrario
     */
    public abstract boolean puedeRecibirNotificaciones();
    
    /**
     * Determina si al propietario se le aplican las bonificaciones asignadas
     * durante los tránsitos por peajes.
     * 
     * @return true si se aplican bonificaciones, false en caso contrario
     */
    public abstract boolean aplicanBonificaciones();
    
    /**
     * Habilita al propietario cambiando su estado.
     * 
     * @param p El propietario a habilitar
     * @throws PeajeException
     */
    public abstract void habilitar(Propietario p) throws PeajeException;
    
    /**
     * Deshabilita al propietario cambiando su estado.
     * 
     * @param p El propietario a deshabilitar
     * @throws PeajeException
     */
    public abstract void deshabilitar(Propietario p) throws PeajeException;
    
    /**
     * Suspende al propietario cambiando su estado.
     * 
     * @param p El propietario a suspender
     * @throws PeajeException
     */
    public abstract void suspender(Propietario p) throws PeajeException; 
    
    /**
     * Penaliza al propietario cambiando su estado.
     * 
     * @param p El propietario a penalizar
     * @throws PeajeException
     */
    public abstract void penalizar(Propietario p) throws PeajeException;
}