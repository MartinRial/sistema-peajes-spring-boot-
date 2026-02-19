package obligatorio.da.sistemaPeajes.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import obligatorio.da.sistemaPeajes.dominio.Puesto;

/**
 * DTO para transferir información de puestos de peaje.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PuestoDTO {
    
    private String nombre;
    private String direccion;
    
    /**
     * Constructor que crea un DTO a partir de una entidad Puesto.
     * 
     * @param puesto La entidad Puesto de dominio
     */
    public PuestoDTO(Puesto puesto) {
        this.nombre = puesto.getNombre();
        this.direccion = puesto.getDireccion();
    }
}
