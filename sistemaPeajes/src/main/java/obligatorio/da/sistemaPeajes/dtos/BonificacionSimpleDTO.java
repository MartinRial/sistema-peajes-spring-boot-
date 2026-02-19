package obligatorio.da.sistemaPeajes.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import obligatorio.da.sistemaPeajes.dominio.EstrategiaBonificacion;

/**
 * DTO simple para transferir información básica de bonificaciones.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BonificacionSimpleDTO {
    
    private String nombre;
    
    /**
     * Constructor que crea un DTO a partir de una entidad Bonificacion.
     * 
     * @param bonificacion La entidad Bonificacion de dominio
     */
    public BonificacionSimpleDTO(EstrategiaBonificacion bonificacion) {
        this.nombre = bonificacion.getNombre();
    }
}
