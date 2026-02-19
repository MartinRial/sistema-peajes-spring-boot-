package obligatorio.da.sistemaPeajes.servicios;

import java.util.Date;
import java.util.List;

import excepciones.PeajeException;
import obligatorio.da.sistemaPeajes.dominio.Administrador;
import obligatorio.da.sistemaPeajes.dominio.EstadoPropietario;
import obligatorio.da.sistemaPeajes.dominio.EstrategiaBonificacion;
import obligatorio.da.sistemaPeajes.dominio.Notificacion;
import obligatorio.da.sistemaPeajes.dominio.Propietario;
import obligatorio.da.sistemaPeajes.dominio.Puesto;
import obligatorio.da.sistemaPeajes.dominio.Tarifa;
import obligatorio.da.sistemaPeajes.dominio.Transito;
import obligatorio.da.sistemaPeajes.dominio.Vehiculo;
import observador.Observable;

public class Fachada extends Observable {

    // Definición de eventos para el patrón Observer
    public enum Eventos {
        TRANSITO_REGISTRADO
    }

    private static final Fachada instancia = new Fachada();
    private final ServicioUsuarios su = new ServicioUsuarios();
    private final ServicioNotificaciones sn = new ServicioNotificaciones();
    private final ServicioTransito st = new ServicioTransito();
    private final ServicioBonificaciones sb = new ServicioBonificaciones();
    private final ServicioVehiculos sv = new ServicioVehiculos();


    private Fachada() {
    }


    public static Fachada getInstancia() {
        return instancia;
    }

    public List<EstrategiaBonificacion> getBonificaciones() {
        return sb.getBonificaciones();
    }

    public void asignarBonificacion(Propietario propietario, Puesto puesto, EstrategiaBonificacion bonificacion) {
        sb.asignarBonificacion(propietario, puesto, bonificacion);
    }

    /**
     * Asigna una bonificación a un propietario para un puesto específico con todas las validaciones necesarias.
     *
     * @param propietario El propietario al que se le asignará la bonificación
     * @param bonificacion La bonificación a asignar
     * @param puesto El puesto para el cual aplica la bonificación
     * @throws PeajeException Si hay algún error en las validaciones
     */
    public void asignarBonificacionConValidaciones(Propietario propietario, EstrategiaBonificacion bonificacion, Puesto puesto) throws PeajeException {
        sb.asignarBonificacionConValidaciones(propietario, bonificacion, puesto);
    }

    public Administrador loginAdministrador(String cedula, String contrasena) throws PeajeException {
        return su.loginAdministrador(cedula, contrasena);
    }


    public Administrador logoutAdministrador(Administrador administrador) throws PeajeException {
        return su.logoutAdministrador(administrador);
    }

    public Propietario loginPropietario(String cedula, String contrasena) throws PeajeException {
        return su.loginPropietario(cedula, contrasena);
    }

    public void agregarPropietario(Propietario propietario) throws PeajeException {
        su.agregarPropietario(propietario);
    }

    public void agregarAdministrador(Administrador administrador) throws PeajeException {
        su.agregarAdministrador(administrador);
    }

    public void agregarPuesto(Puesto puesto) {
        st.agregarPuesto(puesto);
    }

    public List<Puesto> getPuestos() {
        return st.getPuestos();
    }

    public List<Tarifa> listarTarifas(Puesto puesto) {
        return st.listarTarifas(puesto);
    }
    
    public List<Transito> obtenerTransitosPropietario(Propietario propietario){
        return st.obtenerTransitosPropietario(propietario);
    }
    
    public List<Notificacion> obtenerNotificacionesDePropietario(Propietario propietario){
        return sn.obtenerNotificaciones(propietario);
    }
    
    /**
     * Borra todas las notificaciones de un propietario.
     * 
     * @param propietario El propietario cuyas notificaciones serán borradas
     * @return true si se borraron notificaciones, false si no había notificaciones
     */
    public boolean borrarNotificaciones(Propietario propietario) {
        return sn.borrarNotificaciones(propietario);
    }
    
    /**
     * Notifica al propietario sobre un tránsito realizado.
     * 
     * @param propietario El propietario a notificar
     * @param puesto El puesto por el que transitó
     * @param vehiculo El vehículo con el que transitó
     * @param fechaHora La fecha y hora del tránsito
     */
    public void notificarTransito(Propietario propietario, Puesto puesto, Vehiculo vehiculo, Date fechaHora) {
        sn.notificarTransito(propietario, puesto, vehiculo, fechaHora);
    }
    
    /**
     * Notifica al propietario que su saldo está bajo.
     * 
     * @param propietario El propietario a notificar
     * @param fechaHora La fecha y hora de la notificación
     */
    public void notificarSaldoBajo(Propietario propietario, Date fechaHora) {
        sn.notificarSaldoBajo(propietario, fechaHora);
    }
    
    /**
     * Notifica al propietario sobre un cambio de estado.
     * Esta notificación siempre se registra, sin importar el estado actual o anterior.
     * 
     * @param propietario El propietario al que se le cambió el estado
     * @param fechaHora La fecha y hora del cambio de estado
     */
    public void notificarCambioEstado(Propietario propietario, Date fechaHora) {
        sn.notificarCambioEstado(propietario, fechaHora);
    }
    
    public int contarTransitosDeVehiculo(Propietario propietario, Vehiculo vehiculo) {
        return st.contarTransitosDeVehiculo(propietario, vehiculo);
    }

    public double totalGastadoPorVehiculo(Propietario propietario, Vehiculo vehiculo) {
        return st.totalGastadoPorVehiculo(propietario, vehiculo);
    }
    

    public Transito registrarTransito(Puesto puesto, Vehiculo vehiculo, Propietario propietario, Date fechaHora) throws PeajeException {
        Transito transito = st.registrarTransito(puesto, vehiculo, propietario, fechaHora);
        return transito;
    }

    public List<Propietario> getPropietarios() {
        return su.getPropietarios();
    }

    public Propietario buscarPropietarioPorCedula(String cedula) throws PeajeException {
        return su.buscarPropietarioPorCedula(cedula);
    }

    /**
     * Obtiene la lista de estados disponibles en el sistema.
     * 
     * @return Lista de estados de propietario disponibles
     */
    public List<EstadoPropietario> getEstadosDisponibles() {
        return su.getEstadosDisponibles();
    }

    /**
     * Cambia el estado de un propietario y registra una notificación.
     * 
     * @param propietario El propietario al que se le cambiará el estado
     * @param nuevoEstado El nuevo estado a asignar
     * @throws PeajeException Si hay algún error en las validaciones
     */
    public void cambiarEstadoPropietario(Propietario propietario, EstadoPropietario nuevoEstado) throws PeajeException {
        // Cambiar el estado del propietario
        su.cambiarEstadoPropietario(propietario, nuevoEstado);
    }
    
    public void agregarVehiculo(Vehiculo vehiculo, Propietario propietario) throws PeajeException {
        sv.agregarVehiculo(vehiculo, propietario);
    }

    public Vehiculo buscarVehiculoPorMatricula(String matricula) throws PeajeException {
        return sv.buscarVehiculoPorMatricula(matricula);
    }

}