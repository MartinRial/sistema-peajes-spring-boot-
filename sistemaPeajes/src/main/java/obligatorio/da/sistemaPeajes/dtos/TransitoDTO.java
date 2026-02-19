package obligatorio.da.sistemaPeajes.dtos;

import lombok.Getter;
import lombok.Setter;
import obligatorio.da.sistemaPeajes.dominio.Transito;

/**
 * DTO para transferir información de tránsitos al frontend.
 * 
 * Aplica el Principio GRASP de Experto:
 * - El DTO no realiza cálculos ni lógica de negocio
 * - Delega toda la responsabilidad al objeto de dominio Transito
 * - El Transito es el experto en su propia información
 */
@Getter
@Setter
public class TransitoDTO {

    // Campos para mostrar en la tabla de tránsitos
    private final String puesto;
    private final String matricula;
    private final String categoria;
    private final double montoTarifa;        // Monto de la tarifa antes de bonificación
    private final String bonificacion;       // Nombre de la bonificación aplicada
    private final double montoBonificacion;  // Monto descontado por la bonificación
    private final double montoPagado;        // Monto final pagado
    private final String fecha;              // Solo la fecha (dd/MM/yyyy)
    private final String hora;               // Solo la hora (HH:mm:ss)
    
    // Campos adicionales (para otros usos)
    private final String fechaHora;          // Fecha y hora completa (dd/MM/yyyy HH:mm:ss)
    private final String propietario;        // "Juan Pérez (Activo)"
    private final double saldoRestante;      // Saldo luego del tránsito

    /**
     * Constructor que crea un DTO a partir de un objeto Transito.
     * 
     * Aplica el Principio de Experto:
     * - Delega todos los cálculos y formateos al objeto Transito
     * - El DTO solo es un contenedor de datos para transferencia
     * - No duplica lógica de negocio
     * 
     * @param t El objeto Transito del dominio
     */
    public TransitoDTO(Transito t) {
        // Delegar al experto (Transito) para obtener toda la información
        this.puesto = t.obtenerNombrePuesto();
        this.matricula = t.obtenerMatriculaVehiculo();
        this.categoria = t.obtenerNombreCategoria();
        this.montoPagado = t.getMontoPagado();
        
        // Delegar cálculos al experto
        this.montoTarifa = t.obtenerMontoTarifa();
        this.montoBonificacion = t.obtenerMontoBonificacion();
        
        // Delegar formateo de fechas al experto
        this.fechaHora = t.obtenerFechaHoraFormateada();
        this.fecha = t.obtenerFechaFormateada();
        this.hora = t.obtenerHoraFormateada();
        
        // Delegar formateo de bonificación al experto
        this.bonificacion = t.obtenerNombreBonificacion();
        
        // Delegar información del propietario al experto
        this.propietario = t.obtenerNombrePropietarioConEstado();
        this.saldoRestante = t.obtenerSaldoPropietario();
    }
}
