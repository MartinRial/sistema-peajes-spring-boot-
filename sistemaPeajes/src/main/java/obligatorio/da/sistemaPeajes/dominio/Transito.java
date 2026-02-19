package obligatorio.da.sistemaPeajes.dominio;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transito {

    private Puesto puesto;
    private Vehiculo vehiculo;
    private Propietario propietario;
    private AsignacionBonificacion bonificacionAplicada;
    private double montoPagado;
    private Date fechaHora;
    
    public Transito(Puesto puesto, Vehiculo vehiculo, Propietario propietario, 
                    AsignacionBonificacion bonificacionAplicada, double montoPagado, Date fechaHora) {
        this.puesto = puesto;
        this.vehiculo = vehiculo;
        this.propietario = propietario;
        this.bonificacionAplicada = bonificacionAplicada;
        this.montoPagado = montoPagado;
        this.fechaHora = fechaHora;
    }
    
    // ====================================================================
    // PRINCIPIO DE EXPERTO: El Transito es experto en su propia información
    // ====================================================================
    
    /**
     * Obtiene el monto de la tarifa original (antes de aplicar bonificación).
     * 
     * Principio GRASP: Experto de la Información
     * El Transito conoce su vehículo y su puesto, por lo tanto puede delegar
     * al vehículo para que obtenga la tarifa correspondiente en ese puesto.
     * 
     * @return El monto de la tarifa sin bonificación, o 0.0 si no existe tarifa
     */
    public double obtenerMontoTarifa() {
        Tarifa tarifa = vehiculo.obtenerTarifaEn(puesto);
        return (tarifa != null) ? tarifa.getMonto() : 0.0;
    }
    
    /**
     * Calcula el monto descontado por la bonificación aplicada.
     * 
     * Principio GRASP: Experto de la Información
     * El Transito conoce el monto de la tarifa y el monto pagado,
     * por lo tanto puede calcular cuánto se ahorró por la bonificación.
     * 
     * @return El monto de descuento aplicado
     */
    public double obtenerMontoBonificacion() {
        return obtenerMontoTarifa() - montoPagado;
    }
    
    /**
     * Formatea la fecha del tránsito como dd/MM/yyyy.
     * 
     * Principio GRASP: Experto de la Información
     * El Transito conoce su propia fecha/hora, por lo tanto puede formatearla.
     * 
     * @return La fecha formateada
     */
    public String obtenerFechaFormateada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fechaHora);
    }
    
    /**
     * Formatea la hora del tránsito como HH:mm:ss.
     * 
     * Principio GRASP: Experto de la Información
     * El Transito conoce su propia fecha/hora, por lo tanto puede formatearla.
     * 
     * @return La hora formateada
     */
    public String obtenerHoraFormateada() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(fechaHora);
    }
    
    /**
     * Formatea la fecha y hora del tránsito como dd/MM/yyyy HH:mm:ss.
     * 
     * Principio GRASP: Experto de la Información
     * El Transito conoce su propia fecha/hora, por lo tanto puede formatearla.
     * 
     * @return La fecha y hora formateada
     */
    public String obtenerFechaHoraFormateada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return sdf.format(fechaHora);
    }
    
    /**
     * Obtiene el nombre del puesto donde se realizó el tránsito.
     * 
     * Principio GRASP: Experto de la Información
     * El Transito conoce su puesto, por lo tanto puede delegar para obtener el nombre.
     * 
     * @return El nombre del puesto
     */
    public String obtenerNombrePuesto() {
        return puesto.getNombre();
    }
    
    /**
     * Obtiene la matrícula del vehículo que realizó el tránsito.
     * 
     * Principio GRASP: Experto de la Información
     * El Transito conoce su vehículo, por lo tanto puede delegar para obtener la matrícula.
     * 
     * @return La matrícula del vehículo
     */
    public String obtenerMatriculaVehiculo() {
        return vehiculo.getMatricula();
    }
    
    /**
     * Obtiene el nombre de la categoría del vehículo.
     * 
     * Principio GRASP: Experto de la Información
     * El Transito conoce su vehículo, por lo tanto puede delegar para obtener la categoría.
     * 
     * @return El nombre de la categoría
     */
    public String obtenerNombreCategoria() {
        return vehiculo.getCategoria().getNombre();
    }
    
    /**
     * Obtiene el nombre formateado de la bonificación aplicada con su porcentaje.
     * 
     * Principio GRASP: Experto de la Información
     * El Transito conoce su bonificación aplicada, por lo tanto puede formatearla.
     * 
     * @return El nombre de la bonificación formateado, o "Sin bonificación"
     */
    public String obtenerNombreBonificacion() {
        if (bonificacionAplicada != null) {
            String nombre = bonificacionAplicada.getBonificacion().getNombre();
            String porcentaje = bonificacionAplicada.getBonificacion().getPorcentaje();
            return nombre + " (" + porcentaje + ")";
        }
        return "Sin bonificación";
    }
    
    /**
     * Obtiene el nombre completo del propietario con su estado.
     * 
     * Principio GRASP: Experto de la Información
     * El Transito conoce su propietario, por lo tanto puede delegar para obtener la información.
     * 
     * @return El nombre y estado del propietario formateado
     */
    public String obtenerNombrePropietarioConEstado() {
        if (propietario != null) {
            String nombre = propietario.getNombreCompleto();
            String estado = propietario.getNombreEstado();
            return nombre + " (" + estado + ")";
        }
        return "N/A";
    }
    
    /**
     * Obtiene el saldo actual del propietario después del tránsito.
     * 
     * Principio GRASP: Experto de la Información
     * El Transito conoce su propietario, por lo tanto puede delegar para obtener el saldo.
     * 
     * @return El saldo restante del propietario
     */
    public double obtenerSaldoPropietario() {
        if (propietario != null) {
            return propietario.getSaldoActual();
        }
        return 0.0;
    }
}