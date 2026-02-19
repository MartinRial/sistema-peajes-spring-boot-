package obligatorio.da.sistemaPeajes.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import obligatorio.da.sistemaPeajes.dominio.AsignacionBonificacion;
import obligatorio.da.sistemaPeajes.dominio.Propietario;

/**
 * DTO para mostrar información básica de un propietario con sus bonificaciones asignadas.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PropietarioDTO {
    private String cedula;
    private String nombreCompleto;
    private String estado;
    private double saldoActual;
    private List<AsignacionBonificacionDTO> bonificaciones;
    
    public PropietarioDTO(Propietario propietario) {
        this.cedula = propietario.getCedula();
        this.nombreCompleto = propietario.getNombreCompleto();
        this.estado = propietario.getEstado().getNombre();
        this.saldoActual = propietario.getSaldoActual();
        
        // Convertir bonificaciones asignadas a DTOs
        this.bonificaciones = new ArrayList<>();
        for (AsignacionBonificacion asignacion : propietario.getBonificaciones()) {
            this.bonificaciones.add(new AsignacionBonificacionDTO(asignacion));
        }
    }
}
