package obligatorio.da.sistemaPeajes.dtos;

import java.text.SimpleDateFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import obligatorio.da.sistemaPeajes.dominio.AsignacionBonificacion;

/**
 * DTO para representar una bonificación asignada a un propietario.
 * Información: Nombre de la bonificación - nombre del puesto al que está asignada.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AsignacionBonificacionDTO {
    private String nombreBonificacion;
    private String nombrePuesto;
    private String fechaAsignada;
    
    public AsignacionBonificacionDTO(AsignacionBonificacion asignacion) {
        this.nombreBonificacion = asignacion.getBonificacion().getClass().getSimpleName();
        this.nombrePuesto = asignacion.getPuesto().getNombre();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        this.fechaAsignada = sdf.format(asignacion.getFechaAsignada());
    }
}
