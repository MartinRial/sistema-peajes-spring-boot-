package obligatorio.da.sistemaPeajes.dtos;

import java.text.SimpleDateFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import obligatorio.da.sistemaPeajes.dominio.AsignacionBonificacion;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BonificacionDTO {
    
    private String nombreBonificacion;
    private String nombrePuesto;
    private String fechaAsignada;
    
    public BonificacionDTO(AsignacionBonificacion asignacion) {
        // Obtener nombre de la clase de estrategia
        this.nombreBonificacion = asignacion.getBonificacion().getClass().getSimpleName();
        this.nombrePuesto = asignacion.getPuesto().getNombre();
        this.fechaAsignada = formatearFecha(asignacion.getFechaAsignada());
    }
    
    private String formatearFecha(java.util.Date fecha) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(fecha);
    }
}